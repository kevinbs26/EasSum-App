package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.bluejack19_1.eassum.Model.MyFirebaseMessagingService;
import edu.bluejack19_1.eassum.Model.User;

import edu.bluejack19_1.tpa_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.InstanceIdResult;

public class UserUpdateActivity extends AppCompatActivity {

    String key = User.getInstanceUserKey();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser fbUser = mAuth.getCurrentUser();
        
        String username = "";
        String email = "";
        Integer semester = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            email = extras.getString("email");
            semester = extras.getInt("semester");
        }

        String Ssemester = semester.toString();

        final EditText usernametxt = findViewById(R.id.edittext_username);
        final EditText emailtxt = findViewById(R.id.edittext_email);
        final EditText semestertxt = findViewById(R.id.edittext_semester);
        final EditText passwordtxt = findViewById(R.id.edittext_password);

        usernametxt.setText(username);
        emailtxt.setText(email);
        semestertxt.setText(Ssemester);

        Button savebtn = findViewById(R.id.button_user_save);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = usernametxt.getText().toString();
                String newEmail = emailtxt.getText().toString();
                String newSemester = semestertxt.getText().toString();
                String newPassword = passwordtxt.getText().toString();



                if (newUsername.isEmpty()){
                    usernametxt.setError("Email cannot be empty!");
                }
                else if (newUsername.length() < 5 || newUsername.length() > 20){
                    usernametxt.setError("Username must be between 5 - 20!");
                }
                else if (newEmail.isEmpty()){
                    emailtxt.setError("Email cannot be empty!");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                    emailtxt.setError("Invalid Email");
                }
                else if(!(newSemester.equals("1") || newSemester.equals("2") || newSemester.equals("3") || newSemester.equals("4") || newSemester.equals("5") )){
                    semestertxt.setError("Semester must be between 1 - 5!");
                }
                else if (newPassword.isEmpty()){
                    passwordtxt.setError("Password cannot be empty!");
                }
                else if (newPassword.length() < 6){
                    passwordtxt.setError("Password must be more than 6 characters!");
                }
                else{
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("User").child(key).child("email").setValue(newEmail);
                    ref.child("User").child(key).child("username").setValue(newUsername);
                    ref.child("User").child(key).child("semester").setValue(newSemester);
                    ref.child("User").child(key).child("password").setValue(newPassword);

                    MyFirebaseMessagingService.getToken(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            String token = task.getResult().getToken();
                            MyFirebaseMessagingService.sendMessage(getApplicationContext(), token, "Update Profile", "Your Profile has been updated!");
                        }
                    });

                    Toast.makeText(UserUpdateActivity.this, "Success" , Toast.LENGTH_SHORT).show();
                    User.setUser(newUsername , Integer.parseInt(newSemester)  , User.getInstanceUserType() , key);
                    finish();
                }
            }

        });

    }
}
