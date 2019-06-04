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

/**
 * BusinessLayer der Applikation. Abstrahiert Zugriffe der Controller
 * und eventueller anderer Applikationsteile zur Datenbank
 */
public class BusinessLayer {
    private DataAccessLayer DAL;

    /**
     * Konstruktor der BusinessLayer
     * Initialisiert den benötigten DataAccessLayer
     */
    public BusinessLayer(){
        DAL = new DataAccessLayer();
    }


    /**
     * Holen aller Bilder aus der Datenbank
     * @return (Type: List<P_Model_Picture>) Liste der geladenen Bilder
     */
    public List<P_Model_Picture> getAllPictures() {
        List<P_Model_Picture> result = new ArrayList<>();
        List<PictureData> Pics = DAL.getAllPicturesParallel();
        for(PictureData p : Pics){
            result.add(new P_Model_Picture(p,getAuthorToPicture(p)));
        }
        return result;
    }

    /**
     * Holen aller Authoren aus der Datenbank
     * @return  (Type: List<P_Model_Author>) Liste der geladenen Authoren
     */
    public List<P_Model_Author> getAllAuthors(){
        List<P_Model_Author> result = new ArrayList<>();
        List<AuthorData> authors = DAL.getAllAuthors();
        for(AuthorData a : authors){
            result.add(new P_Model_Author(a));
        }
        return result;
    }

    /**
     * Holen aller Bilder aus der Datenbank, welche ein bestimmtes Keyword in der
     * Keywordliste enthalten
     * @param keyword (Type: String) Keyword, nach dem gesucht wird
     * @return (Type: List<P_Model_Picture>) Liste der geladenen Bilder
     */
    public List<P_Model_Picture> getPicturesByKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        List<P_Model_Picture> result = new ArrayList<>();
        List<PictureData> Pics = DAL.getPicturesByKeyword(keyword);
        for(PictureData p : Pics){
            result.add(new P_Model_Picture(p,getAuthorToPicture(p)));
        }
        return result;
    }

    /**
     * Laden der Authorendaten, welche von einem bestimmten Bild referenziert werden
     * @param pic Bilddaten, mit der referenzierten ID des Authors als Foreign Key
     * @return (Type: P_Model_Author) Referenzierter Author
     */
    private P_Model_Author getAuthorToPicture(PictureData pic){
        AuthorData a = DAL.getAuthorByID(pic.get_fk_author());
        P_Model_Author res = new P_Model_Author(a);
        return res;
    }

    /**
     * Vergleich ob 2 P_Model_Authoren Objekte der selbe Author sind oder nicht
     * @param a 1. Authorenobjekt zum vergleichen
     * @param b 2. Authorenobjekt zum vergleichen
     * @return (Type: Boolean) True wenn Authoren gleich, False wenn nicht
     */
    public boolean isEqualAuthor(P_Model_Author a, P_Model_Author b){
        if(a.getFullName().equals(b.getFullName()) && a.getBirthday().equals(b.getBirthday()) && a.getNotes().equals(b.getNotes())){
            return true;
        }
        return false;
    }

    /**
     * Speichern der eventuellen Änderungen eines Bildes in der Datenbank
     * Das Bild muss schon in der Datenbank zur Verfügung stehen
     * @param pic (Type: P_Model_Picture) Daten des zu speichernden Bildes
     */
    public void savePictureData(P_Model_Picture pic){
        long id = pic.getAuthor().getID();
        DAL.editPicture(new PictureData(pic.getID(),pic.getFilePath(),pic.getKeywords(),pic.getDescription(), LocalDateTime.parse(pic.getCreationTime()),pic.getLocation(),pic.getFocalLength(),pic.getExposureTime(),pic.getDazzleNumber(),pic.getAuthor().getID()));
    }

    /**
     * Speichern der eventuellen Änderungen eines Authors in der Datenbank
     * Der Author muss schon in der Datenbank zur Verfügung stehen
     * @param a (Type: P_Model_Author) Daten des zu speichernden Authors
     * @param newAuthor (Type: Boolean) Gibt an ob ein neuer Author angelegt wird (True), oder ein
     *                  vorhandener Datensatz geändert werden soll
     */
    public void saveAuthorData(P_Model_Author a, boolean newAuthor){
        if(newAuthor){
            DAL.createAuthor(new AuthorData(a.getName(),a.getLastName(),a.getBirthday(),a.getNotes()));
        }else{
            DAL.editAuthor(new AuthorData(a.getID(),a.getName(),a.getLastName(),a.getBirthday(),a.getNotes()));
        }
    }

    /**
     * Nachladen der Datenbank, falls neue Bilder vorhanden sind
     */
    public void reloadDatabase(){
        DAL.updateDatabase();
    }

    /**
     * Holen eines Authorendatensatzes mittels dessen ID (Nötig für das neu laden der Authorendaten
     * der Bilder beim ändern der Daten der Authoren)
     * @param ID (Type: Long) ID des Authors
     * @return (Type: P_Model_Author)
     */
    public P_Model_Author getAuthorByID(long ID){
        return new P_Model_Author(DAL.getAuthorByID(ID));
    }

    /**
     * Check ob die Daten eines Bildes valide sind (z.B.: Prüfen ob Stringlängen nicht zu groß für Tabelle)
     * @param pic (Type: P_Model_Picture) Das Bild das Überprüft wird
     * @return (Type: Boolean) True wenn Daten valide sind, sonst False
     */
    public boolean isValidPicture(P_Model_Picture pic){
        AuthorData author = null;
        author = DAL.getAuthorByID(pic.getAuthor().getID());
        if(pic.getFilePath().length()<=255 && pic.getKeywords().length()<=300 && pic.getDescription().length()<=300 && pic.getLocation().length()<=100 && author!=null)
            return true;
        return false;
    }

    /**
     * Check ob die Daten eines Authors valide sind (z.B.: Prüfen ob Stringlängen nicht zu groß für Tabelle)
     * @param a (Type: P_Model_Author) Der Author der Überprüft wird
     * @return (Type: Boolean) True wenn Daten valide sind, sonst False
     */
    public boolean isValidAuthor(P_Model_Author a){
        if((!a.getName().equals("NEW") || !a.getLastName().equals("AUTHOR")) && a.getName().length()<=100 && a.getLastName().length()<=50 && a.getBirthday().isBefore(LocalDate.now()))
            return true;
        else
            return false;
    }
}
