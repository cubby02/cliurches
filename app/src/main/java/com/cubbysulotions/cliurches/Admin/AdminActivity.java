package com.cubbysulotions.cliurches.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private Button btnLogoutAccount, btnSortList, btnSavePaymentMethod;
    private TextView tabLabel;
    private EditText txtNameRecipient;
    private RelativeLayout topBarPanel;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btnLogoutAccount = findViewById(R.id.btnLogoutAccount);
        topBarPanel = findViewById(R.id.topBarPanel);
        tabLabel = findViewById(R.id.tabLabel);
        btnSortList = findViewById(R.id.btnSortList);
        btnSavePaymentMethod = findViewById(R.id.btnSavePaymentMethod);
        txtNameRecipient = findViewById(R.id.txtNameRecipient);

        sortList();
        settings();
        setPaymentMethod();

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#50343434"));
            }

        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }

    }

    private void setPaymentMethod() {
        btnSavePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(txtNameRecipient.getText().toString().isEmpty()){
                        txtNameRecipient.setError("Required");
                    } else {
                        //TODO:insert code hehe
                    }
                }catch (Exception e){
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
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


    private void sortList() {
        btnSortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Dialog dialog = new Dialog(AdminActivity.this);
                    //We have added a title in the custom layout. So let's disable the default title.
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                    dialog.setCancelable(true);
                    //Mention the name of the layout of your custom dialog.
                    dialog.setContentView(R.layout.dialog_sort_by);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    RadioGroup radioGroupSort = dialog.findViewById(R.id.radioGroupSort);
                    RadioButton rbApproved = dialog.findViewById(R.id.rbApproved);
                    RadioButton rbAllLists = dialog.findViewById(R.id.rbAllLists);
                    RadioButton rbDeclined = dialog.findViewById(R.id.rbDeclined);

                    dialog.show();

                    rbApproved.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: add code
                            Log.d("radioButton","Approvedd");

                        }
                    });

                    rbAllLists.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: add code
                            Log.d("radioButton","AllList");

                        }
                    });

                    rbDeclined.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: add code
                            Log.d("radioButton","Declined");
                        }
                    });

                }catch(Exception e){
                    Log.e("error", "onClick", e);
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(AdminActivity.this, msg, Toast.LENGTH_LONG).show();
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