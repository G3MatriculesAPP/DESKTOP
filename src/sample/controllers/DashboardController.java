package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONArray;
import sample.interfaces.impl.CicleImpl;
import sample.interfaces.impl.ModulImpl;
import sample.interfaces.impl.UnitatFormativaImpl;
import sample.models.Cicle;
import sample.models.Modul;
import sample.models.UnitatFormativa;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

	@FXML	private ComboBox<Cicle> cmbCicles;
	@FXML	private Accordion acModul;
	@FXML   private Button bCSV;
	
	CicleImpl cicleManager = new CicleImpl();
	ModulImpl modulManager = new ModulImpl();
	UnitatFormativaImpl ufManager = new UnitatFormativaImpl();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		getAllCicles();
		
		bCSV.setOnAction(event -> {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Importar cicles");
	        fileChooser.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("CSV", "*.csv")
	        );

	        File csvFile = fileChooser.showOpenDialog(bCSV.getScene().getWindow());


			JSONArray arrayJSON = new JSONArray();

	        if (csvFile != null) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../windows/importCSV.fxml"));
					Stage stage = new Stage();
					Parent root = loader.load();
					ImportCSVController importCSVController = loader.getController();
					importCSVController.setImportedJSON(arrayJSON);
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	private void getAllCicles(){

		// getAllCicles()
		// Obtiene la información ya parseada de la API y la añade al ComboBox

		List<Cicle> ciclesList = cicleManager.getAllCicles();
		ObservableList<Cicle> ciclesMenu = FXCollections.observableArrayList();
		ciclesMenu.addAll(ciclesList);
		cmbCicles.setItems(ciclesMenu);

	}

	@FXML
	void getModuls(ActionEvent event){

		// getModuls()
		// Al pusar en un CICLE se obtienen todos su MODULS y se añaden

		acModul.getPanes().clear();

		String idCicle = cmbCicles.getSelectionModel().getSelectedItem().getIdCicle();
		List<Modul> modulsList = modulManager.getAllModulsByCicle(idCicle);
		for (Modul m : modulsList) {
			ListView<UnitatFormativa> ufListView = new ListView<>();
			TitledPane pane = new TitledPane(m.getNomModul(), ufListView);
			pane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) ->{
				if (isNowExpanded){
					List<UnitatFormativa> ufList = ufManager.getAllUFSFromCicleByModul(idCicle, modulsList.indexOf(m));
					ObservableList<UnitatFormativa> ufMenu = FXCollections.observableArrayList();
					ufMenu.addAll(ufList);
					ufListView.getItems().setAll(ufMenu);
				}
			});
			acModul.getPanes().add(pane);
		}




	}

}
