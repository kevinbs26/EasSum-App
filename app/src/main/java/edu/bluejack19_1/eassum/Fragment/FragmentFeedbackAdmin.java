package edu.bluejack19_1.eassum.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.bluejack19_1.tpa_mobile.R;
import edu.bluejack19_1.eassum.adapter.ListFeedbackAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFeedbackAdmin extends Fragment {


    public FragmentFeedbackAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_admin, container, false);

        final ArrayList<String> tempListFeedback = new ArrayList<String>();

        final ListView listview = view.findViewById(R.id.list_view_feedback);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Feedback").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listview.setAdapter(null);
                tempListFeedback.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    tempListFeedback.add(postSnapshot.getValue().toString());
                }

                Context context = getContext();
                listview.setAdapter(new ListFeedbackAdapter(tempListFeedback, context) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;
    }

}
