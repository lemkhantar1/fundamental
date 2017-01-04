package com.lemkhantar.fundamental.entity;

import com.lemkhantar.fundamental.entity.Time;

/**
 * Created by lemkhantar1 on 7/17/16.
 */
public class OperationAuto {
    private int _id;
    private int _idCaisse;
    private float _montant;
    private Time _time;
    private int _active;

    public OperationAuto(int _id, int _idCaisse, float _montant, Time time, int _active) {
        this._id = _id;
        this._idCaisse = _idCaisse;
        this._montant = _montant;
        this._time = time;
        this._active = _active;
    }

    public void set_time(Time _time) {
        this._time = _time;
    }

    public void set_active(int _active) {
        this._active = _active;
    }

    public Time get_time() {

        return _time;
    }

    public int get_active() {
        return _active;
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


    public int get_id() {

        return _id;
    }

    public int get_idCaisse() {
        return _idCaisse;
    }

    public float get_montant() {
        return _montant;
    }

    public OperationAuto() {
    }
}
