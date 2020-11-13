package edu.bluejack19_1.eassum.Fragment;



import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import edu.bluejack19_1.tpa_mobile.R;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

public class CalendarFragment extends Fragment {
    TextView tes;

    public CalendarFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CollapsibleCalendar collapsibleCalendar = view.findViewById(R.id.calendarView);
        tes = view.findViewById(R.id.text);
        collapsibleCalendar.addEventTag(2019,11,24,Color.RED);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDayChanged() {

            }

            @Override
            public void onClickListener() {

            }
            @Override
            public void onDaySelect() {

                if(collapsibleCalendar.getSelectedDay().getDay() == 24 && collapsibleCalendar.getSelectedDay().getMonth() == 11 && collapsibleCalendar.getSelectedDay().getYear() == 2019  ){
                    tes.setText("Christmas Eve\n");
                    tes.append("Kebangkitan Joko Sentosa\n");
                }else {
                    tes.setText("No Events");
                }
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });
    }
}
