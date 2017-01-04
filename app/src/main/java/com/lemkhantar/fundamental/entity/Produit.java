package com.lemkhantar.fundamental.entity;



public class Produit {
    private int _id;
    private int _idSousCategorie;
    private String _designation;
    private String _photo;

    public Produit() {
    }

    public Produit(int _id, int _idSousCategorie, String _designation, String _photo) {
        this._id = _id;
        this._idSousCategorie = _idSousCategorie;
        this._designation = _designation;
        this._photo = _photo;
    }

    public int get_id() {
        return _id;
    }

    public int get_idSousCategorie() {
        return _idSousCategorie;
    }

    public String get_designation() {
        return _designation;
    }



    public String get_photo() {
        return _photo;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_idSousCategorie(int _idSousCategorie) {
        this._idSousCategorie = _idSousCategorie;
    }

    public void set_designation(String _designation) {
        this._designation = _designation;
    }



    public void set_photo(String _photo) {
        this._photo = _photo;
    }

    @Override
    public String toString() {
        return _designation;
    }
}
