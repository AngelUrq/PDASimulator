package com.pda.view;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;

public class Controller {

	@FXML
	private ScrollPane listaScroll;

	@FXML
	public void initialize() {
		System.out.println("Método initialize");
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
	            grid.add(button, 1, r);
			}
        }
		
		listaScroll.setContent(grid);
	}

}