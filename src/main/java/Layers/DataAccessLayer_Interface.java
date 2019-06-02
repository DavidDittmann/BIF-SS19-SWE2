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

    long createPicture(String filePath);
    long createAuthor(AuthorData a);
    void editPicture(PictureData picture);
    void editAuthor( AuthorData a);

    List<PictureData> getAllPictures();
    List<PictureData> getAllPicturesParallel();
    List<PictureData> getPicturesByKeyword(String keyword);
    Set<String> getPicturePathSet();
    AuthorData getAuthorByID(long id);
    List<AuthorData> getAllAuthors();
}
