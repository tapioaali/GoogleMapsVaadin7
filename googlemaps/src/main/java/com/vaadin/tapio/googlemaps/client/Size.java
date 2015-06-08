package com.vaadin.tapio.googlemaps.client;

import java.io.Serializable;

/**
 * Created by andyphillips404 on 6/8/15.
 */
public class Size implements Serializable {
    private static final long serialVersionUID = 1464592723963L;

    private double width = 0;
    private double height = 0;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public Size() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Size point = (Size) o;

        if (width != point.width) return false;
        return height == point.height;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(width);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }}
