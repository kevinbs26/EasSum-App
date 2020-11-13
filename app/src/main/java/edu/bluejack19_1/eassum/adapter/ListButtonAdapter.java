package edu.bluejack19_1.eassum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import edu.bluejack19_1.eassum.Fragment.CourseSemester;
import edu.bluejack19_1.eassum.Fragment.FragmentSummary;
import edu.bluejack19_1.tpa_mobile.R;
import edu.bluejack19_1.eassum.fragmentinterface.FragmentChangeListener;

import java.util.ArrayList;

public class ListButtonAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private CourseSemester courseSemester;
    private int semester;

    public ListButtonAdapter(ArrayList<String> tempListFeedback, Context context, CourseSemester courseSemester , int semester) {
        this.list = tempListFeedback;
        this.context = context;
        this.courseSemester = courseSemester;
        this.semester = semester;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
        //just return 0 if your list items do not have an Id variable.
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_course_fragment, null);
        }

        final Button callbtn= (Button)v.findViewById(R.id.list_btn);
        callbtn.setText(list.get(i));

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String course = callbtn.getText().toString();
                FragmentChangeListener listener = (FragmentChangeListener) courseSemester.getActivity();
                listener.changeFragment(new FragmentSummary(course , semester));
            }
        });

        return v;
    }
}
