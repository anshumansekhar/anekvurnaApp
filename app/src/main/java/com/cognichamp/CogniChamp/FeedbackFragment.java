package com.cognichamp.CogniChamp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Anshuman-HP on 28-09-2017.
 */

public class FeedbackFragment extends Fragment{

    TextView tv, website;
    ImageButton twitter, facebook;

    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/177065656174654"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/CogniChamps/"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.new_feedback,container,false);
        tv=(TextView)v.findViewById(R.id.feedbackText);
        twitter = (ImageButton) v.findViewById(R.id.twitterPAge);
        facebook = (ImageButton) v.findViewById(R.id.facebookPAge);
        website = (TextView) v.findViewById(R.id.website);

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.cognichamp.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getOpenFacebookIntent(getActivity()));
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=cognichamp"));
                    startActivity(intent);

                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/#!/cognichamp")));
                }
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("vnd.android.cursor.item/mail");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@cognichamp.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
//                Uri data = Uri.parse("mailto:contact@cognichamp.com?subject=feedback" );
//                intent.setData(data);
                try {
                    startActivity(Intent.createChooser(intent, "Send Feedback Using"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No Email App Available", Toast.LENGTH_SHORT).show();
                    //TODO: Handle case where no email app is available
                }
            }
        });
        return v;
    }
}
