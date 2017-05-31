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
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class AddFriendsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Button signOutButton;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private TextView requestComeFrom;
    private TextView message;
    private Button sendButton;
    private Button acceptButton;
    private Button declineButton;
    private String emailInput;
    private EditText emailEdit;
    private TextView requestResult;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "AddFriendsActivity";

    private String currUserEmail;

    private String currKey;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String[] arr = {};

    FirebaseOptions options;
    DatabaseReference myRef;
    User user;   //if this doesn;t work include user in global variables
    Request request;

    FirebaseApp firebaseApp;

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
        message = (TextView) findViewById(R.id.friendFrom);

        requestResult = (TextView) findViewById(R.id.sendResult);

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "request sent/clicked");
                emailInput = replaceData(getEditTextString(emailEdit));

                if(User.checkAnyUser(emailInput, myRef)) {
                    request = new Request(currUserEmail, emailInput, myRef);
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
                Friends friends = new Friends(currUserEmail, user.friendList.get(0), myRef); //add both users to each other's friends
                friends.addElement();

               handleRequestDisplay(true);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRequestDisplay(true);
            }
        });

        options = new FirebaseOptions.Builder()
                .setApplicationId("1:1092866304173:android:4b7ec0d493ab8bad")
                .setDatabaseUrl("https://dejaphoto-33.firebaseio.com/")
                .build();

        emailEdit = (EditText) findViewById(R.id.currEmail);

        //FirebaseApp firebaseApp;

        /*try{
            firebaseApp = FirebaseApp.getInstance("dejaphoto-33");
        } catch(IllegalStateException e){
            firebaseApp = FirebaseApp.initializeApp(this, options);
        }*/


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

        //currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
            signIn();

        authentication();

        myRef.child("users").child(currUserEmail).child("requests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //TODO handle request
                currKey = user.getRef(arr).child("requets").getKey();

                // add email address into array list that stores requests
                user.friendList.add(currKey);

                // delete email address from request
                Request.clearRequest(currKey, currUserEmail, myRef);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        updateUI(currentUser);
    }

    private void authentication() {
        Log.d(TAG, "authentication() called");

        if(mAuth != null && mAuth.getCurrentUser() != null) {
            currUserEmail = mAuth.getCurrentUser().getEmail().replace(".", ","); //no periods, only commas
        }
        else
            currUserEmail  = "No User";
    }


    private void signIn() {
        Log.d(TAG, "signIn() called");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            userManager(); //add user to datbase if needed
                        } else {
                            // If sign in fails, display a message to the user.
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

    private void updateUI(boolean signedIn) {
        if(signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            emailEdit.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
        }
        else {
            mStatusTextView.setText(R.string.signed_out);
            user = null;

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            emailEdit.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
        }
    }

    private void updateUI(FirebaseUser fUser) {
        //hideProgressDialog();

        if (fUser != null) {
            user = new User(fUser.getDisplayName(), fUser.getEmail(), myRef); //new user object
            requestComeFrom = (TextView) findViewById(R.id.friendFrom);

            mStatusTextView.setText(getString(R.string.google_status_fmt, fUser.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, fUser.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            emailEdit.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);

            handleRequestDisplay(false);

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            user = null;

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

    //HELPER METHODS
    public String replaceData(String input){
       if(input != null)
        return input.replace(".", ",");

       else return null;

    }

    public String getEditTextString(EditText input){  //extract string from edit text
       if(input.getText() != null)
        return input.getText().toString();

       else
           return null;
    }

    public void displayNoRequests(){ //called when list empty
        requestComeFrom.setText("");
        acceptButton.setVisibility(View.INVISIBLE);
        declineButton.setVisibility(View.INVISIBLE);
    }

    public void displayNextRequest(){ //called when list not empty
        requestComeFrom.setText(user.friendList.get(0));

    }

    public void handleRequestDisplay(boolean click){
        if(!user.friendList.isEmpty()) {
            if(click) removeRequest();
            displayNextRequest();
        }
        else displayNoRequests();  //no requests
    }

    public void removeRequest(){
        user.friendList.remove(0);

        if(user.friendList.size() == 0)
            displayNoRequests();

    }

    public void userManager(){
        FirebaseUser currUser = mAuth.getCurrentUser();
        Log.d(TAG, "currUser: "+ currUser);

        if(currUser != null){
            Log.d(TAG, user.toString());

            boolean exists = user.checkExist(user.email); //check to see if user exists

            if(!exists) {
                user.addElement();
                Log.d(TAG, "adding new user @ signIn()");
            }
            else Log.i(TAG, "user already in database");
        }
        else Log.i(TAG, "no user exists");
    }
}
