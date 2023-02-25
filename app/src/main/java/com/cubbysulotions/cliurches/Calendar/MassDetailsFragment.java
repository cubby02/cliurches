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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class MassDetailsFragment extends Fragment implements BackpressedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mass_details, container, false);
    }

    private Button btnBack, btnProceed;

    private EditText txtName, txtMessage;
    private NavController navController;
    private TextView txtSelectedDate;
    private Spinner spinnerSelectedTime;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        txtSelectedDate = view.findViewById(R.id.txtSelectedDate);
        btnProceed = view.findViewById(R.id.btnPayment);
        btnBack = view.findViewById(R.id.btnBackToCalendar);
        txtName = view.findViewById(R.id.txtNameRecipient);
        txtMessage = view.findViewById(R.id.txtMessage);
        spinnerSelectedTime = view.findViewById(R.id.spinnerSelectedTime);


        selectDate();
        selectTime();
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
        txtSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add open calendar code here
                    materialDatePicker();

                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void materialDatePicker() {
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintBuilder.build());
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getParentFragmentManager(),"DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                txtSelectedDate.setText(materialDatePicker.getHeaderText());
            }
        });
    }

    private void selectTime() {
        try {
            // TODO: add choose payment code here
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.time_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnerSelectedTime.setAdapter(adapter);

        } catch (Exception e) {
            toast("Something went wrong, please try again");
            Log.e(TAG, "onClick: ", e);
        }
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