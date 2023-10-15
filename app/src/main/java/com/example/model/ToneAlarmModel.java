package com.example.model;

public class ToneAlarmModel {
    private int id;
    private String name;

    public ToneAlarmModel() {
    }

    public ToneAlarmModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
