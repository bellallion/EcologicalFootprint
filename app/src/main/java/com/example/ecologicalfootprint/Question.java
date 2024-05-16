package com.example.ecologicalfootprint;

import java.io.Serializable;

public class Question implements Serializable {

    String  text;
    String [] answers;
    int[] p; //points
    Question []questions;
    int variants;
    Person person;

    public Question(){
        Person person = new Person();
    };
    public Question(String text,String [] answers, int [] p, int variants, Person person){
        this.person = person;
        this.answers = answers;
        this.p = p;
        this.text = text;
        this.variants = variants;
        questions = new Question[variants];
    }
}
