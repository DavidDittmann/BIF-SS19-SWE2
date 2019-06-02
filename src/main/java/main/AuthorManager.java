package main;

import DataModels.AuthorData;
import DataModels.P_Model_Author;
import Layers.BusinessLayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AuthorManager implements Initializable {
    boolean autoselect = false;

    @FXML private ComboBox authorCB;
    @FXML private Button saveBtn;
    @FXML private TextField nameTF;
    @FXML private TextField lastNameTF;
    @FXML private DatePicker datePicker;
    @FXML private TextArea notesTA;

    private BusinessLayer BL;
    private P_Model_Author activeAuthor;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BL = new BusinessLayer();
        loadAuthors();
        applyBindings();

        authorCB.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(!autoselect){
                    unbind();
                    saveAuthor(activeAuthor);
                    List<P_Model_Author> tmp = BL.getAllAuthors();    //TODO Debuging code entfernen
                    activeAuthor = (P_Model_Author) observableValue.getValue();
                    applyBindings();
                }
            }
        });
    }
    private void applyBindings(){
        nameTF.textProperty().bindBidirectional(activeAuthor.nameProperty());
        lastNameTF.textProperty().bindBidirectional(activeAuthor.lastNameProperty());
        datePicker.valueProperty().bindBidirectional(activeAuthor.birthdayProperty());
        notesTA.textProperty().bindBidirectional(activeAuthor.notesProperty());
    }

    private void unbind(){
        nameTF.textProperty().unbindBidirectional(activeAuthor.nameProperty());
        lastNameTF.textProperty().unbindBidirectional(activeAuthor.lastNameProperty());
        datePicker.valueProperty().unbindBidirectional(activeAuthor.birthdayProperty());
        notesTA.textProperty().unbindBidirectional(activeAuthor.notesProperty());
    }

    public void loadAuthors(){  //TODO: Eventuel prüfen ob alte weil sonst doppelt?
        ObservableList<P_Model_Author> authors = FXCollections.observableArrayList();
        List<P_Model_Author> tmp = BL.getAllAuthors();
        authorCB.getItems().clear();
        authorCB.getItems().add(new P_Model_Author(new AuthorData("NEW","AUTHOR",null,"Notes...")));
        autoselect = true;
        authorCB.getSelectionModel().selectFirst();
        autoselect=false;

        activeAuthor = (P_Model_Author) authorCB.getItems().get(0);
        if(tmp!=null && tmp.size()!=0){
            for(P_Model_Author a : tmp){
                authors.add(a);
            }
        }
        authorCB.getItems().addAll(authors);
    }

    public void saveAuthor(P_Model_Author a){
        if(BL.isValidAuthor(a)){
            boolean isNewAuthor;
            if(a.getID()==0){
                isNewAuthor=true;
            }
            else
                isNewAuthor=false;
            BL.saveAuthorData(a,isNewAuthor);

            if(isNewAuthor)
                loadAuthors();
        }
    }

    public void saveBtnClicked(MouseEvent mouseEvent) {
        autoselect=true;
        saveAuthor(activeAuthor);
        autoselect=false;
    }

}
