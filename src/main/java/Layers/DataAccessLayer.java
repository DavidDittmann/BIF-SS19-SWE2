package Layers;

//https://www.baeldung.com/java-connection-pooling
//https://www.tutorialspoint.com/apache_derby/apache_derby_create_table.htm

//TODO: IMPLEMENT SEARCH

import DataModels.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataAccessLayer implements DataAccessLayer_Interface{
    public DataAccessLayer() {
    }

    @Override
    public void generateDatabase() {
        if(!doesTableExist("PHOTOGRAPHERS")){
            createTable(PreparedStatements.createTablePhotographers);
        }
        if(!doesTableExist("EXIF")){
            createTable(PreparedStatements.createTableExif);
        }
        if(!doesTableExist("ITPC")){
            createTable(PreparedStatements.createTableItpc);
        }
        if(!doesTableExist("PICTURES")){
            createTable(PreparedStatements.createTablePictures);
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
                createPictureEntry(f.getAbsolutePath());
            }
        }
    }

    @Override
    public PhotographerData getPhotographerData(long id) {
        PhotographerData ph = null;
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepSelectPhotographerByID);
            prepStmt.setLong(1,id);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                ph = new PhotographerData(rs.getLong("ID"),rs.getString("NAME"),rs.getString("LASTNAME"),rs.getString("ADDRESS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(rs,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return ph;
    }

    @Override
    public ExifData getExifData(long id) {
        ExifData exif=null;
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepSelectEXIFByID);
            prepStmt.setLong(1,id);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                exif = new ExifData(rs.getLong("ID"),rs.getTimestamp("DATETIME").toLocalDateTime(),rs.getLong("FOCAL_LENGTH"),rs.getLong("EXPOSURE_TIME"),rs.getLong("DAZZLE_TIME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(prepStmt!=null) {
                try {
                    closeConnection(rs,prepStmt,conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return exif;
    }

    @Override
    public ItpcData getItpcData(long id) {
        ItpcData itpc = null;
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepSelectITPCByID);
            prepStmt.setLong(1,id);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                itpc = new ItpcData(rs.getLong("ID"),rs.getString("PICTURE_NAME"),rs.getString("PICTURE_EXT"),rs.getString("KEYWORDS"));
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

        return itpc;
    }

    @Override
    public long createPictureEntry(String photoFilePath) {
        String pictureName = FilenameUtils.getBaseName(photoFilePath);
        String pictureExt = FilenameUtils.getExtension(photoFilePath);

        PhotographerData ph = new PhotographerData("Anton","Braumeister","Beispielloserweg 7");
        ExifData exif = new ExifData(LocalDateTime.now(),generateRandomDouble(0,30),generateRandomDouble(0,30),generateRandomDouble(0,30));
        ItpcData itpc = new ItpcData(pictureName,pictureExt,null);

        long ph_id = createPhotographer(ph);
        long exif_id = createExifData(exif);
        long itpc_id = createItpcData(itpc);

        if(ph_id == -1 || exif_id == -1 || itpc_id == -1)
            return -1;

        PictureData pic = new PictureData(photoFilePath, ph_id, exif_id, itpc_id);
        Connection conn=null;
        PreparedStatement prepStmt=null;
        long ret=0;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepInsertPicture,Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, photoFilePath);
            prepStmt.setLong(2,ph_id);
            prepStmt.setLong(3,exif_id);
            prepStmt.setLong(4,itpc_id);

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
    public void editPicturePath(PictureData pic) throws Exception {
        File F = new File(pic.get_filepath());
        if(F.exists()){
            throw new Exception("Cannot rename File! There is an existing one with the same name!");
        }
        Connection conn=null;
        PreparedStatement prepStmt=null;

        File oldF = new File(getPicturePathByID(pic.getId()));
        File newF = new File(pic.get_filepath());

        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepUpdatePicturePath);
            prepStmt.setString(1,pic.get_filepath());
            prepStmt.setLong(2,pic.getId());
            long affectedRows = prepStmt.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Editing Picture failed, no rows affected");
            }else{
                oldF.renameTo(newF);
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
    public long createPhotographer(PhotographerData ph) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        long ret = -1;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepInsertPhotographer, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1,ph.get_name());
            prepStmt.setString(2,ph.get_lastName());
            prepStmt.setString(3,ph.get_address());
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
    public void editPhotographer(long id, PhotographerData ph) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepUpdatePhotographer);
            prepStmt.setString(1,ph.get_name());
            prepStmt.setString(2,ph.get_lastName());
            prepStmt.setString(3,ph.get_address());
            prepStmt.setLong(4,ph.get_id());
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
    public long createExifData(ExifData exif) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        long ret = -1;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepInsertEXIF, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setTimestamp(1,Timestamp.valueOf(exif.get_dateTime()));
            prepStmt.setDouble(2,exif.get_focalLength());
            prepStmt.setDouble(3,exif.get_exposureTime());
            prepStmt.setDouble(4,exif.get_dazzleNumber());
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
    public void editExifData(long id, ExifData exif) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepUpdateEXIF);
            prepStmt.setTimestamp(1,Timestamp.valueOf(exif.get_dateTime()));
            prepStmt.setDouble(2,exif.get_focalLength());
            prepStmt.setDouble(3,exif.get_exposureTime());
            prepStmt.setDouble(4,exif.get_dazzleNumber());
            prepStmt.setLong(5,exif.get_id());
            long affectedRows = prepStmt.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Editing EXIF failed, no rows affected");
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
    public long createItpcData(ItpcData itpc) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        long ret = -1;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepInsertITPC, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1,itpc.get_pictureName());
            prepStmt.setString(2,itpc.get_pictureExt());
            prepStmt.setString(3,itpc.get_keywords());
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
    public void editItpcData(long id, ItpcData itpc) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepUpdateITPC);
            prepStmt.setString(1,itpc.get_pictureName());
            prepStmt.setString(2,itpc.get_pictureExt());
            prepStmt.setString(3,itpc.get_keywords());
            prepStmt.setLong(4,itpc.get_id());
            long affectedRows = prepStmt.executeUpdate();
            if(affectedRows==0){
                throw new SQLException("Editing ITPC failed, no rows affected");
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
            prepStmt = conn.prepareStatement(PreparedStatements.prepSelectAllPictures);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                resultList.add(new PictureData(rs.getLong("ID"),rs.getString("FILEPATH"),rs.getLong("FK_PHOTOGRAPHER_ID"),rs.getLong("FK_EXIF_ID"),rs.getLong("FK_ITPC_ID")));
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
    public Set<String> getPicturePathSet() {
        Set<String> result = new HashSet<>();
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;

        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepSelectPicturePaths);
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
    public String getPicturePathByID(long id) {
        Connection conn=null;
        PreparedStatement prepStmt=null;
        ResultSet rs=null;
        String path = null;
        try {
            conn = DBCPDataSource.getConnection();
            prepStmt = conn.prepareStatement(PreparedStatements.prepSelectPictureByID);
            prepStmt.setLong(1,id);
            rs = prepStmt.executeQuery();

            while(rs.next()){
                path = rs.getString("FILEPATH");
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
        return path;
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
}
