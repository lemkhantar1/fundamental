package com.lemkhantar.fundamental.entity;

import java.util.Date;

/**
 * Created by lemkhantar1 on 7/5/16.
 */
public class Revenu extends manip {

    private int _id;
    private int _idCaisse;
    private float _montant;
    private int _kilometrage;
    private int _km_debut;
    private int _km_fin;
    private float _coefficient;
    private Date _date;
    private Time time;
    private float _traite;
    private int _archive;

    public Revenu() {
    }

    public Revenu(int _id, int _idCaisse, float _montant, int _kilometrage,int _km_debut, int _km_fin, float _coefficient, Date _date, Time _time, float _traite, int _archive) {

        this._id = _id;
        this._idCaisse = _idCaisse;
        this._montant = _montant;
        this._kilometrage = _kilometrage;
        this._km_debut = _km_debut;
        this._km_fin = _km_fin;
        this._coefficient = _coefficient;
        this._date = _date;
        this.time = _time;
        this._traite = _traite;
        this._archive = _archive;
    }

    public int get_km_debut() {
        return _km_debut;
    }

    public void set_km_debut(int _km_debut) {
        this._km_debut = _km_debut;
    }

    public int get_km_fin() {
        return _km_fin;
    }

    public void set_km_fin(int _km_fin) {
        this._km_fin = _km_fin;
    }

    public int get_archive() {
        return _archive;
    }

    public void set_archive(int _archive) {
        this._archive = _archive;
    }

    public void set_traite(float _traite) {
        this._traite = _traite;
    }

    public float get_traite() {

        return _traite;
    }

    public int get_id() {
        return _id;
    }

    public int get_idCaisse() {
        return _idCaisse;
    }

    public float get_montant() {
        return _montant;
    }

    public int get_kilometrage() {
        return _kilometrage;
    }

    public float get_coefficient() {
        return _coefficient;
    }

    public Date get_date() {
        return _date;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_idCaisse(int _idCaisse) {
        this._idCaisse = _idCaisse;
    }

    public void set_montant(float _montant) {
        this._montant = _montant;
    }

    public void set_kilometrage(int _kilometrage) {
        this._kilometrage = _kilometrage;
    }

    public void set_coefficient(float _coefficient) {
        this._coefficient = _coefficient;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public Time getTime() {

        return time;
    }

    @Override
    public Time getTemps()
    {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

}
