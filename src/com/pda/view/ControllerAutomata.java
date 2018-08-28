package com.pda.view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;

public class ControllerAutomata {

	@FXML private Button btnAnadir;
	
	@FXML private TextField txtEstadoActual;
	@FXML private TextField txtEntrada;
	@FXML private TextField txtCimaPila;
	@FXML private TextField txtEstadoNuevo;
	@FXML private TextField txtAccion;
	
	@FXML private ScrollPane listaReglas;
	
	private int tamReglas;
	
	private GridPane grid;
	
	@FXML
	public void initialize() {
		tamReglas = 0;
		grid = new GridPane();
	}
	
	@FXML
	public void btnAnadirPresionado(ActionEvent event) throws IOException {
		
		String estadoActual = txtEstadoActual.getText();
		String entrada = txtEntrada.getText();
		String cimaPila = txtCimaPila.getText();
		String estadoNuevo = txtEstadoNuevo.getText();
		String accion = txtAccion.getText();
		
		if(!estadoActual.toString().equals("") && !entrada.toString().equals("") && !cimaPila.toString().equals("") && !estadoNuevo.toString().equals("") && !accion.toString().equals("")) {
			
			Label inicio = new Label();
			Label fin = new Label();
			Label [] separadores = new Label[4];
			
			Font tamaño = new Font(30);
			
			int columnaSeparador = 2;
			
			for(int i = 0; i < separadores.length; i++) {
				separadores[i] = new Label();
				separadores[i].setText(",");
				separadores[i].setFont(tamaño);
				separadores[i].setPrefWidth(16);
				
				grid.add(separadores[i], columnaSeparador, tamReglas);
				columnaSeparador += 2;
			}

			inicio.setText("<");
			inicio.setFont(tamaño);
			fin.setText(">");
			fin.setFont(tamaño);
			
			TextField [] txt = new TextField[5];
			
			for(int i = 0; i < txt.length; i++) {
				txt[i] = new TextField();
			}
			
			txt[0].setText(estadoActual.toString());
			txt[1].setText(entrada.toString());
			txt[2].setText(cimaPila.toString());
			txt[3].setText(estadoNuevo.toString());
			txt[4].setText(accion.toString());
			
			int columnaInicio = 0;
			grid.add(inicio, columnaInicio, tamReglas);
			
			int columnaFinal = 10;
			grid.add(fin, columnaFinal, tamReglas);
		
			for(int i = 0; i < txt.length; i++) {
				txt[i].setPrefWidth(70);
				grid.add(txt[i], 2 * i + 1, tamReglas);
			}
			
			listaReglas.setContent(grid);
			System.out.println("Fila: " + tamReglas++);
		} else {
			Mensaje.mostrarError("Debes ingresar correctamente los datos");
		}
	}
}
