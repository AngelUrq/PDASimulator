package com.pda.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Mensaje {

	public static void mostrarMensaje(String mensaje) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Información");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);

		alert.showAndWait();
	}
	
	public static void mostrarAdvertencia(String mensaje) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Alerta");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);

		alert.showAndWait();
	}

	public static void mostrarError(String mensaje) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Alerta");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);

		alert.showAndWait();
	}
	
}