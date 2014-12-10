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

	/**
	 * Metodo que gestionara el get de este servlet.
	 * 
	 * @param request Objeto de peticion
	 * @param response Objeto de respuesta
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//Se extren los datos de la session.
		Partida partida = extraerDatosPartida(req);
		
		//Se pone el tipo de contenido de la respuesta y se empieza a construir el mensaje de respuesta.
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		inicializarCodigoHTML(out);
		
		crearCabecera(out);
		crearEstados(out, partida);
		crearTablero(out, partida);
		crearLinks(out);
		
		finalizarCodigoHTML(out);
		
		
	}




	/**
	 * Metodo que inicializa el codigo.
	 * 
	 * @param out Objeto de respuesta para escribir.
	 */
	private void inicializarCodigoHTML(PrintWriter out) {
		out.print("<!DOCTYPE html>"+
				  "<html>");
	}
	
	/**
	 * Metodo que escribe la cabecera.
	 * 
	 * @param out Objeto de respuesta para escribir.
	 */
	private void crearCabecera(PrintWriter out) {
		out.print("<head>"+
				  "<meta charset=\"ISO-8859-1\">" +
				  "<title>Hundir la flota</title>"+
				  "</head>");
	}
	
	/**
	 * Metodo que escribe el texto de estado.
	 * 
	 * @param out Objeto de respuesta para escribir.
	 * @param partida Objeto de la partida para sacar datos.
	 */
	private void crearEstados(PrintWriter out, Partida partida) {
		out.print("<body>"
				+ "<h1>Hundir la Flota</h1>"
				+ "<br><br>"
				+ "Solucion PARTIDA<br>"
				+ "Barcos Navegando: "+partida.getBarcosQuedan()+" Barcos Hundidos: "+partida.getBarcosHundidos()+"<br>"
				+ "Numero de disparos efectuados: "+partida.getDisparosEfectuados()+"<br>");
	}

	/**
	 * Metodo que crea el tablero de soluciones.
	 * 
	 * @param out Objeto de respuesta para escribir.
	 * @param partida Objeto de la partida para sacar datos.
	 */
	private void crearTablero(PrintWriter out, Partida partida) {
		out.print("<table width=\"20%\">");
		out.print("<tr>");
		crearLetras(out);
		out.print("</tr>");
		
		//Se crea una matriz que representa la tabla para facilitar el pintar las casillas.
		int[][] matrizSolucionesAux = crearMatrizSoluciones(partida.getSolucion());
		for(int filas = 0; filas<8;filas++){
			out.print("<tr>");
			crearFila(out,matrizSolucionesAux[filas],filas+1);
			out.print("</tr>");
		}
		
		out.print("</table>");
		out.print("<br>");
	}

	/**
	 * Metodo que dibuja una fila de la matriz.
	 * 
	 * @param out Objeto de respuesta para escribir.
	 * @param fila Lista de elementos de la fila.
	 * @param filaActual Numero de la fila con la cual se esta trabajando.
	 */
	private void crearFila(PrintWriter out, int[] fila,int filaActual) {
		out.print("<td><b>"+ filaActual+"</b></td>");
		
		//estadoPosicion es una lista que representa la fila con AZUL o ROJO, dependiendo si hay agua o barco.
		for(int estadoPosicion: fila){
			if(estadoPosicion == AZUL){
				out.print("<td style=\"background-color:blue\"></td>");
			} else {
				out.print("<td style=\"background-color:red\"></td>");
			}
		}
		
	}



	/**
	 * Metodo que crea una matriz de enteros para el facil procesamiento de la solucion de la partida.
	 * 
	 * @param solucion Posicion de los barcos de la solucion.
	 */
	private int[][] crearMatrizSoluciones(String[] solucion) {
		
		//Se crea un mar vacio ...
		int[][] mar = crearMarVacio();
		
		//... y los barcos se pintan encima.
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




	/**
	 * Metodo que genera una matriz de enteros 8x8 que contiene solo agua.
	 * 
	 */
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




	/**
	 * Metodo que marca como rojo las posiciones donde se encuentre un barco.
	 * 
	 * @param mar Matriz llena de agua.
	 * @param fila Fila donde empieza el barco.
	 * @param col Columna donde empieza el barco.
	 * @param orientacion Caracter que indica si el barco esta en vertical o horizontal.
	 * @param tamanyo Entero que indica la longitud del barco.
	 */
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



	/**
	 * Metodo que crea las letras encima del tablero
	 * 
	 * @param out Objeto de respuesta para escribir.
	 */
	private void crearLetras(PrintWriter out) {
		out.print("<td></td>");
		for (char letra = 'A'; letra <= 'H'; letra++) {
			out.print("<th><b>"+letra+"</b></th>");
		}
	}



	/**
	 * Metodo que escribe el apartado de links de la pagina.
	 * 
	 * @param out Objeto de respuesta para escribir.
	 */
	private void crearLinks(PrintWriter out) {
		out.print("<a href=\"NuevaPartidaServlet\">Nueva partida</a><br>"
				+ "<a href=\"SalirPartidaServlet\">Salir</a><br>");
	}
	
	/**
	 * Metodo que finaliza el codigo HTML.
	 * 
	 * @param out Objeto de respuesta para escribir.
	 */
	private void finalizarCodigoHTML(PrintWriter out) {
		out.print("</body></html>");
	}
	
	
	/**
	 * Metodo que extrae de la sesion la partida y la devuelve.
	 * 
	 * @param req Objeto de solicitud.
	 */
	private Partida extraerDatosPartida(HttpServletRequest req) {
		return (Partida) req.getSession().getAttribute("partida");
	}
}
