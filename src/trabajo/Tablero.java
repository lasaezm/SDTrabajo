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


}
