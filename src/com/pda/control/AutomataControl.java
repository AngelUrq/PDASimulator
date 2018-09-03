package com.pda.control;

import java.util.ArrayList;
import java.util.Stack;

import com.pda.entity.Automata;
import com.pda.entity.Regla;
import com.pda.view.Mensaje;

public class AutomataControl {

	private Automata definicion;

	private String cadena;

	private boolean aceptado;
	private ArrayList<Regla> reglasGraf;

	private boolean porEstadoFinal;
	private boolean porPilaVacia;

	public AutomataControl(Automata definicion, String cadena, boolean porEstadoFinal, boolean porPilaVacia) {
		this.definicion = definicion;
		this.aceptado = false;
		this.cadena = cadena;
		this.reglasGraf = new ArrayList<Regla>();
		this.porEstadoFinal = porEstadoFinal;
		this.porPilaVacia = porPilaVacia;
	}

	@SuppressWarnings("unchecked")
	public void simular() {	
		//Probamos para todos los estados iniciales
		for(int i = 0; i < definicion.getEstadosIniciales().size(); i++) {
			probarRegla(definicion.getEstadosIniciales().get(i), 0, (Stack<String>) definicion.getPila().clone(), new ArrayList<Regla>());
		}

		if(!this.aceptado) {
			Mensaje.mostrarError("Cadena no aceptada");
		}

		System.out.println("Simulación finalizada");
	}

	@SuppressWarnings("unchecked")
	private void probarRegla(String estado, int posicion, Stack<String> pila, ArrayList<Regla> camino) {	
		if(!this.aceptado) {
			String caracter = "";
			//El caracter actual
			if(posicion < cadena.length()) {
				caracter = Character.toString(cadena.charAt(posicion));
			}

			String estadoOriginal = estado;
			//Revisamos todas las reglas
			for (int i = 0; i < definicion.getReglas().size(); i++) {
				Stack<String> pilaCopia = (Stack<String>) pila.clone(); 
				ArrayList<Regla> caminoCopia = (ArrayList<Regla>)camino.clone();
				estado = estadoOriginal;

				Regla regla = definicion.getReglas().get(i);
				
				//Comprobamos si es la regla adecuada 
				if(regla.getEstadoActual().equals(estado)) {
					//Comprobamos el tope de la pila
					if(regla.getCimaPila().equals(pilaCopia.get(pilaCopia.size() - 1)) || regla.getCimaPila().equals("Z")) {	
						//Si la entrada está bien probamos una nueva regla
						if(regla.getEntrada().equals(caracter)) {
							estado = regla.getEstadoNuevo();
							//Apilamos o desapilamos
							if(regla.getAccion().equals("#")) {
								pilaCopia.remove(pilaCopia.size() - 1);
							} else if(!regla.getAccion().equals("Z")){
								for(int j = 0; j < regla.getAccion().length(); j++) {
									pilaCopia.add(Character.toString(regla.getAccion().charAt(j)));	
								}
							}
							caminoCopia.add(regla);
							probarRegla(estado, posicion + 1, (Stack<String>)pilaCopia.clone(), (ArrayList<Regla>) caminoCopia.clone());
						} else if(regla.getEntrada().equals("#")) {
							estado = regla.getEstadoNuevo();
							//Apilamos o desapilamos
							if(regla.getAccion().equals("#")) {
								pilaCopia.remove(pilaCopia.size() - 1);
							} else if(!regla.getAccion().equals("Z")){
								for(int j = 0; j < regla.getAccion().length(); j++) {
									pilaCopia.add(Character.toString(regla.getAccion().charAt(j)));	
								}
							}
							caminoCopia.add(regla);
							probarRegla(estado, posicion, (Stack<String>)pilaCopia.clone(), (ArrayList<Regla>) caminoCopia.clone());
						}
					}
				}
			}
		}

		//Comprobamos si ya leímos toda la cadena
		if(posicion == cadena.length() || (posicion == 0 && cadena.equals("#"))) {
			if(porEstadoFinal && contiene(definicion.getEstadosAceptacion(), estado)) {
				//Aceptación por estado final
				this.aceptado = true;
				Mensaje.mostrarMensaje("Cadena aceptada por estado de aceptacion\n");
				this.reglasGraf = camino;
			} else if (pila.size() - 1 >= 0 & porPilaVacia && pila.get(pila.size() - 1).equals(definicion.getSimboloInicial())) {
				//Aceptación por pila vacía
				this.aceptado = true;
				Mensaje.mostrarMensaje("Cadena aceptada por pila vacía");
				this.reglasGraf = camino;
			} 
		} 
	}

	//Método para verificar si un elemento pertenece a un ArrayList
	private boolean contiene(ArrayList<String> lista, String elemento) {
		boolean pertenece = false;

		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).equals(elemento)) {
				pertenece = true;
			}
		}

		return pertenece;
	}

	public ArrayList<Regla> getReglasGraf() {
		return reglasGraf;
	}

}