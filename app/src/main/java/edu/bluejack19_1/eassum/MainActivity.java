package edu.bluejack19_1.eassum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import edu.bluejack19_1.eassum.Fragment.CalendarFragment;
import edu.bluejack19_1.eassum.Fragment.CourseFragment;
import edu.bluejack19_1.eassum.Fragment.FeedbackFragment;
import edu.bluejack19_1.eassum.Fragment.FragmentFeedbackAdmin;
import edu.bluejack19_1.eassum.Fragment.HomeFragment;
import edu.bluejack19_1.eassum.Fragment.UserFragment;
import edu.bluejack19_1.eassum.Model.User;

import edu.bluejack19_1.tpa_mobile.R;

import edu.bluejack19_1.eassum.fragmentinterface.FragmentChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, FragmentChangeListener {

    public static int userAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAdmin = User.getInstanceUserType();

        loadFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.course_menu:
                fragment = new CourseFragment();
                break;
            case R.id.feedback_menu:
                if(userAdmin == 1) fragment = new FragmentFeedbackAdmin();
                else fragment = new FeedbackFragment();
                break;
            case R.id.calendar_menu:
                fragment = new CalendarFragment();
                break;
            case R.id.user_menu:
                fragment = new UserFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void changeFragment(Fragment fragment) {
        loadFragment(fragment);
    }
}
