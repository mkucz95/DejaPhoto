package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dejaphoto.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddFriendsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Button signOutButton;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private static TextView showFriends;
    private static TextView requestComeFrom;
    private Button sendButton;
    private static Button acceptButton;
    private static Button declineButton;
    private String emailInputRequest;
    private EditText emailEdit;
    private TextView requestResult;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "AddFriendsActivity";
    public static String nextRequest;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private String[] arr = {};

    FirebaseOptions options;
    DatabaseReference myRef;

    Request request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set the dimensions of the sign-in button.
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signOutButton = (Button) findViewById(R.id.sign_out_and_disconnect);

        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        requestResult = (TextView) findViewById(R.id.sendResult);
        requestComeFrom = (TextView) findViewById(R.id.friendFrom);
        showFriends =(TextView) findViewById(R.id.friendEmail);

        sendButton = (Button) findViewById(R.id.bt_8);

        acceptButton = (Button) findViewById(R.id.bt_9);
        declineButton = (Button) findViewById(R.id.bt_10);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendButton.setVisibility(View.INVISIBLE);
                emailEdit.setVisibility(View.INVISIBLE);
                signOut();
            }
        });

        emailEdit = (EditText) findViewById(R.id.currEmail);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "request sent/clicked");
                emailInputRequest = replaceData(getEditTextString(emailEdit));
                Log.d(TAG, "emailInput: "+emailInputRequest);

                if(Global.currUser.checkExist(emailInputRequest)) {
                    request = new Request(Global.currUser.email, emailInputRequest, myRef);
                    request.addElement();
                    //todo print success message
                    requestResult.setText("Send successful");

                }
                else{
                    //todo print error message
                    requestResult.setText("User does not exists, try another");
                }
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(true);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(false);
            }
        });

        options = new FirebaseOptions.Builder()
                .setApplicationId("1:1092866304173:android:4b7ec0d493ab8bad")
                .setDatabaseUrl("https://dejaphoto-33.firebaseio.com/")
                .build();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReferenceFromUrl("https://dejaphoto-33.firebaseio.com/");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;

                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if Global.currUser is signed in (non-null) and update UI accordingly.
        User.setDatabaseListener(myRef.child("users"));
        if(Global.currUser != null){
            Request.setRequestListener(myRef.child("users").child(Global.currUser.email).child("requests")); //set listener to curr Global.currUser requests
            myRef.child("users").child(Global.currUser.email).child("requests").child("new");
            myRef.child("users").child(Global.currUser.email).child("requests").child("new").removeValue();
        }

        myRef.child("users").child("user@gmail,com").setValue(true); //update snapshot


        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null)
            signIn();

        updateUI(firebaseUser);
    }

    private void signIn() {
        Log.d(TAG, "signIn() called");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        requestView(false);
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
            }
            handleSignInResult(result);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in currUser's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            userManager(); //add currUser to datbase if needed
                        } else {
                            // If sign in fails, display a message to the currUser.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                              //      Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    public void updateUI(boolean signedIn) {
        if(signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            emailEdit.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
        }
        else {
            mStatusTextView.setText(R.string.signed_out);
            Global.currUser = null;

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            emailEdit.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
        }
    }

    private void updateUI(FirebaseUser fUser) {
        //hideProgressDialog();

        if (fUser != null) {
            Global.currUser = new User(fUser.getDisplayName(), fUser.getEmail(), myRef); //new currUser object

            mStatusTextView.setText(getString(R.string.google_status_fmt, fUser.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, fUser.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            emailEdit.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);

            User.setDatabaseListener(myRef.child("users"));
            Request.setRequestListener( myRef.child("users").child(Global.currUser.email).child("requests")); //set listener to curr currUser requests

            myRef.child("users").child(Global.currUser.email).child("requests").child("new");
            myRef.child("users").child(Global.currUser.email).child("requests").child("new").removeValue();


            myRef.child("users").child("currUser@gmail,com").setValue(true); //update snapshot

            //SetRequestListener

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            Global.currUser = null;

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            emailEdit.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
    }

    //HELPER METHODS---------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------
    public String replaceData(String input){
       if(input != null)
        return input.replace(".", ",");

       else return null;
    }

    public  String getEditTextString(EditText input){  //extract string from edit text
       if(input.getText() != null)
        return input.getText().toString();

       else
           return null;
    }

    public void userManager(){
        FirebaseUser currUser = mAuth.getCurrentUser();
        Log.d(TAG, "currUser: "+ currUser);

        if(Global.currUser != null){
            Log.d(TAG, Global.currUser.toString());

            boolean exists =  Global.currUser.checkExist(Global.currUser.email); //check to see if currUser exists

            if(!exists) {
                Global.currUser.addElement();
                Log.d(TAG, "adding new currUser @ signIn()");
            }
            else Log.i(TAG, "currUser already in database");
        }
        else Log.i(TAG, "no currUser exists");
    }

    public static void requestView(boolean visible){
        if(visible){
            acceptButton.setVisibility(View.VISIBLE);
            declineButton.setVisibility(View.VISIBLE);
            requestComeFrom.setText("Friend Request From");
            showFriends.setText(nextRequest);
        }

        else{
            acceptButton.setVisibility(View.INVISIBLE);
            declineButton.setVisibility(View.INVISIBLE);
            requestComeFrom.setText("");
            showFriends.setText("");
        }
    }

    public  void  handleClick(boolean accept){
        if (accept) {
            Friends friends = new Friends(Global.currUser.email, Global.currUser.requestList.get(0), myRef); //add both users to each other's friends
            friends.addElement();
        } //add friend to friends list
        //else do nothing

        Request.clearRequest(Global.currUser.email, Global.currUser.requestList.get(0), myRef);
        Global.currUser.requestList.remove(0);
        //delete request that was handled
    }
}
