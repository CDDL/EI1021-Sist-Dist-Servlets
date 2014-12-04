package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logica.Partida;

public class SolucionPartidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Partida partida = extraerDatosPartida(req);
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		inicializarCodigoHTML(out);
		
		crearCabecera(out);
		crearEstados(out, partida);
		crearTablero(out, partida);
		crearLinks(out);
		
		finalizarCodigoHTML(out);
		
		
		super.doGet(req, resp);
	}






	private void crearLinks(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}






	private void inicializarCodigoHTML(PrintWriter out) {
		out.print("<!DOCTYPE html>"+
				  "<html>");
	}
	
	private void crearCabecera(PrintWriter out) {
		out.print("<head>"+
				  "<meta charset=\"ISO-8859-1\">" +
				  "<title>Hundir la flota</title>"+
				  "</head>");
	}
	
	private void crearEstados(PrintWriter out, Partida partida) {
		out.print("<body>"
				+ "<h1>Hundir la Flota</h1>"
				+ "<br><br>"
				+ "Solucion PARTIDA<br>"
				+ "Barcos Navegando: "+partida.getBarcosQuedan()+" Barcos Hundidos: "+partida.getBarcosHundidos()+"<br>"
				+ "Numero de disparos efectuados: "+partida.getDisparosEfectuados()+"<br>");
	}

	private void crearTablero(PrintWriter out, Partida partida) {
		crearLetras(out);
		int[][] matrizSolucionesAux = crearMatrizSoluciones(partida.getSolucion());
		for(int filas = 0; filas<8;filas++){
			crearFila(out,matrizSolucionesAux[filas]);
		}
		out.print("<br>");
	}
	
	private Partida extraerDatosPartida(HttpServletRequest req) {
		return (Partida) req.getSession().getAttribute("partida");
	}
}
