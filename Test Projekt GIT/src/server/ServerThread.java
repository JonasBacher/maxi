package server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ServerThread implements Runnable {

	private Socket client;
	private OutputStream out;
	private BufferedReader in;
	private FileInputStream fis;

	private String filename;
	private byte[] output = new byte[4096];

	public ServerThread(Socket client) {
		this.client = client;
		try {
			out = client.getOutputStream();
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			System.out.println("[SERVER]: Fehler beim initialisieren der Streams");
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				filename = in.readLine();
			} catch (IOException e) {
				System.out.println("[SERVER]: Client " + client.getInetAddress() + " disconnected!");
				return;
			}
			File file = new File(filename);
			if (file.isDirectory()) {
				sendDirectoery(filename);
			}else {
				printFile(filename);
			}
		}
	}

	private void sendDirectoery(String path) {
		try {
			FileSystem fs = new FileSystem(path);
			ByteArrayOutputStream dout = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(dout);
			oout.writeObject(fs);
			oout.flush();
			dout.close();
			oout.close();
		} catch (IOException e) {
			printStatus("Fehler beim Laden des Ordners");
		}
	}
	
	private void printFile(String filename) {
		try {
			fis = new FileInputStream(filename);
			if (fis == null) {
				System.out.println("[SERVER]: Datei nicht gefunden");
				fis.close();
			} else {
				fis.read(output);
				out.write(output, 0, output.length);
				out.flush();
				fis.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("[SERVER]: Datei nicht gefunden");
			printStatus("#Datei nicht gefunden!");
		} catch (IOException e) {
			System.out.println("[SERVER]: Fehler beim Lesen der Datei");
			printStatus("#Fehler beim Lesen der Datei!");
		}
	}
	
	
	private void printStatus(String message) {
		try {
			out.write(message.getBytes(), 0, message.length());
			out.flush();
		} catch (IOException e1) {
			System.out.println("[SERVER]: Fatal_ERROR: Senden der Fehlermelung");
		}
	}
}
