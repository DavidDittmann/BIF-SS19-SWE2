package DataModels;

import java.time.LocalDate;
import java.util.Date;

public class AuthorData {
    private long _id;
    private String _name;
    private String _lastName;
    private LocalDate _birthday;
    private String _notes;

    public AuthorData(String _name, String _lastName, LocalDate _birthday, String _notes) {
        this._name = _name;
        this._lastName = _lastName;
        this._birthday = _birthday;
        this._notes = _notes;
    }

    public AuthorData(long _id, String _name, String _lastName, LocalDate _birthday, String _notes) {
        this._id = _id;
        this._name = _name;
        this._lastName = _lastName;
        this._birthday = _birthday;
        this._notes = _notes;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public LocalDate get_birthday() {
        return _birthday;
    }

    public void set_birthday(LocalDate _birthday) {
        this._birthday = _birthday;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }
}
