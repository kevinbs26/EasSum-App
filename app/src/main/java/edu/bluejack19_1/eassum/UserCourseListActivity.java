package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import edu.bluejack19_1.eassum.Model.User;

import edu.bluejack19_1.tpa_mobile.R;

import edu.bluejack19_1.eassum.adapter.ListUserCourseAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserCourseListActivity extends AppCompatActivity {

    Integer semester = User.getInstanceUserSemester();
    String name = User.getInstanceUserName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_course_list);

        final ListView lv = findViewById(R.id.listView_user_course);
        TextView title = findViewById(R.id.textView_title_user_course);
        title.setText(name + "'s course");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("semester").child(semester.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<String>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());

                }

                lv.setAdapter(new ListUserCourseAdapter(list , UserCourseListActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button back = findViewById(R.id.button_course_list_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
