package com.lemkhantar.fundamental.entity;

import java.util.Date;

/**
 * Created by lemkhantar1 on 7/10/16.
 */
public class Repos extends manip {

    private int id;//
    private int duree;//
    private float prixUnitaire;//
    private Date date;//
    private Time time;//
    private String description;
    private int idCaisse;//

    public Repos(int id, int duree, float prixUnitaire, Date date, Time time, String description, int idCaisse) {
        this.id = id;
        this.duree = duree;
        this.prixUnitaire = prixUnitaire;
        this.date = date;
        this.time = time;
        this.description = description;
        this.idCaisse = idCaisse;
    }

    public Repos() {
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdCaisse() {
        return idCaisse;
    }

    public void setIdCaisse(int idCaisse) {
        this.idCaisse = idCaisse;
    }

    public String getDescription() {

        return description;
    }

    public Time getTime() {

        return time;
    }

    @Override
    public Time getTemps()
    {
        return time;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Date get_date() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setPrixUnitaire(float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getId() {
        return id;
    }

    public int getDuree() {
        return duree;
    }

    public float getPrixUnitaire() {
        return prixUnitaire;
    }
}
