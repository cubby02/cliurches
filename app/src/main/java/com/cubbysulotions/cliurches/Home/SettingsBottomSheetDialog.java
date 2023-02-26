package com.cubbysulotions.cliurches.Home;

import android.app.Dialog;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.LoginSignUp.LoginRegisterActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
    private String fullName, user_email;

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

    private Button btnCancelEdit, btnSaveEdit;
    private TextInputLayout txtFNameLayout, txtLNameLayout, txtEmailLayout;
    private TextInputEditText fName, lName, email;
    private RelativeLayout userDetailsLayout, loading, editUserLayout;

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

        populateDetails();
        editDetails();
        cancelEdit();
        closeDetails();
        saveDetails();
        logout();

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

            String URL = "https://cliurches-app.tech/api/user_details/?api_key="+ api +"";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject object = response.getJSONObject(0);

                                fullName = object.getString("fullname");
                                user_email = object.getString("email");

                                txtUserFullName.setText(fullName);
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
            requestQueue.add(jsonArrayRequest);

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

                        //temporary code
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                editUserLayout.setVisibility(View.GONE);
                                userDetailsLayout.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.INVISIBLE);
                                toast("User's details updated");
                            }
                        }, 5000);

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
                    // TODO: Insert Logout code here
                    SessionManagement sessionManagement = new SessionManagement(getActivity());
                    sessionManagement.removeSession();

                    Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    toast("Something went wrong please try again");
                    Log.e(TAG, "editDetails: ", e);
                }
            }
        });
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
                    fName.setText(fullName);
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
