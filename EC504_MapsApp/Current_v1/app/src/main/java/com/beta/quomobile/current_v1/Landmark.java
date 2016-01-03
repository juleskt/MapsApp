package com.beta.quomobile.current_v1;

import java.util.ArrayList;
import java.util.List;

public class Landmark implements Comparable<Landmark>
{
    String state, county;
    double lat,lng;

    public Landmark(String state, String county, double latitude, double longitude)
    {
        this.state = state;
        this.county = county;
        lat = latitude;
        lng = longitude;
    }

    public int compareTo(Landmark other)
    {
        return (int)this.lng - (int)other.lng;
    }

    public String toString()
    {
        return state + " " + county + " " + lat + " " + lng;
    }
}
