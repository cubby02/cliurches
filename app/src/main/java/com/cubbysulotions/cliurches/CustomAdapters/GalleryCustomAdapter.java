package com.cubbysulotions.cliurches.CustomAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.DateTimeUtils;
import com.cubbysulotions.cliurches.Utilities.Gallery;
import com.cubbysulotions.cliurches.Utilities.Posts;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GalleryCustomAdapter extends RecyclerView.Adapter<GalleryCustomAdapter.ViewHolder>{
        List<Gallery> posts;
        Context context;


        public GalleryCustomAdapter(List<Gallery> posts, Context context) {
            this.posts = posts;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemVIew = inflater.inflate(R.layout.item_gallery, parent, false);
            ViewHolder vh = new ViewHolder(itemVIew);
            return vh;
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView img;

            public ViewHolder(final View itemView){
                super(itemView);
                img = itemView.findViewById(R.id.img);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull GalleryCustomAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Gallery item = posts.get(position);

            Picasso.get().load(item.getImgLink()).into(holder.img);

        }

        public void updateDataSet(List<Gallery> newResult){
            if(newResult != null){
                posts = newResult;
            }
            notifyDataSetChanged();
        }



}
