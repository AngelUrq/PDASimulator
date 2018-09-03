package com.pda.entity;

public class Regla {

	private String estadoActual;
	private String entrada;
	private String cimaPila;
	private String estadoNuevo;
	private String accion;
	
	
	public Regla() {
		
	}
	
	public Regla(String estActual, String entrada, String cima, String estNuevo, String accion) {
		this.estadoActual = estActual;
		this.entrada = entrada;
		this.cimaPila = cima;
		this.estadoNuevo = estNuevo;
		this.accion = accion;
	}
	
	

	public String getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}

	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getCimaPila() {
		return cimaPila;
	}

	public void setCimaPila(String cimaPila) {
		this.cimaPila = cimaPila;
	}

	public String getEstadoNuevo() {
		return estadoNuevo;
	}

	public void setEstadoNuevo(String estadoNuevo) {
		this.estadoNuevo = estadoNuevo;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	@Override
	public String toString() {
		return estadoActual + "," + entrada + "," + cimaPila
				+ "," + estadoNuevo + "," + accion;
	}
	
	
}
