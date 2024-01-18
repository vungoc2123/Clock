package com.example.model;

import java.io.Serializable;
import java.util.List;

public class AlarmModel implements Serializable {
    private int id;
    private String label;
    private String time;
    private String repeatDays;
    private String sound;
    private int status;
    private int repeat;

    public AlarmModel() {
    }

    public AlarmModel(int id, String label, String time, String repeatDays, String sound, int status, int repeat) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.repeatDays = repeatDays;
        this.sound = sound;
        this.status = status;
        this.repeat = repeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
