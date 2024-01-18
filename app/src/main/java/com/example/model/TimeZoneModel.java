package com.example.model;

public class TimeZoneModel {
    private int id;
    private String name;
    private String timezone;

    public TimeZoneModel() {
    }

    public TimeZoneModel(int id, String name, String timezone) {
        this.id = id;
        this.name = name;
        this.timezone = timezone;

    }

    public TimeZoneModel(String name, String timezone) {
        this.name = name;
        this.timezone = timezone;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
