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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ControllerDefinicionFormal {

	@FXML private TextField txt_estados;
	@FXML private TextField txt_alfabeto_entrada;
	@FXML private TextField txt_alfabeto_pila;
	@FXML private TextField txt_estado_inicial;
	@FXML private TextField txt_simbolo_inicial_pila;
	@FXML private TextField txt_estados_aceptados;

	@FXML  Button btnComenzar;

	@FXML
	public void initialize() {
		System.out.println("Empezando");
	}

	@FXML	
	public void btnComenzarPresionado(ActionEvent event) throws IOException {		
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
				Mensaje.mostrarError("El símbolo inicial de la pila no pertenece al alfabeto de pila");
			}

			if(segunda_validacion) {
				Mensaje.mostrarMensaje("¡Datos aceptados!");
				
				FileWriter fichero = null;
				PrintWriter pw = null;
				try
				{
					int r = new Random().nextInt(10000);
					fichero = new FileWriter("saves/fichero" + r + ".txt");
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