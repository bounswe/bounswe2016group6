import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.jena.query.ResultSetFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.*;

/**
 * Servlet implementation class EsrefServlet
 */
@WebServlet("/esref_ozdemir")
public class EsrefServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EsrefServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
					"LIMIT 300\n";
		Query query = QueryFactory.create(s1); 
		QueryExecution qExe = QueryExecutionFactory.sparqlService( "https://query.wikidata.org/sparql", query );
		ResultSet results = qExe.execSelect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(outStream, results);
		String json = new String(outStream.toByteArray());
		System.out.println(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
