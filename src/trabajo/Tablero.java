package trabajo;

import java.io.Serializable;

public class Tablero implements Serializable {

	private static final long serialVersionUID = 111L;

	// 10 columnas y 10 filas
	public String[][] tablero = new String[10][10];

	// Constructor: tablero de 10x10 con -
	public Tablero() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				tablero[i][j] = " - ";
			}
		}
	}
	
	// Método para ver el tablero
	public void verTablero() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(tablero[i][j] + " ");
			}
			System.out.println();
		}
	}

	// Método para colocar un barco en el tablero
	public boolean colocarBarcos(int x, int y, char direccion, int tam) {
		String tamCadena = Integer.toString(tam);
		if (direccion == 'H') { // Horizontal
			if (y + tam > 10) {
				return false; // El barco no entra en horizontal
			}
			for (int i = 0; i < tam; i++) {
				if (!tablero[x][y + i].equals(" - ")) {
					return false; // Ya hay un barco en esa posición
				}
			}
			for (int i = 0; i < tam; i++) {
				tablero[x][y + i] = " " + tamCadena + " "; // Se coloca el barco
			}
		} else if (direccion == 'V') { // Vertical
			if (x + tam > 10) {
				return false; // El barco no entra en vertical
			}
			for (int i = 0; i < tam; i++) {
				if (!tablero[x + i][y].equals(" - ")) {
					return false; // Ya hay un barco en esa posición
				}
			}
			for (int i = 0; i < tam; i++) {
				tablero[x + i][y] = " " + tamCadena + " "; // Se coloca el barco
			}
		}
		return true;
	}

	// Método: jugada (Si es correcta, lo reemplaza por T y si no por A)
	public boolean juego(int x, int y, Tablero tableroJugador) {
		if (tablero[x][y].equals(" - ") || tablero[x][y].equals(" A ")) { // Si es agua --> fallo
			tableroJugador.tablero[x][y] = " A "; // agua
			tablero[x][y] = " A "; // agua
			return false;
		} // Si es barco --> acierto
		else {
			tableroJugador.tablero[x][y] = " T "; // Hundido
			tablero[x][y] = " T ";
		}
		return true;
	}

	// Método para verificar si todos los barcos han sido hundidos
	public boolean todosBarcosHundidos() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// Si encuentra un barco (que son los numeros del 1 al 5), devuelve falso
				if (tablero[i][j].equals(" 1 ") || tablero[i][j].equals(" 2 ") || tablero[i][j].equals(" 3 ")
						|| tablero[i][j].equals(" 4 ") || tablero[i][j].equals(" 5 ")) {
					return false; // Aún quedan barcos sin hundir
				}
			}
		}
		return true; // Todos los barcos han sido hundidos
	}

}
