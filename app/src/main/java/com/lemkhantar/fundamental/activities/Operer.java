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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.entity.Operation;
import com.lemkhantar.fundamental.entity.TypeOperation;
import com.lemkhantar.fundamental.helper.Constantes;
import com.lemkhantar.fundamental.helper.DateDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Operer extends AppCompatActivity {

    private final int ID_CATEGORIE = 1;
    private final int ID_SOUS_CATEGORIE = 1;
    private  final int ID_PRODUIT = 1;
    EditText f_date, f_prix,f_description;
    TextView c_montant, c_date, c_description;
    RadioButton crediter, debiter;
    Typeface font;
    Button saveButton, cancelButton;
    boolean FROM_UPDATE;
    int ID_UPDATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operer);

        font = Typeface.createFromAsset(getAssets(), "fonts/police.ttf");

        f_date = (EditText)findViewById(R.id.f_dateRechange);
        f_description = (EditText)findViewById(R.id.f_description);
        f_prix = (EditText)findViewById(R.id.f_prixProduit);
        crediter = (RadioButton) findViewById(R.id.crediter);
        debiter = (RadioButton) findViewById(R.id.debiter);

        c_montant = (TextView)findViewById(R.id.c_montant);
        c_date = (TextView)findViewById(R.id.c_date);
        c_description = (TextView)findViewById(R.id.c_description);

        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        TextView textView = (TextView)findViewById(R.id.total);
        TextView title = (TextView)findViewById(R.id.title);

        f_date.setTypeface(font);
        f_description.setTypeface(font);
        f_prix.setTypeface(font);
        crediter.setTypeface(font);
        debiter.setTypeface(font);
        c_montant.setTypeface(font);
        c_date.setTypeface(font);
        c_description.setTypeface(font);
        textView.setTypeface(font);
        title.setTypeface(font);


        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_OPERATION);
        String s = String.format(Locale.ROOT,"%.1f",caisse.get_total());
        textView.setText(s);


        Intent myIntent = getIntent();
        if(myIntent.getExtras()!=null)
        {

            int idOperation = Integer.parseInt(myIntent.getExtras().get("id").toString());
            Operation operation = dbManager.getOperationById(idOperation);

            Toast.makeText(this, operation.get_type().toString(), Toast.LENGTH_SHORT).show();
            if(operation.get_type() == TypeOperation.DEBIT)
            {
                debiter.setChecked(true);
            }
            else
            {
                crediter.setChecked(true);
            }


            s = String.format(Locale.ROOT,"%.1f",operation.get_montant());
            f_prix.setText(s);
            f_date.setText(DateFormat.format("dd-MM-yyyy", operation.get_date()));
            f_description.setText(operation.get_description());
            FROM_UPDATE = true;
            ID_UPDATE = idOperation;
            }

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

    public void enregistrerOperation(View view) {
        DBManager dbManager = new DBManager(this);
        dbManager.openDataBase();

        if(f_prix.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Veuillez saisir un prix !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if(f_date.getText().toString().equals(""))

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
        else if(f_description.getText().toString().equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Une description est obligatoire !");
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
            Operation operation = new Operation();
            operation.set_idCaisse(Constantes.CAISSE_OPERATION);
            if(crediter.isChecked())
            {
                operation.set_type(TypeOperation.CREDIT);
            }
            else
            {
                operation.set_type(TypeOperation.DEBIT);
            }
            try {
                operation.set_date((new SimpleDateFormat("dd-MM-yyyy").parse(f_date.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            operation.set_montant(Float.parseFloat(f_prix.getText().toString()));
            operation.set_description(f_description.getText().toString());
            if(FROM_UPDATE)
            {
                operation.set_id(ID_UPDATE);
                dbManager.updateOperation(operation);
            }
            else
            {
                dbManager.ajouterOperation(operation);
            }


            dbManager.close();

            Intent i = new Intent(Operer.this, Historique.class);
            startActivity(i);
        }




    }

    public void afficher(View view) {
        Intent i = new Intent(Operer.this, Historique.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Operer.this, Historique.class);
        startActivity(i);

    }

    public void backToTheFuture(View view)
    {
        Intent intent = new Intent(Operer.this, Historique.class);
        startActivity(intent);
    }



}
