package com.example.rolapppi.fragments.cropProtection;

import com.google.firebase.firestore.DocumentId;

public class CropProtectionModel {

    @DocumentId
    private String id;
    private String treatmentTime;
    private String crop;
    private String area;
    private String protectionProduct;
    private String dose;
    private String reason;


    public CropProtectionModel( String treatmentTime, String crop, String area, String protectionProduct, String dose, String reason) {
        this.treatmentTime = treatmentTime;
        this.crop = crop;
        this.area = area;
        this.protectionProduct = protectionProduct;
        this.dose = dose;
        this.reason = reason;
    }

    public CropProtectionModel(String id, String treatmentTime, String crop, String area, String protectionProduct, String dose, String reason) {
        this.id = id;
        this.treatmentTime = treatmentTime;
        this.crop = crop;
        this.area = area;
        this.protectionProduct = protectionProduct;
        this.dose = dose;
        this.reason = reason;
    }

    public CropProtectionModel(){}

    public String getId() {
        return id;
    }


    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProtectionProduct() {
        return protectionProduct;
    }

    public void setProtectionProduct(String protectionProduct) {
        this.protectionProduct = protectionProduct;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
