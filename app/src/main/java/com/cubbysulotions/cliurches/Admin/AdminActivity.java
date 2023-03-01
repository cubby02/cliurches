package com.cubbysulotions.cliurches.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.cubbysulotions.cliurches.Calendar.CalendarFragment.SELECTED_CHURCH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.cubbysulotions.cliurches.Calendar.CalendarContainerFragment;
import com.cubbysulotions.cliurches.Calendar.CalendarFragment;
import com.cubbysulotions.cliurches.Calendar.MassDetailsFragment;
import com.cubbysulotions.cliurches.Calendar.PaymentFragment;
import com.cubbysulotions.cliurches.Camera.CameraContainerFragment;
import com.cubbysulotions.cliurches.Camera.CameraFragment;
import com.cubbysulotions.cliurches.Camera.MatchResultFragment;
import com.cubbysulotions.cliurches.CustomAdapters.RecieptsCustomAdapter;
import com.cubbysulotions.cliurches.Gallery.GalleryFragment;
import com.cubbysulotions.cliurches.Home.ForumContainerFragment;
import com.cubbysulotions.cliurches.Home.SettingsBottomSheetDialog;
import com.cubbysulotions.cliurches.Home.WritePostFragment;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Reciepts.RecieptsFragment;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.UserPamisa;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Button btnLogoutAccount, btnSortList,btnSavePaymentMethod, btnEditPaymentMethod;
    private TextView tabLabel;
    private RelativeLayout topBarPanel;
    boolean doubleBackToExitPressedOnce = false;
    private EditText txtBankName, txtAccountNumber,txtAccountName, txtDonationAmount;
    private TextView txtTotal, txtBankNameStatic, txtAccountNameStatic, txtAccountNumberStatic;
    private CardView cardViewSetPayment;

    private RecyclerView adminReciepts;
    private RecyclerView.LayoutManager layoutManager;
    private RecieptsCustomAdapter recieptsCustomAdapter;
    List<UserPamisa> pamisaList;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        try{
            btnEditPaymentMethod = findViewById(R.id.btnEditPaymentMethod);
            btnLogoutAccount = findViewById(R.id.btnLogoutAccount);
            topBarPanel = findViewById(R.id.topBarPanel);
            tabLabel = findViewById(R.id.tabLabel);
            btnSortList = findViewById(R.id.btnSortList);
            adminReciepts = findViewById(R.id.adminReciepts);
            btnSavePaymentMethod = findViewById(R.id.btnSavePaymentMethod);
            txtBankName = findViewById(R.id.txtBankName);
            txtAccountNumber = findViewById(R.id.txtAccountNumber);
            txtAccountName = findViewById(R.id.txtAccountName);
            txtDonationAmount = findViewById(R.id.txtDoantionAmount);
            loadingDialog =new LoadingDialog(this);
            txtAccountNameStatic = findViewById(R.id.txtAccountNameStatic);
            txtAccountNumberStatic = findViewById(R.id.txtAccountNumberStatic);
            txtBankNameStatic = findViewById(R.id.txtBankNameStatic);
            txtTotal = findViewById(R.id.txtPaymentTotalStatic);
            cardViewSetPayment = findViewById(R.id.cardViewSetPayment);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#50343434"));
            }

            pamisaList = new ArrayList<>();
            recieptsCustomAdapter = new RecieptsCustomAdapter(pamisaList, this);
            layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
            adminReciepts.setLayoutManager(layoutManager);
            adminReciepts.setAdapter(recieptsCustomAdapter);

            setPayment();
            sortList();
            settings();
            savePayment();
            populate("Allpamisa");
            populatePaymentMethod();
            closeKeyboard();

        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }

    }

    private void setPayment() {
        btnEditPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(cardViewSetPayment.getVisibility() == View.GONE){
                        cardViewSetPayment.setVisibility(View.VISIBLE);
                    }else{
                        cardViewSetPayment.setVisibility(View.GONE);
                    }
                }catch(Exception e){
                    Log.e("Error", "setPayment", e);
                }
            }
        });
    }

    private void populatePaymentMethod() {
        try {
            SessionManagement sessionManagement = new SessionManagement(this);
            String api_key = sessionManagement.getSession2();
            String name = sessionManagement.getAdminName();


            String selectedChurch_url = null;
            String api_url = null;


            try {
                selectedChurch_url = URLEncoder.encode(name.replace("'","\\'"), "utf-8");
                api_url = URLEncoder.encode(api_key.replace("'","\\'"), "utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String URL = "https://cliurches-app.tech/api/paymentMethod/?parish="+ selectedChurch_url +"&api_key="+api_url+"";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {


                                JSONObject object = response.getJSONObject(0);

                                txtBankNameStatic.setText(object.getString("method"));
                                txtAccountNameStatic.setText(object.getString("account_name"));
                                txtAccountNumberStatic.setText(object.getString("account_number"));
                                txtTotal.setText("P" + object.getString("donation") + ".00");

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: ", error);
                }
            }
            );
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
        } catch (Exception e){
            Log.e(TAG, "populatePaymentMethod: ", e);
            toast("Something went wrong");
        }
    }

    private void savePayment() {
        btnSavePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (txtDonationAmount.getText().toString().isEmpty() || txtBankName.getText().toString().isEmpty() || txtAccountNumber.getText().toString().isEmpty() || txtAccountName.getText().toString().isEmpty()) {
                        txtBankName.setError("Required");
                        txtAccountNumber.setError("Required");
                        txtAccountName.setError("Required");
                        txtDonationAmount.setError("Required");
                    } else {
                        txtBankName.setError(null);
                        txtAccountNumber.setError(null);
                        txtAccountName.setError(null);
                        txtDonationAmount.setError(null);

                        SessionManagement sessionManagement = new SessionManagement(AdminActivity.this);
                        String api_key = sessionManagement.getSession2();
                        String church = sessionManagement.getAdminName();

                        String bankName = null;
                        String accntNum = null;
                        String AccntName = null;
                        String don = null;
                        try {
                            bankName = URLEncoder.encode(txtBankName.getText().toString().replace("'","\\'"), "utf-8");
                            accntNum = URLEncoder.encode(txtAccountNumber.getText().toString().replace("'","\\'"), "utf-8");
                            AccntName = URLEncoder.encode(txtAccountName.getText().toString().replace("'","\\'"), "utf-8");
                            don = URLEncoder.encode(txtDonationAmount.getText().toString().replace("'","\\'"), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String JSON_URL = "https://cliurches-app.tech/api/admin/paymentMethod/?bank="+ bankName+"&accountNum="+ accntNum +"&accountName="+ AccntName +"&donationPayment="+ don +"&api_key="+ api_key+"";

                        StringRequest postRequest = new StringRequest(
                                Request.Method.POST,
                                JSON_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        toast("Payment account save");
                                        populatePaymentMethod();
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

                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);

                    }
                } catch (Exception e){
                    toast("Something went wrong");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void populate(String type) {
        try {
            pamisaList.clear();
            SessionManagement sessionManagement = new SessionManagement(this);
            String api_key = sessionManagement.getSession2();
            String church = sessionManagement.getAdminName();

            String JSON_URL = "https://cliurches-app.tech/api/admin/"+type+"/?parish="+church+"&api_key="+ api_key +"";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    JSON_URL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject pamisaObject = response.getJSONObject(i);

                                    UserPamisa item = new UserPamisa();

                                    item.setId(pamisaObject.getString("id"));
                                    item.setRecipient(pamisaObject.getString("recipient"));
                                    item.setDate(pamisaObject.getString("time"));
                                    item.setTime(pamisaObject.getString("date"));
                                    item.setForwhom(pamisaObject.getString("forwhom"));
                                    item.setComment(pamisaObject.getString("comment"));
                                    item.setStatus(pamisaObject.getString("status"));
                                    item.setParish(pamisaObject.getString("parish"));
                                    item.setType(pamisaObject.getString("type"));

                                    pamisaList.add(item);

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }

                            Collections.reverse(pamisaList);
                            recieptsCustomAdapter.updateDataSet(pamisaList);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //toast("Something went wrong");
                    Log.e(TAG, "onErrorResponse: ", error);
                }
            }
            );
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
        } catch (Exception e){
            toast("Something went wrong");
            Log.e(TAG, "showAllPosts: ", e);
        }
    }

    private void settings() {
        btnLogoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SettingsBottomSheetDialog settingsBottomSheetDialog = new SettingsBottomSheetDialog();
                    settingsBottomSheetDialog.show(getSupportFragmentManager(), settingsBottomSheetDialog.getTag());
                } catch (Exception e) {
                    Log.e(TAG, "onCreate: ", e);
                }
            }
        });
    }

    private void sortList() {
        btnSortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Dialog dialog = new Dialog(AdminActivity.this);
                    //We have added a title in the custom layout. So let's disable the default title.
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                    dialog.setCancelable(true);
                    //Mention the name of the layout of your custom dialog.
                    dialog.setContentView(R.layout.dialog_sort_by);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    RadioGroup radioGroupSort = dialog.findViewById(R.id.radioGroupSort);
                    RadioButton rbApproved = dialog.findViewById(R.id.rbApproved);
                    RadioButton rbAllLists = dialog.findViewById(R.id.rbAllLists);
                    RadioButton rbDeclined = dialog.findViewById(R.id.rbDeclined);

                    rbApproved.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            populate("approvedList");
                            dialog.dismiss();
                        }
                    });

                    rbAllLists.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            populate("Allpamisa");
                            dialog.dismiss();
                        }
                    });

                    rbDeclined.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            populate("declinedList");
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }catch(Exception e){
                    Log.e("error", "onClick", e);
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(AdminActivity.this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);

    }

    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}