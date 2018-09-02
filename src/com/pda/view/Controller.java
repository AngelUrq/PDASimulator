package com.pda.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

	private Desktop desktop;

	@FXML
	private ScrollPane listaScroll;
	
	@FXML
	private AnchorPane panelPrincipal;

	@FXML
	public void initialize() {
		desktop = Desktop.getDesktop();
		listar();
	}

	public void listar() {		
		GridPane grid = new GridPane();

		File folder = new File("saves/");
		File[] listOfFiles = folder.listFiles();

		for (int r = 0; r < listOfFiles.length;r++) {
			if (listOfFiles[r].isFile()) {
				Button button = new Button(listOfFiles[r].getName());
				button.setPrefWidth(listaScroll.getPrefWidth());
				button.setStyle("-fx-background-color: #357A86; -fx-text-fill: white; -fx-padding: 15px");
			    button.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            		//Abre la ventana de definición formal con los datos del archivo presionado
		            	String fileName = button.getText();
		            	
		            	FXMLLoader loader = new FXMLLoader();
		            	
		        		loader.setLocation(getClass().getResource("frmDefinicionFormal.fxml"));
		        		
		        		try {
		        			
		        		loader.load();	
		        			
		        		}catch(IOException ex){

		        			System.out.println("¡Témpanos de hielo!");
		        		}
		        		
		        		ControllerDefinicionFormal display = loader.getController();
		        		
		        		display.ponerTextoAlCargar(fileName);
		        		
		        		Parent p = loader.getRoot();
		        		Stage stage = new Stage();
		        		stage.setScene(new Scene(p));
		        		stage.showAndWait();
		            	
		        		
		            	
		            	
		            }
		        });
				grid.add(button, 1, r);
			}
		}

		listaScroll.setContent(grid);
	}

	@FXML	
	public void btnCrearPresionado(ActionEvent event) throws IOException {
		Parent pane = (AnchorPane)FXMLLoader.load(getClass().getResource("frmDefinicionFormal.fxml"));
		Scene nuevaEscena = new Scene(pane);
		Stage ventana = (Stage)(((Node) event.getSource()).getScene().getWindow());
		ventana.setScene(nuevaEscena);
	}

	@FXML	
	public void btnCargarPresionado(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(new Stage());
		if (file != null && formatoValido(file)) {
			open(file);
		}
	}
	
	public void open(File file) {
		try {
			desktop.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean formatoValido(File file) {
		
		String nombre = file.getName().substring(0, 8);
		System.out.println(nombre);
		if(nombre.equals("automata")) {
			return true;
		}
		new Alert(Alert.AlertType.ERROR, "Fórmato del archivo inválido").showAndWait();
		return false;
	}

}