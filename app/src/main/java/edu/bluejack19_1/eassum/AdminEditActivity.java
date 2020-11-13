package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.bluejack19_1.tpa_mobile.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminEditActivity extends AppCompatActivity {

    private Integer semester;
    private String courseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

        final EditText titletxt = findViewById(R.id.editText_course_title);
        final EditText summarytxt = findViewById(R.id.editText_course_summary);
        Button save = findViewById(R.id.button_save_summary);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            semester = extras.getInt("semester");
            if(!extras.getString("title").equals("")) {
                courseTitle = extras.getString("title");
                titletxt.setText(courseTitle);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("semester").child(semester.toString()).child(courseTitle).child("summary").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String summary = dataSnapshot.getValue().toString();
                        summarytxt.setText(summary);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titletxt.getText().toString();
                String summary = summarytxt.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                ref.child("semester").child(semester.toString()).child(title).child("summary").setValue(summary).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("HAHAA", "onSuccess: " + "masuk");
                        Toast.makeText(AdminEditActivity.this , "Success" , Toast.LENGTH_SHORT).show();
                        titletxt.setText("");
                        summarytxt.setText("");
                    }
                });

                finish();

            }
        });

    }
}
