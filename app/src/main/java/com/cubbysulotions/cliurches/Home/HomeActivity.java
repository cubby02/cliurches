package com.cubbysulotions.cliurches.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cubbysulotions.cliurches.Calendar.CalendarFragment;
import com.cubbysulotions.cliurches.Camera.CameraFragment;
import com.cubbysulotions.cliurches.Gallery.GalleryFragment;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Reciepts.RecieptsFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class HomeActivity extends AppCompatActivity {

    ChipNavigationBar navigationView;
    private TextView tabLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLabel = findViewById(R.id.tabLabel);

        try {
            home();
            navigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    Fragment fragment = null;
                    Bundle bundle = new Bundle();
                    bundle.putString("details", "calendar");
                    switch (i){
                        case R.id.camera:
                            fragment = new CameraFragment();
                            tabLabel.setText("CAMERA");
                            break;
                        case R.id.nav_gallery:
                            fragment = new GalleryFragment();
                            tabLabel.setText("GALLERY");
                            break;
                        case R.id.nav_home:
                            fragment = new ForumFragment();
                            tabLabel.setText("HOME");
                            break;
                        case R.id.nav_calendar:
                            fragment = new CalendarFragment();
                            tabLabel.setText("CALENDAR");
                            break;
                        case R.id.nav_receipts:
                            fragment = new RecieptsFragment();
                            tabLabel.setText("RECEIPTS");
                            break;
                    }
                    assert fragment != null;
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    public void home(){
        navigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ForumFragment()).commit();
        navigationView.setItemSelected(R.id.nav_home, true);
    }
}