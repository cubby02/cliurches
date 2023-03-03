package com.cubbysulotions.cliurches.Reciepts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cubbysulotions.cliurches.CustomAdapters.RecieptsCustomAdapter;
import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.UserPamisa;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class RecieptsFragment extends Fragment implements BackpressedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reciepts, container, false);
    }

    private RecyclerView recieptsRv;
    private RecyclerView.LayoutManager layoutManager;
    private RecieptsCustomAdapter recieptsCustomAdapter;
    List<UserPamisa> pamisaList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recieptsRv = view.findViewById(R.id.receiptsRv);
        pamisaList = new ArrayList<>();
        recieptsCustomAdapter = new RecieptsCustomAdapter(pamisaList, getActivity());
        layoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recieptsRv.setLayoutManager(layoutManager);
        recieptsRv.setAdapter(recieptsCustomAdapter);

        populate();

    }

    private void populate() {
        try {
        pamisaList.clear();
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        String api_key = sessionManagement.getSession2();

        String JSON_URL = "http://192.3.236.3/cliurches-api/api/displayPamisa/?api_key="+ api_key +"";

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

                        recieptsCustomAdapter.updateDataSet(pamisaList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getClass().equals(ParseError.class)) {
                    // Show timeout error message
                    Toast.makeText(getContext(),
                            "Empty receipts",
                            Toast.LENGTH_LONG).show();
                } else {
                    toast("Something went wrong");
                    Log.e(TAG, "onErrorResponse: ", error);
                }
            }
        }
        );
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    } catch (Exception e){
        toast("Something went wrong");
        Log.e(TAG, "showAllPosts: ", e);
    }
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        ((HomeActivity) getActivity()).home();
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