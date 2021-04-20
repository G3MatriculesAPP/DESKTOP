package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.utils.ConnAPI;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ValidarRequisitsController implements Initializable {

    @FXML    private VBox vBox;
    @FXML    private Label tagName;
    @FXML    private Button btnSave;

    private String idAlumne, nomAlumne, nomPerfil;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Llama a la API para obtener los requisitos y estados del alumno mediante su ID y el nombre de su perfil, por cada
     * requisito que encuentra crea un nuevo HBOX con un TextField no editable, un ChoiceBox con los diferentes estados
     * y preselecionado el estado que esta en la DB, y finalmente un Button para poder descargar el requisito, al clicar
     * en este se abre un DirectoryChooser para elegir la ubicacion y lo guarda con su ID y el nombre del requisitos
     * EJ: 598034759043659387_DNI
     */
    public void getRequisits(){

        tagName.setText(nomAlumne);

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("nomPerfil", nomPerfil);
        requestJSON.put("idAlumne", idAlumne);
        ConnAPI connAPI = new ConnAPI("/api/reqPerfils/readOneByAlumne", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray dataJSON = responseJSON.getJSONArray("data");
        JSONObject moreDataJSON = responseJSON.getJSONObject("alum");
        JSONArray statusReqsJSON = moreDataJSON.getJSONArray("estatRequisits");

        ObservableList observableList = FXCollections.observableArrayList();
        observableList.add("SENSE DADES");
        observableList.add("INVALID");
        observableList.add("PER_VALIDAR");
        observableList.add("VALID");

        for(int i = 0; i < dataJSON.length(); i++){
            JSONObject rawData = dataJSON.getJSONObject(i);
            int reqStatus = 0;
            if(!statusReqsJSON.isNull(i))
                reqStatus = statusReqsJSON.getInt(i);


                HBox hBox = new HBox();
            hBox.setPrefSize(200, 50);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            hBox.setStyle("-fx-background-color:  #4E68F1; -fx-background-radius: 30");

            TextField tf = new TextField(rawData.getString("nomReq"));
            tf.setEditable(false);
            tf.setAlignment(Pos.CENTER);
            tf.setPrefSize(250, 26);
            tf.setStyle("-fx-background-radius: 30");
            hBox.getChildren().add(tf);

            ChoiceBox cb = new ChoiceBox(observableList);
            cb.getSelectionModel().select(reqStatus);
            cb.setPrefSize(150, 26);
            cb.setStyle("-fx-background-radius: 30");
            hBox.getChildren().add(cb);

            Button button = new Button("DESCARREGA");
            button.setStyle("-fx-background-radius: 30");
            button.setOnAction(actionEvent -> {
                JSONObject requestJSON2 = new JSONObject();
                requestJSON2.put("id", idAlumne);
                requestJSON2.put("nomReq", tf.getText().replace(" ", "%20"));

                ConnAPI connAPI1 = new ConnAPI("/api/perfils/getRequisit", "POST", false);
                connAPI1.setData(requestJSON2);
                connAPI1.establishConn();

                JSONObject responseJSON2 = connAPI.getDataJSON();
                if (rawData.getInt("tipusReq") == 0){
                    try {
                        URL imgSrc = new URL(responseJSON2.getString("data").split("'")[1]);
                        BufferedImage bImage = ImageIO.read(imgSrc);
                        String fileName = "/" + idAlumne + "_" + tf.getText() + ".jpg";
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        directoryChooser.setTitle("Guardar fitxer requeriment:");
                        File selectedDirectory = directoryChooser.showDialog(button.getScene().getWindow());
                        if (selectedDirectory != null){
                            File file = new File(selectedDirectory.getAbsolutePath() + fileName);
                            ImageIO.write(bImage, "jpg", file);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (rawData.getInt("tipusReq") == 1){
                    try {
                        // URL pdfSrc = new URL(responseJSON2.getString("data").split("'")[1]);
                        URL pdfSrc = new URL("https://res.cloudinary.com/demo/image/upload/example_pdf.pdf");
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        directoryChooser.setTitle("Guardar fitxer requeriment:");
                        File selectedDirectory = directoryChooser.showDialog(button.getScene().getWindow());
                        if (selectedDirectory != null){
                            InputStream is = pdfSrc.openStream();
                            FileOutputStream fos = new FileOutputStream(new File(selectedDirectory.getAbsolutePath() + "/" + idAlumne + "_" + tf.getText() + ".pdf"));

                            int length = -1;
                            byte[] buffer = new byte[1024];
                            while ((length = is.read(buffer)) > -1){
                                fos.write(buffer, 0, length);
                            }
                            fos.close();
                            is.close();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            hBox.getChildren().add(button);

            vBox.getChildren().add(hBox);
        }

    }

    /**
     * Al clicar en el boton "OK!" recorre entero el vBox en busca de hBox y sus ChoiceBox, obtiene de este su indice seleccionado
     * y lo añade a un JSONArray, finalmente llama a la API pasandole la ID del alumno y este JSONArray para actualizar, dependiendo
     * del resultado de la llamada a la API muestra un mensaje u otro.
     * @param event
     */
    @FXML
    void save(ActionEvent event) {
        JSONArray arrayStatus = new JSONArray();
        for (int i = 0; i < vBox.getChildren().size(); i++){
            HBox hBox = (HBox) vBox.getChildren().get(i);
            ChoiceBox choiceBox = (ChoiceBox) hBox.getChildren().get(1);
            arrayStatus.put(i, choiceBox.getSelectionModel().getSelectedIndex());
        }

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idAlumne);
        requestJSON.put("estatRequisits", arrayStatus);

        ConnAPI connAPI = new ConnAPI("/api/reqPerfils/updateStatus", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        Alert alert;
        switch (status){
            case 0:
                System.out.println("[DEBUG] - Error al contactar con la API...");
                break;

            case 200:
                System.out.println("[DEBUG] - Datos actualizados correctamente!");
                AlumnesController.alumneData.put("estatRequisits", arrayStatus);
                close();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MatriculesAPP | DESKTOP");
                alert.setHeaderText("Datos actualizados correctamente!");
                alert.showAndWait();
                break;

            case 500:
                System.out.println("[DEBUG] - Error al actualizar los datos...");
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MatriculesAPP | DESKTOP");
                alert.setHeaderText("Error al actualizar los datos...");
                alert.showAndWait();
                break;
        }

    }

    /**
     * Cierra este Stage (Window)
     */
    private void close(){
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public String getIdAlumne() {
        return idAlumne;
    }

    public void setIdAlumne(String idAlumne) {
        this.idAlumne = idAlumne;
    }

    public String getNomPerfil() {
        return nomPerfil;
    }

    public void setNomPerfil(String nomPerfil) {
        this.nomPerfil = nomPerfil;
    }

    public String getNomAlumne() {
        return nomAlumne;
    }

    public void setNomAlumne(String nomAlumne) {
        this.nomAlumne = nomAlumne;
    }
}
