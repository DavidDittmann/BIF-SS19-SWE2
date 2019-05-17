package DataModels;

//iptc -> id, pictureName, pictureExtension, keywords

import java.util.List;

public class ItpcData {
    private long _id;
    private String _pictureName;
    private String _pictureExt;
    private String _keywords;

    public ItpcData(String pictureName, String pictureExtension, String keywords){
        _pictureName = pictureName;
        _pictureExt = pictureExtension;
        _keywords = keywords;
    }
    public ItpcData(long id, String pictureName, String pictureExtension, String keywords){
        _id = id;
        _pictureName = pictureName;
        _pictureExt = pictureExtension;
        _keywords = keywords;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_pictureName() {
        return _pictureName;
    }

    public void set_pictureName(String _pictureName) {
        this._pictureName = _pictureName;
    }

    public String get_pictureExt() {
        return _pictureExt;
    }

    public void set_pictureExt(String _pictureExt) {
        this._pictureExt = _pictureExt;
    }

    public String get_keywords() {
        return _keywords;
    }

    public void set_keywords(String _keywords) {
        this._keywords = _keywords;
    }
}
