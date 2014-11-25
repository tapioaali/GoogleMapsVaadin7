package com.vaadin.tapio.googlemaps.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.tapio.googlemaps.client.layers.GoogleMapKmlLayer;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapCircle;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

/**
 * The shared state of the Google Maps. Contains also the default values.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMapState extends AbstractComponentState {
    private static final long serialVersionUID = 646346522643L;

    public String apiKey = null;
    public String clientId = null;

    // defaults to the language setting of the browser
    public String language = null;
    public String mapTypeId = "Roadmap";
    public LatLon center = new LatLon(51.477811, -0.001475);
    public int zoom = 8;
    public int maxZoom = 21;
    public int minZoom = 0;

    public boolean draggable = true;
    public boolean keyboardShortcutsEnabled = true;
    public boolean scrollWheelEnabled = true;

    public boolean visualRefreshEnabled = false;

    public Set<GoogleMapControl> controls = new HashSet<GoogleMapControl>(
            Arrays.asList(GoogleMapControl.MapType, GoogleMapControl.Pan,
                    GoogleMapControl.Rotate, GoogleMapControl.Scale,
                    GoogleMapControl.StreetView, GoogleMapControl.Zoom));
    public boolean locationFromClient = false;

    public boolean limitCenterBounds = false;
    public LatLon centerSWLimit = new LatLon(0.0, 0.0);
    public LatLon centerNELimit = new LatLon(0.0, 0.0);

    public boolean limitVisibleAreaBounds = false;
    public LatLon visibleAreaSWLimit = new LatLon(0.0, 0.0);
    public LatLon visibleAreaNELimit = new LatLon(0.0, 0.0);

    public LatLon fitToBoundsNE = null;
    public LatLon fitToBoundsSW = null;

   // public Set<GoogleMapPolygon> polygons = new HashSet<GoogleMapPolygon>();
   // public Set<GoogleMapPolyline> polylines = new HashSet<GoogleMapPolyline>();
   // public Set<GoogleMapCircle> circles = new HashSet<GoogleMapCircle>();
    public Set<GoogleMapKmlLayer> kmlLayers = new HashSet<GoogleMapKmlLayer>();

    public Map<Long, GoogleMapMarker> markers = new HashMap<Long, GoogleMapMarker>();
    public Map<Long, GoogleMapPolygon> polygons = new HashMap<Long, GoogleMapPolygon>();
    public Map<Long, GoogleMapPolyline> polylines = new HashMap<Long, GoogleMapPolyline>();
    public Map<Long, GoogleMapCircle> circles = new HashMap<Long, GoogleMapCircle>();
    public Map<Long, GoogleMapInfoWindow> infoWindows = new HashMap<Long, GoogleMapInfoWindow>();
}