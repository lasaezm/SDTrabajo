package trabajo;

import java.io.Serializable;

public class Tablero implements Serializable {

	private static final long serialVersionUID = 111L;

	// 10 columnas y 10 filas
	public String[][] tablero = new String[10][10];

	public Tablero() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				tablero[i][j] = " - ";
			}
		}
	}

	protected void verTablero() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print (tablero[i][j] + " ");
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
				tablero[x][y + i] =  " " + tamCadena + " "; // Coloca el barco
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



}
