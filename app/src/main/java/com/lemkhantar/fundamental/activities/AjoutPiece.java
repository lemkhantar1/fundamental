package com.lemkhantar.fundamental.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Categorie;
import com.lemkhantar.fundamental.entity.Produit;
import com.lemkhantar.fundamental.entity.SousCategorie;

import java.util.ArrayList;

public class AjoutPiece extends AppCompatActivity {

    AutoCompleteTextView f_categorie, f_sousCategorie,f_produit;
    Button saveButton, cancelButton;
    Typeface font;
    boolean FOR_UPDATE=false;
    int idProduitToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_piece);

        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");

        f_categorie = (AutoCompleteTextView)findViewById(R.id.nvCategorie);
        f_sousCategorie = (AutoCompleteTextView)findViewById(R.id.nvSousCategorie);
        f_produit = (AutoCompleteTextView) findViewById(R.id.nvProduit);
        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        f_categorie.setTypeface(font);
        f_sousCategorie.setTypeface(font);
        f_produit.setTypeface(font);
        saveButton.setTypeface(font);
        cancelButton.setTypeface(font);

        final DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        ArrayList<Categorie> categories = dbManager.getAllCategorie();
        ArrayAdapter<Categorie> adapter = new ArrayAdapter<Categorie>(this, android.R.layout.simple_dropdown_item_1line, categories);
        f_categorie.setAdapter(adapter);
        f_categorie.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s)
            {
                Categorie categorie = dbManager.getCategorieByDesignation(f_categorie.getText().toString());
                if(categorie!=null)
                {
                    ArrayList<SousCategorie> sousCategories = dbManager.getGroupSousCategorieByCategorie(categorie.get_id());
                    ArrayAdapter<SousCategorie> adapter2 = new ArrayAdapter<SousCategorie>(f_sousCategorie.getContext(), android.R.layout.simple_dropdown_item_1line,sousCategories);
                    f_sousCategorie.setAdapter(adapter2);
                    f_produit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count)
                        {
                            SousCategorie sousCategorie = dbManager.getSousCategorieByDesignation(f_sousCategorie.getText().toString());
                            if(sousCategorie!=null)
                            {
                                ArrayList<Produit> produits = dbManager.getGroupProduitBySousCategorie(sousCategorie.get_id());
                                ArrayAdapter<Produit> adapter3 = new ArrayAdapter<Produit>(f_produit.getContext(), android.R.layout.simple_dropdown_item_1line,produits);
                                f_produit.setAdapter(adapter3);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                }
            }
        });

        Intent myIntent = getIntent();
        if(myIntent.getExtras()!=null)
        {
            if(myIntent.getExtras().get("produit")!=null) {
                String produitNom = myIntent.getExtras().get("produit").toString();
                Produit produit = dbManager.getProduitByDesignation(produitNom);
                idProduitToUpdate = produit.get_id();
                f_categorie.setText(dbManager.getCategorieById(dbManager.getSousCategorieById(produit.get_idSousCategorie()).get_idCategorie()).get_designation());
                f_sousCategorie.setText(dbManager.getSousCategorieById(produit.get_idSousCategorie()).get_designation());
                f_produit.setText(produitNom);
                f_categorie.setEnabled(false);
                f_sousCategorie.setEnabled(false);
                FOR_UPDATE = true;
            }
        }

    }

    public void enregistrerPiece(View view)
    {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();
        Categorie categorie = dbManager.getCategorieByDesignation(f_categorie.getText().toString());
        if(categorie!=null)
        {
            SousCategorie sousCategorie = dbManager.getSousCategorieByDesignation(f_sousCategorie.getText().toString());
            if(sousCategorie!=null)
            {
                Produit produit = dbManager.getProduitByDesignation(f_produit.getText().toString());
                if(produit!=null)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Ce produit existe deja dans le garage !!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    if(f_produit.getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Tapez un nom de produit !!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else
                    {
                        if(FOR_UPDATE)
                        {
                            Produit produit1 = dbManager.getProduitById(idProduitToUpdate);
                            produit1.set_designation(f_produit.getText().toString());
                            dbManager.updateProduit(produit1);
                            Intent i = new Intent(AjoutPiece.this, Garage.class);
                            startActivity(i);
                        }
                        Produit produit1 = new Produit();
                        produit1.set_designation(f_produit.getText().toString());
                        produit1.set_idSousCategorie(sousCategorie.get_id());
                        produit1.set_photo("");
                        dbManager.ajouterProduit(produit1);
                        Intent i = new Intent(AjoutPiece.this, Garage.class);
                        startActivity(i);
                    }
                }
            }
            else
            {
                Produit produit = dbManager.getProduitByDesignation(f_produit.getText().toString());
                if(produit!=null)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Alerte");
                    alertDialog.setMessage("Ce produit existe deja dans \nCategorie : "+dbManager.getCategorieById(dbManager.getSousCategorieById(produit.get_idSousCategorie()).get_idCategorie()).get_designation()+"\nSousCategorie :"+dbManager.getSousCategorieById(produit.get_idSousCategorie()).get_designation());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    if(f_sousCategorie.getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Alerte");
                        alertDialog.setMessage("Tapez une sous categorie !!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else  if(f_produit.getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Alerte");
                        alertDialog.setMessage("Tapez un nom de produit !!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else
                    {
                        SousCategorie sousCategorie1 = new SousCategorie();
                        sousCategorie1.set_designation(f_sousCategorie.getText().toString());
                        sousCategorie1.set_idCategorie(categorie.get_id());
                        dbManager.ajouterSousCategorie(sousCategorie1);


                        Produit produit1 = new Produit();
                        produit1.set_designation(f_produit.getText().toString());
                        produit1.set_idSousCategorie(dbManager.getSousCategorieByDesignation(f_sousCategorie.getText().toString()).get_id());
                        produit1.set_photo("");
                        dbManager.ajouterProduit(produit1);

                        Intent i = new Intent(AjoutPiece.this, Garage.class);
                        startActivity(i);
                    }
                }
            }
        }
        else
        {
            SousCategorie sousCategorie = dbManager.getSousCategorieByDesignation(f_sousCategorie.getText().toString());
            if(sousCategorie!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Alerte");
                alertDialog.setMessage("Cette sous Categorie existe deja dans \nCategorie : "+dbManager.getCategorieById(sousCategorie.get_idCategorie()).get_designation());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            else
            {
                Produit produit = dbManager.getProduitByDesignation(f_produit.getText().toString());
                if(produit!=null)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Alerte");
                    alertDialog.setMessage("Ce produit existe deja dans \nCategorie : "+dbManager.getCategorieById(dbManager.getSousCategorieById(produit.get_idSousCategorie()).get_idCategorie()).get_designation()+"\nSousCategorie :"+dbManager.getSousCategorieById(produit.get_idSousCategorie()).get_designation());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    if(f_categorie.getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Alerte");
                        alertDialog.setMessage("Tapez une categorie !!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else if(f_sousCategorie.getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Alerte");
                        alertDialog.setMessage("Tapez une sous categorie !!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else  if(f_produit.getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Alerte");
                        alertDialog.setMessage("Tapez un nom de produit !!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else
                    {
                        Categorie categorie1 = new Categorie();
                        categorie1.set_designation(f_categorie.getText().toString());
                        categorie1.set_photo("garageicon");
                        dbManager.ajouterCategorie(categorie1);

                        SousCategorie sousCategorie1 = new SousCategorie();
                        sousCategorie1.set_designation(f_sousCategorie.getText().toString());
                        sousCategorie1.set_idCategorie(dbManager.getCategorieByDesignation(f_categorie.getText().toString()).get_id());
                        dbManager.ajouterSousCategorie(sousCategorie1);


                        Produit produit1 = new Produit();
                        produit1.set_designation(f_produit.getText().toString());
                        produit1.set_idSousCategorie(dbManager.getSousCategorieByDesignation(f_sousCategorie.getText().toString()).get_id());
                        produit1.set_photo("");
                        dbManager.ajouterProduit(produit1);

                        Intent i = new Intent(AjoutPiece.this, Garage.class);
                        startActivity(i);
                    }
                }
            }
        }

    }

    public void annuler(View view)
    {
        Intent i = new Intent(AjoutPiece.this, Garage.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        annuler(null);
    }

    public void backToTheFuture(View view)
    {
        Intent intent = new Intent(AjoutPiece.this, Garage.class);
        startActivity(intent);
    }


}
