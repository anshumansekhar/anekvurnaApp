package com.example.anshuman_hp.internship;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 28-08-2017.
 */

public class RateUs {

    private final static String TITLE = "Anekvurna";

    private final static String PACKAGE_NAME = "com.example.anshuman_hp.internship";

    private final static int DAYS_UNTIL_PROMPT = 2;

    private final static int LAUNCHES_UNTIL_PROMPT = 5;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch
                    + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }
    public static void showRateDialog(final Context mContext,
                                      final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        // Set Dialog Title
        dialog.setTitle("Rate " + TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("Give us 5 stars");
        tv.setTextSize(30);
        tv.setWidth(700);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        // First Button
        Button b1 = new Button(mContext);
        b1.setText("Rate 5 Stars");
        b1.setBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//                        .parse("market://details?id=" + PACKAGE_NAME)));
                NavigationDrawer.loadRateFragment();
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        // Second Button
        Button b2 = new Button(mContext);
        b2.setText("Not Right Now");
        b2.setBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        // Third Button
        dialog.setContentView(ll);

        // Show Dialog
        dialog.show();
    }
}
