package com.example.capstonefcls.mainflagments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstonefcls.R;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        v.findViewById(R.id.mapFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mapFragment);
            }
        });

        v.findViewById(R.id.rankFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_rankFragment);
            }
        });

        v.findViewById(R.id.boardFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_boardFragment);
            }
        });


        return v;
    }
}