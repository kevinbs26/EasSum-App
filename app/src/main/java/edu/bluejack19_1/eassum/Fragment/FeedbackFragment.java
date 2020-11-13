package edu.bluejack19_1.eassum.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.bluejack19_1.tpa_mobile.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        final EditText feedbacktxt = view.findViewById(R.id.editText_feedback);

        Button submitFeedback = view.findViewById(R.id.feedbackBtn);
        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = feedbacktxt.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Feedback").push().setValue(feedback);

                Toast.makeText(getActivity() , "Feedback Submitted" , Toast.LENGTH_SHORT).show();
                feedbacktxt.setText("");
            }
        });

        return view;
    }

}
