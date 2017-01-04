package com.lemkhantar.fundamental.entity;

import java.util.Date;

/**
 * Created by lemkhantar1 on 7/5/16.
 */
public class Operation extends manip {

    private int _id;
    private int _idCaisse;
    private TypeOperation _type;
    private float _montant;
    private Date _date;
    private String _description;
    private Time temps;

    public Operation(int _id, int _idCaisse, TypeOperation _type, float _montant, Date _date, String _description, Time temps) {
        this._id = _id;
        this._idCaisse = _idCaisse;
        this._type = _type;
        this._montant = _montant;
        this._date = _date;
        this._description = _description;
        this.temps = temps;
    }

    public Operation() {
    }

    public void setTemps(Time temps) {
        this.temps = temps;
    }

    @Override
    public Time getTemps() {

        return temps;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_idCaisse(int _idCaisse) {
        this._idCaisse = _idCaisse;
    }

    public void set_type(TypeOperation _type) {
        this._type = _type;
    }

    public void set_montant(float _montant) {
        this._montant = _montant;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public int get_id() {
        return _id;
    }

    public int get_idCaisse() {
        return _idCaisse;
    }

    public TypeOperation get_type() {
        return _type;
    }

    public float get_montant() {
        return _montant;
    }

    @Override
    public Date get_date() {
        return _date;
    }

    public String get_description() {
        return _description;
    }
}
