package com.cubbysulotions.cliurches.Home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.LoginSignUp.LoginRegisterActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class SettingsBottomSheetDialog extends BottomSheetDialogFragment {
    View rootView;
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;
    private String first_name, last_name, user_email;

    public SettingsBottomSheetDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rootView;
    }

    private TextView txtUserFullName, txtUserEmail;
    private Button btnClose, btnEdit, btnLogout;

    private Button btnCancelEdit, btnSaveEdit, btnChangePassword;
    private TextInputLayout txtFNameLayout, txtLNameLayout, txtEmailLayout;
    private TextInputEditText fName, lName, email;
    private RelativeLayout userDetailsLayout, loading, editUserLayout;

    LoadingDialog loadingDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userDetailsLayout = view.findViewById(R.id.userDetails);
        editUserLayout = view.findViewById(R.id.editUser);
        loading = view.findViewById(R.id.loading);

        txtUserFullName = view.findViewById(R.id.txtUserFullName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        btnClose = view.findViewById(R.id.btnCloseUserDetails);
        btnEdit = view.findViewById(R.id.btnEditUserDetails);
        btnLogout = view.findViewById(R.id.btnLogoutAccount);

        btnCancelEdit = view.findViewById(R.id.btnCancelEdit);
        btnSaveEdit = view.findViewById(R.id.btnSaveEdit);
        txtFNameLayout = view.findViewById(R.id.fnameEditLayout);
        txtLNameLayout = view.findViewById(R.id.lnameEditLayout);
        txtEmailLayout = view.findViewById(R.id.emailEditLayout);
        fName = view.findViewById(R.id.txtEditFname);
        lName = view.findViewById(R.id.txtEditLname);
        email = view.findViewById(R.id.txtEditEmail);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);

        loadingDialog = new LoadingDialog(getActivity());

        SessionManagement sessionManagement = new SessionManagement(getActivity());
        if(sessionManagement.getAccountType().equals("admin")){
            email.setEnabled(false);
        }

        populateDetails();
        editDetails();
        cancelEdit();
        closeDetails();
        saveDetails();
        logout();
        changePass();

    }

    private void changePass() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    String urlApi = null;

                                    try {
                                        urlApi = URLEncoder.encode(user_email.replace("'","\\'"), "utf-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                    String JSON_URL = "http://171.22.124.181/cliurches-api/api/changePassword/?email="+ urlApi +"";
                                    StringRequest stringRequest = new StringRequest(
                                            Request.Method.POST,
                                            JSON_URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    logoutSession();
                                                    toast("Please check your email");
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, "onErrorResponse: ", error);
                                            loading.setVisibility(View.INVISIBLE);
                                            toast("Something went wrong");
                                        }
                                    }
                                    );
                                    VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to request for change password? Your account will be logout temporarily.").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                } catch (Exception e){
                    Log.e(TAG, "populateDetails: ", e);
                    toast("Something went wrong");
                }
            }
        });
    }

    private void populateDetails() {
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

                                first_name = object.getString("FirstName");
                                last_name = object.getString("LastName");
                                user_email = object.getString("email");

                                txtUserFullName.setText(first_name + " " + last_name );
                                txtUserEmail.setText(user_email);

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

        } catch (Exception e) {
            Log.e(TAG, "populateDetails: ", e);
            toast("Something went wrong");
        }
    }

    private void saveDetails() {
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: insert save edit here

                    if(fName.getText().toString().isEmpty() || lName.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                        txtFNameLayout.setError("Required");
                        txtLNameLayout.setError("Required");
                        txtEmailLayout.setError("Required");
                    } else {
                        loading.setVisibility(View.VISIBLE);
                        txtFNameLayout.setError(null);
                        txtLNameLayout.setError(null);
                        txtEmailLayout.setError(null);

                        String urlFirstname = null;
                        String urlLastname = null;
                        String urlEmail = null;
                        String urlApi = null;
                        SessionManagement sessionManagement = new SessionManagement(getActivity());
                        try {
                            urlFirstname = URLEncoder.encode(fName.getText().toString().replace("'","\\'"), "utf-8");
                            urlLastname = URLEncoder.encode(lName.getText().toString().replace("'","\\'"), "utf-8");
                            urlEmail = URLEncoder.encode(email.getText().toString().replace("'","\\'"), "utf-8");
                            urlApi = URLEncoder.encode(sessionManagement.getSession2().replace("'","\\'"), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String JSON_URL = "http://171.22.124.181/cliurches-api/api/edit_details/?firstname="+ urlFirstname +"&lastname="+ urlLastname +"&email="+ urlEmail +"&api_key="+ urlApi +"";
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                JSON_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("sucess")) {
                                            populateDetails();
                                            userDetailsLayout.setVisibility(View.VISIBLE);
                                            editUserLayout.setVisibility(View.GONE);
                                            loading.setVisibility(View.INVISIBLE);
                                            toast("User's details updated");

                                        }else{
                                            toast("email belongs to an existing user");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "onErrorResponse: ", error);
                                loading.setVisibility(View.INVISIBLE);
                                toast("Something went wrong");
                            }
                        }
                        );
                        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);



                    }
                } catch (Exception e) {
                    toast("Something went wrong please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void logout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    logoutSession();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } catch (Exception e) {
                    toast("Something went wrong please try again");
                    Log.e(TAG, "editDetails: ", e);
                }
            }
        });
    }

    private void logoutSession(){
        toast("Logging out...");
        String urlApi = null;
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        try {
            urlApi = URLEncoder.encode(sessionManagement.getSession2().replace("'","\\'"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String URL = "https://cliurches-app.tech/api/logout/?api_key="+ urlApi +"";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sessionManagement.removeSession();
                        toast("Logged out");
                        Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                toast("Something went wrong");
            }
        }
        );
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void closeDetails() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    toast("Something went wrong please try again");
                    Log.e(TAG, "editDetails: ", e);
                }
            }
        });
    }

    private void cancelEdit() {
        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    editUserLayout.setVisibility(View.GONE);
                    userDetailsLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    toast("Something went wrong please try again");
                    Log.e(TAG, "editDetails: ", e);
                }
            }
        });
    }

    private void editDetails() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    editUserLayout.setVisibility(View.VISIBLE);
                    userDetailsLayout.setVisibility(View.GONE);
                    fName.setText(first_name);
                    lName.setText(last_name);
                    email.setText(user_email);
                } catch (Exception e) {
                    toast("Something went wrong please try again");
                    Log.e(TAG, "editDetails: ", e);
                }
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
