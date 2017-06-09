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


/* Class: AddFriendsActivity
 * Function: This class will add the friends into the firebase for each app user
 *           and take care of any friends requests, so that user can accept or
 *           decline the request
 */
public class AddFriendsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    // Variable declaration
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


    /* Method: onCreate
     * Param: Bundle savedInstance
     * Function: This method will display the main layout of when the app starts
     *           and set sign in button's listeners to make action
     * Return: null
     */
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

        // Print out message when signed in
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        // Print out message when there are requests
        requestResult = (TextView) findViewById(R.id.sendResult);
        requestComeFrom = (TextView) findViewById(R.id.friendFrom);
        showFriends = (TextView) findViewById(R.id.friendEmail);

        // set button listeners
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

        // Customized email input to add friends
        emailEdit = (EditText) findViewById(R.id.currEmail);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "request sent/clicked");
                emailInputRequest = replaceData(getEditTextString(emailEdit));
                Log.d(TAG, "emailInput: " + emailInputRequest);

                if (Global.currUser.checkExist(emailInputRequest)) {
                    request = new Request(Global.currUser.email, emailInputRequest, myRef);
                    request.addElement();
                    //todo print success message
                    requestResult.setText("Send successful");

                } else {
                    //todo print error message
                    requestResult.setText("User does not exists, try another");
                }
            }
        });

        // button listeners to take care of handling friends requests
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


        // Connect our app with the firebase address
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

        // Set the authentication rule for our database
        mAuth = FirebaseAuth.getInstance();

        // sign in button listener
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


    /* Method: onStart
     * Param: none
     * Function: this method will update user interface and set a
     *           database listener when new user's emails are added
     * Return: none
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if Global.currUser is signed in (non-null) and update UI accordingly.
        User.setDatabaseListener(myRef.child("users"));
        PhotoStorage.setDatabaseListener(myRef.child("photos"));

        if (Global.currUser != null) {
            Request.setRequestListener(myRef.child("users").child(Global.currUser.email).child("requests")); //set listener to curr Global.currUser requests

            updateListeners();
        }

        // Get the current user authenticated
        firebaseUser = mAuth.getCurrentUser();

        // sign in
        if (firebaseUser == null)
            signIn();

        updateUI(firebaseUser);
    }

    /* Method: signIn
     * Param: none
     * Function: create a new intent to sign in google account
     * return: none
     */
    private void signIn() {
        Log.d(TAG, "signIn() called");

        // Connect google account
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /* Method: signOut
     * Param: none
     * Function: sign out both firebase and google account
     * Return: none
     */
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

    /* Method: onActivityResult
     * Param: int requestCode, int resultCode, Intent data
     * Function: this method will handle the intent and to check if
     *           the user email is valid and then sign in the user with
     *           his gmail
     * Return: none
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // update UI
                updateUI(null);
            }
            handleSignInResult(result);
        }
    }

    /* Method: firebaseAuthWithGoogle
     * Param: GoogleInAccount acct
     * Function: Connect firebase account with google account
     * Return: none
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        // Create credential for current user
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

    /* Method: handleSignInResult
     * Param: GoogleSignInResult result
     * Function: handles the firebase sign in with gmail and prints out
     *           message if succeed
     * Return: none
     */
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

    /* Method: updateUi
     * Param: boolean signedIn
     * Function: update user interface based on siged in with google account on app
     * Return: none
     */
    public void updateUI(boolean signedIn) {
        // Set sign out button visible
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            emailEdit.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
        } else {
            // prints out message and make sign in visible
            mStatusTextView.setText(R.string.signed_out);
            Global.currUser = null;

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            emailEdit.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
        }
    }

    /* Method: updateUi
    * Param: Firebase fUser
    * Function: update user interface based on siged in with google account on firebase
    * Return: none
    */
    private void updateUI(FirebaseUser fUser) {
        //hideProgressDialog();

        if (fUser != null) {
            Global.currUser = new User(fUser.getDisplayName(), fUser.getEmail(), myRef); //new currUser object

            // mainly update UI on app
            mStatusTextView.setText(getString(R.string.google_status_fmt, fUser.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, fUser.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            emailEdit.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);

            User.setDatabaseListener(myRef.child("users"));
            PhotoStorage.setDatabaseListener(myRef.child("photos"));
            Request.setRequestListener(myRef.child("users").child(Global.currUser.email).child("requests")); //set listener to curr currUser requests

            // take a snapshot for any user change on firebase
            updateListeners();


        } else {
            // mainly update UI on App
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            Global.currUser = null;

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            emailEdit.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
        }
    }

    /* Method: onConnectionFailed
     * Param: ConnectionResult connectionResult
     * Function: listens to if the sign in process is cancelled
     * Return: none
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /* Method: onClick
     * Param: View v
     * Function: none
     * Return: none
     */
    @Override
    public void onClick(View v) {
    }

    //HELPER METHODS---------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------
    /* Method: replaceData
     * Param: String input
     * Function: replace illegal characters on firebase with others
     * Return: String
     */
    public String replaceData(String input) {
        if (input != null)
            return input.replace(".", ",");

        else return null;
    }

    /* Method: getEditTextString
     * Param: EditText input
     * Purpose: extract string from edit text
     * Return: String
     */
    public String getEditTextString(EditText input) {  //extract string from edit text
        if (input.getText() != null)
            return input.getText().toString();

        else
            return null;
    }

    /* Method: userManage
     * Param: none
     * Purpose: to check if the user exists already on firebase
     * Return: none
     */
    public void userManager() {
        FirebaseUser currUser = mAuth.getCurrentUser();
        Log.d(TAG, "currUser: " + currUser);

        if (Global.currUser != null) {
            Log.d(TAG, Global.currUser.toString());

            boolean exists = Global.currUser.checkExist(Global.currUser.email); //check to see if currUser exists

            if (!exists) {
                // Add current user if not exists on firebase
                Global.currUser.addElement();
                Log.d(TAG, "adding new currUser @ signIn()");
            } else Log.i(TAG, "currUser already in database");
        } else Log.i(TAG, "no currUser exists");
    }

    /* Method: requestView
     * Param: boolean visible
     * Purpose: update ui to notice user all friends requests
     * Return: none
     */
    public static void requestView(boolean visible) {
        if (visible) {
            acceptButton.setVisibility(View.VISIBLE);
            declineButton.setVisibility(View.VISIBLE);
            requestComeFrom.setText("Friend Request From");
            showFriends.setText(nextRequest);
        } else {
            acceptButton.setVisibility(View.INVISIBLE);
            declineButton.setVisibility(View.INVISIBLE);
            requestComeFrom.setText("");
            showFriends.setText("");
        }
    }

    /* Method: handleClick
     * Param: boolean accept
     * Purpose: handle friend acceptance and decline
     * Return: none
     */
    public void handleClick(boolean accept) {
        if (accept) {
            Friends friends = new Friends(Global.currUser.email, Global.currUser.requestList.get(0), myRef); //add both users to each other's friends
            friends.addElement();
        } //add friend to friends list
        //else do nothing

        Request.clearRequest(Global.currUser.email, Global.currUser.requestList.get(0), myRef);
        Global.currUser.requestList.remove(0);
        //delete request that was handled
    }

    /* Method: updateListeners
     * Param: none
     * Purpose: actual change on data change
     * Return: none
     */
    private void updateListeners() {
        myRef.child("users").child("user@gmail,com").setValue(true); //update user snapshot

        myRef.child("photos").child("user@gmail,com").child("update").setValue(true); //update photo snapshot
        myRef.child("photos").child("user@gmail,com").child("update").removeValue(); //update photo snapshot

        myRef.child("users").child(Global.currUser.email).child("requests").child("new");
        myRef.child("users").child(Global.currUser.email).child("requests").child("new").removeValue();
    }
}
