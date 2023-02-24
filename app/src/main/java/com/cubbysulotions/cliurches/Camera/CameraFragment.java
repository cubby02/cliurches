package com.cubbysulotions.cliurches.Camera;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class CameraFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    private NavController navController;
    private ImageView imgSearch;
    private Button btnSnapPhoto, btnOpenGallery;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        imgSearch = view.findViewById(R.id.imgSearchChurch);
        btnSnapPhoto = view.findViewById(R.id.btnSnap);
        btnOpenGallery = view.findViewById(R.id.btnGallery);

        snapPhoto();
        openGallery();
    }

    private void openGallery() {
        try {
            // TODO: add open gallery here


        } catch (Exception e){
            toast("Something went wrong, please try again");
            Log.e(TAG, "onClick: ", e);
        }
    }

    private void snapPhoto() {
        btnSnapPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add take picture code here

                    navController.navigate(R.id.action_cameraFragment_to_matchResultFragment);
                    ((HomeActivity)getActivity()).hideNavigationBar(true);
                    ((HomeActivity)getActivity()).hideTopBarPanel(true);
                } catch (Exception e){
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void toast(String msg ){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}