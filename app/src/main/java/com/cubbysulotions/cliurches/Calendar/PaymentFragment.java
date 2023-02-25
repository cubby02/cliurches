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
import android.widget.TextView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class PaymentFragment extends Fragment implements BackpressedListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    private NavController navController;
    private Button btnComplete, btnBack;
    private TextView txtOrderID, txtSelectedDate, txtSelectedTime, txtName, txtMOP, txtTotal;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnBack = view.findViewById(R.id.btnBackToReservation);
        btnComplete = view.findViewById(R.id.btnCompleteReservation);
        txtSelectedDate = view.findViewById(R.id.txtPaymentDate);
        txtSelectedTime = view.findViewById(R.id.txtPaymentTime);
        txtName = view.findViewById(R.id.txtPaymentRecipient);
        txtMOP = view.findViewById(R.id.txtPaymentMOP);
        txtTotal = view.findViewById(R.id.txtPaymentTotal);

        back();
        proceed();
    }

    private void proceed() {
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add proceed code here
                    ((HomeActivity)getActivity()).hideNavigationBar(false);
                    ((HomeActivity)getActivity()).hideTopBarPanel(false);
                    navController.navigate(R.id.action_paymentFragment_to_calendarFragment);
                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    navController.navigate(R.id.action_paymentFragment_to_massDetailsFragment);
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
        navController.navigate(R.id.action_paymentFragment_to_massDetailsFragment);
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