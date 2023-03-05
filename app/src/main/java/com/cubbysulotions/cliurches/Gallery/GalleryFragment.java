package com.cubbysulotions.cliurches.Gallery;

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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cubbysulotions.cliurches.CustomAdapters.GalleryCustomAdapter;
import com.cubbysulotions.cliurches.CustomAdapters.PostsCustomAdapter;
import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.cubbysulotions.cliurches.Utilities.Gallery;
import com.cubbysulotions.cliurches.Utilities.Posts;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class GalleryFragment extends Fragment implements BackpressedListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    RecyclerView rvGallery;
    private GalleryCustomAdapter galleryCustomAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Gallery> galleryList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGallery = view.findViewById(R.id.rvGallery);
        galleryList = new ArrayList<>();
        galleryCustomAdapter = new GalleryCustomAdapter(galleryList, getActivity());
        layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rvGallery.setLayoutManager(layoutManager);
        rvGallery.setAdapter(galleryCustomAdapter);

        populate();
    }

    private void populate() {
        try {
            galleryList.clear();
            SessionManagement sessionManagement = new SessionManagement(getActivity());
            String JSON_URL = "http://171.22.124.181/cliurches-api/api/media/usergallery/?api_key="+ sessionManagement.getSession2() +"";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    JSON_URL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject postObject = response.getJSONObject(i);

                                    Gallery item = new Gallery();
                                    item.setDateStamp(postObject.getString("dateStamp"));
                                    item.setImgLink(postObject.getString("img_link"));
                                    item.setUid(postObject.getString("uid"));
                                    item.setChurch(postObject.getString("church"));


                                    galleryList.add(item);

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }

                            galleryCustomAdapter.updateDataSet(galleryList);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getClass().equals(ParseError.class)) {
                        // Show timeout error message
                        Toast.makeText(getContext(),
                                "Empty gallery",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onErrorResponse: ", error);
                    }

                }
            }
            );
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);
        } catch (Exception e){
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "showAllPosts: ", e);
        }
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