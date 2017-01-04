package com.lemkhantar.fundamental.entity;

import java.util.Date;

/**
 * Created by lemkhantar1 on 6/16/16.
 */
public class Rechange extends manip {

    private int _id;
    private Produit _produit;
    private Date _date;
    private float _prix;
    private float _main;
    private String _description;
    private int _idCaisse;
    private Time temps;

    public void set_idCaisse(int _idCaisse) {
        this._idCaisse = _idCaisse;
    }

    public int get_idCaisse() {

        return _idCaisse;
    }

    public Rechange() {
    }

    public Rechange(int _id, Produit _produit, Date _date, float _prix, float _main, String _description, int _idCaisse, Time temps) {
        this._id = _id;
        this._produit = _produit;
        this._date = _date;
        this._prix = _prix;
        this._main = _main;
        this._description = _description;
        this._idCaisse = _idCaisse;
        this.temps = temps;
    }

    public void setTemps(Time temps) {
        this.temps = temps;
    }

    @Override
    public Time getTemps() {

        return temps;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Produit get_produit() {
        return _produit;
    }

    public void set_produit(Produit _produit) {
        this._produit = _produit;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public void set_prix(float _prix) {
        this._prix = _prix;
    }



    public void set_description(String _description) {
        this._description = _description;
    }

    public int get_id() {
        return _id;
    }


    @Override
    public Date get_date() {
        return _date;
    }

    public float get_main() {
        return _main;
    }

    public void set_main(float _main) {
        this._main = _main;
    }

    public float get_prix() {
        return _prix;
    }



    public String get_description() {
        return _description;
    }
}
