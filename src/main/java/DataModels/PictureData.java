package DataModels;

import javafx.scene.image.Image;

public class PictureData {
    private int _id;
    private Image _image;
    private String _filepath;
    private int _fk_id_photographer;
    private int _fk_id_exif;
    private int _fk_id_itpc;

    public PictureData(Image image, String filepath, int photographer_id, int exif_id, int itpc_id){
        _image = image;
        _filepath = filepath;
        _fk_id_photographer = photographer_id;
        _fk_id_exif = exif_id;
        _fk_id_itpc = itpc_id;
    }
    public PictureData(int id, Image image, String filepath, int photographer_id, int exif_id, int itpc_id){
        _id = id;
        _image = image;
        _filepath = filepath;
        _fk_id_photographer = photographer_id;
        _fk_id_exif = exif_id;
        _fk_id_itpc = itpc_id;
    }

    public int getId() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Image get_image() {
        return _image;
    }

    public String get_filepath() {
        return _filepath;
    }

    public int get_fk_id_photographer() {
        return _fk_id_photographer;
    }

    public int get_fk_id_exif() {
        return _fk_id_exif;
    }

    public int get_fk_id_itpc() {
        return _fk_id_itpc;
    }

    public void set_filepath(String _filepath) {
        this._filepath = _filepath;
    }

    public void set_fk_id_photographer(int _fk_id_photographer) {
        this._fk_id_photographer = _fk_id_photographer;
    }

    public void set_fk_id_exif(int _fk_id_exif) {
        this._fk_id_exif = _fk_id_exif;
    }

    public void set_fk_id_itpc(int _fk_id_itpc) {
        this._fk_id_itpc = _fk_id_itpc;
    }
}
