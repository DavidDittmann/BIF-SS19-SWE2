package main;

import DataModels.PictureData;
import Layers.DataAccessLayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*DataAccessLayer dal = new DataAccessLayer();
        dal.generateDatabase();
        dal.updateDatabase();*/

        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));

        primaryStage.setTitle("Picture DB");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
