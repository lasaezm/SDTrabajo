package trabajo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		try (Socket s = new Socket("localhost", 8000);
			 ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			 ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			 Scanner sc = new Scanner(System.in);)
		{
			System.out.println(in.readLine()); // Frase: Eres el jugador boolean
			boolean tocaJugar = in.readBoolean(); // Leer booleano
			if (tocaJugar) { // Colocación de barcos
				for (int i = 0; i < 5; i++) {
					boolean colocado = false;
					while (!colocado) {
						int[] barco = posicionValidaBarco(sc, i + 1);
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
			// OK
			boolean todosHundidos = false;
			while (!todosHundidos) {
				System.out.print(in.readLine()); //coordenada x
				int x = sc.nextInt();
				out.writeInt(x);
				out.flush();
				while (x > 9 || x < 0) {
					System.out.print(in.readLine());	//no valido
					x = sc.nextInt();
					out.writeInt(x);
					out.flush();
				}
				System.out.print(in.readLine()); //coordenada y
				int y = sc.nextInt();
				out.writeInt(y);
				out.flush();
				while (y > 9 || y < 0) {
					System.out.print(in.readLine());  //no valido
					y = sc.nextInt();
					out.writeInt(y);
					out.flush();
				}
				System.out.println(in.readLine()); // Acierto o fallo
				Tablero t = (Tablero) in.readObject();
				t.verTablero();
				System.out.println(in.readLine());	//HUNDIDO?
				System.out.println(in.readLine());	//True,false, ...
				todosHundidos = in.readBoolean();
			}
			System.out.println(in.readLine()); // Juego terminado
			System.out.println(in.readLine()); // Tiempo
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Método para ver si la posicion es válida, 
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
					System.out.println("Error. La posición X debe estar entre 0 y 9, intenta de nuevo.");
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
					System.out.println("Error. La posición Y debe estar entre 0 y 9, intenta de nuevo.");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// Validar dirección
		while (true) {
			System.out.print("Dirección (H = 0, V = 1): ");
			try {
				direccion = Integer.parseInt(sc.next()); // Es un número
				if (direccion == 0 || direccion == 1) {
					break; // Salir del bucle si es válida
				} else
					System.out.println("Error. La dirección debe ser 0 (Horizontal) o 1 (Vertical), intenta de nuevo.");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return new int[] { x, y, i, direccion }; // Devolver el barco como un array
	}

}