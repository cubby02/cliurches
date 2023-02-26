package com.cubbysulotions.cliurches.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.Posts;

import java.util.List;

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

        @Override
        public void onBindViewHolder(@NonNull PostsCustomAdapter.ViewHolder holder, int position) {
            Posts item = posts.get(position);

            holder.txtName.setText(item.getFullname());
            holder.txtTime.setText(item.getTimeStamp()); //temp
            holder.txtPost.setText(item.getPostContent());
            holder.txtNumberOfLikes.setText(item.getLikes() + " Likes"); //temp

        }

        public void updateDataSet(List<Posts> newResult){
            if(newResult != null){
                posts = newResult;
            }
            notifyDataSetChanged();
        }



}
