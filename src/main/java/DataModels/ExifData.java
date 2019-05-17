package DataModels;

//exif -> id, datetime, focalLength, exposureTime, dazzleNumber

import java.time.LocalDateTime;

public class ExifData {
    private long _id;
    private LocalDateTime _dateTime;
    private double _focalLength;
    private double _exposureTime;
    private double _dazzleNumber;

    public ExifData(LocalDateTime dateTime, double focalLength, double exposureTime, double dazzleNumber){
        _dateTime = dateTime;
        _focalLength = focalLength;
        _exposureTime = exposureTime;
        _dazzleNumber = dazzleNumber;
    }

    public ExifData(long id, LocalDateTime dateTime, double focalLength, double exposureTime, double dazzleNumber){
        _id = id;
        _dateTime = dateTime;
        _focalLength = focalLength;
        _exposureTime = exposureTime;
        _dazzleNumber = dazzleNumber;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public LocalDateTime get_dateTime() {
        return _dateTime;
    }

    public void set_dateTime(LocalDateTime _dateTime) {
        this._dateTime = _dateTime;
    }

    public double get_focalLength() {
        return _focalLength;
    }

    public void set_focalLength(double _focalLength) {
        this._focalLength = _focalLength;
    }

    public double get_exposureTime() {
        return _exposureTime;
    }

    public void set_exposureTime(double _exposureTime) {
        this._exposureTime = _exposureTime;
    }

    public double get_dazzleNumber() {
        return _dazzleNumber;
    }

    public void set_dazzleNumber(double _dazzleNumber) {
        this._dazzleNumber = _dazzleNumber;
    }
}
