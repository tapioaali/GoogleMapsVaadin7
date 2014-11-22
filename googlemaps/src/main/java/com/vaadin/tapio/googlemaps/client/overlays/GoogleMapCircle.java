package com.vaadin.tapio.googlemaps.client.overlays;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * A class representing a circle overlay of Google Maps.
 * 
 * @author George Perreas (zandinux)
 * 
 */
public class GoogleMapCircle implements Serializable
{
	
	private static final long serialVersionUID = 3884094661917437608L;

	private static long idCounter = 0;

    private long id;

    private LatLon position = new LatLon(0, 0);

    private String fillColor = "#ffffff";
    
    private double fillOpacity = 1.0;
    
    private String strokeColor = "#000000";

    private double strokeOpacity = 1.0;

    private int strokeWeight = 1;
    
    private int radius = 10;

    /**
     * Instantiates a new polygon overlay using default values.
     */
    public GoogleMapCircle() {
        id = idCounter;
        idCounter++;
    }
    
    /**
     * Instantiates a new circle overlay with the given position and radius
     * 
     * @param position
     * @param radius
     */
    public GoogleMapCircle(LatLon position, int radius) {
        this();
        this.position = position;
        this.radius = radius;
    }

    /**
     * Instantiates a new circle layer using the given values.
     * 
     * @param position
     *            The position of the circle.
     * @param fillColor
     *            The fill color of the circle.
     * @param fillOpacity
     *            The fill opacity of the circle.
     * @param strokeColor
     *            The stroke color of the circle.
     * @param strokeOpacity
     *            The stroke opacity of the circle.
     * @param strokeWeight
     *            The stroke weight of the circle.
     * @param radius
     *            The radius of the circle.
     */
    public GoogleMapCircle(LatLon position, String fillColor,
            double fillOpacity, String strokeColor,
            double strokeOpacity, int strokeWeight, int radius) {
        this(position, radius);
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
        this.strokeColor = strokeColor;
        this.strokeOpacity = strokeOpacity;
        this.strokeWeight = strokeWeight;
    }

    public LatLon getPosition() {
        return position;
    }

    public void setPosition(LatLon position) {
        this.position = position;
    }
    
    /**
     * Returns the fill color of the circle.
     * 
     * @return the fill color
     */
    public String getFillColor() {
        return fillColor;
    }

    /**
     * Sets the fill color of the circle.
     * 
     * @param fillColor
     *            The new fill color.
     */
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the fill opacity of the circle.
     * 
     * @return the fill opacity
     */
    public double getFillOpacity() {
        return fillOpacity;
    }
      
    /**
     * Sets the fill opacity of the circle.
     * 
     * @param fillOpacity
     *            The new fill opacity.
     */
    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    /**
     * Returns the stroke color of the circle.
     * 
     * @return the stroke color
     */
    public String getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the stroke color of the circle.
     * 
     * @param strokeColor
     *            The new stroke color.
     */
    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * Returns the stroke opacity of the circle.
     * 
     * @return the stroke opacity
     */
    public double getStrokeOpacity() {
        return strokeOpacity;
    }

    /**
     * Sets the stroke opacity of the circle.
     * 
     * @param strokeOpacity
     *            The new stroke opacity.
     */
    public void setStrokeOpacity(double strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    /**
     * Returns the stroke weight of the circle.
     * 
     * @return the stroke weight
     */
    public int getStrokeWeight() {
        return strokeWeight;
    }

    /**
     * Sets the stroke weight of the circle.
     * 
     * @param strokeWeight
     *            The new stroke weight.
     */
    public void setStrokeWeight(int strokeWeight) {
        this.strokeWeight = strokeWeight;
    }
    
    /**
     * Returns the radius of the circle.
     * 
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the circle.
     * 
     * @param radius
     *            The new radius.
     */
    public void setRadius(int radius) {	
        this.radius = radius;
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
        GoogleMapCircle other = (GoogleMapCircle) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}
