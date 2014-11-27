package tests;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletDeBienvenida
 */
@WebServlet("/ServletDeBienvenida")
public class ServletDeBienvenida extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDeBienvenida() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String nombre = request.getParameter("nombre").toString();
		String apellidos = request.getParameter("apellidos").toString();
		
		out.print("<html>"
				+ "<head>"
				+ "<title>Servlet ServletDeBienvenida</title>"
				+ "</head>"
				+ "<body>"
				+ "<h2>Pagina de bienvenida en " + request.getContextPath() + "<h2>"
				+ "<p>Bienvenido " + nombre + " " + apellidos + "</p>"
				+ "</body>"
				+ "</html"
				);
		out.close();
	}

}
