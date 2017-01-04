package com.lemkhantar.fundamental.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.entity.Categorie;
import com.lemkhantar.fundamental.entity.HistoRevenu;
import com.lemkhantar.fundamental.entity.Operation;
import com.lemkhantar.fundamental.entity.OperationAuto;
import com.lemkhantar.fundamental.entity.Produit;
import com.lemkhantar.fundamental.entity.Rechange;
import com.lemkhantar.fundamental.entity.Repos;
import com.lemkhantar.fundamental.entity.Revenu;
import com.lemkhantar.fundamental.entity.SousCategorie;
import com.lemkhantar.fundamental.entity.Time;
import com.lemkhantar.fundamental.entity.TypeOperation;
import com.lemkhantar.fundamental.entity.manip;
import com.lemkhantar.fundamental.helper.Constantes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DBManager extends SQLiteOpenHelper
{
    private static final int CONVERSION_TIME = 3600000;

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "loganii";


    private static final String TABLE_CATEGORIE = "categorie";
    private static final String CATEGORIE_ID = "_id";
    private static final String CATEGORIE_DESIGNATION = "_designation";
    private static final String CATEGORIE_PHOTO = "_photo";

    private static final String TABLE_CAISSE = "caisse";
    private static final String CAISSE_ID = "_id";
    private static final String CAISSE_TOTAL = "_total";
    private static final String CAISSE_LAST_OPERATION = "_lastOperation";
    private static final String CAISSE_PLUS = "_plus";

    private static final String TABLE_SOUS_CATEGORIE = "sous_categorie";
    private static final String SOUS_CATEGORIE_ID = "_id";
    private static final String SOUS_CATEGORIE_ID_CATEGORIE = "_idCategorie";
    private static final String SOUS_CATEGORIE_DESIGNATION = "_designation";

    private static final String TABLE_PRODUIT = "produit";
    private static final String PRODUIT_ID = "_id";
    private static final String PRODUIT_ID_SOUS_CATEGORIE = "_idSousCategorie";
    private static final String PRODUIT_DESIGNATION = "_designation";
    private static final String PRODUIT_PHOTO = "_photo";

    private static final String TABLE_OPERATION = "operation";
    private static final String OPERATION_ID = "_id";
    private static final String OPERATION_ID_CAISSE = "_idCaisse";
    private static final String OPERATION_TYPE = "_type";
    private static final String OPERATION_MONTANT = "_montant";
    private static final String OPERATION_DATE = "_date";
    private static final String OPERATION_DESCRIPTION = "_description";
    private static final String OPERATION_TEMPS = "_time";

    private static final String TABLE_RECHANGE = "rechange";
    private static final String RECHANGE_ID = "_id";
    private static final String RECHANGE_ID_PRODUIT = "_idProduit";
    private static final String RECHANGE_DATE = "_date";
    private static final String RECHANGE_PRIX = "_prix";
    private static final String RECHANGE_ID_CAISSE = "_idCaisse";
    private static final String RECHANGE_MAIN = "_main";
    private static final String RECHANGE_DESCRIPTION = "_description";
    private static final String RECHANGE_TEMPS = "_time";


    private static final String TABLE_REPOS = "repos";
    private static final String REPOS_ID = "_id";
    private static final String REPOS_ID_CAISSE = "_idCaisse";
    private static final String REPOS_DUREE = "_duree";
    private static final String REPOS_PRIX_UNITAIRE = "_prixUnitaire";
    private static final String REPOS_DATE = "_date";
    private static final String REPOS_TEMPS = "_time";
    private static final String REPOS_DESCRIPTION = "_description";



    private static final String TABLE_REVENU = "revenu";
    private static final String REVENU_ID = "_id";
    private static final String REVENU_ID_CAISSE = "_idCaisse";
    private static final String REVENU_MONTANT = "_montant";
    private static final String REVENU_KILOMETRAGE = "_kilometrage";
    private static final String REVENU_COEFFICIENT = "_coefficient";
    private static final String REVENU_DATE = "_date";
    private static final String REVENU_TEMPS = "_time";
    private static final String REVENU_TRAITE = "_traite";
    private static final String REVENU_ARCHIVE = "_archive";
    private static final String REVENU_KM_DEBUT = "_km_debut";
    private static final String REVENU_KM_FIN = "_km_fin";


    private static final String TABLE_HISTO_REVENU = "histo_revenu";
    private static final String HISTO_REVENU_ID = "_id";
    private static final String HISTO_REVENU_TOTAL = "_total";
    private static final String HISTO_REVENU_DATE = "_date";
    private static final String HISTO_REVENU_KILOMETRAGE = "_kilometrage";


    private static final String TABLE_OPERATION_AUTO = "operation_auto";
    private static final String OPERATION_AUTO_ID = "_id";
    private static final String OPERATION_AUTO_ID_CAISSE = "_idCaisse";
    private static final String OPERATION_AUTO_MONTANT = "_montant";
    private static final String OPERATION_AUTO_TIME = "_time";
    private static final String OPERATION_AUTO_ACTIVE = "_active";

    private static final String TABLE_VERSION = "version";
    private static final String VERSION_VERSION = "_version";


    private static String DB_PATH = "/data/data/com.lemkhantar.fundamental/databases/";
    private static String DB_FILE_NAME = "loganii.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    //////////////--FONCTIONS DU DB-MANAGER--//////////////

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(!dbExist)
        {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
        else
        {

            try
            {
                String myPath = DB_PATH + DB_FILE_NAME;
                SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
                Cursor cursor = checkDB.query(TABLE_VERSION, new String[]{VERSION_VERSION},null, null, null, null, null, null);
                if (cursor != null)
                    cursor.moveToFirst();
                int version = cursor.getInt(cursor.getColumnIndex(VERSION_VERSION));


                if(version != DATABASE_VERSION)
                {
                    if(version==1)
                    {

                        try{String strSQL = "alter table "+TABLE_REVENU+" add column "+REVENU_KM_DEBUT+" integer default 0";
                            checkDB.execSQL(strSQL);}catch (SQLException e){}
                        try{String strSQL = "alter table "+TABLE_REVENU+" add column "+REVENU_KM_FIN+" integer default 0";
                            checkDB.execSQL(strSQL);}catch (SQLException e){}
                        try{String strSQL = "update  "+TABLE_VERSION+" set "+VERSION_VERSION+" = 2 where 1=1";
                            checkDB.execSQL(strSQL);}catch (SQLException e){}
                    }
                }
                else
                {

                }
            }
            catch(SQLiteException e)
            {


//                String myPath = DB_PATH + DB_FILE_NAME;
//                SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//                String strSQL = "create table version (_version integer default 1)";
//                checkDB.execSQL(strSQL);
//                strSQL = "insert into version values (1)";
//                checkDB.execSQL(strSQL);

            }
        }
    }
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try
        {

            String myPath = DB_PATH + DB_FILE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch(Exception e){}
        if(checkDB != null){checkDB.close();}

        return checkDB != null ? true : false;
    }
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_FILE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_FILE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_FILE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        myDataBase.enableWriteAheadLogging();

    }
    @Override public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }
    @Override public void onCreate(SQLiteDatabase db) {}
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    //////////////--FIN FONCTIONS DU DB-MANAGER--//////////////

    //////////////--FONCTIONS DES CATEGORIES--//////////////
    public void ajouterCategorie(Categorie categorie) {
        ContentValues values = new ContentValues();
        values.put(CATEGORIE_DESIGNATION, categorie.get_designation());
        values.put(CATEGORIE_PHOTO, categorie.get_photo());
        myDataBase.insert(TABLE_CATEGORIE, null, values);
    }
    public Categorie getCategorieByDesignation(String designation) {
        Cursor cursor = myDataBase.query(TABLE_CATEGORIE, new String[]{CATEGORIE_ID,
                        CATEGORIE_DESIGNATION, CATEGORIE_PHOTO}, CATEGORIE_DESIGNATION + "=?",
                new String[]{String.valueOf(designation)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Categorie categorie = new Categorie(
                    cursor.getInt(cursor.getColumnIndex(CATEGORIE_ID)),
                    cursor.getString(cursor.getColumnIndex(CATEGORIE_DESIGNATION)),
                    cursor.getString(cursor.getColumnIndex(CATEGORIE_PHOTO)),
                    getGroupSousCategorieByCategorie(cursor.getInt(cursor.getColumnIndex(CATEGORIE_ID)))
            );
            return categorie;
        }
        else
            return null;

    }
    public Categorie getCategorieById(int idCategorie) {
        Cursor cursor = myDataBase.query(TABLE_CATEGORIE, new String[]{CATEGORIE_ID,
                        CATEGORIE_DESIGNATION, CATEGORIE_PHOTO}, CATEGORIE_ID + "=?",
                new String[]{String.valueOf(idCategorie)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Categorie categorie = new Categorie(
                cursor.getInt(cursor.getColumnIndex(CATEGORIE_ID)),
                cursor.getString(cursor.getColumnIndex(CATEGORIE_DESIGNATION)),
                cursor.getString(cursor.getColumnIndex(CATEGORIE_PHOTO)),
                getGroupSousCategorieByCategorie(cursor.getInt(cursor.getColumnIndex(CATEGORIE_ID)))
        );
        return categorie;
    }
    public ArrayList<Categorie> getAllCategorie() {
        ArrayList<Categorie> categories = new ArrayList<Categorie>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIE;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Categorie categorie = new Categorie();
                categorie.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CATEGORIE_ID))));
                categorie.set_designation(cursor.getString(cursor.getColumnIndex(CATEGORIE_DESIGNATION)));
                categorie.set_photo(cursor.getString(cursor.getColumnIndex(CATEGORIE_PHOTO)));
                categorie.setSousCategories(getGroupSousCategorieByCategorie(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CATEGORIE_ID)))));
                categories.add(categorie);
            } while (cursor.moveToNext());
        }
        return categories;
    }
    public int getCategorieCount() {
        String countQuery = "SELECT * FROM " + TABLE_CATEGORIE;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    //////////////--FIN FONCTIONS DES CATEGORIES--//////////////

    //////////////--FONCTIONS DES SOUS-CATEGORIES--//////////////
    public void ajouterSousCategorie(SousCategorie sousCategorie) {
        ContentValues values = new ContentValues();
        values.put(SOUS_CATEGORIE_DESIGNATION, sousCategorie.get_designation());
        values.put(SOUS_CATEGORIE_ID_CATEGORIE, sousCategorie.get_idCategorie());
        myDataBase.insert(TABLE_SOUS_CATEGORIE, null, values);

    }
    public SousCategorie getSousCategorieByDesignation(String designation) {
        Cursor cursor = myDataBase.query(TABLE_SOUS_CATEGORIE, new String[]{SOUS_CATEGORIE_ID,
                        SOUS_CATEGORIE_DESIGNATION, SOUS_CATEGORIE_ID_CATEGORIE}, SOUS_CATEGORIE_DESIGNATION + "=?",
                new String[]{String.valueOf(designation)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            SousCategorie souscategorie = new SousCategorie(
                    cursor.getInt(cursor.getColumnIndex(SOUS_CATEGORIE_ID)),
                    cursor.getInt(cursor.getColumnIndex(SOUS_CATEGORIE_ID_CATEGORIE)),
                    cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_DESIGNATION)),
                    getGroupProduitBySousCategorie(cursor.getColumnIndex(SOUS_CATEGORIE_ID))
            );
            return souscategorie;
        }
        else
            return null;

    }
    public SousCategorie getSousCategorieByDesignationByCategorie(String designation, int idCategorie) {
        Cursor cursor = myDataBase.query(TABLE_SOUS_CATEGORIE, new String[]{SOUS_CATEGORIE_ID,
                        SOUS_CATEGORIE_DESIGNATION, SOUS_CATEGORIE_ID_CATEGORIE}, SOUS_CATEGORIE_DESIGNATION + "=? AND "+SOUS_CATEGORIE_ID_CATEGORIE+" =?",
                new String[]{String.valueOf(designation), String.valueOf(idCategorie)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            SousCategorie souscategorie = new SousCategorie(
                    cursor.getInt(cursor.getColumnIndex(SOUS_CATEGORIE_ID)),
                    cursor.getInt(cursor.getColumnIndex(SOUS_CATEGORIE_ID_CATEGORIE)),
                    cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_DESIGNATION)),
                    getGroupProduitBySousCategorie(cursor.getColumnIndex(SOUS_CATEGORIE_ID))
            );
            return souscategorie;
        }
        else
            return null;

    }
    public SousCategorie getSousCategorieById(int idSousCategorie) {
        Cursor cursor = myDataBase.query(TABLE_SOUS_CATEGORIE, new String[]{SOUS_CATEGORIE_ID,
                        SOUS_CATEGORIE_ID_CATEGORIE, SOUS_CATEGORIE_DESIGNATION}, SOUS_CATEGORIE_ID + "=?",
                new String[]{String.valueOf(idSousCategorie)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        SousCategorie sousCategorie = new SousCategorie(
                cursor.getInt(cursor.getColumnIndex(SOUS_CATEGORIE_ID)),
                cursor.getInt(cursor.getColumnIndex(SOUS_CATEGORIE_ID_CATEGORIE)),
                cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_DESIGNATION)),
                getGroupProduitBySousCategorie(cursor.getInt(cursor.getColumnIndex(SOUS_CATEGORIE_ID)))
        );
        return sousCategorie;
    }
    public ArrayList<SousCategorie> getAllSousCategorie() {
        ArrayList<SousCategorie> souscategories = new ArrayList<SousCategorie>();
        String selectQuery = "SELECT * FROM " + TABLE_SOUS_CATEGORIE;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SousCategorie souscategorie = new SousCategorie();
                souscategorie.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_ID))));
                souscategorie.set_idCategorie(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_ID_CATEGORIE))));
                souscategorie.set_designation(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_DESIGNATION)));
                souscategorie.setProduits(getGroupProduitBySousCategorie(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_ID)))));
                souscategories.add(souscategorie);
            } while (cursor.moveToNext());
        }
        return souscategories;
    }
    public ArrayList<SousCategorie> getGroupSousCategorieByCategorie(int idCategorie) {
        ArrayList<SousCategorie> souscategories = new ArrayList<SousCategorie>();
        String selectQuery = "SELECT * FROM " + TABLE_SOUS_CATEGORIE+" WHERE "+SOUS_CATEGORIE_ID_CATEGORIE+" = "+idCategorie;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SousCategorie souscategorie = new SousCategorie();
                souscategorie.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_ID))));
                souscategorie.set_idCategorie(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_ID_CATEGORIE))));
                souscategorie.set_designation(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_DESIGNATION)));
                souscategorie.setProduits(getGroupProduitBySousCategorie(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUS_CATEGORIE_ID)))));
                souscategories.add(souscategorie);
            } while (cursor.moveToNext());
        }
        return souscategories;
    }
    public int getSousCategorieCount() {
        String countQuery = "SELECT * FROM " + TABLE_SOUS_CATEGORIE;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    //////////////--FIN FONCTIONS DES SOUS-CATEGORIES--////////////

    //////////////--FONCTIONS DES PRODUIT--//////////////
    public Produit getProduitByDesignationBySousCategorie(String designation, int idSousCategorie) {
        Cursor cursor = myDataBase.query(TABLE_PRODUIT, new String[]{PRODUIT_ID,
                        PRODUIT_ID_SOUS_CATEGORIE, PRODUIT_DESIGNATION, PRODUIT_PHOTO}, PRODUIT_DESIGNATION + "=? AND "+PRODUIT_ID_SOUS_CATEGORIE+" =?",
                new String[]{String.valueOf(designation), String.valueOf(idSousCategorie)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Produit produit = new Produit(
                    cursor.getInt(cursor.getColumnIndex(PRODUIT_ID)),
                    cursor.getInt(cursor.getColumnIndex(PRODUIT_ID_SOUS_CATEGORIE)),
                    cursor.getString(cursor.getColumnIndex(PRODUIT_DESIGNATION)),
                    cursor.getString(cursor.getColumnIndex(PRODUIT_PHOTO))
            );
            return produit;
        }
        else
            return null;

    }
    public Produit getProduitByDesignation(String designation) {
        Cursor cursor = myDataBase.query(TABLE_PRODUIT, new String[]{PRODUIT_ID,
                        PRODUIT_ID_SOUS_CATEGORIE, PRODUIT_DESIGNATION, PRODUIT_PHOTO}, PRODUIT_DESIGNATION + "=?",
                new String[]{String.valueOf(designation)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Produit produit = new Produit(
                    cursor.getInt(cursor.getColumnIndex(PRODUIT_ID)),
                    cursor.getInt(cursor.getColumnIndex(PRODUIT_ID_SOUS_CATEGORIE)),
                    cursor.getString(cursor.getColumnIndex(PRODUIT_DESIGNATION)),
                    cursor.getString(cursor.getColumnIndex(PRODUIT_PHOTO))
            );
            return produit;
        }
        else
            return null;

    }
    public Produit getProduitById(int idProduit) {
        Cursor cursor = myDataBase.query(TABLE_PRODUIT, new String[]{PRODUIT_ID,
                        PRODUIT_ID_SOUS_CATEGORIE, PRODUIT_DESIGNATION, PRODUIT_PHOTO}, PRODUIT_ID + "=?",
                new String[]{String.valueOf(idProduit)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Produit produit = new Produit(
                    cursor.getInt(cursor.getColumnIndex(PRODUIT_ID)),
                    cursor.getInt(cursor.getColumnIndex(PRODUIT_ID_SOUS_CATEGORIE)),
                    cursor.getString(cursor.getColumnIndex(PRODUIT_DESIGNATION)),
                    cursor.getString(cursor.getColumnIndex(PRODUIT_PHOTO))
            );
            return produit;
        }
        return null;

    }
    public ArrayList<Produit> getAllProduit() {
        ArrayList<Produit> produits = new ArrayList<Produit>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUIT;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Produit produit = new Produit();
                produit.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUIT_ID))));
                produit.set_idSousCategorie(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUIT_ID_SOUS_CATEGORIE))));
                produit.set_designation(cursor.getString(cursor.getColumnIndex(PRODUIT_DESIGNATION)));
                produit.set_photo(cursor.getString(cursor.getColumnIndex(PRODUIT_PHOTO)));
                produits.add(produit);
            } while (cursor.moveToNext());
        }
        return produits;
    }
    public ArrayList<Produit> getGroupProduitBySousCategorie(int idSousCategorie) {
        ArrayList<Produit> produits = new ArrayList<Produit>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUIT+" WHERE "+PRODUIT_ID_SOUS_CATEGORIE+" = "+idSousCategorie;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Produit produit = new Produit();
                produit.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUIT_ID))));
                produit.set_idSousCategorie(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUIT_ID_SOUS_CATEGORIE))));
                produit.set_designation(cursor.getString(cursor.getColumnIndex(PRODUIT_DESIGNATION)));
                produit.set_photo(cursor.getString(cursor.getColumnIndex(PRODUIT_PHOTO)));
                produits.add(produit);
            } while (cursor.moveToNext());
        }
        return produits;
    }
    public int getProduitCount() {
        String countQuery = "SELECT * FROM " + TABLE_PRODUIT;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    public void ajouterProduit(Produit produit) {
        ContentValues values = new ContentValues();
        values.put(PRODUIT_DESIGNATION, produit.get_designation());
        values.put(PRODUIT_ID_SOUS_CATEGORIE, produit.get_idSousCategorie());
        values.put(PRODUIT_PHOTO, produit.get_photo());
        myDataBase.insert(TABLE_PRODUIT, null, values);
    }
    public void deleteProduit(String prouitNom) {
        Produit produit = getProduitByDesignation(prouitNom);
        String strSQL = "DELETE FROM "+TABLE_PRODUIT+" WHERE "+PRODUIT_ID+" = "+ produit.get_id();
        myDataBase.execSQL(strSQL);

    }
    public void updateProduit(Produit produit)
    {
        String strSQL = "UPDATE "+TABLE_PRODUIT+" SET "
                +PRODUIT_DESIGNATION+" = '"+produit.get_designation()+"'"
                +" WHERE "+PRODUIT_ID+" = "+ produit.get_id();
        myDataBase.execSQL(strSQL);
    }
    //////////////--FIN FONCTIONS DES PRODUIT--//////////////

    //////////////--FONCTIONS DES RECHANGE--//////////////
    public Rechange getRechangeById(int idRechange) {
        Cursor cursor = myDataBase.query(TABLE_RECHANGE, new String[]{RECHANGE_ID,
                        RECHANGE_ID_PRODUIT, RECHANGE_DATE, RECHANGE_PRIX, RECHANGE_ID_CAISSE, RECHANGE_MAIN, RECHANGE_DESCRIPTION, RECHANGE_TEMPS}, RECHANGE_ID + "=?",
                new String[]{String.valueOf(idRechange)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(RECHANGE_TEMPS)));
            tmps = String.format("%06d", Integer.parseInt(tmps));
            String[] tmpsArray = tmps.split("");
            int heure, minute, seconde;

            heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
            minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
            seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);

            Rechange rechange = new Rechange(
                    cursor.getInt(cursor.getColumnIndex(RECHANGE_ID)),
                    getProduitById(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_PRODUIT))),
                    new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*CONVERSION_TIME),
                    cursor.getFloat(cursor.getColumnIndex(RECHANGE_PRIX)),
                    cursor.getFloat(cursor.getColumnIndex(RECHANGE_MAIN)),
                    cursor.getString(cursor.getColumnIndex(RECHANGE_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_CAISSE)),
                    new Time(heure, minute,seconde)
            );
            return rechange;
        }
        else
            return null;
    }
    public ArrayList<Rechange> getAllRechange(int idCaisse) {
        ArrayList<Rechange> rechanges = new ArrayList<Rechange>();
        String selectQuery = "SELECT * FROM " + TABLE_RECHANGE + " WHERE "+RECHANGE_ID_CAISSE+" = "+idCaisse;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if(cursor.getCount()>0)
        {
            if (cursor.moveToFirst()) {
                do {
                    String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(RECHANGE_TEMPS)));
                    String[] tmpsArray = tmps.split("");
                    int heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
                    int minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
                    int seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);
                    Rechange rechange = new Rechange();
                    rechange.set_id(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID)));
                    rechange.set_produit(getProduitById(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_PRODUIT))));
                    rechange.set_date(new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*CONVERSION_TIME));
                    rechange.set_prix(cursor.getFloat(cursor.getColumnIndex(RECHANGE_PRIX)));
                    rechange.set_idCaisse(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_CAISSE)));
                    rechange.set_main(cursor.getFloat(cursor.getColumnIndex(RECHANGE_MAIN)));
                    rechange.set_description(cursor.getString(cursor.getColumnIndex(RECHANGE_DESCRIPTION)));
                    rechange.setTemps(new Time(heure,minute,seconde));
                    rechanges.add(rechange);
                } while (cursor.moveToNext());
            }
            return rechanges;
        }
        return null;

    }
    public ArrayList<Rechange> getAllRechangeByProduit(int idProduit, int idCaisse) {


        ArrayList<Rechange> rechanges = new ArrayList<Rechange>();
        String selectQuery = "SELECT * FROM " + TABLE_RECHANGE+" WHERE "+RECHANGE_ID_PRODUIT+" = "+idProduit+" AND "+RECHANGE_ID_CAISSE+" = "+idCaisse;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(RECHANGE_TEMPS)));
                String[] tmpsArray = tmps.split("");
                int heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
                int minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
                int seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);
                Rechange rechange = new Rechange();
                rechange.set_id(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID)));
                rechange.set_produit(getProduitById(idProduit));
                rechange.set_date(new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*CONVERSION_TIME));
                rechange.set_prix(cursor.getFloat(cursor.getColumnIndex(RECHANGE_PRIX)));
                rechange.set_idCaisse(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_CAISSE)));
                rechange.set_main(cursor.getFloat(cursor.getColumnIndex(RECHANGE_MAIN)));
                rechange.set_description(cursor.getString(cursor.getColumnIndex(RECHANGE_DESCRIPTION)));
                rechange.setTemps(new Time(heure, minute, seconde));
                rechanges.add(rechange);
                //System.out.println("*****   OUTPUT  :"+new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*3600000)+" <--  <-- "+cursor.getInt(cursor.getColumnIndex(RECHANGE_DATE)));

            } while (cursor.moveToNext());
        }
        return rechanges;
    }
    public Rechange getLastRechangeByProduit(int idProduit, int idCaisse) throws ParseException {
        Cursor cursor = myDataBase.query(TABLE_RECHANGE, new String[]{RECHANGE_ID,
                        RECHANGE_ID_PRODUIT,RECHANGE_DATE, RECHANGE_PRIX, RECHANGE_MAIN, RECHANGE_DESCRIPTION, RECHANGE_ID_CAISSE}, RECHANGE_ID_PRODUIT + "=? AND "+RECHANGE_ID_CAISSE+" =?",
                new String[]{String.valueOf(idProduit), String.valueOf(idCaisse)}, null, null,RECHANGE_DATE+" DESC" , "1");
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(RECHANGE_TEMPS)));
            String[] tmpsArray = tmps.split("");
            int heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
            int minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
            int seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);
            Rechange rechange = new Rechange(
                    cursor.getInt(cursor.getColumnIndex(RECHANGE_ID)),
                    getProduitById(idProduit),
                    new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*CONVERSION_TIME),
                    cursor.getFloat(cursor.getColumnIndex(RECHANGE_PRIX)),
                    cursor.getFloat(cursor.getColumnIndex(RECHANGE_MAIN)),
                    cursor.getString(cursor.getColumnIndex(RECHANGE_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_CAISSE)),
                    new Time(heure,minute,seconde)
            );
            return rechange;

        }
        else
            return null;

    }
    public void ajouterRechange(Rechange rechange) {
        ContentValues values = new ContentValues();


        final Calendar c = Calendar.getInstance();
        String heure = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String seconde = String.valueOf(c.get(Calendar.SECOND));
        if(heure.length()==1)heure="0"+heure;
        if(minute.length()==1)minute="0"+minute;
        if(seconde.length()==1)seconde="0"+seconde;

        values.put(RECHANGE_ID_PRODUIT, rechange.get_produit().get_id());
        values.put(RECHANGE_DATE, rechange.get_date().getTime()/CONVERSION_TIME);
        values.put(RECHANGE_MAIN, rechange.get_main());
        values.put(RECHANGE_PRIX, rechange.get_prix());
        values.put(RECHANGE_DESCRIPTION, rechange.get_description());
        values.put(RECHANGE_ID_CAISSE, rechange.get_idCaisse());
        values.put(RECHANGE_TEMPS,Integer.parseInt(heure+minute+seconde));
        myDataBase.insert(TABLE_RECHANGE, null, values);

        Caisse caisse = getCaisse(rechange.get_idCaisse());
        String strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()-(rechange.get_prix()+rechange.get_main()))+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        myDataBase.execSQL(strSQL);
        calibrerCaisse(caisse.get_id());
        //System.out.println("*****   INPUT  :"+rechange.get_date() +" --> "+rechange.get_date().getTime()/3600000);



    }
    public ArrayList<Rechange> getAllRechangeByProduitOrderByDate(int idProduit, int idCaisse) {
        ArrayList<Rechange> rechanges = new ArrayList<Rechange>();
        String selectQuery = "SELECT * FROM " + TABLE_RECHANGE+" WHERE "+RECHANGE_ID_PRODUIT+" = "+idProduit+" AND "+ RECHANGE_ID_CAISSE+" = "+idCaisse+" ORDER BY "+RECHANGE_DATE+" DESC";
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(RECHANGE_TEMPS)));
                String[] tmpsArray = tmps.split("");
                int heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
                int minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
                int seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);
                Rechange rechange = new Rechange();
                rechange.set_id(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID)));
                rechange.set_produit(getProduitById(idProduit));
                rechange.set_date(new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*CONVERSION_TIME));
                rechange.set_prix(cursor.getFloat(cursor.getColumnIndex(RECHANGE_PRIX)));
                rechange.set_main(cursor.getFloat(cursor.getColumnIndex(RECHANGE_MAIN)));
                rechange.set_description(cursor.getString(cursor.getColumnIndex(RECHANGE_DESCRIPTION)));
                rechange.set_idCaisse(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_CAISSE)));
                rechange.setTemps(new Time(heure, minute,seconde));
                rechanges.add(rechange);
                //System.out.println("*****   OUTPUT  :"+new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*3600000)+" <--  <-- "+cursor.getInt(cursor.getColumnIndex(RECHANGE_DATE)));
            } while (cursor.moveToNext());
        }
        return rechanges;
    }
    public ArrayList<Rechange> getAllRechangeOrderByDate(int idCaisse) {
        ArrayList<Rechange> rechanges = new ArrayList<Rechange>();
        String selectQuery = "SELECT * FROM " + TABLE_RECHANGE+" WHERE "+RECHANGE_ID_CAISSE+" = "+idCaisse+"  ORDER BY "+RECHANGE_DATE+" DESC, "+RECHANGE_TEMPS+" DESC";
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(RECHANGE_TEMPS)));
                tmps = String.format("%06d", Integer.parseInt(tmps));
                String[] tmpsArray = tmps.split("");
                int heure, minute, seconde;

                    heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
                    minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
                    seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);

                Rechange rechange = new Rechange();
                rechange.set_id(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID)));
                rechange.set_produit(getProduitById(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_PRODUIT))));
                rechange.set_date(new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*CONVERSION_TIME));
                rechange.set_prix(cursor.getFloat(cursor.getColumnIndex(RECHANGE_PRIX)));
                rechange.set_main(cursor.getFloat(cursor.getColumnIndex(RECHANGE_MAIN)));
                rechange.set_description(cursor.getString(cursor.getColumnIndex(RECHANGE_DESCRIPTION)));
                rechange.set_idCaisse(cursor.getInt(cursor.getColumnIndex(RECHANGE_ID_CAISSE)));
                rechange.setTemps(new Time(heure, minute,seconde));
                rechanges.add(rechange);
                //System.out.println("*****   OUTPUT  :"+new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*3600000)+" <--  <-- "+cursor.getInt(cursor.getColumnIndex(RECHANGE_DATE)));
            } while (cursor.moveToNext());
        }
        return rechanges;
    }
    public void updateRechange(Rechange rechange) {
        Rechange rechange1 = getRechangeById(rechange.get_id());
        float montant = (rechange1.get_main() + rechange1.get_prix()) - (rechange.get_main() + rechange.get_prix());
        String strSQL = "UPDATE "+TABLE_RECHANGE+" SET "
                +RECHANGE_ID_PRODUIT+" = "+rechange.get_produit().get_id()+","
                +RECHANGE_PRIX+" = "+rechange.get_prix()+","
                +RECHANGE_MAIN+" = "+rechange.get_main()+","
                +RECHANGE_DATE+" = "+rechange.get_date().getTime()/CONVERSION_TIME+","
                +RECHANGE_DESCRIPTION+" = '"+rechange.get_description()+"'"
                +" WHERE "+RECHANGE_ID+" = "+ rechange.get_id();
        myDataBase.execSQL(strSQL);
        AddtoCaisse((float) montant, rechange.get_idCaisse());
    }
    public void deleteRechange(int idRechange) {
        Rechange rechange = getRechangeById(idRechange);
        float montant = (float) (rechange.get_prix()+rechange.get_main());
        String strSQL = "DELETE FROM "+TABLE_RECHANGE+" WHERE "+RECHANGE_ID+" = "+ idRechange;
        myDataBase.execSQL(strSQL);
        AddtoCaisse(montant, rechange.get_idCaisse());
    }
    //////////////--FIN FONCTIONS DES RECHANGE--//////////////

    //////////////--FONCTIONS DES OPERATIONS--//////////////
    public Operation getOperationById(int idOperation) {
        Cursor cursor = myDataBase.query(TABLE_OPERATION, new String[]{OPERATION_ID,
                        OPERATION_ID_CAISSE, OPERATION_TYPE, OPERATION_MONTANT, OPERATION_DATE, OPERATION_DESCRIPTION, OPERATION_TEMPS}, OPERATION_ID + "=?",
                new String[]{String.valueOf(idOperation)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(OPERATION_TEMPS)));
            tmps = String.format("%06d", Integer.parseInt(tmps));
            String[] tmpsArray = tmps.split("");
            int heure, minute, seconde;

            heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
            minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
            seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);


            Operation operation = new Operation(
                    cursor.getInt(cursor.getColumnIndex(OPERATION_ID)),
                    cursor.getInt(cursor.getColumnIndex(OPERATION_ID_CAISSE)),
                    cursor.getString(cursor.getColumnIndex(OPERATION_TYPE)).equals("DEBIT")? TypeOperation.DEBIT:TypeOperation.CREDIT,
                    cursor.getFloat(cursor.getColumnIndex(OPERATION_MONTANT)),
                    new Date(cursor.getLong(cursor.getColumnIndex(OPERATION_DATE))*CONVERSION_TIME),
                    cursor.getString(cursor.getColumnIndex(OPERATION_DESCRIPTION)),
                    new Time(heure, minute,seconde)
            );
            return operation;
        }
        else
            return null;
    }
    public ArrayList<Operation> getAllOperationOrderByDate(int idCaisse) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        String selectQuery = "SELECT * FROM " + TABLE_OPERATION + " WHERE "+OPERATION_ID_CAISSE+" = "+idCaisse+"  ORDER BY "+OPERATION_DATE+" DESC, "+OPERATION_TEMPS+" DESC";
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if(cursor.getCount()>0)
        {
            if (cursor.moveToFirst()) {
                do {
                    String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(OPERATION_TEMPS)));
                    tmps = String.format("%06d", Integer.parseInt(tmps));
                    String[] tmpsArray = tmps.split("");
                    int heure, minute, seconde;

                        heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
                        minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
                        seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);

                    Operation operation = new Operation();
                    operation.set_id(cursor.getInt(cursor.getColumnIndex(OPERATION_ID)));
                    operation.set_idCaisse(cursor.getInt(cursor.getColumnIndex(OPERATION_ID_CAISSE)));
                    operation.set_date(new Date(cursor.getLong(cursor.getColumnIndex(OPERATION_DATE))*CONVERSION_TIME));
                    operation.set_description(cursor.getString(cursor.getColumnIndex(OPERATION_DESCRIPTION)));
                    String type = cursor.getString(cursor.getColumnIndex(OPERATION_TYPE));
                    if(type.equals("CREDIT"))
                        operation.set_type(TypeOperation.CREDIT);
                    else
                        operation.set_type(TypeOperation.DEBIT);
                    operation.setTemps(new Time(heure, minute, seconde));
                    operation.set_montant(cursor.getFloat(cursor.getColumnIndex(OPERATION_MONTANT)));
                    operations.add(operation);
                } while (cursor.moveToNext());
            }

        }
        return  operations;

    }
    public void ajouterOperation(Operation operation) {
        ContentValues values = new ContentValues();
        final Calendar c = Calendar.getInstance();
        String heure = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String seconde = String.valueOf(c.get(Calendar.SECOND));

        if(heure.length()==1)heure="0"+heure;
        if(minute.length()==1)minute="0"+minute;
        if(seconde.length()==1)seconde="0"+seconde;

        values.put(OPERATION_ID_CAISSE, operation.get_idCaisse());
        values.put(OPERATION_MONTANT, operation.get_montant());
        values.put(OPERATION_TYPE, operation.get_type().toString());
        values.put(OPERATION_DATE, operation.get_date().getTime()/CONVERSION_TIME);
        values.put(OPERATION_DESCRIPTION, operation.get_description());
        values.put(OPERATION_TEMPS,Integer.parseInt(heure+minute+seconde));
        myDataBase.insert(TABLE_OPERATION, null, values);

        Caisse caisse = getCaisse(operation.get_idCaisse());

        String strSQL;
        if(operation.get_type() == TypeOperation.CREDIT)
            strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()+operation.get_montant())+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        else
            strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()-operation.get_montant())+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        myDataBase.execSQL(strSQL);
        calibrerCaisse(caisse.get_id());
        //System.out.println("*****   INPUT  :"+rechange.get_date() +" --> "+rechange.get_date().getTime()/3600000);






    }
    public void updateOperation(Operation operation) {
        Operation operation1 = getOperationById(operation.get_id());
        String strSQL = "UPDATE "+TABLE_OPERATION+" SET "
                +OPERATION_TYPE+" = '"+operation.get_type().toString()+"',"
                +OPERATION_MONTANT+" = "+operation.get_montant()+","
                +OPERATION_DATE+" = "+operation.get_date().getTime()/CONVERSION_TIME+","
                +OPERATION_DESCRIPTION+" = '"+operation.get_description()+"'"
                +" WHERE "+OPERATION_ID+" = "+ operation.get_id();
        myDataBase.execSQL(strSQL);
        if(operation1.get_type().toString().equals("DEBIT"))
        {
            AddtoCaisse(operation1.get_montant(), operation1.get_idCaisse());
        }
        else
        {
            SubFromCaisse(operation1.get_montant(), operation1.get_idCaisse());
        }
        if(operation.get_type().toString().equals("DEBIT"))
        {
            SubFromCaisse(operation.get_montant(),operation.get_idCaisse());
        }
        else
        {
            AddtoCaisse(operation.get_montant(), operation.get_idCaisse());
        }
    }
    public void deleteOperation(int idOperation) {
        Operation operation = getOperationById(idOperation);
        String strSQL = "DELETE FROM "+TABLE_OPERATION+" WHERE "+OPERATION_ID+" = "+ idOperation;
        myDataBase.execSQL(strSQL);
        if(operation.get_type()==TypeOperation.DEBIT)
        {
            AddtoCaisse(operation.get_montant(), operation.get_idCaisse());
        }
        else
        {
            SubFromCaisse(operation.get_montant(), operation.get_idCaisse());
        }

    }
    //////////////--FIN FONCTIONS DES OPERATIONS--//////////////

    /////////////-- FONCTION DES CAISSES--///////////////
    public Caisse getCaisse(int idCaisse) {
        Cursor cursor = myDataBase.query(TABLE_CAISSE, new String[]{CAISSE_ID,
                        CAISSE_TOTAL, CAISSE_LAST_OPERATION, CAISSE_PLUS}, CAISSE_ID + "=?",
                new String[]{String.valueOf(idCaisse)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Caisse caisse = new Caisse(
                    cursor.getInt(cursor.getColumnIndex(CAISSE_ID)),
                    cursor.getFloat(cursor.getColumnIndex(CAISSE_TOTAL)),
                    cursor.getFloat(cursor.getColumnIndex(CAISSE_LAST_OPERATION)),
                    cursor.getInt(cursor.getColumnIndex(CAISSE_PLUS))
            );
            return caisse;
        }
        else
            return null;
    }
    public float getLastOperation(int idCaisse)
    {
        return getCaisse(idCaisse).get_lastOperation();
    }
    public void setCaisseLastOperation(int idCaisse, float montantLastOperation) {
        Caisse caisse = getCaisse(idCaisse);
        String strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_LAST_OPERATION+" = "+montantLastOperation+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        myDataBase.execSQL(strSQL);
        calibrerCaisse(idCaisse);
    }
    public void undoCaisse(int idCaisse) {
        Caisse caisse = getCaisse(idCaisse);
        SubFromCaisse(caisse.get_lastOperation(), idCaisse);
        calibrerCaisse(idCaisse);
    }
    public void reinitialiserCaisse(int idCaisse) {
        Caisse caisse = getCaisse(idCaisse);
        if(caisse.get_total()!=0)caisse.set_lastOperation(-1*caisse.get_total());
        caisse.set_total(0);
        caisse.set_plus(0);
        updateCaisse(caisse);
    }
    public void AddtoCaisse(float montant, int idCaisse) {
        Caisse caisse = getCaisse(idCaisse);
        String strSQL;
        if(montant!=0)
         strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()+(montant))+", "+CAISSE_LAST_OPERATION+" = "+montant+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        else  strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()+(montant))+"  WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        myDataBase.execSQL(strSQL);
        calibrerCaisse(idCaisse);

    }
    public void SubFromCaisse(float montant, int idCaisse) {
        Caisse caisse = getCaisse(idCaisse);
        String strSQL;
        if(montant!=0)
         strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()-(montant))+", "+CAISSE_LAST_OPERATION+" = "+(-1*montant)+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        else
            strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()-(montant))+"  WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        myDataBase.execSQL(strSQL);
        calibrerCaisse(idCaisse);
    }
    public void calibrerCaisse(int idCaisse) {
        Caisse caisse = getCaisse(idCaisse);
        if(Math.abs(caisse.get_total())<0.5)
        {
            String strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = 0 WHERE "+CAISSE_ID+" = "+ caisse.get_id();
            myDataBase.execSQL(strSQL);
        }
    }
    public void calibrerCaissePersonnelle()
    {
        ArrayList<Revenu> revenus = getAllRevenuOrderByDate(Constantes.CAISSE_PERSONNELLE);
        float somme = 0;
        for(int i=0; i<revenus.size(); i++)
        {
            if(revenus.get(i).get_archive() == 0)
                somme+=revenus.get(i).get_montant();
        }
        Caisse caisse = getCaisse(Constantes.CAISSE_PERSONNELLE);
        AddtoCaisse(somme-caisse.get_total(), caisse.get_id());

    }
    public void updateCaisse(Caisse c)
    {
        String strSQL = "UPDATE "+TABLE_CAISSE+" SET "
                +CAISSE_TOTAL+" = "+c.get_total()+","
                +CAISSE_LAST_OPERATION+" = "+c.get_lastOperation()+","
                +CAISSE_PLUS+" = "+c.get_plus()
                +" WHERE "+CAISSE_ID+" = "+ c.get_id();
        myDataBase.execSQL(strSQL);
    }
    /////////////-- FIN FONCTION DES CAISSES--///////////////

    /////////////-- FONCTION REPOS--//////////////
    public Repos getReposById(int idRepos) {
        Cursor cursor = myDataBase.query(TABLE_REPOS, new String[]{REPOS_ID,
                        REPOS_ID_CAISSE, REPOS_DUREE, REPOS_PRIX_UNITAIRE, REPOS_DATE, REPOS_TEMPS, REPOS_DESCRIPTION}, REPOS_ID + "=?",
                new String[]{String.valueOf(idRepos)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(REPOS_TEMPS)));
            tmps = String.format("%06d", Integer.parseInt(tmps));
            String[] tmpsArray = tmps.split("");
            int heure, minute, seconde;

            heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
            minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
            seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);


            Repos repos = new Repos(
                    cursor.getInt(cursor.getColumnIndex(REPOS_ID)),
                    cursor.getInt(cursor.getColumnIndex(REPOS_DUREE)),
                    cursor.getFloat(cursor.getColumnIndex(REPOS_PRIX_UNITAIRE)),
                    new Date(cursor.getLong(cursor.getColumnIndex(REPOS_DATE))*CONVERSION_TIME),
                    new Time(heure, minute,seconde),
                    cursor.getString(cursor.getColumnIndex(REPOS_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(REPOS_ID_CAISSE))
            );
            return repos;
        }
        else
            return null;
    }
    public void ajouterRepos(Repos repos) {
        ContentValues values = new ContentValues();


        final Calendar c = Calendar.getInstance();
        String heure = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String seconde = String.valueOf(c.get(Calendar.SECOND));
        if(heure.length()==1)heure="0"+heure;
        if(minute.length()==1)minute="0"+minute;
        if(seconde.length()==1)seconde="0"+seconde;



        values.put(REPOS_PRIX_UNITAIRE, repos.getPrixUnitaire());
        values.put(REPOS_DATE, repos.get_date().getTime()/CONVERSION_TIME);
        values.put(REPOS_DUREE, repos.getDuree());
        values.put(REPOS_TEMPS, Integer.parseInt(heure+minute+seconde));
        values.put(REPOS_DESCRIPTION, repos.getDescription());
        values.put(REPOS_ID_CAISSE, repos.getIdCaisse());
        values.put(REPOS_ID_CAISSE, repos.getIdCaisse());
        myDataBase.insert(TABLE_REPOS, null, values);
        System.out.println("----------------------------------------------\n\n");
        System.out.println("Repos Ajouté (duree : "+repos.getDuree()+") ");
        System.out.println("----------------------------------------------\n\n");

        Caisse caisse = getCaisse(repos.getIdCaisse());
        String s = String.format(Locale.ROOT,"%.1f", (caisse.get_total()-(repos.getDuree()*(repos.getPrixUnitaire()/60f))));
        s.replaceAll(".", ",");
        String strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+s+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        myDataBase.execSQL(strSQL);
        calibrerCaisse(caisse.get_id());



    }
    public ArrayList<Repos> getAllReposOrderByDate(int idCaisse) {
        ArrayList<Repos> reposes = new ArrayList<Repos>();
        String selectQuery = "SELECT * FROM " + TABLE_REPOS+" WHERE "+REPOS_ID_CAISSE+" = "+idCaisse+"  ORDER BY "+REPOS_DATE+" DESC, "+REPOS_TEMPS+" DESC";

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        System.out.println("----------------------------------------------\n\n");
        System.out.println(cursor.getCount()+" Repos trouvés dans la base !");
        System.out.println("----------------------------------------------\n\n");
        if(cursor.getCount()>0)
        {
            if (cursor.moveToFirst()) {
                do {
                    String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(REPOS_TEMPS)));
                    tmps = String.format("%06d", Integer.parseInt(tmps));
                    String[] tmpsArray = tmps.split("");
                    int heure, minute, seconde;

                        heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
                        minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
                        seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);

                    Repos repos = new Repos();
                    repos.setId(cursor.getInt(cursor.getColumnIndex(REPOS_ID)));
                    repos.setIdCaisse(cursor.getInt(cursor.getColumnIndex(REPOS_ID_CAISSE)));
                    repos.setDuree(cursor.getInt(cursor.getColumnIndex(REPOS_DUREE)));
                    repos.setDate(new Date(cursor.getLong(cursor.getColumnIndex(REPOS_DATE))*CONVERSION_TIME));
                    repos.setPrixUnitaire(cursor.getFloat(cursor.getColumnIndex(REPOS_PRIX_UNITAIRE)));
                    repos.setTime(new Time(heure, minute,seconde));
                    repos.setDescription(cursor.getString(cursor.getColumnIndex(REPOS_DESCRIPTION)));
                    reposes.add(repos);
                    //System.out.println("*****   OUTPUT  :"+new Date(cursor.getLong(cursor.getColumnIndex(RECHANGE_DATE))*3600000)+" <--  <-- "+cursor.getInt(cursor.getColumnIndex(RECHANGE_DATE)));
                } while (cursor.moveToNext());
            }
        }
        return reposes;
    }
    public void updateRepos(Repos repos) {
        Repos repos1 = getReposById(repos.getId());
        String strSQL = "UPDATE "+TABLE_REPOS+" SET "
                +REPOS_DUREE+" = "+repos.getDuree()+","
                +REPOS_PRIX_UNITAIRE+" = "+repos.getPrixUnitaire()+","
                +REPOS_DATE+" = "+repos.get_date().getTime()/CONVERSION_TIME+","
                +REPOS_DESCRIPTION+" = '"+repos.getDescription()+"'"
                +" WHERE "+REPOS_ID+" = "+ repos.getId();
        myDataBase.execSQL(strSQL);
        String s = String.format(Locale.ROOT,"%.1f",repos1.getDuree()*(repos1.getPrixUnitaire()/60));
        AddtoCaisse(Float.parseFloat(s), repos1.getIdCaisse());

        s = String.format(Locale.ROOT,"%.1f",repos.getDuree()*(repos.getPrixUnitaire()/60));

        SubFromCaisse(Float.parseFloat(s), repos.getIdCaisse());


    }
    public void deleteRepos(int idRepos) {
        Repos repos = getReposById(idRepos);
        String strSQL = "DELETE FROM "+TABLE_REPOS+" WHERE "+REPOS_ID+" = "+ idRepos;
        myDataBase.execSQL(strSQL);
        String s = String.format(Locale.ROOT,"%.1f",(repos.getDuree()*(repos.getPrixUnitaire()/60)));
        s.replaceAll(",", ".");
        AddtoCaisse(Float.parseFloat(s), repos.getIdCaisse());
    }
    /////////////-- FIN FONCTION REPOS--////////////

    /////////////-- FONCTION REVENU--//////////////
    public Revenu getRevenuById(int idRevenu) {
        Cursor cursor = myDataBase.query(TABLE_REVENU, new String[]{REVENU_ID,
                        REVENU_ID_CAISSE, REVENU_MONTANT, REVENU_KILOMETRAGE, REVENU_COEFFICIENT, REVENU_DATE, REVENU_TEMPS, REVENU_TRAITE, REVENU_ARCHIVE, REVENU_KM_DEBUT, REVENU_KM_FIN}, REVENU_ID + "=?",
                new String[]{String.valueOf(idRevenu)}, null, null, null, null);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(REVENU_TEMPS)));
            tmps = String.format("%06d", Integer.parseInt(tmps));
            String[] tmpsArray = tmps.split("");
            int heure, minute, seconde;

            heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
            minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
            seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);


            Revenu revenu = new Revenu(
                    cursor.getInt(cursor.getColumnIndex(REVENU_ID)),
                    cursor.getInt(cursor.getColumnIndex(REVENU_ID_CAISSE)),
                    cursor.getFloat(cursor.getColumnIndex(REVENU_MONTANT)),
                    cursor.getInt(cursor.getColumnIndex(REVENU_KILOMETRAGE)),
                    cursor.getInt(cursor.getColumnIndex(REVENU_KM_DEBUT)),
                    cursor.getInt(cursor.getColumnIndex(REVENU_KM_FIN)),
                    cursor.getFloat(cursor.getColumnIndex(REVENU_COEFFICIENT)),
                    new Date(cursor.getLong(cursor.getColumnIndex(REVENU_DATE))*CONVERSION_TIME),
                    new Time(heure, minute, seconde),
                    cursor.getFloat(cursor.getColumnIndex(REVENU_TRAITE)),
                    cursor.getInt(cursor.getColumnIndex(REVENU_ARCHIVE))
            );
            return revenu;
        }
        else
            return null;
    }
    public void ajouterRevenu(Revenu revenu) {
        ContentValues values = new ContentValues();


        final Calendar c = Calendar.getInstance();
        String heure = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String seconde = String.valueOf(c.get(Calendar.SECOND));
        if(heure.length()==1)heure="0"+heure;
        if(minute.length()==1)minute="0"+minute;
        if(seconde.length()==1)seconde="0"+seconde;



        values.put(REVENU_COEFFICIENT, revenu.get_coefficient());
        values.put(REVENU_DATE, revenu.get_date().getTime()/CONVERSION_TIME);
        values.put(REVENU_ID_CAISSE, revenu.get_idCaisse());
        values.put(REVENU_KILOMETRAGE, revenu.get_kilometrage());
        values.put(REVENU_KM_DEBUT, revenu.get_km_debut());
        values.put(REVENU_KM_FIN, revenu.get_km_fin());
        values.put(REVENU_MONTANT, revenu.get_montant());
        values.put(REVENU_TEMPS,Integer.parseInt(heure+minute+seconde));
        values.put(REVENU_TRAITE, revenu.get_traite());
        values.put(REVENU_ARCHIVE, revenu.get_archive());
        myDataBase.insert(TABLE_REVENU, null, values);

        Caisse caisse = getCaisse(revenu.get_idCaisse());
        String strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = "+(caisse.get_total()+revenu.get_montant())+" WHERE "+CAISSE_ID+" = "+ caisse.get_id();
        myDataBase.execSQL(strSQL);
        calibrerCaisse(caisse.get_id());



    }
    public ArrayList<Revenu> getAllRevenuOrderByDate(int idCaisse) {
        ArrayList<Revenu> revenus = new ArrayList<Revenu>();
        String selectQuery = "SELECT * FROM " + TABLE_REVENU+" WHERE "+REVENU_ID_CAISSE+" = "+idCaisse+"  ORDER BY "+REVENU_DATE+" DESC, "+REVENU_TEMPS+" DESC";

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);


        if(cursor.getCount()>0)
        {
            if (cursor.moveToFirst()) {
                do {
                    String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(REVENU_TEMPS)));
                    tmps = String.format("%06d", Integer.parseInt(tmps));
                    String[] tmpsArray = tmps.split("");
                    int heure, minute, seconde;

                        heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
                        minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
                        seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);



                    Revenu revenu = new Revenu();
                    revenu.set_coefficient(cursor.getFloat(cursor.getColumnIndex(REVENU_COEFFICIENT)));
                    revenu.set_date(new Date(cursor.getLong(cursor.getColumnIndex(REVENU_DATE))*CONVERSION_TIME));
                    revenu.set_idCaisse(cursor.getInt(cursor.getColumnIndex(REVENU_ID_CAISSE)));
                    revenu.set_kilometrage(cursor.getInt(cursor.getColumnIndex(REVENU_KILOMETRAGE)));
                    revenu.set_km_debut(cursor.getInt(cursor.getColumnIndex(REVENU_KM_DEBUT)));
                    revenu.set_km_fin(cursor.getInt(cursor.getColumnIndex(REVENU_KM_FIN)));
                    revenu.set_montant(cursor.getFloat(cursor.getColumnIndex(REVENU_MONTANT)));
                    revenu.setTime(new Time(heure, minute,seconde));
                    revenu.set_id(cursor.getInt(cursor.getColumnIndex(REVENU_ID)));
                    revenu.set_traite(cursor.getFloat(cursor.getColumnIndex(REVENU_TRAITE)));
                    revenu.set_archive(cursor.getInt(cursor.getColumnIndex(REVENU_ARCHIVE)));

                    revenus.add(revenu);
                } while (cursor.moveToNext());
            }
        }
        return revenus;
    }
    public int getNbRevenusNonArchives()
    {
        ArrayList<Revenu> revenus = getAllRevenuOrderByDate(Constantes.CAISSE_PERSONNELLE);
        int cmp = 0;
        for(int i=0; i<revenus.size(); i++)
        {
            if(revenus.get(i).get_archive() == 0)
                cmp ++;
        }
        return cmp;
    }
    public void desarchiver(int idHistoRevenu)
    {
        ArrayList<Revenu> revenus = getAllRevenuOrderByDate(Constantes.CAISSE_PERSONNELLE);
        HistoRevenu histoRevenu = getHistoRevenuById(idHistoRevenu);
        for(int i=0; i<revenus.size(); i++)
        {
            if(revenus.get(i).get_date().getMonth() == histoRevenu.get_date().getMonth() && revenus.get(i).get_date().getYear() == histoRevenu.get_date().getYear())
            {
                revenus.get(i).set_archive(0);
                AddtoCaisse(revenus.get(i).get_montant(),Constantes.CAISSE_PERSONNELLE);
                updateRevenu(revenus.get(i));
            }

        }
        deleteHistoRevenu(idHistoRevenu);

    }
    public void viderRevenu(int idCaisse) {
        String strSQL = "DELETE FROM "+TABLE_REVENU+" WHERE 1";
        myDataBase.execSQL(strSQL);
        strSQL = "UPDATE "+TABLE_CAISSE+" SET "+CAISSE_TOTAL+" = 0 WHERE "+CAISSE_ID+" = "+ Constantes.CAISSE_PERSONNELLE;
        myDataBase.execSQL(strSQL);
        calibrerCaisse(idCaisse);
    }
    public void updateRevenu(Revenu revenu) {
        Revenu revenu1 = getRevenuById(revenu.get_id());
        String strSQL = "UPDATE "+TABLE_REVENU+" SET "
                +REVENU_MONTANT+" = "+revenu.get_montant()+","
                +REVENU_KILOMETRAGE+" = "+revenu.get_kilometrage()+","
                +REVENU_KM_DEBUT+" = "+revenu.get_km_debut()+","
                +REVENU_KM_FIN+" = "+revenu.get_km_fin()+","
                +REVENU_TRAITE+" = "+revenu.get_traite()+","
                +REVENU_DATE+" = "+revenu.get_date().getTime()/CONVERSION_TIME+","
                +REVENU_ARCHIVE+" = "+revenu.get_archive()+","
                +REVENU_COEFFICIENT+" = "+revenu.get_coefficient()
                +" WHERE "+REVENU_ID+" = "+ revenu.get_id();
        myDataBase.execSQL(strSQL);

        SubFromCaisse(revenu1.get_montant(), revenu1.get_idCaisse());
        AddtoCaisse(revenu.get_montant(), revenu.get_idCaisse());
    }
    public void deleteRevenu(int idRevenu) {
        Revenu revenu = getRevenuById(idRevenu);
        String strSQL = "DELETE FROM "+TABLE_REVENU+" WHERE "+REVENU_ID+" = "+ idRevenu;
        myDataBase.execSQL(strSQL);
        SubFromCaisse(revenu.get_montant(), revenu.get_idCaisse());
    }
    /////////////-- FIN FONCTION REVENU--//////////////

    /////////////-- FONCTION HISTO_REVENU--//////////////
    public void ajouterHistoRevenu(HistoRevenu histoRevenu) {
        ContentValues values = new ContentValues();
        values.put(HISTO_REVENU_DATE, histoRevenu.get_date().getTime()/CONVERSION_TIME);
        values.put(HISTO_REVENU_TOTAL, histoRevenu.get_total());
        values.put(HISTO_REVENU_KILOMETRAGE, histoRevenu.get_kilometrage());
        myDataBase.insert(TABLE_HISTO_REVENU, null, values);
    }
    public ArrayList<HistoRevenu> getAllHistoRevenuOrderByDate() {
        ArrayList<HistoRevenu> histoRevenus = new ArrayList<HistoRevenu>();
        String selectQuery = "SELECT * FROM " + TABLE_HISTO_REVENU+"  ORDER BY "+HISTO_REVENU_DATE+" DESC";

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        if(cursor.getCount()>0)
        {
            if (cursor.moveToFirst()) {
                do {


                    HistoRevenu histoRevenu = new HistoRevenu();
                    histoRevenu.set_id(cursor.getInt(cursor.getColumnIndex(HISTO_REVENU_ID)));
                    histoRevenu.set_date(new Date(cursor.getLong(cursor.getColumnIndex(HISTO_REVENU_DATE))*CONVERSION_TIME));
                    histoRevenu.set_total(cursor.getFloat(cursor.getColumnIndex(HISTO_REVENU_TOTAL)));
                    histoRevenu.set_kilometrage(cursor.getInt(cursor.getColumnIndex(HISTO_REVENU_KILOMETRAGE)));

                    histoRevenus.add(histoRevenu);
                } while (cursor.moveToNext());
            }
        }
        return histoRevenus;
    }
    public HistoRevenu getHistoRevenuById(int idHistoRevenu)
    {
        Cursor cursor = myDataBase.query(TABLE_HISTO_REVENU, new String[]{HISTO_REVENU_ID,
                        HISTO_REVENU_DATE, HISTO_REVENU_KILOMETRAGE, HISTO_REVENU_TOTAL}, HISTO_REVENU_ID + "=?",
                new String[]{String.valueOf(idHistoRevenu)}, null, null, null, null);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();


            HistoRevenu histoRevenu = new HistoRevenu(
                    cursor.getInt(cursor.getColumnIndex(HISTO_REVENU_ID)),
                    cursor.getFloat(cursor.getColumnIndex(HISTO_REVENU_TOTAL)),
                    new Date(cursor.getLong(cursor.getColumnIndex(HISTO_REVENU_DATE))*CONVERSION_TIME),
                    cursor.getInt(cursor.getColumnIndex(HISTO_REVENU_KILOMETRAGE))
            );
            return histoRevenu;
        }
        else
            return null;
    }
    public void deleteHistoRevenu(int idHistoRevenu)
    {
        String strSQL = "DELETE FROM "+TABLE_HISTO_REVENU+" WHERE "+HISTO_REVENU_ID+" = "+ idHistoRevenu;
        myDataBase.execSQL(strSQL);
    }
    public void updateHistoRevenu(HistoRevenu hr)
    {
        String strSQL = "UPDATE "+TABLE_HISTO_REVENU+" SET "
                +HISTO_REVENU_TOTAL+" = "+hr.get_total()+","
                +HISTO_REVENU_KILOMETRAGE+" = "+hr.get_kilometrage()+","
                +HISTO_REVENU_DATE+" = "+hr.get_date().getTime()/CONVERSION_TIME
                +" WHERE "+HISTO_REVENU_ID+" = "+ hr.get_id();
        myDataBase.execSQL(strSQL);

    }
    /////////////-- FIN FONCTION HISTO_REVENU--//////////////

    ////////////// FONCTION OPERATION AUTO ///////////
    public void updateOperationAuto (int idOperation, float montantOperationAuto) {
        String strSQL = "UPDATE "+TABLE_OPERATION_AUTO+" SET "+OPERATION_AUTO_MONTANT+" = "+montantOperationAuto+" WHERE "+OPERATION_AUTO_ID+" = "+ idOperation;
        myDataBase.execSQL(strSQL);

    }
    public OperationAuto getOperationAuto(int idOperationAuto){
        Cursor cursor = myDataBase.query(TABLE_OPERATION_AUTO, new String[]{OPERATION_AUTO_ID,
                        OPERATION_AUTO_ID_CAISSE, OPERATION_AUTO_MONTANT, OPERATION_AUTO_ACTIVE, OPERATION_AUTO_MONTANT, OPERATION_AUTO_TIME}, OPERATION_AUTO_ID + "=?",
                new String[]{String.valueOf(idOperationAuto)}, null, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            String tmps = String.valueOf(cursor.getInt(cursor.getColumnIndex(OPERATION_AUTO_TIME)));
            tmps = String.format("%06d", Integer.parseInt(tmps));
            String[] tmpsArray = tmps.split("");
            int heure, minute, seconde;

            heure = Integer.parseInt(tmpsArray[0]+tmpsArray[1]);
            minute = Integer.parseInt(tmpsArray[2]+tmpsArray[3]);
            seconde = Integer.parseInt(tmpsArray[4]+tmpsArray[5]);
            OperationAuto operationAuto = new OperationAuto(
                    cursor.getInt(cursor.getColumnIndex(OPERATION_AUTO_ID)),
                    cursor.getInt(cursor.getColumnIndex(OPERATION_AUTO_ID_CAISSE)),
                    cursor.getFloat(cursor.getColumnIndex(OPERATION_AUTO_MONTANT)),
                    new Time(heure, minute,seconde),
                    cursor.getInt(cursor.getColumnIndex(OPERATION_AUTO_ACTIVE))
                    );
            return operationAuto;
        }
        else
            return null;
    }
    public void executerOperationAuto(int idOperationAuto) {
        OperationAuto operationAuto = getOperationAuto(idOperationAuto);


        Operation operation = new Operation();
        operation.set_idCaisse(operationAuto.get_idCaisse());
        operation.set_type(TypeOperation.DEBIT);
        operation.set_montant(operationAuto.get_montant());

        final Calendar c = Calendar.getInstance();
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        String year = String.valueOf(c.get(Calendar.YEAR));


        try {
            operation.set_date((new SimpleDateFormat("dd-MM-yyyy").parse(day+"-"+month+"-"+year)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        operation.set_description("Operation programmée");
        ajouterOperation(operation);
    }
    public void activerOperationAuto(int idOperationAuto) {
        String strSQL = "UPDATE "+TABLE_OPERATION_AUTO+" SET "+OPERATION_AUTO_ACTIVE+" = 1"+" WHERE "+OPERATION_AUTO_ID+" = "+idOperationAuto;
        myDataBase.execSQL(strSQL);
    }
    public void desactiverOperationAuto(int idOperationAuto) {
        String strSQL = "UPDATE "+TABLE_OPERATION_AUTO+" SET "+OPERATION_AUTO_ACTIVE+" = 0"+" WHERE "+OPERATION_AUTO_ID+" = "+idOperationAuto;
        myDataBase.execSQL(strSQL);

    }
    ////////////// FIN FONCTION OPERATION AUTO ///////////

    public ArrayList<manip> getHistorique(int idCaisse) {
        ArrayList<manip> rechAndOpAndRep = new ArrayList<>();

        ArrayList<Operation> operations = getAllOperationOrderByDate(Constantes.CAISSE_OPERATION);
        ArrayList<Rechange> rechanges = getAllRechangeOrderByDate(Constantes.CAISSE_OPERATION);
        ArrayList<Repos> reposes = getAllReposOrderByDate(Constantes.CAISSE_OPERATION);
        System.out.println("----------------------------------------------\n\n");
        System.out.println(operations.size()+" Operations.");
        System.out.println(rechanges.size()+" Rechanges.");
        System.out.println(reposes.size()+" Repos.");
        System.out.println("----------------------------------------------\n\n");

        int compteur_ope = 0, compteur_rechg = 0, compteur_repo = 0;
        while (compteur_ope<operations.size() && compteur_rechg<rechanges.size() && compteur_repo<reposes.size())
        {
            if(balance(operations.get(compteur_ope),rechanges.get(compteur_rechg))==1)
            {
                if(balance(operations.get(compteur_ope),reposes.get(compteur_repo))==1)
                {
                    rechAndOpAndRep.add(operations.get(compteur_ope));
                    compteur_ope++;
                }
                else if(balance(operations.get(compteur_ope),reposes.get(compteur_repo))==2)
                {
                    rechAndOpAndRep.add(reposes.get(compteur_repo));
                    compteur_repo++;
                }
            }
            else if(balance(operations.get(compteur_ope),rechanges.get(compteur_rechg))==2)
            {
                if(balance(rechanges.get(compteur_rechg),reposes.get(compteur_repo))==1)
                {
                    rechAndOpAndRep.add(rechanges.get(compteur_rechg));
                    compteur_rechg++;
                }
                else if(balance(rechanges.get(compteur_rechg),reposes.get(compteur_repo))==2)
                {
                    rechAndOpAndRep.add(reposes.get(compteur_repo));
                    compteur_repo++;
                }
            }
        }

        if(compteur_ope==operations.size())
        {
            while(compteur_rechg<rechanges.size() && compteur_repo<reposes.size())
            {
                if(balance(rechanges.get(compteur_rechg),reposes.get(compteur_repo))==1)
                {
                    rechAndOpAndRep.add(rechanges.get(compteur_rechg));
                    compteur_rechg++;
                }
                else if(balance(rechanges.get(compteur_rechg),reposes.get(compteur_repo))==2)
                {
                    rechAndOpAndRep.add(reposes.get(compteur_repo));
                    compteur_repo++;
                }
            }
            if(compteur_rechg==rechanges.size())
            {
                while(compteur_repo<reposes.size())
                {
                    rechAndOpAndRep.add(reposes.get(compteur_repo));
                    compteur_repo++;
                }
            }
            else
            {
                while(compteur_rechg<rechanges.size())
                {
                    rechAndOpAndRep.add(rechanges.get(compteur_rechg));
                    compteur_rechg++;
                }
            }

        }
        else if(compteur_rechg==rechanges.size())
        {
            while(compteur_ope<operations.size()  && compteur_repo<reposes.size())
            {
                if(balance(operations.get(compteur_ope),reposes.get(compteur_repo))==1)
                {
                    rechAndOpAndRep.add(operations.get(compteur_ope));
                    compteur_ope++;
                }
                else if(balance(operations.get(compteur_ope),reposes.get(compteur_repo))==2)
                {
                    rechAndOpAndRep.add(reposes.get(compteur_repo));
                    compteur_repo++;
                }
            }
            if(compteur_ope==operations.size())
            {
                while(compteur_repo<reposes.size())
                {
                    rechAndOpAndRep.add(reposes.get(compteur_repo));
                    compteur_repo++;
                }
            }
            else
            {
                while(compteur_ope<operations.size())
                {
                    rechAndOpAndRep.add(operations.get(compteur_ope));
                    compteur_ope++;
                }
            }
        }
        else
        {
            while(compteur_ope<operations.size() && compteur_rechg<rechanges.size())
            {
                if(balance(operations.get(compteur_ope),rechanges.get(compteur_rechg))==1)
                {
                    rechAndOpAndRep.add(operations.get(compteur_ope));
                    compteur_ope++;
                }
                else if(balance(operations.get(compteur_ope),rechanges.get(compteur_rechg))==2)
                {
                    rechAndOpAndRep.add(rechanges.get(compteur_rechg));
                    compteur_rechg++;
                }
            }
            if(compteur_ope==operations.size())
            {
                while(compteur_rechg<rechanges.size())
                {
                    rechAndOpAndRep.add(rechanges.get(compteur_rechg));
                    compteur_rechg++;
                }
            }
            else
            {
                while(compteur_ope<operations.size())
                {
                    rechAndOpAndRep.add(operations.get(compteur_ope));
                    compteur_ope++;
                }
            }
        }

        return rechAndOpAndRep;
    }
    public int balance(manip m1, manip m2) {
        if(m1.get_date().getTime()>m2.get_date().getTime())
        {
            return 1;
        }
        else if(m1.get_date().getTime()<m2.get_date().getTime())
        {
            return 2;
        }
        else
        {
            if(m1.getTemps().getTime()>m2.getTemps().getTime())
            {
                return 1;
            }
            else if(m1.getTemps().getTime()<m2.getTemps().getTime())
            {
                return 2;
            }
        }
        return 0;
    }
}
