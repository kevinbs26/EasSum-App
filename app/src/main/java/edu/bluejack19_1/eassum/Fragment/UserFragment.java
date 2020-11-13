package edu.bluejack19_1.eassum.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import edu.bluejack19_1.eassum.LoginActivity;
import edu.bluejack19_1.eassum.Model.User;
import edu.bluejack19_1.tpa_mobile.R;
import edu.bluejack19_1.eassum.UserCourseListActivity;
import edu.bluejack19_1.eassum.UserUpdateActivity;
import com.facebook.login.LoginManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {

    String key = User.getInstanceUserKey();
    DatabaseReference ref;
    TextView user,em,sems,gpa;
    LineChart line;
    LineDataSet lineDataSet;
    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = view.findViewById(R.id.namaTxt);
        em = view.findViewById(R.id.emailUserTxt);
        sems = view.findViewById(R.id.semesterUserTxt);
        gpa = view.findViewById(R.id.GPATxt);



        ref = FirebaseDatabase.getInstance().getReference("User");
        ref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String username = dataSnapshot.child("username").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                Integer semester = Integer.parseInt(dataSnapshot.child("semester").getValue().toString());
                Double GPa = Double.parseDouble(dataSnapshot.child("gpa").getValue().toString());


                user.setText(username);
                em.setText(email);
                sems.setText(semester.toString() + "");
                gpa.setText(GPa.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);


        cartesian.yAxis(0).title("Grade Point Average(GPA)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1", 3.6));
        seriesData.add(new CustomDataEntry("2",3.7 ));
        seriesData.add(new CustomDataEntry("3", 3.8));
        seriesData.add(new CustomDataEntry("4", 3.9));
        seriesData.add(new CustomDataEntry("5", 4.0));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        com.anychart.core.cartesian.series.Line series1 = cartesian.line(series1Mapping);
        series1.name( User.getInstanceUserName());
        series1.hovered().markers().enabled(true);
        series1.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5d).offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);




        Button updatebtn = view.findViewById(R.id.button_update_user);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref = FirebaseDatabase.getInstance().getReference();
                ref.child("User").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.child("username").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        Integer semester = Integer.parseInt(dataSnapshot.child("semester").getValue().toString());

                        Intent i = new Intent(getActivity() , UserUpdateActivity.class);
                        i.putExtra("username" , username);
                        i.putExtra("email" , email);
                        i.putExtra("semester" , semester);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        Button listbtn = view.findViewById(R.id.button_user_list_course);
        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity() , UserCourseListActivity.class);
                startActivity(i);
            }
        });

        Button logoutbtn = view.findViewById(R.id.button_logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are You Sure You Want To Logout ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int in) {
                        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("myshared", 0);
                        sharedPreferences.edit().putString("email", "").apply();
                        sharedPreferences.edit().putString("name", "").apply();
                        sharedPreferences.edit().putInt("semester", 1 ).apply();
                        sharedPreferences.edit().putInt("type", 0).apply();
                        sharedPreferences.edit().putString("key", "").apply();

                        Intent i = new Intent(getContext() , LoginActivity.class);
                        LoginManager.getInstance().logOut();
                        startActivity(i);
                        getActivity().finish();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}
