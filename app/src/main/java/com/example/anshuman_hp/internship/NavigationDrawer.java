package com.example.anshuman_hp.internship;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_INVITE =1 ;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FloatingActionButton floatingActionButton;

    static FragmentManager fm;

    int selected;
    Toolbar toolbar;
    ActionBar actionBar;

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

        fm=getSupportFragmentManager();



            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new AnotherActivity());
            transaction.commit();

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            startActivity(new Intent(NavigationDrawer.this,MainActivity.class));
            return true;
        }
        else if(id==R.id.save)
        {
            Log.e("Clciked","agd");
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
        Fragment selectedFragment = null;
        switch (id){
            case R.id.nav_share:
                shareApplication();
                floatingActionButton.hide();
                break;
            case R.id.nav_send:
                onInviteClicked();
                floatingActionButton.hide();
                break;
            case R.id.nav_profile:
                ProfileFragment profileFragment=ProfileFragment.newInstance();
                selectedFragment=profileFragment;
                floatingActionButton.hide();
                actionBar.setTitle("Profile");
                break;
            case R.id.nav_hobby:
                HobbiesFragment hobbiesFragment = HobbiesFragment.newInstance();
                floatingActionButton.show();
                floatingActionButton.setOnClickListener(hobbiesFragment.listener);
                floatingActionButton.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                selectedFragment=hobbiesFragment;
                actionBar.setTitle("Hobbies");
                break;
            case R.id.presentClassVideos:
                AnotherActivity anotherActivityFrag=new AnotherActivity();
                selectedFragment=anotherActivityFrag;
                floatingActionButton.hide();
                break;
            case R.id.favoritesVideos:
                FavoriteVideosActivity favoriteVideosFrag=new FavoriteVideosActivity();
                selectedFragment =favoriteVideosFrag;
                actionBar.setTitle("Favourites");
                floatingActionButton.hide();
                break;
            case R.id.EducationDetails:
                EducationFragment educationFragment=EducationFragment.newInstance();
                floatingActionButton.hide();
                selectedFragment = educationFragment;
                actionBar.setTitle("Education");
                break;
            case R.id.rateUs:
                SharedPreferences prefs = getSharedPreferences("rateus", 0);
                selectedFragment=loadRateFragment();
                actionBar.setTitle("Rate US");
                floatingActionButton.hide();
                break;
            case R.id.nav_family:
                FamilyFragment fragment = FamilyFragment.newInstance();
                floatingActionButton.show();
                floatingActionButton.setOnClickListener(fragment.listener());
                floatingActionButton.setImageResource(R.drawable.ic_person_add_black_24dp);
                selectedFragment=fragment;
                actionBar.setTitle("Family");
                break;
            case R.id.nav_account:
                AccountFragment accountFragment=AccountFragment.newInstance();
                selectedFragment = accountFragment;
                floatingActionButton.hide();
                actionBar.setTitle("Account");
                break;

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment f=getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Invite Your Friends")
                .setMessage("I am Using This awesome Message")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    public static Fragment loadRateFragment(){
        return new AppRatingActivity();

    }
}
