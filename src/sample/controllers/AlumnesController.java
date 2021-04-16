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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AlumnesController implements Initializable {

    @FXML    private ComboBox<Cicle> cmbCicles;
    @FXML    private Button bCSVAlumnes;
    @FXML    private ListView<String> listAlumnes;
    @FXML    private VBox vBoxData;
    @FXML    private GridPane gridPaneData, gpDireccio, gpCentreProcedencia, gpEnsenyament, gpCentre, gpConvocatoria;
    @FXML    private TitledPane acTutors;

    private JSONArray arrayJSON;
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
        arrayJSON = Data.alumneManager.getAlumnesByCourse(cmbCicles.getSelectionModel().getSelectedItem().getNomCicle());
        for (int i = 0; i < arrayJSON.length(); i++){
            JSONObject json = arrayJSON.getJSONObject(i);
            listAlumnes.getItems().add(json.getString("nom") + " " + json.getString("primerCognom") + " " + json.getString("segonCognom") + " \nDNI: " + json.getString("dni"));
        }
    }

    @FXML
    void getDataAlumne(MouseEvent event) {

        int pos = listAlumnes.getSelectionModel().getSelectedIndex();
        JSONObject alumneData = Data.alumneManager.getAlumneData(arrayJSON.getJSONObject(pos).getString("_id"));
        transformData(alumneData);
    }

    private void transformData(JSONObject alumneData){

        vBoxData.setVisible(true);
        if (alumneData != null){
            gridPaneData.add(new TextField(alumneData.getString("nom")), 0, 0, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("primerCognom")), 1, 0, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("segonCognom")), 2, 0, 1, 1);

            gridPaneData.add(new TextField(alumneData.getString("telefon")), 0, 1, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("email")), 1, 1, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("nacionalitat")), 2, 1, 1, 1);

            gridPaneData.add(new DatePicker(), 0, 2, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("paisNaixement")), 1, 2, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("municipiNaixement")), 2, 2, 1, 1);

            String[] tipusDocument = new String[]{"DNI", "NIE"};
            ChoiceBox cbDoc = new ChoiceBox();
            cbDoc.getItems().addAll(tipusDocument);
            cbDoc.getSelectionModel().select(alumneData.getInt("tipusDocument"));
            gridPaneData.add(cbDoc, 0, 3, 1, 1);
            if (cbDoc.getSelectionModel().getSelectedIndex() == 0)
                gridPaneData.add(new TextField(alumneData.getString("dni")), 1, 3, 1, 1);
            else if(cbDoc.getSelectionModel().getSelectedIndex() == 1)
                gridPaneData.add(new TextField(alumneData.getString("nie")), 1, 3, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("idRALC")), 2, 3, 1, 1);

            String[] listSexes = new String[]{"HOME", "DONA"};
            ChoiceBox cbSexe = new ChoiceBox();
            cbSexe.getItems().addAll(listSexes);
            if (alumneData.getString("sexe").equals("Home"))
                cbSexe.getSelectionModel().select(0);
            else if(alumneData.getString("sexe").equals("Dona"))
                cbSexe.getSelectionModel().select(1);

            gridPaneData.add(cbSexe, 0, 4, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("tipusAlumne")), 1, 4, 1, 1);
            gridPaneData.add(new TextField(alumneData.getString("codiCentreAssignat")), 2, 4, 1, 1);

            // GRIDPANE - TUTORS

            JSONArray arrayTutors = alumneData.getJSONArray("tutors");
            VBox vBoxTutors = new VBox();
            vBoxTutors.setSpacing(10);
            for (int i = 0; i < arrayTutors.length(); i++){
                GridPane gpTutor = new GridPane();
                JSONObject tutorJSON = arrayTutors.getJSONObject(i);
                gpTutor.add(new TextField(tutorJSON.getString("primerCognom")), 0, 0, 1, 1);
                if (tutorJSON.isNull("segonCognom"))
                    tutorJSON.put("segonCognom", "");
                gpTutor.add(new TextField(tutorJSON.getString("segonCognom")), 1, 0, 1, 1);
                gpTutor.add(new TextField(tutorJSON.getString("tipusDocument")), 2, 0, 1, 1);

                gpTutor.add(new TextField(tutorJSON.getString("nom")), 0, 1, 1, 1);
                gpTutor.add(new TextField(tutorJSON.getString("numDocument")), 1, 1, 1, 1);
                vBoxTutors.getChildren().add(gpTutor);
            }

            acTutors.setContent(vBoxTutors);

            // GRIDPANE - CONVOCATORIA:
            JSONObject convocatoriaJSON = alumneData.getJSONObject("convocatoria");
            gpConvocatoria.add(new TextField(convocatoriaJSON.getString("estatSolicitud")), 0, 0, 1, 1);
            gpConvocatoria.add(new TextField(convocatoriaJSON.getString("nom")), 1, 0, 1, 1);
            gpConvocatoria.add(new TextField(convocatoriaJSON.getString("tipus")), 0, 1, 1, 1);
            gpConvocatoria.add(new TextField(convocatoriaJSON.getString("codi")), 1, 1, 1, 1);

            JSONObject ensenyamentJSON = convocatoriaJSON.getJSONObject("ensenyament");
            gpEnsenyament.add(new TextField(ensenyamentJSON.getString("torn")), 0, 0, 1, 1);
            gpEnsenyament.add(new TextField(ensenyamentJSON.getString("nom")), 1, 0, 1, 1);
            gpEnsenyament.add(new TextField(Integer.toString(ensenyamentJSON.getInt("curs"))), 2, 0, 1, 1);
            gpEnsenyament.add(new TextField(ensenyamentJSON.getString("regim")), 0, 1, 1, 1);
            gpEnsenyament.add(new TextField(ensenyamentJSON.getString("codi")), 1, 1, 1, 1);

            JSONObject centreJSON = convocatoriaJSON.getJSONObject("centre");
            gpCentre.add(new TextField(centreJSON.getString("municipi")), 0, 0, 1, 1);
            gpCentre.add(new TextField(centreJSON.getString("sstt")), 1, 0, 1, 1);
            gpCentre.add(new TextField(centreJSON.getString("naturalesa")), 2, 0, 1, 1);
            gpCentre.add(new TextField(centreJSON.getString("nom")), 0, 1, 1, 1);
            gpCentre.add(new TextField(centreJSON.getString("codi")), 1, 1, 1, 1);

            // GRIDPANE - DIRECCIO
            JSONObject direccioJSON = alumneData.getJSONObject("direccio");
            gpDireccio.add(new TextField(direccioJSON.getString("nom")), 0, 0, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("codiPostal")), 1, 0, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("numero")), 2, 0, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("altresDades")), 0, 1, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("municipi")), 1, 1, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("provincia")), 2, 1, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("tipusVia")), 0, 2, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("pais")), 1, 2, 1, 1);
            gpDireccio.add(new TextField(direccioJSON.getString("localitat")), 2, 2, 1, 1);

            // GRIDPANE - CENTRE PROCEDENCIA
            JSONObject centreProcedenciaJSON = alumneData.getJSONObject("centreProcedencia");
            gpCentreProcedencia.add(new TextField(centreProcedenciaJSON.getString("codiEnsenyament")), 0, 0, 1, 1);
            gpCentreProcedencia.add(new TextField(centreProcedenciaJSON.getString("nomEnsenyament")), 1, 0, 1, 1);
            gpCentreProcedencia.add(new TextField(centreProcedenciaJSON.getString("llengua")), 2, 0, 1, 1);
            gpCentreProcedencia.add(new TextField(centreProcedenciaJSON.getString("nom")), 0, 1, 1, 1);
            gpCentreProcedencia.add(new TextField(centreProcedenciaJSON.getString("curs")), 1, 1, 1, 1);
            gpCentreProcedencia.add(new TextField(centreProcedenciaJSON.getString("codi")), 2, 1, 1, 1);

        }


    }


}
