package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		Parent root;
		try {
			//FXML - Dokument wird geladen
			root = FXMLLoader.load(getClass().getResource("Aufgabe3.fxml"));
			primaryStage.setTitle("Aufgabe 3");
	        primaryStage.setScene(new Scene(root));
	        primaryStage.show();
		} catch (IOException e) {
			//Fehlermeldung, falls beim Laden entwas schief geht
			System.out.println("Fehler beim Laden des FXML-Dokuments");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
