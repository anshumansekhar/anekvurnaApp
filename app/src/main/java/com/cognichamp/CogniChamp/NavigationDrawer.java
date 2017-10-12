package com.cognichamp.CogniChamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cognichamp.CogniChamp.R.drawable;
import com.cognichamp.CogniChamp.R.id;
import com.cognichamp.CogniChamp.R.layout;
import com.cognichamp.CogniChamp.R.string;
import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitation.IntentBuilder;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationDrawer extends AppCompatActivity
        implements OnNavigationItemSelectedListener {
    private static final int REQUEST_INVITE =1 ;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FloatingActionButton floatingActionButton;
    static FragmentManager fm;
    MenuItem itemWER;
    Intent fg;
    boolean isFirstLaunch;
    int selected;
    String previousFragment;
    Toolbar toolbar;
    ActionBar actionBar;
    Fragment selectedFragment = new AnotherActivity();
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_navigation_drawer);
        RateUs.app_launched(this);
        this.toolbar = (Toolbar) this.findViewById(id.toolbar);
        this.setSupportActionBar(this.toolbar);
        this.actionBar = this.getSupportActionBar();
        Toolbar toolbar = (Toolbar) this.findViewById(id.toolbar);
        this.setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getString(string.default_web_client_id))
                .requestEmail()
                .build();

        this.mGoogleApiClient = new Builder(this)
                .enableAutoManage(this, new OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        this.fg = this.getIntent();
        this.isFirstLaunch = this.fg.getBooleanExtra("IsFirstTime", false);
        if (this.isFirstLaunch) {
            NavigationDrawer.fm = this.getSupportFragmentManager();
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.replace(id.frame_layout, new ProfileFragment());
            this.selectedFragment = new ProfileFragment();
            this.actionBar.setTitle("Student Profile Details");
            transaction.commit();
        }else {
            this.previousFragment = this.fg.getStringExtra("PreviousFrag");
            if (this.previousFragment != null) {
                this.getPreviousFragment(this.previousFragment);
            }
            else{
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(id.frame_layout, new AnotherActivity());
                this.actionBar.setTitle("Videos");
                transaction.commit();
            }
        }
        this.floatingActionButton = (FloatingActionButton) this.findViewById(id.FloatingActionButton);
        this.floatingActionButton.hide();
        NavigationView navigationView = (NavigationView) this.findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NavigationDrawer.REQUEST_INVITE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(RESULT_OK, data);
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
        DrawerLayout drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(menu.navigation_drawer, menu);
        this.itemWER = menu.findItem(id.save);
        this.itemWER.setVisible(false);
        if (this.selectedFragment instanceof ProfileFragment) {
            this.itemWER.setVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == id.sign_out) {
            this.signOut();
            return true;
        } else if (id == id.save)
        {
            Fragment f = this.getSupportFragmentManager().findFragmentById(id.frame_layout);
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
            case id.nav_share:
                this.sharePlayStoreLink();
                this.floatingActionButton.hide();
                break;
            case id.nav_send:
                this.onInviteClicked();
                this.floatingActionButton.hide();
                break;
            case id.nav_profile:
                this.itemWER.setVisible(true);
                this.selectedFragment = ProfileFragment.newInstance();
                this.floatingActionButton.hide();
                this.actionBar.setTitle("Student Profile");
                break;
            case id.nav_hobby:
                this.itemWER.setVisible(false);
                HobbiesFragment hobbiesFragment = HobbiesFragment.newInstance();
                this.floatingActionButton.show();
                this.floatingActionButton.setOnClickListener(hobbiesFragment.listener);
                this.floatingActionButton.setImageResource(drawable.ic_playlist_add_black_24dp);
                this.selectedFragment = hobbiesFragment;
                this.actionBar.setTitle("Hobbies");
                break;
            case id.presentClassVideos:
                this.closeOptionsMenu();
                this.itemWER.setVisible(false);
                this.selectedFragment = new AnotherActivity();
                this.floatingActionButton.hide();
                this.actionBar.setTitle("Videos");
                break;
            case id.favoritesVideos:
                this.closeOptionsMenu();
                this.itemWER.setVisible(false);
                this.selectedFragment = new FavoriteVideosActivity();
                this.actionBar.setTitle("My Favourites");
                this.floatingActionButton.hide();
                break;
            case id.EducationDetails:
                this.itemWER.setVisible(false);
                EducationFragment educationFragment=EducationFragment.newInstance();
                this.floatingActionButton.hide();
                this.selectedFragment = educationFragment;
                this.actionBar.setTitle("Education");
                break;
            case id.rateUs:
                this.closeOptionsMenu();
                this.itemWER.setVisible(false);
                this.selectedFragment = NavigationDrawer.loadRateFragment();
                this.actionBar.setTitle("Contact us");
                this.floatingActionButton.hide();
                break;
            case id.nav_family:
                this.itemWER.setVisible(false);
                FamilyFragment fragment = FamilyFragment.newInstance();
                this.floatingActionButton.show();
                this.floatingActionButton.setOnClickListener(fragment.listener());
                this.floatingActionButton.setImageResource(drawable.ic_person_add_black_24dp);
                this.selectedFragment = fragment;
                this.actionBar.setTitle("Family");
                break;
            case id.nav_account:
                this.closeOptionsMenu();
                this.itemWER.setVisible(false);
                this.selectedFragment = AccountFragment.newInstance();
                this.floatingActionButton.hide();
                this.actionBar.setTitle("Account");
                break;

        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(id.frame_layout, this.selectedFragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void onInviteClicked() {
        //TODO change the link
        Intent intent = new IntentBuilder("Invite Your Friends")
                .setEmailSubject("Invitation to Join " + this.getResources().getString(string.app_name))
                .setMessage("I am using this awesome App.Join this to take advantage of cognitive learning for your child.")
                .setEmailHtmlContent("http://play.google.com/store/apps/details?id=com.cognichamp.CogniChamp")
                .build();
        this.startActivityForResult(intent, NavigationDrawer.REQUEST_INVITE);
    }

    private void onInviteClicked() {
        //TODO change the link
        Intent intent = new IntentBuilder("Invite Your Friends")
                .setEmailSubject("Invitation to Join " + this.getResources().getString(string.app_name))
                .setMessage("I am using this awesome App.Join this to take advantage of cognitive learning for your child.")
                .setEmailHtmlContent("http://play.google.com/store/apps/details?id=com.cognichamp.CogniChamp")
                .build();
        this.startActivityForResult(intent, NavigationDrawer.REQUEST_INVITE);
    }

    public static Fragment loadRateFragment() {
        return new FeedbackFragment();
    }

    public void sharePlayStoreLink(){
        //TODO change the link
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=com.cognichamp.CogniChamp");
        share.putExtra(Intent.EXTRA_SUBJECT, "I am using this awesome App. You must also join this to take advantage of cognitive learning for your child");
        this.startActivity(Intent.createChooser(share, "Share The App Link"));
    }
    public void getPreviousFragment(String previousFrag){
        NavigationDrawer.fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        if(previousFrag.equals("Hobby")){
            transaction.replace(id.frame_layout, new HobbiesFragment());
            this.floatingActionButton = (FloatingActionButton) this.findViewById(id.FloatingActionButton);
            this.floatingActionButton.setImageResource(drawable.ic_playlist_add_black_24dp);
            this.floatingActionButton.show();
            this.actionBar.setTitle("Hobbies");
        }
        else if(previousFrag.equals("addFamily")){
            transaction.replace(id.frame_layout, new FamilyFragment());
            this.floatingActionButton = (FloatingActionButton) this.findViewById(id.FloatingActionButton);
            this.floatingActionButton.setImageResource(drawable.ic_person_add_black_24dp);
            this.floatingActionButton.show();
            this.actionBar.setTitle("Family");
        }
        else if(previousFrag.equals("addSubject")){
            transaction.replace(id.frame_layout, new EducationFragment());
            this.actionBar.setTitle("Education");
        }
        else if(previousFrag.equals("addSchool")){
            transaction.replace(id.frame_layout, new EducationFragment());
            this.actionBar.setTitle("Education");
        }
        else if(previousFrag.equals("account")){
            transaction.replace(id.frame_layout, new AccountFragment());
        }
        transaction.commit();
    }
    public void signOut(){
        if (this.firebaseAuth.getCurrentUser().getProviders().get(0).contains("google")) {
            Auth.GoogleSignInApi.signOut(this.mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            NavigationDrawer.this.firebaseAuth.signOut();
                            NavigationDrawer.this.startActivity(new Intent(NavigationDrawer.this, SignUpChooseActivity.class));
                        }
                    });
        } else if (this.firebaseAuth.getCurrentUser().getProviders().get(0).contains("facebook")) {
            LoginManager.getInstance().logOut();
            this.firebaseAuth.signOut();
            this.startActivity(new Intent(this, SignUpChooseActivity.class));
        }
        else {
            this.firebaseAuth.signOut();
            this.startActivity(new Intent(this, SignUpChooseActivity.class));
        }
    }
}
