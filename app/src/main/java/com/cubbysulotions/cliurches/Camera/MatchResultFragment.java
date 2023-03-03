package com.cubbysulotions.cliurches.Camera;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.cubbysulotions.cliurches.Camera.CameraFragment.IMG_URL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.squareup.picasso.Picasso;

public class MatchResultFragment extends Fragment implements BackpressedListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_result, container, false);
    }

    private NavController navController;
    private Button btnBack, btnSave, btnSearch, btnMaps;
    private TextView txtChurch, txtChurchDescription;
    private ImageView imgChurch;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnBack = view.findViewById(R.id.btnBackToCamera);
        btnSave = view.findViewById(R.id.btnSaveToGallery);
        btnSearch = view.findViewById(R.id.btnGoogleSearch);
        btnMaps = view.findViewById(R.id.btnMaps);
        txtChurch = view.findViewById(R.id.txtChurchName);
        txtChurchDescription = view.findViewById(R.id.txtChurchOverview);
        imgChurch = view.findViewById(R.id.imgSearch);

        Picasso.get().load(getArguments().getString(IMG_URL)).into(imgChurch);
        back();
        search();
        save();

    }

    private void save() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add save code here

                    navController.navigate(R.id.action_matchResultFragment_to_cameraFragment);
                    ((HomeActivity)getActivity()).hideNavigationBar(false);
                    ((HomeActivity)getActivity()).hideTopBarPanel(false);
                } catch (Exception e){
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void search() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // TODO: add search code here
                } catch (Exception e){
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
                navController.navigate(R.id.action_matchResultFragment_to_cameraFragment);
                ((HomeActivity)getActivity()).hideNavigationBar(false);
                ((HomeActivity)getActivity()).hideTopBarPanel(false);
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        navController.navigate(R.id.action_matchResultFragment_to_cameraFragment);
        ((HomeActivity)getActivity()).hideNavigationBar(false);
        ((HomeActivity)getActivity()).hideTopBarPanel(false);
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