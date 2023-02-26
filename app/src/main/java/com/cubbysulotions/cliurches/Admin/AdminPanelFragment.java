package com.cubbysulotions.cliurches.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;

public class AdminPanelFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_panel, container, false);
    }

    private NavController navController;
    private Button btnCompletePayment, btnPendingPayment, btnScheduleMass;
    private Spinner spinnerSelectedChurch;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnCompletePayment = view.findViewById(R.id.btnCompletePayment);
        btnScheduleMass = view.findViewById(R.id.btnScheduleMass);
        btnPendingPayment = view.findViewById(R.id.btnPendingPayment);
        spinnerSelectedChurch = view.findViewById(R.id.spinnerSelectedChurch);

        btnCompletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_adminPanelFragment_to_completedPaymentFragment);
                ((AdminActivity) getActivity()).hideTopBarPanel(true);
            }
        });

        btnPendingPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_adminPanelFragment_to_pendingPaymentFragment);
                ((AdminActivity) getActivity()).hideTopBarPanel(true);
            }
        });

        scheduleMass();
        selectChurch();

    }

    private void selectChurch() {
        try {
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.church_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnerSelectedChurch.setAdapter(adapter);

        } catch (Exception e) {
            toast("Something went wrong, please try again");
            Log.e(TAG, "onClick: ", e);
        }
    }

    private void scheduleMass() {
        btnScheduleMass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add select mass code here

                    navController.navigate(R.id.action_adminPanelFragment_to_massScheduleFragment);
                    ((AdminActivity) getActivity()).hideTopBarPanel(true);

                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}