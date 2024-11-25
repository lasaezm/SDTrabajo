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
			System.out.println(in.readLine());
			boolean tocaJugar = in.readBoolean();
			
			

			Tablero tablero = (Tablero) in.readObject(); // Recibir el tablero del servidor
			System.out.println("Tu tablero es:");
			tablero.verTablero(); // Mostrar el tablero

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}