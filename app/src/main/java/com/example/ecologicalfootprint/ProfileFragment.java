package com.example.ecologicalfootprint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private String USER_KEY = "User";
    private FirebaseAuth myAuth ;
    private Button sign;
    private boolean is_sign_in = false;
    private ListView listView, listViewProf;
    private ArrayAdapter<String> adapter, adapterProf;
    private FirebaseDatabase mDatabase;
    private List<String> listData;
    private List<String> listProf;
    TextView profile, res;


    private  void init(View view){
        res = (TextView) view.findViewById(R.id.res);
        sign = (Button) view.findViewById(R.id.sign);
        listView = (ListView) view.findViewById(R.id.list_results);
        listViewProf = (ListView) view.findViewById(R.id.list_profile);
        listData = new ArrayList<>();
        listProf = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listData);
        adapterProf = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listProf);
        listView.setAdapter(adapter);
        listViewProf.setAdapter(adapterProf);
        myAuth = FirebaseAuth.getInstance();
        profile = (TextView) view.findViewById(R.id.profile);

        if (myAuth.getUid() != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY).getDatabase();
        }

    }

    private String resultString(double res){
        return new DecimalFormat("#00.0").format(res);
    }
    private void getProfileFromDB(){

        mDatabase.getReference(USER_KEY).child(myAuth.getUid()).child("Person").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                } else {
                    Person person = task.getResult().getValue(Person.class);
                    if(person != null) {

                        profile.setText(" "+ person.login+" ");
                        if(listProf.size() > 0) listProf.clear();
                        listProf.add( "Почта:   " + person.email);
                        if(person.best_result >= 0)
                            listProf.add( "Лучший результат = " + resultString(person.best_result));
                        adapterProf.notifyDataSetChanged(); // обновить
                    }
                }
            }
        });
    }


    private void getResFromDB(){

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size() > 0) listData.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Results results = ds.getValue(Results.class);  // выдает все, что есть
                    if(results != null && results.result != -1) {
                        listData.add( "День: " + results.dateText  +  "   Результат:    "+ resultString(results.result) + " га" + "\nВремя: " + results.timeText);
                    }
                }
                adapter.notifyDataSetChanged(); // обновить
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.getReference(USER_KEY).child(myAuth.getUid()).addValueEventListener(valueEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser cUser = myAuth.getCurrentUser();
        if(cUser != null) { // пользователь вошел в аккаунт
            is_sign_in = true;
            sign.setText("Выйти");
            res.setText("Ваши результаты:");

            getProfileFromDB();
        }
        else{
            sign.setText("Войти");
            is_sign_in = false;
            listView.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);

        sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(is_sign_in) {
                    FirebaseAuth.getInstance().signOut();
                }

                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_signInFragment);

            }
        });
        if(myAuth.getUid() != null){
            getResFromDB();
        }

        return view;
    }
}