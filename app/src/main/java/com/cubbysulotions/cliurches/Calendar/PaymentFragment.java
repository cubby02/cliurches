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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.cubbysulotions.cliurches.Calendar.CalendarFragment.SELECTED_CHURCH;
import static com.cubbysulotions.cliurches.Calendar.CalendarFragment.SELECTED_DATE;
import static com.cubbysulotions.cliurches.Calendar.MassDetailsFragment.ADD_MESS;
import static com.cubbysulotions.cliurches.Calendar.MassDetailsFragment.FOR_WHOM;
import static com.cubbysulotions.cliurches.Calendar.MassDetailsFragment.NAME;
import static com.cubbysulotions.cliurches.Calendar.MassDetailsFragment.SELECTED_TIME;
import static com.cubbysulotions.cliurches.Calendar.MassDetailsFragment.URI_PADASAL;

public class PaymentFragment extends Fragment implements BackpressedListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    private NavController navController;
    private Button btnComplete, btnBack;
    private TextView txtOrderID, txtSelectedDate, txtSelectedTime, txtName, txtWhom, txtPadasal, txtAddMess, txtTotal, txtChurch, txtBankName, txtAccountName, txtAccountNumber;
    private LoadingDialog loadingDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnBack = view.findViewById(R.id.btnBackToReservation);
        btnComplete = view.findViewById(R.id.btnCompleteReservation);
        txtSelectedDate = view.findViewById(R.id.txtPaymentDate);
        txtSelectedTime = view.findViewById(R.id.txtPaymentTime);
        txtName = view.findViewById(R.id.txtPaymentRecipient);
        txtWhom = view.findViewById(R.id.txtForWhom);
        txtPadasal = view.findViewById(R.id.txtPadasal);
        txtAddMess = view.findViewById(R.id.txtAddMess);
        txtTotal = view.findViewById(R.id.txtPaymentTotal);
        txtChurch = view.findViewById(R.id.txtChurch);
        loadingDialog = new LoadingDialog(getActivity());
        txtAccountName = view.findViewById(R.id.txtAccountName);
        txtAccountNumber = view.findViewById(R.id.txtAccountNumber);
        txtBankName = view.findViewById(R.id.txtBankName);

        populate();
        back();
        proceed();
    }

    private void populate() {
        try {

            txtSelectedDate.setText(getArguments().getString(SELECTED_DATE));
            txtSelectedTime.setText(getArguments().getString(SELECTED_TIME));
            txtName.setText(getArguments().getString(NAME));
            txtWhom.setText(getArguments().getString(FOR_WHOM));
            txtPadasal.setText(getArguments().getString(URI_PADASAL));
            txtAddMess.setText(getArguments().getString(ADD_MESS));
            populatePaymentMethod();

        } catch (Exception e) {
            Log.e(TAG, "populate: ", e);
            toast("Something went wrong");
        }
    }

    private void populatePaymentMethod() {
        try {
            SessionManagement sessionManagement = new SessionManagement(getActivity());
            String api_key = sessionManagement.getSession2();


            String selectedChurch_url = null;
            String api_url = null;


            try {
                selectedChurch_url = URLEncoder.encode(getArguments().getString(SELECTED_CHURCH).replace("'","\\'"), "utf-8");
                api_url = URLEncoder.encode(api_key.replace("'","\\'"), "utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String URL = "http://192.3.236.3/cliurches-api/api/paymentMethod/?parish="+ selectedChurch_url +"&api_key="+api_url+"";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject object = response.getJSONObject(0);

                                txtBankName.setText(object.getString("method"));
                                txtAccountName.setText(object.getString("account_name"));
                                txtAccountNumber.setText(object.getString("account_number"));
                                txtTotal.setText("P" + object.getString("donation") + ".00");

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            );
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);
        } catch (Exception e){
            Log.e(TAG, "populatePaymentMethod: ", e);
            toast("Something went wrong");
        }
    }

    private void proceed() {
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loadingDialog.startLoading("Please wait...");


                    SessionManagement sessionManagement = new SessionManagement(getActivity());
                    String api_key = sessionManagement.getSession2();

                    String selectedDate_url = null;
                    String selectedTime_url = null;
                    String selectedChurch_url = null;
                    String name_url = null;
                    String forWhom_url = null;
                    String selectedPadasal_url = null;
                    String additional_url = null;
                    String api_url = null;

                    try {
                        selectedTime_url = URLEncoder.encode(getArguments().getString(SELECTED_DATE).replace("'","\\'"), "utf-8");
                        selectedDate_url = URLEncoder.encode(getArguments().getString(SELECTED_TIME).replace("'","\\'"), "utf-8");
                        selectedChurch_url = URLEncoder.encode(getArguments().getString(SELECTED_CHURCH).replace("'","\\'"), "utf-8");
                        api_url = URLEncoder.encode(api_key.replace("'","\\'"), "utf-8");
                        name_url = URLEncoder.encode(getArguments().getString(NAME).toString().replace("'","\\'"), "utf-8");
                        forWhom_url = URLEncoder.encode(getArguments().getString(FOR_WHOM).replace("'","\\'"), "utf-8");
                        selectedPadasal_url = URLEncoder.encode(getArguments().getString(URI_PADASAL).toString().replace("'","\\'"), "utf-8");
                        additional_url = URLEncoder.encode(getArguments().getString(ADD_MESS).replace("'","\\'"), "utf-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String URL = "http://192.3.236.3/cliurches-api/api/pamisa/?parish="+ selectedChurch_url +"&recipient="+name_url +"&forwhom="+ forWhom_url +"&date="+ selectedDate_url +"&time=" + selectedTime_url +"&type=" + selectedPadasal_url +"&comment=" + additional_url +"&api_key="+ api_url+"";
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loadingDialog.stopLoading();
                                    ((HomeActivity)getActivity()).hideNavigationBar(false);
                                    ((HomeActivity)getActivity()).hideTopBarPanel(false);
                                    toast("Mass scheduled!");
                                    navController.navigate(R.id.action_paymentFragment_to_calendarFragment);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: ", error);
                            loadingDialog.stopLoading();
                        }
                    }
                    );
                    VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);


                } catch (Exception e) {
                    loadingDialog.stopLoading();
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

                    Bundle bundle = new Bundle();
                    bundle.putString(SELECTED_CHURCH, getArguments().getString(SELECTED_CHURCH));
                    bundle.putString(SELECTED_DATE, getArguments().getString(SELECTED_DATE));
                    bundle.putString(SELECTED_TIME, getArguments().getString(SELECTED_TIME));
                    bundle.putString(NAME, getArguments().getString(NAME));
                    bundle.putString(FOR_WHOM, getArguments().getString(FOR_WHOM));
                    bundle.putString(URI_PADASAL, getArguments().getString(URI_PADASAL));
                    bundle.putString(ADD_MESS, getArguments().getString(ADD_MESS));


                    navController.navigate(R.id.action_paymentFragment_to_massDetailsFragment, bundle);
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
        Bundle bundle = new Bundle();
        bundle.putString(SELECTED_CHURCH, getArguments().getString(SELECTED_CHURCH));
        bundle.putString(SELECTED_DATE, getArguments().getString(SELECTED_DATE));
        bundle.putString(SELECTED_TIME, getArguments().getString(SELECTED_TIME));
        bundle.putString(NAME, getArguments().getString(NAME));
        bundle.putString(FOR_WHOM, getArguments().getString(FOR_WHOM));
        bundle.putString(URI_PADASAL, getArguments().getString(URI_PADASAL));
        bundle.putString(ADD_MESS, getArguments().getString(ADD_MESS));


        navController.navigate(R.id.action_paymentFragment_to_massDetailsFragment, bundle);
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