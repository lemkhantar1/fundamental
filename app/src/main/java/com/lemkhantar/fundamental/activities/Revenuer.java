package com.lemkhantar.fundamental.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.entity.Revenu;
import com.lemkhantar.fundamental.helper.Constantes;
import com.lemkhantar.fundamental.helper.DateDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Revenuer extends AppCompatActivity {

    EditText f_montant, f_kilometrage,f_kilometrage2, f_coefficient, f_dateRechange, f_traite;
    private  boolean FROM_ACCUMULATEUR;
    private  boolean FROM_ACCUMULATEUR_WITH_KILO;
    Typeface font;
    TextView c_montant, c_kilometrage,c_kilometrage2, c_coeff, c_date, title, total, c_traite;
    Button saveButton, cancelButton;
    boolean FROM_UPDATE;
    int ID_UPDATE, DEBUT_KILO, FIN_KILO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenuer);

        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");

       FROM_ACCUMULATEUR = false;
        FROM_ACCUMULATEUR_WITH_KILO = false;

        f_dateRechange = (EditText)findViewById(R.id.f_dateRechange);
        f_montant = (EditText)findViewById(R.id.f_prixProduit);
        f_kilometrage = (EditText)findViewById(R.id.f_kilometrage);
        f_kilometrage2 = (EditText)findViewById(R.id.f_kilometrage2);
        f_coefficient = (EditText)findViewById(R.id.f_coefficient);
        f_traite = (EditText)findViewById(R.id.f_traite);

        c_coeff = (TextView) findViewById(R.id.c_coefficient);
        c_kilometrage = (TextView) findViewById(R.id.c_kilometrage);
        c_kilometrage2 = (TextView) findViewById(R.id.c_kilometrage2);
        c_montant = (TextView) findViewById(R.id.c_montant);
        c_date = (TextView) findViewById(R.id.c_date);
        c_traite = (TextView) findViewById(R.id.c_traite);
        title = (TextView) findViewById(R.id.title);
        total = (TextView) findViewById(R.id.total);

        saveButton = (Button)findViewById(R.id.saveButton) ;
        cancelButton = (Button)findViewById(R.id.cancelButton) ;

        f_dateRechange.setTypeface(font);f_montant.setTypeface(font);f_kilometrage.setTypeface(font);f_kilometrage2.setTypeface(font);f_coefficient.setTypeface(font);f_traite.setTypeface(font);
        c_coeff.setTypeface(font);c_kilometrage.setTypeface(font);c_kilometrage2.setTypeface(font);c_montant.setTypeface(font);c_date.setTypeface(font);c_traite.setTypeface(font);
        title.setTypeface(font);total.setTypeface(font);
        saveButton.setTypeface(font);cancelButton.setTypeface(font);

        f_coefficient.setText("0.5");
        f_traite.setText("210");

        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        Intent myIntent = getIntent(); // gets the previously created intent
        if(myIntent.getExtras()!=null)
        {
            if(myIntent.getExtras().get("total")!=null)
            {
                FROM_ACCUMULATEUR = true;
                float total = Float.parseFloat(myIntent.getExtras().get("total").toString());
                if(myIntent.getExtras().get("debut")!=null) {
                    DEBUT_KILO = Integer.parseInt(myIntent.getExtras().get("debut").toString());
                    FIN_KILO = Integer.parseInt(myIntent.getExtras().get("fin").toString());
                    f_kilometrage.setText((DEBUT_KILO)+"");
                    f_kilometrage2.setText((FIN_KILO)+"");
                    FROM_ACCUMULATEUR_WITH_KILO = true;
                }
                f_montant.setText(total+"");
                saveButton.requestFocus();


            }
            else
            {
                int idRevenu = Integer.parseInt(myIntent.getExtras().get("id").toString());
                Revenu revenu = dbManager.getRevenuById(idRevenu);

                f_dateRechange.setText(DateFormat.format("dd-MM-yyyy", revenu.get_date()));
                f_coefficient.setText(revenu.get_coefficient()+"");
                f_montant.setText(""+(revenu.get_montant()+(revenu.get_kilometrage()*revenu.get_coefficient()+revenu.get_traite())));
                f_kilometrage.setText(revenu.get_km_debut()+"");
                f_kilometrage2.setText(revenu.get_km_fin()+"");
                f_traite.setText(revenu.get_traite()+"");


                FROM_UPDATE = true;
                ID_UPDATE = idRevenu;
            }

        }

        TextView textView = (TextView)findViewById(R.id.total);
        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_PERSONNELLE);
        String s = String.format(Locale.ROOT,"%.1f",caisse.get_total());
        textView.setText(s);

    }


    @Override
    protected void onStart() {
        super.onStart();
        final EditText champDate = (EditText) findViewById(R.id.f_dateRechange);
        champDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
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



    public void enregistrerRevenu(View view) {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        if(f_montant.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Montant invalide !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if(f_kilometrage.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Kilometrage de debut invalide !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if(f_kilometrage2.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Kilometrage de fin invalide !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if(f_coefficient.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Coefficient invalide !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if(f_dateRechange.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("date obligatoire !");
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
            Revenu revenu = new Revenu();
            try {
                revenu.set_date((new SimpleDateFormat("dd-MM-yyyy").parse(f_dateRechange.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            revenu.set_coefficient(Float.parseFloat(f_coefficient.getText().toString()));
            revenu.set_traite(Float.parseFloat(f_traite.getText().toString()));
            revenu.set_km_debut(Integer.parseInt(f_kilometrage.getText().toString()));
            revenu.set_km_fin(Integer.parseInt(f_kilometrage2.getText().toString()));
            revenu.set_kilometrage(revenu.get_km_fin()-revenu.get_km_debut());

            float unite = Float.parseFloat(f_coefficient.getText().toString());
            int kilometrage = revenu.get_kilometrage();
            float montant = Float.parseFloat(f_montant.getText().toString());
            float traite = Float.parseFloat(f_traite.getText().toString());

            revenu.set_montant(montant-(unite*kilometrage+traite));
            revenu.set_idCaisse(Constantes.CAISSE_PERSONNELLE);

            if(FROM_UPDATE)
            {
                revenu.set_id(ID_UPDATE);
                dbManager.updateRevenu(revenu);
            }
            else
            {
                dbManager.ajouterRevenu(revenu);
            }
            dbManager.close();


            Intent i = new Intent(Revenuer.this, Revenus.class);
            startActivity(i);
//            dbManager.ajouterOperation(operation);
//            dbManager.close();
//
//            Intent i = new Intent(Operer.this, Historique.class);
//            startActivity(i);
        }




    }

    public void afficher(View view) {
        if(FROM_ACCUMULATEUR)
        {
            DBManager dbManager = new DBManager(this);
            dbManager.openDataBase();
            dbManager.undoCaisse(Constantes.CAISSE_TEMPORAIRE);
            Intent i = new Intent(Revenuer.this, Accumulateur.class);
            if(FROM_ACCUMULATEUR_WITH_KILO)
            {
                i.putExtra("debut",DEBUT_KILO);
                i.putExtra("fin",FIN_KILO);
            }
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(Revenuer.this, Revenus.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {

        if(FROM_ACCUMULATEUR)
        {
            DBManager dbManager = new DBManager(this);
            dbManager.openDataBase();
            dbManager.undoCaisse(Constantes.CAISSE_TEMPORAIRE);
            Intent i = new Intent(Revenuer.this, Accumulateur.class);
            if(FROM_ACCUMULATEUR_WITH_KILO)
            {
                i.putExtra("debut",DEBUT_KILO);
                i.putExtra("fin",FIN_KILO);
            }
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(Revenuer.this, Revenus.class);
            startActivity(i);
        }


    }

    public void backToTheFuture(View view)
    {
        Intent intent = new Intent(Revenuer.this, Historique.class);
        startActivity(intent);
    }
}
