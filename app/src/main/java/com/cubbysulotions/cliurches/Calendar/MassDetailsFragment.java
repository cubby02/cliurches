package com.cubbysulotions.cliurches.Calendar;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.cubbysulotions.cliurches.Utilities.DateTimeUtils;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimeZone;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.cubbysulotions.cliurches.Calendar.CalendarFragment.SELECTED_CHURCH;
import static com.cubbysulotions.cliurches.Calendar.CalendarFragment.SELECTED_DATE;

public class MassDetailsFragment extends Fragment implements BackpressedListener {

    public static final String SELECTED_TIME = "selected_time";
    public static final String NAME = "name";
    public static final String FOR_WHOM = "for_whom";
    public static final String URI_PADASAL = "padasal";
    public static final String ADD_MESS = "add_mess";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mass_details, container, false);
    }

    private Button btnBack, btnProceed;

    private EditText txtName, txtMessage, txtWhom;
    private NavController navController;
    private TextView txtSelectedDate;
    private Spinner spinnerSelectedTime, spinnerPadasal;
    private String selectedDate, selectedTime, selectedPadasal, txtRecipient, txtForwhom, txtAdditionalMess, selectedChurch;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        txtSelectedDate = view.findViewById(R.id.txtSelectedDate);
        btnProceed = view.findViewById(R.id.btnPayment);
        btnBack = view.findViewById(R.id.btnBackToCalendar);
        txtName = view.findViewById(R.id.txtNameRecipient);
        txtWhom = view.findViewById(R.id.txtName);
        txtMessage = view.findViewById(R.id.txtMessage);
        spinnerSelectedTime = view.findViewById(R.id.spinnerSelectedTime);
        spinnerPadasal = view.findViewById(R.id.spinnerPadasal);
        selectedDate = getArguments().getString(SELECTED_DATE);
        selectedChurch = getArguments().getString(SELECTED_CHURCH);

        txtSelectedDate.setText(DateTimeUtils.formattedShortDate(selectedDate));
        populateName();
        selectDate();
        selectTime();
        selectPadasal();
        proceedPayment();
        back();
    }

    private void selectPadasal() {
        try {
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.padasal_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnerPadasal.setAdapter(adapter);


        } catch (Exception e) {
            toast("Something went wrong, please try again");
            Log.e(TAG, "onClick: ", e);
        }
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
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(Long.parseLong(selection.toString()));
                selectedDate = (calendar.get(Calendar.MONTH) + 1) + "/"+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
            }
        });
    }

    private void selectTime() {
        try {
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
                    selectedPadasal = spinnerPadasal.getSelectedItem().toString();
                    selectedTime = spinnerSelectedTime.getSelectedItem().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString(SELECTED_CHURCH, selectedChurch);
                    bundle.putString(SELECTED_DATE, selectedDate);
                    bundle.putString(SELECTED_TIME, selectedTime);
                    bundle.putString(NAME, txtName.getText().toString());
                    bundle.putString(FOR_WHOM, txtWhom.getText().toString());
                    bundle.putString(URI_PADASAL, selectedPadasal);
                    if (txtMessage.getText().toString().isEmpty()){
                        bundle.putString(ADD_MESS, "n/a");
                    } else {
                        bundle.putString(ADD_MESS, txtMessage.getText().toString());
                    }

                    if(txtName.getText().toString().isEmpty() || txtWhom.getText().toString().isEmpty()){
                        txtName.setError("Required");
                        txtWhom.setError("Required");
                    } else {
                        navController.navigate(R.id.action_massDetailsFragment_to_paymentFragment, bundle);
                    }
                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void populateName() {
        try {
            SessionManagement sessionManagement = new SessionManagement(getActivity());
            String api = null;
            try {
                api = URLEncoder.encode(sessionManagement.getSession2().toString().replace("'","\\'"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String URL = "http://171.22.124.181/cliurches-api/api/user_details/?api_key="+ api +"";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject object = response.getJSONObject(0);

                                String first_name = object.getString("FirstName");
                                String last_name = object.getString("LastName");

                                txtName.setText(first_name + " " + last_name );

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    txtName.setText("");
                }
            }
            );
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);

        } catch (Exception e) {
            txtName.setText("");
            Log.e(TAG, "populateDetails: ", e);
            toast("Something went wrong");
        }
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