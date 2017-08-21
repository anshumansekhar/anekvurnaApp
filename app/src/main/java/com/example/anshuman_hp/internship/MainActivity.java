package com.example.anshuman_hp.internship;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;


    EditText email;
    EditText password;
    EditText phone;
    LoginButton fbLogin;
    SignInButton googleSignin;
    Button submit;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;
    String emailText,passwordText,phoneText;
    CallbackManager callbackManager;
    ShareLinkContent shareLinkContent;
    ShareDialog shareDialog;
    GoogleApiClient mGoogleApiClient;




    public static final String TAG="MainActivity";

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if (key.equals("AnotherActivity") && value.equals("True")) {
                    Intent intent = new Intent(this, AnotherActivity.class);
                    intent.putExtra("value", value);
                    startActivity(intent);
                    finish();
                }
            }
        }
        //subscribeToPushService();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this,NavigationDrawer.class));
        }
    }
    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("AndroidBash", "Subscribed");
        Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        Log.d("AndroidBash", token);
        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager=CallbackManager.Factory.create();
        Log.e(TAG,"Building sharelink");
        shareLinkContent=new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://internship2-4d772.firebaseapp.com/"))
                .setQuote("I am Using this Awesome App")
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag(getResources().getString(R.string.app_name))
                        .build())
                .build();
        shareDialog=new ShareDialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG,"connection failed with google client");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        phone=(EditText)findViewById(R.id.mobile);
        fbLogin=(LoginButton)findViewById(R.id.fbloginButton);
        googleSignin=(SignInButton)findViewById(R.id.googlelogin);
        googleSignin.setSize(SignInButton.SIZE_STANDARD);
        submit=(Button)findViewById(R.id.Submit);


        firebaseDatabase=FirebaseDatabase.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Submit Clicked");
                emailText=email.getText().toString();
                passwordText=password.getText().toString();
                phoneText=phone.getText().toString();
                if(!emailText.isEmpty()) {
                    if (checkEmailPattern(emailText)) {
                        firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Log.e(TAG, "Sign in Successful starting next activity");
                                        startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, e.toString());
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Confirmation");
                                builder.setMessage("The entered Emailt has not Yet been Registered.Do you want to Register now?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        Log.e(TAG, "Creating User ");
                                                        Log.e(TAG, "Creating User Successful");
                                                        User user = new User(emailText
                                                                , ""
                                                                , firebaseAuth.getCurrentUser().getUid()
                                                                , ""
                                                                , ""
                                                                , "true"
                                                                , passwordText
                                                                , phoneText);
                                                        firebaseDatabase.
                                                                getReference("Users").
                                                                child(firebaseAuth.getCurrentUser().getUid()).
                                                                setValue(user)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.e(TAG, "User Added to Database");
                                                                            Log.e(TAG, "Starting Acitvity");
                                                                            startActivity(new Intent(MainActivity.this, Registration.class));
                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.e(TAG, "Failed to add User to database");
                                                                        Log.e(TAG, e.toString());
                                                                    }
                                                                });
                                                    }


                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, e.toString());
                                            }
                                        });
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    } else if (!checkEmailPattern(emailText)) {
                        email.setError("Enter a Valid Email Address");
                    }
                }
                else
                {
                    if(checkPhonePattern(phoneText))
                    {
                        signInWithPhone(phoneText);

                    }
                }



            }
        });
        fbLogin.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d( "facebook:onSuccess:", loginResult.toString());
                Profile currentFbProfile=Profile.getCurrentProfile();
                handleFacebookAccessToken(loginResult.getAccessToken(),currentFbProfile);
            }

            @Override
            public void onCancel() {
                Log.e(TAG,"Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG,error.toString());

            }
        });
        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Signing with Google");
                signInWithGoogle();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.e(TAG,"Siging with google successful");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                }
            }
        }

    public void signInWithGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void handleFacebookAccessToken(final AccessToken token, final Profile currentProfile) {
        Log.d("handle", token.toString());
        Log.e(TAG,"Loginning with firrbase from facebook");
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(currentProfile!=null) {
                                Log.e(TAG, "sign in successful adding user to database");
                                User user = new User(""
                                        , currentProfile.getName()
                                        , currentProfile.getId()
                                        , currentProfile.getProfilePictureUri(100, 100).toString()
                                        , ""
                                        , "true"
                                        , ""
                                        , "");
                                firebaseDatabase.
                                        getReference("Users").
                                        child(firebaseAuth.getCurrentUser().getUid()).
                                        setValue(user);
                            }
                            else
                            {
                                Log.e(TAG, "sign in successful adding user to database");
                                User user = new User(""
                                        , ""
                                        , firebaseAuth.getCurrentUser().getUid()
                                        , ""
                                        , ""
                                        , "true"
                                        , ""
                                        , "");
                                firebaseDatabase.
                                        getReference("Users").
                                        child(firebaseAuth.getCurrentUser().getUid()).
                                        setValue(user);
                            }
                            Log.e(TAG,"Starting Activity");
                            startActivity(new Intent(MainActivity.this,NavigationDrawer.class));
                            Log.e(TAG,"Facebook dialog");
                            shareDialog.show(shareLinkContent);
                        } else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Failure not looged in");
                Log.e(TAG,e.toString());

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
                            Log.e(TAG,"Sign in with google firebase succesful");
                            User user=new User(acct.getEmail()
                            ,acct.getDisplayName()
                            ,acct.getId()
                            ,acct.getPhotoUrl().toString()
                            ,""
                            ,"true"
                            ,""
                            ,"");
                            firebaseDatabase
                                    .getReference("Users")
                                    .child(firebaseAuth.getCurrentUser().getUid())
                                    .setValue(user);
                            startActivity(new Intent(MainActivity.this,NavigationDrawer.class));
                            Log.e(TAG,"opening plushare");
                            Intent shareIntent = new PlusShare.Builder(getApplicationContext())
                                    .setType("text/plain")
                                    .setText("I am Using This awesome App"+getResources().getString(R.string.app_name))
                                    .setContentUrl(Uri.parse("https://internship2-4d772.firebaseapp.com/"))
                                    .getIntent();
                            Log.e(TAG,"starting activity");
                            startActivityForResult(shareIntent, 0);
                        } else {
                        }
                    }
                });
    }
    public boolean checkEmailPattern(String email)
    {
        String emailRegex="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        boolean value=Pattern.matches(emailRegex,email);
        return value ;
    }
    public boolean checkPhonePattern(String Phone)
    {
        String phoneRegex="[789]{1}[1234567890]{9}";
        boolean value1=Pattern.matches(phoneRegex,Phone);
        return value1;
    }
    public void signInWithPhone(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
}
