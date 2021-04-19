package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONArray;
import sample.models.Cicle;
import sample.models.Modul;
import sample.models.UnitatFormativa;
import sample.utils.Data;
import sample.utils.Parser;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

	@FXML	private ComboBox<Cicle> cmbCicles;
	@FXML	private Accordion acModul;
	@FXML   private Button bCSVCicles;

	private ObservableList<Cicle> ciclesMenu;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		getAllCicles();		// Importa todos los CICLES actuales de la DB.

		bCSVCicles.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Importar cicles");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("CSV", "*.csv")
					);
			File csvFile = fileChooser.showOpenDialog(bCSVCicles.getScene().getWindow());
			if (csvFile != null) {
				try {
					JSONArray jsonArray = Parser.parseCiclesCSV(csvFile);
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../windows/importCSVCicles.fxml"));
					Stage stage = new Stage();
					Parent root = loader.load();
					ImportCSVCiclesController importCSVController = loader.getController();
					importCSVController.setImportedJSON(jsonArray);
					importCSVController.setMainStage((Stage)bCSVCicles.getScene().getWindow());
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("MatriculesAPP | DESKTOP");
					alert.setHeaderText("Fichero .CSV no válido....");
					alert.showAndWait();
				}
			}
		});


	}

	@FXML
	private void addCicle(ActionEvent event) {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("MatriculesAPP | DESKTOP");
		dialog.setHeaderText("Nom i dades del cicle");

		ButtonType okButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		grid.add(new Label("Nom:"), 0, 0);
		TextField nameCicle = new TextField();
		nameCicle.setPromptText("Nom del cicle");
		grid.add(nameCicle, 1, 0);

		grid.add(new Label("Codi:"), 0, 1);
		TextField idCicle = new TextField();
		idCicle.setPromptText("Codi del cicle");
		grid.add(idCicle, 1, 1);
		
		grid.add(new Label("Codi d'adaptació curricular:"), 0, 2);
		TextField codiAdapCicle = new TextField();
		codiAdapCicle.setPromptText("Codi d'adaptació curricular del cicle");
		grid.add(codiAdapCicle, 1, 2);

		grid.add(new Label("Hores:"), 0, 3);
		TextField hoursCicle = new TextField();
		hoursCicle.setPromptText("Duració (en hores) del cicle");
		grid.add(hoursCicle, 1, 3);

		grid.add(new Label("Data d'inici:"), 0, 4);
		DatePicker dataIniciCicle = new DatePicker();
		dataIniciCicle.setPromptText("Data d'inici del cicle");
		grid.add(dataIniciCicle, 1, 4);

		grid.add(new Label("Mòduls:"), 0, 5);
		ListView<Modul> listModulsCicle = new ListView<Modul>();
		listModulsCicle.setPrefHeight(150);
		grid.add(listModulsCicle, 1, 5);
		
		GridPane modulGridPane = new GridPane();		

		Button addModulButton = new Button("Afegir");
		addModulButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Reemplazar mockups por un nuevo Dialog donde el usuario pueda introducir Modulos
				Modul modul1 = new Modul("M01", "Programació avançada", 60, 60, new Date(), new Date());
				listModulsCicle.getItems().add(modul1);
				
				Modul modul2 = new Modul("M02", "Bases de dades", 60, 60, new Date(), new Date());
				listModulsCicle.getItems().add(modul2);
				
				Modul modul3 = new Modul("M03", "Sistemes operatius", 60, 60, new Date(), new Date());
				listModulsCicle.getItems().add(modul3);
			}
		});
		modulGridPane.add(addModulButton, 0, 1);
		
		Button deleteModulButton = new Button("Suprimir");
		deleteModulButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				listModulsCicle.getItems().remove(listModulsCicle.getSelectionModel().getSelectedItem());
			}
		});
		modulGridPane.add(deleteModulButton, 0, 2);
		
		grid.add(modulGridPane, 2, 5);

		dialog.getDialogPane().setContent(grid);
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType)
				return new Pair<>(nameCicle.getText(), idCicle.getText());
			return null;
		});

		dialog.show();
	}

	/**
	 * getAllCicles()
	 * Obtiene la información ya parseada de la API y la añade al ComboBox
	 */
	@SuppressWarnings("unchecked")
	public void getAllCicles(){

		if (Data.ciclesList != null){
			ciclesMenu = FXCollections.observableArrayList();
			ciclesMenu.addAll(Data.ciclesList);
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
