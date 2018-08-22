package com.pda.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class ControllerDefinicionFormal {

	
	@FXML  TextField txt1;
	
	@FXML  TextField txt2;
	
	@FXML  TextField txt3;
	@FXML  TextField txt4;
	@FXML  TextField txt5;
	@FXML  TextField txt6;



	@FXML  Button btnComenzar;

	
	@FXML	
	public void btnComenzarPresionado(ActionEvent event) throws IOException {
		
		
		System.out.println("Eudaimonia!");
	}
	
}
