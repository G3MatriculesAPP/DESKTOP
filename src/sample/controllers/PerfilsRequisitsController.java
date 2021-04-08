package sample.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.json.JSONObject;
import sample.models.PerfilRequeriment;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PerfilsRequisitsController implements Initializable {

    @FXML    private Button btnAdd, btnDelete, btnAddReq, btnDeleteReq, btnModify, btnConfirm;
    @FXML    private TableView tableReq;
    @FXML    private ListView<PerfilRequeriment> listProfiles;
    @FXML    private Pane paneOptions;
    @FXML    private TableColumn<JSONObject, String> nameColumn;
    @FXML    private TableColumn<JSONObject, String> extensionColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initTable();

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
            paneOptions.setVisible(true);
            tableReq.setVisible(true);
            btnConfirm.setVisible(true);
            System.out.println("Nom=" + nomDesc.getKey() + ", Descripcio=" + nomDesc.getValue());
        });

    }

    @FXML
    void addReq(ActionEvent event) {
        addRow();
        addRow();
        addRow();

    }

    @FXML
    void deletePerfil(ActionEvent event) {

    }

    @FXML
    void deleteReq(ActionEvent event) {

    }

    @FXML
    void modifyPerfil(ActionEvent event) {

    }

    @FXML
    void checkData(ActionEvent event) {

    }


    private void initTable(){

        initCols();
    }
    private void initCols(){

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("NOM"));
        extensionColumn.setCellValueFactory(new PropertyValueFactory<>("EXTENSIÓ"));

    }

    private void addRow(){
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).put("nom", e.getNewValue());
        });

        extensionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        extensionColumn.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).put("nom", e.getNewValue());
        });
    }



}
