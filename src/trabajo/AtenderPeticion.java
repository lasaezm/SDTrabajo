package trabajo;

import java.io.*;
import java.net.*;
import java.util.*;

public class AtenderPeticion extends Thread {

	private Tablero tablero1 = new Tablero();
	private Tablero tablero2 = new Tablero();
	private Tablero tableroJugador1 = new Tablero();
	private Tablero tableroJugador2 = new Tablero();

	private Socket s1;
	private Socket s2;
	private static boolean turnoJugador;

	public AtenderPeticion(Socket s1, Socket s2) {
		this.s1 = s1;
		this.s2 = s2;
		turnoJugador = new Random().nextBoolean();
	}

	public void run() {
		try (ObjectOutputStream out1 = new ObjectOutputStream(s1.getOutputStream());
			 ObjectInputStream in1 = new ObjectInputStream(s1.getInputStream());
			 ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());
			 ObjectInputStream in2 = new ObjectInputStream(s2.getInputStream())) {
			if (turnoJugador) {
				// Colocar barcos del jugador 1 
				out1.writeBytes("Eres el jugador 1 \n");
				out1.writeBoolean(true);
				out1.flush();
				completarTablero(out1, in1, tablero1); 
				// Colocar barcos del jugador 2
				out2.writeBytes("Eres el jugador 2 \n");
				out2.writeBoolean(true);
				out2.flush();
				completarTablero(out2, in2, tablero2);
			} else {
				out2.writeBytes("Eres el jugador 1 \n");
				out2.writeBoolean(true);
				out2.flush();
				completarTablero(out2, in2, tablero1); // Colocar barcos del jugador 1
				out1.writeBytes("Eres el jugador 2 \n");
				out1.writeBoolean(true);
				out1.flush();
				completarTablero(out1, in1, tablero2); // Colocar barcos del jugador 2
			}
			// Mostrar tableros 
			System.out.println("Tablero escogido por el Jugador 1:");
			tablero1.verTablero();
			System.out.println("Tablero escogido por el Jugador 2:");
			tablero2.verTablero();
			out1.writeObject(tablero1); // Tablero del jugador 1 
			out1.flush();
			out2.writeObject(tablero2); // Tablero del jugador 2 
			out2.flush();
			// OK
			long tiempoInicial = System.currentTimeMillis();
			boolean todosHundidos = false;
			while (!todosHundidos) {
				if (turnoJugador) {
					jugada(out1, in1, tableroJugador1, tablero2);
					boolean[] estado1 = mostrarEstadoBarcos(tablero2);
					out1.writeBytes("HUNDIDO? Barco 1  Barco 2  Barco 3  Barco 4  Barco 5\n");
					out1.writeBytes("	   " + Arrays.toString(estado1) + "\n"); 
					out1.flush();
					todosHundidos = tablero2.todosBarcosHundidos();
				} else {
					jugada(out2, in2, tableroJugador2, tablero1);
					boolean[] estado2 = mostrarEstadoBarcos(tablero1);
					out2.writeBytes("HUNDIDO? Barco 1  Barco 2  Barco 3  Barco 4  Barco 5\n");
					out2.writeBytes("	   " + Arrays.toString(estado2) + "\n");
					out2.flush();
					todosHundidos = tablero1.todosBarcosHundidos();
				}
				turnoJugador = !turnoJugador;
				out1.writeBoolean(todosHundidos);
				out2.writeBoolean(todosHundidos);
			}
			long tiempoFinal = System.currentTimeMillis() - tiempoInicial;
			// Mensajes de quien gana y quien pierde 
			finPartida(out1, in1, out2, in2, turnoJugador, tiempoFinal);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (s1 != null) {
					s1.close();
				}
				if (s2 != null) {
					s2.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//Método donde se realizada la jugada. Introducir coordenada x e y.
	private void jugada(ObjectOutputStream out, ObjectInputStream in, Tablero tabJug, Tablero t) {
		try {
			out.writeBytes("Tu turno! Introduce coordenada X (0-9): \n");
			out.flush();
			int x = in.readInt();
			while (x < 0 || x > 9) {
				out.writeBytes("Coordenada no válida. Introduce coordenada X (0-9): \n");
				out.flush();
				x = in.readInt();
			}
			out.writeBytes("Introduce coordenada Y (0-9): \n");
			out.flush();
			int y = in.readInt();
			while (y > 9 || y < 0) {
				out.writeBytes("Coordenada no válida. Introduce coordenada Y (0-9): \n");
				out.flush();
				y = in.readInt();
			}
			
			boolean acierto = t.juego(x, y, tabJug);
			if (acierto) {
				out.writeBytes("Acierto! \n");
			} else {
				out.writeBytes("Fallo :( \n");
			}
			out.flush();
			out.writeObject(tabJug);
			out.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Método para mostrar mensajes de fin de partida y ganador o perdedor
	private void finPartida(ObjectOutputStream out1, ObjectInputStream in1, ObjectOutputStream out2, ObjectInputStream in2, boolean turnoJugador, long tiempoFinal) {
		try {
			if (turnoJugador) {
				out1.writeBytes("Partida finalizada. Has perdido :( \n");
				out2.writeBytes("Partida finalizada. Has ganado!! \n");

			} else {
				out1.writeBytes("Partida finalizada. Has ganado!! \n");
				out2.writeBytes("Partida finalizada. Has perdido :( \n");
			}
			String duracion = "La partida ha durado: " + tiempoFinal/60000 + " minutos. \n";
			out1.writeBytes(duracion);
			out2.writeBytes(duracion);
			out1.flush();
			out2.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	// Método para colocar los barcos en el tablero
	private void completarTablero(ObjectOutputStream out, ObjectInputStream in, Tablero tablero) {
		for (int i = 0; i < 5; i++) {
			boolean colocado = false;
			while (!colocado) { // Recibir los datos del barco (posición, tamaño, dirección)
				try {
					int[] barco = (int[]) in.readObject();
					int x = barco[0];
					int y = barco[1];
					int tam = barco[2];
					int direccion = barco[3];
					// Convertir dirección 0 -> Horizontal, 1 -> Vertical
					char direccionChar = (direccion == 0) ? 'H' : 'V';
					// Verificar si la colocación es válida
					colocado = tablero.colocarBarcos(x, y, direccionChar, tam);
					// Enviamos si el barco ha sido colocado o no 
					out.writeBoolean(colocado);
					out.flush();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
		}
	}
	
	// Método para mostrar si los barcos han sido hundidos
	private boolean[] mostrarEstadoBarcos(Tablero tablero) {
		boolean[] estodobarcos = new boolean[5];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				String casilla = tablero.tablero[i][j]; 
				if (casilla.equals(" 1 ")) {
					estodobarcos[0] = true;
				} else if (casilla.equals(" 2 ")) {
					estodobarcos[1] = true;
				} else if (casilla.equals(" 3 ")) {
					estodobarcos[2] = true;
				} else if (casilla.equals(" 4 ")) {
					estodobarcos[3] = true;
				} else if (casilla.equals(" 5 ")) {
					estodobarcos[4] = true;
				}
			}
		}
		for (int n = 0; n < estodobarcos.length; n++) {
			estodobarcos[n] = !estodobarcos[n];
	    }
		return estodobarcos;
	}
}
