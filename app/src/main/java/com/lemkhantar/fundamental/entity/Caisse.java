package com.lemkhantar.fundamental.entity;

/**
 * Created by lemkhantar1 on 7/5/16.
 */
public class Caisse {

    private int _id;
    private float _total;
    private float _lastOperation;
    private int _plus;

    public Caisse() {
    }

    public Caisse(int _id, float _total, float _lastOperation, int _plus) {
        this._total = _total;
        this._id = _id;
        this._lastOperation = _lastOperation;
        this._plus = _plus;
    }

    public void set_plus(int _plus) {
        this._plus = _plus;
    }

    public int get_plus() {

        return _plus;
    }

    public float get_total() {
        return _total;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_total(float _total) {
        this._total = _total;
    }

    public void set_lastOperation(float _lastOperation) {
        this._lastOperation = _lastOperation;
    }

    public float get_lastOperation() {

        return _lastOperation;
    }
}
