package com.main.cognichamp.CogniChamp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    Intent g;
    video[] videos;

    String videoURL;
    String videoID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        g=getIntent();
        videoURL=g.getStringExtra("VideoURL");
        videoID=g.getStringExtra("VideoID");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(getResources().getString(R.string.youtubeDeveloperKey), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,final YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(videoID);
            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {

                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Erroe", youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getResources().getString(R.string.youtubeDeveloperKey), this);
        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.youtube, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.shareVideomenu) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoURL));
            startActivity(Intent.createChooser(share, "Share Video"));


        }
        return super.onOptionsItemSelected(item);

    }
}
