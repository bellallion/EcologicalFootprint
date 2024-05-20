package com.example.ecologicalfootprint;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class FirstSurveyFragment extends Fragment {
    Button start;

    private  void init(View view){
        start = (Button) view.findViewById(R.id.start);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_survey, container, false);
        init(view);
        start.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_firstSurveyFragment_to_navigation_survey));


        return view;
    }
}