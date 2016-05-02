

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AhmetServlet
 */
@WebServlet("/ahmet_zorer")
public class AhmetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AhmetServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String query = "SELECT ?item ?itemLabel ?pic WHERE { ?item ?x1 wd:Q146 . OPTIONAL { ?item wdt:P18 ?pic } SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" } }";
        String website = "https://query.wikidata.org/sparql?query=";
		try {
			String queryResult = getHTML("https://query.wikidata.org/sparql?query=SELECT%20?h%20WHERE{?h%20wdt:P31%20wd:Q3624078}");
			out.println(queryResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getHTML(String urlToRead) throws Exception {
		 URL oracle = new URL(urlToRead);
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(oracle.openStream()));

	        String sum,inputLine;
	        while ((inputLine = in.readLine()) != null)
	            System.out.println(inputLine);
	        in.close();
	      return inputLine;
	   }

}
