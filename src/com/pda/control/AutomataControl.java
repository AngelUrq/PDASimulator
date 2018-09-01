package com.pda.control;

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
		for(int i = 0; i < definicion.getEstadosIniciales().size(); i++) {
			probarRegla("q0", 0, (Stack<String>) definicion.getPila().clone());
		}
	}
	
	public void probarRegla(String estado, int posicion, Stack<String> pila) {
		String caracter = Character.toString(cadena.charAt(0));
		
		for (Regla regla: definicion.getReglas()) {
			//if(regla.getEntrada() == estado && regla.getAccion() == ) {
				
			//}
		}
	}
	
}