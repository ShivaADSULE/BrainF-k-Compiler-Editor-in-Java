/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brainf_ck;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author SHIVA
 */
public class BrainF_ck extends Application {
    
    private Parent root;
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.DECORATED);
        Parent load = FXMLLoader.load(getClass().getResource(("loading.fxml")));
        root = FXMLLoader.load(getClass().getResource(("FXMLDocument.fxml")));
        Scene scene1 = new Scene(load);
        Scene scene2 = new Scene(root);
        stage.setTitle("BrainF_ck Interpreter by The_Shiva");
        stage.getIcons().add(new Image("file:D:\\My_Softs\\BrainF_ck\\src\\brainf_ck\\bf.png"));
        stage.setScene(scene1);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
        AnchorPane pane = (AnchorPane)load.lookup("#anim");
        //Load splash screen with fade in effect
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), pane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
 
        //Finish splash with fade out effect
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), pane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
 
        fadeIn.play();
        
        fadeIn.setOnFinished((e) -> {
            fadeOut.play();
        });
        
        fadeOut.setOnFinished((e) -> {
            stage.setScene(scene2);
            scene2.getRoot().requestFocus();
            stage.show();
            stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
