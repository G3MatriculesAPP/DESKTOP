package sample.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.utils.ConnAPI;
import sample.utils.Data;

import java.net.URL;
import java.util.ResourceBundle;

public class ImportCSVCiclesController implements Initializable {

    @FXML    private Label lblNumCicles;
    @FXML    private TableView<JSONObject> tableView;
    @FXML    private TableColumn<JSONObject, String> tcCode;
    @FXML    private TableColumn<JSONObject, String> tcName;

    private Stage mainStage;
    private JSONArray importedJSON;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    private void setData(){
        try{
            ObservableList<JSONObject> observableList = FXCollections.observableArrayList();
            for (int i = 0; i < importedJSON.length(); i++){
                JSONObject jsonObject = importedJSON.getJSONObject(i);
                observableList.add(jsonObject);
            }

            lblNumCicles.setText("S'han trobat ["+observableList.size()+"] CICLES");

            tableView.setItems(observableList);
            tcCode.setCellValueFactory(jsonObjectStringCellDataFeatures -> new ReadOnlyObjectWrapper<String>(jsonObjectStringCellDataFeatures.getValue().getString("codi")));
            tcName.setCellValueFactory(jsonObjectStringCellDataFeatures -> new ReadOnlyObjectWrapper<String>(jsonObjectStringCellDataFeatures.getValue().getString("nom")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  importData()
     *  Recoge los CICLES que quiere el usuario importar a la DB y se los pasa a la API llamandola, una vez pasados
     *	los datos recoge el STATUS y muestra un mensaje dependiendo del resultado.
     */
    @FXML
    void importData(MouseEvent event) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", importedJSON.toString());

        ConnAPI connAPI = new ConnAPI("/api/cicles/insertMany", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        switch (status){
            case 0:
                System.out.println("[DEBUG] - Error al obtener STATUS");
                break;

            case 200:
                try{
                    System.out.println("[DEBUG] - Datos introducidos correctamente!");
                    Data.ciclesList = Data.cicleManager.getAllCicles();
                    Stage stage = (Stage) tableView.getScene().getWindow();
                    stage.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("MatriculesAPP | DESKTOP");
                    alert.setHeaderText("Cicles afegits correctament!");
                    alert.showAndWait();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case 500:
                System.out.println("[DEBUG] - Error al a??adir datos en la DB...");
                break;
        }
    }

    public JSONArray getImportedJSON() {
        return importedJSON;
    }

    public void setImportedJSON(JSONArray importedJSON) {
        this.importedJSON = importedJSON;
        setData();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
