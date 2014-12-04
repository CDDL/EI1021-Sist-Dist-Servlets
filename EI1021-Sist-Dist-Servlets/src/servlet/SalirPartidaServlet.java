package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SalirPartidaServlet
 */
@WebServlet("/SalirPartidaServlet")
public class SalirPartidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SalirPartidaServlet() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtiene la session
		HttpSession session = request.getSession();
	
		// Invalidamos la session
		session.invalidate();
		
		response.sendRedirect("index.html");
			
	}

}
