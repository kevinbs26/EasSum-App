package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import edu.bluejack19_1.eassum.Model.User;

import edu.bluejack19_1.tpa_mobile.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    Button loginButton,registerButton;
    EditText emails, passwords;
    DatabaseReference ref;
    CheckBox check;
    SignInButton googleSignIn;
    FirebaseAuth auth;
    GoogleSignInClient ggs;
    LoginButton facebookButton;

    private CallbackManager callbackManager;

    void initView(){
        loginButton = findViewById(R.id.loginBtn);
        registerButton = findViewById(R.id.regisBtn);
        emails = findViewById(R.id.emailTxt);
        passwords = findViewById(R.id.passTxt);
        ref = FirebaseDatabase.getInstance().getReference();
        check = findViewById(R.id.checkBox);
        googleSignIn = findViewById(R.id.googleButton);
        auth = FirebaseAuth.getInstance();
        facebookButton = findViewById(R.id.facebookButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("FIREBASE", "token "+ FirebaseInstanceId.getInstance().getToken());

        SharedPreferences sharedPreferences = getSharedPreferences("myshared", 0);
        String email = sharedPreferences.getString("email", "");
        if(!email.equals("")) {
            User.setUser( sharedPreferences.getString("name", "") , sharedPreferences.getInt("semester", 1), sharedPreferences.getInt("type", 1) , sharedPreferences.getString("key",""));

            User.addFCMTokenFromService();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
        initView();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cat","Hello");

                ref.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> listEmail = new ArrayList<>();
                        ArrayList<String> listPass = new ArrayList<>();
                        ArrayList<Integer> listSemester = new ArrayList<>();
                        ArrayList<Integer> listType = new ArrayList<>();
                        ArrayList<String> listName = new ArrayList<>();
                        ArrayList<String> listKey = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String email = ds.child("email").getValue(String.class);
                            String password = ds.child("password").getValue(String.class);
                            int semester = Integer.parseInt(ds.child("semester").getValue().toString());
                            int type = Integer.parseInt(ds.child("type").getValue().toString());
                            String name = ds.child("username").getValue().toString();
                            String key = ds.getKey().toString();

                            listEmail.add(email);
                            listPass.add(password);
                            listSemester.add(semester);
                            listType.add(type);
                            listName.add(name);
                            listKey.add(key);

                        }

                        for(int i = 0 ; i < listEmail.size() ; i++)  {
                            String emailTxt = emails.getText().toString();
                            String passTxt = passwords.getText().toString();

                            if(!emailTxt.equals(listEmail.get(i))){
                                emails.setError("Invalid email!");
                            }else if(emailTxt.equals("")){
                                emails.setError("Email cannot be empty!");
                            }else if(!passTxt.equals(listPass.get(i))) {
                                passwords.setError("Incorrect password!");
                            }else if(passTxt.equals("")){
                                passwords.setError("Password cannot be empty!");
                            } else {
                                int semester = listSemester.get(i);
                                int type = listType.get(i);
                                String name = listName.get(i).toString();
                                String key = listKey.get(i);

                                if (check.isChecked()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("myshared", 0);
                                    sharedPreferences.edit().putString("email", emailTxt).apply();
                                    sharedPreferences.edit().putString("name", name).apply();
                                    sharedPreferences.edit().putInt("semester", semester).apply();
                                    sharedPreferences.edit().putInt("type", semester).apply();
                                    sharedPreferences.edit().putString("key", key).apply();
                                }

                                User.setUser(name, semester, type, key);
                                Log.d("yb", "onDataChange:hALO ");
                                Intent x = new Intent(LoginActivity.this, MainActivity.class);
                                User.addFCMTokenFromService();
                                startActivity(x);
                                finish();
                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        ggs = GoogleSignIn.getClient(this , gso);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ggs.getSignInIntent();
                startActivityForResult(i, 101);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        facebookButton.setReadPermissions(Arrays.asList("email","public_profile"));

        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if(isLoggedIn){
                    Toast.makeText(LoginActivity.this, "Successfully Login!", Toast.LENGTH_SHORT).show();
                    loaduserProfile(accessToken);

                }

            }

            public void onCancel() {
                // App code
            }

            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Toast.makeText(this, "Successfully Login!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d("TAG", "Not logged in");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode , data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                try {
                    Log.d("CODCOD", "TEST");
                    Intent i  = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = task.getResult(ApiException.class);

//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//                    String key = ref.child("User").push().getKey();
//                    ref.child("User").child(key).child("email").setValue(email);
//                    ref.child("User").child(key).child("username").setValue(first_name);
//                    ref.child("User").child(key).child("semester").setValue(1);
//                    ref.child("User").child(key).child("type").setValue(0);
//
//                    User.setUser(first_name , 1 , 0 , key);

                } catch (ApiException e) {
                    Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
                }
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

        }
    };

    private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    String key = ref.child("User").push().getKey();
                    ref.child("User").child(key).child("email").setValue(email);
                    ref.child("User").child(key).child("username").setValue(first_name);
                    ref.child("User").child(key).child("semester").setValue(1);
                    ref.child("User").child(key).child("type").setValue(0);
                    ref.child("User").child(key).child("gpa").setValue(3.5);

                    User.setUser(first_name , 1 , 0 , key);
                    Toast.makeText(LoginActivity.this, "Successfully Login!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields" , "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

}