package com.lemkhantar.fundamental.entity;


import java.util.ArrayList;

public class Categorie {
    private int _id;
    private String _designation;
    private String _photo;
    private ArrayList<SousCategorie> sousCategories;

    public Categorie(int _id, String _designation, String _photo, ArrayList<SousCategorie> sousCategories) {
        this._id = _id;
        this._designation = _designation;
        this._photo = _photo;
        this.sousCategories = sousCategories;
    }

    public Categorie() {
    }

    public ArrayList<SousCategorie> getSousCategories() {
        return sousCategories;
    }

    public int get_id() {
        return _id;
    }

    public String get_designation() {
        return _designation;
    }

    public String get_photo() {
        return _photo;
    }

    public void setSousCategories(ArrayList<SousCategorie> sousCategories) {
        this.sousCategories = sousCategories;
    }

    public void set_id(int _id) {
        this._id = _id;
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
