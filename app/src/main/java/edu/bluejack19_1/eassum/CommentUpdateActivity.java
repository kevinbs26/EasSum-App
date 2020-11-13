package edu.bluejack19_1.eassum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.bluejack19_1.tpa_mobile.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentUpdateActivity extends AppCompatActivity {

    String comment;
    String name;
    String key;
    Integer semester;
    String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_update);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            comment = extras.getString("comment");
            key = extras.getString("key");
            name = extras.getString("name");
            semester = extras.getInt("semester");
            course = extras.getString("course");
        }

        final EditText updatetxt = findViewById(R.id.editText_update_comment_course);
        updatetxt.setText(comment);

        Button updateBtn = findViewById(R.id.button_update_comment_course);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedComment = updatetxt.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("semester").child(semester.toString()).child(course).child("comment").child(name).child(key).setValue(updatedComment);

                finish();
            }
        });

    }
}
