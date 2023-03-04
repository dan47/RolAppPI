package com.example.rolapppi.user;

public class UserData {
    private String farm_id;

    public UserData() {}

    public UserData(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getFarmId() {
        return farm_id;
    }

    public void setFarmId(String farm_id) {
        this.farm_id = farm_id;
    }
}
