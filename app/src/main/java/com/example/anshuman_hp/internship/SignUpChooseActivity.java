package com.example.anshuman_hp.internship;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

/**
 * Created by Anshuman-HP on 20-09-2017.
 */

public class SignUpChooseActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignUpChooseActivity";

    TextView login;
    Button emailSignUp;
    LoginButton fbLogin;
    SignInButton googleSignIn;


    CallbackManager callbackManager;
    ShareLinkContent shareLinkContent;
    ShareDialog shareDialog;
    GoogleApiClient mGoogleApiClient;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(SignUpChooseActivity.this,NavigationDrawer.class));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        shareLinkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://internship2-4d772.firebaseapp.com/"))
                .setQuote("I am Using this Awesome App")
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag(getResources().getString(R.string.app_name))
                        .build())
                .build();
        shareDialog=new ShareDialog(SignUpChooseActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "connection failed with google client");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        fbLogin = (LoginButton) findViewById(R.id.fbLogin);
        googleSignIn = (SignInButton) findViewById(R.id.GLogin);
        login=(TextView)findViewById(R.id.loginText);
        emailSignUp=(Button)findViewById(R.id.EmailSignUp);

        fbLogin.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("dv",loginResult.getAccessToken().toString());
                Log.d("facebook:onSuccess:", loginResult.toString());
                Profile currentFbProfile = Profile.getCurrentProfile();
                handleFacebookAccessToken(loginResult.getAccessToken(), currentFbProfile);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.toString());
            }
        });
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Signing with Google");
                signInWithGoogle();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpChooseActivity.this,LoginEmail.class));
            }
        });
        emailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpChooseActivity.this,Registration.class));
            }
        });
    }
    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.e(TAG, "Siging with google successful");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }
    public void handleFacebookAccessToken(final AccessToken token, final Profile currentProfile) {
        Log.d("handle", token.toString());
        Log.e(TAG, "Loginning with firrbase from facebook");
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                                    .child("UserProfile")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                startActivity(new Intent(SignUpChooseActivity.this, NavigationDrawer.class));
                                            } else {
                                                user_profile userProfile = new user_profile(Profile.getCurrentProfile().getName()
                                                        , ""
                                                        , "true"
                                                        , "1"
                                                        , Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString()
                                                        ,""
                                                        ,""
                                                        ,""
                                                        ,"");
                                                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                                                        .child("UserProfile")
                                                        .setValue(userProfile);
                                                //pushClassDetails(firebaseAuth.getCurrentUser().getUid());
                                                shareDialog.show(shareLinkContent);
                                                startActivity(new Intent(SignUpChooseActivity.this, NavigationDrawer.class));

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                        } else {
                            Log.e("sagd","Task FAiled");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failure not looged in");
                Log.e(TAG, e.toString());

            }
        });
    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseDatabase.getReference("UserProfile")
                                    .child(firebaseAuth.getCurrentUser().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                startActivity(new Intent(SignUpChooseActivity.this, NavigationDrawer.class));
                                            } else {
                                                user_profile userProfile = new user_profile(acct.getDisplayName()
                                                        , ""
                                                        , "true"
                                                        , "1"
                                                        , acct.getPhotoUrl().toString()
                                                        , ""
                                                        ,""
                                                        ,""
                                                        ,"");
                                                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                                                        .child("UserProfile")
                                                        .setValue(userProfile);
                                                startActivity(new Intent(SignUpChooseActivity.this, NavigationDrawer.class));
                                                Intent shareIntent = new PlusShare.Builder(getApplicationContext())
                                                        .setType("text/plain")
                                                        .setText("I am Using This awesome App" + getResources().getString(R.string.app_name))
                                                        .setContentUrl(Uri.parse("https://internship2-4d772.firebaseapp.com/"))
                                                        .getIntent();
                                                startActivityForResult(shareIntent, 0);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                        } else {
                        }
                    }
                });
    }
}
