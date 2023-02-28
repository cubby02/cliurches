package com.cubbysulotions.cliurches.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class WritePostFragment extends Fragment implements BackpressedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_post, container, false);
    }

    private NavController navController;
    private Button btnBack, btnPost;
    private ImageView imgPFP;
    private EditText txtPost;
    private TextView txtCharNum;

    private LoadingDialog loadingDialog;
    private String api_key;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnBack = view.findViewById(R.id.btnBackToForum);
        btnPost = view.findViewById(R.id.btnPost);
        imgPFP = view.findViewById(R.id.imgPFP);
        txtPost = view.findViewById(R.id.txtPost);
        txtCharNum = view.findViewById(R.id.txtCharNum);
        loadingDialog = new LoadingDialog(getActivity());

        SessionManagement sessionManagement = new SessionManagement(getActivity());
        api_key = sessionManagement.getSession2();

        txtPost.addTextChangedListener(mTextEditorWatcher);

        back();
        post();
    }

    private void post() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loadingDialog.startLoading("Posting...");
                    String post = null;
                    String api = null;
                    try {
                        post = URLEncoder.encode(txtPost.getText().toString().replace("'","\\'"), "utf-8");
                        api = URLEncoder.encode(api_key.replace("'","\\'"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String JSON_URL = "https://cliurches-app.tech/api/create_post/?title=[post title]&content="+ post +"&category=[post category]&api_key="+ api +"";
                    StringRequest postRequest = new StringRequest(
                            Request.Method.POST,
                            JSON_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    toast("Content posted");
                                    loadingDialog.stopLoading();

                                    closeKeyboard();
                                    navController.navigate(R.id.action_writePostFragment2_to_forumFragment);
                                    ((HomeActivity)getActivity()).hideNavigationBar(false);
                                    ((HomeActivity)getActivity()).hideTopBarPanel(false);
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
                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            txtCharNum.setText(String.valueOf(s.length()));
            if(s.length() > 0){
                btnPost.setEnabled(true);
                btnPost.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
            } else {
                btnPost.setEnabled(false);
                btnPost.setBackground(getResources().getDrawable(R.drawable.btn_rounded_disabled));
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = getActivity().getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_writePostFragment2_to_forumFragment);
                closeKeyboard();
                ((HomeActivity)getActivity()).hideNavigationBar(false);
                ((HomeActivity)getActivity()).hideTopBarPanel(false);
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        navController.navigate(R.id.action_writePostFragment2_to_forumFragment);
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