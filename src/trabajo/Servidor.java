package trabajo;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Servidor {
	public static void main(String[] args) {

		ExecutorService pool=Executors.newCachedThreadPool();
		try(ServerSocket ss=new ServerSocket(8000)){
			System.out.println("Servidor funcionando...");
			while(true) {
				try {
					Socket s1=ss.accept();
					System.out.println("Jugador 1 conectado");
					Socket s2=ss.accept();
					System.out.println("Jugador 2 conectado");
					AtenderPeticion ap = new AtenderPeticion(s1,s2);
					pool.execute(ap);
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			pool.shutdown();
		}
	}
}
