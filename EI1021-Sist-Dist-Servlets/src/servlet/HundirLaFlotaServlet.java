package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logica.Partida;

/**
 * Servlet implementation class HundirLaFlotaServlet
 */
@WebServlet("/HundirLaFlotaServlet")
public class HundirLaFlotaServlet extends HttpServlet {
	
	private static final int NUMFILAS = 8, NUMCOL = 8, NUMBARCOS = 6;
	private static final int NUEVA = 0, FINAL = 1, PROCESO = 2; 
	private static final int AGUA = -1, TOCADO = -2, HUNDIDO = -3;
	
	private static final long serialVersionUID = 1L;
	Partida partida;
	int fila, columna, estadoDisparo;
	int estadoPartida;
	
	
    public HundirLaFlotaServlet() {
        super();
        
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Recuperar el objeto sesion
		HttpSession session = request.getSession(true);
		
		partida = (Partida) session.getAttribute("partida");
		if (partida == null) {
			partida = new Partida(NUMFILAS,NUMCOL, NUMBARCOS);
			estadoPartida = NUEVA;
		}
		else {
			if (partida.getBarcosQuedan() > 0) {
				estadoPartida = PROCESO;
				String botonDisparo = (String) session.getAttribute("disparo");
				String[] casillasDisparo = botonDisparo.split("x");
				fila = Integer.valueOf(casillasDisparo[0]);
				columna = Integer.valueOf(casillasDisparo[1]);
				estadoDisparo = partida.pruebaCasilla(fila, columna);
			}
			else
				estadoPartida = FINAL;
		}
		
		// Guardar la sesion
		session.setAttribute("partida", partida);
		
		// Definir el tipo de contenido
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pr = response.getWriter();
		
		//Cabecera pagina
		pr.println("<html>"
				+ "<head><title>Hundir la flota</title></head>");
		
		//Cuerpo pagina
		pr.println("<body>"
				+ "<h2>Hundir la flota</h2> <br>");
		
		estadoPartida(pr);
		dibujarTablero(pr);
		
		// Pie de pagina
		//TODO Poner los nombres del servlet
		pr.println("<a href=\"\">Muestra solución</a><br>"
				+ "<a href=\"\">NuevaPartida</a><br>"
				+ "<a href=\"\">Salir</a>");
		
		pr.println("</body>"
				+ "</html>");
	}


	private void estadoPartida(PrintWriter pr) {
		switch (estadoPartida) {
			case NUEVA:
				pr.println("<b> Nueva partida</b> <br>"
						+ "Barcos navegando: " + NUMBARCOS + "- Barcos hundidos: 0 <br>"
						+ "Número de disparos efectuados: 0 <br>");
				break;
				
			case PROCESO:
				pr.println("Página de resultado del disparo en (" + fila + ", " + ((char)columna + 64) + "): Ok!<br>"
						+ "Barcos navegando: " + (6 - partida.getBarcosQuedan()) + "- Barcos hundidos:" + partida.getBarcosQuedan() + "<br>"
								+ "Número de disparos efectuados: " + partida.getDisparosEfectuados() + "<br>");
				break;
			
			case FINAL:
				pr.println("<b>GAME OVER</b>");
				break;
		}
			
	}
	
	private void dibujarTablero(PrintWriter pr) {
		if (estadoPartida == NUEVA) {
			pr.println("<table width=\"20\">");
			for (int filaT = 0; filaT <= 8; filaT++) {
				// Dibuja las letra que son el numero de columna
				if (filaT == 0) {
					for (int columnaT = 0; columnaT <= 8; columnaT++) {
						pr.println("<tr>");
						if (columnaT == 0)
							pr.println("<td></td>");
						else
							pr.println("<td align=\"center\">" + ((char) columnaT + 64) + "</td>");
					}
					pr.println("</tr>");
				}
				// Dibuja el resto del tablero
				else {
					pr.println("<tr>");
					for (int columnaT = 1; columnaT <= 8; columnaT++) {
						// Dibuja los numeros de la fila
						if (columnaT == 0)
							pr.println("<td align=\"center\">" + filaT + "</td>");
						else {
							boolean isDisparado = partida.getEstadoDisparo(filaT,columnaT);
							pr.print("<td");
							// Si la casilla ha sido disparada le pone color
							if (isDisparado) {
								int estadoCasilla = partida.getCasilla(filaT, columnaT);
								switch (estadoCasilla) {
									case AGUA:
										pr.print(" style=\"background-color:cyan\"");
										break;
									
									case TOCADO:
										pr.print(" style=\"background-color:orange\"");
										break;
	
									case HUNDIDO:
										pr.print(" style=\"background-color:red\"");
										break;
								}
							}
							// La casilla no ha sido disparada no pone el Style por lo que no la pinta y rellena el html de toda la fila
							pr.print( "><input type=\"radio\" name=\"radioboton\" value=\"" + filaT + "#" + columnaT +"\" style=\"display: block;margin:auto;\"></td>");
						}
					}
					pr.println("</tr>"); 
				}
			} // fin for de filas
			// Dibuja el boton de submit
			pr.println("<tr>"
					+ "<td align=\"center\"><input type=\"sumit\" value=\"submit\"></td>"
					+ "</tr>"
				+ "</table>");
		}
	} // fin dibujarTableroVacio
} // fin servlet
