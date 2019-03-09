package com.tvsauto.mytvs.holder;

import java.io.Serializable;


public class Map implements Serializable {

    private String name, city;

    public Map(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
