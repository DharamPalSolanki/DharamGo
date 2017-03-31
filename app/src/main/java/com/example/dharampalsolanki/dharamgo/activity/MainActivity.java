package com.example.dharampalsolanki.dharamgo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.dharampalsolanki.dharamgo.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private int RC_SIGN_IN = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    public   GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
public static  String email;
    static  MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        progressDialog = new ProgressDialog(MainActivity.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Logging in");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.setMax(100);
                break;
            // ...
        }
    }
    private void signIn() {
        progressDialog.show();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
progressDialog.dismiss();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
progressDialog.dismiss();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            email = acct.getEmail();//acct.
     String tokenId = acct.getIdToken();
            Log.d("email", ""+email);
            Log.d("name", ""+name);
            Log.d("tokenId", ""+tokenId);
            progressDialog.dismiss();
            Intent listview = new Intent(MainActivity.this, ListViewActivity.class);
            listview.putExtra("name",""+name);
            listview.putExtra("tokenId",""+tokenId);
            listview.putExtra("email", ""+email);
            startActivity(listview);
          //  mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    public boolean signOut() {

mGoogleApiClient.connect();
        boolean a= true;
        if (mGoogleApiClient.isConnected()) {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(

                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.d("status", "" + status);

                        }});
        }
        else {
            a=false;
            Log.d("yes connected", "no");
        }
        return a;
    }
    public static MainActivity getInstance(){

        return instance;
    }
    public  void onStart(){
        super.onStart();
        Log.d("onStart","onStart");
    }
    public void onResume(){
        super.onResume();
        Log.d("onResume", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPause", "onPause");
    }
    public  void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
