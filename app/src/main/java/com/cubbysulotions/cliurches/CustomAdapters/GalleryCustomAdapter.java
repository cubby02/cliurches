package com.cubbysulotions.cliurches.CustomAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.helper.widget.MotionEffect;
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


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Dialog dialog = new Dialog(context);
                        //We have added a title in the custom layout. So let's disable the default title.
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                        dialog.setCancelable(true);
                        //Mention the name of the layout of your custom dialog.
                        dialog.setContentView(R.layout.dialog_gallery_image_details);
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.MATCH_PARENT);
                        window.setGravity(Gravity.CENTER);

                        ImageView btnClose = dialog.findViewById(R.id.btnClose);
                        ImageView imgViewGallery = dialog.findViewById(R.id.imgViewGallery);
                        Button btnGoogleSearch = dialog.findViewById(R.id.btnGoogleSearch);
                        Button btnMaps = dialog.findViewById(R.id.btnMaps);
                        TextView txtChurchOverview = dialog.findViewById(R.id.txtChurchOverview);
                        TextView txtChurchName = dialog.findViewById(R.id.txtChurchName);

                        Picasso.get().load(item.getImgLink()).into(imgViewGallery);

                        String church = item.getChurch();

                        switch (church){
                            case "bolo":
                                txtChurchName.setText(context.getResources().getString(R.string.bolo));
                                txtChurchOverview.setText(context.getResources().getString(R.string.bolo_desc));

                                break;
                            case "bauan":
                                txtChurchName.setText(context.getResources().getString(R.string.bauan));
                                txtChurchOverview.setText(context.getResources().getString(R.string.bauan_desc));

                                break;
                            case "aplaya":
                                txtChurchName.setText(context.getResources().getString(R.string.aplaya));
                                txtChurchOverview.setText(context.getResources().getString(R.string.aplaya_desc));

                                break;
                            case "sanpascual":
                                txtChurchName.setText(context.getResources().getString(R.string.sanPascual));
                                txtChurchOverview.setText(context.getResources().getString(R.string.sanPascual_desc));

                                break;
                        }

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    dialog.dismiss();
                                } catch (Exception e){
                                    Toast.makeText(context,"Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onClick", e);
                                }

                            }
                        });

                        btnGoogleSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String church = item.getChurch();
                                Intent intent = null;
                                switch (church){
                                    case "bolo":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.bolo_googleSearch)));
                                        break;
                                    case "bauan":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.bauan_googleSearch)));
                                        break;
                                    case "aplaya":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.aplaya_googleSearch)));
                                        break;
                                    case "sanpascual":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.sanPascual_googleSearch)));
                                        break;
                                }

                                context.startActivity(intent);
                            }
                        });

                        btnMaps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String church = item.getChurch();
                                Intent intent = null;
                                switch (church){
                                    case "bolo":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.bolo_maps)));
                                        break;
                                    case "bauan":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.bauan_maps)));
                                        break;
                                    case "aplaya":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.aplaya_maps)));
                                        break;
                                    case "sanpascual":
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.sanPascual_maps)));
                                        break;
                                }

                                    intent.setPackage("com.google.android.apps.maps");
                                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                                        context.startActivity(intent);
                                    } else {
                                        Toast.makeText(context, "No G-Maps", Toast.LENGTH_SHORT).show();
                                    }

                            }
                        });

                        dialog.show();
                    }catch (Exception e) {
                        Log.e(TAG, "onClick: ", e);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        public void updateDataSet(List<Gallery> newResult){
            if(newResult != null){
                posts = newResult;
            }
            notifyDataSetChanged();
        }



}
