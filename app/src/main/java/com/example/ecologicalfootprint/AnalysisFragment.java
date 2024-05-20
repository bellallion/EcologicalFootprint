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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisFragment extends Fragment {
    private static double procent = 0.0;
    private static double count_better = 0.0;
    private static double count_all = 0.0;

    private static final String key = "res";
    private String USER_KEY = "User";
    private String RES_KEY = "Results";
    private TextView res, profile;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;

    private Person person = new Person();
    private FirebaseDatabase mDatabase, mResults;
    private FirebaseAuth myAuth;


    private void init(View view) {
        listView = (ListView) view.findViewById(R.id.list_results);
        res = (TextView) view.findViewById(R.id.res);
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

    private void getProfileFromDB(){

        mDatabase.getReference(USER_KEY).child(myAuth.getUid()).child("Person").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                } else {
                    Person c_person = task.getResult().getValue(Person.class);
                    if (c_person != null) {
                        person = c_person;
                        profile.setText(person.login);
                    }
                }
            }
        });
    }

//    private void getProsentFromDB(){
//
//        mDatabase.getReference(RES_KEY).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
//                } else {
//                    Results results = task.getResult().getValue(Results.class);
//                    if (results != null) {
//                        Toast.makeText(getContext(), "999", Toast.LENGTH_SHORT).show();
//                        if(results.result > person.result)
//                            count_better++;
//                        count_all++;
//                    }
//                }
//            }
//        });
//        if( count_all == 0)
//            procent = 0;
//        else
//            procent = count_better /count_all*100;
//    }

    // заполнение данных
    private void bestResultChanges(int res){
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
                            listData.add( results.login +  "   "+ results.result + "    " + results.dateText);
                        }
                            count_all++;
                        if(count_all-1 == 0) procent = 0;
                        else procent = count_better/count_all-1;
//                        Toast.makeText(getContext(), String.valueOf(count_all), Toast.LENGTH_SHORT).show();
                        res.setText("Вы лучше " +  String.valueOf(procent)+ "   опросов\n" + results.dateText + "  " + results.timeText + "  " + person.result);
                    }



                }
                adapter.notifyDataSetChanged(); // обновить
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mResults.getReference(RES_KEY).addValueEventListener(valueEventListener);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person = (Person) getArguments().getSerializable(key);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        init(view);
        assert (myAuth.getUid() != null);
            getProfileFromDB();

        if (person.result > person.best_result) {
            person.best_result = person.result;
            bestResultChanges(person.result);
        }
        if (person.result == -1) {

            res.setText("Вы пока не проходили тест");
        } else{
//            Person c_person = getLoginFromDB();
//            if(!c_person.id.toString().equals("")) {
                Results results = new Results();
                results.result = person.result;
                if (myAuth.getUid() != null) {
//                    getProfileFromDB();
//                    DatabaseReference reference = FirebaseDatabase.getInstance()
//                            .getReference(myAuth.getUid())
//                            .child("Person").child("login");
//                    reference.g
                    //getProsentFromDB();
                    results.login = profile.getText().toString();
                    Toast.makeText(getContext(), results.login, Toast.LENGTH_SHORT).show();
                    getDataFromBetterRes();
                    mDatabase.getReference(USER_KEY).child(myAuth.getUid()).push().setValue(results);
                    mResults.getReference(RES_KEY).push().setValue(results);

//                    res.setText("Вы лучше " + String.valueOf(procent) + "   опросов\n" + results.dateText + "  " + results.timeText + "  " + person.result);
                } else {
                    res.setText("Войдите в систему\n" + results.dateText + "  " + results.timeText + "  " + person.result);
                }
//            }else{
//                Toast.makeText(getContext(), "wwwwwwwwwwwwww", Toast.LENGTH_SHORT).show();
//            }

        }
        return view;
    }
}