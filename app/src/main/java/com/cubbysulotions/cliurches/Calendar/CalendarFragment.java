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
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;

import org.w3c.dom.Text;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class CalendarFragment extends Fragment implements BackpressedListener {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnSelectMass = view.findViewById(R.id.btnSelectMass);
        txtNoMassLabel = view.findViewById(R.id.txtNoMassLabel);
        calendarView = view.findViewById(R.id.calendarView);

        selectMass();
    }

    private void selectMass() {
        btnSelectMass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add select mass code here

                    navController.navigate(R.id.action_calendarFragment_to_massDetailsFragment);
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