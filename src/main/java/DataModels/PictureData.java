package DataModels;

import javafx.scene.image.Image;

import java.time.LocalDateTime;

public class PictureData {
    private long _id;
    private String _filepath;
    private String _keywords;
    private String _desc;
    private LocalDateTime _date;
    private String _location;
    private double _focal;
    private double _exposure;
    private double _dazzle;
    private long _fk_author;

    public PictureData(String _filepath, String _keywords, String _desc, LocalDateTime _date, String _location, double _focal, double _exposure, double _dazzle, long _fk_author) {
        this._filepath = _filepath;
        this._keywords = _keywords;
        this._desc = _desc;
        this._date = _date;
        this._location = _location;
        this._focal = _focal;
        this._exposure = _exposure;
        this._dazzle = _dazzle;
        this._fk_author = _fk_author;
    }

    public PictureData(long _id, String _filepath, String _keywords, String _desc, LocalDateTime _date, String _location, double _focal, double _exposure, double _dazzle, long _fk_author) {
        this._id = _id;
        this._filepath = _filepath;
        this._keywords = _keywords;
        this._desc = _desc;
        this._date = _date;
        this._location = _location;
        this._focal = _focal;
        this._exposure = _exposure;
        this._dazzle = _dazzle;
        this._fk_author = _fk_author;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_filepath() {
        return _filepath;
    }

    public String get_keywords() {
        return _keywords;
    }

    public void set_keywords(String _keywords) {
        this._keywords = _keywords;
    }

    public void set_filepath(String _filepath) {
        this._filepath = _filepath;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public LocalDateTime get_date() {
        return _date;
    }

    public void set_date(LocalDateTime _date) {
        this._date = _date;
    }

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public double get_focal() {
        return _focal;
    }

    public void set_focal(double _focal) {
        this._focal = _focal;
    }

    public double get_exposure() {
        return _exposure;
    }

    public void set_exposure(double _exposure) {
        this._exposure = _exposure;
    }

    public double get_dazzle() {
        return _dazzle;
    }

    public void set_dazzle(double _dazzle) {
        this._dazzle = _dazzle;
    }

    public long get_fk_author() {
        return _fk_author;
    }

    public void set_fk_author(long _fk_author) {
        this._fk_author = _fk_author;
    }
}
