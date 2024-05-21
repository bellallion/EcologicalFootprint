package com.example.ecologicalfootprint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignInFragment extends Fragment {
    private EditText mail, password, login;
    private Button sig_up, sig_in;
    private FirebaseAuth myAuth;
    private DatabaseReference mDatabase;
    private String USER_KEY = "User";

    private void init(View view){
        mail = (EditText) view.findViewById(R.id.mail);
        password = (EditText) view.findViewById(R.id.password);
        login = (EditText) view.findViewById(R.id.login);
        sig_up = (Button) view.findViewById(R.id.sig_up);
        sig_in = (Button) view.findViewById(R.id.sig_in);

        myAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser cUser = myAuth.getCurrentUser();
        if(cUser != null) { // пользователь вошел в аккаунт
            mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
            Toast.makeText(getContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
        }
//        else{
//            Toast.makeText(getContext(), "Войдите/авторизуйтесь", Toast.LENGTH_SHORT).show();
//        }

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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        init(view);
        sig_up.setOnClickListener(new View.OnClickListener() {

            // регистрация пользователя
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mail.getText().toString()) && !TextUtils.isEmpty(login.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()))
                {
                    myAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
                                String c_id = myAuth.getUid(); // current
                                String c_login = login.getText().toString();
                                String c_password = password.getText().toString();
                                String c_mail = mail.getText().toString();

                                Person c_person = new Person(c_id,c_login,c_mail, c_password);
                                mDatabase.child(myAuth.getUid()).child("Person").setValue(c_person);


                                Toast.makeText(getContext(), "Пользователь авторизован", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_navigation_signIn_to_navigation_profile);
                            }
                            else{
                                Toast.makeText(getContext(), "Данные пароля или email некорректные", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Данные не введены", Toast.LENGTH_SHORT).show();
                }

            }
        });


        sig_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mail.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    myAuth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY).child(myAuth.getUid());
                                Toast.makeText(getContext(), "Ввод выполнен", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_navigation_signIn_to_navigation_profile);
                            }
                            else{
                                Toast.makeText(getContext(), "Ошибка\nПроверьте корретность данных", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Данные не введены", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}