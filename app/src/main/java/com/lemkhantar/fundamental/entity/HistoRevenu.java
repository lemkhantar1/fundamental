package com.lemkhantar.fundamental.entity;

import java.util.Date;

/**
 * Created by lemkhantar1 on 7/16/16.
 */
public class HistoRevenu {

    private int _id;
    private float _total;
    private Date _date;
    private int _kilometrage;

    public HistoRevenu() {
    }

    public HistoRevenu(int _id, float _total, Date _date, int _kilometrage) {
        this._id = _id;
        this._total = _total;
        this._date = _date;
        this._kilometrage = _kilometrage;
    }

    public int get_kilometrage() {
        return _kilometrage;
    }

    public void set_kilometrage(int _kilometrage) {
        this._kilometrage = _kilometrage;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_total(float _total) {
        this._total = _total;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public int get_id() {
        return _id;
    }

    public float get_total() {
        return _total;
    }

    public Date get_date() {
        return _date;
    }
}
