package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Controller {
    //Menu
    @FXML public MenuItem menuSaveBtn;

    //Main Picture
    @FXML private BorderPane picPane_ID;

    //Gallerybox
    @FXML public HBox galleryBox_ID;

    //ITPC
    @FXML public TextField picNameTF_ID;
    @FXML public TextField picFormatTF_ID;
    @FXML public ChoiceBox photographerCB_ID;
    @FXML public TextArea keywordsTA_ID;

    //EXIF
    @FXML public TextField dateTimeTF_ID;
    @FXML public TextField focalLengthTF_ID;
    @FXML public TextField expoTimeTF_ID;
    @FXML public TextField dazzleNumberTF_ID;


    public void menuSaveBtn_pressed(ActionEvent actionEvent) {

    }
}
