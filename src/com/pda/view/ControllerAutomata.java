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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.pda.control.AutomataControl;
import com.pda.entity.Automata;
import com.pda.entity.Regla;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	
	@FXML private CheckBox cbPilaVacia;
	@FXML private CheckBox cbEstadoAceptado;

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
	
	private boolean pilaVacia;
	private boolean estadoAceptacion;

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

		pilaVacia = false;
		estadoAceptacion = false;

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
		automata.setSimboloInicial(simboloInicialPila);
		automata.setReglas(reglas);
		
		Stack<String> pila = new Stack<String>();
		pila.push(simboloInicialPila);
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

			//AÃ±ade la pila grafica y la acomoda en su sitio
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
					System.out.println("Entre");
					for(int i = reglas.size() - 1; i >= 0; i--) {
						if(reglas.get(i).getEstadoActual().equals(estadoActual) && reglas.get(i).getEntrada().equals(entrada) && reglas.get(i).getCimaPila().equals(cimaPila) && reglas.get(i).getEstadoNuevo().equals(estadoNuevo)){
							reglaRepetida = true;			
						}
					}
				}

				if(!reglaRepetida) {
					
					dibujarRegla(estadoActual, entrada, cimaPila, estadoNuevo, accion);
					regla = new Regla(txt[0].getText(), txt[1].getText(), txt[2].getText(), txt[3].getText(), txt[4].getText());

					reglas.add(regla);
					tamReglas++;

					System.out.println("Fila: " + tamReglas);

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
		//Abre la ventana de definiciÃ³n formal con los datos del archivo presionado
		String fileName = ControllerDefinicionFormal.archivo.substring(6);
		System.out.println(fileName);

		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(getClass().getResource("frmDefinicionFormal.fxml"));

		try {

			loader.load();	

		}catch(IOException ex){

			System.out.println("Â¡TÃ©mpanos de hielo!");
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
		
		if(cimaPila.equals("Z")) {
			validarAlfabetoPila = true;
		}

		entradasValidadas = validarEstados && validarEntradas && validarAlfabetoPila;
		return entradasValidadas;	
	}


	public void cargarReglas() {
		
		leerReglas();
		//Dibuja las reglas en la interfaz
		for (int i = 0; i < reglas.size(); i++) {
			dibujarRegla(reglas.get(i).getEstadoActual(), reglas.get(i).getEntrada(), reglas.get(i).getCimaPila(), reglas.get(i).getEstadoNuevo(), reglas.get(i).getAccion());
			tamReglas++;	
		}
	}

	public void dibujarPila(String[] palabras)  {

		Timer t = new Timer();

		TimerTask tt = new TimerTask() {
			@Override
			public void run() {

				for(int i = 0; i < palabras.length; i++) {	
					list.add(0,palabras[i]);
				}
				objetosPila.setItems(list);
				panePila.setContent(objetosPila);
				objetosPila.setTranslateY(objetosPila.getTranslateY() - 25 * palabras.length);
				t.cancel();
			};
		};
		t.schedule(tt,400,400);

	}

	public void borrarPila()  {
		list.remove(0);
		objetosPila.setTranslateY(objetosPila.getTranslateY() + 25);
	}

	public ArrayList<String> convertir(String[] string) {
		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(string));		
		return arrayList;
	}

	public void leerReglas() {
		
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(ControllerDefinicionFormal.archivo);
			br = new BufferedReader(fr);

			String sCurrentLine = "";
			
			//Lee las reglas y las guarda
			while ((sCurrentLine = br.readLine()) != null) { 
				leerTexto();
				Regla regla = new Regla();
				String[] r = sCurrentLine.split(",");
				
				try {
					if(contiene(listaEstados, r[0]) && contiene(listaAlfabeto, r[1]) && contiene(listaAlfabetoPila, r[2]) && contiene(listaEstados, r[3])) {
						regla.setEstadoActual(r[0]);
						regla.setEntrada(r[1]);
						regla.setCimaPila(r[2]);
						regla.setEstadoNuevo(r[3]);
						regla.setAccion(r[4]);

						reglas.add(regla);
					}
				}catch (Exception e) {
				}
				
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}

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
		String palabra = txtPalabraEntrada.getText();
		if(palabra.equals("")) {
			palabra = "#";
		}
		
		if(palabraPerteneceAlAlfabeto(palabra)) {
			estadoAceptacion = cbEstadoAceptado.isSelected();
			pilaVacia = cbPilaVacia.isSelected();
			if(estadoAceptacion || pilaVacia) {
				AutomataControl automataControl = new AutomataControl(automata,palabra,estadoAceptacion, pilaVacia);
				automataControl.simular();
			} else {
				Mensaje.mostrarError("No se marcÃ³ si el autÃ³mata acepta por pila vacÃ­a o estado de aceptaciÃ³n");
			}
		}else {
			new Alert(Alert.AlertType.ERROR, "La palabra no pertenece al lenguaje").showAndWait();
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

	private void dibujarRegla(String estadoActual, String entrada, String cimaPila, String estadoNuevo, String accion) {
		
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

		txt[0].setText(estadoActual);
		txt[1].setText(entrada);
		txt[2].setText(cimaPila);
		txt[3].setText(estadoNuevo);
		txt[4].setText(accion);

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

