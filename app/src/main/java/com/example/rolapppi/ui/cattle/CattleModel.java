package com.example.rolapppi.ui.cattle;

import android.text.format.DateFormat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CattleModel {
    @DocumentId
    private String animal_id;
    private String birthday;
    private String gender, mother_id;

    public CattleModel(String animal_id, String birthday, String gender, String mother_id) {
        this.animal_id = animal_id;
        this.birthday = birthday;
        this.gender = gender;
        this.mother_id = mother_id;
    }

    public CattleModel(){

    }

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getBirthday() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(birthday.toDate());
//        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
//        return date;
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMother_id() {
        return mother_id;
    }

    public void setMother_id(String mother_id) {
        this.mother_id = mother_id;
    }
}