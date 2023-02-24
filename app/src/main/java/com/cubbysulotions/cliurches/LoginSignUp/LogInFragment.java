package com.cubbysulotions.cliurches.LoginSignUp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private Button btnLogin;
    private TextView txtForgotPassword, register;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        txtEmail = view.findViewById(R.id.txtLoginEmail);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnLogin = view.findViewById(R.id.btnLoginAccount);
        txtForgotPassword = view.findViewById(R.id.btnForgotPass);
        register = view.findViewById(R.id.btnToRegister);

        login();
        forgotPassword();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_logInFragment_to_signInFragment);
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
                    dialog.setContentView(R.layout.reset_password);
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
                    // TODO: add login code here
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