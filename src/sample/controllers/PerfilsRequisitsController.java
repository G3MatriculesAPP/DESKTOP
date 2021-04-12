package sample.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.models.PerfilRequeriment;
import sample.utils.Data;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PerfilsRequisitsController implements Initializable {

    @FXML    private Button btnAdd, btnDelete, btnAddReq, btnDeleteReq, btnConfirm;
    @FXML    private ListView<PerfilRequeriment> listProfiles;
    @FXML    private VBox paneReqs;
    @FXML    private ScrollPane scrollPane;
    @FXML    private Label tagProfileName;
    @FXML    private Pane paneTag;

    private String nomPerfil, descriptionPerfil;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getPerfils();
    }

    private void getPerfils() {
        listProfiles.getItems().clear();
        listProfiles.getItems().addAll(Data.perfilsList);
    }

    @FXML
    void addPerfil(ActionEvent event) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("MatriculesAPP | DESKTOP");
        dialog.setHeaderText("Nom i descripció del perfil");

        ButtonType okButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namePerfil = new TextField();
        namePerfil.setPromptText("Nom del perfil");
        TextArea descPerfil = new TextArea();
        descPerfil.setPromptText("Descripció del perfil a crear");

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(namePerfil, 1, 0);
        grid.add(new Label("Descripció:"), 0, 1);
        grid.add(descPerfil, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        Platform.runLater(() -> namePerfil.requestFocus());

        namePerfil.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(namePerfil.getText(), descPerfil.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(nomDesc -> {
            clearData();
            scrollPane.setVisible(true);
            btnAddReq.setVisible(true);
            btnConfirm.setVisible(true);
            paneTag.setVisible(true);
            nomPerfil = nomDesc.getKey();
            descriptionPerfil = nomDesc.getValue();
            tagProfileName.setText(nomPerfil.toUpperCase());
            addReq(event);
        });

    }

    @FXML
    void getReqs(MouseEvent event) {
        PerfilRequeriment perfilRequeriment = listProfiles.getSelectionModel().getSelectedItem();
        perfilRequeriment = Data.reqPerfilsManager.getPerfilRequeriments(perfilRequeriment);

        clearData();
        scrollPane.setVisible(true);
        btnAddReq.setVisible(true);
        btnConfirm.setVisible(true);
        paneTag.setVisible(true);
        nomPerfil = perfilRequeriment.getNomPerfil();
        descriptionPerfil = perfilRequeriment.getDescrPerfil();
        tagProfileName.setText(nomPerfil.toUpperCase());

        for (int i = 0; i < perfilRequeriment.getRequisits().length(); i++){
            JSONObject rawJSON = perfilRequeriment.getRequisits().getJSONObject(i);
            addNewRow(rawJSON.getString("nomReq"), rawJSON.getInt("tipusReq"));
        }
    }

    /**
     * Añade 3 nuevos TextField en blanco
     * @param event
     */
    @FXML
    void addReq(ActionEvent event) {
        addNewRow();
        addNewRow();
        addNewRow();
    }

    @FXML
    void deletePerfil(ActionEvent event) {

    }

    @FXML
    void checkData(ActionEvent event) {
        int rowCount = paneReqs.getChildren().size();
        JSONObject dataPerfil = new JSONObject();
        dataPerfil.put("nom", nomPerfil);
        dataPerfil.put("descripcio", descriptionPerfil);
        JSONArray arrayJSON = new JSONArray();
        for (int i = 0; i < rowCount; i++){
            HBox row = (HBox) paneReqs.getChildren().get(i);
            TextField tfRow = (TextField) row.getChildren().get(0);
            if (!tfRow.getText().isEmpty()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nomReq", tfRow.getText());
                ChoiceBox cbRow = (ChoiceBox) row.getChildren().get(1);
                if (cbRow.getSelectionModel().getSelectedItem() != null){
                    jsonObject.put("tipusReq", cbRow.getSelectionModel().getSelectedIndex());
                    arrayJSON.put(jsonObject);
                }
            }
        }

        if (arrayJSON.length() > 0){
            dataPerfil.put("requeriments", arrayJSON);
            boolean result = Data.reqPerfilsManager.createPerfil(dataPerfil);
            if (result){
                Data.perfilsList = Data.reqPerfilsManager.getAllPerfils();
                getPerfils();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MatriculesAPP | DESKTOP");
                alert.setHeaderText("Perfil añadido correctamente!");
                alert.showAndWait();
            }
        }
    }

    /**
     * Crea un nuevo TextField y lo añade al panel.
     */
    private void addNewRow() {

        HBox hBox = new HBox();
        hBox.setPrefSize(200, 35);
        hBox.setSpacing(15);

        TextField nameReq = new TextField();
        nameReq.setPrefSize(350, 35);
        hBox.getChildren().add(nameReq);

        ChoiceBox<String> extensionReq = new ChoiceBox();
        extensionReq.setPrefSize(100, 35);
        extensionReq.getItems().setAll(Data.optionsReqs);
        hBox.getChildren().add(extensionReq);

        paneReqs.getChildren().add(hBox);
    }

    private void addNewRow(String reqName, int reqExtension) {

        HBox hBox = new HBox();
        hBox.setPrefSize(200, 35);
        hBox.setSpacing(15);

        TextField nameReq = new TextField();
        nameReq.setPrefSize(350, 35);
        nameReq.setText(reqName);
        hBox.getChildren().add(nameReq);

        ChoiceBox<String> extensionReq = new ChoiceBox();
        extensionReq.setPrefSize(100, 35);
        extensionReq.getItems().setAll(Data.optionsReqs);
        extensionReq.getSelectionModel().select(reqExtension);
        hBox.getChildren().add(extensionReq);

        paneReqs.getChildren().add(hBox);
    }

    private void clearData(){

        paneReqs.getChildren().clear();

    }

}
