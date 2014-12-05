package com.vaadin.tapio.googlemaps.client.overlays;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * A class representing a polygon overlay of Google Maps.
 */
public class GoogleMapPolygon implements Serializable {

    private static final long serialVersionUID = 646346543641L;

    private static long idCounter = 0;

    private long id;

    private List<LatLon> coordinates = new ArrayList<LatLon>();

    private String fillColor = "#ffffff";

    private double fillOpacity = 1.0;

    private String strokeColor = "#000000";

    private double strokeOpacity = 1.0;

    private int strokeWeight = 1;

    private int zIndex = 0;

    private boolean geodesic = false;

    /**
     * Instantiates a new polygon overlay using default values.
     */
    public GoogleMapPolygon() {
        id = idCounter;
        idCounter++;
    }

    /**
     * Instantiates a new polygon overlay with the given coordinates, using
     * defaults for other values.
     * 
     * @param coordinates
     *            The ordered sequence of coordinates of the overlay. Note that
     *            the sequence is closed automatically and thus the last value
     *            doesn't have to be same as the first.
     */
    public GoogleMapPolygon(List<LatLon> coordinates) {
        this();
        this.coordinates = coordinates;
    }

    /**
     * Instantiates a new polygon layer using the given values.
     * 
     * @param coordinates
     *            The ordered sequence of coordinates of the overlay. Note that
     *            the sequence is closed automatically and thus the last value
     *            doesn't have to be same as the first.
     * @param fillColor
     *            The fill color of the polygon.
     * @param fillOpacity
     *            The fill opacity of the polygon.
     * @param strokeColor
     *            The stroke color of the polygon.
     * @param strokeOpacity
     *            The stroke opacity of the polygon.
     * @param strokeWeight
     *            The stroke weight of the polygon.
     */
    public GoogleMapPolygon(List<LatLon> coordinates, String fillColor,
            double fillOpacity, String strokeColor, double strokeOpacity,
            int strokeWeight) {
        this(coordinates);
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
        this.strokeColor = strokeColor;
        this.strokeOpacity = strokeOpacity;
        this.strokeWeight = strokeWeight;
    }

    /**
     * Returns the coordinates of the polygon.
     * 
     * @return the coordinates
     */
    public List<LatLon> getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the coordinates of the overlay. Note that the sequence is closed
     * automatically and thus the last value doesn't have to be same as the
     * first.
     * 
     * @param coordinates
     *            the new coordinates
     */
    public void setCoordinates(List<LatLon> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns the fill color of the polygon.
     * 
     * @return the fill color
     */
    public String getFillColor() {
        return fillColor;
    }

    /**
     * Sets the fill color of the polygon.
     * 
     * @param fillColor
     *            The new fill color.
     */
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the fill opacity of the polygon.
     * 
     * @return the fill opacity
     */
    public double getFillOpacity() {
        return fillOpacity;
    }

    /**
     * Sets the fill opacity of the polygon.
     * 
     * @param fillOpacity
     *            The new fill opacity.
     */
    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    /**
     * Returns the stroke color of the polygon.
     * 
     * @return the stroke color
     */
    public String getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the stroke color of the polygon.
     * 
     * @param strokeColor
     *            The new stroke color.
     */
    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * Returns the stroke opacity of the polygon.
     * 
     * @return the stroke opacity
     */
    public double getStrokeOpacity() {
        return strokeOpacity;
    }

    /**
     * Sets the stroke opacity of the polygon.
     * 
     * @param strokeOpacity
     *            The new stroke opacity.
     */
    public void setStrokeOpacity(double strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    /**
     * Returns the stroke weight of the polygon.
     * 
     * @return the stroke weight
     */
    public int getStrokeWeight() {
        return strokeWeight;
    }

    /**
     * Sets the stroke weight of the polygon.
     * 
     * @param strokeWeight
     *            The new stroke weight.
     */
    public void setStrokeWeight(int strokeWeight) {
        this.strokeWeight = strokeWeight;
    }

    /**
     * Returns the z index compared to other polygons.
     * 
     * @return the z index
     */
    public int getzIndex() {
        return zIndex;
    }

    /**
     * Sets the z index compared to other polygons.
     * 
     * @param zIndex
     *            the new z index
     */
    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Checks if the polygon is geodesic.
     * 
     * @return true, if it is geodesic
     */
    public boolean isGeodesic() {
        return geodesic;
    }

    /**
     * Enables/disables geodesicity of the polygon.
     * 
     * @param geodesic
     *            Set true to enable geodesicity.
     */
    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GoogleMapPolygon other = (GoogleMapPolygon) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}
