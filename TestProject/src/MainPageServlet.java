import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainPageServlet
 */
@WebServlet("/")
public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");	
		response.getWriter().append("<ul>" +
									"<li><a href=\"ahmet_zorer\">Ahmet Zorer</a></li>" +
									"<li><a href=\"esra_alinca\">Esra Alinca</a></li>" +
									"<li><a href=\"muaz_ekici\">Muaz Ekici</a></li>" +
									"<li><a href=\"esref_ozdemir\">Esref Ozdemir</a></li>" +
									"<li><a href=\"ali_can_erkilic\">Ali Can Erkilic</a></li>" +
									"<li><a href=\"erhan_cagirici\">Erhan Cagirici</a></li>" +
									"<li><a href=\"azmi_ozgen\">Azmi Ozgen</a></li>" +
									"<li><a href=\"utku_alhan\">Utku Alhan</a></li>" +
									"</ul>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
