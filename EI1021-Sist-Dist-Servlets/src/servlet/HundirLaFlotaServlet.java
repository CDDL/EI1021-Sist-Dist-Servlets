package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logica.Partida;

public class HundirLaFlotaServlet extends HttpServlet {
	
	private static final int NUMFILAS = 8, NUMCOL = 8, NUMBARCOS = 6;
	private static final int NUEVA = 0, FINAL = 1, PROCESO = 2, SINDATO = -1 ; 
	private static final int AGUA = -1, TOCADO = -2, HUNDIDO = -3;
	
	private static final long serialVersionUID = 1L;
	Partida partida;
	int fila, columna, estadoDisparo;
	int estadoPartida;
	PrintWriter pr;
	
	/**
	 * Constructor por defecto
	 */
	public HundirLaFlotaServlet() {
		super();
	}

	/**
	 * Método que ejecutará una petición con el METHOD GET
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		recuperarDatos(request);
		guardarSession(request);
		// Definir el tipo de contenido
		response.setContentType("text/html;charset=UTF-8");
		pr = response.getWriter();
		
		escribirCabecera();
		estadoPartida();
		dibujarTablero();
		crearLinks();
		escribierPie();
		
	}

	/**
	 * Método para recuperar los datos de la session, si hay, y se ejecuta la acción en partida
	 * @param request La peticion recibida del Formulario
	 */
	private void recuperarDatos(HttpServletRequest request) {
		// Recuperar el objeto sesion
		HttpSession session = request.getSession(true);
		
		partida = (Partida) session.getAttribute("partida");
		// Si es null, se crea una partida nueva
		if (partida == null) {
			partida = new Partida(NUMFILAS,NUMCOL, NUMBARCOS);
			estadoPartida = NUEVA;
		}
		// Hay una partida iniciada y continua en ella
		else { 
			// Aun quedan barcos por destruir, el juego sigue
			if (partida.getBarcosQuedan() > 0) {
				estadoPartida = PROCESO;
				String botonDisparo = (String) request.getParameter("disparo");
				// Condición por si se hace un submit sin un boton seleccionado
				if (botonDisparo == null)
					estadoPartida = SINDATO;
				else {
					String[] casillasDisparo = botonDisparo.split("x");
					fila = Integer.valueOf(casillasDisparo[0]);
					columna = Integer.valueOf(casillasDisparo[1]);
					estadoDisparo = partida.pruebaCasilla(fila, columna);
					if (partida.getBarcosQuedan() == 0)
						estadoPartida = FINAL;
				}
			}
			// No quedan barcos por destruir, el juego se ha acabado
			else
				estadoPartida = FINAL;
		}
		
	}
	
	/**
	 * Método para guardar la partida y definir el tiempo de inactividad en infinito
	 * @param request La petición recibida del formulario
	 */
	private void guardarSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		// Guardar la sesion, con la acción realizada
				session.setMaxInactiveInterval(-1);
				session.setAttribute("partida", partida);
	}
	
	/**
	 * Método para escribir la cabecera HTML y el principio del cuerpo
	 */
	private void escribirCabecera() {
		//Cabecera pagina
		pr.println("<html>"
				+ "<head><title>Hundir la flota</title></head>");
		
		//Cuerpo pagina
		pr.println("<body>"
				+ "<h1>Hundir la flota</h1> <br>");
	}

	/**
	 * Método para escribir el código HTML de la información de la partida
	 */
	private void estadoPartida() {
		switch (estadoPartida) {
			case NUEVA:
				pr.println("<b> Nueva partida</b> <br>"
						+ "Barcos navegando: " + NUMBARCOS + " - Barcos hundidos: 0 <br>"
						+ "Número de disparos efectuados: 0 <br>");
				break;
			case PROCESO:
				pr.println("Página de resultado del disparo en (" + fila + ", " + ((char) (columna + 65)) + "): Ok!<br>"
						+ "Barcos navegando: " + partida.getBarcosQuedan() + " - Barcos hundidos:" + partida.getBarcosHundidos() + "<br>"
								+ "Número de disparos efectuados: " + partida.getDisparosEfectuados() + "<br>");
				break;
			case FINAL:
				pr.println("<b>GAME OVER</b>");
				break;
			case SINDATO:
				pr.println("Sin disparo efectuado <br>"
						+ "Barcos navegando: " + NUMBARCOS + " - Barcos hundidos: 0 <br>"
						+ "Número de disparos efectuados: 0 <br>");
		}
			
	}
	
	/**
	 * Método para escribir el código HTML para crear el tablero de juego y dibujar la partida en proceso
	 */
	private void dibujarTablero() {
		pr.print("<form action=\"HundirLaFlotaServlet\" method=\"GET\">");
		pr.println("<table style=\"width:400\">");
		pr.println("<tr>");
		crearLetras();
		pr.println("</tr>");
		for (int i = 0; i < 8; i++) {
			pr.println("<tr>");
			crearFila(i);
			pr.println("</tr>");
		}
		pr.println("<tr>"
				+ "<td align=\"center\" colspan=\"9\"><input type=\"submit\" value=\"submit\"></td>"
				+ "</tr>");
		pr.println("<br>"
				+ "</table>"
				+ "</form>");
	}
	
	/**
	 * Método para dibujar la parte superior del tablero que corresponde a la letras
	 */
	private void crearLetras() {
		pr.print("<td></td>");
		for (char letra = 'A'; letra <= 'H'; letra++) {
			pr.print("<td align=\"center\"><b>"+letra+"</b></td>");
		}
	}

	/**
	 * Método para dibujar la fila del tablero numFila
	 * @param numFila número de la fila a dibujar.
	 */
	private void crearFila(int numFila) {
		pr.println("<td align=\"center\"><b>" + (numFila+1) + "</b></td>");
		for (int col = 0; col < 8; col++) {
			crearColumnas(numFila, col);
		}
	}

	/**
	 * Método para dibujar y pintar la columna numCol de la fila numFila
	 * @param numFila fila en la que esta la columna
	 * @param numCol en que columna estas
	 */
	private void crearColumnas(int numFila, int numCol) {
		pr.print("<td align=\"center\" ");
		boolean isDisparado = partida.getEstadoDisparo(numFila, numCol);
		if (isDisparado)  {
			int estadoDisp = partida.getCasilla(numFila, numCol);
			switch (estadoDisp) {
			case AGUA:
				pr.print("style=\"background-color:cyan\"");
				break;
			case TOCADO:
				pr.print("style=\"background-color:orange\"");
				break;
			case HUNDIDO:
				pr.print("style=\"background-color:red\"");
			}
			}
		pr.print("><input type=\"radio\" name=\"disparo\" value=\"" + numFila + "x" + numCol + "\"></td>");
	}
	
	/**
	 * Método para escribir el código HTML para los enlaces
	 */
	private void crearLinks() {
		pr.println("<a href=\"SolucionPartidaServlet\">Muestra solución</a><br>"
				+ "<a href=\"NuevaPartidaServlet\">Nueva Partida</a><br>"
				+ "<a href=\"SalirPartidaServlet\">Salir</a>");
	}
	
	/** 
	 * Método para escribir el pie del código HTML
	 */
	private void escribierPie() {
		pr.println("</body>"
				+ "</html>");
	}
} // fin servlet
