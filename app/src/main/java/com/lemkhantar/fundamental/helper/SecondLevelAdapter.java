package com.lemkhantar.fundamental.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.activities.AjoutPiece;
import com.lemkhantar.fundamental.activities.Rechanger;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.Categorie;

import java.util.ArrayList;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    Typeface font;


     int categorieIndice;
    private ArrayList<Categorie> categories;

    public SecondLevelAdapter(final Context mContext, final int categorieIndice, ArrayList<Categorie> categories) {
        this.mContext = mContext;
        this.categorieIndice = categorieIndice;
        this.categories = categories;
        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/police.ttf");

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(categorieIndice).getSousCategories().get(groupPosition).getProduits().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {




        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        final DBManager dbManager = new DBManager(convertView.getContext());
        dbManager.openDataBase();

        final TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtListChild.setText(categories.get(categorieIndice).getSousCategories().get(groupPosition).getProduits().get(childPosition).toString());
        txtListChild.setTypeface(font);
        txtListChild.setTextColor(Color.BLACK);

        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(categorieIndice+" - "+groupPosition+" - "+childPosition);
                Intent myIntent = new Intent(v.getContext(), Rechanger.class);
                myIntent.putExtra("categorie",categorieIndice);
                myIntent.putExtra("sousCategorie",groupPosition);
                myIntent.putExtra("produit",childPosition);
                v.getContext().startActivity(myIntent);
            }
        });

        txtListChild.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(txtListChild.getText().toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNeutralButton("Modifier", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(v.getContext(), AjoutPiece.class);
                                myIntent.putExtra("produit", txtListChild.getText().toString());
                                v.getContext().startActivity(myIntent);

                            }
                        })
                        .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.deleteProduit(txtListChild.getText().toString());
                                ((Activity)mContext).finish();
                                mContext.startActivity(((Activity)mContext).getIntent());
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                return true;
            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return categories.get(categorieIndice).getSousCategories().get(groupPosition).getProduits().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(categorieIndice).getSousCategories().get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return categories.get(categorieIndice).getSousCategories().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_group_second, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(categories.get(categorieIndice).getSousCategories().get(groupPosition).toString());
        lblListHeader.setTextColor(Color.BLACK);
        lblListHeader.setTypeface(font);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
