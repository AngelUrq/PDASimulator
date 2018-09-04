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
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

	@FXML private Pane panelPrincipal;

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
		objetosPila.setFixedCellSize(25);


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

			if(validarReglas(estadoActual, entrada, cimaPila, estadoNuevo, accion)) {
				if(tamReglas > 0) {
					System.out.println("Entre");
					for(int i = reglas.size() - 1; i >= 0; i--) {
						if(reglas.get(i).getEstadoActual().equals(estadoActual) && reglas.get(i).getEntrada().equals(entrada) && reglas.get(i).getCimaPila().equals(cimaPila) && reglas.get(i).getEstadoNuevo().equals(estadoNuevo) && reglas.get(i).getAccion().equals(accion)){
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

		FileWriter fichero = null;
		PrintWriter pw = null;

		boolean validado = true;

		if(reglas.size() > 0) {
			reglas.clear();
		}

		for(int i = 0; i < getRowCount(grid); i++) {
			for(int j = 0; j < txt.length ;j++) {
				txt[j] = (TextField) getNodeByRowColumnIndex(i, 2 * j + 1 , grid);
			}

			if(validarReglas(txt[0].getText(), txt[1].getText(), txt[2].getText(), txt[3].getText(), txt[4].getText())) {
				Regla regla = new Regla(txt[0].getText(), txt[1].getText() , txt[2].getText() , txt[3].getText() , txt[4].getText());
				reglas.add(regla);
				System.out.println(regla.toString());
			}
			else {
				validado = false;
				Mensaje.mostrarError("Ingresaste una regla mal. Revisa que estas pertenezcan a tu definicion formal");
			}
		}


		if(validado) {
			try
			{
				fichero = new FileWriter(ControllerDefinicionFormal.archivo);

				pw = new PrintWriter(fichero);
				pw.println(concatenarListas(listaEstados, ","));
				pw.println(concatenarListas(listaAlfabeto, ","));
				pw.println(concatenarListas(listaAlfabetoPila, ","));
				pw.println(concatenarListas(listaEstadosIniciales, ","));
				pw.println(simboloInicialPila);
				pw.println(concatenarListas(listaEstadosAceptacion, ","));

				for(int i = 0; i < reglas.size(); i++) {
					pw.println(reglas.get(i).toString());
					System.out.println(reglas.get(i).toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fichero) {
						fichero.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		}

	}

	//Obtiene el numero de columnas del GridPane
	private int getRowCount(GridPane pane) {

		int numRows = pane.getRowConstraints().size();
		for (int i = 0; i < pane.getChildren().size(); i++) {
			Node child = pane.getChildren().get(i);
			if (child.isManaged()) {
				Integer rowIndex = GridPane.getRowIndex(child);
				if(rowIndex != null){
					numRows = Math.max(numRows,rowIndex+1);
				}
			}
		}
		return numRows;

	}

	//Obtiene los objetos que estan en una fila y columna de un GridPane
	@SuppressWarnings("static-access")
	public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();

		for (Node node : childrens) {
			if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
				result = node;
				break;
			}
		}
		return result;
	}

	//Aqui termina---------------------------------------------------------

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

	public void volverAlPrincipio() {
		Parent pane = null;
		try {
			pane = (AnchorPane)FXMLLoader.load(getClass().getResource("app.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene nuevaEscena = new Scene(pane);
		Stage ventana = (Stage)panelPrincipal.getScene().getWindow();
		ventana.setScene(nuevaEscena);
	}

	public boolean validarReglas(String estadoActual, String entrada, String cimaPila, String estadoNuevo, String accion) {
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
		
		boolean validarAccion = true;
		int j = 1;
		for(int i = cimaPila.length() - 1; i >= 0 ; i--) {
			System.out.println(accion.charAt(accion.length() - j) + " != " + cimaPila.charAt(i) );
			if((accion.charAt(accion.length() - j) != cimaPila.charAt(i)) || accion.equals("#")) {
				validarAccion = false;
			}
			j++;
		}

		entradasValidadas = validarEstados && validarEntradas && validarAlfabetoPila && validarAccion;
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

		for(int i = 0; i < palabras.length; i++) {	
			list.add(0,palabras[i]);
		}
		objetosPila.setItems(list);
		panePila.setContent(objetosPila);
		objetosPila.setTranslateY(objetosPila.getTranslateY() -objetosPila.getFixedCellSize()* palabras.length);


	}

	public void borrarPila()  {
		list.remove(0);
		objetosPila.setTranslateY(objetosPila.getTranslateY() +objetosPila.getFixedCellSize());
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
			String[] r = new String[5];

			//Lee las reglas y las guarda
			while ((sCurrentLine = br.readLine()) != null) { 
				leerTexto();
				Regla regla = new Regla();
				r = sCurrentLine.split(",");
				try {
					regla.setEstadoActual(r[0]);
					regla.setEntrada(r[1]);
					regla.setCimaPila(r[2]);
					regla.setEstadoNuevo(r[3]);
					regla.setAccion(r[4]);
					reglas.add(regla);
				}
				catch (Exception e) {
				}
			}

			//if(!(contiene(listaEstados, r[0]) && contiene(listaAlfabeto, r[1]) && contiene(listaAlfabetoPila, r[2]) && contiene(listaEstados, r[3]))) {
			//Mensaje.mostrarAdvertencia("Se cambio la definicion formal, por favor revise las reglas nuevamente para evitar problemas con su automata.");
			//}
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

				//Proceso gráfico automático
				ArrayList<Regla> rules = automataControl.getReglasGraf();

				if(rules.size() > 0) {
					String mensaje = "Pasos para la aceptación: \n";
					for(int i = 0; i < automataControl.getReglasGraf().size(); i++) {
						mensaje += (i + 1) + ". <";
						mensaje += automataControl.getReglasGraf().get(i).toString();
						mensaje += ">\n";
					}
					Mensaje.mostrarMensaje(mensaje);
				}

				Timer t = new Timer();

				TimerTask tt = new TimerTask() {

					int i = 0;

					@Override
					public void run() {

						if( i >= rules.size()) {
							t.cancel();
						}

						if(rules.get(i).getAccion().equals("#")) {
							borrarPila();
						}else {
							String[] r = rules.get(i).getAccion().split("");
							dibujarPila(r);
						}

						i++;
					};

				};
				t.schedule(tt,1000,1000);		
			} else {
				Mensaje.mostrarError("No se marco si el automata acepta por pila vaci­a o estado de aceptacion");
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

	public String concatenarListas(String[] lista, String separador) {
		String palabra = "";
		for(int i = 0; i < lista.length; i++) {
			if(i == lista.length - 1) {
				palabra += lista[i].toString();
			}else {
				palabra = palabra + lista[i].toString() + separador;
			}
		}
		return palabra;
	}
	
}

