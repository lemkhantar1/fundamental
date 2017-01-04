package com.lemkhantar.fundamental.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.entity.Categorie;

import java.util.ArrayList;
import java.util.List;


public class ParentLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    Typeface font;

    private  ArrayList<Categorie> categories = new ArrayList<>();

    public ParentLevelAdapter(Context mContext, List<String> mListDataHeader, ArrayList<Categorie> categories) {
        this.mContext = mContext;
        this.categories = categories;
        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/police.ttf");
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        //String parentNode = (String) getGroup(groupPosition);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.mContext, groupPosition, categories));
        secondLevelExpListView.setGroupIndicator(null);
        secondLevelExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    secondLevelExpListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        return secondLevelExpListView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return categories.size();
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
            convertView = layoutInflater.inflate(R.layout.drawer_list_group, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setTextColor(Color.BLACK);
        lblListHeader.setText(categories.get(groupPosition).toString());
        lblListHeader.setTypeface(font);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        int image = mContext.getResources().getIdentifier(categories.get(groupPosition).get_photo(),"drawable",mContext.getPackageName());
        imageView.setImageResource(image);
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
