package com.cognichamp.CogniChamp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cognichamp.CogniChamp.R.id;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Anshuman-HP on 03-09-2017.
 */

public class pagerAdapter extends FragmentStatePagerAdapter {
    Fragment currentFragment;
    marksFragments marksFragments=new marksFragments();
    schoolDetails schoolDetails=new schoolDetails();
    MainReportCard reportCardFragment = new MainReportCard();

    boolean isAge = false;
    public pagerAdapter(FragmentManager fm) {
        super(fm);
        this.currentFragment = fm.findFragmentById(id.pager);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            if (this.currentFragment instanceof schoolDetails) {

            }
            return this.schoolDetails;
        }
        else if(position==1){
            if (this.currentFragment instanceof schoolDetails) {
            }
            return this.marksFragments;
        } else if (position == 2) {
            return this.reportCardFragment;
        }
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) {
            return "School Details";
        }
        else if(position==1){
            if (isAge) {
                return "Grades";
            } else {
                return "Marks";
            }
        } else if (position == 2) {

            return "Report Card";
        }
        return super.getPageTitle(position);
    }

    public void updateAgeDetails(DatabaseReference ref, Context ctx) {
        isAge = true;
        this.marksFragments.setSpinnerAdapter();
        this.marksFragments.setUpLayoutForAge();
        this.schoolDetails.update();
        this.marksFragments.setUpRecyclerViewForAge(ref, ctx);
        this.reportCardFragment.reportCardFragment.update();
        this.reportCardFragment.generatedReportFragment.update();
    }
    public void update(DatabaseReference ref, Context ctx){
        this.marksFragments.setUpForNonAge();
        this.marksFragments.setSpinnerAdapter();
        this.marksFragments.setUpRecyclerView(ref, ctx);
        this.schoolDetails.update();
        this.reportCardFragment.reportCardFragment.update();
        this.reportCardFragment.generatedReportFragment.update();
    }
}
