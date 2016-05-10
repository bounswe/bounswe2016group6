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
	public class CITY {
		String name;
		double longtitude;
		double latitude;
		double pop;
		
		CITY(String name, double longtitude, double latitude, double pop) {
			this.name = name;
			this.longtitude = longtitude;
			this.latitude = latitude;	
			this.pop = pop;
		}
	}

	private static final long serialVersionUID = 1L;
	private static ArrayList<CITY> parks = null;
	private static String url = "jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EsraServlet() {
		super();
	}
	
	/** Constructs a CITY object from each result in the ResultSet and adds them to parks ArrayList.
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
			parks.add(new CITY(column[0],Double.parseDouble(column[1]),Double.parseDouble(column[2]),Double.parseDouble(column[3])));
			prev_index = index+2;
		}
		return ("Name||Longtitude||Latitude||Population&&"+data);
	}
	
	private ArrayList<CITY> getSelectedParks(String filter) {
		String[] idStrings = filter.split(" ");
		ArrayList<CITY> result = new ArrayList<CITY>();
		for (String idStr : idStrings) {
			result.add(parks.get(Integer.parseInt(idStr)));
		}
		return result;
	}
	
	private String queryData(HttpServletRequest request) {
		StringBuilder data = new StringBuilder("Name||Longtitude||Latitude||Population&&");
		if (parks != null) {
			for(CITY np : parks) {
				data.append(np.name + "||" + np.longtitude + "||" + np.latitude + "||" + np.pop + "&&");
			}
			return data.toString();
		}
		parks = new ArrayList<CITY>();
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
		
	private String insertData(HttpServletRequest request) {
		String filter = request.getParameter("input");
		if (filter.equals("undefined")) {
			return "0";
		}
		ArrayList<CITY> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			StringBuilder sqlStmt = new StringBuilder("INSERT INTO db.CITY VALUES");
			for (int i = 0; i < checkedNPs.size() - 1; ++i) {
				CITY np = checkedNPs.get(i);
				sqlStmt.append("(\""); 
				sqlStmt.append(np.name); 
				sqlStmt.append("\", \""); 
				sqlStmt.append(np.longtitude); 
				sqlStmt.append("\", "); 
				sqlStmt.append(np.latitude); 
				sqlStmt.append(", "); 
				sqlStmt.append(np.pop); 
				sqlStmt.append("), ");
			}
			CITY last = checkedNPs.get(checkedNPs.size() - 1);
			sqlStmt.append("(\""); 
			sqlStmt.append(last.name); 
			sqlStmt.append("\", \""); 
			sqlStmt.append(last.longtitude); 
			sqlStmt.append("\", "); 
			sqlStmt.append(last.latitude); 
			sqlStmt.append(", "); 
			sqlStmt.append(last.pop); 
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getParameter("type");
		if (requestType == null) {
			request.getRequestDispatcher("/WEB-INF/Home.jsp").forward(request, response);;
		} else if (requestType.equals("queryData")) {
			response.getWriter().write(queryData(request));
		} else if (requestType.equals("insertData")) {
			response.getWriter().write(insertData(request));
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