package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import edu.bluejack19_1.eassum.Model.User;

import edu.bluejack19_1.tpa_mobile.R;

import edu.bluejack19_1.eassum.adapter.ListCommentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private ArrayList<String> listName = new ArrayList<String>();
    private ArrayList<String> listKey = new ArrayList<String>();
    private ArrayList<String> list = new ArrayList<String>();
    int type;
    String name;
    Integer semester;
    String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        type = User.getInstanceUserType();
        name = User.getInstanceUserName();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            course = extras.getString("course");
            semester = extras.getInt("semester");
        }

        TextView commentTitle = findViewById(R.id.textview_comment_course);
        final EditText commenttxt = findViewById(R.id.editText_comment_course);

        commentTitle.setText(course + " comment");

        final ListView listView = findViewById(R.id.listView_Comment);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("semester").child(semester.toString()).child(course).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                listName.clear();
                listKey.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds : postSnapshot.getChildren()) {
                        list.add(postSnapshot.getKey() + " : " + ds.getValue());
                        listName.add(postSnapshot.getKey());
                        listKey.add(ds.getKey());
                    }
                }

                listView.setAdapter(new ListCommentAdapter(list, CommentActivity.this , semester , course , listName , listKey));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Button commentBtn = findViewById(R.id.button_comment_course);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commenttxt.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("semester").child(semester.toString()).child(course).child("comment").child(name).push().setValue(comment);
                commenttxt.setText("");
            }
        });

    }
}
