package com.example.rolapppi.fragments.cattle;

import com.google.firebase.firestore.DocumentId;


public class CattleModel {
    @DocumentId
    private String animal_id;
    private String birthday;
    private String gender, mother_id;

    private String caliving;

    public CattleModel(String animal_id, String birthday, String gender, String mother_id) {
        this.animal_id = animal_id;
        this.birthday = birthday;
        this.gender = gender;
        this.mother_id = mother_id;
        this.caliving = "";
    }

    public CattleModel(){ }

    public CattleModel(String animal_id, String birthday, String gender, String mother_id, String caliving) {
        this.animal_id = animal_id;
        this.birthday = birthday;
        this.gender = gender;
        this.mother_id = mother_id;
        this.caliving = caliving;
    }

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getBirthday() { return birthday; }

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

    public void setCaliving(String caliving) {
        this.caliving = caliving;
    }
}