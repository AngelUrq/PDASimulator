package com.pda.control;

import java.util.ArrayList;
import java.util.Stack;

import com.pda.entity.Regla;

public class Automata {
	
	private ArrayList<Regla> reglas;
	private String estadoActual;
	private ArrayList<String> estados;
	private ArrayList<String> alfabetoEntrada;
	private ArrayList<String> estadosIniciales;
	private ArrayList<String> alfabetoPila;
	private ArrayList<String> estadosAceptacion;
	private Stack pila;
	
	public Automata(ArrayList<Regla> reglas, String estadoActual, ArrayList<String> estados,
			ArrayList<String> alfabetoEntrada, ArrayList<String> estadosIniciales, ArrayList<String> alfabetoPila,
			ArrayList<String> estadosAceptacion, Stack pila) {
		this.reglas = reglas;
		this.estadoActual = estadoActual;
		this.estados = estados;
		this.alfabetoEntrada = alfabetoEntrada;
		this.estadosIniciales = estadosIniciales;
		this.alfabetoPila = alfabetoPila;
		this.estadosAceptacion = estadosAceptacion;
		this.pila = pila;
	}
	
	public ArrayList<Regla> getReglas() {
		return reglas;
	}
	
	public void setReglas(ArrayList<Regla> reglas) {
		this.reglas = reglas;
	}
	
	public String getEstadoActual() {
		return estadoActual;
	}
	
	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}
	
	public ArrayList<String> getEstados() {
		return estados;
	}
	
	public void setEstados(ArrayList<String> estados) {
		this.estados = estados;
	}
	
	public ArrayList<String> getAlfabetoEntrada() {
		return alfabetoEntrada;
	}
	
	public void setAlfabetoEntrada(ArrayList<String> alfabetoEntrada) {
		this.alfabetoEntrada = alfabetoEntrada;
	}
	
	public ArrayList<String> getEstadosIniciales() {
		return estadosIniciales;
	}
	
	public void setEstadosIniciales(ArrayList<String> estadosIniciales) {
		this.estadosIniciales = estadosIniciales;
	}
	
	public ArrayList<String> getAlfabetoPila() {
		return alfabetoPila;
	}
	
	public void setAlfabetoPila(ArrayList<String> alfabetoPila) {
		this.alfabetoPila = alfabetoPila;
	}
	
	public ArrayList<String> getEstadosAceptacion() {
		return estadosAceptacion;
	}
	
	public void setEstadosAceptacion(ArrayList<String> estadosAceptacion) {
		this.estadosAceptacion = estadosAceptacion;
	}
	
	public Stack getPila() {
		return pila;
	}
	
	public void setPila(Stack pila) {
		this.pila = pila;
	}
}
