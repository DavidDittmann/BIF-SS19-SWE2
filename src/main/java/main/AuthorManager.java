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


/**
 * AuthorenManager UI zum Bearbeiten / Neuanlegen eines Authors
 */
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


    /**
     * Initialisierung der Controllers des AuthorManagers
     * Laden des Businesslayers und implementierung zum Laden des Listeners
     * für die Combobox (Speichern der Daten, falls Valide bei wechseln von einem Author zum nächsten)
     * @param url Standard-Initialisierungsparameter
     * @param resourceBundle Standard-Initialisierungsparameter
     */
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
                    activeAuthor = (P_Model_Author) observableValue.getValue();
                    applyBindings();
                }
            }
        });
    }

    /**
     * Binden der Properties des gerade aktiven Authors mit den Properties der UI
     */
    private void applyBindings(){
        nameTF.textProperty().bindBidirectional(activeAuthor.nameProperty());
        lastNameTF.textProperty().bindBidirectional(activeAuthor.lastNameProperty());
        datePicker.valueProperty().bindBidirectional(activeAuthor.birthdayProperty());
        notesTA.textProperty().bindBidirectional(activeAuthor.notesProperty());
    }

    /**
     * Unbinden der Properties des gerade aktiven Authors von den Properties der UI
     * Wird benötigt beim Wechsel des anzuzeigenden Auhtors, damit die Daten nicht überschrieben werden
     */
    private void unbind(){
        nameTF.textProperty().unbindBidirectional(activeAuthor.nameProperty());
        lastNameTF.textProperty().unbindBidirectional(activeAuthor.lastNameProperty());
        datePicker.valueProperty().unbindBidirectional(activeAuthor.birthdayProperty());
        notesTA.textProperty().unbindBidirectional(activeAuthor.notesProperty());
    }

    /**
     * Laden aller bekannten Authoren von der Datenbank und aktuallisierung der Combobox zur Auswahl
     */
    public void loadAuthors(){
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

    /**
     * Speichern von eventuell geänderten Authorendaten oder eines neuen Authors
     * @param a (Type: P_Model_Author) Daten, welche gespeichert werden sollen
     */
    public void saveAuthor(P_Model_Author a){
        if(BL.isValidAuthor(a)){
            boolean isNewAuthor;
            if(a.getID()==0){
                isNewAuthor=true;
            }
            else
                isNewAuthor=false;
            BL.saveAuthorData(a,isNewAuthor);

            loadAuthors();
        }
    }

    /**
     * Aufruf des Speicherns des aktiven Authors
     * @param mouseEvent (Type: MouseEvent) Button wird geklickt
     */
    public void saveBtnClicked(MouseEvent mouseEvent) {
        autoselect=true;
        saveAuthor(activeAuthor);
        autoselect=false;
    }
}
