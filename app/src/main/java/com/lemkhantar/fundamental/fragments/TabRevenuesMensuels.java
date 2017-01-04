package com.lemkhantar.fundamental.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.HistoRevenu;
import com.lemkhantar.fundamental.entity.Revenu;
import com.lemkhantar.fundamental.helper.Constantes;

import java.util.ArrayList;
import java.util.Locale;


public class TabRevenuesMensuels extends Fragment {

    DBManager dbManager;
    TableLayout table_layout;
    Typeface font;
    Spinner spinner;
    Activity activity;
    private static final String[] mois = {"--Mois--", "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Decembre"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_revenues_mensuels, container, false);
        activity = getActivity();

        font = Typeface.createFromAsset(getContext().getAssets(), "fonts/police.ttf");

        dbManager = new DBManager(activity);
        dbManager.openDataBase();


        table_layout = (TableLayout) v.findViewById(R.id.table);
        ArrayList<HistoRevenu> lignes = dbManager.getAllHistoRevenuOrderByDate();
        int rows;
        if (lignes != null) {
            rows = lignes.size();
        } else {
            rows = 0;
        }

        int cols = 2;
        table_layout.removeAllViews();
        BuildTable(rows, cols);
        dbManager.close();

        spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, mois);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return v;
    }

    private void BuildTable(int rows, int cols) {


        TableRow headRow = new TableRow(activity);
        headRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));


        TextView headNom = new TextView(activity);
        headNom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headNom.setBackgroundResource(R.drawable.cell_shapeee);
        headNom.setPadding(0, 20, 0, 20);
        headNom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headNom.setText("MONTANT /DHS");
        headNom.setTypeface(font);
        headRow.addView(headNom);

//        TextView headDate = new TextView(this);
//        headDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
//        headDate.setBackgroundResource(R.drawable.cell_shapeee);
//        headDate.setPadding(0,20,0,20);
//        headDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        headDate.setText("DATE");
//        headDate.setTypeface(font);
//        headRow.addView(headDate);

        TextView headPrix = new TextView(activity);
        headPrix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headPrix.setBackgroundResource(R.drawable.cell_shapeee);
        headPrix.setPadding(0, 20, 0, 20);
        headPrix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headPrix.setText("DATE");
        headPrix.setTypeface(font);
        headRow.addView(headPrix);

        TextView headDetails = new TextView(activity);
        headDetails.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headDetails.setBackgroundResource(R.drawable.cell_shapeee);
        headDetails.setPadding(0, 20, 0, 20);
        headDetails.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headDetails.setText("KMS");
        headDetails.setTypeface(font);
        headRow.addView(headDetails);

        TextView headPlus = new TextView(activity);
        headPlus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headPlus.setBackgroundResource(R.drawable.cell_shapeee);
        headPlus.setPadding(0, 20, 0, 20);
        headPlus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headPlus.setText("PLUS");
        headPlus.setTypeface(font);
        headRow.addView(headPlus);

        table_layout.addView(headRow);


        dbManager.openDataBase();
        ArrayList<HistoRevenu> lignes = dbManager.getAllHistoRevenuOrderByDate();
        dbManager.close();

        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(activity);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            final HistoRevenu histoRevenu = lignes.get(i);

            TextView nom = new TextView(activity);
            nom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
            nom.setPadding(0, 20, 0, 20);
            nom.setTextColor(Color.BLACK);
            nom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            nom.setText("" + histoRevenu.get_total());
            row.addView(nom);

            TextView prix = new TextView(activity);
            prix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            prix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
            prix.setPadding(0, 20, 0, 20);
            prix.setTextColor(Color.BLACK);
            prix.setText(DateFormat.format("MMM-yyyy", histoRevenu.get_date()).toString());

            row.addView(prix);

            TextView date = new TextView(activity);
            date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
            date.setPadding(0, 20, 0, 20);
            date.setTextColor(Color.BLACK);
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            date.setText("" + histoRevenu.get_kilometrage());
            row.addView(date);


            Button detail = new Button(activity);
            detail.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
            detail.setText("+plus");
            detail.setTextColor(Color.BLACK);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    afficherRevenus(histoRevenu.get_id());

                }
            });

            nom.setBackgroundResource(R.drawable.cell_shapee);
            detail.setBackgroundResource(R.drawable.cell_shapee);
            prix.setBackgroundResource(R.drawable.cell_shapee);
            date.setBackgroundResource(R.drawable.cell_shapee);


            row.addView(detail);


            table_layout.addView(row);

        }


    }

    public void afficherRevenus(int idHistoRevenus) {
        Dialog dialog;
        dialog = new Dialog(activity);

        dialog.setContentView(R.layout.listerevenusarchive);
        TableLayout table_layout2;
        table_layout2 = (TableLayout) dialog.findViewById(R.id.tableLayout);

        dbManager = new DBManager(activity);
        dbManager.openDataBase();


        TextView moyeffe = (TextView) dialog.findViewById(R.id.moyeffe);
        TextView moymens = (TextView) dialog.findViewById(R.id.moymens);
        moyeffe.setTypeface(font);
        moyeffe.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        moymens.setTypeface(font);
        moymens.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);




        final HistoRevenu histoRevenu = dbManager.getHistoRevenuById(idHistoRevenus);

        Button desarchiver = (Button) dialog.findViewById(R.id.desarchiver);
        desarchiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager dbManager1 = new DBManager(activity);
                dbManager1.openDataBase();
                dbManager1.desarchiver(histoRevenu.get_id());
                dbManager1.calibrerCaissePersonnelle();
                activity.finish();
                startActivity(activity.getIntent());
            }
        });


        TableRow headRow = new TableRow(activity);
        headRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));


        TextView headPrix = new TextView(activity);
        headPrix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headPrix.setBackgroundResource(R.drawable.cell_shapeee);
        headPrix.setPadding(0, 20, 0, 20);
        headPrix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headPrix.setText("DATE");
        headPrix.setTypeface(font);
        headRow.addView(headPrix);


        TextView headNom = new TextView(activity);
        headNom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headNom.setBackgroundResource(R.drawable.cell_shapeee);
        headNom.setPadding(0, 20, 0, 20);
        headNom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headNom.setText("NET /DHS");
        headNom.setTypeface(font);
        headRow.addView(headNom);

        TextView headDate = new TextView(activity);
        headDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headDate.setBackgroundResource(R.drawable.cell_shapeee);
        headDate.setPadding(0, 20, 0, 20);
        headDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headDate.setText("KM");
        headDate.setTypeface(font);
        headRow.addView(headDate);


        table_layout2.addView(headRow);


        dbManager.openDataBase();
        ArrayList<Revenu> lignes = dbManager.getAllRevenuOrderByDate(Constantes.CAISSE_PERSONNELLE);
        dbManager.close();
        int cmp = 0;

        for (int i = 0; i < lignes.size(); i++) {

            TableRow row = new TableRow(activity);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            final Revenu revenu = lignes.get(i);

            if (revenu.get_archive() == 1 && revenu.get_date().getYear() == histoRevenu.get_date().getYear() && revenu.get_date().getMonth() == histoRevenu.get_date().getMonth()) {
                cmp++;
                TextView prix = new TextView(activity);
                prix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                prix.setTypeface(font);
                prix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
                prix.setPadding(0, 20, 0, 20);
                prix.setTextColor(Color.BLACK);
                prix.setText(DateFormat.format("MM-dd", revenu.get_date()).toString());
                row.addView(prix);


                TextView nom = new TextView(activity);
                nom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
                nom.setPadding(0, 20, 0, 20);
                nom.setTextColor(Color.BLACK);
                nom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nom.setTypeface(font);
                String s = String.format(Locale.ROOT, "%.1f", revenu.get_montant());
                nom.setText(s);
                row.addView(nom);


                TextView date = new TextView(activity);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
                date.setPadding(0, 20, 0, 20);
                date.setTextColor(Color.BLACK);
                date.setTypeface(font);
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                date.setText(revenu.get_kilometrage() + "");
                row.addView(date);


                if (revenu.get_montant() > 0) {
                    nom.setBackgroundResource(R.drawable.cell_shapee);
                    prix.setBackgroundResource(R.drawable.cell_shapee);
                    date.setBackgroundResource(R.drawable.cell_shapee);
                } else {
                    nom.setBackgroundResource(R.drawable.cell_shape);
                    prix.setBackgroundResource(R.drawable.cell_shape);
                    date.setBackgroundResource(R.drawable.cell_shape);
                }


                table_layout2.addView(row);
            }


        }

        moyeffe.setText("Moyenne effective :\n " + String.format(Locale.ROOT, "%.1f", histoRevenu.get_total() / cmp));
        if (histoRevenu.get_date().getMonth() == 0 || histoRevenu.get_date().getMonth() == 2 || histoRevenu.get_date().getMonth() == 4 || histoRevenu.get_date().getMonth() == 6 || histoRevenu.get_date().getMonth() == 7 || histoRevenu.get_date().getMonth() == 9 || histoRevenu.get_date().getMonth() == 11)
            moymens.setText("Moyenne Mensuelle :\n " + String.format(Locale.ROOT, "%.1f", histoRevenu.get_total() / 31));
        else if (histoRevenu.get_date().getMonth() == 3 || histoRevenu.get_date().getMonth() == 5 || histoRevenu.get_date().getMonth() == 8 || histoRevenu.get_date().getMonth() == 10)
            moymens.setText("Moyenne Mensuelle :\n " + String.format(Locale.ROOT, "%.1f", histoRevenu.get_total() / 30));
        else
            moymens.setText("Moyenne Mensuelle :\n " + String.format(Locale.ROOT, "%.1f", histoRevenu.get_total() / 28));


        dialog.show();
    }

    public Spinner getSpinner()
    {
        return spinner;
    }


}
