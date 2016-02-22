package com.vaadin.tapio.googlemaps.client;

/**
 * Base map types supported by Google Maps.
 */
public enum GoogleMapTypeId {
    HYBRID("Hybrid"), ROADMAP("Roadmap"), SATELLITE("Satellite"), TERRAIN(
            "Terrain");

    private String name;

    private GoogleMapTypeId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLowercaseName() {
        return name.toLowerCase();
    }

    public static GoogleMapTypeId fromValue(String mapId) {
        return valueOf(mapId.toUpperCase());
    }

}
