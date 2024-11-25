package trabajo;

import java.io.*;
import java.net.*;
import java.util.*;

public class AtenderPeticion extends Thread {

	private Tablero tablero1 = new Tablero();
	private Tablero tablero2 = new Tablero();
	private Socket s1;
	private Socket s2;
	private static int turno;

	public AtenderPeticion(Socket s1, Socket s2) {
		this.s1 = s1;
		this.s2 = s2;
		turno = new Random().nextInt(2);
	}

	public void run() {
		try (ObjectOutputStream out1 = new ObjectOutputStream(s1.getOutputStream());
			 ObjectInputStream in1 = new ObjectInputStream(s1.getInputStream());
				ObjectOutputStream out2 = new ObjectOutputStream(s2.getOutputStream());
				ObjectInputStream in2 = new ObjectInputStream(s2.getInputStream())) {

			if (turno == 0) {
				out1.writeBytes("Eres el jugador 1 \n");
				out1.writeBoolean(true);
				out1.flush();
				//colocarBarcos(out1, in1, tablero1, turno + 1);
				out2.writeBytes("Eres el jugador 2 \n");
				out2.writeBoolean(false);
				out2.flush();
				//colocarBarcos(out2, in2, tablero2, turno + 2);
			} else {
				out2.writeBytes("Eres el jugador 1 \n");
				out2.writeBoolean(true);
				out2.flush();
				//colocarBarcos(out2, in2, tablero1, turno);
				out1.writeBytes("Eres el jugador 2 \n");
				out1.writeBoolean(false);
				out1.flush();
				//colocarBarcos(out1, in1, tablero2, turno + 1);
			}

			// Mostrar tableros
			System.out.println("Tablero del Jugador 1:");
			tablero1.verTablero();
			System.out.println("Tablero del Jugador 2:");
			tablero2.verTablero();
			
			
			out1.writeObject(tablero1); // Tablero del jugador 1
			out1.flush();
			out2.writeObject(tablero2); // Tablero del jugador 2
			out2.flush();

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
				System.err.println("Error cerrando los sockets: " + e.getMessage());
			}
		}
	}

}
