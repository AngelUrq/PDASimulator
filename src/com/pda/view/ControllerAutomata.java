package com.pda.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import com.pda.control.AutomataControl;
import com.pda.entity.Automata;
import com.pda.entity.Regla;

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

	private String[] listaEstados;
	private String[] listaAlfabeto;
	private String[] listaAlfabetoPila;
	private String[] listaEstadosIniciales;
	private String simboloInicialPila;
	private String[] listaEstadosAceptacion;

	private Automata automata;

	private String nombreArchivo;

	private ArrayList<Regla> reglas;

	private BufferedReader br;
	private FileReader fr;

	//Logica para la visualizacion de la pila

	@FXML private ScrollPane panePila;
	private ListView<String> objetosPila = new ListView<String>();
	private ObservableList<String> list = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		tamReglas = 0;
		grid = new GridPane();
		palabras = new ArrayList<String>();
		reglas = new ArrayList<Regla>();
		
		
		
		leerTexto();
		cargarReglas();
		crearAutomata();


	}
	
	public void crearAutomata() {
		
		automata = new Automata(); 

		automata.setEstados(convertir(listaEstados)); 
		automata.setAlfabetoEntrada(convertir(listaAlfabeto)); 
		automata.setAlfabetoPila(convertir(listaAlfabetoPila)); 
		automata.setEstadosIniciales(convertir(listaEstadosIniciales)); 
		automata.setEstadosAceptacion(convertir((listaEstadosAceptacion))); 
		automata.setReglas(leerReglas());
		
		Stack<String> pila = new Stack<String>();
		pila.push("Zo");
		automata.setPila(pila);
	}
	
	public void leerTexto() {

		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(ControllerDefinicionFormal.archivo);
			br = new BufferedReader(fr);
			
			String sCurrentLine;
			
			

				listaEstados = (br.readLine()).split(","); 
				listaAlfabeto = (br.readLine()).split(","); 
				listaAlfabetoPila = (br.readLine()).split(","); 
				listaEstadosIniciales = (br.readLine()).split(","); 
				simboloInicialPila = br.readLine(); 
				listaEstadosAceptacion = (br.readLine()).split(","); 
				

			
			//Añade la pila grafica y la acomoda en su sitio
			list.add(0,simboloInicialPila);
			objetosPila.setItems(list);
			panePila.setContent(objetosPila);
			objetosPila.setTranslateX(75);
			objetosPila.setTranslateY(525);		
			

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@FXML
	public void btnAnadirPresionado(ActionEvent event) throws IOException {

		String estadoActual = txtEstadoActual.getText();
		String entrada = txtEntrada.getText();
		String cimaPila = txtCimaPila.getText();
		String estadoNuevo = txtEstadoNuevo.getText();
		String accion = txtAccion.getText();	

		if(!estadoActual.toString().equals("") && !entrada.toString().equals("") && !cimaPila.toString().equals("") && !estadoNuevo.toString().equals("") && !accion.toString().equals("")) {

			Regla regla;
			boolean reglaRepetida = false;

			if(validarReglas(estadoActual, entrada, cimaPila, estadoNuevo)) {
				if(tamReglas > 0) {
					for(int i = reglas.size() - 2; i >= 0; i--) {
						if(reglas.get(i).getEstadoActual().equals(estadoActual) && reglas.get(i).getEntrada().equals(entrada) && reglas.get(i).getCimaPila().equals(cimaPila) && reglas.get(i).getEstadoNuevo().equals(estadoNuevo)){
							reglaRepetida = true;			
						}
					}
				}

				if(!reglaRepetida) {
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

					regla = new Regla(txt[0].getText(), txt[1].getText(), txt[2].getText(), txt[3].getText(), txt[4].getText());

					reglas.add(regla);

					System.out.println("Fila: " + tamReglas++);

					txtEstadoActual.setText("");
					txtEntrada.setText("");
					txtCimaPila.setText("");
					txtEstadoNuevo.setText("");
					txtAccion.setText("");	

					try(FileWriter fw = new FileWriter(ControllerDefinicionFormal.archivo, true);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter out = new PrintWriter(bw))
					{
						out.println(regla.toString());
					} catch (IOException e) {

					}
				} else {
					Mensaje.mostrarError("No deben existir reglas repetidas");
				}
			} else {
				Mensaje.mostrarAdvertencia("Debes ingresar datos que pertenezcan a la definicion formal");
			}
		} else {
			Mensaje.mostrarError("Debes ingresar datos");
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


	public boolean validarReglas(String estadoActual, String entrada, String cimaPila, String estadoNuevo) {
		boolean entradasValidadas = false;
		boolean validarEstados = false;
		boolean validarEntradas = false;
		boolean validarAlfabetoPila = false;
		boolean validarEstadoInicial = false;
		boolean validarEstadoFinal = false;

		for(int i = 0; i < listaEstados.length; i++) {
			if(listaEstados[i].equals(estadoActual)) {
				validarEstadoInicial = true;
			} 
			if(listaEstados[i].equals(estadoNuevo)) {
				validarEstadoFinal = true;
			}
		}
		validarEstados = validarEstadoInicial && validarEstadoFinal;

		for(int i = 0; i <  listaAlfabeto.length; i++) {
			
			if(entrada.equals("#")) {
				validarEntradas = true;
			}
			if(listaAlfabeto[i].equals(entrada)) {
				validarEntradas = true;
			}
		}

		for(int i = 0; i < listaAlfabetoPila.length; i++) {
			if(listaAlfabetoPila[i].equals(cimaPila)) {
				validarAlfabetoPila = true;
			}
		}

		entradasValidadas = validarEstados && validarEntradas && validarAlfabetoPila;
		return entradasValidadas;	
	}
	
	
	
	public void cargarReglas() {
		
		ArrayList<Regla> reglas = leerReglas();
					
			//Dibuja las reglas en la interfaz
			for (Regla regla : reglas) {
				
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

				txt[0].setText(regla.getEstadoActual());
				txt[1].setText(regla.getEntrada());
				txt[2].setText(regla.getCimaPila());
				txt[3].setText(regla.getEstadoNuevo());
				txt[4].setText(regla.getAccion());

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
			}
		
	}

	public void dibujarPila(String[] palabras) throws InterruptedException {

		for(int i = 0; i < palabras.length; i++) {	
			list.add(0,palabras[i]);
		}
		objetosPila.setItems(list);
		panePila.setContent(objetosPila);
		objetosPila.setTranslateY(objetosPila.getTranslateY() - 25 * palabras.length);
		TimeUnit.SECONDS.sleep(1);
	}

	public void borrarPila() throws InterruptedException {
		list.remove(0);
		objetosPila.setTranslateY(objetosPila.getTranslateY() + 25);
		TimeUnit.SECONDS.sleep(1);
	}

	public ArrayList<String> convertir(String[] string) {
		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(string));		
		return arrayList;
	}
	
	public ArrayList<Regla> leerReglas() {
		ArrayList<Regla> reglas = new ArrayList<Regla>();
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(ControllerDefinicionFormal.archivo);
			br = new BufferedReader(fr);
			
			String sCurrentLine = "";
			
			//Lee las primeras líneas para llegar hasta donde están las reglas
			for(int i  = 0;i < 6; i++) {	
				
				sCurrentLine = br.readLine();
			}

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

	//En este metodo se realiza el proceso logico del automata
	@FXML
	public void leerPalabra() {
		
		String [] a = {"a"};
		String [] b = {"b"};
		try {
			dibujarPila(a);
			dibujarPila(b);
			dibujarPila(a);
			dibujarPila(a);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String palabra = txtPalabraEntrada.getText();
		if(palabraPerteneceAlAlfabeto(palabra)) {
			if(palabra.equals("")) {
				palabra = "#";
			}
			AutomataControl automataControl = new AutomataControl(automata,palabra);
			automataControl.simular();
				
		}else {
			new Alert(Alert.AlertType.ERROR, "La palabra no pertenece al lenguaje").showAndWait();
		}	
	}
	


}

