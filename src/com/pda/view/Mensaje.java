package com.pda.view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class Mensaje {

	public static void mostrarMensaje(String mensaje) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Informacion");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file::../../resources/icono.png"));

		alert.showAndWait();
	}
	
	public static void mostrarAdvertencia(String mensaje) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Alerta");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file::../../resources/icono.png"));
		
		alert.showAndWait();
	}

	public static void mostrarError(String mensaje) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Alerta");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file::../../resources/icono.png"));
		
		alert.showAndWait();
	}
	
	public static String mostrarInput(String mensaje1, String mensaje2) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Ingresar datos");
		dialog.setHeaderText(mensaje1);
		dialog.setContentText(mensaje2);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    return result.get();
		}
		
		return "";
	}
	
}