package com.cubbysulotions.cliurches.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.R;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class WritePostFragment extends Fragment {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnBack = view.findViewById(R.id.btnBackToForum);
        btnPost = view.findViewById(R.id.btnPost);
        imgPFP = view.findViewById(R.id.imgPFP);
        txtPost = view.findViewById(R.id.txtPost);

        back();
        post();
    }

    private void post() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add post code here

                    navController.navigate(R.id.action_writePostFragment2_to_forumFragment);
                    ((HomeActivity)getActivity()).hideNavigationBar(false);
                    ((HomeActivity)getActivity()).hideTopBarPanel(false);
                } catch (Exception e) {
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_writePostFragment2_to_forumFragment);
                ((HomeActivity)getActivity()).hideNavigationBar(false);
                ((HomeActivity)getActivity()).hideTopBarPanel(false);
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}