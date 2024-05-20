package com.example.ecologicalfootprint;

import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;

public class Person implements Serializable {
    public String id = null, login = null, email = null, password = null;
    public double result = -1; // текущий
    public double best_result = 0;

    public Person(){};


    public Person(String id, String login, String email, String password) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
    }

}
