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
import org.json.JSONObject;

import sample.interfaces.impl.UnitatFormativaImpl;
import sample.models.Cicle;
import sample.models.Modul;
import sample.models.UnitatFormativa;
import sample.utils.ConnAPI;
import sample.utils.Data;
import sample.utils.Parser;

import java.io.File;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

	@FXML	private ComboBox<Cicle> cmbCicles;
	@FXML	private Accordion acModul;
	@FXML   private Button bCSVCicles;
	private Cicle cicle;
	private Modul modul;

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
		cicle = null;

		Dialog dialog = new Dialog();
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
				Modul modul = addModulDialog();
				if (modul != null)
					listModulsCicle.getItems().add(modul);
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

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == okButtonType) {
			Cicle cicle = new Cicle();
			cicle.setCodiCicle(idCicle.getText());
			cicle.setNomCicle(nameCicle.getText());
			cicle.setCodiAdaptacioCurs(codiAdapCicle.getText());
			cicle.setHoresCicle(Integer.parseInt(hoursCicle.getText()));
			cicle.setDataIniciCicle(Date.from(dataIniciCicle.getValue().atStartOfDay().toInstant(ZoneOffset.ofHours(2))));
			cicle.setModuls(listModulsCicle.getItems());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("nom", cicle.getNomCicle());
			jsonObject.put("hores", cicle.getHoresCicle());
			jsonObject.put("codi", cicle.getCodiCicle());
			jsonObject.put("codiAdaptacioCurricular", cicle.getCodiAdaptacioCurs());
			jsonObject.put("dataInici", cicle.getDataIniciCicle().toString());
			JSONArray modulsJsonArray = new JSONArray();
			
			for (Modul modul : cicle.getModuls()) {
				JSONObject modulJsonObject = new JSONObject();
				modulJsonObject.put("nomModul", modul.getNomModul());
				modulJsonObject.put("codiModul", modul.getCodiModul());
				modulJsonObject.put("duradaMinModul", modul.getDuradaMinModul());
				modulJsonObject.put("duradaMaxModul", modul.getDuradaMaxModul());
				modulJsonObject.put("dataIniciModul", modul.getDataIniciModul().toString());
				modulJsonObject.put("dataFiModul", modul.getDataFiModul().toString());
				modulsJsonArray.put(modulJsonObject);
			}
			jsonObject.put("moduls", modulsJsonArray);

			JSONObject requestJSON = new JSONObject();
			requestJSON.put("data", jsonObject.toString());

			ConnAPI connAPI = new ConnAPI("/api/cicles/insertOne", "POST", false);
			connAPI.setData(requestJSON);
			connAPI.establishConn();

			int status = connAPI.getStatus();
			Alert alert;
			switch (status){
				case 0:
					System.out.println("[DEBUG] - Error al contactar con la API...");
					break;

				case 200:
					System.out.println("[DEBUG] - Cicle afegit correctament!");
					cmbCicles.getItems().add(cicle);
					alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("MatriculesAPP | DESKTOP");
					alert.setHeaderText("Cicle afegit correctament!");
					alert.showAndWait();
					break;

				case 500:
					System.out.println("[DEBUG] - Error al afegir el cicle...");
					alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("MatriculesAPP | DESKTOP");
					alert.setHeaderText("Error al afegir el cicle...!");
					alert.showAndWait();
					break;
			}

		}
	}

	@FXML
	void deleteCicle(ActionEvent event) {
		String id  = cmbCicles.getSelectionModel().getSelectedItem().getIdCicle();

		JSONObject requestJSON = new JSONObject();
		requestJSON.put("id", id);

		ConnAPI connAPI = new ConnAPI("/api/cicles/deleteOne", "DELETE", false);
		connAPI.setData(requestJSON);
		connAPI.establishConn();

		int status = connAPI.getStatus();
		Alert alert;
		switch (status){
			case 0:
				System.out.println("[DEBUG] - Error al contactar con la API...");
				break;

			case 200:
				System.out.println("[DEBUG] - Cicle eliminat correctament!");
				cmbCicles.getItems().remove(cmbCicles.getSelectionModel().getSelectedIndex());
				alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("MatriculesAPP | DESKTOP");
				alert.setHeaderText("Cicle eliminat correctament!");
				alert.showAndWait();
				break;

			case 500:
				System.out.println("[DEBUG] - Error al eliminar el cicle...");
				alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("MatriculesAPP | DESKTOP");
				alert.setHeaderText("Error al eliminar el cicle...!");
				alert.showAndWait();
				break;
		}
	}

	private Modul addModulDialog() {
		modul = null;

		Dialog dialog = new Dialog();
		dialog.setTitle("MatriculesAPP | DESKTOP");
		dialog.setHeaderText("Nom i dades del mòdul");

		ButtonType okButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		grid.add(new Label("Nom:"), 0, 0);
		TextField nameModul = new TextField();
		nameModul.setPromptText("Nom del mòdul");
		grid.add(nameModul, 1, 0);

		grid.add(new Label("Codi:"), 0, 1);
		TextField idModul = new TextField();
		idModul.setPromptText("Codi del mòdul");
		grid.add(idModul, 1, 1);

		grid.add(new Label("Durada mínima:"), 0, 2);
		TextField duradaMinModul = new TextField();
		duradaMinModul.setPromptText("Durada mínima del mòdul");
		grid.add(duradaMinModul, 1, 2);

		grid.add(new Label("Durada màxima:"), 0, 3);
		TextField duradaMaxModul = new TextField();
		duradaMaxModul.setPromptText("Durada màxima del mòdul");
		grid.add(duradaMaxModul, 1, 3);

		grid.add(new Label("Data d'inici:"), 0, 4);
		DatePicker dataIniciModul = new DatePicker();
		dataIniciModul.setPromptText("Data d'inici del mòdul");
		grid.add(dataIniciModul, 1, 4);

		grid.add(new Label("Data de fi:"), 0, 5);
		DatePicker dataFiModul = new DatePicker();
		dataFiModul.setPromptText("Data de fi del mòdul");
		grid.add(dataFiModul, 1, 5);

		grid.add(new Label("Unitats formatives:"), 0, 6);
		ListView<UnitatFormativa> listUFsModul= new ListView<UnitatFormativa>();
		listUFsModul.setPrefHeight(150);
		grid.add(listUFsModul, 1, 6);

		GridPane modulGridPane = new GridPane();		

		Button addUfButton = new Button("Afegir");
		addUfButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				UnitatFormativa uf = new UnitatFormativa("1234", "Introducció als bucles for", 70, new Date(), 1);
				listUFsModul.getItems().add(uf);
			}
		});
		modulGridPane.add(addUfButton, 0, 1);

		Button deleteUfButton = new Button("Suprimir");
		deleteUfButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				listUFsModul.getItems().remove(listUFsModul.getSelectionModel().getSelectedItem());
			}
		});
		modulGridPane.add(deleteUfButton, 0, 2);

		grid.add(modulGridPane, 2, 6);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				Modul modulLocal = new Modul();
				if (!idModul.getText().isBlank())
					modulLocal.setCodiModul(idModul.getText());
				if (!nameModul.getText().isBlank())
					modulLocal.setNomModul(nameModul.getText());
				if (!duradaMinModul.getText().isBlank())
					modulLocal.setDuradaMinModul(Integer.parseInt(duradaMinModul.getText()));
				if (!duradaMaxModul.getText().isBlank())
					modulLocal.setDuradaMaxModul(Integer.parseInt(duradaMaxModul.getText()));
				if (dataIniciModul.getValue() != null)
					modulLocal.setDataIniciModul(Date.from(dataIniciModul.getValue().atStartOfDay().toInstant(ZoneOffset.ofHours(2))));
				if (dataFiModul.getValue() != null)
					modulLocal.setDataFiModul(Date.from(dataFiModul.getValue().atStartOfDay().toInstant(ZoneOffset.ofHours(2))));
				modulLocal.setUnitatsFormatives(listUFsModul.getItems());
				return new Pair<>("modul", modulLocal);
			}
			return dialogButton;
		});

		Optional<Pair<String, Modul>> result = dialog.showAndWait();
		result.ifPresent(action -> {
			modul = action.getValue();
		});

		return modul;
	}

	/**
	 * getAllCicles()
	 * Obtiene la información ya parseada de la API y la añade al ComboBox
	 */
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
