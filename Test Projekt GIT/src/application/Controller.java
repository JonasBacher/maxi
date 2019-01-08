package application;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import server.FileSystem;

public class Controller {
	@FXML
	private Button btn_take;
	@FXML
	private TextField txt_take;
	@FXML
	private TextField txt_save;
	@FXML
	private TextArea txt_status;

	private Socket server;
	private InputStream in;
	private PrintWriter out;
	private FileOutputStream fos;
	private BufferedOutputStream bos;

	private int bytesRead, current = 0;

	private byte[] input = new byte[40960];

	@FXML
	public void initialize() {
		try {
			printStatus("verbinde zu Server...", false);
			server = new Socket("localhost", 8888);
			out = new PrintWriter(server.getOutputStream(), true);
			in = server.getInputStream();
			printStatus("verbunden", false);
		} catch (UnknownHostException e) {
			printStatus("Server nicht erreichbar!", false);
		} catch (IOException e) {
			printStatus("Fehler beim erstellen der Streams", false);
		}
	}

	@FXML
	public void onTake() {
		if (!(txt_take.getText().isEmpty()) && !(txt_save.getText().isEmpty())) {
			out.println(txt_take.getText());
			out.flush();
			try {
				printStatus("Datei wird heruntergeladen...", false);
				btn_take.setDisable(true);
				File file = new File(txt_take.getText());
				if (file.isDirectory()) {
					saveDirectory(txt_save.getText());
				} else {
					in.read(input, 0, input.length);
					String message = new String(input, 0, input.length);
					if (message.startsWith("#"))
						printStatus(message, true);
					else {
						printFile(new String(input, 0, input.length), "noname");
					}
				}
				btn_take.setDisable(false);
			} catch (IOException e) {
				printStatus("Fehler beim Lesen der Daten vom Server", false);
			}
		} else {
			printStatus("geben Sie einen Dateipfad an!", false);
		}
	}

	private void saveDirectory(String path) {
		try {
			FileSystem fs = new FileSystem(path);
			fs.rebuild(path);
			ByteArrayInputStream din = new ByteArrayInputStream(input);
			ObjectInputStream doin = new ObjectInputStream(din);

			fs = (FileSystem) doin.readObject();
		} catch (IOException e) {
			printStatus("Fehler beim Speichern des Ordners", false);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			printStatus("Fehler beim Empfangen des Ordners", false);
		}
	}

	private void printFile(String data, String name) {
		try {
			if (!name.equals("noname")) {
				fos = new FileOutputStream(name);
				fos.write(data.getBytes());
				fos.close();
			} else {
				fos = new FileOutputStream(txt_save.getText());
				fos.write(data.getBytes());
				fos.close();
			}
		} catch (FileNotFoundException e) {
			printStatus("Fehler beim Schreiben der Datei", false);
		} catch (IOException e) {
			printStatus("Fehler beim Erstellen einer oder mehrerer Dateien", false);
		}
	}

	private void printStatus(String message, boolean server) {
		String text = message.substring(1, message.length());
		if (server)
			txt_status.appendText("[SERVER]: " + text + "\n");
		else
			txt_status.appendText("[CLIENT]: " + message + "\n");
	}

}
