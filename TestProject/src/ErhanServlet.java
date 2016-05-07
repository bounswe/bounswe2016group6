


import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmpe352group6.util.DBConnectionPool;
import com.cmpe352group6.util.DBManager;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.sql.*;
/**
 * Servlet implementation class ErhanServlet
 * @author Erhan Cagirici
 */
@WebServlet("/erhan_cagirici")
public class ErhanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Gson gson = new Gson();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ErhanServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryTerm = request.getParameter("queryTerm");
		System.out.println("Hello!");


		if(queryTerm == null){
			System.out.println("Welcome Page");
			request.getRequestDispatcher("/WEB-INF/erhanHome.jsp").forward(request, response);
		} else {
			System.out.println("SQL starts");
			try {
				Connection con = DBConnectionPool.getConnection();
				System.out.println("DB manager");
				DBManager dbManager = new DBManager(con);
				ResultSet rs = dbManager.callSQLFunc();
				System.out.println("Really");
				
				String json = jsonWrite(rs);
				System.out.println(json);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(queryTerm);
			response.getWriter().println(queryTerm + " SEARCHED!");
		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public static String jsonWrite(ResultSet rs)

			throws IOException {
		try {
			StringWriter outStringWriter = new StringWriter();

			JsonWriter writer = new JsonWriter(outStringWriter);

			ResultSetMetaData meta = rs.getMetaData();
			int cc = meta.getColumnCount();
			// out.print("{\"JSONData\":");
			writer.beginArray();

			while (rs.next()) {
				writer.beginObject();
				for (int i = 1; i <= cc; ++i) {
					writer.name(meta.getColumnName(i));
					Class<?> type = Class.forName(meta.getColumnClassName(i));
					gson.toJson(rs.getObject(i), type, writer);
					// writer.value(rs.getString(i));
				}
				writer.endObject();
			}

			writer.endArray();
			outStringWriter.close();
			return outStringWriter.toString();
			// out.print("}");
		} catch (SQLException e) {
			throw new RuntimeException(e.getClass().getName(), e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getClass().getName(), e);
		}
	}
}
