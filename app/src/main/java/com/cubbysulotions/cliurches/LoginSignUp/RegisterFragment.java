package com.cubbysulotions.cliurches.LoginSignUp;

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
import android.widget.EditText;
import android.widget.Toast;

import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private NavController navController;
    private Button btnBack, btnRegister;
    private EditText txtFN, txtLN, txtEmail, txtPassword;
    LoadingDialog loadingDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnBack = view.findViewById(R.id.btnBack);
        btnRegister = view.findViewById(R.id.btnRegisterAccount);
        txtFN = view.findViewById(R.id.txtFn);
        txtLN = view.findViewById(R.id.txtLn);
        txtEmail = view.findViewById(R.id.txtEmailRegister);
        txtPassword = view.findViewById(R.id.txtPasswordRegister);
        loadingDialog = new LoadingDialog(getActivity());

        register();
        back();
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
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add register code here

                    navController.navigate(R.id.action_signInFragment_to_logInFragment);
                } catch (Exception e){
                    toast("Something went wrong");
                    Log.e(TAG, "onClick register: ", e);
                }
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}