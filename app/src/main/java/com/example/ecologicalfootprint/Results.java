package com.example.ecologicalfootprint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Results implements Serializable {
    //String id;
    public String timeText, dateText;
    public int result;
    public String login;
    public Results( ){
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeText = timeFormat.format(currentDate);
    }
}
