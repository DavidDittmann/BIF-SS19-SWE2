package main;

import Misc.ConfigManager;
import Misc.Logging;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.List;


public class Main extends Application {


    /**
     * Inititalisierung der Appliaktion. Start des Hauptcontrollers und UI
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Logging.LogInfo(this.getClass(),"Application started");
        ConfigManager conf = ConfigManager.getInstance();

        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));

        primaryStage.setTitle("Picture DB");
        primaryStage.setScene(new Scene(root, conf.getStartWidth(), conf.getStartHeight()));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                conf.setStartWidth(primaryStage.getWidth());
                conf.setStartHeight(primaryStage.getHeight());
                try {
                    conf.saveConfig();
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                }
                Logging.LogInfo(this.getClass(),"Application closed normaly");
            }
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
