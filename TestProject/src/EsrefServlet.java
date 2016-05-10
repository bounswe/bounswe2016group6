import java.io.IOException;
import com.cmpe352group6.util.DataParser;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
 * Servlet implementation class EsrefServlet
 */
@WebServlet("/esref_ozdemir")
public class EsrefServlet extends HttpServlet {
	
	/** Returns the distance between two points.
	 * 
	 * @param x1 First x coordinate.
	 * @param y1 First y coordinate.
	 * @param x2 Second x coordinate.
	 * @param y2 Second y coordinate.
	 * @return Distance between two points.
	 */
	public double distance(double x1, double y1, double x2, double y2) {
		return (y2 - y1)*(y2 - y1) + (x2 - x1)*(x2 - x1);
	}
	
	/** NationalPark class representing a national park with name, country, and corodinates.
	 */
	public class NationalPark {
		public String name;
		public String country;
		public double longtitude;
		public double latitude;

		/** Default NationalPark constructor.
		 * 
		 */
		NationalPark() {
			this.name = "EMPTY";
			this.country = "EMPTY";
			this.longtitude = 0;
			this.latitude = 0;
		}
		
		/** 4 parameter NationalPark constructor.
		 * 
		 * @param name Name of the national park.
		 * @param country Country of the national park.
		 * @param longtitude Longtitude of the national park.
		 * @param latitude Latitude of the national park.
		 */
		NationalPark(String name, String country, double longtitude, double latitude) {
			this.name = name;
			this.country = country;
			this.longtitude = longtitude;
			this.latitude = latitude;		
		}
		
		/** Constructs a national park from a given data format row. Row is expected to be of the
		 * following format.
		 * 
		 * 
		 * @param row Of the following format Name||Country||Longtitude||Latitude
		 */
		NationalPark(String row) {
			String[] cols = row.split("\\|\\|");
			this.name = cols[0];
			this.country = cols[1];
			this.longtitude = Double.parseDouble(cols[2]);
			this.latitude = Double.parseDouble(cols[3]);
		}
	}

	public static final long serialVersionUID = 1L;
	public static ArrayList<NationalPark> parks = null;
	public static String url = "jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db";

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
	public void parseData(ResultSet results) {
		String[] headers = {"?objectLabel", "?countryLabel", "?coord"};
		String data = DataParser.jenaToData(results, new ArrayList<String>(Arrays.asList(headers)));
		int index = 0;
		int prev_index = 0;
		while ((index = data.indexOf("&&", index+2)) != -1) {
			String row = data.substring(prev_index, index);
			parks.add(new NationalPark(row));
			prev_index = index+2;
		}
	}

	/** Computes the number of steps it will take to get to the second string from the first one
	 * by computing the levenshtein distance between the two strings.
	 * 
	 * @param lhs Starting string
	 * @param rhs Target string
	 * @return Number of steps it will take to get to the second string from the first one.
	 * 
	 * Source: https://en.wikipedia.org/wiki/Levenshtein_distance
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
	
	/** Sorts the given NationalPark list with respect to the given NationalPark.
	 * The list will be sorted according to the distance of each element to NationalPark np.
	 * 
	 * @param list List of NationalParks to be sorted.
	 * @param np NationalPark object according to which the list will be sorted.
	 */
	public void sortByPark(List<NationalPark> list, NationalPark np) {
		Collections.sort(list, new Comparator<NationalPark>() {
			public int compare(NationalPark np1, NationalPark np2) {
				double d1 = distance(np.longtitude, np.latitude, np1.longtitude, np1.latitude);
				double d2 = distance(np.longtitude, np.latitude, np2.longtitude, np2.latitude);
				return (int)(d1 - d2);
			}
		});
	}

	/** Semantically reorders the parks ArrayList according to the given input.
	 *  Given input must be the name of a national park. If no such national park exists, then
	 *  this algorithm doesn't do any reordering.
	 *  
	 * @param term Name of a national park.
	 */
	public void semanticRanking(String term) {
		ArrayList<NationalPark> rankedList = new ArrayList<NationalPark>();
		int minSteps = 10000; //min steps to convert "term" into a national park name
		NationalPark inputPark = null;
		for(NationalPark np : parks){
			int current = levenshteinDistance(term, np.name);
			if(current < minSteps){
				minSteps = current;
				inputPark = np;
			}
		}
		ArrayList<NationalPark> orderedParks = new ArrayList<NationalPark>();
		int lastIndex = 0; //last index of the national parks with the same country
		for (NationalPark np : parks) {
			if (np.country.equals(inputPark.country)) {
				orderedParks.add(0, np);
				++lastIndex;
			} else {
				orderedParks.add(np);
			}
		}
		//sort the parks up till lastIndex with respect to distance
		sortByPark(orderedParks.subList(0,  lastIndex), inputPark);
		//sort the parks from lastIndex up till the end of the list with respect to distance
		sortByPark(orderedParks.subList(lastIndex + 1,  orderedParks.size() - 1), inputPark);
		parks = orderedParks;
	}
	
	/** Returns the selected NationalPark objects in an ArrayList; which objects are selected is
	 * specified in input filter string.
	 * 
	 * @param filter Filter string specifying which parks to return. Has a format of "1 3 12 25"
	 * @return ArrayList of selected NationalPark objects.
	 */
	public ArrayList<NationalPark> getSelectedParks(String filter) {
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
	 * ROW&&ROW&&ROW&&...&&ROW
	 * 
	 * Each ROW is formatted as follows:
	 * COLUMN||COLUMN||...||COLUMN
	 * 
	 * @param request HTTPServletRequest object containing the request parameters input and type.
	 * @return Resulting data in an internal data format.
	 */
	public String queryData(HttpServletRequest request) {
		StringBuilder data = new StringBuilder("Name||Country||Longtitude||Latitude&&");
		if (parks != null) {
			this.semanticRanking(request.getParameter("input"));
			for(NationalPark np : parks) {
				data.append(np.name + "||" + np.country + "||" + np.longtitude + "||" + np.latitude + "&&");
			}
			return data.toString();
		}
		parks = new ArrayList<NationalPark>();
		String s1 =
				"PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
						"PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
						"PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
						"PREFIX wd: <http://www.wikidata.org/entity/>\n" +
						"select ?objectLabel ?countryLabel ?coord\n" +
						"where {\n" +
						"	?object wdt:P31 wd:Q46169 . \n" + //object is a national park
						"	?object wdt:P625 ?coord  .\n" + //get the coordinates
						"	?object wdt:P17 ?country .\n" + //get the country
						"	?country wdt:P31 wd:Q185441 .\n" + //country should be in european union
						"	SERVICE wikibase:label {\n" +
						"		bd:serviceParam wikibase:language \"en\" .\n" +
						"	}\n" +
						"}";
		Query query = QueryFactory.create(s1); 
		QueryExecution qExe = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
		ResultSet results = qExe.execSelect();
		this.parseData(results);
		this.semanticRanking(request.getParameter("input"));
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
	public String insertData(HttpServletRequest request) {
		String filter = request.getParameter("input");
		if (filter.equals("undefined")) {
			return "0";
		}
		ArrayList<NationalPark> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
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
	public String deleteData(HttpServletRequest request) {
		String filter = request.getParameter("input");
		if (filter.equals("undefined")) {
			return "0";
		}
		ArrayList<NationalPark> checkedNPs = getSelectedParks(filter);
		java.sql.Connection connection;
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
	public String listData(HttpServletRequest request) {
		java.sql.Connection connection;
		StringBuilder data = new StringBuilder("Name||Country||Longtitude||Latitude&&");
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
			request.getRequestDispatcher("/WEB-INF/esrefHome.jsp").forward(request, response);
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