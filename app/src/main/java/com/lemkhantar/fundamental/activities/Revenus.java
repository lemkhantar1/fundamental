package com.lemkhantar.fundamental.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.HistoRevenu;
import com.lemkhantar.fundamental.entity.Revenu;
import com.lemkhantar.fundamental.fragments.TabRevenuesMensuels;
import com.lemkhantar.fundamental.helper.Constantes;
import com.lemkhantar.fundamental.helper.PagerAdapterRevenu;

import java.util.ArrayList;

public class Revenus extends AppCompatActivity {

    Spinner spinner;
    ViewPager viewPager;
    PagerAdapterRevenu adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenues);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
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

                                Intent intent = new Intent(Revenus.this, Historique.class);
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

                                Intent intent = new Intent(Revenus.this, Garage.class);
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

                                Intent intent = new Intent(Revenus.this, Reglages.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                    }
                    else if(id == R.id.nav_accumulateur)
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

                                Intent intent = new Intent(Revenus.this, Accumulateur.class);
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


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Quotidiens"));
        tabLayout.addTab(tabLayout.newTab().setText("Mensuels"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapterRevenu(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setSelectedTabIndicatorColor(Color.BLACK);

//        TabLayout.Tab tab = tabLayout.getTabAt(1);
//        tab.select();
    }

    public void calculerSomme(View view) {
        spinner = ((TabRevenuesMensuels)adapter.instantiateItem(viewPager, viewPager.getCurrentItem())).getSpinner();
        int M = spinner.getSelectedItemPosition();
        if (M == 0) {
            Toast.makeText(this, "Mois invalide ! Veuillez choisir un mois", Toast.LENGTH_LONG).show();
        } else {
            int compteur = 0;
            final DBManager dbManager = new DBManager(this);
            dbManager.openDataBase();
            ArrayList<Revenu> revenus = dbManager.getAllRevenuOrderByDate(Constantes.CAISSE_PERSONNELLE);
            final ArrayList<HistoRevenu> histoRevenus = dbManager.getAllHistoRevenuOrderByDate();
            final ArrayList<Revenu> revenusMois = new ArrayList<>();
            for (int i = 0; i < revenus.size(); i++) {
                if (revenus.get(i).get_archive() == 0 && (revenus.get(i).get_date().getMonth() + 1) == M) {
                    compteur++;
                    revenusMois.add(revenus.get(i));
                }
            }
            if (compteur == 0) {
                Toast.makeText(this, "Aucun revenu ne correspond a ce mois", Toast.LENGTH_LONG).show();
            } else {
                float somme = 0;
                int kilometrage = 0;
                for (int i = 0; i < revenusMois.size(); i++) {
                    somme += revenusMois.get(i).get_montant();
                    kilometrage += revenusMois.get(i).get_kilometrage();
                }
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Calcul de somme !");
                final float finalSomme = somme;
                final int finalKilometrage = kilometrage;
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continuer",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HistoRevenu histoRevenu = new HistoRevenu();
                                int flag = 0;
                                for (int k = 0; k < histoRevenus.size(); k++) {
                                    if (revenusMois.get(0).get_date().getMonth() == histoRevenus.get(k).get_date().getMonth() && revenusMois.get(0).get_date().getYear() == histoRevenus.get(k).get_date().getYear()) {
                                        histoRevenu = histoRevenus.get(k);
                                        histoRevenu.set_total(histoRevenu.get_total() + finalSomme);
                                        histoRevenu.set_kilometrage(histoRevenu.get_kilometrage() + finalKilometrage);
                                        dbManager.updateHistoRevenu(histoRevenu);
                                        flag = 1;
                                        break;
                                    }
                                }

                                if (flag == 0) {
                                    histoRevenu.set_total(finalSomme);
                                    histoRevenu.set_kilometrage(finalKilometrage);
                                    histoRevenu.set_date(revenusMois.get(0).get_date());
                                    dbManager.ajouterHistoRevenu(histoRevenu);
                                }


                                // activer archive pour les revenus
                                for (int i = 0; i < revenusMois.size(); i++) {
                                    revenusMois.get(i).set_archive(1);
                                    dbManager.SubFromCaisse(revenusMois.get(i).get_montant(), Constantes.CAISSE_PERSONNELLE);
                                    dbManager.updateRevenu(revenusMois.get(i));

                                }

                                dbManager.calibrerCaissePersonnelle();
                                dialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setMessage("\t* " + compteur + " revenus\n\t* " + somme + " DHS\n\t* " + kilometrage + " KMS");
                alertDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Revenus.this, Accumulateur.class);
            startActivity(intent);
        }

    }


}
