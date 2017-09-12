package com.example.anshuman_hp.internship;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class pagerAdapter extends FragmentStatePagerAdapter {
    Fragment currentFragment;
    public pagerAdapter(FragmentManager fm) {
        super(fm);
        currentFragment=fm.findFragmentById(R.id.pager);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            if(currentFragment instanceof schoolDetails) {
                ((schoolDetails) currentFragment).saveChanges();
            }
            return new schoolDetails();
        }
        else if(position==1){
            if(currentFragment instanceof schoolDetails) {
                ((schoolDetails) currentFragment).saveChanges();
            }
            return new marksFragments();
        }
        else
            return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) {
            return "School Details";
        }
        else if(position==1){
            return "Marks";
        }
        return super.getPageTitle(position);
    }
}
