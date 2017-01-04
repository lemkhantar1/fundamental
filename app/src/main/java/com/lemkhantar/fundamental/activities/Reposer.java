package com.lemkhantar.fundamental.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.entity.Repos;
import com.lemkhantar.fundamental.helper.Constantes;
import com.lemkhantar.fundamental.helper.DateDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reposer extends AppCompatActivity {

    EditText f_date, f_debut,f_fin, f_description;
    TextView c_date, c_debut, c_fin, c_description;
    Button saveButton, cancelButton;
    Typeface font;
    boolean FROM_UPDATE;
    int ID_UPDATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");

        f_date = (EditText)findViewById(R.id.f_dateRechange);
        f_debut = (EditText)findViewById(R.id.f_debut);
        f_fin = (EditText)findViewById(R.id.f_fin);
        f_description = (EditText)findViewById(R.id.f_description);

        c_date = (TextView)findViewById(R.id.c_date);
        c_debut = (TextView)findViewById(R.id.c_debut);
        c_fin = (TextView)findViewById(R.id.c_fin);
        c_description = (TextView)findViewById(R.id.c_description);

        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);



        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        TextView textView = (TextView)findViewById(R.id.total);

        f_date.setTypeface(font);
        f_debut.setTypeface(font);
        f_fin.setTypeface(font);
        c_date.setTypeface(font);
        c_debut.setTypeface(font);
        c_fin.setTypeface(font);
        c_description.setTypeface(font);
        textView.setTypeface(font);
        saveButton.setTypeface(font);
        cancelButton.setTypeface(font);


        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_OPERATION);
        textView.setText(caisse.get_total()+"");


        Intent myIntent = getIntent();
        if(myIntent.getExtras()!=null)
        {

            int idRepos = Integer.parseInt(myIntent.getExtras().get("id").toString());
            Repos repos = dbManager.getReposById(idRepos);

            f_date.setText(DateFormat.format("dd-MM-yyyy", repos.get_date()));
            f_description.setText(repos.getDescription());

            FROM_UPDATE = true;
            ID_UPDATE = idRepos;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        final EditText champDate = (EditText)findViewById(R.id.f_dateRechange);
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

        final EditText champDebut = (EditText)findViewById(R.id.f_debut);
        champDebut.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {

                    TimePickerDialog dialog1 = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            champDebut.setText(hourOfDay+":"+minute);
                        }
                    }, 0,0,true);

                    dialog1.show();


                }
            }
        });
        champDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog1 = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        champDebut.setText(hourOfDay+":"+minute);
                    }
                }, 0,0,true);

                dialog1.show();
            }
        });

        final EditText champFin = (EditText)findViewById(R.id.f_fin);
        champFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {

                    TimePickerDialog dialog1 = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            champFin.setText(hourOfDay+":"+minute);
                        }
                    }, 0,0,true);

                    dialog1.show();


                }
            }
        });
        champFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog1 = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        champFin.setText(hourOfDay+":"+minute);
                    }
                }, 0,0,true);

                dialog1.show();
            }
        });
    }

    public void afficher(View view) {
        Intent i = new Intent(Reposer.this, Historique.class);
        startActivity(i);
    }

    public void enregistrerOperation(View view) {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        if(f_date.getText().toString().equals(""))
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
        else if(f_debut.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("debut de repos invalide !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if(f_fin.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("debut de repos invalide !");
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
            Repos repos = new Repos();
            try {
                repos.setDate((new SimpleDateFormat("dd-MM-yyyy").parse(f_date.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            repos.setPrixUnitaire(50);
            repos.setIdCaisse(Constantes.CAISSE_OPERATION);


            String debut = f_debut.getText().toString();
            String fin = f_fin.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");

            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format.parse(debut);
                d2 = format.parse(fin);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();
                long diffMinutes = diff / (60 * 1000);
                repos.setDuree((int) diffMinutes);


            } catch (Exception e) {
                e.printStackTrace();
            }

            repos.setDescription(f_description.getText().toString());
            if(FROM_UPDATE)
            {
                repos.setId(ID_UPDATE);
                dbManager.updateRepos(repos);
            }
            else
            {
                dbManager.ajouterRepos(repos);
            }

            dbManager.close();


            Intent i = new Intent(Reposer.this, Historique.class);
            startActivity(i);
//            dbManager.ajouterOperation(operation);
//            dbManager.close();
//
//            Intent i = new Intent(Operer.this, Historique.class);
//            startActivity(i);
        }




    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Reposer.this, Historique.class);
        startActivity(i);

    }

    public void backToTheFuture(View view)
    {
        Intent intent = new Intent(Reposer.this, Historique.class);
        startActivity(intent);
    }
}
