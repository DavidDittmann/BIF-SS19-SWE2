package Layers;

//https://www.baeldung.com/java-connection-pooling
//https://www.tutorialspoint.com/apache_derby/apache_derby_create_table.htm

//TODO: IMPLEMENT SEARCH

import DataModels.*;
import Misc.PrepStatements;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataAccessLayer implements DataAccessLayer_Interface{
    public DataAccessLayer() {
    }

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

    @Override
    public void updateDatabase() {
        File folder = new File("img"); //TODO: config file
        List<File> filelist = listFilesInFolder(folder);
        Set<String> picturePathSet = getPicturePathSet();

        for(File f : filelist){
            if(!picturePathSet.contains(f.getAbsolutePath())){
                createPicture(f.getAbsolutePath());
            }
        }
    }

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

    @Override
    public List<PictureData> getAllPicturesParallel(){
        return getAllPictures().parallelStream().filter(img->img.get_filepath().contains("")).collect(Collectors.toList());
    }

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

    @Override
    public List<PictureData> getPicturesByKeyword(String keyword){
        //TODO: get_filePath mit getKeywords ersetzen
        return getAllPictures().parallelStream().filter(img->img.get_keywords().toLowerCase().contains(keyword)).collect(Collectors.toList());
    }

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

    private List<File> listFilesInFolder(File rootFolder) {
        List<File> fileList = new LinkedList<>();
        for (File fileEntry : rootFolder.listFiles()) {
            if (fileEntry.isDirectory()) {
                fileList.addAll(listFilesInFolder(fileEntry));
            } else {
                fileList.add(fileEntry);
            }
        }
        return fileList;
    }

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
