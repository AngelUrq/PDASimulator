package com.pda.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.pda.entity.Regla;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControllerDefinicionFormal {

	@FXML private TextField txt_estados;
	@FXML private TextField txt_alfabeto_entrada;
	@FXML private TextField txt_alfabeto_pila;
	@FXML private TextField txt_estado_inicial;
	@FXML private TextField txt_simbolo_inicial_pila;
	@FXML private TextField txt_estados_aceptados;
	@FXML private Button btnComenzar;

	@FXML private Pane panelPrincipal;
	
	private boolean archivoCargado;

	public static String archivo;

	@FXML
	public void initialize() {
		archivoCargado = false;
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

		if(!alfabetoEntrada.matches("((\\w)|#)+(,((\\w)|#)+)*")) {
			validacion = false;
		}

		if(!alfabetoPila.matches("((\\w))+(,((\\w))+)*")) {
			validacion = false;
		}

		if(!estadoInicial.matches("(\\w)+(,(\\w)+)*")) {
			validacion = false;
		}

		if(!simboloInicialPila.matches("(\\w)+")) {
			validacion = false;
		}

		if(!estadosAceptados.matches("((\\w)+(,(\\w)+)*)?")) {
			validacion = false;
		}

		if(validacion) {
			String[] listaEstados = estados.split(",");
			String[] listaAlfabetoEntrada = alfabetoEntrada.split(",");
			String[] listaAlfabetoPila = alfabetoPila.split(",");
			String[] listaEstadosIniciales = estadoInicial.split(",");
			String[] listaEstadosAceptados = estadosAceptados.split(",");

			boolean segunda_validacion = true;
			boolean tercera_validacion = true;

			for(int i = 0; i < listaEstadosIniciales.length; i++) {
				if(!contiene(listaEstados, listaEstadosIniciales[i])) {
					segunda_validacion = false;
					Mensaje.mostrarError("Los estados iniciales no pertenecen al conjunto de estados.");
				}
			}

			for(int i = 0; i < listaEstadosAceptados.length; i++) {
				if(!contiene(listaEstados, listaEstadosAceptados[i]) && !listaEstadosAceptados[i].equals("")) {
					segunda_validacion = false;
					Mensaje.mostrarError("Los estados aceptados no pertenecen al conjunto de estados.");
				}
			}

			if(!contiene(listaAlfabetoPila, simboloInicialPila)) {
				segunda_validacion = false;
				Mensaje.mostrarError("El simbolo inicial de la pila no pertenece al alfabeto de pila");
			}

			if(!palabraRepetida(listaEstados)) {
				tercera_validacion = false;
				Mensaje.mostrarError("No puedes ingresar estados repetidos");
			}

			if(!palabraRepetida(listaAlfabetoEntrada)) {
				tercera_validacion = false;
				Mensaje.mostrarError("No puedes ingresar simbolos repetidos");
			}

			if(!palabraRepetida(listaAlfabetoPila)) {
				tercera_validacion = false;
				Mensaje.mostrarError("No puedes ingresar simbolos repetidos");
			}

			if(!palabraRepetida(listaEstadosIniciales)) {
				tercera_validacion = false;
				Mensaje.mostrarError("No puedes ingresar simbolos repetidos");
			}

			if(!palabraRepetida(listaEstadosAceptados)) {
				tercera_validacion = false;
				Mensaje.mostrarError("No puedes ingresar simbolos repetidos");
			}

			if(segunda_validacion && tercera_validacion) {
				Mensaje.mostrarMensaje("Datos aceptados");

				FileWriter fichero = null;
				PrintWriter pw = null;

				try
				{
					if(archivoCargado) {

						ArrayList<Regla> reg = recuperarReglas();

						fichero = new FileWriter(ControllerDefinicionFormal.archivo);

						pw = new PrintWriter(fichero);
						pw.println(estados);
						pw.println(alfabetoEntrada);
						pw.println(alfabetoPila);
						pw.println(estadoInicial);
						pw.println(simboloInicialPila);
						pw.println(estadosAceptados);

						if(reg != null) {

							for(Regla regla : reg) {
								pw.println(regla.getEstadoActual() + "," + regla.getEntrada() + "," + regla.getCimaPila()
								+ "," + regla.getEstadoNuevo() + "," + regla.getAccion());
							}
						}
					}else {
						String nombre = Mensaje.mostrarInput("Creando un nuevo autómata","Ingresa el nombre del archivo: ");
						
						fichero = new FileWriter("saves/automata-" + nombre + ".txt");
						ControllerDefinicionFormal.archivo = "saves/automata-" + nombre + ".txt";
						pw = new PrintWriter(fichero);
						pw.println(estados);
						pw.println(alfabetoEntrada + ",#");
						pw.println(alfabetoPila);
						pw.println(estadoInicial);
						pw.println(simboloInicialPila);
						pw.println(estadosAceptados);

					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fichero) {
							fichero.close();
						}
						Parent pane = (AnchorPane)FXMLLoader.load(getClass().getResource("frmAutomata.fxml"));
						Scene nuevaEscena = new Scene(pane);
						Stage ventana = (Stage)(((Node) event.getSource()).getScene().getWindow());
						ventana.setResizable(false);
						ventana.getIcons().add(new Image("file::../../resources/icono.png"));
						ventana.setScene(nuevaEscena);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			} 
		}else {
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

	public void ponerTextoAlCargar(String nombreArchivo) {

		//Coloca el texto en los TextField para poder editar
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader("saves/" + nombreArchivo);
			br = new BufferedReader(fr);

			txt_estados.setText(br.readLine());;
			txt_alfabeto_entrada.setText(br.readLine());;
			txt_alfabeto_pila.setText(br.readLine());
			txt_estado_inicial.setText(br.readLine());;
			txt_simbolo_inicial_pila.setText(br.readLine());;
			txt_estados_aceptados.setText(br.readLine());;

			ControllerDefinicionFormal.archivo = "saves/" + nombreArchivo;

			archivoCargado = true;

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	private boolean palabraRepetida(String[] lista) {
		boolean contiene_repeticion = true;
		for(int i = 0; i < lista.length; i++) {
			String palabra = lista[i].toString();
			int contador = 0;
			while(contador < lista.length) {
				if(lista[contador].equals(palabra) && i != contador) {
					contiene_repeticion = false;
					break;
				}
				contador++;
			}
		}
		return contiene_repeticion;
	}

	public ArrayList<Regla> recuperarReglas() {

		ArrayList<Regla> reglas = new ArrayList<Regla>();
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(ControllerDefinicionFormal.archivo);
			br = new BufferedReader(fr);

			String sCurrentLine;

			//Lee las primeras líneas para llegar hasta donde están las reglas


			for(int i  = 0;i < 6; i++) {	

				sCurrentLine = br.readLine();
			}
			sCurrentLine = "";

			//Lee las reglas y las guarda

			while ((sCurrentLine = br.readLine()) != null) { 
				Regla regla = new Regla();
				String[] r = sCurrentLine.split(",");
				regla.setEstadoActual(r[0]);
				regla.setEntrada(r[1]);
				regla.setCimaPila(r[2]);
				regla.setEstadoNuevo(r[3]);
				regla.setAccion(r[4]);

				reglas.add(regla);
			}




		} catch (IOException e) {

			e.printStackTrace();
		}
		return reglas;



	}	

}