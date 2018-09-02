package com.pda.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.plaf.synth.SynthSpinnerUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

public class ControllerAutomata {

	@FXML private Button btnAnadir;
	@FXML private Button btnGuardarReglas;
	@FXML private Button btnLeerPalabra;
	
	
	
	@FXML private Menu menu;
	@FXML private ScrollPane listaReglas;

	
	@FXML private TextField txtEstadoActual;
	@FXML private TextField txtEntrada;
	@FXML private TextField txtCimaPila;
	@FXML private TextField txtEstadoNuevo;
	@FXML private TextField txtAccion;
	@FXML private TextField [] txt;
	@FXML private TextField txtPalabraEntrada;
	
	
	
	private int tamReglas;
	
	private GridPane grid;
	
	private ArrayList<String> palabras;
	
	private ControllerDefinicionFormal definicionFormal;
	
	private String [] listaEstados;
	private String [] listaAlfabeto;
	private String [] listaAlfabetoPila;
	private String [] listaEstadosIniciales;
	private String simboloInicialPila;
	private String [] listaEstadosAceptacion;
	
	private Automata automata ;
	private String nombreArchivo;
	
	
	
	//Lógica para la visualización de la pila
	
			@FXML private ScrollPane panePila;
				  private ListView<String> objetosPila = new ListView<String>();
				  private ObservableList<String> list = FXCollections.observableArrayList();

	
	@FXML
	public void initialize() {
		tamReglas = 0;
		grid = new GridPane();
		palabras = new ArrayList<String>();
		
		String simboloInicial = "";

		//Le coloca al autómata todos sus atributos, dejándolo listo para la recibir reglas 
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(ControllerDefinicionFormal.archivo);
			br = new BufferedReader(fr);
		
			automata = new Automata();

			automata.setEstados(convertir(br.readLine().split(",")));
			automata.setAlfabetoEntrada(convertir((br.readLine()).split(",")));
			automata.setAlfabetoPila(convertir((br.readLine()).split(",")));
			automata.setEstadosIniciales(convertir((br.readLine()).split(",")));
			simboloInicial = (br.readLine());
			automata.setEstadosAceptacion(convertir((br.readLine()).split(",")));	
			
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		
		//Añade la pila gráfica y la acomoda en su sitio
		
		list.add(0,simboloInicial);
		objetosPila.setItems(list);
		panePila.setContent(objetosPila);
		objetosPila.setTranslateX(75);
		objetosPila.setTranslateY(525);		

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
				
				Font tamano = new Font(30);
				
				int columnaSeparador = 2;
				
				for(int i = 0; i < separadores.length; i++) {
					separadores[i] = new Label();
					separadores[i].setText(",");
					separadores[i].setFont(tamano);
					separadores[i].setPrefWidth(16);
					
					grid.add(separadores[i], columnaSeparador, tamReglas);
					columnaSeparador += 2;
				}

				inicio.setText("<");
				inicio.setFont(tamano);
				fin.setText(">");
				fin.setFont(tamano);
				
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
				Mensaje.mostrarAdvertencia("Debes ingresar datos que pertenezcan a la definici�n formal");
			}
		} else {
			Mensaje.mostrarError("�Debes ingresar datos!");
		}
	}
	
	public void btnGuardarReglas(ActionEvent event) throws IOException {
		System.out.println("Guardando...");
		
		for(int i = 0; i < palabras.size(); i++) {
			System.out.println();
		}	
	}
	
	public void irADefFormal() {
		//Abre la ventana de definición formal con los datos del archivo presionado
    	String fileName = ControllerDefinicionFormal.archivo.substring(6);
    	System.out.println(fileName);
    	
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
	
	
	public void dibujarPila(String[] palabras) {
		
		for(int i = 0; i < palabras.length; i++) {	
			list.add(0,palabras[i]);
		}
		objetosPila.setItems(list);
		panePila.setContent(objetosPila);
		objetosPila.setTranslateY(objetosPila.getTranslateY()-25*palabras.length);
	}
	
	public void borrarPila() {
		list.remove(0);
		objetosPila.setTranslateY(objetosPila.getTranslateY()+25);
	}
	
	public ArrayList<String> convertir(String[] string) {
		
		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(string));		
		return arrayList;
	}
	
	
	public boolean palabraPerteneceAlAlfabeto(String palabra) {
	
		ArrayList<String> alfabeto = automata.getAlfabetoEntrada();
		boolean esValido;
		boolean pertenece = true;
		if(palabra.equals("")) {
			pertenece = true;
		}
		String[] p = palabra.split("");
		
		for(int i = 0; i< p.length; i++) {//para cada letra de la palabra
			 esValido = false;

			for(int j = 0; j< alfabeto.size(); j++) {//para cada letra del alfabeto

				if(p[i].equals(alfabeto.get(j))) {
					esValido =  true;
				}			
			}
			if(!esValido) {
				pertenece = false;
			}
		}
		return pertenece;
	
	}
	
	
	
	
	//En éste método se realiza el proceso lógico del autómata
	@FXML
	public void leerPalabra() {
		
		 String palabra = txtPalabraEntrada.getText();
		 if(palabraPerteneceAlAlfabeto(palabra)) {
			 if(palabra.equals("")) {
				 palabra = "#";
			 }
			 		 
			 //Aqui toda la lógica del autómata
				 
		 }else {
			 
				new Alert(Alert.AlertType.ERROR, "La palabra no pertenece al lenguaje").showAndWait();
		 }	
	}

}
