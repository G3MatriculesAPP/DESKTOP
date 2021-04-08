package sample.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML    private AnchorPane contentPane;

    private FXMLLoader fxmlLoader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gotoCourses(null);
    }

    @FXML
    void gotoCourses(MouseEvent event) {
        try {
            transitionEffect();
            fxmlLoader = new FXMLLoader(getClass().getResource("../windows/dashboard.fxml"));
            contentPane.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoAlumnes(MouseEvent event) {
        try {
            transitionEffect();
            fxmlLoader = new FXMLLoader(getClass().getResource("../windows/alumnes.fxml"));
            contentPane.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transición al cambiar de Scene, efecto de desvanecido.
     */
    private void transitionEffect(){

        FadeTransition fadeTransition = new FadeTransition(new Duration(250), contentPane);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1);
        fadeTransition.play();

    }





}
