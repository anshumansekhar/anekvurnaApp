package com.cognichamp.CogniChamp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cognichamp.CogniChamp.R.color;


/**
 * Created by Anshuman-HP on 28-08-2017.
 */

public class RateUs {

    private static final String TITLE = "CogniChamp";

    private static final String PACKAGE_NAME = "com.cognichamp.CogniChamp";

    private static final int DAYS_UNTIL_PROMPT = 10;

    private static final int LAUNCHES_UNTIL_PROMPT = 15;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        if (launch_count >= RateUs.LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch
                    + RateUs.DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                RateUs.showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }
    public static void showRateDialog(final Context mContext,
                                      Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        // Set Dialog Title
        dialog.setTitle("Rate " + RateUs.TITLE);

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
        b1.setBackgroundColor(mContext.getResources().getColor(color.cardview_light_background));
        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + RateUs.PACKAGE_NAME)));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        // Second Button
        Button b2 = new Button(mContext);
        b2.setText("Not Right Now");
        b2.setBackgroundColor(mContext.getResources().getColor(color.cardview_light_background));
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
                Editor editor = prefs.edit();
                editor.putLong("launch_count", 0);
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
                Editor editor = prefs.edit();
                editor.putLong("launch_count", 0);
                dialog.dismiss();

            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
                Editor editor = prefs.edit();
                editor.putLong("launch_count", 0);
                dialog.dismiss();
            }
        });

        // Third Button
        dialog.setContentView(ll);

        // Show Dialog
        dialog.show();
    }
}
