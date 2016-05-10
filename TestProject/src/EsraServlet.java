import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import com.cmpe352group6.util.DataParser;


/**Servlet implementation class EsraServlet*/
@WebServlet("/esra_alinca")
public class EsraServlet extends HttpServlet {
	public class City {
		String name;
		double longtitude;
		double latitude;
		double pop;
		
		City(String name, double longtitude, double latitude, double pop) {
			this.name = name;
			this.longtitude = longtitude;
			this.latitude = latitude;	
			this.pop = pop;
		}
	}

	private static final long serialVersionUID = 1L;
	private static ArrayList<City> parks = null;
	private static String url = "jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EsraServlet() {
		super();
	}
	
	/** Constructs a City object from each result in the ResultSet and adds them to parks ArrayList.
	 *  After the execution of this method, all the data obtained from the SPARQL query is stored
	 *  in the parks ArrayList.
	 * @param results Jena ResultSet object, containing the result of SPARQL query.
	 */
	
	private String parseData(ResultSet results) {
		String[] headers = {"?itemLabel", "?coord", "?pop"};
		String data = DataParser.jenaToData(results, new ArrayList<String>(Arrays.asList(headers)));
		int index = 0;
		int prev_index = 0;
		while ((index = data.indexOf("&&", index+2)) != -1) {
			String row = data.substring(prev_index, index);
			String[] column = row.split("\\|\\|");
			parks.add(new City(column[0],Double.parseDouble(column[1]),Double.parseDouble(column[2]),Double.parseDouble(column[3])));
			prev_index = index+2;
		}
		return data;
	}
	
	
	private ArrayList<City> getSelectedParks(String filter) {
		String[] idStrings = filter.split(" ");
		ArrayList<City> result = new ArrayList<City>();
		for (String idStr : idStrings) {
			result.add(parks.get(Integer.parseInt(idStr)));
		}
		return result;
	}
	
	/** Queries data from wikidata and returns the resulting data as a String in an internal data format.
	 * 
	 * Internal data format is as follows:
	 * ROW&&ROW&&ROW&&...&&ROW
	 * 
	 * Each ROW is formatted as follows:
	 * COLUMN||COLUMN||...||COLUMN
	 * 
	 * @param request HTTPServletRequest object containing the request parameters input and type.
	 * @return Resulting data in an internal data format.
	 */
	private String queryData(HttpServletRequest request) {
		parks = new ArrayList<City>();
		String s1 =
				"PREFIX bd: <http://www.bigdata.com/rdf#>"
						+ "PREFIX wikibase: <http://wikiba.se/ontology#>"
						+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>"
						+ "	PREFIX wd: <http://www.wikidata.org/entity/>"
						+ "SELECT ?item ?itemLabel ?coord ?pop "
						+ "WHERE"
						+ "{"
						+ "	?item wdt:P31 wd:Q515 ."
						+ "  	?item wdt:P625 ?coord."
						+ "  	?item wdt:P1082 ?pop."
						+ "	SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }"
						+ "}"
						+ "limit 1000";
		
		Query query = QueryFactory.create(s1); 
		QueryExecution qExe = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
		ResultSet results = qExe.execSelect();

		return parseData(results);
	}
		
		/*this.parseData(results);
		this.semanticRanking(request.getParameter("input"));
		StringBuilder data = new StringBuilder("Name||Country||Longtitude||Latitude&&");
		for(NationalPark np : parks) {
			data.append(np.name + "||" + np.country + "||" + np.longtitude + "||" + np.latitude + "&&");
		}
		return data.toString();*/
	
	
	/** Inserts the records in the specified indices to MySQL server.
	 * Which records are going to be inserted is specified in "input" parameter of GET call.
	 * "input" parameter is a space separated string with each entry specifying the index of
	 * the record to be inserted.
	 * 
	 * @param request HTTPServletRequest object containing the request parameters input and type.
	 * @return Resulting data as a String.
	 */
	private String insertData(HttpServletRequest request) {
		String filter = request.getParameter("input");
		if (filter.equals("undefined")) {
			return "0";
		}
		ArrayList<City> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			StringBuilder sqlStmt = new StringBuilder("INSERT INTO db.NationalPark VALUES");
			for (int i = 0; i < checkedNPs.size() - 1; ++i) {
				City np = checkedNPs.get(i);
				sqlStmt.append("(\""); 
				sqlStmt.append(np.name); 
				sqlStmt.append("\", \""); 
				//sqlStmt.append(np.country); 
				sqlStmt.append("\", "); 
				sqlStmt.append(np.longtitude); 
				sqlStmt.append(", "); 
				sqlStmt.append(np.latitude); 
				sqlStmt.append("), ");
			}
			City last = checkedNPs.get(checkedNPs.size() - 1);
			sqlStmt.append("(\""); 
			sqlStmt.append(last.name); 
			sqlStmt.append("\", \""); 
			//sqlStmt.append(last.country); 
			sqlStmt.append("\", "); 
			sqlStmt.append(last.longtitude); 
			sqlStmt.append(", "); 
			sqlStmt.append(last.latitude); 
			sqlStmt.append(");");
			stmt.executeUpdate(sqlStmt.toString());
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "0";
		}
		return "1";
	}
	
	/** Deletes the records in the specified indices from MySQL server.
	 * Which records are going to be deleted is specified in "input" parameter of GET call.
	 * "input" parameter is a space separated string with each entry specifying the index of
	 * the record to be inserted.
	 * 
	 * @param request HTTPServletRequest object containing the request parameters input and type.
	 * @return Resulting data as a String.
	 */
	private String deleteData(HttpServletRequest request) {
		String filter = request.getParameter("input");
		if (filter.equals("undefined")) {
			return "0";
		}
		ArrayList<City> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			for (City np : checkedNPs) {
				String sqlStmt = "DELETE FROM db.NationalPark WHERE Name = \"" + np.name + "\";";
				stmt.executeUpdate(sqlStmt);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "0";
		}			
		return "1";
	}
	
	/** Gets all the records stored in MySQL server and returns them as a String.
	 * 
	 * @param request HTTPServletRequest object contaning the request parameters input and type.
	 * @return Resulting data as a String.
	 */
	private String listData(HttpServletRequest request) {
		java.sql.Connection connection;
		StringBuilder data = new StringBuilder("");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			String sqlStmt = "SELECT * FROM db.NationalPark;";
			java.sql.ResultSet res = stmt.executeQuery(sqlStmt);
			while (res.next()) {
				data.append(res.getString("Name"));
				data.append("||");
				data.append(res.getString("Country"));
				data.append("||");
				data.append(res.getDouble("Longtitude"));
				data.append("||");
				data.append(res.getDouble("Latitude"));
				data.append("&&");
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "0";
		}			
		return data.toString();
	}

	/** doGet method responding to GET calls.
	 * According to different request types, executes corresponding processes and returns the resulting data.
	 * 
	 * Accepts two parameters from the GET method:
	 * 1. type : Request type. May be
	 * 	i.   queryData
	 *  ii.  insertData
	 *  iii. listData
	 * 2. input : Input data of a request. Each request type requires different input:
	 * queryData  : A search term such as name of a national park.
	 * insertData : List of checkbox id's as a string.
	 * listData   : No input
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getParameter("type");
		if (requestType == null) {
			request.getRequestDispatcher("/WEB-INF/esra.jsp").forward(request, response);;
		} else if (requestType.equals("queryData")) {
			response.getWriter().write(queryData(request));
		} else if (requestType.equals("insertData")) {
			response.getWriter().write(insertData(request));
		} else if (requestType.equals("deleteData")) {
			response.getWriter().write(deleteData(request));
		} else if (requestType.equals("listData")) {
			response.getWriter().write(listData(request));
		} else {
			System.out.println("Unexpected request type: " + requestType);
		}
	}

	/** doPost method responding to POST calls.
	 * Currently not used.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}