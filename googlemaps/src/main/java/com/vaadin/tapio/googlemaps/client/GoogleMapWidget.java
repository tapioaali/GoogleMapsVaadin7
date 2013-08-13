package com.vaadin.tapio.googlemaps.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polygon;
import com.google.maps.gwt.client.PolygonOptions;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;

public class GoogleMapWidget extends FlowPanel {

    public static final String CLASSNAME = "googlemap";
    private GoogleMap map;
    private MapOptions mapOptions;
    private Map<Marker, GoogleMapMarker> markerMap = new HashMap<Marker, GoogleMapMarker>();
    private Map<Polygon, GoogleMapPolygon> polygonMap = new HashMap<Polygon, GoogleMapPolygon>();
    private Map<Polyline, GoogleMapPolyline> polylineMap = new HashMap<Polyline, GoogleMapPolyline>();
    private MarkerClickListener markerClickListener = null;
    private MarkerDragListener markerDragListener = null;

    private MapMoveListener mapMoveListener = null;
    private LatLngBounds allowedBounds = null;
    private LatLng center = null;
    private double zoom = 0;
    private boolean forceBoundUpdate = false;

    public GoogleMapWidget() {
        setStyleName(CLASSNAME);
    }

    public void initMap(LatLon center, double zoom, String mapTypeId) {
        this.center = LatLng.create(center.getLat(), center.getLon());
        this.zoom = zoom;

        mapOptions = MapOptions.create();
        mapOptions.setMapTypeId(MapTypeId.fromValue(mapTypeId.toLowerCase()));
        mapOptions.setCenter(this.center);
        mapOptions.setZoom(this.zoom);
        map = GoogleMap.create(getElement(), mapOptions);

        // always when center has changed, check that it does not go out from
        // the given bounds
        map.addCenterChangedListener(new GoogleMap.CenterChangedHandler() {
            public void handle() {
                forceBoundUpdate = checkCenterBoundLimits();
            }
        });

        // do all updates when the map has stopped moving
        map.addIdleListener(new GoogleMap.IdleHandler() {
            @Override
            public void handle() {
                updateBounds(forceBoundUpdate);
            }
        });
    }

    private void updateBounds(boolean forceUpdate) {
        if (forceUpdate || zoom != map.getZoom() || center == null
                || center.lat() != map.getCenter().lat()
                || center.lng() != map.getCenter().lng()) {
            zoom = map.getZoom();
            center = map.getCenter();
            mapOptions.setZoom(zoom);
            mapOptions.setCenter(center);

            if (mapMoveListener != null) {
                mapMoveListener.mapMoved(map.getZoom(), new LatLon(map
                        .getCenter().lat(), map.getCenter().lng()), new LatLon(
                        map.getBounds().getNorthEast().lat(), map.getBounds()
                                .getNorthEast().lng()), new LatLon(map
                        .getBounds().getSouthWest().lat(), map.getBounds()
                        .getSouthWest().lng()));
            }
        }
    }

    private boolean checkCenterBoundLimits() {
        LatLng center = map.getCenter();
        if (allowedBounds == null || allowedBounds.contains(center)) {
            return false;
        }

        double lat = center.lat();
        double lng = center.lng();

        LatLng nortEast = allowedBounds.getNorthEast();
        LatLng southWest = allowedBounds.getSouthWest();
        if (lat > nortEast.lat()) {
            lat = nortEast.lat();
        }
        if (lng > nortEast.lng()) {
            lng = nortEast.lng();
        }
        if (lat < southWest.lat()) {
            lat = southWest.lat();
        }
        if (lng < southWest.lng()) {
            lng = southWest.lng();
        }

        if (lat != center.lat() || lng != center.lng()) {
            setCenter(new LatLon(lat, lng));
            return true;
        }
        return false;

    }

    public boolean isMapInitiated() {
        return !(map == null);
    }

    public void setCenter(LatLon center) {
        this.center = LatLng.create(center.getLat(), center.getLon());
        mapOptions.setZoom(map.getZoom());
        mapOptions.setCenter(this.center);
        map.panTo(this.center);
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        mapOptions.setZoom(this.zoom);
        map.setZoom(this.zoom);
    }

    public void setMarkers(Set<GoogleMapMarker> markers) {
        for (Marker marker : markerMap.keySet()) {
            marker.setMap((GoogleMap) null);
        }
        markerMap.clear();

        for (GoogleMapMarker googleMapMarker : markers) {
            final Marker marker = addMarker(googleMapMarker);
            markerMap.put(marker, googleMapMarker);

            marker.addClickListener(new Marker.ClickHandler() {
                @Override
                public void handle(MouseEvent event) {
                    if (markerClickListener != null) {
                        markerClickListener.markerClicked(markerMap.get(marker));
                    }
                }
            });
            marker.addDragEndListener(new Marker.DragEndHandler() {
                @Override
                public void handle(MouseEvent event) {
                    if (markerDragListener != null) {
                        markerDragListener.markerDragged(markerMap.get(marker),
                                new LatLon(marker.getPosition().lat(), marker
                                        .getPosition().lng()));
                    }
                }
            });
        }
    }

    public void setMarkerClickListener(MarkerClickListener listener) {
        markerClickListener = listener;
    }

    public void setMapMoveListener(MapMoveListener listener) {
        mapMoveListener = listener;
    }

    public void setMarkerDragListener(MarkerDragListener listener) {
        markerDragListener = listener;
    }

    private Marker addMarker(GoogleMapMarker googleMapMarker) {
        LatLng center = LatLng.create(googleMapMarker.getPosition().getLat(),
                googleMapMarker.getPosition().getLon());
        MarkerOptions options = MarkerOptions.create();
        options.setPosition(center);
        options.setTitle(googleMapMarker.getCaption());
        options.setDraggable(googleMapMarker.isDraggable());
        options.setAnimation(Animation.DROP);

        final Marker marker = Marker.create(options);
        marker.setMap(map);

        return marker;
    }

    public double getZoom() {
        return map.getZoom();
    }

    public double getLatitude() {
        return map.getCenter().lat();
    }

    public double getLongitude() {
        return map.getCenter().lng();
    }

    public void setCenterBoundLimits(LatLon limitNE, LatLon limitSW) {
        allowedBounds = LatLngBounds.create(
                LatLng.create(limitSW.getLat(), limitSW.getLon()),
                LatLng.create(limitNE.getLat(), limitNE.getLon()));
    }

    public void clearCenterBoundLimits() {
        allowedBounds = null;
    }

    public void setPolygonOverlays(Set<GoogleMapPolygon> polyOverlays) {
        for (Polygon polygon : polygonMap.keySet()) {
            polygon.setMap((GoogleMap) null);
        }
        polygonMap.clear();

        for (GoogleMapPolygon overlay : polyOverlays) {
            MVCArray<LatLng> points = MVCArray.create();
            for (LatLon latLon : overlay.getCoordinates()) {
                LatLng latLng = LatLng.create(latLon.getLat(), latLon.getLon());
                points.push(latLng);
            }

            PolygonOptions options = PolygonOptions.create();
            options.setFillColor(overlay.getFillColor());
            options.setFillOpacity(overlay.getFillOpacity());
            options.setGeodesic(overlay.isGeodesic());
            options.setStrokeColor(overlay.getStrokeColor());
            options.setStrokeOpacity(overlay.getStrokeOpacity());
            options.setStrokeWeight(overlay.getStrokeWeight());
            options.setZindex(overlay.getzIndex());

            Polygon polygon = Polygon.create();
            polygon.setOptions(options);
            polygon.setPath(points);
            polygon.setMap(map);

            polygonMap.put(polygon, overlay);
        }

    }

    public void setPolylineOverlays(Set<GoogleMapPolyline> polylineOverlays) {
        for (Polyline polyline : polylineMap.keySet()) {
            polyline.setMap((GoogleMap) null);
        }
        polylineMap.clear();

        for (GoogleMapPolyline overlay : polylineOverlays) {
            MVCArray<LatLng> points = MVCArray.create();
            for (LatLon latLon : overlay.getCoordinates()) {
                LatLng latLng = LatLng.create(latLon.getLat(), latLon.getLon());
                points.push(latLng);
            }

            PolylineOptions options = PolylineOptions.create();
            options.setGeodesic(overlay.isGeodesic());
            options.setStrokeColor(overlay.getStrokeColor());
            options.setStrokeOpacity(overlay.getStrokeOpacity());
            options.setStrokeWeight(overlay.getStrokeWeight());
            options.setZindex(overlay.getzIndex());

            Polyline polyline = Polyline.create();
            polyline.setOptions(options);
            polyline.setPath(points);
            polyline.setMap(map);

            polylineMap.put(polyline, overlay);
        }
    }

    public void setMapType(String mapTypeId) {
        mapOptions.setMapTypeId(MapTypeId.fromValue(mapTypeId.toLowerCase()));
        map.setOptions(mapOptions);
    }

    public void setControls(Set<GoogleMapControl> controls) {
        mapOptions.setMapTypeControl(controls
                .contains(GoogleMapControl.MapType));
        mapOptions.setOverviewMapControl(controls
                .contains(GoogleMapControl.OverView));
        mapOptions.setPanControl(controls.contains(GoogleMapControl.Pan));
        mapOptions.setRotateControl(controls.contains(GoogleMapControl.Rotate));
        mapOptions.setScaleControl(controls.contains(GoogleMapControl.Scale));
        mapOptions.setStreetViewControl(controls
                .contains(GoogleMapControl.StreetView));
        mapOptions.setZoomControl(controls.contains(GoogleMapControl.Zoom));

        map.setOptions(mapOptions);
    }

    public void setDraggable(boolean draggable) {
        mapOptions.setDraggable(draggable);
        map.setOptions(mapOptions);
    }

    public void setKeyboardShortcutsEnabled(boolean keyboardShortcutsEnabled) {
        mapOptions.setKeyboardShortcuts(keyboardShortcutsEnabled);
        map.setOptions(mapOptions);
    }

    public void setScrollWheelEnabled(boolean scrollWheelEnabled) {
        mapOptions.setScrollwheel(scrollWheelEnabled);
        map.setOptions(mapOptions);
    }

}