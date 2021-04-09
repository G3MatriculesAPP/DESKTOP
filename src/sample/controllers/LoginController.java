package sample.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;
import sample.utils.ConnAPI;
import sample.utils.Data;
import sample.utils.Encypter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML    private TextField etEmail;
    @FXML    private PasswordField etPassword;
    @FXML    private Button btnLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        etPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    checkData(null);
            }
        });

    }

    /** 
     * checkData()
     * Verifica que los datos introducidos son validos y si lo son, ejecuta checkLogin()
     * para realizar la llamada a la API.
     */
    @FXML
    void checkData(MouseEvent event) {

        String email = etEmail.getText();
        String pass = etPassword.getText();

        if (email.isEmpty() || pass.isEmpty()){
            System.out.println("[DEBUG] - Faltan campos por rellenar...");
        }else {
            if (!email.contains("@")){
                System.out.println("[DEBUG] - El email no es valido...");
            }else {
                checkLogin(email, pass);
            }
        }

    }
    
    /**
     *  checkLogin()
     *  Llama a la API y consulta si el login es correcto o no
     */
    private void checkLogin(String email, String pass) {

               

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itEmail", email);
        jsonObject.put("itPassword", Encypter.hashMD5(pass));

        ConnAPI connAPI = new ConnAPI("/admin/login", "POST", false);
        connAPI.setData(jsonObject);
        connAPI.establishConn();


        int status = connAPI.getStatus();
        JSONObject responseJSON = connAPI.getDataJSON();

        switch (status){
            case 0:
                System.out.println("[DEBUG] - Fallo al obtener STATUS...");
                break;
            case 200:
                System.out.println("[DEBUG] - Login correcto!");
                Data.TOKEN = responseJSON.getString("token");
                gotoLogin();
                break;
            case 500:
                System.out.println("[DEBUG] - Datos introducidos incorrectos...");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MatriculesAPP | DESKTOP");
                alert.setHeaderText("Login incorrecto....");
                alert.showAndWait();
                break;
        }

        connAPI.closeConn();
    }

    /**
     *  gotoLogin()
     *  Permite cambiar la SCENE, pasando de Login a Dashboard
     */
    private void gotoLogin() {

        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../windows/mainWindow.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
