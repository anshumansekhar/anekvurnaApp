package com.cognichamp.CogniChamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_INVITE =1 ;
    static FragmentManager fm;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FloatingActionButton floatingActionButton;
    MenuItem itemWER;
    Intent fg;
    boolean isFirstLaunch;
    int selected;
    String previousFragment;
    Toolbar toolbar;
    ActionBar actionBar;
    Fragment selectedFragment = new AnotherActivity();
    GoogleApiClient mGoogleApiClient;

    public static Fragment loadRateFragment() {
        return new FeedbackFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        RateUs.app_launched(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fg = getIntent();
        isFirstLaunch = fg.getBooleanExtra("IsFirstTime", false);
        if (isFirstLaunch) {
            fm = getSupportFragmentManager();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new ProfileFragment());
            selectedFragment = new ProfileFragment();
            actionBar.setTitle(R.string.ProfileTitle);
            transaction.commit();
        }else {
            previousFragment = fg.getStringExtra("PreviousFrag");
            if (previousFragment != null) {
                getPreviousFragment(previousFragment);
            }
            else{
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, new AnotherActivity());
                actionBar.setTitle(R.string.VideosTitle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        floatingActionButton = (FloatingActionButton) findViewById(R.id.FloatingActionButton);
        floatingActionButton.hide();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(Activity.RESULT_OK, data);
                for (String id : ids) {
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
                Toast.makeText(this, "Invite Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            if (selectedFragment instanceof AnotherActivity)
                //System.exit(0);
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        itemWER = menu.findItem(R.id.save);
        itemWER.setVisible(false);
        if (selectedFragment instanceof ProfileFragment) {
            itemWER.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sign_out) {
            signOut();
            return true;
        } else if (id == R.id.save)
        {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if(f instanceof EducationFragment)
            {
            }
            else if(f instanceof ProfileFragment)
            {
                ((ProfileFragment) f).saveChanges();
            }
            else if(f instanceof AccountFragment)
            {
                ((AccountFragment) f).saveChanges();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_share:
                sharePlayStoreLink();
                floatingActionButton.hide();
                break;
            case R.id.nav_send:
                onInviteClicked();
                floatingActionButton.hide();
                break;
            case R.id.nav_profile:
                itemWER.setVisible(true);
                selectedFragment = ProfileFragment.newInstance();
                floatingActionButton.hide();
                actionBar.setTitle(R.string.ProfileTitle);
                break;
            case R.id.nav_hobby:
                itemWER.setVisible(false);
                HobbiesFragment hobbiesFragment = HobbiesFragment.newInstance();
                floatingActionButton.show();
                floatingActionButton.setOnClickListener(hobbiesFragment.listener);
                floatingActionButton.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                selectedFragment = hobbiesFragment;
                actionBar.setTitle(R.string.HobbiesTitle);
                break;
            case R.id.presentClassVideos:
                closeOptionsMenu();
                itemWER.setVisible(false);
                selectedFragment = new AnotherActivity();
                floatingActionButton.hide();
                actionBar.setTitle(R.string.VideosTitle);
                break;
            case R.id.favoritesVideos:
                closeOptionsMenu();
                itemWER.setVisible(false);
                selectedFragment = new FavoriteVideosActivity();
                actionBar.setTitle(R.string.FavoritesTitle);
                floatingActionButton.hide();
                break;
            case R.id.EducationDetails:
                itemWER.setVisible(false);
                EducationFragment educationFragment=EducationFragment.newInstance();
                floatingActionButton.hide();
                selectedFragment = educationFragment;
                actionBar.setTitle(R.string.EducationTitle);
                break;
            case R.id.rateUs:
                closeOptionsMenu();
                itemWER.setVisible(false);
                selectedFragment = loadRateFragment();
                actionBar.setTitle(R.string.ContactUSTitle);
                floatingActionButton.hide();
                break;
            case R.id.nav_family:
                itemWER.setVisible(false);
                FamilyFragment fragment = FamilyFragment.newInstance();
                floatingActionButton.show();
                floatingActionButton.setOnClickListener(fragment.listener());
                floatingActionButton.setImageResource(R.drawable.ic_person_add_black_24dp);
                selectedFragment = fragment;
                actionBar.setTitle(R.string.FamilyTitle);
                break;
            case R.id.nav_account:
                closeOptionsMenu();
                itemWER.setVisible(false);
                selectedFragment = AccountFragment.newInstance();
                floatingActionButton.hide();
                actionBar.setTitle(R.string.AccoutTitle);
                break;

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onInviteClicked() {
        //TODO change the link
        Intent intent = new AppInviteInvitation.IntentBuilder("Invite Your Friends")
                .setEmailSubject("Invitation to Join " + getResources().getString(R.string.app_name))
                .setMessage("I am using this awesome App. Join this to take advantage of cognitive learning for your child. ")
                .setEmailHtmlContent("http://play.google.com/store/apps/details?id=com.cognichamp.CogniChamp")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    public void sharePlayStoreLink(){
        //TODO change the link
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "I am using this awesome App. You must also join this to take advantage of cognitive learning for your child\n"
                + "http://play.google.com/store/apps/details?id=com.cognichamp.CogniChamp");
        share.putExtra(Intent.EXTRA_SUBJECT, "Invitation ");
        startActivity(Intent.createChooser(share, "Share The App Link"));
    }
    public void getPreviousFragment(String previousFrag){
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(previousFrag.equals("Hobby")){
            transaction.replace(R.id.frame_layout, new HobbiesFragment());
            floatingActionButton = (FloatingActionButton) findViewById(R.id.FloatingActionButton);
            floatingActionButton.setImageResource(R.drawable.ic_playlist_add_black_24dp);
            floatingActionButton.show();
            actionBar.setTitle(R.string.HobbiesTitle);
        } else if (previousFrag.equals("account")) {
            transaction.replace(R.id.frame_layout, new AccountFragment());
            floatingActionButton = (FloatingActionButton) findViewById(R.id.FloatingActionButton);
            floatingActionButton.hide();
            actionBar.setTitle(R.string.AccoutTitle);

        }
        else if(previousFrag.equals("addFamily")){
            FamilyFragment familyFragment = new FamilyFragment();
            transaction.replace(R.id.frame_layout, familyFragment);
            floatingActionButton = (FloatingActionButton) findViewById(R.id.FloatingActionButton);
            floatingActionButton.show();
            Log.e("dgs", "" + floatingActionButton.isShown());
            floatingActionButton.setOnClickListener(familyFragment.listener());
            floatingActionButton.setImageResource(R.drawable.ic_person_add_black_24dp);

            actionBar.setTitle(R.string.FamilyTitle);
        }
        else if(previousFrag.equals("addSubject")){
            transaction.replace(R.id.frame_layout, new EducationFragment());
            floatingActionButton = (FloatingActionButton) findViewById(R.id.FloatingActionButton);
            floatingActionButton.setImageResource(R.drawable.ic_person_add_black_24dp);
            floatingActionButton.hide();
            actionBar.setTitle(R.string.EducationTitle);
        }
        else if(previousFrag.equals("addSchool")){
            transaction.replace(R.id.frame_layout, new EducationFragment());
            actionBar.setTitle(R.string.EducationTitle);
        } else if (previousFrag.equals(R.string.AccoutTitle)) {
            transaction.replace(R.id.frame_layout, new AccountFragment());
        } else if (previousFrag.equals("FullScreen")) {
            transaction.replace(R.id.frame_layout, new EducationFragment());
            actionBar.setTitle(R.string.EducationTitle);

        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void signOut(){
        if (firebaseAuth.getCurrentUser().getProviders().get(0).contains("google")) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            firebaseAuth.signOut();
                            startActivity(new Intent(NavigationDrawer.this, SignUpChooseActivity.class));
                        }
                    });
        } else if (firebaseAuth.getCurrentUser().getProviders().get(0).contains("facebook")) {
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
            startActivity(new Intent(this, SignUpChooseActivity.class));
        }
        else {
            firebaseAuth.signOut();
            startActivity(new Intent(this, SignUpChooseActivity.class));
        }
    }
}
