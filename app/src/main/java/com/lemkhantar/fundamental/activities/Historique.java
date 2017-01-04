package com.lemkhantar.fundamental.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.entity.Operation;
import com.lemkhantar.fundamental.entity.OperationAuto;
import com.lemkhantar.fundamental.entity.Rechange;
import com.lemkhantar.fundamental.entity.Repos;
import com.lemkhantar.fundamental.entity.TypeOperation;
import com.lemkhantar.fundamental.entity.manip;
import com.lemkhantar.fundamental.helper.Constantes;

import java.util.ArrayList;
import java.util.Locale;

public class Historique extends AppCompatActivity {


    TableLayout table_layout;

    TextView title;
    DBManager dbManager;
    Dialog dialog;
    Typeface font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_accumulateur) {
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

                                Intent intent = new Intent(Historique.this, Accumulateur.class);
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

                                Intent intent = new Intent(Historique.this, Garage.class);
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

                                Intent intent = new Intent(Historique.this, Reglages.class);
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

                                Intent intent = new Intent(Historique.this, Revenus.class);
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

        dbManager = new DBManager(this);
        dbManager.openDataBase();



        TextView textView = (TextView)findViewById(R.id.total);
        textView.setTypeface(font);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(font);
        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_OPERATION);
        String s = String.format(Locale.ROOT,"%.1f",caisse.get_total());
        textView.setText(s);



        table_layout = (TableLayout) findViewById(R.id.table);
        ArrayList<manip> lignes = dbManager.getHistorique(Constantes.CAISSE_OPERATION);
        int rows;
        if(lignes!=null)
        {
            rows = lignes.size();
        }
        else
        {
            rows = 0;
        }

        int cols = 3;
        table_layout.removeAllViews();
        BuildTable(rows, cols);








    }


    private void BuildTable(int rows, int cols) {


        TableRow headRow = new TableRow(this);
        headRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));



        TextView headNom = new TextView(this);
        headNom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
        headNom.setBackgroundResource(R.drawable.cell_shapeee);
        headNom.setPadding(0,20,0,20);
        headNom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headNom.setText("OPERATION");
        headNom.setTypeface(font);
        headRow.addView(headNom);

//        TextView headDate = new TextView(this);
//        headDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
//        headDate.setBackgroundResource(R.drawable.cell_shapeee);
//        headDate.setPadding(0,20,0,20);
//        headDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        headDate.setText("DATE");
//        headDate.setTypeface(font);
//        headRow.addView(headDate);

        TextView headPrix = new TextView(this);
        headPrix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
        headPrix.setBackgroundResource(R.drawable.cell_shapeee);
        headPrix.setPadding(0,20,0,20);
        headPrix.setText("PRIX /DHS");
        headPrix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headPrix.setTypeface(font);
        headRow.addView(headPrix);

        TextView headDetails = new TextView(this);
        headDetails.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
        headDetails.setBackgroundResource(R.drawable.cell_shapeee);
        headDetails.setPadding(0,20,0,20);
        headDetails.setText("PLUS");
        headDetails.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headDetails.setTypeface(font);
        headRow.addView(headDetails);

        table_layout.addView(headRow);


        dbManager.openDataBase();
        ArrayList<manip> lignes = dbManager.getHistorique(Constantes.CAISSE_OPERATION);
        dbManager.close();

        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            final Object object = lignes.get(i);
            if(object instanceof Rechange)
            {
                final Rechange rechange = (Rechange)object;
                TextView nom = new TextView(this);
                nom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                nom.setPadding(0,20,0,20);
                nom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nom.setTextColor(Color.BLACK);
                nom.setTypeface(font);
                nom.setText("Rechange de \n"+rechange.get_produit().get_designation());
                row.addView(nom);
//                TextView date = new TextView(this);
//                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
//                date.setPadding(0,20,0,20);
//                date.setTextColor(Color.BLACK);
//                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                date.setText(new SimpleDateFormat("  dd-MM-yyyy ").format(rechange.get_date()));
//                row.addView(date);
                TextView prix = new TextView(this);
                prix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                prix.setPadding(0,20,0,20);
                prix.setTextColor(Color.BLACK);
                prix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                prix.setTypeface(font);
                String s = String.format(Locale.ROOT,"%.1f",rechange.get_prix());
                prix.setText(s);
                row.addView(prix);
                Button detail = new Button(this);
                detail.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                detail.setText("+");
                detail.setTypeface(font);
                detail.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                detail.setTextColor(Color.BLACK);
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       new AlertDialog.Builder(v.getContext())
                                .setTitle(rechange.get_produit().get_designation())
                                .setMessage("Date du Rechange : \n\t\t\t"+ DateFormat.format("dd - MM - yyyy", rechange.get_date()).toString()+"\n"+"Main d'oeuvre : \n\t\t\t"+rechange.get_main()+" DHS\n"+"DESCRIPTION : \n\t\t\t"+rechange.get_description()+"\n")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNeutralButton("Modifier", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent myIntent = new Intent(Historique.this, Rechanger.class);
                                        myIntent.putExtra("id",rechange.get_id());
                                        startActivity(myIntent);
                                    }
                                })
                                .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbManager.openDataBase();
                                        dbManager.deleteRechange(rechange.get_id());
                                        finish();
                                        startActivity(getIntent());
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();

                    }
                });

                    nom.setBackgroundResource(R.drawable.cell_shape);
                    detail.setBackgroundResource(R.drawable.cell_shape);
                    prix.setBackgroundResource(R.drawable.cell_shape);
//                    date.setBackgroundResource(R.drawable.cell_shape);


                row.addView(detail);
            }
            else if(object instanceof Operation)
            {
                final Operation operation = (Operation)object;
                TextView nom = new TextView(this);
                nom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                nom.setPadding(0,20,0,20);
                nom.setTextColor(Color.BLACK);
                nom.setTypeface(font);
                nom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nom.setText(operation.get_type().toString());
                row.addView(nom);
//                TextView date = new TextView(this);
//                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
//                date.setPadding(0,20,0,20);
//                date.setTextColor(Color.BLACK);
//                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                date.setText(new SimpleDateFormat("  dd-MM-yyyy ").format(operation.get_date()));
//                row.addView(date);
                TextView prix = new TextView(this);
                prix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                prix.setPadding(0,20,0,20);
                prix.setTextColor(Color.BLACK);
                prix.setTypeface(font);
                prix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                String s = String.format(Locale.ROOT,"%.1f",operation.get_montant());
                prix.setText(s);
                row.addView(prix);
                Button detail = new Button(this);
                detail.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                detail.setText("+");
                detail.setTypeface(font);
                detail.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("DETAILS")
                                .setMessage("Date d'operation : \n\t\t\t"+DateFormat.format("dd - MM - yyyy", operation.get_date()).toString()+"\n"+"Description : \n\t\t\t"+operation.get_description())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNeutralButton("Modifier", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent myIntent = new Intent(Historique.this, Operer.class);
                                        myIntent.putExtra("id",operation.get_id());
                                        startActivity(myIntent);
                                    }
                                })
                                .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbManager.openDataBase();
                                        dbManager.deleteOperation(operation.get_id());
                                        finish();
                                        startActivity(getIntent());
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                });


                if(operation.get_type() == TypeOperation.DEBIT)
                {
                    nom.setBackgroundResource(R.drawable.cell_shape);
                    detail.setBackgroundResource(R.drawable.cell_shape);
                    prix.setBackgroundResource(R.drawable.cell_shape);
//                    date.setBackgroundResource(R.drawable.cell_shape);
                }
                else
                {
                    nom.setBackgroundResource(R.drawable.cell_shapee);
                    detail.setBackgroundResource(R.drawable.cell_shapee);
                    prix.setBackgroundResource(R.drawable.cell_shapee);
//                    date.setBackgroundResource(R.drawable.cell_shapee);
                }

                row.addView(detail);
            }
            else if(object instanceof Repos)
            {
                final Repos repos = (Repos) object;
                TextView nom = new TextView(this);
                nom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                nom.setPadding(0,20,0,20);
                nom.setTypeface(font);
                nom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nom.setTextColor(Color.BLACK);
                nom.setText("Repos");
                row.addView(nom);
//                TextView date = new TextView(this);
//                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
//                date.setPadding(0,20,0,20);
//                date.setTextColor(Color.BLACK);
//                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                date.setText(new SimpleDateFormat("  dd-MM-yyyy ").format(rechange.get_date()));
//                row.addView(date);
                TextView prix = new TextView(this);
                prix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                prix.setPadding(0,20,0,20);
                prix.setTypeface(font);
                prix.setTextColor(Color.BLACK);
                prix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                String s = String.format(Locale.ROOT,"%.1f", (repos.getDuree()*(repos.getPrixUnitaire()/60)));
                prix.setText(s);
                row.addView(prix);

                Button detail = new Button(this);
                detail.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .66f));
                detail.setText("+");
                detail.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                detail.setTypeface(font);
                detail.setTextColor(Color.BLACK);
                final String duree;
                if(getHeure(repos.getDuree())==0)
                {
                    duree = getRestMinute(repos.getDuree())+" Minutes";
                }
                else if(getRestMinute(repos.getDuree())==0)
                {
                    duree = getHeure(repos.getDuree())+" Heures";
                }
                else
                {
                    duree = getHeure(repos.getDuree())+" Heures "+getRestMinute(repos.getDuree())+" Minutes";
                }
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("DETAILS")
                                .setMessage("Date du repos : \n\t\t\t"+DateFormat.format("dd - MM - yyyy", repos.get_date()).toString()+"\n"+"Duree : \n\t\t\t"+duree+"\n"+"Description :\n\t\t\t"+repos.getDescription()+"\n")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNeutralButton("Modifier", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent myIntent = new Intent(Historique.this, Reposer.class);
                                        myIntent.putExtra("id",repos.getId());
                                        startActivity(myIntent);
                                    }
                                })
                                .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbManager.openDataBase();
                                        dbManager.deleteRepos(repos.getId());
                                        finish();
                                        startActivity(getIntent());
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                });

                nom.setBackgroundResource(R.drawable.cell_shape);
                detail.setBackgroundResource(R.drawable.cell_shape);
                prix.setBackgroundResource(R.drawable.cell_shape);
//                    date.setBackgroundResource(R.drawable.cell_shape);


                row.addView(detail);
            }



            table_layout.addView(row);

        }


    }

    public void ajouterOperationOuRechange(View view)
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.choixoperationrechange);
        dialog.setTitle("AJOUT D'OPERATION");
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void hide(View view)
    {
        dialog.hide();
    }

    public void pageajout(View view)
    {
        RadioButton rechange = (RadioButton)dialog.findViewById(R.id.radioButton);
        RadioButton creditdebit = (RadioButton)dialog.findViewById(R.id.radioButton2);

        if(rechange.isChecked())
        {
            Intent i = new Intent(Historique.this, Rechanger.class);
            startActivity(i);
        }
        else if(creditdebit.isChecked())
        {
            Intent i = new Intent(Historique.this, Operer.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(Historique.this, Reposer.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Historique.this, Accumulateur.class);
            startActivity(intent);
        }

    }

    public int getHeure(int minutes)
    {
        return minutes/60;
    }

    public int getRestMinute(int minutes)
    {
        return minutes%60;
    }


}
