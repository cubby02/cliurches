package com.cubbysulotions.cliurches.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.CustomAdapters.PostsCustomAdapter;
import com.cubbysulotions.cliurches.LoginSignUp.LoginRegisterActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.Posts;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class ForumFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    private NavController navController;
    private CardView btnWritePost;
    private RecyclerView rvPosts;
    private ImageView imgPFP;
    private PostsCustomAdapter postsCustomAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Posts> postsList;

    private String api_key;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnWritePost = view.findViewById(R.id.btnWriteAPost);
        rvPosts = view.findViewById(R.id.posts);
        imgPFP = view.findViewById(R.id.imgPFP);

        postsList = new ArrayList<>();
        postsCustomAdapter = new PostsCustomAdapter(postsList, getActivity());
        layoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(postsCustomAdapter);


        SessionManagement sessionManagement = new SessionManagement(getActivity());
        api_key = sessionManagement.getSession2();

        writePost();

        showAllPosts();
    }

    private void showAllPosts() {
        try {
            postsList.clear();
            String JSON_URL = "https://cliurches-app.tech/api/view_timeline/?&api_key="+ api_key +"";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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

                                    Posts item = new Posts();
                                    item.setPostId(postObject.getString("postId"));
                                    item.setTimeStamp(postObject.getString("timeStamp"));
                                    item.setDateStamp(postObject.getString("dateStamp"));
                                    item.setUserId(postObject.getString("userId"));
                                    item.setFullname(postObject.getString("fullname"));
                                    item.setPostTitle(postObject.getString("postTitle"));
                                    item.setPostContent(postObject.getString("postContent"));
                                    item.setLikes(postObject.getString("likes"));
                                    item.setCategory(postObject.getString("category"));

                                    postsList.add(item);

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }

                            postsCustomAdapter.updateDataSet(postsList);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    toast("Something went wrong");
                    Log.e(TAG, "onErrorResponse: ", error);
                }
            }
            );
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e){
            toast("Something went wrong");
            Log.e(TAG, "showAllPosts: ", e);
        }
    }

    private void writePost() {
        btnWritePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.action_forumFragment_to_writePostFragment2);
                ((HomeActivity)getActivity()).hideNavigationBar(true);
                ((HomeActivity)getActivity()).hideTopBarPanel(true);
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}