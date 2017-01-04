package com.lemkhantar.fundamental.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.activities.Revenuer;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Caisse;
import com.lemkhantar.fundamental.entity.Revenu;
import com.lemkhantar.fundamental.helper.Constantes;

import java.util.ArrayList;
import java.util.Locale;


public class TabRevenuesQuotidiens extends Fragment {

    DBManager dbManager;
    TableLayout table_layout;
    Typeface font;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_revenues_quotidiens, container, false);
        font = Typeface.createFromAsset(getContext().getAssets(), "fonts/police.ttf");
        activity = getActivity();


        dbManager = new DBManager(activity.getApplicationContext());
        dbManager.openDataBase();

        TextView textView = (TextView)v.findViewById(R.id.total);
        TextView moyenne = (TextView)v.findViewById(R.id.moyenne);


        textView.setTypeface(font);
        moyenne.setTypeface(font);

        Caisse caisse = dbManager.getCaisse(Constantes.CAISSE_PERSONNELLE);
        String s = String.format(Locale.ROOT,"%.1f",caisse.get_total());
        textView.setText(s);

        int nbRevenus = dbManager.getNbRevenusNonArchives();
        float moy;
        if(nbRevenus!=0)
            moy = caisse.get_total()/dbManager.getNbRevenusNonArchives();
        else moy = 0;
        s = String.format(Locale.ROOT,"%.1f",moy);
        moyenne.setText(s);



        table_layout = (TableLayout) v.findViewById(R.id.table);
        ArrayList<Revenu> lignes = dbManager.getAllRevenuOrderByDate(Constantes.CAISSE_PERSONNELLE);
        int rows;
        if(lignes!=null)
        {
            rows = lignes.size();
        }
        else
        {
            rows = 0;
        }

        int cols = 4;
        table_layout.removeAllViews();
        BuildTable(rows, cols);
        dbManager.close();
        return v;

    }

    private void BuildTable(int rows, int cols) {


        TableRow headRow = new TableRow(this.getContext());
        headRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));



        TextView headPrix = new TextView(this.getContext());
        headPrix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headPrix.setBackgroundResource(R.drawable.cell_shapeee);
        headPrix.setPadding(0,20,0,20);
        headPrix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headPrix.setText("DATE");
        headPrix.setTypeface(font);
        headRow.addView(headPrix);




        TextView headNom = new TextView(this.getContext());
        headNom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headNom.setBackgroundResource(R.drawable.cell_shapeee);
        headNom.setPadding(0,20,0,20);
        headNom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headNom.setText("NET /DHS");
        headNom.setTypeface(font);
        headRow.addView(headNom);

        TextView headDate = new TextView(this.getContext());
        headDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headDate.setBackgroundResource(R.drawable.cell_shapeee);
        headDate.setPadding(0,20,0,20);
        headDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headDate.setText("KM");
        headDate.setTypeface(font);
        headRow.addView(headDate);



        TextView headDetails = new TextView(this.getContext());
        headDetails.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
        headDetails.setBackgroundResource(R.drawable.cell_shapeee);
        headDetails.setPadding(0,20,0,20);
        headDetails.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        headDetails.setText("PLUS");
        headDetails.setTypeface(font);
        headRow.addView(headDetails);

        table_layout.addView(headRow);


        dbManager.openDataBase();
        ArrayList<Revenu> lignes = dbManager.getAllRevenuOrderByDate(Constantes.CAISSE_PERSONNELLE);
        dbManager.close();

        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            final Revenu revenu = lignes.get(i);

            if(revenu.get_archive()==0)
            {
                TextView prix = new TextView(this.getContext());
                prix.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                prix.setTypeface(font);
                prix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
                prix.setPadding(0,20,0,20);
                prix.setTextColor(Color.BLACK);
                prix.setText(DateFormat.format("MM-dd", revenu.get_date()).toString());
                row.addView(prix);



                TextView nom = new TextView(this.getContext());
                nom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
                nom.setPadding(0,20,0,20);
                nom.setTextColor(Color.BLACK);
                nom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nom.setTypeface(font);
                String s = String.format(Locale.ROOT,"%.1f",revenu.get_montant());
                nom.setText(s);
                row.addView(nom);


                TextView date = new TextView(this.getContext());
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
                date.setPadding(0,20,0,20);
                date.setTextColor(Color.BLACK);
                date.setTypeface(font);
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                date.setText(revenu.get_kilometrage()+"");
                row.addView(date);



                Button detail = new Button(this.getContext());
                detail.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, .75f));
                detail.setText("+plus");
                detail.setTypeface(font);
                detail.setTextColor(Color.BLACK);
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Choisissez une action")
                                .setMessage(" - KM debut : "+revenu.get_km_debut()+"\n - KM fin : "+revenu.get_km_fin())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNeutralButton("Modifier", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent myIntent = new Intent(activity, Revenuer.class); //// HERE
                                        myIntent.putExtra("id",revenu.get_id());
                                        startActivity(myIntent);
                                    }
                                })
                                .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbManager.openDataBase();
                                        dbManager.deleteRevenu(revenu.get_id());
                                        activity.finish();
                                        startActivity(activity.getIntent());
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                });

                if(revenu.get_montant()>0)
                {
                    nom.setBackgroundResource(R.drawable.cell_shapee);
                    detail.setBackgroundResource(R.drawable.cell_shapee);
                    prix.setBackgroundResource(R.drawable.cell_shapee);
                    date.setBackgroundResource(R.drawable.cell_shapee);
                }
                else
                {
                    nom.setBackgroundResource(R.drawable.cell_shape);
                    detail.setBackgroundResource(R.drawable.cell_shape);
                    prix.setBackgroundResource(R.drawable.cell_shape);
                    date.setBackgroundResource(R.drawable.cell_shape);
                }





                row.addView(detail);





                table_layout.addView(row);
            }



        }


    }


}
