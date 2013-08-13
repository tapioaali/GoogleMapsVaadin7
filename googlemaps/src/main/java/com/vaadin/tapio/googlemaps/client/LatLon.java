package com.vaadin.tapio.googlemaps.client;

/**
 * Class representing coordiates of a point.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 * 
 */
public class LatLon {
    private double lat = 0.0;
    private double lon = 0.0;

    public LatLon() {
    }

    public LatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

}
