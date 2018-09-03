package com.pda.control;

import java.util.ArrayList;
import java.util.Stack;

import com.pda.entity.Automata;
import com.pda.entity.Regla;

public class AutomataControl {

	private Automata definicion;

	private String cadena;

	private boolean aceptado;

	public AutomataControl(Automata definicion, String cadena) {
		this.definicion = definicion;
		this.aceptado = false;
		this.cadena = cadena;
	}

	public void simular() {
		//Probamos para todos los estados iniciales
		for(int i = 0; i < definicion.getEstadosIniciales().size(); i++) {
			probarRegla(definicion.getEstadosIniciales().get(i), 0, (Stack<String>) definicion.getPila().clone());
		}

		System.out.println("Simulación finalizada");
		System.exit(0);
	}

	private void probarRegla(String estado, int posicion, Stack<String> pila) {	
		//Comprobamos si ya leímos toda la cadena
		if(posicion == cadena.length()) {
			if(contiene(definicion.getEstadosAceptacion(), estado)) {
				//Aceptación por estado final
				this.aceptado = true;
				System.out.println("Cadena aceptada por estado de aceptación");
			} else if (definicion.getEstadosAceptacion().size() == 0 && pila.get(pila.size() - 1).equals("z0")) {
				//Aceptación por pila vacía
				this.aceptado = true;
				System.out.println("Cadena aceptada por pila vacía");
			} 
		} 

		String caracter = "";
		//El caracter actual
		if(posicion < cadena.length()) {
			caracter = Character.toString(cadena.charAt(posicion));
		}

		String estadoOriginal = estado;
		//Revisamos todas las reglas
		for (int i = 0; i < definicion.getReglas().size(); i++) {
			Stack<String> pilaCopia = (Stack<String>) pila.clone(); 
			estado = estadoOriginal;

			Regla regla = definicion.getReglas().get(i);

			//Comprobamos si es la regla adecuada 
			if(regla.getEstadoActual().equals(estado)) {
				if(regla.getCimaPila().equals(pilaCopia.get(pilaCopia.size() - 1)) || regla.getCimaPila().equals("Z")) {	
					estado = regla.getEstadoNuevo();
					//Apilamos o desapilamos
					if(regla.getAccion().equals("#")) {
						pilaCopia.remove(pilaCopia.size() - 1);
						
					} else if(!regla.getAccion().equals("Z")){
						for(int j = 0; j < regla.getAccion().length(); j++) {
							pilaCopia.add(Character.toString(regla.getAccion().charAt(j)));	
						}
					}
					//Si la entrada está bien probamos una nueva regla
					if(regla.getEntrada().equals(caracter)) {
						//Probamos el siguiente caracter
						probarRegla(estado, posicion + 1, (Stack<String>)pilaCopia.clone());
					} else if(regla.getEntrada().equals("#")) {
						//Comprobamos el tope de la pila
						probarRegla(estado, posicion, (Stack<String>)pilaCopia.clone());
					}
				}
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

}