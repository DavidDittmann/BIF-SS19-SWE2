package DataModels;

import javafx.scene.image.Image;

public class PictureData {
    private long _id;
    private String _filepath;
    private long _fk_id_photographer;
    private long _fk_id_exif;
    private long _fk_id_itpc;

    public PictureData(String filepath, long photographer_id, long exif_id, long itpc_id){
        _filepath = filepath;
        _fk_id_photographer = photographer_id;
        _fk_id_exif = exif_id;
        _fk_id_itpc = itpc_id;
    }
    public PictureData(long id, String filepath, long photographer_id, long exif_id, long itpc_id){
        _id = id;
        _filepath = filepath;
        _fk_id_photographer = photographer_id;
        _fk_id_exif = exif_id;
        _fk_id_itpc = itpc_id;
    }

    public long getId() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_filepath() {
        return _filepath;
    }

    public long get_fk_id_photographer() {
        return _fk_id_photographer;
    }

    public long get_fk_id_exif() {
        return _fk_id_exif;
    }

    public long get_fk_id_itpc() {
        return _fk_id_itpc;
    }

    public void set_filepath(String _filepath) {
        this._filepath = _filepath;
    }

    public void set_fk_id_photographer(long _fk_id_photographer) {
        this._fk_id_photographer = _fk_id_photographer;
    }

    public void set_fk_id_exif(long _fk_id_exif) {
        this._fk_id_exif = _fk_id_exif;
    }

    public void set_fk_id_itpc(long _fk_id_itpc) {
        this._fk_id_itpc = _fk_id_itpc;
    }
}
