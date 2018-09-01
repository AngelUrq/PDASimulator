package com.pda.view;

import java.util.ArrayList;
import java.util.Stack;

import com.pda.control.AutomataControl;
import com.pda.entity.Automata;
import com.pda.entity.Regla;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("app.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Simulador de automatas de pila");
			primaryStage.getIcons().add(new Image("file::../../resources/icono.png"));
			//primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Regla r1 = new Regla("q0","a","Z","q1","Z");
		Regla r2 = new Regla("q0","#","Z","Fin","Z");
		Regla r3 = new Regla("q1","a","Z","q0","Z");
	
		ArrayList<Regla> listaReglas = new ArrayList<Regla>();
		listaReglas.add(r1);
		listaReglas.add(r2);
		listaReglas.add(r3);
		
		ArrayList<String> estados = new ArrayList<String>();
		estados.add("q0");
		estados.add("q1");
		estados.add("Fin");
		
		ArrayList<String> alfabetoEntrada = new ArrayList<String>();
		alfabetoEntrada.add("a");
		alfabetoEntrada.add("#");
		
		ArrayList<String> estadosIniciales = new ArrayList<String>();
		estadosIniciales.add("q0");
		
		ArrayList<String> alfabetoPila = new ArrayList<String>();
		alfabetoPila.add("z0");
		
		ArrayList<String> estadosAceptacion  = new ArrayList<String>();
		estadosAceptacion.add("Fin");
		
		Stack<String> pila = new Stack<String>();
		pila.add("z0");
	
		Automata definicion = new Automata(listaReglas, estados, alfabetoEntrada, estadosIniciales, alfabetoPila, estadosAceptacion, pila);
		
		AutomataControl automata = new AutomataControl(definicion, "aa");
		automata.simular();
		
		launch(args);
	}
	
}
