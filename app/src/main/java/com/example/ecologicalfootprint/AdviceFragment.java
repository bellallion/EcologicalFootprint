package com.example.ecologicalfootprint;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdviceFragment extends Fragment {
    TextView string_advice;
    ImageView image_advice;
    Button next_advice;

    private void init(View view){
        next_advice = (Button) view.findViewById(R.id.next_advice);
        image_advice = (ImageView) view.findViewById(R.id.image_advice);
        string_advice = (TextView) view.findViewById(R.id.string_advice);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);

        init(view);

        Advice advice = new Advice();
        // рандомное число
        int minValue_string = 0;
        int maxValue_string = advice.advices.length-1;
        int randomValue_string = minValue_string + (int) (Math.random() * (maxValue_string - minValue_string + 1));

        string_advice.setText(advice.advices[randomValue_string]);

        advice.drawables.add(getDrawable(getContext(),R.drawable.photo1));
        int minValue_image = 0;
        int maxValue_image = advice.drawables.size()-1;
        int randomValue_image = minValue_image + (int) (Math.random() * (maxValue_image - minValue_image + 1));
        image_advice.setImageDrawable(advice.drawables.get(randomValue_image));
        next_advice.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_advice_self));

        return view;
    }
}