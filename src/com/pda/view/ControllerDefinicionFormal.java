package com.pda.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControllerDefinicionFormal {

	@FXML private TextField txt_estados;
	@FXML private TextField txt_alfabeto_entrada;
	@FXML private TextField txt_alfabeto_pila;
	@FXML private TextField txt_estado_inicial;
	@FXML private TextField txt_simbolo_inicial_pila;
	@FXML private TextField txt_estados_aceptados;

	@FXML  Button btnComenzar;
	
	public static String archivo;

	@FXML
	public void initialize() {
		System.out.println("Empezando");
	}

	@FXML	
	public void btnComenzarPresionado(ActionEvent event) throws IOException {		
		txt_estados.setText("q0,q1,q2");
		txt_alfabeto_entrada.setText("a,b,c");
		txt_alfabeto_pila.setText("z0,A,B,C");
		txt_estado_inicial.setText("q0");
		txt_simbolo_inicial_pila.setText("z0");
		txt_estados_aceptados.setText("q2");
		
		String estados = txt_estados.getText().toString().replaceAll("\\s", "");
		String alfabetoEntrada = txt_alfabeto_entrada.getText().toString().replaceAll("\\s", "");
		String alfabetoPila = txt_alfabeto_pila.getText().toString().replaceAll("\\s", "");
		String estadoInicial = txt_estado_inicial.getText().toString().replaceAll("\\s", "");
		String simboloInicialPila = txt_simbolo_inicial_pila.getText().toString().replaceAll("\\s", "");
		String estadosAceptados = txt_estados_aceptados.getText().toString().replaceAll("\\s", ""); 

		boolean validacion = true;

		if(!estados.matches("(\\w)+(,(\\w)+)*")) {
			validacion = false;
		}

		if(!alfabetoEntrada.matches("(\\w)+(,(\\w)+)*")) {
			validacion = false;
		}

		if(!alfabetoPila.matches("(\\w)+(,(\\w)+)*")) {
			validacion = false;
		}

		if(!estadoInicial.matches("(\\w)+(,(\\w)+)*")) {
			validacion = false;
		}

		if(!simboloInicialPila.matches("(\\w)+")) {
			validacion = false;
		}

		if(!estadosAceptados.matches("(\\w)+(,(\\w)+)*")) {
			validacion = false;
		}

		if(validacion) {
			String[] listaEstados = estados.split(",");
			String[] listaAlfabetoPila = alfabetoPila.split(",");
			String[] listaEstadosIniciales = estadoInicial.split(",");
			String[] listaEstadosAceptados = estadosAceptados.split(",");

			boolean segunda_validacion = true;

			for(int i = 0; i < listaEstadosIniciales.length; i++) {
				if(!contiene(listaEstados, listaEstadosIniciales[i])) {
					segunda_validacion = false;
					Mensaje.mostrarError("Los estados iniciales no pertenecen al conjunto de estados.");
				}
			}

			for(int i = 0; i < listaEstadosAceptados.length; i++) {
				if(!contiene(listaEstados, listaEstadosAceptados[i])) {
					segunda_validacion = false;
					Mensaje.mostrarError("Los estados aceptados no pertenecen al conjunto de estados.");
				}
			}

			if(!contiene(listaAlfabetoPila, simboloInicialPila)) {
				segunda_validacion = false;
				Mensaje.mostrarError("El simbolo inicial de la pila no pertenece al alfabeto de pila");
			}

			if(segunda_validacion) {
				Mensaje.mostrarMensaje("Datos aceptados");
				
				FileWriter fichero = null;
				PrintWriter pw = null;
				try
				{
					int r = new Random().nextInt(10000);
					fichero = new FileWriter("saves/fichero" + r + ".txt");
					ControllerDefinicionFormal.archivo = "saves/fichero" + r + ".txt";
					pw = new PrintWriter(fichero);
					pw.println(estados);
					pw.println(alfabetoEntrada);
					pw.println(alfabetoPila);
					pw.println(estadoInicial);
					pw.println(simboloInicialPila);
					pw.println(estadosAceptados);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fichero)
							fichero.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			}
			
			Parent pane = (AnchorPane)FXMLLoader.load(getClass().getResource("frmAutomata.fxml"));
			Scene nuevaEscena = new Scene(pane);
			Stage ventana = (Stage)(((Node) event.getSource()).getScene().getWindow());
			ventana.setScene(nuevaEscena);
		} else {
			Mensaje.mostrarError("Datos incorrectos");
		}
	}

	private boolean contiene(String[] lista, String elemento) {
		boolean contiene = false;

		for(int i = 0; i < lista.length; i++) {
			if(lista[i].equals(elemento)) {
				contiene = true;
			}
		}
		return contiene;
	}
}