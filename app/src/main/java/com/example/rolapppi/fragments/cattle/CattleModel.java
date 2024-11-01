package com.example.rolapppi.fragments.cattle;

import static java.time.temporal.ChronoUnit.DAYS;

import com.google.firebase.firestore.DocumentId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CattleModel {
    @DocumentId
    private String animal_id;
    private String birthday;
    private String gender, mother_id;

    private String caliving;
    private String previousCaliving;
    //dodać rasę

    public CattleModel(String animal_id, String birthday, String gender, String mother_id) {
        this.animal_id = animal_id;
        this.birthday = birthday;
        this.gender = gender;
        this.mother_id = mother_id;
        this.caliving = "";
        this.previousCaliving = "";
    }

    public CattleModel() {
    }

    public CattleModel(String animal_id, String birthday, String gender, String mother_id, String caliving, String previousCaliving) {
        this.animal_id = animal_id;
        this.birthday = birthday;
        this.gender = gender;
        this.mother_id = mother_id;
        this.caliving = caliving;
        this.previousCaliving = previousCaliving;
    }

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getBirthday() {
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

    public String getCaliving() {
        return caliving;
    }

    public long getDurationCalving() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate;
        if (!caliving.isEmpty() ) {
            localDate = java.time.LocalDate.parse(caliving, formatter);
            long duration = DAYS.between(localDate, LocalDate.now());
            return duration;
        } else if (!previousCaliving.isEmpty()) {
            localDate = java.time.LocalDate.parse(previousCaliving, formatter);
            long duration = DAYS.between(localDate, LocalDate.now());
            return duration;
        }
        return 0;
    }


    public void setCaliving(String caliving) {
        this.caliving = caliving;
    }

    public String getPreviousCaliving() {
        return previousCaliving;
    }

    public void setPreviousCaliving(String previousCaliving) {
        this.previousCaliving = previousCaliving;
    }

    //zmien date urodzin z String na Date
    //dodać powiadomienie 90 dni plus dla samic?
    public Date getBirthdayDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.parse(birthday);
    }

//    public Date getNotificationDate() throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(getBirthdayDate());
//        calendar.add(Calendar.DATE, 90);
//        return calendar.getTime();
//    }
}