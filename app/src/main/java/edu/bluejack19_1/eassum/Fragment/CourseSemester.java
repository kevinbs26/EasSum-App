package edu.bluejack19_1.eassum.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import edu.bluejack19_1.eassum.AdminActivity;
import edu.bluejack19_1.tpa_mobile.R;
import edu.bluejack19_1.eassum.adapter.ListButtonAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.bluejack19_1.eassum.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseSemester extends Fragment {

    ArrayList<String> list = new ArrayList<>();
    Integer semester;

    public CourseSemester() {
        // Required empty public constructor

    }

    public CourseSemester(int semester){
        this.semester = semester;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_semester, container, false);

        TextView titleTV = view.findViewById(R.id.titleSemester);

        titleTV.setText("Semester " + semester);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("semester").child(semester.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());
                    Log.d("FIREBASE", "onDataChange: " + postSnapshot.getKey().toString());
                }

                Context context = getContext();
                ListView listview = view.findViewById(R.id.list_view_semester);
                listview.setAdapter(new ListButtonAdapter(list, context , CourseSemester.this , semester) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button adminBtn = view.findViewById(R.id.button_admin);

        if(MainActivity.userAdmin == 0){
            adminBtn.setVisibility(View.INVISIBLE);
        }

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity() , AdminActivity.class);
                i.putExtra("semester", semester);
                startActivity(i);
            }
        });

    }
}
