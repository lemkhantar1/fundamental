package com.lemkhantar.fundamental.entity;


import java.util.ArrayList;

public class SousCategorie {
    private int _id;
    private int _idCategorie;
    private String _designation;
    private ArrayList<Produit> produits;


    public SousCategorie() {
    }

    public SousCategorie(int _id,int _idCategorie, String _designation, ArrayList<Produit> produits) {
        this._id=_id;
        this._idCategorie = _idCategorie;
        this._designation = _designation;
        this.produits = produits;
    }


    public ArrayList<Produit> getProduits() {
        return produits;
    }

    public int get_id() {
        return _id;
    }

    public int get_idCategorie() {
        return _idCategorie;
    }

    public String get_designation() {
        return _designation;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_idCategorie(int _idCategorie) {
        this._idCategorie = _idCategorie;
    }

    public void set_designation(String _designation) {
        this._designation = _designation;
    }

    public void setProduits(ArrayList<Produit> produits) {
        this.produits = produits;
    }

    @Override
    public String toString() {
        return _designation;
    }
}
