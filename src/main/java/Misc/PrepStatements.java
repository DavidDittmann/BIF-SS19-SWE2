package Misc;

public class PrepStatements {
    public static String CREATE_TABLE_PICTURES =
            "CREATE TABLE PICTURES (" +
            "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "FILEPATH VARCHAR(255) NOT NULL," +
            "KEYWORDS VARCHAR(300)," +
            "DESCRIPTION VARCHAR(300)," +
            "DATE_CREATED TIMESTAMP," +
            "LOCATION VARCHAR(100)," +
            "FOCAL_LENGTH FLOAT DEFAULT 0," +
            "EXPOSURE_TIME FLOAT DEFAULT 0," +
            "DAZZLE_TIME FLOAT DEFAULT 0," +
            "FK_AUTHOR_ID BIGINT REFERENCES AUTHORS(ID)," +
            "CONSTRAINT primary_key_pictures PRIMARY KEY(ID)" +
            ")";

    public static String CREATE_TABLE_AUTHORS =
            "CREATE TABLE AUTHORS(" +
            "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "NAME VARCHAR(100)," +
            "LASTNAME VARCHAR(50) NOT NULL," +
            "BIRTHDAY DATE," +
            "NOTES LONG VARCHAR," +
            "CONSTRAINT primary_key_authors PRIMARY KEY(ID)" +
            ")";

    public static String INSERT_PICTURE =
            "INSERT INTO PICTURES " +
            "(FILEPATH, KEYWORDS, DESCRIPTION, DATE_CREATED, LOCATION, FOCAL_LENGTH, EXPOSURE_TIME, DAZZLE_TIME, FK_AUTHOR_ID)" +
            "VALUES (?,?,?,?,?,?,?,?,?)";

    public static String INSERT_AUTHOR  =
            "INSERT INTO AUTHORS" +
            "(NAME, LASTNAME, BIRTHDAY, NOTES)" +
            "VALUES (?,?,?,?)";

    public static String UPDATE_PICTURE =
            "UPDATE PICTURES SET " +
            "FILEPATH=?, KEYWORDS=?, DESCRIPTION=?, DATE_CREATED=?, LOCATION=?, FOCAL_LENGTH=?, EXPOSURE_TIME=?, DAZZLE_TIME=?, FK_AUTHOR_ID=? " +
            "WHERE ID=?";

    public static String UPDATE_AUTHOR =
            "UPDATE AUTHORS SET" +
            "NAME=?, LASTNAME=?, BIRTHDAY=?, NOTES=?" +
            "WHERE ID=?";

    public static String GET_ALL_PICTURES  =
            "SELECT ID, FILEPATH, KEYWORDS, DESCRIPTION, DATE_CREATED, LOCATION, FOCAL_LENGTH, EXPOSURE_TIME, DAZZLE_TIME, FK_AUTHOR_ID FROM PICTURES";

    public static String GET_ALL_AUTHORS =
            "SELECT ID, NAME, LASTNAME, BIRTHDAY, NOTES FROM AUTHORS";

    public static String GET_AUTHOR_BY_ID =
            "SELECT NAME, LASTNAME, BIRTHDAY, NOTES FROM AUTHORS WHERE ID=?";
}
