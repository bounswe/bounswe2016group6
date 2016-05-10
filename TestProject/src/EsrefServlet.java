import java.io.ByteArrayOutputStream;
import org.apache.jena.atlas.json.*;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
/**
 * Servlet implementation class EsrefServlet
 */
@WebServlet("/esref_ozdemir")
public class EsrefServlet extends HttpServlet {

	public class NationalPark {
		public String name;
		public String country;
		public double longtitude;
		public double latitude;

		NationalPark() {
			this.name = "EMPTY";
			this.country = "EMPTY";
			this.longtitude = 0;
			this.latitude = 0;
		}
		
		NationalPark(String name, String country, double longtitude, double latitude) {
			this.name = name;
			this.country = country;
			this.longtitude = longtitude;
			this.latitude = latitude;		
		}
	}

	private static final long serialVersionUID = 1L;
	private static ArrayList<NationalPark> parks = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EsrefServlet() {
		super();
	}
	
	/** Constructs a NationalPark object from each result in the ResultSet and adds them to parks ArrayList.
	 *  After the execution of this method, all the data obtained from the SPARQL query is stored
	 *  in the parks ArrayList.
	 * @param results Jena ResultSet object, containing the result of SPARQL query.
	 */
	private void parseData(ResultSet results) {
		this.parks = new ArrayList<NationalPark>();
		while (results.hasNext()) {
			QuerySolution next = results.nextSolution();
			String name = next.get("?objectLabel").toString();
			String country = next.get("?countryLabel").toString();
			String point = next.get("?coord").toString();
			name = name.substring(0, name.indexOf('@'));
			country = country.substring(0, country.indexOf('@'));
			double longtitude = Double.parseDouble(point.substring(point.indexOf('(')+1, point.indexOf(' ')));
			double latitude = Double.parseDouble(point.substring(point.indexOf(' ')+1, point.indexOf(')')));
			parks.add(new NationalPark(name, country, longtitude, latitude));
		}
	}
	
	/** Finds the first NationalPark object with name field equal to the given name parameter and
	 * returns it.
	 * 
	 * @param name Name of a NationalPark object.
	 * @return NationalPark object with name field equal to given name parameter. If no such NationalPark exists, then returns null.
	 */
	private NationalPark find(String name) {
		for (NationalPark np : parks) {
			if (np.name == name) {
				return np;
			}
		}
		return null;
	}
	
	/** Semantically reorders the parks ArrayList according to the given input.
	 * 
	 * @param term Name of a national park.
	 */
	private void semanticRanking(String term) {
		ArrayList<NationalPark> rankedList = new ArrayList<NationalPark>();
		NationalPark inputPark = this.find(term);
		if (inputPark != null) {
			//TODO: implement semantic ranking algorithm
			this.parks = rankedList;
		} //if inputPark == null, then no such park exists. Do nothing.
	}
	
	/** Returns the selected NationalPark objects in an ArrayList; which objects are selected is
	 * specified in input filter string.
	 * 
	 * @param filter Filter string specifying which parks to return. Has a format of "1 3 12 25"
	 * @return ArrayList of selected NationalPark objects.
	 */
	private ArrayList<NationalPark> getSelectedParks(String filter) {
		String[] idStrings = filter.split(" ");
		ArrayList<NationalPark> result = new ArrayList<NationalPark>();
		for (String idStr : idStrings) {
			result.add(parks.get(Integer.parseInt(idStr)));
		}
		return result;
	}
	
	/** Queries data from wikidata and returns the resulting data as a String in an internal data format.
	 * 
	 * Internal data format is as follows:
	 * <ROW>&&<ROW>&&<ROW>&&...&&<ROW>
	 * 
	 * Each <ROW> is formatted as follows:
	 * <COLUMN>||<COLUMN>||...||<COLUMN>
	 * 
	 * @param request HTTPServletRequest object containing the request parameters input and type.
	 * @return Resulting data in an internal data format.
	 */
	private String queryData(HttpServletRequest request) {
		parks = new ArrayList<NationalPark>();
		String s1 = 
				"PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
				"PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
				"PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
				"PREFIX wd: <http://www.wikidata.org/entity/>\n" +
				"select ?objectLabel ?countryLabel ?coord\n" +
				"where {\n" +
				"?object wdt:P31 wd:Q46169 . \n" +
				"?object wdt:P625 ?coord .\n" +
				"?object wdt:P17 ?country .\n" +
				"SERVICE wikibase:label {\n" +
				"bd:serviceParam wikibase:language \"en\" .\n" +
				"}\n" +
				"}" +
				"LIMIT 100\n";
		Query query = QueryFactory.create(s1); 
		QueryExecution qExe = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
		ResultSet results = qExe.execSelect();
		this.parseData(results);
		//this.semanticRanking(request.getParameter("input"));
		StringBuilder data = new StringBuilder("Name||Country||Longtitude||Latitude&&");
		for(NationalPark np : parks) {
			data.append(np.name + "||" + np.country + "||" + np.longtitude + "||" + np.latitude + "&&");
		}
		return data.toString();
	}
	
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
		ArrayList<NationalPark> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
		String url = "jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			StringBuilder sqlStmt = new StringBuilder("INSERT INTO db.NationalPark VALUES");
			for (int i = 0; i < checkedNPs.size() - 1; ++i) {
				NationalPark np = checkedNPs.get(i);
				sqlStmt.append("(\""); 
				sqlStmt.append(np.name); 
				sqlStmt.append("\", \""); 
				sqlStmt.append(np.country); 
				sqlStmt.append("\", "); 
				sqlStmt.append(np.longtitude); 
				sqlStmt.append(", "); 
				sqlStmt.append(np.latitude); 
				sqlStmt.append("), ");
			}
			NationalPark last = checkedNPs.get(checkedNPs.size() - 1);
			sqlStmt.append("(\""); 
			sqlStmt.append(last.name); 
			sqlStmt.append("\", \""); 
			sqlStmt.append(last.country); 
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
		ArrayList<NationalPark> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
		String url = "jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			for (NationalPark np : checkedNPs) {
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
		String url = "jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db";
		StringBuilder data = new StringBuilder("");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			String sqlStmt = "SELECT * FROM db.NationalPark;";
			java.sql.ResultSet res = stmt.executeQuery(sqlStmt);
			//TODO: convert sql.ResultSet to our internal data format
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
			request.getRequestDispatcher("/WEB-INF/esrefHome.jsp").forward(request, response);;
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