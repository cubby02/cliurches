package com.cubbysulotions.cliurches.CustomAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.UserPamisa;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecieptsCustomAdapter extends RecyclerView.Adapter<RecieptsCustomAdapter.ViewHolder> {
    List<UserPamisa> pamisaList;
    Context context;

    public RecieptsCustomAdapter(List<UserPamisa> pamisaList, Context context) {
        this.pamisaList = pamisaList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecieptsCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_receipts, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public int getItemCount() {
        return pamisaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtOrderID, txtRecipient, txtDate, txtTime,
                txtPadasalParaKay, txtUriNgPadasal, txtAddMessage,
                txtStatus, txtChurch;
        public Button btnCancel, btnAccept, btnDecline;
        public RelativeLayout adminButtons;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txtOrderID);
            txtRecipient = itemView.findViewById(R.id.txtRecipient);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtPadasalParaKay = itemView.findViewById(R.id.txtPadasalParaKay);
            txtUriNgPadasal = itemView.findViewById(R.id.txtUriNgPadasal);
            txtAddMessage = itemView.findViewById(R.id.txtAddMessage);
            btnCancel = itemView.findViewById(R.id.btnUserCancel);
            adminButtons = itemView.findViewById(R.id.adminButtons);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtChurch = itemView.findViewById(R.id.txtChurch);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecieptsCustomAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            UserPamisa item = pamisaList.get(position);
            SessionManagement sessionManagement = new SessionManagement(context);
            String role = sessionManagement.getAccountType();

            switch (role){
                case "user":
                    holder.adminButtons.setVisibility(View.GONE);
                    holder.btnCancel.setVisibility(View.VISIBLE);
                    break;
                case "admin":
                    holder.adminButtons.setVisibility(View.VISIBLE);
                    holder.btnCancel.setVisibility(View.GONE);
                    break;
            }

            if (item.getStatus().equals("approved") || item.getStatus().equals("declined")){
                holder.adminButtons.setVisibility(View.GONE);
                holder.btnCancel.setVisibility(View.GONE);
            }


            holder.txtOrderID.setText("#" + item.getId());
            holder.txtRecipient.setText(item.getRecipient());
            holder.txtDate.setText(DateTimeUtils.formattedShortDate(item.getDate()));
            holder.txtTime.setText(item.getTime());
            holder.txtPadasalParaKay.setText(item.getForwhom());
            holder.txtUriNgPadasal.setText(item.getTime());
            holder.txtAddMessage.setText(item.getComment());
            holder.txtStatus.setText(item.getStatus());

            String church = item.getParish();
            switch (church){
                case "bauan":
                    holder.txtChurch.setText("Bauan - Immaculate Concepcion Parish");
                    break;
                case "aplaya":
                    holder.txtChurch.setText("Aplaya - Our Mother of Perpetual Help Parish");
                    break;
                case "bolo":
                    holder.txtChurch.setText("Bolo - Holy Family Parish");
                    break;
                case "sanpascual":
                    holder.txtChurch.setText("San Pascual - Baylon Parish");
                    break;
            }

            holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SessionManagement sessionManagement = new SessionManagement(context);
                    String api_key = sessionManagement.getSession2();

                    String postID = null;
                    String api = null;
                    try {
                        postID = URLEncoder.encode(item.getId().replace("'","\\'"), "utf-8");
                        api = URLEncoder.encode(api_key.replace("'","\\'"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String URL  = "http://171.22.124.181/cliurches-api/api/cancelPamisa/?id="+ postID +"&api_key="+ api +"";

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("[\"status\",\"success\"]")){
                                        pamisaList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeRemoved(position, pamisaList.size());
                                        Toast.makeText(context, "Schedule cancelled", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: ", error);
                        }
                    }
                    );
                    VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
                }
            });

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SessionManagement sessionManagement = new SessionManagement(context);
                    String api_key = sessionManagement.getSession2();

                    String postID = null;
                    String api = null;
                    try {
                        postID = URLEncoder.encode(item.getId().replace("'","\\'"), "utf-8");
                        api = URLEncoder.encode(api_key.replace("'","\\'"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String URL  = "http://171.22.124.181/cliurches-api/api/admin/approve/?id="+ postID +"&api_key=" + api+"";

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("[\"status\",\"success\"]")){
                                        Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
                                        holder.txtStatus.setText("approved");
                                        holder.adminButtons.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: ", error);
                        }
                    }
                    );
                    VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
                }
            });

            holder.btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SessionManagement sessionManagement = new SessionManagement(context);
                    String api_key = sessionManagement.getSession2();

                    String postID = null;
                    String api = null;
                    try {
                        postID = URLEncoder.encode(item.getId().replace("'","\\'"), "utf-8");
                        api = URLEncoder.encode(api_key.replace("'","\\'"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String URL  = "http://171.22.124.181/cliurches-api/api/admin/decline/?id="+ postID +"&api_key=" + api+"";

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("[\"status\",\"success\"]")){
                                        Toast.makeText(context, "Declined", Toast.LENGTH_SHORT).show();
                                        holder.txtStatus.setText("declined");
                                        holder.adminButtons.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: ", error);
                        }
                    }
                    );
                    VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
                }
            });



        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ", e);
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


    }


    public void updateDataSet(List<UserPamisa> newResult){
        if(newResult != null){
            pamisaList = newResult;
        }
        notifyDataSetChanged();
    }
}
