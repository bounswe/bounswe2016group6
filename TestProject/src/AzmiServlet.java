import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

/**
 * Servlet implementation class for AzmiServlet
 */
@WebServlet("/azmi_ozgen")
public class AzmiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String jsonData = null;

    public AzmiServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String requestType = request.getParameter("type");

		 if (requestType == null) {
			 request.getRequestDispatcher("/WEB-INF/azmiMain.jsp").forward(request, response);;
		 }
		 else if (requestType.equals("queryData")) {
			 String inputTerm = request.getParameter("input");
			 String s1 = "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
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
			 QueryExecution qExe = QueryExecutionFactory.sparqlService( "https://query.wikidata.org/sparql", query );
			 ResultSet results = qExe.execSelect();
			 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			 ResultSetFormatter.outputAsJSON(outStream, results);
			 jsonData = new String(outStream.toByteArray());
			 response.getWriter().write(jsonData);
		 }
		 else if (requestType.equals("selectData")) {
					 System.out.println("selectData");
					 String input = request.getParameter("input");
					 System.out.println(input);
					 if (jsonData == null) {
					 }

			  //db insert action
			 if (true/*success*/) {
				 response.getWriter().write(1);
			 }
			 else {
				 response.getWriter().write(0);
			 }
		 }
		 else if (requestType.equals("listData")) {
			 	System.out.println("listData");
			 	String sqlData = "SQLDATA"; //all the data in mySQL server
			 	response.getWriter().write(sqlData);
		 }
		 else {
			 System.out.println("Unexpected request type: " + requestType);
		 }
	 }

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 this.doGet(request, response);
	 }
 }
