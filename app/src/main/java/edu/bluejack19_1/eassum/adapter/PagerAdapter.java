package edu.bluejack19_1.eassum.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.bluejack19_1.eassum.fragmentHome.AboutUs;
import edu.bluejack19_1.eassum.fragmentHome.Home;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public PagerAdapter(FragmentManager fm,int NumberOfTabs) {
        super(fm);
        this.numOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Home h = new Home();
                return h;
            case 1:
                AboutUs au = new AboutUs();
                return au;
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
