package com.cognichamp.CogniChamp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    YouTubePlayer mPlayer;
    Intent g;
    video[] videos;
    String videoURL;
    String videoID;
    TextView currentDuration;
    TextView videoDuration;
    SeekBar videoSeekBar;
    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
            videoDuration.setText(formatTime(mPlayer.getDurationMillis()));

        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();

        }
    };
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mPlayer != null) {
                long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
                mPlayer.seekToMillis((int) lengthPlayed);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private YouTubePlayerView youTubeView;
    private Handler mHandler = null;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };
    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);


        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();


        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        currentDuration = (TextView) findViewById(R.id.currentTime);
        videoDuration = (TextView) findViewById(R.id.videoDuration);
        videoSeekBar = (SeekBar) findViewById(R.id.videoSeekBar);

        mHandler = new Handler();

        videoSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);


        g=getIntent();
        videoURL=g.getStringExtra("VideoURL");
        videoID=g.getStringExtra("VideoID");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(getResources().getString(R.string.youtubeDeveloperKey), this);
        Log.e("zhfgd", youTubeView.getChildAt(0).getRootView().toString());

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,final YouTubePlayer youTubePlayer, boolean b) {
        if (null == youTubePlayer) return;
        mPlayer = youTubePlayer;
        displayCurrentTime();
        if (!b) {
            youTubePlayer.cueVideo(videoID);
        }
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        youTubePlayer.setPlaybackEventListener(mPlaybackEventListener);
        youTubePlayer.setPlayerStateChangeListener(mPlayerStateChangeListener);



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

    private void displayCurrentTime() {
        if (null == mPlayer) return;
        if (mPlayer != null) {
            String formattedTime = formatTime(mPlayer.getCurrentTimeMillis());
            currentDuration.setText(formattedTime);
        }
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer = null;
    }
}
