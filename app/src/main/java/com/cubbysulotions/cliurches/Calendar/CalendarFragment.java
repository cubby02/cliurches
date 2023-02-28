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
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;

import org.w3c.dom.Text;

import java.util.Calendar;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class CalendarFragment extends Fragment implements BackpressedListener {

    public static final String SELECTED_DATE = "selected_date";
    public static final String SELECTED_CHURCH = "selected_church";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private NavController navController;
    private Button btnSelectMass;
    private TextView txtNoMassLabel;
    private CalendarView calendarView;
    private Spinner spinnerSelectedChurch;
    private String selectedDate, selectedChurch;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnSelectMass = view.findViewById(R.id.btnSelectMass);
        txtNoMassLabel = view.findViewById(R.id.txtNoMassLabel);
        calendarView = view.findViewById(R.id.calendarView);
        spinnerSelectedChurch = view.findViewById(R.id.spinnerSelectedChurch);

        selectMass();
        selectChurch();
        selectDate();
    }

    private void selectDate() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i, i1, i2);
                selectedDate = (i1 + 1) + "/" + i2 + "/" + i;
            }
        });
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

    private void selectMass() {
        btnSelectMass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    switch ((int) spinnerSelectedChurch.getSelectedItemId()){
                        case 0:
                            selectedChurch = "bauan";
                            break;
                        case 1:
                            selectedChurch = "aplaya";
                            break;
                        case 2:
                            selectedChurch = "bolo";
                            break;
                        case 3:
                            selectedChurch = "sanpascual";
                            break;
                    }


                    Bundle bundle = new Bundle();
                    if (selectedDate == null) {
                        Calendar calendar = Calendar.getInstance();
                        selectedDate = (calendar.get(Calendar.MONTH) + 1) + "/"+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
                    }

                    bundle.putString(SELECTED_DATE, selectedDate);
                    bundle.putString(SELECTED_CHURCH, selectedChurch);

                    navController.navigate(R.id.action_calendarFragment_to_massDetailsFragment, bundle);
                    ((HomeActivity)getActivity()).hideNavigationBar(true);
                    ((HomeActivity)getActivity()).hideTopBarPanel(true);
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
        ((HomeActivity) getActivity()).home();
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