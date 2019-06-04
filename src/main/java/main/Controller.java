package main;

import DataModels.P_Model_Author;
import DataModels.P_Model_Picture;
import Layers.BusinessLayer;
import Misc.Logging;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.NumberStringConverter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 *  Hauptcontroller der Applikation. Handled das Verhalten des Hauptfensters.
 */
public class Controller implements Initializable {
    private BusinessLayer BL;
    private P_Model_Picture MainPicture;
    private List<P_Model_Picture> Pictures;

    //Menuspeichern / Mainspeichern
    @FXML public MenuItem authorManager;
    @FXML private Button mainSaveBtn;
    @FXML private MenuItem reloadBtn;

    //Searchbar
    @FXML TextField searchBar_ID;

    //Main Picture
    @FXML private BorderPane picPane_ID;
    @FXML private javafx.scene.image.ImageView mainPicture_ID;
    //ITPC
    @FXML private TextField picNameTF_ID;
    @FXML private TextField picFormatTF_ID;
    @FXML private ChoiceBox photographerCB_ID;
    @FXML private TextArea keywordsTA_ID;
    @FXML private TextArea descriptionTA_ID;
    //EXIF
    @FXML private TextField dateTimeTF_ID;
    @FXML private TextField locationTF_ID;
    @FXML private TextField focalLengthTF_ID;
    @FXML private TextField expoTimeTF_ID;
    @FXML private TextField dazzleNumberTF_ID;
    //Gallerybox
    @FXML private ScrollPane galleryScrollPane;

    private boolean maybeNewAuthor = false;
    private boolean autochange = false;

    /**
     * Initialisierung der Hauptcontrollers der Applikation
     * Laden des Businesslayers und implementierung zum Laden der Eventhandler
     * für die Searchbar, den Menübutton "Reload" und den Menübutton AuthorManager
     * @param url Standard-Initialisierungsparameter
     * @param resourceBundle Standard-Initialisierungsparameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BL = new BusinessLayer();
        reloadData();

        photographerCB_ID.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(!autochange)
                    Logging.LogInfo(this.getClass(),"Author changed >>> new author for picture ["+MainPicture.getFileName()+"]: <"+t1+">");
            }
        });

        authorManager.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                System.out.println(getClass().getResource("/AuthorManager.fxml"));
                fxmlLoader.setLocation(getClass().getResource("/AuthorManager.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(),600,400);
                    Stage stage = new Stage();
                    stage.setTitle("Authoren Manager");
                    stage.setScene(scene);

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            maybeNewAuthor = true;
                            reloadData();
                        }
                    });

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        reloadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reloadData();
            }
        });

        searchBar_ID.textProperty().addListener((observable,oldvalue,newvalue) ->{
            loadPictures(newvalue);
            loadImagesGallery();
        });

        Logging.LogInfo(this.getClass(),"Controller initialised");
    }

    /**
     * Wraper zum Laden aller verfügbaren Bilder und Authoren aus der Datenbank
     */
    public void reloadData(){
        Logging.LogDebug(this.getClass(),"Data reloading...");
        BL.reloadDatabase();
        loadAuthors();
        loadPictures(null);
        loadImagesGallery();
        Logging.LogDebug(this.getClass(),"Data reloaded!");
    }

    /**
     * Binden der Properties des gerade aktiven Bildes mit den Properties der UI
     */
    public void applyBindings(){
        picNameTF_ID.textProperty().bindBidirectional(MainPicture.fileNameProperty());
        picFormatTF_ID.textProperty().bindBidirectional(MainPicture.fileExtProperty());
        keywordsTA_ID.textProperty().bindBidirectional(MainPicture.keywordsProperty());
        descriptionTA_ID.textProperty().bindBidirectional(MainPicture.descriptionProperty());
        dateTimeTF_ID.textProperty().bindBidirectional(MainPicture.creationTimeProperty());
        locationTF_ID.textProperty().bindBidirectional(MainPicture.locationProperty());
        expoTimeTF_ID.textProperty().bindBidirectional(MainPicture.exposureTimeProperty(),new NumberStringConverter());
        focalLengthTF_ID.textProperty().bindBidirectional(MainPicture.focalLengthProperty(),new NumberStringConverter());
        dazzleNumberTF_ID.textProperty().bindBidirectional(MainPicture.dazzleNumberProperty(),new NumberStringConverter());
        photographerCB_ID.valueProperty().bindBidirectional(MainPicture.authorProperty());
    }

    /**
     * Unbinden der Properties des gerade aktiven Bildes von den Properties der UI
     * Wird benötigt beim Wechsel des anzuzeigenden Bildes, damit die Daten nicht überschrieben werden
     */
    public void unbindMainPicture(){
        picNameTF_ID.textProperty().unbindBidirectional(MainPicture.fileNameProperty());
        picFormatTF_ID.textProperty().unbindBidirectional(MainPicture.fileExtProperty());
        keywordsTA_ID.textProperty().unbindBidirectional(MainPicture.keywordsProperty());
        descriptionTA_ID.textProperty().unbindBidirectional(MainPicture.descriptionProperty());
        dateTimeTF_ID.textProperty().unbindBidirectional(MainPicture.creationTimeProperty());
        locationTF_ID.textProperty().unbindBidirectional(MainPicture.locationProperty());
        expoTimeTF_ID.textProperty().unbindBidirectional(MainPicture.exposureTimeProperty());
        focalLengthTF_ID.textProperty().unbindBidirectional(MainPicture.focalLengthProperty());
        dazzleNumberTF_ID.textProperty().unbindBidirectional(MainPicture.dazzleNumberProperty());
        photographerCB_ID.valueProperty().unbindBidirectional(MainPicture.authorProperty());
    }

    /**
     * Auslösen des Speichervorgangs des aktuell angezeigten Bildes be idrücken des Speichern-Buttons
     * @param mouseEvent Mouseevent (Clicked) auf welches reagiert wird
     */
    public void mainSaveBtnClicked(MouseEvent mouseEvent) {
        if(BL.isValidPicture(MainPicture)){
            BL.savePictureData(MainPicture);
            Logging.LogDebug(this.getClass(),"Picture saved");
        }

    }

    /**
     * Laden der anzuzeigenden Bilder.
     * @param keyword (Type: String) Wenn ein keyword angegeben wird, werden beim Laden nur Bilder
     *                berücksichtigt, welche dieses Keyword als Ganzes oder als Teil in der Keywordliste haben
     *                Wenn keyword = null oder "" --> Alle verfügbaren bilder werden geladen
     */
    public void loadPictures(String keyword){
        if(keyword == null){
             Pictures = BL.getAllPictures();
        }
        else{
            Pictures = BL.getPicturesByKeyword(keyword);
        }
        if(Pictures!=null && Pictures.size()!=0){
            changeMainPicture(Pictures.get(0));
        }
    }

    /**
     * Laden der Authoren, welche in der Datenbank zur Verfügung stehen
     * Setzen der Combobox auf den Author des gerade aktiven Bildes, falls schon geladen
     */
    public void loadAuthors(){
        if(MainPicture!=null)
            photographerCB_ID.valueProperty().unbindBidirectional(MainPicture.authorProperty());
        photographerCB_ID.getItems().clear();
        ObservableList<P_Model_Author> authors = FXCollections.observableArrayList();
        List<P_Model_Author> tmp = BL.getAllAuthors();
        if(tmp!=null && tmp.size()!=0){
            for(P_Model_Author a : tmp){
                authors.add(a);
            }
        }
        photographerCB_ID.getItems().addAll(authors);

        if(maybeNewAuthor){
            MainPicture.setAuthor(BL.getAuthorByID(MainPicture.getAuthor().getID()));
            photographerCB_ID.getSelectionModel().selectFirst();
            P_Model_Author t = (P_Model_Author) photographerCB_ID.getSelectionModel().getSelectedItem();
            autochange=true;
            while(!BL.isEqualAuthor(t,MainPicture.getAuthor())){
                photographerCB_ID.getSelectionModel().selectNext();
                t = (P_Model_Author) photographerCB_ID.getSelectionModel().getSelectedItem();
            }
            autochange=false;
            Logging.LogDebug(this.getClass(),"Author found in CB");
            photographerCB_ID.valueProperty().bindBidirectional(MainPicture.authorProperty());
        }
        maybeNewAuthor = false;
    }

    /**
     * Laden / Wechseln des anzuzeigenden Bildes. Beim Wechsel werden alle Daten des zuletzt aktive Bildes
     * gespeichert falls die Validierung eine gültige Änderung ergibt.
     * Wird auch beim erstmaligen Start aufgerufen (Check ob aktives Bild = null ist)
     * @param pic (Type: P_Model_Picture) Das neue zu ladende Bild
     */
    public void changeMainPicture(P_Model_Picture pic){
        //save changed data
        if(MainPicture!=null){
            unbindMainPicture();
            if(BL.isValidPicture(MainPicture)){
                BL.savePictureData(MainPicture);
            }
        }
        //change picture
        MainPicture = pic;
        mainPicture_ID.setImage(new Image(new File(MainPicture.getFilePath()).toURI().toString()));
        mainPicture_ID.fitHeightProperty().bind(picPane_ID.heightProperty());
        mainPicture_ID.fitWidthProperty().bind(picPane_ID.widthProperty());

        photographerCB_ID.getSelectionModel().selectFirst();
        P_Model_Author tmp = (P_Model_Author) photographerCB_ID.getSelectionModel().getSelectedItem();
        autochange=true;
        while(!BL.isEqualAuthor(tmp,MainPicture.getAuthor())){
            photographerCB_ID.getSelectionModel().selectNext();
            tmp = (P_Model_Author) photographerCB_ID.getSelectionModel().getSelectedItem();
        }
        autochange=false;
        applyBindings();
    }

    /**
     * Laden der Bildergallerie aus den verfügbaren Bildern. Jedes Bild bekommt ein setOnMouseClick
     * Eventhandler, welcher den Wechsel des Bildes veranlasst
     */
    public void loadImagesGallery(){
        HBox box = new HBox();
        for(P_Model_Picture pic : Pictures){
            pic.getIv().fitHeightProperty().bind(galleryScrollPane.heightProperty());
            pic.getIv().setPreserveRatio(true);
            pic.getIv().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    changeMainPicture(pic);
                }
            });

            box.getChildren().add(pic.getIv());
        }
        galleryScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        galleryScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        galleryScrollPane.setContent(box);

        galleryScrollPane.setOnScroll(event -> {
            if(event.getDeltaX() == 0 && event.getDeltaY() != 0) {
                galleryScrollPane.setHvalue(galleryScrollPane.getHvalue() - event.getDeltaY() / box.getWidth()*4);
            }
        });

    }
}
