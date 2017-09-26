package com.example.anshuman_hp.CogniChamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        RateUs.app_launched(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fg=getIntent();
        isFirstLaunch=fg.getBooleanExtra("IsFirstTime",false);
        if(isFirstLaunch){
            fm=getSupportFragmentManager();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new ProfileFragment());
            actionBar.setTitle("Student Profile Details");
            transaction.commit();
        }else {
            previousFragment = fg.getStringExtra("PreviousFrag");
            if(previousFragment!=null) {
                getPreviousFragment(previousFragment);
            }
            else{
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, new AnotherActivity());
                actionBar.setTitle("Student Profile Details");
                transaction.commit();
            }
        }
        floatingActionButton=(FloatingActionButton)findViewById(R.id.FloatingActionButton);
        floatingActionButton.hide();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    //Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
                Toast.makeText(NavigationDrawer.this,"Invite Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        itemWER= menu.findItem(R.id.save);
        itemWER.setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            startActivity(new Intent(NavigationDrawer.this,SignUpChooseActivity.class));
            return true;
        }
        else if(id==R.id.save)
        {
            Fragment f=getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if(f instanceof EducationFragment)
            {
                Log.e("Tag","Show Dialog");
            }
            else if(f instanceof ProfileFragment)
            {
                ((ProfileFragment) f).saveChanges();
                Log.e("Tag","Show Dialog");
            }
            else if(f instanceof AccountFragment)
            {


                    ((AccountFragment) f).saveChanges();
                    Log.e("Tag","Show Dialog");

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
                ProfileFragment profileFragment=ProfileFragment.newInstance();
                selectedFragment=profileFragment;
                floatingActionButton.hide();
                actionBar.setTitle("Student Profile");
                break;
            case R.id.nav_hobby:
                itemWER.setVisible(false);
                HobbiesFragment hobbiesFragment = HobbiesFragment.newInstance();
                floatingActionButton.show();
                floatingActionButton.setOnClickListener(hobbiesFragment.listener);
                floatingActionButton.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                selectedFragment=hobbiesFragment;
                actionBar.setTitle("Hobbies");
                break;
            case R.id.presentClassVideos:
                closeOptionsMenu();
                itemWER.setVisible(false);
                AnotherActivity anotherActivityFrag=new AnotherActivity();
                selectedFragment=anotherActivityFrag;
                floatingActionButton.hide();
                actionBar.setTitle("Tutorials");
                break;
            case R.id.favoritesVideos:
                closeOptionsMenu();
                itemWER.setVisible(false);
                FavoriteVideosActivity favoriteVideosFrag=new FavoriteVideosActivity();
                selectedFragment =favoriteVideosFrag;
                actionBar.setTitle("My Favourites");
                floatingActionButton.hide();
                break;
            case R.id.EducationDetails:
                itemWER.setVisible(false);
                EducationFragment educationFragment=EducationFragment.newInstance();
                floatingActionButton.hide();
                selectedFragment = educationFragment;
                actionBar.setTitle("Education");
                break;
            case R.id.rateUs:
                closeOptionsMenu();
                itemWER.setVisible(false);
                selectedFragment=loadRateFragment();
                actionBar.setTitle("Contact us");
                floatingActionButton.hide();
                break;
            case R.id.nav_family:
                itemWER.setVisible(true);
                FamilyFragment fragment = FamilyFragment.newInstance();
                floatingActionButton.show();
                floatingActionButton.setOnClickListener(fragment.listener());
                floatingActionButton.setImageResource(R.drawable.ic_person_add_black_24dp);
                selectedFragment=fragment;
                actionBar.setTitle("Family");
                break;
            case R.id.nav_account:
                closeOptionsMenu();
                itemWER.setVisible(false);
                AccountFragment accountFragment=AccountFragment.newInstance();
                selectedFragment = accountFragment;
                floatingActionButton.hide();
                actionBar.setTitle("Account");
                break;

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Invite Your Friends")
                .setMessage("I am using this awesome App. You must also join this to take advantage of cognitive learning for your child.")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    public static Fragment loadRateFragment(){
        return new feedbackActivity();
    }
    public void sharePlayStoreLink(){
        //TODO change the link
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=com.example.anshuman_hp.internship");
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, "I am using this awesome App. You must also join this to take advantage of cognitive learning for your child");
        startActivity(Intent.createChooser(share, "Share The App Link"));
    }
    public void getPreviousFragment(String previousFrag){
        fm=getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(previousFrag.equals("Hobby")){
            transaction.replace(R.id.frame_layout, new HobbiesFragment());
        }
        else if(previousFrag.equals("addFamily")){
            transaction.replace(R.id.frame_layout, new FamilyFragment());
        }
        else if(previousFrag.equals("addSubject")){
            transaction.replace(R.id.frame_layout, new EducationFragment());
        }
        else if(previousFrag.equals("addSchool")){
            transaction.replace(R.id.frame_layout, new EducationFragment());
        }
        transaction.commit();
    }
}
