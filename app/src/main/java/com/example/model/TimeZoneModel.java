package com.example.model;

public class TimeZoneModel {
    private int id;
    private String name;
    private String timezone;
    private int status;

    public TimeZoneModel() {
    }

    public TimeZoneModel(int id, String name, String timezone, int status) {
        this.id = id;
        this.name = name;
        this.timezone = timezone;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
