package com.cubbysulotions.cliurches.LoginSignUp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.UserSessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class LogInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    private NavController navController;
    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnBack, btnTogglePassword;
    private TextView txtForgotPassword, register;
    LoadingDialog loadingDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        txtEmail = view.findViewById(R.id.txtLoginEmail);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnLogin = view.findViewById(R.id.btnLoginAccount);
        btnBack = view.findViewById(R.id.btnBack);
        txtForgotPassword = view.findViewById(R.id.btnForgotPass);
        register = view.findViewById(R.id.btnToRegister);
        btnTogglePassword = view.findViewById(R.id.btnTogglePassword);
        loadingDialog = new LoadingDialog(getActivity());
        BounceView.addAnimTo(btnLogin);

        back();
        login();
        forgotPassword();
        togglePassword();

        boolean isInternet = ((LoginRegisterActivity)getActivity()).checkInternet();
        Log.d(TAG, "onViewCreated: " + isInternet);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_logInFragment_to_signInFragment);
            }
        });
    }

    private boolean isClicked = true;
    private void togglePassword() {
        btnTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isClicked){
                        txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        btnTogglePassword.setBackground(getResources().getDrawable(R.drawable.eye_off));
                        isClicked = false;
                    } else {
                        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        btnTogglePassword.setBackground(getResources().getDrawable(R.drawable.eye_on));
                        isClicked = true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "togglePassword: ", e);
                }
            }
        });

    }

    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_logInFragment_to_welcomeScreenFragment);
            }
        });
    }

    private void forgotPassword() {
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Dialog dialog = new Dialog(getActivity());
                    //We have added a title in the custom layout. So let's disable the default title.
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                    dialog.setCancelable(true);
                    //Mention the name of the layout of your custom dialog.
                    dialog.setContentView(R.layout.dialog_reset_password);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    EditText resetEmail = dialog.findViewById(R.id.forgotPassEmail);
                    Button buttonSend = dialog.findViewById(R.id.btnSend);

                    resetEmail.setText(txtEmail.getText().toString());
                    buttonSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                // TODO: Add send reset link code here
                            } catch (Exception e){
                                toast("Something went wrong, please try again");
                                Log.e(TAG, "onClick send: ", e);
                            }

                        }
                    });

                    dialog.show();


                } catch (Exception e){
                    toast("Something went wrong, Please try again");
                    Log.e("Forgot Pass Error", "exception", e);
                }
            }
        });
    }

    private void login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
                        txtEmail.setError("Required");
                        txtPassword.setError("Required");
                    } else {
                        loadingDialog.startLoading("Please wait...");
                        boolean isInternet = ((LoginRegisterActivity)getActivity()).checkInternet();

                        if(isInternet){
                            txtEmail.setError(null);
                            txtPassword.setError(null);

                            String email = null;
                            String password = null;
                            try {
                                password = URLEncoder.encode(txtPassword.getText().toString().replace("'","\\'"), "utf-8");
                                email = URLEncoder.encode(txtEmail.getText().toString().replace("'","\\'"), "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            String JSON_URL = "https://cliurches-app.tech/api/login/?email="+ email +"&password="+ password+"";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.GET,
                                    JSON_URL,
                                    null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            loadingDialog.stopLoading();
                                            try {
                                                String status = response.getString("status");
                                                if (status.equals("wrong credentials")){
                                                    toast("Wrong credentials");
                                                } else if (status.equals("not verified")){
                                                    toast("Please verify your email");
                                                } else {
                                                    UserSessionManagement user = new UserSessionManagement(1, response.getString("api_key"));
                                                    SessionManagement sessionManagement = new SessionManagement(getActivity());
                                                    sessionManagement.saveSession(user);

                                                    toast("User logged in");

                                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loadingDialog.stopLoading();
                                        toast("Something went wrong, please try again");
                                        Log.e(TAG, "onErrorResponse: ", error);
                                    }
                                }
                            );

                            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                        } else {
                            loadingDialog.stopLoading();
                            toast("Please try again");
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick Login: ", e);
                }
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}