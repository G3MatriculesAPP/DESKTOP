package sample.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.utils.ConnAPI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImportCSVController implements Initializable {

    @FXML   private ListView<JSONObject> listView;

    private JSONArray importedJSON;
    private ArrayList<JSONObject> arrayJSON = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setData();

    }

    private void setData(){

        // setData()
        // A través de los datos del CSV se crea por cada CICLE una fila con el nombre del ciclo y con una CHECKBOX
        // por defecto premarcada.

        ObservableList<JSONObject> observableList = FXCollections.observableArrayList();
        for (int i = 0; i < importedJSON.length(); i++){
            JSONObject jsonObject = importedJSON.getJSONObject(i);
            observableList.add(jsonObject);
        }

        listView.getItems().addAll(observableList);
        listView.setCellFactory(CheckBoxListCell.forListView(s -> {
            BooleanProperty checkBox = new SimpleBooleanProperty();
            checkBox.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected)
                    arrayJSON.add(s);
                else
                    arrayJSON.remove(s);
            });
            checkBox.setValue(true);
            return checkBox;
        }));

    }

    @FXML
    void importData(MouseEvent event) {

        // importData()
        // Recoge los CICLES que quiere el usuario importar a la DB y se los pasa a la API llamandola, una vez pasados
        // los datos recoge el STATUS y muestra un mensaje dependiendo del resultado.

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", arrayJSON);

        ConnAPI connAPI = new ConnAPI("/api/upload/cicles", "POST", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        switch (status){
            case 0:
                System.out.println("[DEBUG] - Error al obtener STATUS");
                break;

            case 200:
                System.out.println("[DEBUG] - Datos introducidos correctamente!");
                break;

            case 500:
                System.out.println("[DEBUG] - Error al añadir datos en la DB...");
        }
    }

    @FXML
    void selectAll(MouseEvent event) {

    }

    public JSONArray getImportedJSON() {
        return importedJSON;
    }

    public void setImportedJSON(JSONArray importedJSON) {
        this.importedJSON = importedJSON;
    }
}
