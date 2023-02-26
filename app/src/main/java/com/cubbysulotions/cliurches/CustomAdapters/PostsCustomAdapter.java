package com.cubbysulotions.cliurches.CustomAdapters;

import android.content.Context;
import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.DateTimeUtils;
import com.cubbysulotions.cliurches.Utilities.Posts;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PostsCustomAdapter extends RecyclerView.Adapter<PostsCustomAdapter.ViewHolder>{
        List<Posts> posts;
        Context context;


        public PostsCustomAdapter(List<Posts> posts, Context context) {
            this.posts = posts;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemVIew = inflater.inflate(R.layout.item_forum, parent, false);
            ViewHolder vh = new ViewHolder(itemVIew);
            return vh;
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txtName, txtTime, txtPost, txtNumberOfLikes;
            public Button btnMore;
            public ImageView btnLike;

            public ViewHolder(final View itemView){
                super(itemView);
                txtName = itemView.findViewById(R.id.txtName);
                txtTime = itemView.findViewById(R.id.txtTime);
                txtPost = itemView.findViewById(R.id.txtPost);
                txtNumberOfLikes = itemView.findViewById(R.id.txtNumberOfLikes);
                btnMore = itemView.findViewById(R.id.btnMore);
                btnLike = itemView.findViewById(R.id.btnLike);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull PostsCustomAdapter.ViewHolder holder, int position) {
            Posts item = posts.get(position);

            boolean isLiked = Boolean.parseBoolean(item.getStatus());
            holder.txtTime.setText(DateTimeUtils.timeAgo(item.getDateStamp(), item.getTimeStamp()));
            holder.txtName.setText(item.getFullname());
            holder.txtPost.setText(item.getPostContent());
            holder.txtNumberOfLikes.setText(item.getLikes() + " Likes"); //temp

            if(isLiked){
                holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
            }

            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_24));

                        SessionManagement sessionManagement = new SessionManagement(context);
                        String api_key = sessionManagement.getSession2();

                        String postID = null;
                        String api = null;
                        try {
                            postID = URLEncoder.encode(item.getPostId().replace("'","\\'"), "utf-8");
                            api = URLEncoder.encode(api_key.toString().replace("'","\\'"), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String URL  = "https://cliurches-app.tech/api/sendlike/?postid="+ postID +"&api_key="+ api +"";
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("{\"status\":\"success\"}")){
                                            int likes = Integer.parseInt(item.getLikes());
                                            likes++;
                                            holder.txtNumberOfLikes.setText(likes + " Likes");
                                        } else {
                                            Toast.makeText(context, "Already like", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "onErrorResponse: ", error);
                                    holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24));
                                }
                            }
                        );
                        requestQueue.add(stringRequest);
                    } catch (Exception e) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onClick: ", e);
                    }
                }
            });
        }

        public void updateDataSet(List<Posts> newResult){
            if(newResult != null){
                posts = newResult;
            }
            notifyDataSetChanged();
        }



}
