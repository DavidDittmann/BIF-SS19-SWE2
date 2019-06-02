package Layers;

import DataModels.AuthorData;
import DataModels.P_Model_Author;
import DataModels.P_Model_Picture;
import DataModels.PictureData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BusinessLayer {
    private DataAccessLayer DAL;

    public BusinessLayer(){
        DAL = new DataAccessLayer();
    }


    public List<P_Model_Picture> getAllPictures() {
        List<P_Model_Picture> result = new ArrayList<>();
        List<PictureData> Pics = DAL.getAllPicturesParallel();
        for(PictureData p : Pics){
            result.add(new P_Model_Picture(p,getAuthorToPicture(p)));
        }
        return result;
    }

    public List<P_Model_Author> getAllAuthors(){
        List<P_Model_Author> result = new ArrayList<>();
        List<AuthorData> authors = DAL.getAllAuthors();
        for(AuthorData a : authors){
            result.add(new P_Model_Author(a));
        }
        return result;
    }

    public List<P_Model_Picture> getPicturesByKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        List<P_Model_Picture> result = new ArrayList<>();
        List<PictureData> Pics = DAL.getPicturesByKeyword(keyword);
        for(PictureData p : Pics){
            result.add(new P_Model_Picture(p,getAuthorToPicture(p)));
        }
        return result;
    }

    public P_Model_Author getAuthorToPicture(PictureData pic){
        AuthorData a = DAL.getAuthorByID(pic.get_fk_author());
        P_Model_Author res = new P_Model_Author(a);
        return res;
    }

    public boolean isEqualAuthor(P_Model_Author a, P_Model_Author b){
        if(a.getFullName().equals(b.getFullName()) && a.getBirthday().equals(b.getBirthday()) && a.getNotes().equals(b.getNotes())){
            return true;
        }
        return false;
    }

    public void savePictureData(P_Model_Picture pic){
        long id = pic.getAuthor().getID();
        DAL.editPicture(new PictureData(pic.getID(),pic.getFilePath(),pic.getKeywords(),pic.getDescription(), LocalDateTime.parse(pic.getCreationTime()),pic.getLocation(),pic.getFocalLength(),pic.getExposureTime(),pic.getDazzleNumber(),pic.getAuthor().getID()));
    }

    public void saveAuthorData(P_Model_Author a, boolean newAuthor){
        if(newAuthor){
            DAL.createAuthor(new AuthorData(a.getName(),a.getLastName(),a.getBirthday(),a.getNotes()));
        }else{
            DAL.editAuthor(new AuthorData(a.getID(),a.getName(),a.getLastName(),a.getBirthday(),a.getNotes()));
        }
    }

    public void reloadDatabase(){
        DAL.updateDatabase();
    }

    public boolean isValidPicture(P_Model_Picture pic){
        AuthorData author = null;
        author = DAL.getAuthorByID(pic.getAuthor().getID());
        if(pic.getFilePath().length()<=255 && pic.getKeywords().length()<=300 && pic.getDescription().length()<=300 && pic.getLocation().length()<=100 && author!=null)
            return true;
        return false;
    }

    public boolean isValidAuthor(P_Model_Author a){
        if((!a.getName().equals("NEW") || !a.getLastName().equals("AUTHOR")) && a.getName().length()<=100 && a.getLastName().length()<=50 && a.getBirthday().isBefore(LocalDate.now()))
            return true;
        else
            return false;
    }
}
