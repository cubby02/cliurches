package com.cubbysulotions.cliurches.LoginSignUp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.Admin.AdminActivity;
import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;

public class LoginRegisterActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsignup);
    }

    private boolean isInternet;
    public boolean checkInternet(){
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    "https://www.google.com/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //With internet
                            isInternet = true;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                        isInternet = false;
                        Toast.makeText(LoginRegisterActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            );

            Volley.newRequestQueue(this).add(stringRequest);

            return isInternet;
        } catch (Exception e) {
            Log.e("Login", "exception", e);
            Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
        }
        return isInternet;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SessionManagement sessionManagement = new SessionManagement(LoginRegisterActivity.this);
        int userID = sessionManagement.getSession();
        String user_api_key = sessionManagement.getSession2();
        String role = sessionManagement.getAccountType();

        if(userID != -1){
            switch (role){
                case "user":
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("api_key", user_api_key);
                    startActivity(intent);
                    finish();
                    break;
                case "admin":
                    Intent intent2 = new Intent(this, AdminActivity.class);
                    intent2.putExtra("api_key", user_api_key);
                    startActivity(intent2);
                    finish();
                    break;
            }


        }
    }
}