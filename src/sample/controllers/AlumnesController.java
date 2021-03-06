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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private int pos;
    private VBox vBoxTutors;
    public static JSONObject alumneData;

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
                    alert.setHeaderText("Fichero .CSV no v??lido....");
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * getAllCicles()
     * Obtiene la informaci??n ya parseada de la API y la a??ade al ComboBox
     */
    public void getAllCicles(){

        if (Data.ciclesList != null){
            ciclesMenu = FXCollections.observableArrayList();
            ciclesMenu.addAll(Data.ciclesList);
            cmbCicles.setItems(ciclesMenu);
        }
    }

    /**
     * Se obtiene el nombre del ciclo seleccionado y se llama a la API para obtener todos los alumnos que tengan ese ciclo
     * establecido, una vez obtenidos se a??aden y se muestran en una lista.
     * @param event
     */
    @FXML
    void getAlumnesByCicle(ActionEvent event){
        vBoxData.setVisible(false);
        listAlumnes.getItems().clear();
        arrayJSON = Data.alumneManager.getAlumnesByCourse(cmbCicles.getSelectionModel().getSelectedItem().getNomCicle());
        for (int i = 0; i < arrayJSON.length(); i++){
            JSONObject json = arrayJSON.getJSONObject(i);
            if (json.isNull("dni"))
                listAlumnes.getItems().add(json.getString("nom") + " " + json.getString("primerCognom") + " " + json.getString("segonCognom") + " \nNIE: " + json.getString("nie"));
            else {
                listAlumnes.getItems().add(json.getString("nom") + " " + json.getString("primerCognom") + " " + json.getString("segonCognom") + " \nDNI: " + json.getString("dni"));
            }
        }
    }

    /**
     * Se obtiene el JSONObject del JSONArray de alumnos y su ID, mediante esa ID se llama a la API para obtener todos los
     * datos del alumno y se llama a transformData() para mostrar todos los datos
     * @param event
     */
    @FXML
    void getDataAlumne(MouseEvent event) {

        pos = listAlumnes.getSelectionModel().getSelectedIndex();
        alumneData = new JSONObject();
        alumneData = Data.alumneManager.getAlumneData(arrayJSON.getJSONObject(pos).getString("_id"));
        transformData(alumneData);
    }

    /**
     * Recorre entero el JSONObject y por cada campo crea un nuevo TextField con el valor en esa clave del JSONObject
     * como texto predefinido, si ese valor es variable se crea una ChoiceBox con las diferentes opciones y se establece
     * cual es la que esta definida en el JSON. Si hay algun campo sin datos se crea un nuevo TextField pero sin texto predefinido
     * esto se controla desde aqui y desde AlumneImpl.
     * @param alumneData - JSONObject con todos los datos de 1 alumno
     */
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
            vBoxTutors = new VBox();
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
            gpCentreProcedencia.add(new TextField(Integer.toString(centreProcedenciaJSON.getInt("curs"))), 1, 1, 1, 1);
            gpCentreProcedencia.add(new TextField(centreProcedenciaJSON.getString("codi")), 2, 1, 1, 1);

        }
    }

    @FXML
    void newAlumne(ActionEvent event) {

        vBoxData.setVisible(true);

        gridPaneData.add(new TextField(), 0, 0, 1, 1);
        gridPaneData.add(new TextField(), 1, 0, 1, 1);
        gridPaneData.add(new TextField(), 2, 0, 1, 1);

        gridPaneData.add(new TextField(), 0, 1, 1, 1);
        gridPaneData.add(new TextField(), 1, 1, 1, 1);
        gridPaneData.add(new TextField(), 2, 1, 1, 1);

        gridPaneData.add(new DatePicker(), 0, 2, 1, 1);
        gridPaneData.add(new TextField(), 1, 2, 1, 1);
        gridPaneData.add(new TextField(), 2, 2, 1, 1);

        String[] tipusDocument = new String[]{"DNI", "NIE"};
        ChoiceBox cbDoc = new ChoiceBox();
        cbDoc.getItems().addAll(tipusDocument);
        gridPaneData.add(cbDoc, 0, 3, 1, 1);
        gridPaneData.add(new TextField(), 1, 3, 1, 1);
        gridPaneData.add(new TextField(), 2, 3, 1, 1);

        String[] listSexes = new String[]{"HOME", "DONA"};
        ChoiceBox cbSexe = new ChoiceBox();
        cbSexe.getItems().addAll(listSexes);
        gridPaneData.add(cbSexe, 0, 4, 1, 1);
        gridPaneData.add(new TextField(), 1, 4, 1, 1);
        gridPaneData.add(new TextField(), 2, 4, 1, 1);

        // GRIDPANE - TUTORS

        vBoxTutors = new VBox();
        vBoxTutors.setSpacing(10);
        for (int i = 0; i < 1; i++){
            GridPane gpTutor = new GridPane();
            gpTutor.add(new TextField(), 0, 0, 1, 1);
            gpTutor.add(new TextField(), 1, 0, 1, 1);
            gpTutor.add(new TextField(), 2, 0, 1, 1);

            gpTutor.add(new TextField(), 0, 1, 1, 1);
            gpTutor.add(new TextField(), 1, 1, 1, 1);
            vBoxTutors.getChildren().add(gpTutor);
        }

        acTutors.setContent(vBoxTutors);

        // GRIDPANE - CONVOCATORIA:
        gpConvocatoria.add(new TextField(), 0, 0, 1, 1);
        gpConvocatoria.add(new TextField(), 1, 0, 1, 1);
        gpConvocatoria.add(new TextField(), 0, 1, 1, 1);
        gpConvocatoria.add(new TextField(), 1, 1, 1, 1);

        gpEnsenyament.add(new TextField(), 0, 0, 1, 1);
        gpEnsenyament.add(new TextField(), 1, 0, 1, 1);
        gpEnsenyament.add(new TextField(), 2, 0, 1, 1);
        gpEnsenyament.add(new TextField(), 0, 1, 1, 1);
        gpEnsenyament.add(new TextField(), 1, 1, 1, 1);

        gpCentre.add(new TextField(), 0, 0, 1, 1);
        gpCentre.add(new TextField(), 1, 0, 1, 1);
        gpCentre.add(new TextField(), 2, 0, 1, 1);
        gpCentre.add(new TextField(), 0, 1, 1, 1);
        gpCentre.add(new TextField(), 1, 1, 1, 1);

        // GRIDPANE - DIRECCIO
        gpDireccio.add(new TextField(), 0, 0, 1, 1);
        gpDireccio.add(new TextField(), 1, 0, 1, 1);
        gpDireccio.add(new TextField(), 2, 0, 1, 1);
        gpDireccio.add(new TextField(), 0, 1, 1, 1);
        gpDireccio.add(new TextField(), 1, 1, 1, 1);
        gpDireccio.add(new TextField(), 2, 1, 1, 1);
        gpDireccio.add(new TextField(), 0, 2, 1, 1);
        gpDireccio.add(new TextField(), 1, 2, 1, 1);
        gpDireccio.add(new TextField(), 2, 2, 1, 1);

        // GRIDPANE - CENTRE PROCEDENCIA
        gpCentreProcedencia.add(new TextField(), 0, 0, 1, 1);
        gpCentreProcedencia.add(new TextField(), 1, 0, 1, 1);
        gpCentreProcedencia.add(new TextField(), 2, 0, 1, 1);
        gpCentreProcedencia.add(new TextField(), 0, 1, 1, 1);
        gpCentreProcedencia.add(new TextField(), 1, 1, 1, 1);
        gpCentreProcedencia.add(new TextField(), 2, 1, 1, 1);

    }

    /**
     * Recorre todos los TextField y va montando un nuevo JSONObject con los datos en este, los datos clave como la contrase??a
     * id o perfilSeleccionado se obtienen del JSON original para evitar que al actualizar desaparezcan.
     * @param event
     */
    @FXML
    void saveAlumne(ActionEvent event) {
        try {
            JSONObject alumneJSON = new JSONObject();
            String[] mainKeysJSON = new String[]{"nom", "primerCognom", "segonCognom",
                    "telefon", "email", "nacionalitat",
                    "dataNaixement", "paisNaixement", "municipiNaixement", "tipusDocument", "",
                    "idRALC", "sexe", "tipusAlumne", "codiCentreAssignat"};

            for (int i = 0; i < mainKeysJSON.length; i++){
                if (i == 6){
                    DatePicker dp = (DatePicker) gridPaneData.getChildren().get(i);
                    LocalDate localDate = dp.getValue();
                    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                    Date date = Date.from(instant);
                    System.out.println(date);
                    alumneJSON.put(mainKeysJSON[i], date.toGMTString());
                }else if (i == 9 || i == 12){
                    ChoiceBox cb = (ChoiceBox) gridPaneData.getChildren().get(i);
                    String selection = (String) cb.getSelectionModel().getSelectedItem();
                    if (selection.equals("DNI")){
                        i++;
                        TextField tf = (TextField) gridPaneData.getChildren().get(i);
                        alumneJSON.put("dni", tf.getText());
                    }else if(selection.equals("NIE")){
                        i++;
                        TextField tf = (TextField) gridPaneData.getChildren().get(i);
                        alumneJSON.put("nie", tf.getText());
                    }else if (selection.equals("HOME")){
                        alumneJSON.put(mainKeysJSON[i], "Home");
                    }else if(selection.equals("DONA")){
                        alumneJSON.put(mainKeysJSON[i], "Dona");
                    }
                }else{
                    TextField tf = (TextField) gridPaneData.getChildren().get(i);
                    alumneJSON.put(mainKeysJSON[i], tf.getText());
                }

            }

            // TUTORS:

            String[] tutorKeysJSON = new String[]{"primerCognom", "segonCognom", "tipusDocument", "nom", "numDocument"};
            JSONArray arrayTutorsJSON = new JSONArray();
            for (int i = 0; i < vBoxTutors.getChildren().size(); i++){
                GridPane gp = (GridPane) vBoxTutors.getChildren().get(i);
                JSONObject tutorJSON = new JSONObject();
                for (int x = 0; x < tutorKeysJSON.length; x++){
                    TextField tf = (TextField) gp.getChildren().get(x);
                    tutorJSON.put(tutorKeysJSON[x], tf.getText());
                }
                arrayTutorsJSON.put(i, tutorJSON);
            }
            alumneJSON.put("tutors", arrayTutorsJSON);

            // CONVOCATORIA:
            String[] convocatoriaKeysJSON = new String[]{"estatSolicitud", "nom", "tipus", "codi"};
            String[] ensenyamentKeysJSON = new String[]{"torn", "nom", "curs", "regim", "codi"};
            String[] centreKeysJSON = new String[]{"municipi", "sstt", "naturalesa", "nom", "codi"};

            JSONObject convocatoriaJSON = new JSONObject();
            JSONObject ensenyamentJSON = new JSONObject();
            JSONObject centreJSON = new JSONObject();

            for (int i = 0; i < convocatoriaKeysJSON.length; i++){
                TextField tf = (TextField) gpConvocatoria.getChildren().get(i);
                convocatoriaJSON.put(convocatoriaKeysJSON[i], tf.getText());
            }

            for (int i = 0; i < ensenyamentKeysJSON.length; i++){
                TextField tf = (TextField) gpEnsenyament.getChildren().get(i);
                ensenyamentJSON.put(ensenyamentKeysJSON[i], tf.getText());
            }

            for (int i = 0; i < centreKeysJSON.length; i++){
                TextField tf = (TextField) gpCentre.getChildren().get(i);
                centreJSON.put(centreKeysJSON[i], tf.getText());
            }

            convocatoriaJSON.put("centre", centreJSON);
            convocatoriaJSON.put("ensenyament", ensenyamentJSON);
            alumneJSON.put("convocatoria", convocatoriaJSON);

            // DIRECCIO:
            JSONObject direccioJSON = new JSONObject();
            String[] direccioKeysJSON = new String[]{"nom", "codiPostal", "numero", "altresDades", "municipi", "provincia", "tipusVia", "pais", "localitat"};
            for (int i = 0; i < direccioKeysJSON.length; i++){
                TextField tf = (TextField) gpDireccio.getChildren().get(i);
                direccioJSON.put(direccioKeysJSON[i], tf.getText());
            }

            alumneJSON.put("direccio", direccioJSON);

            // CENTRE PROCEDENCIA:

            JSONObject centreProcedenciaJSON = new JSONObject();
            String[] centreProcedenciaKeysJSON = new String[]{"codiEnsenyament", "nomEnsenyament", "llengua", "nom", "curs", "codi"};
            for (int i = 0; i < centreProcedenciaKeysJSON.length; i++){
                TextField tf = (TextField) gpCentreProcedencia.getChildren().get(i);
                centreProcedenciaJSON.put(centreProcedenciaKeysJSON[i], tf.getText());
            }

            alumneJSON.put("centreProcedencia", centreProcedenciaJSON);
            JSONObject userData = arrayJSON.getJSONObject(pos);
            String id = userData.getString("_id");

            alumneJSON.put("_id", id);
            if (!alumneData.isNull("pass")){
                String pass = alumneData.getString("pass");
                alumneJSON.put("pass", pass);
            }
            else
                alumneJSON.put("pass", "");

            if (!alumneData.isNull("perfilRequisits")){
                String perfil = alumneData.getString("perfilRequisits");
                alumneJSON.put("perfilRequisits", alumneData.getString("perfilRequisits"));
            }
            else
                alumneJSON.put("perfilRequisits", "");

            if (!alumneData.isNull("estatRequisits")){
                JSONArray arrayEstats = alumneData.getJSONArray("estatRequisits");
                alumneJSON.put("estatRequisits", alumneData.getJSONArray("estatRequisits"));
            }else
                alumneJSON.put("estatRequisits", new JSONArray());

            boolean updated = Data.alumneManager.updateAlumne(alumneJSON);

            Alert alert;
            if (updated){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MatriculesAPP | DESKTOP");
                alert.setHeaderText("Alumno actualizado correctamente!");
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MatriculesAPP | DESKTOP");
                alert.setHeaderText("Error al actualizar...");
            }
            alert.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MatriculesAPP | DESKTOP");
            alert.setHeaderText("S'ha produit un error...");
        }
    }

    /**
     * Primero se comprueba si el alumno tiene un perfil marcado, si no es asi se muestra un mensaje notificando que este alumno
     * no tiene perfiles especiales, si es el caso se abre una nueva Stage de ValidarRequisitsController y se le pasa la ID del alumno
     * el nombre del alumno y el del perfil.
     * @param event
     */
    @FXML
    void checkRequisits(ActionEvent event) {

        if (alumneData.isNull("perfilRequisits") || alumneData.getString("perfilRequisits").isEmpty() || alumneData.getString("perfilRequisits").isBlank() || alumneData.getString("perfilRequisits").equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MatriculesAPP | DESKTOP");
            alert.setHeaderText("Aquest alumne no te res a validar");
            alert.showAndWait();
        }else {
            try {
                Stage stage = new Stage();
                URL url = new File("src/sample/windows/validarRequisits.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Parent root = loader.load();
                ValidarRequisitsController validarRequisitsController = (ValidarRequisitsController) loader.getController();
                validarRequisitsController.setIdAlumne(alumneData.getString("_id"));
                validarRequisitsController.setNomPerfil(alumneData.getString("perfilRequisits"));
                validarRequisitsController.setNomAlumne(alumneData.getString("nom") + " " + alumneData.getString("primerCognom") + " " + alumneData.getString("segonCognom"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                validarRequisitsController.getRequisits();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Llama a la API pasandole la ID del usuario, si la api devuelve STATUS 200 se elimna de la lista y se limpian los datos
     * de la pantalla para evitar confusiones, la propia API lo elimina de la DB. Dependiendo del status se muestra un mensaje
     * u otro.
     * @param event
     */
    @FXML
    void deleteUser(ActionEvent event) {
        pos = listAlumnes.getSelectionModel().getSelectedIndex();
        boolean isDeleted = Data.alumneManager.deleteAlumne(arrayJSON.getJSONObject(pos).getString("_id"));
        if (isDeleted){
            vBoxData.setVisible(false);
            listAlumnes.getItems().remove(pos);
            gridPaneData.getChildren().clear();
            gpCentre.getChildren().clear();
            gpEnsenyament.getChildren().clear();
            gpConvocatoria.getChildren().clear();
            gpDireccio.getChildren().clear();
            gpCentreProcedencia.getChildren().clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MatriculesAPP | DESKTOP");
            alert.setHeaderText("Alumno eliminado correctamente!");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MatriculesAPP | DESKTOP");
            alert.setHeaderText("Error al eliminar alumno....");
            alert.showAndWait();
        }
    }

}
