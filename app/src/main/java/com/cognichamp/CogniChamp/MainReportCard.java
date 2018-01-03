package com.cognichamp.CogniChamp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Anshuman-HP on 17-10-2017.
 */

public class MainReportCard extends Fragment {
    TabLayout tabLayout;
    ReportCardFragment reportCardFragment = new ReportCardFragment();
    generatedReportFragment generatedReportFragment = new generatedReportFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report_card_frame, container, false);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.reportCardContainer, reportCardFragment);
        transaction.commit();

        tabLayout = (TabLayout) v.findViewById(R.id.reportLayout);
        FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.reportCardContainer, generatedReportFragment);
        transaction1.commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.reportCardContainer, generatedReportFragment);
                    transaction.commit();

                } else if (tab.getPosition() == 1) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.reportCardContainer, reportCardFragment);
                    transaction.commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return v;
    }
}
