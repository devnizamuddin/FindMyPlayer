package com.example.findmyplayer.PoJo;

import java.io.Serializable;

public class EventPoJo implements Serializable {

    String id;

    String sports;
    String location;
    String date;

    public EventPoJo(String id, String sports, String location, String date) {
        this.id = id;
        this.sports = sports;
        this.location = location;
        this.date = date;
    }

    public EventPoJo(String sports, String location, String date) {
        this.sports = sports;
        this.location = location;
        this.date = date;
    }

    public EventPoJo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
