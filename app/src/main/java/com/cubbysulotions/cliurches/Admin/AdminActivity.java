package com.cubbysulotions.cliurches.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Calendar.CalendarContainerFragment;
import com.cubbysulotions.cliurches.Calendar.CalendarFragment;
import com.cubbysulotions.cliurches.Calendar.MassDetailsFragment;
import com.cubbysulotions.cliurches.Calendar.PaymentFragment;
import com.cubbysulotions.cliurches.Camera.CameraContainerFragment;
import com.cubbysulotions.cliurches.Camera.CameraFragment;
import com.cubbysulotions.cliurches.Camera.MatchResultFragment;
import com.cubbysulotions.cliurches.Gallery.GalleryFragment;
import com.cubbysulotions.cliurches.Home.ForumContainerFragment;
import com.cubbysulotions.cliurches.Home.SettingsBottomSheetDialog;
import com.cubbysulotions.cliurches.Home.WritePostFragment;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Reciepts.RecieptsFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AdminActivity extends AppCompatActivity {
    private Button btnLogoutAccount;
    private TextView tabLabel;
    private RelativeLayout topBarPanel;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btnLogoutAccount = findViewById(R.id.btnLogoutAccount);
        topBarPanel = findViewById(R.id.topBarPanel);
        tabLabel = findViewById(R.id.tabLabel);

        settings();

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#50343434"));
            }

        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }

    }

    private void settings() {
        btnLogoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    SettingsBottomSheetDialog settingsBottomSheetDialog = new SettingsBottomSheetDialog();
                    settingsBottomSheetDialog.show(getSupportFragmentManager(), settingsBottomSheetDialog.getTag());
                } catch (Exception e) {
                    Log.e(TAG, "onCreate: ", e);
                }
            }
        });
    }

    public void hideTopBarPanel(boolean flag){
        if(flag){
            topBarPanel.setVisibility(View.GONE);
        } else{
            topBarPanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);

    }
}