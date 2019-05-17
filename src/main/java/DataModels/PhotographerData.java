package DataModels;

public class PhotographerData {
    private long _id;
    private String _name;
    private String _lastName;
    private String _address;

    public PhotographerData(String name, String lastName, String address){
        _name=name;
        _lastName=lastName;
        _address=address;
    }
    public PhotographerData(long id, String name, String lastName, String address){
        _id=id;
        _name=name;
        _lastName=lastName;
        _address=address;
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

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }
}
