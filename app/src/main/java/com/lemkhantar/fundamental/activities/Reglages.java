package com.lemkhantar.fundamental.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.OperationAuto;
import com.lemkhantar.fundamental.helper.Constantes;

public class Reglages extends AppCompatActivity {

    Typeface font;
    EditText montantPro, unite, traite;
    Switch operationAuto;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglages);

        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");

        montantPro = (EditText)findViewById(R.id.montantPro);
        operationAuto = (Switch) findViewById(R.id.switch1);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(4).setChecked(true);
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

                                Intent intent = new Intent(Reglages.this, Accumulateur.class);
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

                                Intent intent = new Intent(Reglages.this, Garage.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                    }
                    else if(id == R.id.nav_caisse)
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

                                Intent intent = new Intent(Reglages.this, Historique.class);
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

                                Intent intent = new Intent(Reglages.this, Revenus.class);
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





        dbManager = new DBManager(getApplicationContext());
        dbManager.openDataBase();

        OperationAuto operationAuto2 = dbManager.getOperationAuto(Constantes.OPERATIO_AUTO_1);
        montantPro.setText(operationAuto2.get_montant()+"");

        if(operationAuto2.get_active()==1)
        {
            operationAuto.setChecked(true);
            montantPro.setEnabled(false);
        }
        else
        {
            operationAuto.setChecked(false);
            montantPro.setEnabled(true);
        }

        operationAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    try
                    {
                        dbManager = new DBManager(getApplicationContext());
                        dbManager.openDataBase();
                        float montant = Float.parseFloat(montantPro.getText().toString());
                        dbManager.updateOperationAuto(Constantes.OPERATIO_AUTO_1, montant);
                        dbManager.activerOperationAuto(Constantes.OPERATIO_AUTO_1);
                        dbManager.close();
                        Toast.makeText(getApplicationContext(), "Operation programmée activé", Toast.LENGTH_SHORT).show();
                        montantPro.setEnabled(false);

                    }
                    catch (NumberFormatException e)
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(buttonView.getContext()).create();
                        alertDialog.setTitle("montant invalide !");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        operationAuto.setChecked(false);
                    }

                }
                else
                {
                    dbManager = new DBManager(getApplicationContext());
                    dbManager.openDataBase();
                    dbManager.desactiverOperationAuto(Constantes.OPERATIO_AUTO_1);
                    dbManager.close();
                    Toast.makeText(getApplicationContext(), "Operation programmée desactivé", Toast.LENGTH_SHORT).show();
                    montantPro.setEnabled(true);

                }
            }
        });








    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Reglages.this, Accumulateur.class);
            startActivity(intent);
        }

    }

    public void enregistrer(View view)
    {

    }

    public void annuler(View view)
    {
        Intent intent = new Intent(Reglages.this, Accumulateur.class);
        startActivity(intent);
    }
}
