package com.lemkhantar.fundamental.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Categorie;
import com.lemkhantar.fundamental.helper.ParentLevelAdapter;

import java.util.ArrayList;
import java.util.List;

public class Garage extends AppCompatActivity {

    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
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

                                Intent intent = new Intent(Garage.this, Historique.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                    } else if (id == R.id.nav_accumulateur) {
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

                                Intent intent = new Intent(Garage.this, Accumulateur.class);
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

                                Intent intent = new Intent(Garage.this, Reglages.class);
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

                                Intent intent = new Intent(Garage.this, Revenus.class);
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
        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");
        ((TextView)findViewById(R.id.titre)).setTypeface(font);

        List<String> listDataHeader = new ArrayList<>();
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        final ArrayList<Categorie> categories = dbManager.getAllCategorie();
        final ExpandableListView mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView_Parent);

        if (mExpandableListView != null)
        {
            ParentLevelAdapter parentLevelAdapter = new ParentLevelAdapter(this, listDataHeader, categories);
            mExpandableListView.setAdapter(parentLevelAdapter);
            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
            {
                int previousGroup = -1;
                @Override
                public void onGroupExpand(int groupPosition)
                {
                    if (groupPosition != previousGroup)
                        mExpandableListView.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Garage.this, Accumulateur.class);
            startActivity(intent);
        }

    }


    public void addPiece(View view)
    {
        Intent i = new Intent(Garage.this, AjoutPiece.class);
        startActivity(i);
    }


}
