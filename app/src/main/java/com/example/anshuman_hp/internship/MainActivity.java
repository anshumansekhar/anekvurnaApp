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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;
    String emailText, passwordText, phoneText;
    CallbackManager callbackManager;
    ShareLinkContent shareLinkContent;
    ShareDialog shareDialog;
    GoogleApiClient mGoogleApiClient;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    public static final String TAG = "MainActivity";

    @Override
    protected void onStart() {
        super.onStart();
        //subscribeToPushService();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
        }
    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("AndroidBash", "Subscribed");
        Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("AndroidBash", token);
        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        Log.e(TAG, "Building sharelink");
        shareLinkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://internship2-4d772.firebaseapp.com/"))
                .setQuote("I am Using this Awesome App")
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag(getResources().getString(R.string.app_name))
                        .build())
                .build();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.e(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                if (phoneAuthCredential != null)
                    signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.e(TAG, "onCodeSent:" + s);
                final String id = s;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Verification");
                builder.setMessage("Enter the Code sent to " + phoneText);
                final EditText code = new EditText(getApplicationContext());
                builder.setView(code);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, code.getText().toString().trim());
                        signInWithPhoneAuthCredential(credential);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        };
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
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.mobile);
        fbLogin = (LoginButton) findViewById(R.id.fbloginButton);
        googleSignin = (SignInButton) findViewById(R.id.googlelogin);
        googleSignin.setSize(SignInButton.SIZE_STANDARD);
        submit = (Button) findViewById(R.id.Submit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Submit Clicked");
                emailText = email.getText().toString();
                passwordText = password.getText().toString();
                phoneText = phone.getText().toString();
                if (!emailText.isEmpty()) {
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
                                Log.e(TAG, e.toString());
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Confirmation");
                                builder.setMessage("The entered Emaild has not Yet been Registered.Do you want to Register now?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent f = new Intent(MainActivity.this, Registration.class);
                                        f.putExtra("PhoneAuth", false);
                                        f.putExtra("Email", emailText);
                                        f.putExtra("Password", passwordText);
                                        Log.e(TAG, "Staring Registration");
                                        startActivity(f);
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
                } else {
                    if (checkPhonePattern(phoneText)) {
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
        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Signing with Google");
                signInWithGoogle();
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().getEmail().isEmpty()) {
                                Intent i = new Intent(MainActivity.this, Registration.class);
                                i.putExtra("Mobile", phoneText);
                                i.putExtra("PhoneAuth", true);
                                startActivity(i);
                            }
                        } else
                            Log.e(TAG, "TaskFailed");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
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

    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                                                startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
                                            } else {
                                                user_profile userProfile = new user_profile(Profile.getCurrentProfile().getName()
                                                        , ""
                                                        , "true"
                                                        , "1"
                                                        , Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString()
                                                        ,""
                                                        ,""
                                                        ,"");
                                                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                                                        .child("UserProfile")
                                                        .setValue(userProfile);
                                                pushClassDetails(firebaseAuth.getCurrentUser().getUid());
                                                startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
                                                shareDialog.show(shareLinkContent);
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
                                                startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
                                            } else {
                                                user_profile userProfile = new user_profile(acct.getDisplayName()
                                                        , ""
                                                        , "true"
                                                        , "1"
                                                        , acct.getPhotoUrl().toString()
                                                        , ""
                                                        ,""
                                                        ,"");
                                                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                                                        .child("UserProfile")
                                                        .setValue(userProfile);
                                                pushClassDetails(firebaseAuth.getCurrentUser().getUid());
                                                startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
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

    public static boolean checkEmailPattern(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        boolean value = Pattern.matches(emailRegex, email);
        return value;
    }

    public static boolean checkPhonePattern(String Phone) {
        String phoneRegex = "[789]{1}[1234567890]{9}";
        boolean value1 = Pattern.matches(phoneRegex, Phone);
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

    public void pushClassDetails(String id) {
        ArrayList<ClassDetails> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            if (i != 10 && i != 11) {
                list.add(new ClassDetails());
                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("ClassDetails").setValue(list);
            } else if (i == 11) {
                HashMap<String, ClassDetails> map = new HashMap<>();
                map.put("Arts", new ClassDetails());
                map.put("Commerce", new ClassDetails());
                map.put("Science", new ClassDetails());
                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("ClassDetails")
                        .child("11")
                        .setValue(map);
            } else if (i == 10) {
                HashMap<String, ClassDetails> map = new HashMap<>();
                map.put("Arts", new ClassDetails());
                map.put("Commerce", new ClassDetails());
                map.put("Science", new ClassDetails());
                firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid())
                        .child("ClassDetails")
                        .child("10")
                        .setValue(map);
            }
        }
    }
}
