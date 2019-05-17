package Layers;

public class PreparedStatements {
    public static String createTablePhotographers = "CREATE TABLE PHOTOGRAPHERS(" +
                                                    "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                                                    "NAME VARCHAR(30)," +
                                                    "LASTNAME VARCHAR(50)," +
                                                    "ADDRESS VARCHAR(150)," +
                                                    "CONSTRAINT primary_key_photographers PRIMARY KEY(ID)" +
                                                    ")";

    public static String createTableExif =  "CREATE TABLE EXIF(" +
                                            "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                                            "DATETIME TIMESTAMP NOT NULL," +
                                            "FOCAL_LENGTH FLOAT DEFAULT 0," +
                                            "EXPOSURE_TIME FLOAT DEFAULT 0," +
                                            "DAZZLE_TIME FLOAT DEFAULT 0," +
                                            "CONSTRAINT primary_key_exif PRIMARY KEY(ID)" +
                                            ")";

    public static String createTableItpc =  "CREATE TABLE ITPC(" +
                                            "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                                            "PICTURE_NAME VARCHAR(300) NOT NULL," +
                                            "PICTURE_EXT VARCHAR(300) NOT NULL," +
                                            "KEYWORDS VARCHAR(300)," +
                                            "CONSTRAINT primary_key_itpc PRIMARY KEY(ID)" +
                                            ")";

    public static String createTablePictures =  "CREATE TABLE PICTURES(" +
                                                "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                                                "FILEPATH VARCHAR(255) NOT NULL," +
                                                "FK_PHOTOGRAPHER_ID BIGINT NOT NULL," +
                                                "FK_EXIF_ID BIGINT NOT NULL," +
                                                "FK_ITPC_ID BIGINT NOT NULL," +
                                                "CONSTRAINT primary_key_pictures PRIMARY KEY(ID)," +
                                                "CONSTRAINT picture_photographer_fk FOREIGN KEY (ID) REFERENCES PHOTOGRAPHERS(ID)," +
                                                "CONSTRAINT picture_exif_fk FOREIGN KEY (ID) REFERENCES EXIF(ID)," +
                                                "CONSTRAINT picture_itpc_fk FOREIGN KEY (ID) REFERENCES ITPC(ID)" +
                                                ")";

    public static String prepInsertPicture =    "INSERT INTO PICTURES (FILEPATH,FK_PHOTOGRAPHER_ID,FK_EXIF_ID,FK_ITPC_ID) " +
                                                "VALUES (?,?,?,?)";

    public static String prepInsertPhotographer =   "INSERT INTO PHOTOGRAPHERS (NAME,LASTNAME,ADDRESS) " +
                                                    "VALUES (?,?,?)";

    public static String prepInsertITPC =   "INSERT INTO ITPC (PICTURE_NAME,PICTURE_EXT,KEYWORDS) " +
                                            "VALUES (?,?,?)";

    public static String prepInsertEXIF =   "INSERT INTO EXIF (DATETIME,FOCAL_LENGTH,EXPOSURE_TIME,DAZZLE_TIME) " +
                                            "VALUES (?,?,?,?)";

    public static String prepUpdatePicturePath =   "UPDATE PICTURES SET FILEPATH=? WHERE ID=?";

    public static String prepUpdatePhotographer =   "UPDATE PHOTOGRAPHERS SET NAME=?, LASTNAME=?, ADDRESS=? WHERE ID=?";

    public static String prepUpdateEXIF =   "UPDATE EXIF SET DATETIME=?, FOCAL_LENGTH=?, EXPOSURE_TIME=?, DAZZLE_TIME=? WHERE ID=?";

    public static String prepUpdateITPC =   "UPDATE ITPC SET PICTURE_NAME=?, PICTURE_EXT=?, KEYWORDS=? WHERE ID=?";

    public static String prepSelectAllPictures = "Select ID, FILEPATH, FK_PHOTOGRAPHER_ID, FK_EXIF_ID, FK_ITPC_ID FROM PICTURES";

    public static String prepSelectPhotographerByID = "Select ID,NAME,LASTNAME,ADDRESS FROM PHOTOGRAPHERS WHERE ID=?";

    public static String prepSelectEXIFByID = "Select ID,DATETIME,FOCAL_LENGTH,EXPOSURE_TIME,DAZZLE_TIME FROM EXIF WHERE ID=?";

    public static String prepSelectITPCByID = "Select ID,PICTURE_NAME,PICTURE_EXT,KEYWORDS FROM ITPC WHERE ID=?";

    public static String prepSelectPicturePaths = "Select FILEPATH FROM PICTURES";

    public static String prepSelectPictureByID = "Select FILEPATH FROM PICTURES WHERE ID=?";
}
