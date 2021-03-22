package sample;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	URL url = new File("res/ETLogo.jpg").toURI().toURL();
    	Image icon = new Image(String.valueOf(url));
    	primaryStage.getIcons().add(icon);
    	Parent root = FXMLLoader.load(getClass().getResource("windows/login.fxml"));
    	primaryStage.setTitle("MatriculesAPP | DESKTOP");
    	primaryStage.setScene(new Scene(root, 1280, 720));

    	primaryStage.show();

    }


    public static void main(String[] args) {


    	Fuente: https://www.iteramos.com/pregunta/54713/-icono-de-aplicacion-javafx-2-
    		launch(args);
    }
}
