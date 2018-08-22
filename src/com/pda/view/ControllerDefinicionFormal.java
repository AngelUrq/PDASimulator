package com.pda.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
		
		TextField[] txtArray = new TextField[6];
		
		txtArray[0] = txt1;
		txtArray[1] = txt2;
		txtArray[2] = txt3;
		txtArray[3] = txt4;
		txtArray[4] = txt5;
		txtArray[5] = txt6;

		
		String Filename = "Aqui_pongan_el_nombre_que_quieran.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		for(int i = 0; i < txtArray.length; i++) {
			
			try {

				String content = txtArray[i].getText();

				fw = new FileWriter(Filename);
				bw = new BufferedWriter(fw);
				bw.write(content);
				bw.newLine();


				System.out.println("Done" + " " + txtArray[i].getText());

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
			}			
		}
		
	}
	
}
