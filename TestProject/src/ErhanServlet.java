


import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.util.NodeUtils.EqualityTest;

import com.cmpe352group6.util.DBConnectionPool;
import com.cmpe352group6.util.DBManager;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Servlet implementation class ErhanServlet
 * @author Erhan Cagirici
 */
@WebServlet("/erhan_cagirici")
public class ErhanServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	static Gson gson = new Gson();
	private static ArrayList<River> rivers ;
	static River queryRiver;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ErhanServlet() {
		super();
		
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Provides the server-side operations for querying the Wikidata, parsing results,
	 * semantically ranking the results 
	 * returning structured json
	 * saving results to database
	 * 
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//If any query term specified with the request, store 
		String queryTerm = request.getParameter("queryTerm");
		//If any request type is specified , store
		String requestType = "" + request.getParameter("type");
		System.out.println("Hello!");
        
		//If user wants to save data to db
        if(requestType.equals("saveData")){
        	//Retrieve a connection
        	Connection con = DBConnectionPool.getConnection();
        	//Initialize a db manager for database connection
        	DBManager dbManager = new DBManager(con);
    		
        	//Retrieve datalist parameter of request
        	String dataList = request.getParameter("dataList");
        	//Convert the data into ordinary Java array
    		String[] dataForSave = gson.fromJson(dataList,String[].class);
    		System.out.println(dataForSave[0]);
    		int success = 0;
    		//For every data try to add it to the database
    		for (int i = 0; i < dataForSave.length; i++) {
    			
    			String dataUrl = dataForSave[i];
    			
    			try {
    				//Run db function
    				int result = dbManager.addDataUrl(dataUrl);
    				if (result == 1) {
    					success++; //update number of succeeds 
    				}
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		//Return the operaion result by informng the number of successive operations.
    		response.getWriter().print("Saving completed." + success +"/" + dataForSave.length + " entries saved");
        }
        
        //If the queryTerm is null user is redirected into homepage
        else if(queryTerm == null){
			System.out.println("Welcome Page");
			request.getRequestDispatcher("/WEB-INF/erhanHome.jsp").forward(request, response);
		} else {
			//otherwise it means user has sent a search query
			try {
				//SPARQL Query Strings which retrieves the rivers in the world, with coordinates and length
				String queryString = "PREFIX wd: <http://www.wikidata.org/entity/>\nPREFIX wdt: <http://www.wikidata.org/prop/direct/>\nPREFIX wikibase: <http://wikiba.se/ontology#>\nPREFIX p: <http://www.wikidata.org/prop/>\nPREFIX ps: <http://www.wikidata.org/prop/statement/>\nPREFIX pq: <http://www.wikidata.org/prop/qualifier/>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX bd: <http://www.bigdata.com/rdf#>\n\nSELECT ?river ?riverLabel ?length ?location WHERE {\n  ?river wdt:P31/wdt:P279* wd:Q4022 .\n  ?river wdt:P2043 ?length .\n  ?river wdt:P625 ?location\n  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" . }\n} ORDER BY DESC(?length) ?itemLabel\nLIMIT 300";
				
				//Using jena execute the query and obtain the results
				Query q = QueryFactory.create(queryString);
				String service = "https://query.wikidata.org/sparql";
				QueryExecution qExec = QueryExecutionFactory.sparqlService(service, q);
				org.apache.jena.query.ResultSet rs = qExec.execSelect();
				
				//Parse the results into rivers arraylist
				resultToArrayList(rs);
				//Find the river that the user has searched
				int ix = rivers.indexOf(new River(queryTerm,0,0,0));
				
				if(ix>-1){
					//If found, obtain the river and set it as reference while calculating the 
					//distance between the rivers
					queryRiver = rivers.get(ix);
					River.referenceRiver = queryRiver;
					
					System.out.println("Found the river : " + queryRiver.name );
					
					//Sort the results according to their distance to the user's query.
					Collections.sort(rivers);
					
				} else {
					//If the user's input can't be found in the database,
					//No distance sort is applied, the data is preseneted
					// From longest river in descending length (implemented in SPARQL query)
					System.out.println("Can't find the river");
				}
				
				//Convert the java array into json string
				String json  = arrayTojson();
				//Send the json as a response to the user request
				response.getWriter().print(json);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * Convert resultset into standard Java ArrayList with River objects.
	 * @param rs The SPARQL result set
	 */
	public void resultToArrayList(org.apache.jena.query.ResultSet rs){
		rivers = new ArrayList<River>();
		while(rs.hasNext()){
			QuerySolution qs = rs.next();
			String riverName = qs.get("riverLabel").toString();
			String point = qs.get("location").toString();
			int length = qs.get("length").asLiteral().getInt();
			
			riverName = riverName.split("@")[0];
			double longtitude = Double.parseDouble(point.substring(point.indexOf('(')+1, point.indexOf(' ')));
			double latitude = Double.parseDouble(point.substring(point.indexOf(' ')+1, point.indexOf(')')));
			
			rivers.add(new River(riverName,latitude,longtitude,length));
		}
	}
	/**
	 * Convert the rivers array into a json String.
	 */
	public static String arrayTojson() throws IOException, ClassNotFoundException{
		StringWriter outStringWriter = new StringWriter();

		JsonWriter writer = new JsonWriter(outStringWriter);
		writer.beginArray();
		for(int i = 0; i<rivers.size() ; i++) {

			writer.beginObject();
			
				writer.name("riverLabel");
				Class<?> type = Class.forName("java.lang.String");
				gson.toJson(rivers.get(i).name, type, writer);
				
				writer.name("Latitude");
				gson.toJson(""+rivers.get(i).latitude, type, writer);
				
				writer.name("Longtitude");
				gson.toJson(""+rivers.get(i).longtitude, type, writer);
				
				
				writer.name("Length");
				gson.toJson(""+rivers.get(i).length, type, writer);
			writer.endObject();
		}
		writer.endArray();
		outStringWriter.close();
		return outStringWriter.toString();
		// out.print("}");
	}
}
