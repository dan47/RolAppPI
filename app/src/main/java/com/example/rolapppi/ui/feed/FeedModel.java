package com.example.rolapppi.ui.feed;

import com.google.firebase.firestore.DocumentId;

public class FeedModel {

    @DocumentId
    private String id;
    private String purchaseDate;
    private String seller;
    private String producer;
    private String nameFeed;
    private String batch;
    private String count;
    private String packageType;

    public FeedModel(String id, String purchaseDate, String seller, String producer, String nameFeed, String batch, String count, String packageType) {
        this.id = id;
        this.purchaseDate = purchaseDate;
        this.seller = seller;
        this.producer = producer;
        this.nameFeed = nameFeed;
        this.batch = batch;
        this.count = count;
        this.packageType = packageType;
    }

    public FeedModel(String purchaseDate, String seller, String producer, String nameFeed, String batch, String count, String packageType) {
        this.purchaseDate = purchaseDate;
        this.seller = seller;
        this.producer = producer;
        this.nameFeed = nameFeed;
        this.batch = batch;
        this.count = count;
        this.packageType = packageType;
    }

    public FeedModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getNameFeed() {
        return nameFeed;
    }

    public void setNameFeed(String nameFeed) {
        this.nameFeed = nameFeed;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

}
