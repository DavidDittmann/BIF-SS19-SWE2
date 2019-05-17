package Layers;

import DataModels.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DataAccessLayer_Interface {
    void generateDatabase();
    boolean doesTableExist(String tablename);
    void createTable(String prepStmt);
    void updateDatabase();
    PhotographerData getPhotographerData(long id);
    ExifData getExifData(long id);
    ItpcData getItpcData(long id);
    long createPictureEntry(String photoFilePath);
    void editPicturePath(PictureData pic) throws Exception;
    long createPhotographer(PhotographerData ph);
    void editPhotographer(long id, PhotographerData ph);
    long createExifData(ExifData exif);
    void editExifData(long id, ExifData exif);
    long createItpcData(ItpcData itpc);
    void editItpcData(long id, ItpcData itpc);
    List<PictureData> getAllPictures();
    Set<String> getPicturePathSet();
    String getPicturePathByID(long id);
    List<PictureData> searchPhotographer(String name);
    List<PictureData> searchExif(LocalDateTime datetime, double focalLength, double exposureTime, double dazzleNumber);
    List<PictureData> searchItpc(String pictureName, String pictureExtension, String keyword);
}
