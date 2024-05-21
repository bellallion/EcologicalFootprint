package com.example.ecologicalfootprint;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisFragment extends Fragment {
    private static double procent = 0.0;
    private static double count_better = 0.0;
    private static double count_all = 0.0;
    private boolean get_result = false;

    private static final String key = "res";
    private String USER_KEY = "User";
    private String RES_KEY = "Results";
    private TextView res, profile, planet;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;

    private Person person = new Person();
    private FirebaseDatabase mDatabase, mResults;
    private FirebaseAuth myAuth;


    private void init(View view) {
        listView = (ListView) view.findViewById(R.id.list_results);
        res = (TextView) view.findViewById(R.id.res);
        planet = (TextView) view.findViewById(R.id.planet);
        listData = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        myAuth = FirebaseAuth.getInstance();
        profile = (TextView) view.findViewById(R.id.profile);

        if (myAuth.getUid() != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(myAuth.getUid()).getDatabase();
            mResults = FirebaseDatabase.getInstance().getReference(RES_KEY).getDatabase();
        }
    }
    private String percent(double res){
        return new DecimalFormat("#00.00").format(res);
    }
    private String resultString(double res){
        return new DecimalFormat("#00.0").format(res);
    }
    private void get_planet(double res){
        if(res <= 1.8) planet.setText("Потребовалась бы 1 планета,\nесли бы все люди жили так же, как вы!");
        else if(res <= 3.6) planet.setText("Потребовалось бы 2 планеты,\nесли бы все люди жили так же, как вы!");
        else if(res <= 5.4) planet.setText("Потребовалось бы 3 планеты,\nесли бы все люди жили так же, как вы!");
        else if(res <= 7.2) planet.setText("Потребовалось бы 4 планеты,\nесли бы все люди жили так же, как вы!");
        else if(res <= 9.0) planet.setText("Потребовалось бы 5 планет,\nесли бы все люди жили так же, как вы!");
        else  planet.setText("Потребовалось бы 6 планет,\nесли бы все люди жили так же, как вы!");
    }

    private void getProfileFromDB(){

        mDatabase.getReference(USER_KEY).child(myAuth.getUid()).child("Person").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                } else {
                    Person c_person = task.getResult().getValue(Person.class);
                    if (c_person != null) {
                        person.login = c_person.login;
                        profile.setText(person.login);
                        Results results = new Results();
                        results.result = person.result;
                        results.login = person.login;
                        if(results.result == -1){
                            res.setText("Вы пока не проходили тест");
                            listView.setVisibility(View.INVISIBLE);
                        }else {
                            mDatabase.getReference(USER_KEY).child(myAuth.getUid()).push().setValue(results);
                            mResults.getReference(RES_KEY).push().setValue(results);
                            getDataFromBetterRes();
                        }
                    }
                }
            }


        });

    }


    // заполнение данных
    private void bestResultChanges(double res){
        Map<String, Object> data= new HashMap<>();
        data.put("best_result", res);
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference(USER_KEY)
                .child(myAuth.getUid())
                .child("Person");

        reference.updateChildren(data, (databaseError, databaseReference) -> {
            if (databaseError == null) {
            } else {
                //произошла ошибка. Она тут: databaseError.toException()
                Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // постоянно обновляется
    private void getDataFromBetterRes(){
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size() > 0) listData.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Results results = ds.getValue(Results.class);  // выдает все, что есть
                    if(results != null ) {
                        if(results.result > person.result){
                            count_better ++;

                        }
                        if(results.result < person.result && !results.login.equals(person.login)){
                            listData.add( results.login +  "   "+ resultString(results.result) + "    " + results.dateText);
                        }
                            count_all++;
                        if(count_all-1 <= 0) {
                            procent = 0;
                            res.setText("День: " + results.dateText + "  Время:  " + results.timeText + "  Результат:  " + resultString(person.result) + " га");
                        }
                        else {
                            procent = count_better/(count_all-1) * 100;
                            res.setText("Ваш результат лучше " +  percent(procent)+ " % других результатов\n" + "День: " + results.dateText + "  Время:  " + results.timeText + "  Результат:  " + resultString(person.result) + " га");
                        }
                        get_planet(results.result);
                    }



                }
                adapter.notifyDataSetChanged(); // обновить
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        count_all = 0;
        count_better = 0;
        mResults.getReference(RES_KEY).addValueEventListener(valueEventListener);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            person = (Person) getArguments().getSerializable(key);
            if(person.result < 0) person.result = 0.1;
            get_result = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        init(view);
// обновление лучшего резцльтатата
        if  (myAuth.getUid() != null) {
            if((person.result >= 0)&&((person.result < person.best_result)||(person.best_result == -1.0))) {
                person.best_result = person.result;
                bestResultChanges(person.result);
            }
        }
        if (myAuth.getUid() != null) {
            getProfileFromDB();
        } else {
            listView.setVisibility(View.INVISIBLE);
            if(get_result) {
                Results results = new Results();
                results.result = person.result;
                res.setText("День: " + results.dateText + "  Время:  " + results.timeText + "  Результат:  " + resultString(person.result) + " га");
                get_planet(person.result);
            }


        }
        return view;
    }
}