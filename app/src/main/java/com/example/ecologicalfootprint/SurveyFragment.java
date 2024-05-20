package com.example.ecologicalfootprint;

import static android.system.Os.remove;
import static androidx.fragment.app.FragmentManagerKt.commit;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecologicalfootprint.databinding.FragmentSurveyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;


public class SurveyFragment extends Fragment {

    TakeSurvey survey = new TakeSurvey();
    Question  question = survey.current_question;
    private static final String key = "question";
    private static final String key2 = "res";
    private CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6, checkbox7, checkbox8;
    private TextView tv;
    Handler handler;
    private String USER_KEY = "User";
    private FirebaseDatabase mDatabase;
    private FirebaseAuth myAuth ;

    private void init(View view){

        tv = (TextView) view.findViewById(R.id.tv);

        checkbox1 = (CheckBox)view.findViewById(R.id.chb1);
        checkbox2 = (CheckBox)view.findViewById(R.id.chb2);
        checkbox3 = (CheckBox)view.findViewById(R.id.chb3);
        checkbox4 = (CheckBox)view.findViewById(R.id.chb4);
        checkbox5 = (CheckBox)view.findViewById(R.id.chb5);
        checkbox6 = (CheckBox)view.findViewById(R.id.chb6);
        checkbox7 = (CheckBox)view.findViewById(R.id.chb7);
        checkbox8 = (CheckBox)view.findViewById(R.id.chb8);

//        myAuth = FirebaseAuth.getInstance();
//
//        if (myAuth.getUid() != null) {
//            mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY).getDatabase();
//        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(key);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey, container, false);

        init(view);

        int n = question.answers.length;



    //-------------------------------------установка значений

        handler = new Handler(Looper.getMainLooper()) {
            // Looper – запускает цикл обработки сообщений
            // getMainLooper – цикл в главном потоке обработки (UI)
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);
                char[] chars = (char[]) msg.obj;
                String str = String.valueOf(chars);
                tv.setText(str);
            }
        };
        MyThread myThreads = new MyThread(question.text, handler);
        myThreads.start();

        if(n >= 1)
            checkbox1.setText(question.answers[0]);
        if(n >= 2)
            checkbox2.setText(question.answers[1]);
        if(n >= 3)
            checkbox3.setText(question.answers[2]);
        if(n >= 4)
            checkbox4.setText(question.answers[3]);
        if(n >= 5)
            checkbox5.setText(question.answers[4]);
        if(n >= 6)
            checkbox6.setText(question.answers[5]);
        if(n >= 7)
            checkbox7.setText(question.answers[6]);
        if(n >= 8)
            checkbox8.setText(question.answers[7]);


//-----------------------проверка нажатий

        if(n >= 1) {
            checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[0];
                }
            });
        }
        else {
            checkbox1.setVisibility(View.INVISIBLE);
        }
        if(n >= 2) {
            checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[1];
                }
            });
        }
        else {
            checkbox2.setVisibility(View.INVISIBLE);
        }
        if(n >= 3) {
            checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[2];
                }
            });
        }
        else{
            checkbox3.setBackgroundColor(Color.TRANSPARENT);
        }
        if(n >= 4) {
            checkbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[3];
                }
            });
        }
        else {
            checkbox4.setVisibility(View.INVISIBLE);
        }
        if(n >= 5) {
            checkbox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[4];
                }
            });
        }
        else {
            checkbox5.setVisibility(View.INVISIBLE);
        }
        if(n >= 6) {
            checkbox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[5];
                }
            });
        }
        else {
            checkbox6.setVisibility(View.INVISIBLE);
        }
        if(n >= 7) {
            checkbox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[6];
                }
            });
        }else {
            checkbox7.setVisibility(View.INVISIBLE);
        }
        if(n >= 8) {
            checkbox8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked)
                        question.person.result += question.p[7];
                }
            });
        }
        else {
            checkbox8.setVisibility(View.INVISIBLE);
        }
//-----------------------

        Button btn = (Button )view.findViewById(R.id.next);
        Bundle bundle = new Bundle();
        if(question.variants == 0) {
            bundle.putSerializable(key2,  question.person);

            btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_survey_to_navigation_analysis, bundle));

        }
        else {
            bundle.putSerializable(key, question.questions[0]);
            btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_survey_self, bundle));
        }
        return view;
    }

}