package com.cubbysulotions.cliurches.Calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class MassDetailsFragment extends Fragment implements BackpressedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mass_details, container, false);
    }

    private Button btnBack, btnSelectedDate, btnSelectedTime, btnMOP, btnProceed;
    private EditText txtName;
    private NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnSelectedDate = view.findViewById(R.id.btnSelectedDate);
        btnSelectedTime = view.findViewById(R.id.btnSelectedTime);
        btnMOP = view.findViewById(R.id.btnMOP);
        btnProceed = view.findViewById(R.id.btnPayment);
        btnBack = view.findViewById(R.id.btnBackToCalendar);
        txtName = view.findViewById(R.id.txtNameRecipient);

        selectDate();
        selectTime();
        choosePayment();
        proceedPayment();
        back();
    }

    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    navController.navigate(R.id.action_massDetailsFragment_to_calendarFragment);
                    ((HomeActivity)getActivity()).hideNavigationBar(false);
                    ((HomeActivity)getActivity()).hideTopBarPanel(false);
                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void selectDate() {
        btnSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add open calendar code here

                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void selectTime() {
        btnSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add open time picker code here

                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void choosePayment() {
        btnMOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add choose payment code here

                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void proceedPayment() {
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: proceed to payment code
                    navController.navigate(R.id.action_massDetailsFragment_to_paymentFragment);
                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        navController.navigate(R.id.action_massDetailsFragment_to_calendarFragment);
        ((HomeActivity)getActivity()).hideNavigationBar(false);
        ((HomeActivity)getActivity()).hideTopBarPanel(false);
    }

    public static BackpressedListener backpressedlistener;

    @Override
    public void onPause() {
        backpressedlistener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        backpressedlistener = this;
    }
}