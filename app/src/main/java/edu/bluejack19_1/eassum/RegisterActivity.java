package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.bluejack19_1.tpa_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail,edtPassword,edtUsername,edtConfPassword,edtSemester;
    private Button btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        edtUsername = findViewById(R.id.userName);
        edtConfPassword = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btn_sign_up);
        edtSemester = findViewById(R.id.semester);
        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = edtEmail.getText().toString().trim();
                String userName = edtUsername.getText().toString().trim();
                String passwordUser = edtPassword.getText().toString().trim();
                String confPassword = edtConfPassword.getText().toString().trim();
                String semester = edtSemester.getText().toString().trim();

                if (emailUser.isEmpty()){
                    edtEmail.setError("Email cannot be empty!");
                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()){
                    edtEmail.setError("Invalid Email");
                }

                else if(userName.isEmpty()){
                    edtUsername.setError("Username cannot be empty!");
                }
                else if (userName.length() < 5 || userName.length() > 20){
                    edtUsername.setError("Username must be between 5 - 20!");
                }

                else if(!(semester.equals("1") || semester.equals("2") || semester.equals("3") || semester.equals("4") || semester.equals("5") )){
                    edtSemester.setError("Semester must be between 1 - 3!");
                }

                else if (passwordUser.isEmpty()){
                    edtPassword.setError("Password cannot be empty!");
                }

                else if (passwordUser.length() < 6){
                    edtPassword.setError("Password must be more than 6 characters!");
                }

                else if(!confPassword.equals(passwordUser)){
                    edtConfPassword.setError("Must be the same as Password!");
                }
                else {

                    auth.createUserWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Register gagal karena " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    });

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    String key = ref.child("User").push().getKey();
                    ref.child("User").child(key).child("email").setValue(emailUser);
                    ref.child("User").child(key).child("password").setValue(passwordUser);
                    ref.child("User").child(key).child("username").setValue(userName);
                    ref.child("User").child(key).child("semester").setValue(semester);
                    ref.child("User").child(key).child("type").setValue(0);
                    ref.child("User").child(key).child("gpa").setValue(3.5);
                }
            }
        });
    }
}