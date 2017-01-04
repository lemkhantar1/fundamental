package com.lemkhantar.fundamental.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.helper.Constantes;

import java.util.Locale;

public class Accumulateur extends AppCompatActivity {

    Typeface font;
    Button ajouter, annuler, reinitialiser, valider;
    TextView total, c_debut, c_fin;
    EditText montant, f_debut, f_fin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accumulateur);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_caisse) {
                        drawer.closeDrawer(GravityCompat.START);
                        Thread thread=  new Thread(){
                            @Override
                            public void run(){
                                try {
                                    synchronized(this){
                                        wait(210);
                                    }
                                }
                                catch(InterruptedException ex){
                                }

                                Intent intent = new Intent(Accumulateur.this, Historique.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                    } else if (id == R.id.nav_gerage) {
                        drawer.closeDrawer(GravityCompat.START);
                        Thread thread=  new Thread(){
                            @Override
                            public void run(){
                                try {
                                    synchronized(this){
                                        wait(210);
                                    }
                                }
                                catch(InterruptedException ex){
                                }

                                Intent intent = new Intent(Accumulateur.this, Garage.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                    }
                    else if(id == R.id.nav_reglage)
                    {
                        drawer.closeDrawer(GravityCompat.START);
                        Thread thread=  new Thread(){
                            @Override
                            public void run(){
                                try {
                                    synchronized(this){
                                        wait(210);
                                    }
                                }
                                catch(InterruptedException ex){
                                }

                                Intent intent = new Intent(Accumulateur.this, Reglages.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                    }
                    else if(id == R.id.nav_revenus)
                    {
                        drawer.closeDrawer(GravityCompat.START);
                        Thread thread=  new Thread(){
                            @Override
                            public void run(){
                                try {
                                    synchronized(this){
                                        wait(210);
                                    }
                                }
                                catch(InterruptedException ex){
                                }

                                Intent intent = new Intent(Accumulateur.this, Revenus.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                    }


                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }


        updateTotal();
        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");
        ajouter = (Button)findViewById(R.id.ajouter);
        ajouter.setTypeface(font);
        valider = (Button)findViewById(R.id.valider);
        valider.setTypeface(font);
        annuler = (Button)findViewById(R.id.annuler);
        annuler.setTypeface(font);
        reinitialiser = (Button)findViewById(R.id.reinitialiser);
        reinitialiser.setTypeface(font);
        total = (TextView)findViewById(R.id.total);
        total.setTypeface(font);
        c_debut = (TextView)findViewById(R.id.c_debut);
        c_debut.setTypeface(font);
        c_fin = (TextView)findViewById(R.id.c_fin);
        c_fin.setTypeface(font);



        montant = (EditText)findViewById(R.id.montant);
        montant.setTypeface(font);
        f_debut = (EditText)findViewById(R.id.f_debut);
        f_debut.setTypeface(font);
        f_fin = (EditText)findViewById(R.id.f_fin);
        f_fin.setTypeface(font);


        reinitialiser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                reinitialiser(v);
                return true;
            }
        });

        Intent myIntent = getIntent();
        if(myIntent.getExtras()!=null)
        {
            f_debut.setText(myIntent.getExtras().get("debut").toString());
            f_fin.setText(myIntent.getExtras().get("fin").toString());
        }
        else
        {
            DBManager dbManager = new DBManager(this);
            dbManager.openDataBase();
            Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_TEMPORAIRE);
            if(caisse.get_plus()!=0)
                f_debut.setText(caisse.get_plus()+"");
            else
                f_debut.setText("");
        }

        montant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if(f_debut.getText().toString().equals(""))
                    {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Alerte")
                                .setMessage("saisissez le kilometrage de depart ")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                        f_debut.requestFocus();
                    }
                }
            }
        });

        f_debut.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    DBManager dbManager = new DBManager(v.getContext());
                    dbManager.openDataBase();
                    Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_TEMPORAIRE);
                    if(!f_debut.getText().toString().equals(""))
                    {
                        caisse.set_plus(Integer.parseInt(f_debut.getText().toString()));
                        dbManager.updateCaisse(caisse);

                    }
                    else
                    {
                        caisse.set_plus(0);
                        dbManager.updateCaisse(caisse);
                    }
                }
            }
        });







    }
    @Override
    protected void onPause() {
        super.onPause();
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();
        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_TEMPORAIRE);
        if(!f_debut.getText().toString().equals(""))
        {
            caisse.set_plus(Integer.parseInt(f_debut.getText().toString()));
            dbManager.updateCaisse(caisse);

        }
        else
        {
            caisse.set_plus(0);
            dbManager.updateCaisse(caisse);
        }
    }
    public void message(View view) {
        Toast.makeText(this, "Appuiyez longtemps pour reinitialiser", Toast.LENGTH_LONG).show();
    }
    public void updateTotal() {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();
        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_TEMPORAIRE);
        TextView total = (TextView)findViewById(R.id.total);
        String s = String.format(Locale.ROOT,"%.1f",caisse.get_total());
        total.setText(s+" Dhs");

        dbManager.close();

    }
    public void reinitialiser(View view) {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();
        dbManager.reinitialiserCaisse(Constantes.CAISSE_TEMPORAIRE);
        dbManager.close();
        updateTotal();
    }
    public void ajouter(View view) {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();
        EditText montant = (EditText)findViewById(R.id.montant);
        try{
            dbManager.AddtoCaisse(Float.parseFloat(montant.getText().toString()),Constantes.CAISSE_TEMPORAIRE);
        }catch (NumberFormatException e)
        {
            dbManager.AddtoCaisse(0, Constantes.CAISSE_TEMPORAIRE);
        }

        montant.setText("");


        dbManager.close();
        updateTotal();
    }
    public void annuler(View view) {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();
        dbManager.undoCaisse(Constantes.CAISSE_TEMPORAIRE);
        dbManager.close();
        updateTotal();
    }
    public void valider(final View view) {

        if(f_debut.getText().toString().equals("") || f_fin.getText().toString().equals(""))
        {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Alerte")
                    .setMessage("Kilometrage invalide !")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
        else
        {
            int debut = Integer.parseInt(f_debut.getText().toString());
            int fin = Integer.parseInt(f_fin.getText().toString());
            int kilo = fin - debut;
            if(kilo<=0)
            {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Alerte")
                        .setMessage("Kilometrage invalide ! ")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
            else
            {
                DBManager dbManager = new DBManager(this);
                dbManager.openDataBase();
                Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_TEMPORAIRE);
                float total = caisse.get_total();
                reinitialiser(view);
                Intent myIntent = new Intent(Accumulateur.this, Revenuer.class);
                myIntent.putExtra("total",total);
                myIntent.putExtra("debut",debut);
                myIntent.putExtra("fin",fin);
                startActivity(myIntent);
            }

        }

    }
    boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed() {


        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();
        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_TEMPORAIRE);
        if(!f_debut.getText().toString().equals(""))
        {
            caisse.set_plus(Integer.parseInt(f_debut.getText().toString()));
            dbManager.updateCaisse(caisse);
        }
        else
        {
            caisse.set_plus(0);
            dbManager.updateCaisse(caisse);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Cliquez une deuxieme fois pour quitter", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }



    }



}
