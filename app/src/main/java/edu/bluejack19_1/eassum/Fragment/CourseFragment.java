package edu.bluejack19_1.eassum.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.bluejack19_1.eassum.Model.User;
import edu.bluejack19_1.tpa_mobile.R;
import edu.bluejack19_1.eassum.fragmentinterface.FragmentChangeListener;

import java.util.ArrayList;

public class CourseFragment extends Fragment {

    int userSemester;
    ArrayList<String> listCourse1 = new ArrayList<String>();
    ArrayList<String> listCourse2 = new ArrayList<String>();
    ArrayList<String> listCourse3 = new ArrayList<String>();
    ArrayList<String> listCourse4 = new ArrayList<String>();
    ArrayList<String> listCourse5 = new ArrayList<String>();

    Button btnSms1;
    Button btnSms2;
    Button btnSms3;
    Button btnSms4;
    Button btnSms5;

    public CourseFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        userSemester = User.getInstanceUserSemester();

        btnSms1 = (Button) view.findViewById(R.id.buttonSemester1);
        btnSms2 = (Button) view.findViewById(R.id.buttonSemester2);
        btnSms3 = (Button) view.findViewById(R.id.buttonSemester3);
        btnSms4 = (Button) view.findViewById(R.id.buttonSemester4);
        btnSms5 = (Button) view.findViewById(R.id.buttonSemester5);

        if(userSemester < 5){
            btnSms5.setVisibility(View.INVISIBLE);
        }
        if(userSemester < 4){
            btnSms4.setVisibility(View.INVISIBLE);
        }
        if(userSemester < 3){
            btnSms3.setVisibility(View.INVISIBLE);
        }
        if(userSemester < 2){
            btnSms2.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSms1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChangeListener listener = (FragmentChangeListener) CourseFragment.this.getActivity();
                listener.changeFragment(new CourseSemester(1));
            }
        });

        btnSms2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChangeListener listener = (FragmentChangeListener) CourseFragment.this.getActivity();
                listener.changeFragment(new CourseSemester(2));
            }
        });

        btnSms3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChangeListener listener = (FragmentChangeListener) CourseFragment.this.getActivity();
                listener.changeFragment(new CourseSemester(3));
            }
        });

        btnSms4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChangeListener listener = (FragmentChangeListener) CourseFragment.this.getActivity();
                listener.changeFragment(new CourseSemester(4));
            }
        });

        btnSms5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChangeListener listener = (FragmentChangeListener) CourseFragment.this.getActivity();
                listener.changeFragment(new CourseSemester(5));
            }
        });
        
    }
}
