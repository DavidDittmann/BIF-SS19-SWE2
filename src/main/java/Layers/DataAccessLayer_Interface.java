package Layers;

import DataModels.*;

import java.time.LocalDateTime;
import java.util.List;

public interface DataAccessLayer_Interface {
    void generateDatabase();
    void checkupDatabase();
    PictureData getPicture(int id);
    PhotographerData getPhotographer(int id);
    ExifData getExifData(int id);
    ItpcData getItpcData(int id);
    void createEntry(String photoFilePath);
    void createPhotographer(PhotographerData ph);
    void editPhotographer(int id);
    void createExifData(ExifData exif);
    void editExifData(int id);
    void createItpcData(ItpcData itpc);
    void editItpcData(int id);
    List<PictureData> searchPhotographer(String name);
    List<PictureData> searchExif(LocalDateTime datetime, double focalLength, double exposureTime, double dazzleNumber);
    List<PictureData> searchItpc(String pictureName, String pictureExtension, String keyword);
}
