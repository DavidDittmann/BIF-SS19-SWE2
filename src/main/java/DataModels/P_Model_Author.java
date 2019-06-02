package DataModels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Date;

public class P_Model_Author {
    private long ID=0;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
    private StringProperty notes = new SimpleStringProperty();

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public P_Model_Author(AuthorData author){
        ID = author.get_id();
        name.set(author.get_name());
        lastName.set(author.get_lastName());
        birthday.set(author.get_birthday());
        notes.set(author.get_notes());
    }

    public String getFullName(){
        return getName()+" "+getLastName();
    }

    @Override
    public String toString(){
        return getFullName();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public LocalDate getBirthday() {
        return birthday.get();
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
