package logica;

import java.util.Random;
import java.util.Vector;

public class Partida {
	
	private static final int AGUA = -1, TOCADO = -2, HUNDIDO = -3;
	
	/**
	 * El mar se representa mediante una matriz de casillas
	 * En una casilla no tocada con un barco se guarda el indice del barco en el vector de barcos
	 * El resto de valores posibles (AGUA, TOCADO y HUNDIDO) se representan mediante 
	 * constantes enteras negativas.
	 */
	private int mar[][] = null;				// matriz que contendra el mar y los barcos en distintos estados
	private boolean misDisparos[][] = null; // matriz que contendra los disparos realizados
	private int numFilas, 					// numero de filas del tablero
				numColumnas;				// numero de columnas del tablero
	private Vector<Barco> barcos = null;	// vector dinamico de barcos
	private int numBarcos, 					// numero de barcos de la partida
	            quedan,  					// numero de barcos no hundidos
	            disparos; 					// numero de disparos efectuados
	
	/**
	 * Contructor por defecto. Parametros por defecto
	 */
	public Partida() {
		this(8,8,6);
	}
	
	/**
	 * Constructor de una partida
	 * @param	nf	numero de filas del tablero
	 * @param   nf  numero de columnas del tablero
	 * @param   nc  numero de barcos
	 */
	public Partida(int nf, int nc, int nb) {
        this.numFilas = nf;
        this.numColumnas = nc;
        crearMar();
        iniciaDisparos();
        
        this.numBarcos = nb;
        crearBarcos();
        
        this.quedan = nb;
        this.disparos = 0;
        
	}

    /**
     * Crea los barcos y los coloca en la matriz MAR
     */
	private void crearBarcos() {
		barcos = new Vector<Barco>();
		ponBarcos();
	}

    /**
     * Coloca todos los valores de la matriz MAR al valor de AGUA
     */
	private void crearMar() {
		mar = new int[numFilas][numColumnas];
		for(int fil=0;fil<numFilas;fil++){
			for(int col=0;col<numColumnas;col++){
				mar[fil][col] = AGUA;
			}
		}

	}
	 /**
	  * Crea la matriz de misDisparos y pone el valor false a todas
	  */
	private void iniciaDisparos() {
		misDisparos = new boolean[numFilas][numColumnas];
		for (int fil = 0; fil < numFilas; fil++) {
			for (int col = 0; col < numColumnas; col++)
				misDisparos[fil][col] = false;
		}
	}

	/**
	 * Dispara sobre una casilla y devuelve el resultado
	 * @param	f	fila de la casilla
	 * @param   c   columna de la casilla
	 * @return		resultado de marcar la casilla: AGUA, ya TOCADO, ya HUNDIDO, identidad del barco recien hundido
	 */
    public int pruebaCasilla(int f, int c) {
    	int contenido = mar[f][c];
    	boolean disparado = casillaDisparada(f,c);
    	if (disparado) {
	        if(contenido>=0){  // Si contenido es > a 0 es la id de barco, no puede ser agua ni ninguna otra opcion
	        	Barco barco = barcos.get(contenido);
	        	barco.tocarBarco();
	        	misDisparos[f][c] = true;
	            mar[f][c] = TOCADO;
	        	if(barco.estaDestruido()){ // Debemos comprobar si una vez tocado el barco ha sido destruido
	                destruirBarco(barco.toString());  // Cambia todas las casillas del barco a destruidas
	        		return contenido; // Devuelve la id del barco
	        	}
	        	return TOCADO;
	
	        }
    	}
    	return contenido; // Devuelve si es agua, tocado, o hundido solo
    }


    private boolean casillaDisparada(int f, int c) {
    	if (misDisparos[f][c] == false)
    		return true;
    	return false;
	}

	/**
     * Descompone las caracteristicas del barco i las asignas a variables locales del metodo
     * @param   barco   Caracteristicas del barco
     */
    private void destruirBarco(String barco) {
        String[] splittedString = barco.split("#"); // Las caracteristicas del barco partido
        int fila = Integer.parseInt(splittedString[0]);
        int col = Integer.parseInt(splittedString[1]);
        String orientacion = splittedString[2]; // "V" o "H"
        int tamanyo = Integer.parseInt(splittedString[3]);

        marcarCasillasHundido(fila, col, tamanyo, orientacion);
    }

    /**
     * Marca las casillas en las que se encuentra el barco en HUNDIDO
     * @param   fila    fila de la casilla
     * @param   col     columna de la casilla
     * @param   tamanyo tamanyo del barco
     * @param   orientacion la orientacion en la que se encuentra el barco
     */
    private void marcarCasillasHundido(int fila, int col, int tamanyo, String orientacion) {
        if (orientacion.equals("V")) {
            for (int i = 0; i < tamanyo; i++) {
                mar[fila+i][col] = HUNDIDO;
            }
        } else {
            for (int i = 0; i < tamanyo; i++) {
                mar[fila][col+i] = HUNDIDO;
            }
        }
    }

    /**
	 * Devuelve una cadena con los datos de un barco dado: filIni, colIni, orientacion, tamanyo
	 * Los datos se separan con el caracter especial '#'
	 * @param	idBarco	indice del barco en el vector de barcos
	 * @return	        cadena con los datos del barco
	 */	
	public String getBarco(int idBarco) {
		return barcos.get(idBarco).toString();
	}
	
	/**
	 * Devuelve un vector de cadenas con los datos de todos los barcos
	 * @return	vector de cadenas, una por barco con la informacion de getBarco
	 */	
	public String[] getSolucion() {
		String[] solucion = new String[numBarcos];
		for(int barco=0;barco<numBarcos;barco++)
			solucion[barco] = getBarco(barco);
		return solucion;
	}
    
	public int getCasilla(int f, int c) {
		return mar[f][c];
	}
	
	public boolean getEstadoDisparo(int f, int c) {
		return misDisparos[f][c];
	}
	
	public int getDisparosEfectuados() {
		return disparos;
	}
	
	public int getBarcosQuedan() {
		return quedan;
	}
	
	/********************************    METODOS PRIVADOS  ********************************************/
    
	
	/**
	 * Coloca los barcos en el tablero
	 */	
	private void ponBarcos() {
		/* Por defecto colocamos un barco de tamaño 4, uno de tamaño 3, otro de tamaño 2 y tres barcos de tamaño 1 */
		barcos.add( ponBarco(0, 4) );
		barcos.add( ponBarco(1, 3) );
		barcos.add( ponBarco(2, 2) );
		barcos.add( ponBarco(3, 1) );
		barcos.add( ponBarco(4, 1) );
		barcos.add( ponBarco(5, 1) );
	}
	
	
	/**
	 * Busca hueco para un barco y lo coloca en el tablero.
	 * @param  id   indice del barco en el vector de barcos
	 * @param  tam  tamanyo del barco
	 * @return      un barco guardado como un objeto Barco
	 */	
	private Barco ponBarco(int id, int tam) {
        char orientacion=' ';
        boolean ok = false;
        int fila = 0, col = 0;
        Random random = new Random(); // Para generar aleatoriamente la orientacion y posicion de los barcos
        
        // Itera hasta que encuentra hueco para colocar el barco cumpliendo las restricciones
        while (!ok) {
        	// Primero genera aleatoriamente la orientacion del barco
            if (random.nextInt(2) == 0) { // Se dispone horizontalmente
            	// Ahora genera aleatoriamente la posicion del barco
                col = random.nextInt(numColumnas + 1 - tam); // resta tam para asegurar que cabe
                fila = random.nextInt(numFilas);

                // Comprueba si cabe a partir de la posicion generada con mar o borde alrededor
                if (librePosiciones(fila, col, tam+1, 'H')) {
                	// Coloca el barco en el mar
                    for (int i = 0; i < tam; i++) {
                        mar[fila][col+i] = id;
                    }
                    ok = true;
                    orientacion = 'H';
                }
            }
            else { //Se dispone verticalmente
                fila = random.nextInt(numFilas + 1 - tam);
                col = random.nextInt(numColumnas);
                if (librePosiciones(fila, col, tam+1, 'V')) {
                    for (int i = 0; i < tam; i++) {
                        mar[fila + i][col] = id;
                    }
                    ok = true;
                    orientacion = 'V';
                }
            } // end if H o V
        } // end while
        return new Barco(fila, col, orientacion, tam);
	}

	/**
	 * Comprueba si hay hueco para un barco a partir de una casilla inicial.
	 * Los barcos se colocan dejando una casilla de hueco con los otros.
	 * Pueden pegarse a los bordes.
	 * @param  fila   fila de la casilla inicial
	 * @param  col    columna de la casilla inicial
	 * @param  tam    tamanyo del barco + 1 para dejar hueco alrededor
	 * @param  ori    orientacion del barco: 'H' o 'V'
	 * @return        true si se encuentra hueco, false si no.
	 */	
    private boolean librePosiciones(int fila, int col, int tam, char ori) {
    	int i;
        if (ori == 'H') {
        	i = ( (col > 0) ? -1 : 0 );
        	// Comprueba que "cabe" horizontalmente a partir de la columna dada.
        	// Esto implica que:
        	// haya 'tam' casillas vacias (con mar) en la fila 'fila'
        	// y que quede rodeado por el mar o por un borde
        	while ( (col+i < numColumnas) && (i<tam) && (mar[fila][col+i] == AGUA) && 
        			( (fila == 0) || (mar[fila-1][col+i] == AGUA) )  &&
        			( (fila == numFilas-1) || (mar[fila+1][col+i] == AGUA) )         			
        		  ) i++;
        }
        else { // ori == 'V'
        	i = ( (fila > 0) ? -1 : 0 );
        	while ( (fila+i < numFilas) &&  (i<tam) && (mar[fila+i][col] == AGUA) &&
        			( (col == 0) || (mar[fila+i][col-1] == AGUA) )  &&
        			( (col == numColumnas-1) || (mar[fila+i][col+1] == AGUA) )  
        		  ) i++;
        }
        // Ha encontrado un hueco cuando ha generado el barco totalmente rodeado de agua o
        boolean resultado = (i == tam);
        // lo ha generado horizontal pegado al borde derecho o
        resultado = resultado || ( (ori == 'H') && (col+i == numColumnas) );
        // lo ha generado vertical pegado al borde inferior.
        resultado = resultado || ( (ori == 'V') && (fila+i == numFilas) );
        return resultado;
    }

	public int getBarcosHundidos() {
		return numBarcos-quedan;
	}
    
    
} // end class Partida
