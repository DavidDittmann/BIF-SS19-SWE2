package Layers;

//https://www.baeldung.com/java-connection-pooling
//https://www.tutorialspoint.com/apache_derby/apache_derby_create_table.htm

import DataModels.*;
import Misc.ConfigManager;
import Misc.PrepStatements;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Der DataAccessLayer ist die Schnittstelle der Applikation zum Filesystem
 * Zuständig für alle Fileoperationen (Ausgenommen des ConfigManagers)
 */
public class DataAccessLayer implements DataAccessLayer_Interface{
    private ConfigManager conf;
    public DataAccessLayer() {
        conf = ConfigManager.getInstance();
    }


    /**
     * Funktion zum Generieren der Bilderdatenbank. Falls Datenbank bereits vorhanden, wird keine
     * Aktion ausgeführt
     */
    @Override
    public void generateDatabase() {
        if(!doesTableExist("AUTHORS")){
            createTable(PrepStatements.CREATE_TABLE_AUTHORS);
            LocalDate n = LocalDate.now();
            createAuthor(new AuthorData("Max","Mustermann",n,"danke"));
            createAuthor(new AuthorData("Roman","Freud",n,"deine"));
            createAuthor(new AuthorData("Susanne","Jahn",n,"auch"));
        }
        if(!doesTableExist("PICTURES")){
            createTable(PrepStatements.CREATE_TABLE_PICTURES);
        }
    }


    /**
     * Methode zum checken, ob eine Tabelle bereits in der Datenbank vorhanden ist
     * @param tablename (Type: String) Name der Tabelle, welche überprüft werden soll
     * @return
     */
    @Override
    public boolean doesTableExist(String tablename) {
        tablename = tablename.toLowerCase();
        Connection conn=null;
        ResultSet rs=null;
        try {
            conn = DBCPDataSource.getConnection();
            DatabaseMetaData dbMeta = conn.getMetaData();
            rs = dbMeta.getTables(null,"APP",null, new String []{"TABLE"});
            while(rs.next()){
                if (rs.getString("TABLE_NAME").toLowerCase().equals(tablename)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(conn!=null) {
                try {
                    if(rs!=null)
                        rs.close();
                    if(conn!=null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Erstellen einer Tabelle in der Datenbank mit vorgegebenen Prepared Statement
     * @param prepStmtStr (Type: String) Das Prepared Statement, mit dem die Tabelle erstellt wird
     */
    @Override
    public void createTable(String prepStmtStr) {
        PreparedStatement prepStmt=null;
        Connection conn=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(prepStmtStr);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(null,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checken des angegebenen Ordners (Configfile) in dem die Bilder liegen, ob
     * es neue Bilder gibt, die nachgeladen werden müssen
     */
    @Override
    public void updateDatabase() {
        generateDatabase();
        File folder = null;
        try {
            folder = new File(conf.getImageBaseFolder());
            List<File> filelist = listFilesInFolder(folder);
            Set<String> picturePathSet = getPicturePathSet();

            for(File f : filelist){
                if(!picturePathSet.contains(f.getAbsolutePath())){
                    createPicture(f.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellen eines Bildeintrages in der Datenbank.
     * ! Das Bild bekommt Pseudodaten, da echte Bilddaten (EXIF/ITPC) nicht ausgelesen werden können
     * @param filePath (Type: String) Pfad zum Bild, welches geladen werden soll
     * @return (Type: Long) ID des angelegten Tabelleneintrages
     */
    @Override
    public long createPicture(String filePath) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        long ret=0;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.INSERT_PICTURE,Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, filePath);
            prepStmt.setString(2, "");  //keyword list
            prepStmt.setString(3, "");  //Description
            prepStmt.setTimestamp(4,convertToDatabaseColumn(LocalDateTime.now()));
            prepStmt.setString(5,"");   //Location
            prepStmt.setDouble(6,generateRandomDouble(0,30));
            prepStmt.setDouble(7,generateRandomDouble(0,30));
            prepStmt.setDouble(8,generateRandomDouble(0,30));
            prepStmt.setLong(9,new Random().nextInt(3)+1);

            long affectedRows = prepStmt.executeUpdate();
            if(affectedRows == 0){
                throw new SQLException("Creating Photographer failed, no rows affected");
            }
            ret = getGenKeys(prepStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(null,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * Erstellen eines Authors in der Datenbank
     * @param a (Type: AuthorData) Die Daten des Authors, welcher abgespeichert werden soll
     * @return (Type: Long) ID des Tabelleneintrags der erstellt wurde
     */
    @Override
    public long createAuthor(AuthorData a) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        long ret = -1;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1,a.get_name());
            prepStmt.setString(2,a.get_lastName());
            prepStmt.setDate(3, Date.valueOf(a.get_birthday()));
            prepStmt.setString(4,a.get_notes());
            int affectedRows = prepStmt.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Creating Photographer failed, no rows affected");
            }
            ret = getGenKeys(prepStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(null,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * Datenbank Update eines bereits vorhandenen Bildereintrages
     * @param pic (Type: PictureData) Daten des zu ändernden Bildes
     */
    @Override
    public void editPicture(PictureData pic) {
        Connection conn = null;
        PreparedStatement prepStmt=null;

        try{
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.UPDATE_PICTURE, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1,pic.get_filepath());
            prepStmt.setString(2,pic.get_keywords());
            prepStmt.setString(3,pic.get_desc());
            prepStmt.setTimestamp(4,convertToDatabaseColumn(pic.get_date()));
            prepStmt.setString(5,pic.get_location());
            prepStmt.setDouble(6,pic.get_focal());
            prepStmt.setDouble(7,pic.get_exposure());
            prepStmt.setDouble(8,pic.get_dazzle());
            prepStmt.setLong(9,pic.get_fk_author());
            prepStmt.setLong(10,pic.get_id());
            int affectedRows = prepStmt.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("Editing Picture failed, no rows affected");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                closeConnection(null,prepStmt,conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Datenbank Update eines bereits vorhandenen Authoreneintrages
     * @param a (Type: AuthorData) Daten des zu ändernden Authors
     */
    @Override
    public void editAuthor(AuthorData a) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.UPDATE_AUTHOR);
            prepStmt.setString(1,a.get_name());
            prepStmt.setString(2,a.get_lastName());
            prepStmt.setDate(3,Date.valueOf(a.get_birthday()));
            prepStmt.setString(4,a.get_notes());
            prepStmt.setLong(5,a.get_id());
            long affectedRows = prepStmt.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Editing Photographer failed, no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(null,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Laden aller Bildereinträge aus der Datenbank
     * @return (Type: List<PictureData>) Liste an BilderDaten
     */
    @Override
    public List<PictureData> getAllPictures() {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        List<PictureData> resultList = new LinkedList<>();
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.GET_ALL_PICTURES);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                resultList.add(new PictureData(
                        rs.getLong("ID"),rs.getString("FILEPATH"),rs.getString("KEYWORDS"),
                        rs.getString("DESCRIPTION"),convertToEntityAttribute(rs.getTimestamp("DATE_CREATED")),
                        rs.getString("LOCATION"),rs.getDouble("FOCAL_LENGTH"),rs.getDouble("EXPOSURE_TIME"),
                        rs.getDouble("DAZZLE_TIME"),rs.getLong("FK_AUTHOR_ID")

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(rs,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultList;
    }

    /**
     * Laden aller Bilderdaten aus der Datenbank parallel (Suchbeschleunigung)
     * @return (Type: List<PictureData>) Liste an gefundenen Bildeinträgen in der DB
     */
    @Override
    public List<PictureData> getAllPicturesParallel(){
        return getAllPictures().parallelStream().filter(img->img.get_filepath().contains("")).collect(Collectors.toList());
    }

    /**
     * Laden der Dateipfade aller bekannten Bilder. Wird benötigt zum checken, ob neue Bilder
     * im angegebenen Ordner (ConfigManager) vorhanden sind
     * @return (Type: Set<String>) Set and bekannten Bildpfaden
     */
    @Override
    public Set<String> getPicturePathSet() {
        Set<String> result = new HashSet<>();
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;

        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.GET_ALL_PICTURES);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                result.add(rs.getString("FILEPATH"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(rs,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * Authordaten holen, mit Angabe der dazugehörigen ID
     * @param id (Type: Long) ID des Authoreintrages in der Datenbank
     * @return (Type: AuthorData) Daten des gesuchten wenn vorhandenen Authors
     */
    @Override
    public AuthorData getAuthorByID(long id) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        AuthorData author = null;

        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.GET_AUTHOR_BY_ID);
            prepStmt.setLong(1,id);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                author = new AuthorData(id,rs.getString("NAME"),rs.getString("LASTNAME"),rs.getDate("BIRTHDAY").toLocalDate(),rs.getString("NOTES"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(rs,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return author;
    }

    /**
     * Laden aller bekannten Authoren aus der Datenbank
     * @return (Type: List<AuthorData>) List an bekannten Authoren
     */
    @Override
    public List<AuthorData> getAllAuthors() {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        List<AuthorData> resultList = new LinkedList<>();
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PrepStatements.GET_ALL_AUTHORS);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                resultList.add(new AuthorData(rs.getLong("ID"),rs.getString("NAME"),rs.getString("LASTNAME"),rs.getDate("BIRTHDAY").toLocalDate(),rs.getString("NOTES")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(rs,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultList;
    }

    /**
     * Laden aller Bilder mit bestimmten Keyword in der Keywordliste
     * @param keyword (Type: String) Keyword, nach dem gesucht wird
     * @return (Type: List<PictureData>) Liste an gefundenen Bildern mit enthaltenen Keyword
     */
    @Override
    public List<PictureData> getPicturesByKeyword(String keyword){
        return getAllPictures().parallelStream().filter(img->img.get_keywords().toLowerCase().contains(keyword)).collect(Collectors.toList());
    }

    /**
     * Laden der ID eines zuvor erstellten Eintrages in der Datenbank
     * @param prepStmt Das zuvor ausgeführte Prepared Statement
     * @return (Type: Long) Erstellte ID des Datensatzes
     * @throws SQLException
     */
    private long getGenKeys(PreparedStatement prepStmt) throws SQLException{
        long ret = -1;
        try (ResultSet generatedKeys = prepStmt.getGeneratedKeys()){
            if(generatedKeys.next()){
                ret = generatedKeys.getLong(1);
            }
            else{
                throw new SQLException("No Keys generated");
            }
        }
        return ret;
    }

    private double generateRandomDouble(double min, double max){
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    /**
     * Auflistung aller Files (auch rekursiv) im angegeben Ordner (Bildersuche)
     * @param rootFolder (Type: File) Basisordner, in dem gesucht werden soll
     * @return (Type: List<File>) gefundene Files im angegebenen Ordner und Unterordnern
     */
    private List<File> listFilesInFolder(File rootFolder) {
        List<File> fileList = new LinkedList<>();
        for (File fileEntry : rootFolder.listFiles()) {
            if (fileEntry.isDirectory()) {
                fileList.addAll(listFilesInFolder(fileEntry));
            } else {
                if(FilenameUtils.isExtension(fileEntry.getAbsolutePath(),"jpg") || FilenameUtils.isExtension(fileEntry.getAbsolutePath(),"png"))
                    fileList.add(fileEntry);
            }
        }
        return fileList;
    }

    /**
     * Schließen der atkiven angegeben Verbindungen zur Datenbank (Connection, PreparedStatement und Resultset)
     * @param rs (Type: ResultSet)
     * @param ps (Type: PreparedStatement)
     * @param conn (Type: Connection)
     * @throws SQLException
     */
    private void closeConnection(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException{
        if(rs!=null)
            rs.close();
        if(ps!=null)
            ps.close();
        if(conn!=null)
            conn.close();
    }

    public Timestamp convertToDatabaseColumn(LocalDateTime ldt) {
        return Timestamp.valueOf(ldt);
    }
    public LocalDateTime convertToEntityAttribute(Timestamp ts) {
        if(ts!=null){
            return ts.toLocalDateTime();
        }
        return null;
    }
}
