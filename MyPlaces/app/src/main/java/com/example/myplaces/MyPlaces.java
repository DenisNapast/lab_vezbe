package com.example.myplaces;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MyPlaces {

    public String name;
    public String description;
    public String longitude = "0.0";
    public String latitude = "0.0";
    @Exclude
    public String key;

    public MyPlaces(){}

    public MyPlaces(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public MyPlaces(String name, String desc, String lat, String lon) {
        this.name = name;
        this.description = desc;
        this.latitude = lat;
        this.longitude = lon;
    }

    public MyPlaces(String nme)
    {
        this.name = nme;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDesc()
    {
        return this.description;
    }

    public void setName(String nme)
    {
        this.name = nme;
    }

    public void setDesc(String desc)
    {
        this.description = desc;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    @Override
    public String toString()
    {
        return this.name;
    }


}
