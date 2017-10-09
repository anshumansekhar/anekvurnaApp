package com.cognichamp.CogniChamp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class pagerAdapter extends FragmentStatePagerAdapter {
    Fragment currentFragment;
    marksFragments marksFragments=new marksFragments();
    schoolDetails schoolDetails=new schoolDetails();
    public pagerAdapter(FragmentManager fm) {
        super(fm);
        currentFragment=fm.findFragmentById(R.id.pager);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            if(currentFragment instanceof schoolDetails) {

            }
            return schoolDetails;
        }
        else if(position==1){
            if(currentFragment instanceof schoolDetails) {
            }
            return marksFragments;
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
    public void update(DatabaseReference ref, Context ctx){
        marksFragments.setUpRecyclerView(ref,ctx);
        marksFragments.setSpinnerAdapter();
        schoolDetails.update();
    }
}
