package com.lemkhantar.fundamental.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.entity.Categorie;
import com.lemkhantar.fundamental.entity.Produit;
import com.lemkhantar.fundamental.entity.Rechange;
import com.lemkhantar.fundamental.entity.SousCategorie;
import com.lemkhantar.fundamental.helper.Constantes;
import com.lemkhantar.fundamental.helper.DateDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Rechanger extends AppCompatActivity {

    EditText f_date, f_prix, f_main, f_description;
    AutoCompleteTextView f_nom, f_categorie, f_sousCategorie;
    TextView c_categorie, c_sousCategorie, c_produit, c_prix, c_main, c_date, c_description, total, title;
    private  boolean FROM_GARAGE, FROM_UPDATE;
    private int ID_UPDATE;
    Button saveButton, cancelButton;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechanger);
        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");

        FROM_GARAGE = false;
        FROM_UPDATE = false;

        f_date = (EditText)findViewById(R.id.f_dateRechange);
        f_nom = (AutoCompleteTextView) findViewById(R.id.f_nomProduit);
        f_categorie = (AutoCompleteTextView) findViewById(R.id.f_categorie);
        f_sousCategorie = (AutoCompleteTextView) findViewById(R.id.f_sousCategorie);
        f_main = (EditText)findViewById(R.id.f_main);
        f_description = (EditText)findViewById(R.id.f_description);
        f_prix = (EditText)findViewById(R.id.f_prixProduit);


        c_categorie = (TextView)findViewById(R.id.c_categorie);
        c_sousCategorie = (TextView)findViewById(R.id.c_sousCategorie);
        c_produit = (TextView)findViewById(R.id.c_produit);
        c_prix = (TextView)findViewById(R.id.c_prix);
        c_main = (TextView)findViewById(R.id.c_main);
        c_date = (TextView)findViewById(R.id.c_date);
        c_description = (TextView)findViewById(R.id.c_description);
        total = (TextView)findViewById(R.id.total);
        title = (TextView)findViewById(R.id.title);

        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        f_date.setTypeface(font);
        f_nom.setTypeface(font);
        f_categorie.setTypeface(font);
        f_sousCategorie.setTypeface(font);
        f_main.setTypeface(font);
        f_description.setTypeface(font);
        f_prix.setTypeface(font);
        c_categorie.setTypeface(font);
        c_sousCategorie.setTypeface(font);
        c_produit.setTypeface(font);
        c_prix.setTypeface(font);
        c_main.setTypeface(font);
        c_date.setTypeface(font);
        c_description.setTypeface(font);
        total.setTypeface(font);
        title.setTypeface(font);
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
                    f_nom.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count)
                        {
                            SousCategorie sousCategorie = dbManager.getSousCategorieByDesignation(f_sousCategorie.getText().toString());
                            if(sousCategorie!=null)
                            {
                                ArrayList<Produit> produits = dbManager.getGroupProduitBySousCategorie(sousCategorie.get_id());
                                ArrayAdapter<Produit> adapter3 = new ArrayAdapter<Produit>(f_nom.getContext(), android.R.layout.simple_dropdown_item_1line,produits);
                                f_nom.setAdapter(adapter3);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                }
            }
        });

        Intent myIntent = getIntent(); // gets the previously created intent
        if(myIntent.getExtras()!=null)
        {
            if(myIntent.getExtras().get("categorie")!=null)
            {
                int categorie = Integer.parseInt(myIntent.getExtras().get("categorie").toString());
                int sousCategorie = Integer.parseInt(myIntent.getExtras().get("sousCategorie").toString());
                int produit = Integer.parseInt(myIntent.getExtras().get("produit").toString());

                Categorie categorieObject = dbManager.getCategorieById(categorie+1);
                f_categorie.setText(categorieObject.get_designation());
                f_sousCategorie.setText(categorieObject.getSousCategories().get(sousCategorie).get_designation());
                f_nom.setText(categorieObject.getSousCategories().get(sousCategorie).getProduits().get(produit).get_designation());
                f_prix.requestFocus();

                FROM_GARAGE = true;
            }
            else
            {
                int idRechange = Integer.parseInt(myIntent.getExtras().get("id").toString());
                Rechange rechange = dbManager.getRechangeById(idRechange);
                f_date.setText(DateFormat.format("dd-MM-yyyy", rechange.get_date()));
                f_nom.setText(rechange.get_produit().get_designation());
                f_categorie.setText(dbManager.getCategorieById(dbManager.getSousCategorieById(rechange.get_produit().get_idSousCategorie()).get_idCategorie()).get_designation());
                f_sousCategorie.setText(dbManager.getSousCategorieById(rechange.get_produit().get_idSousCategorie()).get_designation());
                f_main.setText(rechange.get_main()+"");
                f_description.setText(rechange.get_description());
                String s = String.format(Locale.ROOT,"%.1f",rechange.get_prix());
                f_prix.setText(s);

                FROM_UPDATE = true;
                ID_UPDATE = idRechange;
            }

        }


        TextView textView = (TextView)findViewById(R.id.total);
        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_OPERATION);
        String s = String.format(Locale.ROOT,"%.1f",caisse.get_total());
        textView.setText(s);




    }
    @Override
    protected void onStart() {
        super.onStart();
        EditText champDate = (EditText)findViewById(R.id.f_dateRechange);
        champDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    DateDialog dialog = new DateDialog();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        champDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });
    }

    public void enregistrerRechange(View view){


        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        String cat = f_categorie.getText().toString();
        Categorie categorie = dbManager.getCategorieByDesignation(cat);
        if(categorie==null)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Categorie invalide !");
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
            String sousCat = f_sousCategorie.getText().toString();
            SousCategorie sousCategorie = dbManager.getSousCategorieByDesignationByCategorie(sousCat, categorie.get_id());
            if(sousCategorie == null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Sous Categorie invalide !");
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
                String prod = f_nom.getText().toString();
                Produit produit = dbManager.getProduitByDesignationBySousCategorie(prod, sousCategorie.get_id());
                if(produit==null)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Produit invalid !");
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
                    if(((EditText)findViewById(R.id.f_prixProduit)).getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Veuilliez saisir un prix !");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else if(((EditText)findViewById(R.id.f_dateRechange)).getText().toString().equals(""))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Date invalide !");
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
                        Rechange rechange = new Rechange();
                        rechange.set_produit(dbManager.getProduitById(produit.get_id()));
                        try {
                            rechange.set_date((new SimpleDateFormat("dd-MM-yyyy").parse(f_date.getText().toString())));
                        } catch (ParseException e) {
                            rechange.set_date(Calendar.getInstance().getTime());
                        }

                        try
                        {
                            rechange.set_prix(Float.parseFloat(f_prix.getText().toString()));
                            rechange.set_main(Float.parseFloat(f_main.getText().toString()));
                        }
                        catch (NumberFormatException e)
                        {
                            rechange.set_prix(-1);
                            rechange.set_main(-1);
                        }
                        rechange.set_idCaisse(Constantes.CAISSE_OPERATION);
                        rechange.set_description(f_description.getText().toString());
                        if(FROM_UPDATE)
                        {
                            rechange.set_id(ID_UPDATE);
                            dbManager.updateRechange(rechange);
                        }
                        else
                        {
                            dbManager.ajouterRechange(rechange);
                        }

                        dbManager.close();

                        Intent i = new Intent(Rechanger.this, Historique.class);
                        startActivity(i);



                    }

                }
            }

        }


    }

    public void afficher(View view) {
        if(FROM_GARAGE)
        {
            Intent i = new Intent(Rechanger.this, Garage.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(Rechanger.this, Historique.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {

        if(FROM_GARAGE)
        {
            Intent i = new Intent(Rechanger.this, Garage.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(Rechanger.this, Historique.class);
            startActivity(i);
        }


    }

    public void backToTheFuture(View view)
    {
        Intent intent = new Intent(Rechanger.this, Historique.class);
        startActivity(intent);
    }

}
