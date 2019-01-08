package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static ServerSocket serverSocket;
	private static Socket client;

	private static BufferedReader in;
	private static OutputStream out;
	private static FileInputStream fis;
	
	public static void main(String[] args) {
		try {
			System.out.println("[SERVER]: gestartet");
			serverSocket = new ServerSocket(8888);
			while(true) {
				client = serverSocket.accept();
				System.out.println("[SERVER]: Client " + client.getInetAddress() + " verbunden");
				new Thread(new ServerThread(client)).start();
			}
		} catch (IOException e) {
			System.out.println("[SERVER]: Sockets und Streams konnten nicht initialisiert werden");
		}
	}
}
