package Layers;

//https://www.baeldung.com/java-connection-pooling
//https://www.tutorialspoint.com/apache_derby/apache_derby_create_table.htm

/*
photo -> id, filepath, fk_id_photographer, fk_id_exif, fk_id_itpc
photographer -> id, name, address
exif -> id, datetime, focalLength, exposureTime, dazzleNumber
iptc -> id, pictureName, pictureExtension, keywords
*/

import DataModels.ExifData;
import DataModels.ItpcData;
import DataModels.PhotographerData;
import DataModels.PictureData;

import java.time.LocalDateTime;
import java.util.List;

public class DataAccessLayer implements DataAccessLayer_Interface{
    public DataAccessLayer() {

    }

    @Override
    public void generateDatabase() {

    }

    @Override
    public void checkupDatabase() {

    }

    @Override
    public PictureData getPicture(int id) {
        return null;
    }

    @Override
    public PhotographerData getPhotographer(int id) {
        return null;
    }

    @Override
    public ExifData getExifData(int id) {
        return null;
    }

    @Override
    public ItpcData getItpcData(int id) {
        return null;
    }

    @Override
    public void createEntry(String photoFilePath) {

    }

    @Override
    public void createPhotographer(PhotographerData ph) {

    }

    @Override
    public void editPhotographer(int id) {

    }

    @Override
    public void createExifData(ExifData exif) {

    }

    @Override
    public void editExifData(int id) {

    }

    @Override
    public void createItpcData(ItpcData itpc) {

    }

    @Override
    public void editItpcData(int id) {

    }

    @Override
    public List<PictureData> searchPhotographer(String name) {
        return null;
    }

    @Override
    public List<PictureData> searchExif(LocalDateTime datetime, double focalLength, double exposureTime, double dazzleNumber) {
        return null;
    }

    @Override
    public List<PictureData> searchItpc(String pictureName, String pictureExtension, String keyword) {
        return null;
    }
}
