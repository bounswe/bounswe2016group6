import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;

import com.cmpe352group6.util.DataParser;
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

		public University(String name, String date, String lon,String lat) {
			this.name = name;
			this.date = Integer.parseInt(date.substring(0,4));
			
			this.longtitude = Double.parseDouble(lon);
			this.latitude = Double.parseDouble(lat);		
		}
	}

	private static final long serialVersionUID = 1L;
	private static ArrayList<University> uni = null;
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
		
		uni = new ArrayList<University>();
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

		String[] headers = {"?universityLabel", "?date", "?coord"};
		String data = DataParser.jenaToData(results, new ArrayList<String>(Arrays.asList(headers)));
		int index = 0;
		int prev_index = 0;
		while ((index = data.indexOf("&&", index+2)) != -1) {
			String row = data.substring(prev_index, index);
			String[] column = row.split("\\|\\|");
			uni.add(new University(column[0], column[1], column[2],column[3]));
			prev_index = index+2;
		}
		data = "Name||Date||Longtitude||Latitude&&"+data;
		
		//Semantic sort
		for(int i =0;i<uni.size();i++){
			
			University univ = uni.get(i);
			if(univ.name.equals(request.getParameter("input"))){
				Collections.sort(uni, new Comparator<University>() {
					public int compare(University uni1, University uni2) {
						double d1 = distance(univ.longtitude, univ.latitude, uni1.longtitude, uni1.latitude);
						double d2 = distance(univ.longtitude, univ.latitude, uni2.longtitude, uni2.latitude);
						return Double.compare(d1, d2);
					}
				});
				break;
			}
		}

		
		return data;
	}
	
	protected double distance(double longtitude, double latitude, double longtitude2, double latitude2) {
		return (longtitude-longtitude2)*(longtitude-longtitude2)+(latitude-latitude2)*(latitude-latitude2);
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
			universityList.add(uni.get(Integer.parseInt(idStr)));
		}
		java.sql.Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			StringBuilder sqlStmt = new StringBuilder("INSERT INTO db.univ VALUES");
			for (int i = 0; i < universityList.size() - 1; ++i) {
				University uni = universityList.get(i);
				sqlStmt.append("(\""); 
				sqlStmt.append(uni.name); 
				sqlStmt.append("\", \""); 
				sqlStmt.append(uni.date); 
				sqlStmt.append("\", "); 
				sqlStmt.append(uni.longtitude); 
				sqlStmt.append(", "); 
				sqlStmt.append(uni.latitude); 
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
		String[] idStrings = filter.split(" ");
		ArrayList<University> results = new ArrayList<University>();
		for (String idStr : idStrings) {
			results.add(uni.get(Integer.parseInt(idStr)));
		}
		java.sql.Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection (url,"root","pembePanter");
			java.sql.Statement stmt = connection.createStatement();
			for (University uni : results) {
				String sqlStmt = "DELETE FROM db.univ WHERE Name = \"" + uni.name + "\";";
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
			String sqlStmt = "SELECT * FROM db.univ;";
			java.sql.ResultSet res = stmt.executeQuery(sqlStmt);
			while (res.next()) {
				data.append(res.getString("NAME"));
				data.append("||");
				data.append(res.getString("DATE"));
				data.append("||");
				data.append(res.getDouble("LON"));
				data.append("||");
				data.append(res.getDouble("LAT"));
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
	 * executes corresponding processes and returns the resulting data.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getParameter("type");
		if (requestType == null) {
			request.getRequestDispatcher("/WEB-INF/Home.jsp").forward(request, response);;
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