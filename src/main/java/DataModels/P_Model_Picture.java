package DataModels;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

public class P_Model_Picture {
    private String FilePath = null;
    private long ID = 0;
    private StringProperty FileName = new SimpleStringProperty();
    private StringProperty FileExt = new SimpleStringProperty();
    private StringProperty Keywords = new SimpleStringProperty();
    private StringProperty Description = new SimpleStringProperty();
    private StringProperty CreationTime = new SimpleStringProperty();
    private StringProperty Location = new SimpleStringProperty();
    private DoubleProperty FocalLength = new SimpleDoubleProperty();
    private DoubleProperty ExposureTime = new SimpleDoubleProperty();
    private DoubleProperty DazzleNumber = new SimpleDoubleProperty();
    private ObjectProperty<P_Model_Author> Author = new SimpleObjectProperty<>();
    private ImageView iv = null;

    public P_Model_Picture(PictureData pic, P_Model_Author author){
        FilePath = pic.get_filepath();
        ID = pic.get_id();
        FileName.set(FilenameUtils.getBaseName(pic.get_filepath()));
        FileExt.set(FilenameUtils.getExtension(pic.get_filepath()));
        Keywords.set(pic.get_keywords());
        Description.set(pic.get_desc());
        CreationTime.set(pic.get_date().toString());
        Location.set(pic.get_location());
        FocalLength.set(pic.get_focal());
        ExposureTime.set(pic.get_exposure());
        DazzleNumber.set(pic.get_dazzle());
        Author.set(author);
        iv = new ImageView(new Image(new File(pic.get_filepath()).toURI().toString()));
    }

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public P_Model_Author getAuthor() {
        return Author.get();
    }

    public ObjectProperty<P_Model_Author> authorProperty() {
        return Author;
    }

    public void setAuthor(P_Model_Author author) {
        this.Author.set(author);
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileName() {
        return FileName.get();
    }

    public StringProperty fileNameProperty() {
        return FileName;
    }

    public void setFileName(String fileName) {
        this.FileName.set(fileName);
    }

    public String getFileExt() {
        return FileExt.get();
    }

    public StringProperty fileExtProperty() {
        return FileExt;
    }

    public void setFileExt(String fileExt) {
        this.FileExt.set(fileExt);
    }

    public String getKeywords() {
        return Keywords.get();
    }

    public StringProperty keywordsProperty() {
        return Keywords;
    }

    public void setKeywords(String keywords) {
        this.Keywords.set(keywords);
    }

    public String getDescription() {
        return Description.get();
    }

    public StringProperty descriptionProperty() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description.set(description);
    }

    public String getCreationTime() {
        return CreationTime.get();
    }

    public StringProperty creationTimeProperty() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        this.CreationTime.set(creationTime);
    }

    public String getLocation() {
        return Location.get();
    }

    public StringProperty locationProperty() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location.set(location);
    }

    public double getFocalLength() {
        return FocalLength.get();
    }

    public DoubleProperty focalLengthProperty() {
        return FocalLength;
    }

    public void setFocalLength(double focalLength) {
        this.FocalLength.set(focalLength);
    }

    public double getExposureTime() {
        return ExposureTime.get();
    }

    public DoubleProperty exposureTimeProperty() {
        return ExposureTime;
    }

    public void setExposureTime(double exposureTime) {
        this.ExposureTime.set(exposureTime);
    }

    public double getDazzleNumber() {
        return DazzleNumber.get();
    }

    public DoubleProperty dazzleNumberProperty() {
        return DazzleNumber;
    }

    public void setDazzleNumber(double dazzleNumber) {
        this.DazzleNumber.set(dazzleNumber);
    }
}
