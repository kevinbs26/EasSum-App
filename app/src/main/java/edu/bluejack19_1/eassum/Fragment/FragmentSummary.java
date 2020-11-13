package edu.bluejack19_1.eassum.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.bluejack19_1.eassum.CommentActivity;
import edu.bluejack19_1.tpa_mobile.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSummary extends Fragment {

    private String course;
    private Integer semester;

    public FragmentSummary(String course , int semester) {
        // Required empty public constructor
        this.course = course;
        this.semester = semester;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        TextView titletxt = view.findViewById(R.id.textview_summary_title);
        final TextView summarytxt = view.findViewById(R.id.textview_summary);

        titletxt.setText(course.toUpperCase());
        summarytxt.setText("this place is for summary");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("semester").child(semester.toString()).child(course).child("summary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String summary = dataSnapshot.getValue().toString();
                    summarytxt.setText(summary);
                    Log.d("semester", "onDataChange: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button commentBtn = view.findViewById(R.id.button_comment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getActivity(), CommentActivity.class);
                x.putExtra("semester", semester);
                x.putExtra("course" , course);
                startActivity(x);
            }
        });



        return view;
    }

}
