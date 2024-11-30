package trabajo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		try (Socket s = new Socket("localhost", 8000);
			 ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			 ObjectInputStream in = new ObjectInputStream(s.getInputStream());
				Scanner sc = new Scanner(System.in);) {
			System.out.println(in.readLine()); // Frase: Eres el jugador
			boolean tocaJugar = in.readBoolean(); // Leer booleano
			
			if (tocaJugar) {
				// Colocación de barcos
				for (int i = 0; i < 5; i++) {
					boolean colocado = false; 
					while (!colocado) {
						int[] barco = posicionValidaBarco(sc, i+1);
						out.writeObject(barco); // Enviar el objeto tablero con los barcos al servidor
						out.flush();
						colocado = in.readBoolean();
						if (colocado) {
							System.out.println("Barco colocado correctamente.");
						} else {
							System.out.println("Error al colocar el barco, intenta de nuevo.");
						}
					}
				}
			}
			
			Tablero tablero = (Tablero) in.readObject(); // Recibir el tablero del servidor
			System.out.println("\nTablero escogido para que adivine tu contrincante \n");
			tablero.verTablero(); // Mostrar el tablero

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static int[] posicionValidaBarco(Scanner sc, int i) {
		int x, y, direccion;
		System.out.println("Coloca el barco de tamaño " + i);
		// Validar posición X
		while (true) {
			System.out.print("Posición X (0-9): ");
			try {
				x = Integer.parseInt(sc.next()); // Es un número
				if (x >= 0 && x <= 9) {
					break; // Salir del bucle si es válida
				} else
					System.out.println("Error: La posición X debe estar entre 0 y 9. Inténtalo de nuevo.");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// Validar posición Y
		while (true) {
			System.out.print("Posición Y (0-9): ");
			try {
				y = Integer.parseInt(sc.next()); // Es un número
				if (y >= 0 && y <= 9) {
					break; // Salir del bucle si es válida
				} else
					System.out.println("Error: La posición Y debe estar entre 0 y 9. Inténtalo de nuevo.");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// Validar dirección
		while (true) {
			System.out.print("Dirección (0=H, 1=V): ");
			try {
				direccion = Integer.parseInt(sc.next()); // Es un número
				if (direccion == 0 || direccion == 1) {
					break; // Salir del bucle si es válida
				} else
					System.out.println("Error: La dirección debe ser 0 (Horizontal) o 1 (Vertical). Inténtalo de nuevo.");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return new int[] {x,y, i, direccion}; // Devolver el barco como un array
	}
	
	
}