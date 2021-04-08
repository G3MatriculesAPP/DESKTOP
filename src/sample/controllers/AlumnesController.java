package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.models.Cicle;
import sample.utils.Data;
import sample.utils.Parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AlumnesController implements Initializable {

    @FXML    private ComboBox<Cicle> cmbCicles;
    @FXML    private Button bCSVAlumnes;
    @FXML    private ListView<String> listAlumnes;

    private ObservableList<Cicle> ciclesMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getAllCicles();		// Importa todos los CICLES actuales de la DB.

        bCSVAlumnes.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importar alumnes admesos");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV", "*.csv")
            );
            File csvFile = fileChooser.showOpenDialog(bCSVAlumnes.getScene().getWindow());
            if (csvFile != null) {
                try {
                    JSONArray jsonArray = Parser.parseAlumnesCSV(csvFile);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../windows/importCSVAlumnes.fxml"));
                    Stage stage = new Stage();
                    Parent root = loader.load();
                    ImportCSVAlumnesController importCSVController = loader.getController();
                    importCSVController.setImportedJSON(jsonArray);
                    importCSVController.setMainStage((Stage)bCSVAlumnes.getScene().getWindow());
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

    @FXML
    void getAlumnesByCicle(ActionEvent event){

        listAlumnes.getItems().clear();
        JSONArray arrayJSON = Data.alumneManager.getAlumnesByCourse(cmbCicles.getSelectionModel().getSelectedItem().getNomCicle());
        for (int i = 0; i < arrayJSON.length(); i++){
            JSONObject json = arrayJSON.getJSONObject(i);
            listAlumnes.getItems().add(json.getString("nom") + " " + json.getString("primerCognom") + " " + json.getString("segonCognom") + " \nDNI: " + json.getString("dni"));
        }

    }


}
