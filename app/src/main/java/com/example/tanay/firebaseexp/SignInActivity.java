package com.example.tanay.firebaseexp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.Serializable;

/**
 * Created by tanay on 13/2/17.
 */
public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authenticationListener;

    private String TAG = "SingInActivity";

    private SignInButton signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("102626000701-e4qnjt0l0tn2d2uol4im7er40duh0kp0.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        authentication = FirebaseAuth.getInstance();
        authenticationListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = authentication.getCurrentUser();
                if(user != null) {

                    Intent startMainActivity = new Intent(SignInActivity.this,MainActivity.class);
                    startMainActivity.putExtra("displayName", user.getDisplayName());
                    startMainActivity.putExtra("uid",user.getUid());
                    startActivity(startMainActivity);
                }
                else
                    Toast.makeText(SignInActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            }
        };

        setContentView(R.layout.activity_sign_in);

        intiUI();
    }

    private void intiUI() {

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "sendButtonClick: ");

                signIn();

            }
        });

    }
/*
    public void sendButtonClick(View view){

        Log.d(TAG, "sendButtonClick: ");

        signIn();

    }*/

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthenticationWithGoogle(account);
            }

        }
    }

    private void firebaseAuthenticationWithGoogle(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthenticationWithGoogle: "+ account.getId());

        AuthCredential credentials = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        authentication.signInWithCredential(credentials)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Authentication falied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                });

    }

   /* private void handleSignInResult(GoogleSignInResult result) {

        if(result.isSuccess()){
            Intent startMainActivity = new Intent(this,MainActivity.class);
            startMainActivity.putExtra("user_name",account.getDisplayName());
            startMainActivity.putExtra("user_id",account.getId());
            startActivity(startMainActivity);
        }
        else
            Toast.makeText(SignInActivity.this, "Could not login", Toast.LENGTH_SHORT).show();


    }*/

    @Override
    protected void onStart() {
        super.onStart();
        authentication.addAuthStateListener(authenticationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authentication != null)
            authentication.removeAuthStateListener(authenticationListener);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
