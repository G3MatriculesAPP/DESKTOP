package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import sample.interfaces.impl.CicleImpl;
import sample.models.Cicle;
import sample.models.Modul;

import java.io.File;
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

	@FXML
	private Button bCCSV;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		
		CicleImpl cicleManager = new CicleImpl();
		
		List <Cicle> patata = cicleManager.getAllCicles();
		ObservableList<Cicle> cicles = FXCollections.observableArrayList();
		cicles.addAll(patata);
		cBCicles.setItems(cicles);

		List <Modul> moniato = MocaModul();

		for(int i=0; i<moniato.size(); i++) {

			TitledPane pane1 = new TitledPane( moniato.get(i).toString() , new Label("UFs"));


			acModuls.getPanes().add(pane1);


		}
		





	}
	
	@FXML
    void busc(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar csv ciclos");

        // Agregar filtros para facilitar la busqueda
        fileChooser.getExtensionFilters().addAll(
                
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );

       
        
    }
	
	public List<Cicle> MocaCicles() {
		ArrayList <Cicle> patata = new ArrayList<Cicle>();
		for(int i=0; i<10; i++) {

			java.util.Date fecha = new Date();

			Cicle c= new Cicle( "hola" , "nom".concat( Integer.toString(i+1))," ", i , fecha, " ");
			patata.add(c);

	
		}
		List <Cicle> Kartofen=patata; 
		return Kartofen;
	}

	public List<Modul> MocaModul(){
		ArrayList <Modul> moniato = new ArrayList<Modul>();
		for(int i=0; i<10; i++) {

			java.util.Date fecha = new Date();

			Modul c= new Modul(  );
			moniato.add(c);

		}
		List <Modul> subkartoffel = moniato;
		return subkartoffel;
	}

}
