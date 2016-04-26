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
		response.getWriter().append("<table border=\"1\" style=\"width:100%\">  <tr>" +
									"<td><a href=\"ahmet_zorer\">Ahmet Zorer</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/Ahmet-Zorer\"> git page</a></td> </tr>  <tr>" +
									"<td><a href=\"esra_alinca\">Esra Alinca</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/Esra-Al%C4%B1nca\"> git page</a></td> </tr>  <tr>" +
									"<td><a href=\"muaz_ekici\">Muaz Ekici</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/Muaz-Ekici\"> git page</a></td> </tr>  <tr>" +
									"<td><a href=\"esref_ozdemir\">Esref Ozdemir</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/E%C5%9Fref-%C3%96zdemir\"> git page</a></td> </tr>  <tr>" +
									"<td><a href=\"ali_can_erkilic\">Ali Can Erkilic</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/Ali-Can-Erkilic\"> git page</a></td> </tr>  <tr>" +
									"<td><a href=\"erhan_cagirici\">Erhan Cagirici</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/Erhan-%C3%87a%C4%9F%C4%B1r%C4%B1c%C4%B1\"> git page</a></td> </tr>  <tr>" +
									"<td><a href=\"azmi_ozgen\">Azmi Ozgen</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/Azmi-%C3%96zgen\"> git page</a></td> </tr>  <tr>" +
									"<td><a href=\"utku_alhan\">Utku Alhan</a></td><td><a href=\"https://github.com/bounswe/bounswe2016group6/wiki/Utku-Alhan\"> git page</a></td> </tr>" +
									"</table>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
