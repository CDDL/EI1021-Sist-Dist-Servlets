package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.Partida;

public class SolucionPartidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int ROJO = 1;
	private static final int AZUL = 0;

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
		out.print("<table width=\"20%\">");
		out.print("<tr>");
		crearLetras(out);
		out.print("</tr>");
		int[][] matrizSolucionesAux = crearMatrizSoluciones(partida.getSolucion());
		for(int filas = 0; filas<8;filas++){
			out.print("<tr>");
			crearFila(out,matrizSolucionesAux[filas],filas+1);
			out.print("</tr>");
		}
		out.print("</table>");
		out.print("<br>");
	}

	private void crearFila(PrintWriter out, int[] fila,int filaActual) {
		out.print("<td><b>"+ filaActual+"</b></td>");
		for(int estadoPosicion: fila){
			if(estadoPosicion == AZUL){
				out.print("<td style=\"background-color:blue\"></td>");
			} else {
				out.print("<td style=\"background-color:red\"></td>");
			}
		}
		
	}





	private int[][] crearMatrizSoluciones(String[] solucion) {
		int[][] mar = crearMarVacio();
		for (String barco: solucion) {
	        String[] splittedString = barco.split("#"); // Las caracteristicas del barco partido
	        int fila = Integer.parseInt(splittedString[0]);
	        int col = Integer.parseInt(splittedString[1]);
	        String orientacion = splittedString[2]; // "V" o "H"
	        int tamanyo = Integer.parseInt(splittedString[3]);
	        
	        pintarBarco(mar,fila,col,orientacion,tamanyo);
		}
		return mar;
	}





	private int[][] crearMarVacio() {
		int numFilas = 8;
		int numColumnas = 8;
		
		int[][] mar = new int[numFilas][numColumnas];
		for(int fil=0;fil<numFilas;fil++){
			for(int col=0;col<numColumnas;col++){
				mar[fil][col] = AZUL;
			}
		}
		return mar;
	}





	private void pintarBarco(int[][] mar, int fila, int col,
			String orientacion, int tamanyo) {
        if (orientacion.equals("V")) {
            for (int i = 0; i < tamanyo; i++) {
                mar[fila+i][col] = ROJO;
            }
        } else {
            for (int i = 0; i < tamanyo; i++) {
                mar[fila][col+i] = ROJO;
            }
        }
	}





	private void crearLetras(PrintWriter out) {
		out.print("<td></td>");
		for (char letra = 'A'; letra <= 'H'; letra++) {
			out.print("<th><b>"+letra+"</b></th>");
		}
	}





	private void crearLinks(PrintWriter out) {
		out.print("<a href=\"NuevaPartidaServlet\">Nueva partida</a><br>"
				+ "<a href=\"SalirPartidaServlet\">Salir</a><br>");
	}
	
	private void finalizarCodigoHTML(PrintWriter out) {
		out.print("</body></html>");
	}
	
	private Partida extraerDatosPartida(HttpServletRequest req) {
		return (Partida) req.getSession().getAttribute("partida");
//		return new Partida();
	}
}
