package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import sample.interfaces.impl.*;
import sample.models.Cicle;
import sample.models.Modul;
import sample.models.UnitatFormativa;
import sample.utils.Data;
import sample.utils.Parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

	@FXML	private ComboBox<Cicle> cmbCicles;
	@FXML	private Accordion acModul;
	@FXML   private Button bCSV;

	private ObservableList<Cicle> ciclesMenu;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		getAllCicles();		// Importa todos los CICLES actuales de la DB.

		bCSV.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Importar cicles");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("CSV", "*.csv")
					);

			File csvFile = fileChooser.showOpenDialog(bCSV.getScene().getWindow());
			if (csvFile != null) {
				try {
					JSONArray jsonArray = Parser.parseCiclesCSV(csvFile);
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../windows/importCSV.fxml"));
					Stage stage = new Stage();
					Parent root = loader.load();
					ImportCSVController importCSVController = loader.getController();
					importCSVController.setImportedJSON(jsonArray);
					importCSVController.setMainStage((Stage) bCSV.getScene().getWindow());
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	public void getAllCicles(){

		/** 
		 * getAllCicles()
		 * Obtiene la información ya parseada de la API y la añade al ComboBox
		*/ 

		List<Cicle> ciclesList = Data.cicleManager.getAllCicles();
		if (ciclesList != null){
			ciclesMenu = FXCollections.observableArrayList();
			ciclesMenu.addAll(ciclesList);
			cmbCicles.setItems(ciclesMenu);
		}
	}
	/**
	 *  getModuls()
	 *	Al pusar en un CICLE se obtienen todos su MODULS y se añaden
	 */
	@FXML
	void getModuls(ActionEvent event) {

		
		
		acModul.getPanes().clear();

		String idCicle = cmbCicles.getSelectionModel().getSelectedItem().getIdCicle();
		List<Modul> modulsList = Data.modulManager.getAllModulsByCicle(idCicle);
		for (Modul m : modulsList) {
			ListView<UnitatFormativa> ufListView = new ListView<>();
			TitledPane pane = new TitledPane(m.getNomModul(), ufListView);
			pane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) ->{
				if (isNowExpanded){
					List<UnitatFormativa> ufList = Data.ufManager.getAllUFSFromCicleByModul(idCicle, modulsList.indexOf(m));
					ObservableList<UnitatFormativa> ufMenu = FXCollections.observableArrayList();
					ufMenu.addAll(ufList);
					ufListView.getItems().setAll(ufMenu);
				}
			});
			acModul.getPanes().add(pane);
		}
	}

}
