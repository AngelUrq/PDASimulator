package com.pda.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSpinnerUI;

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
	@FXML private Button btnGuardarReglas;
	
	@FXML private TextField txtEstadoActual;
	@FXML private TextField txtEntrada;
	@FXML private TextField txtCimaPila;
	@FXML private TextField txtEstadoNuevo;
	@FXML private TextField txtAccion;
	@FXML private TextField [] txt;
	
	@FXML private ScrollPane listaReglas;
	
	private int tamReglas;
	
	private GridPane grid;
	
	private ArrayList<String> palabras;
	
	private ControllerDefinicionFormal definicionFormal;
	
	private String[] listaEstados;
	private String[] listaAlfabeto;
	private String[] listaAlfabetoPila;
	private String[] listaEstadosIniciales;
	private String simboloInicialPila;
	private String[] listaEstadosAceptacion;
	
	@FXML
	public void initialize() {
		tamReglas = 0;
		grid = new GridPane();
		palabras = new ArrayList<String>();
	}
	
	@FXML
	public void btnAnadirPresionado(ActionEvent event) throws IOException {
		boolean validarEstados = false;
		boolean validarEntradas = false;
		boolean validarAlfabetoPila = false;
		
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(ControllerDefinicionFormal.archivo);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				listaEstados = sCurrentLine.split(",");
				listaAlfabeto = (br.readLine()).split(",");
				listaAlfabetoPila = (br.readLine()).split(",");
				listaEstadosIniciales = (br.readLine()).split(",");
				simboloInicialPila = br.readLine();
				listaEstadosAceptacion = (br.readLine()).split(",");		
			}
			
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		
		String estadoActual = txtEstadoActual.getText();
		String entrada = txtEntrada.getText();
		String cimaPila = txtCimaPila.getText();
		String estadoNuevo = txtEstadoNuevo.getText();
		String accion = txtAccion.getText();	
		
		if(!estadoActual.toString().equals("") && !entrada.toString().equals("") && !cimaPila.toString().equals("") && !estadoNuevo.toString().equals("") && !accion.toString().equals("")) {
			boolean validarEstadoInicial = false;
			boolean validarEstadoFinal = false;
			
			for(int i = 0; i < listaEstados.length; i++) {
				System.out.println(listaEstados[i] + "==" + estadoActual);
				
				if(listaEstados[i].equals(estadoActual)) {
					validarEstadoInicial = true;
				} 
				if(listaEstados[i].equals(estadoNuevo)) {
					validarEstadoFinal = true;
				}
			}
			
			if(validarEstadoInicial && validarEstadoFinal) {
				validarEstados = true;
			}
			
			for(int i = 0; i <  listaAlfabeto.length; i++) {
				if(listaAlfabeto[i].equals(entrada)) {
					validarEntradas = true;
				}
			}
			
			for(int i = 0; i < listaAlfabetoPila.length; i++) {
				if(listaAlfabetoPila[i].equals(cimaPila)) {
					validarAlfabetoPila = true;
				}
			}
			
			if(validarEstados && validarEntradas && validarAlfabetoPila) {
				Label inicio = new Label();
				Label fin = new Label();
				Label [] separadores = new Label[4];
				
				Font tam = new Font(30);
				
				int columnaSeparador = 2;
				
				for(int i = 0; i < separadores.length; i++) {
					separadores[i] = new Label();
					separadores[i].setText(",");
					separadores[i].setFont(tam);
					separadores[i].setPrefWidth(16);
					
					grid.add(separadores[i], columnaSeparador, tamReglas);
					columnaSeparador += 2;
				}

				inicio.setText("<");
				inicio.setFont(tam);
				fin.setText(">");
				fin.setFont(tam);
				
				txt = new TextField[5];
				
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
					palabras.add(txt[i].getText().toString());
				}
				
				listaReglas.setContent(grid);
				System.out.println("Fila: " + tamReglas++);
			} else {
				Mensaje.mostrarAdvertencia("Debes ingresar datos que pertenezcan a la definicion formal");
			}
		} else {
			Mensaje.mostrarError("Debes ingresar datos!");
		}
	}
	
	public void btnGuardarReglas(ActionEvent event) throws IOException {
		System.out.println("Guardando...");
		
		for(int i = 0; i < palabras.size(); i++) {
			System.out.println();
		}	
	}
}
