package com.lemkhantar.fundamental.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lemkhantar.fundamental.fragments.TabRevenuesMensuels;
import com.lemkhantar.fundamental.fragments.TabRevenuesQuotidiens;

public class PagerAdapterRevenu extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterRevenu(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabRevenuesQuotidiens tab1 = new TabRevenuesQuotidiens();
                return tab1;
            case 1:
                TabRevenuesMensuels tab2 = new TabRevenuesMensuels();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}