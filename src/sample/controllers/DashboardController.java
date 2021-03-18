package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.models.Cicle;
import sample.models.Modul;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
	@FXML
	private ComboBox<Cicle> cBCicles;

	@FXML
	private Accordion acModuls;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		List <Cicle> patata = MocaCicles();
		ObservableList<Cicle> cicles = FXCollections.observableArrayList();
		cicles.addAll(patata);
		cBCicles.setItems(cicles);
		
		List <Modul> moniato = MocaModul();
		
		for(int i=0; i<moniato.size(); i++) {
			
			TitledPane pane1 = new TitledPane( moniato.get(i).toString() , new Label("UFs"));
	      

	        acModuls.getPanes().add(pane1);
	        
			
		}
		
		
		
	}
	
	public List<Cicle> MocaCicles() {
		ArrayList <Cicle> patata = new ArrayList<Cicle>();
		for(int i=0; i<10; i++) {
			
			java.util.Date fecha = new Date();
			
			Cicle c= new Cicle( "hola" , "nom".concat( Integer.toString(i+1)), i , fecha, 1 );
			patata.add(c);
			
		}
		List <Cicle> Kartofen=patata; 
		return Kartofen;
	}
	
	public List<Modul> MocaModul(){
		ArrayList <Modul> moniato = new ArrayList<Modul>();
		for(int i=0; i<10; i++) {

			java.util.Date fecha = new Date();

			Modul c= new Modul( "hola" , "M0".concat( Integer.toString(i+1)), i , fecha );
			moniato.add(c);

		}
		List <Modul> subkartoffel = moniato;
		return subkartoffel;
	}

}
