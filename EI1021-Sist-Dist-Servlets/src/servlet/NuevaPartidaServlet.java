package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class NuevaPartidaServlet
 */
@WebServlet("/NuevaPartidaServlet")
public class NuevaPartidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String direccion = "HundirLaFlotaServlet";
	
	public NuevaPartidaServlet() {
		super();
	}

	/**
	 * Metodo que gestionara el get de este servlet.
	 * 
	 * @param request Objeto de peticion
	 * @param response Objeto de respuesta
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtiene la session
		HttpSession session = request.getSession();
				
		//Invalidamos la sesion para que HundirLaFlotaServlet cree otra partida
		session.invalidate();
		
		response.sendRedirect(direccion);
	}

}
