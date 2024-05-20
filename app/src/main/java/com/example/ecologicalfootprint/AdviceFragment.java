package com.example.ecologicalfootprint;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdviceFragment extends Fragment {
    TextView string_advice;
    ImageView image_advice;
    Button next_advice;
    private String STRING_KEY = "advice_string";
    private FirebaseDatabase mDatabase;
    private int randomValue_string = 1;
    public interface UserListCallback {
        void onCallback(Integer value);
    }

    private void init(View view){
        next_advice = (Button) view.findViewById(R.id.next_advice);
        image_advice = (ImageView) view.findViewById(R.id.image_advice);
        string_advice = (TextView) view.findViewById(R.id.string_advice);

        mDatabase = FirebaseDatabase.getInstance().getReference(STRING_KEY).getDatabase();
    }

    private void getStringFromDB(){

        UserListCallback myCallback = new UserListCallback() {
            @Override
            public void onCallback(Integer value) {
                int minValue_string = 1;
                int maxValue_string = value;
                randomValue_string = minValue_string + (int) (Math.random() * (maxValue_string - minValue_string + 1));

                mDatabase.getReference(STRING_KEY).child(String.valueOf(randomValue_string)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                        } else {
                            String advice_string = task.getResult().getValue(String.class);
                            string_advice.setText(advice_string);
                        }
                    }
                });
            }


        };
        mDatabase.getReference(STRING_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                count = (int)dataSnapshot.getChildrenCount();
                myCallback.onCallback((int)dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Произошел сбой", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);

        init(view);

        getStringFromDB();
        next_advice.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_advice_self));

        return view;
    }
}