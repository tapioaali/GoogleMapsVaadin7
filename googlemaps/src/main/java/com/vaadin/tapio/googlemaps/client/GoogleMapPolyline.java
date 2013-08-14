package com.vaadin.tapio.googlemaps.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a poly line (a line consisting of multiple points)
 * overlay of Google Maps.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMapPolyline implements Serializable {

    private static final long serialVersionUID = 646346543563L;

    private List<LatLon> coordinates = new ArrayList<LatLon>();

    private String strokeColor = "#000000";

    private double strokeOpacity = 1.0;

    private int strokeWeight = 1;

    private int zIndex = 0;

    private boolean geodesic = false;

    public GoogleMapPolyline() {
    }

    public GoogleMapPolyline(List<LatLon> coordinates) {
        this.coordinates = coordinates;
    }

    public GoogleMapPolyline(List<LatLon> coordinates, String strokeColor,
            double strokeOpacity, int strokeWeight) {
        this.coordinates = coordinates;
        this.strokeColor = strokeColor;
        this.strokeOpacity = strokeOpacity;
        this.strokeWeight = strokeWeight;
    }

    /**
     * Returns the coordinates of the polyline.
     * 
     * @return coordinates
     * 
     */
    public List<LatLon> getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the coordinates of the polyline.
     * 
     * @param coordinates
     *            the new coordinates
     */
    public void setCoordinates(List<LatLon> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns the stroke color of the polyline.
     * 
     * @return the stroke color
     */
    public String getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the stroke color of the polyline.
     * 
     * @param strokeColor
     *            The new stroke color.
     */
    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * Returns the stroke opacity of the polyline.
     * 
     * @return the stroke opacity
     */
    public double getStrokeOpacity() {
        return strokeOpacity;
    }

    /**
     * Sets the stroke opacity of the polyline.
     * 
     * @param strokeOpacity
     *            The new stroke opacity.
     */
    public void setStrokeOpacity(double strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    /**
     * Returns the stroke weight of the polyline.
     * 
     * @return the stroke weight
     */
    public int getStrokeWeight() {
        return strokeWeight;
    }

    /**
     * Sets the stroke weight of the polyline.
     * 
     * @param strokeWeight
     *            The new stroke weight.
     */
    public void setStrokeWeight(int strokeWeight) {
        this.strokeWeight = strokeWeight;
    }

    /**
     * Returns the z index compared to other polyline.
     * 
     * @return the z index
     */
    public int getzIndex() {
        return zIndex;
    }

    /**
     * Sets the z index compared to other polyline.
     * 
     * @param zIndex
     *            the new z index
     */
    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Checks if the polyline is geodesic.
     * 
     * @return true, if it is geodesic
     */
    public boolean isGeodesic() {
        return geodesic;
    }

    /**
     * Enables/disables geodesicity of the polyline.
     * 
     * @param geodesic
     *            Set true to enable geodesicity.
     */
    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
    }
}
