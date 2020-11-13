package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import edu.bluejack19_1.tpa_mobile.R;

import edu.bluejack19_1.eassum.adapter.ListEditAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private Integer semester;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            semester = extras.getInt("semester");
        }

        final ListView listView = findViewById(R.id.list_view_semester_admin);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("semester").child(semester.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());

                }


                listView.setAdapter(new ListEditAdapter(list , AdminActivity.this , semester));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Button insertBtn = findViewById(R.id.button_admin);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this , AdminEditActivity.class);
                i.putExtra("semester", semester);
                i.putExtra("title" , "");
                startActivity(i);
            }
        });
    }
}
