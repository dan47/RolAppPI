package com.example.rolapppi.ui.feedProduced;

import com.google.firebase.firestore.DocumentId;

public class FeedProducedModel {

    @DocumentId
    private String id;
    private String nameFeed;
    private String origin;
    private String count;
    private String weight;
    private String destination;
    private String cattleType;
    private String acquisition;

    public FeedProducedModel(String id, String nameFeed, String origin, String count, String weight, String destination, String cattleType, String acquisition) {
        this.id = id;
        this.nameFeed = nameFeed;
        this.origin = origin;
        this.count = count;
        this.weight = weight;
        this.destination = destination;
        this.cattleType = cattleType;
        this.acquisition = acquisition;
    }

    public FeedProducedModel(String nameFeed, String origin, String count, String weight, String destination, String cattleType, String acquisition) {
        this.nameFeed = nameFeed;
        this.origin = origin;
        this.count = count;
        this.weight = weight;
        this.destination = destination;
        this.cattleType = cattleType;
        this.acquisition = acquisition;
    }

    public FeedProducedModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameFeed() {
        return nameFeed;
    }

    public void setNameFeed(String nameFeed) {
        this.nameFeed = nameFeed;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCattleType() {
        return cattleType;
    }

    public void setCattleType(String cattleType) {
        this.cattleType = cattleType;
    }

    public String getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(String acquisition) {
        this.acquisition = acquisition;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
