package com.cubbysulotions.cliurches.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnWritePost = view.findViewById(R.id.btnWriteAPost);
        rvPosts = view.findViewById(R.id.posts);
        imgPFP = view.findViewById(R.id.imgPFP);

        writePost();

        SessionManagement sessionManagement = new SessionManagement(getActivity());
        String api_key = sessionManagement.getSession2();

        toast("Message: " + api_key);
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