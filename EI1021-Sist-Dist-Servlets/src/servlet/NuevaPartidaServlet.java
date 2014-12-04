package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logica.Partida;

/**
 * Servlet implementation class NuevaPartidaServlet
 */
@WebServlet("/NuevaPartidaServlet")
public class NuevaPartidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String direccion = "NuevaPartidaServlet";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtiene la session
		HttpSession session = request.getSession();
		
		// Invalidamos la session
		session.invalidate();
		
		// Creamos una nueva session
		session.setAttribute("partida", new Partida());
		
		response.sendRedirect(direccion);
	}

}
