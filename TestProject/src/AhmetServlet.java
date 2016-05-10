import java.awt.Point;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
/**
 * Servlet implementation class AhmetServlet
 */
@WebServlet("/ahmet_zorer")
public class AhmetServlet extends HttpServlet {
	public class University {
		String name;
		int date;
		double longtitude;
		double latitude;

		public University(String name, String date, String coord) {
			this.name = name;
			this.date = Integer.parseInt(date);
			
			this.longtitude = Double.parseDouble(coord.substring(0,coord.indexOf(" ")));
			this.latitude = Double.parseDouble(coord.substring(coord.indexOf(" ")+1));		
		}
	}

	private static final long serialVersionUID = 1L;
	private static ArrayList<University> parks = null;
	private static String url = "jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AhmetServlet() {
		super();
	}
	
	/** Computes the number of steps it will take to get to the second string from the first one
	 * by computing the levenshtein distance between the two strings.
	 * 
	 * @param lhs Starting string
	 * @param rhs Target string
	 * @return Number of steps it will take to get to the second string from the first one.
	 */
	public static int levenshteinDistance(CharSequence lhs, CharSequence rhs) {      
		int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];        
		for (int i = 0; i <= lhs.length(); i++) {
			distance[i][0] = i;
		}
		for (int j = 1; j <= rhs.length(); j++) {
			distance[0][j] = j;
		}
		for (int i = 1; i <= lhs.length(); i++) {
			for (int j = 1; j <= rhs.length(); j++) {
				int first = Math.min(distance[i-1][j]+1, distance[i][j-1]+1);
				int second = distance[i-1][j-1] + ((lhs.charAt(i-1) == rhs.charAt(j-1)) ? 0 : 1);
				distance[i][j] = Math.min(first, second);
			}
		}
		return distance[lhs.length()][rhs.length()];                           
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
		parks = new ArrayList<University>();
		String queryString ="PREFIX wikibase: <http://wikiba.se/ontology#>"
				+ "PREFIX bd: <http://www.bigdata.com/rdf#>"
				+ "PREFIX wd: <http://www.wikidata.org/entity/>"
				+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>"
				+ "select ?universityLabel ?universityDescription ?date ?coord WHERE {"
				+ "	?university wdt:P31/wdt:P279* wd:Q3918 ;"
				+ "		wdt:P17 wd:Q43 ;"
				+ "		wdt:P625 ?coord;"
				+ "        wdt:P571 ?date."
				+ "	OPTIONAL {"
				+ "		?university wdt:P856 ?website"
				+ "	}"
				+ "	SERVICE wikibase:label {"
				+ "		bd:serviceParam wikibase:language \"en, tr\" ."
				+ "	}"
				+ "}";
		Query query = QueryFactory.create(queryString); 
		QueryExecution qExe = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
		ResultSet results = qExe.execSelect();

		while (results.hasNext()) {
			QuerySolution next = results.nextSolution();
			String name = next.get("?universityLabel").toString();
			String date = next.get("?date").toString().substring(0,4);
			String coord = next.get("?coord").toString();
			if (name.contains("@")) {
				name = name.substring(0, name.indexOf('@'));
			}
			coord = coord.substring(6, coord.indexOf(")"));
			System.out.println(name+", "+date+", "+coord);
			parks.add(new University(name, date, coord));

		}
		
		
		
		//TODO
		
		
		/*this.parseData(results);
		this.semanticRanking(request.getParameter("input"));
		StringBuilder data = new StringBuilder("Name||Country||Longtitude||Latitude&&");
		for(NationalPark np : parks) {
			data.append(np.name + "||" + np.country + "||" + np.longtitude + "||" + np.latitude + "&&");
		}*/
		return "";
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
		String[] idStrings = filter.split(" ");
		ArrayList<University> universityList = new ArrayList<University>();
		for (String idStr : idStrings) {
			universityList.add(parks.get(Integer.parseInt(idStr)));
		}
		java.sql.Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			StringBuilder sqlStmt = new StringBuilder("INSERT INTO db.University VALUES");
			for (int i = 0; i < universityList.size() - 1; ++i) {
				University np = universityList.get(i);
				sqlStmt.append("(\""); 
				sqlStmt.append(np.name); 
				sqlStmt.append("\", \""); 
				sqlStmt.append(np.date); 
				sqlStmt.append("\", "); 
				sqlStmt.append(np.longtitude); 
				sqlStmt.append(", "); 
				sqlStmt.append(np.latitude); 
				sqlStmt.append("), ");
			}
			University last = universityList.get(universityList.size() - 1);
			sqlStmt.append("(\""); 
			sqlStmt.append(last.name); 
			sqlStmt.append("\", \""); 
			sqlStmt.append(last.date); 
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
	

	
	private String deleteData(HttpServletRequest request) {
		String filter = request.getParameter("input");
		if (filter.equals("undefined")) {
			return "0";
		}
		ArrayList<University> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			for (University np : checkedNPs) {
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
			request.getRequestDispatcher("/WEB-INF/ahmetMain.jsp").forward(request, response);;
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