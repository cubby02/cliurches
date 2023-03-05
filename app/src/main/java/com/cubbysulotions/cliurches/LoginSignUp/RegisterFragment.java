package com.cubbysulotions.cliurches.LoginSignUp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;
import com.github.hariprasanths.bounceview.BounceView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private NavController navController;
    private Button btnBack, btnRegister, btnTogglePassword;
    private EditText txtFN, txtLN, txtEmail, txtPassword;
    LoadingDialog loadingDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnBack = view.findViewById(R.id.btnBack);
        btnRegister = view.findViewById(R.id.btnRegisterAccount);
        btnTogglePassword = view.findViewById(R.id.btnTogglePassword);
        txtFN = view.findViewById(R.id.txtFn);
        txtLN = view.findViewById(R.id.txtLn);
        txtEmail = view.findViewById(R.id.txtEmailRegister);
        txtPassword = view.findViewById(R.id.txtPasswordRegister);
        loadingDialog = new LoadingDialog(getActivity());
        BounceView.addAnimTo(btnBack);
        BounceView.addAnimTo(btnRegister);

        boolean isInternet = ((LoginRegisterActivity)getActivity()).checkInternet();
        Log.d(TAG, "onViewCreated: " + isInternet);
        togglePassword();
        back();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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

    private void imeDone(){
        try{
            txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        register();
                        handled = true;
                    }
                    return handled;
                }
            });
        }catch(Exception e){
            Log.e(TAG, "imeAction", e);
        }
    }

    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_signInFragment_to_welcomeScreenFragment);
            }
        });
    }

    private void register() {
                try {
                    if(txtFN.getText().toString().isEmpty() || txtLN.getText().toString().isEmpty()
                            || txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
                        txtFN.setError("Required");
                        txtLN.setError("Required");
                        txtEmail.setError("Required");
                        txtPassword.setError("Required");
                    }
                    else {
                        loadingDialog.startLoading("Please wait...");
                        boolean isInternet = ((LoginRegisterActivity)getActivity()).checkInternet();

                        if(isInternet){
                            txtFN.setError(null);
                            txtLN.setError(null);
                            txtEmail.setError(null);
                            txtPassword.setError(null);

                            String firstName = null;
                            String lastName = null;
                            String email = null;
                            String password = null;
                            try {
                                firstName = URLEncoder.encode(txtFN.getText().toString().replace("'","\\'"), "utf-8");
                                lastName = URLEncoder.encode(txtLN.getText().toString().replace("'","\\'"), "utf-8");
                                password = URLEncoder.encode(txtPassword.getText().toString().replace("'","\\'"), "utf-8");
                                email = URLEncoder.encode(txtEmail.getText().toString().replace("'","\\'"), "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            String JSON_URL = "http://171.22.124.181/cliurches-api/api/new_user/?email="+ email +"&password="+ password +"&firstname="+ firstName +"&lastname="+ lastName +"";

                            StringRequest postRequest = new StringRequest(
                                    Request.Method.POST,
                                    JSON_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("{\"status\":\"email already exist\"}")){
                                                loadingDialog.stopLoading();
                                                toast("Email already exist, try again");
                                            } else {
                                                toast("Registered Successfully");
                                                loadingDialog.stopLoading();
                                                navController.navigate(R.id.action_signInFragment_to_logInFragment);
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

                            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(postRequest);
                        }
                        else {
                            loadingDialog.stopLoading();
                            toast("Please try again");
                        }
                    }
                } catch (Exception e){
                    toast("Something went wrong");
                    Log.e(TAG, "onClick register: ", e);
                }
    }

    private void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}