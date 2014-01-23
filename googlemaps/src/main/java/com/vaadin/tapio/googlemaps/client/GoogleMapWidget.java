package com.vaadin.tapio.googlemaps.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.KmlLayer;
import com.google.maps.gwt.client.KmlLayerOptions;
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
import com.google.maps.gwt.client.Size;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.layers.GoogleMapKmlLayer;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

public class GoogleMapWidget extends FlowPanel implements RequiresResize {

    public static final String CLASSNAME = "googlemap";
    private GoogleMap map;
    private MapOptions mapOptions;
    private Map<Marker, GoogleMapMarker> markerMap = new HashMap<Marker, GoogleMapMarker>();
    private Map<GoogleMapMarker, Marker> gmMarkerMap = new HashMap<GoogleMapMarker, Marker>();
    private Map<Polygon, GoogleMapPolygon> polygonMap = new HashMap<Polygon, GoogleMapPolygon>();
    private Map<Polyline, GoogleMapPolyline> polylineMap = new HashMap<Polyline, GoogleMapPolyline>();
    private Map<InfoWindow, GoogleMapInfoWindow> infoWindowMap = new HashMap<InfoWindow, GoogleMapInfoWindow>();
    private Map<KmlLayer, GoogleMapKmlLayer> kmlLayerMap = new HashMap<KmlLayer, GoogleMapKmlLayer>();
    private MarkerClickListener markerClickListener = null;
    private MarkerDragListener markerDragListener = null;
    private InfoWindowClosedListener infoWindowClosedListener = null;

    private MapMoveListener mapMoveListener = null;
    private LatLngBounds allowedBoundsCenter = null;
    private LatLngBounds allowedBoundsVisibleArea = null;

    private MapClickListener mapClickListener = null;

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
                forceBoundUpdate = checkVisibleAreaBoundLimits();
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

        map.addClickListener(new GoogleMap.ClickHandler() {
            @Override
            public void handle(MouseEvent event) {

                if (mapClickListener != null) {
                    LatLon position = new LatLon(event.getLatLng().lat(), event
                            .getLatLng().lng());
                    mapClickListener.mapClicked(position);
                }

            }
        });
    }

    private boolean checkVisibleAreaBoundLimits() {
        if (allowedBoundsVisibleArea == null) {
            return false;
        }
        double newCenterLat = map.getCenter().lat();
        double newCenterLng = map.getCenter().lng();

        LatLng mapNE = map.getBounds().getNorthEast();
        LatLng mapSW = map.getBounds().getSouthWest();

        LatLng limitNE = allowedBoundsVisibleArea.getNorthEast();
        LatLng limitSW = allowedBoundsVisibleArea.getSouthWest();

        double mapWidth = mapNE.lng() - mapSW.lng();
        double mapHeight = mapNE.lat() - mapSW.lat();

        double maxWidth = limitNE.lng() - limitSW.lng();
        double maxHeight = limitNE.lat() - limitSW.lat();

        if (mapWidth > maxWidth) {
            newCenterLng = allowedBoundsVisibleArea.getCenter().lng();
        } else if (mapNE.lng() > limitNE.lng()) {
            newCenterLng -= (mapNE.lng() - limitNE.lng());
        } else if (mapSW.lng() < limitSW.lng()) {
            newCenterLng += (limitSW.lng() - mapSW.lng());
        }

        if (mapHeight > maxHeight) {
            newCenterLat = allowedBoundsVisibleArea.getCenter().lat();
        } else if (mapNE.lat() > limitNE.lat()) {
            newCenterLat -= (mapNE.lat() - limitNE.lat());
        } else if (mapSW.lat() < limitSW.lat()) {
            newCenterLat += (limitSW.lat() - mapSW.lat());
        }

        if (newCenterLat != map.getCenter().lat()
                || newCenterLng != map.getCenter().lng()) {
            setCenter(new LatLon(newCenterLat, newCenterLng));
            return true;
        }

        return false;
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
        if (allowedBoundsCenter == null || allowedBoundsCenter.contains(center)) {
            return false;
        }

        double lat = center.lat();
        double lng = center.lng();

        LatLng nortEast = allowedBoundsCenter.getNorthEast();
        LatLng southWest = allowedBoundsCenter.getSouthWest();
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

        setCenter(new LatLon(lat, lng));
        return true;
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

    public void setMarkers(Collection<GoogleMapMarker> markers) {

        // clear removed markers
        for (Marker marker : markerMap.keySet()) {
            GoogleMapMarker gMapMarker = markerMap.get(marker);
            if (!markers.contains(gMapMarker)) {
                marker.setMap((GoogleMap) null);
                gmMarkerMap.remove(gMapMarker);
                markerMap.remove(marker);
            }
        }

        for (GoogleMapMarker googleMapMarker : markers) {
            if (!gmMarkerMap.containsKey(googleMapMarker)) {

                final Marker marker = addMarker(googleMapMarker);
                markerMap.put(marker, googleMapMarker);
                gmMarkerMap.put(googleMapMarker, marker);

                marker.addClickListener(new Marker.ClickHandler() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (markerClickListener != null) {
                            markerClickListener.markerClicked(markerMap
                                    .get(marker));
                        }
                    }
                });
                marker.addDragEndListener(new Marker.DragEndHandler() {
                    @Override
                    public void handle(MouseEvent event) {
                        GoogleMapMarker gMarker = markerMap.get(marker);
                        LatLon oldPosition = gMarker.getPosition();
                        gMarker.setPosition(new LatLon(marker.getPosition()
                                .lat(), marker.getPosition().lng()));

                        if (markerDragListener != null) {
                            markerDragListener.markerDragged(gMarker,
                                    oldPosition);
                        }
                    }
                });
            } else {
                updateMarker(googleMapMarker);
            }
        }
    }

    private void updateMarker(GoogleMapMarker googleMapMarker) {
        Marker marker = gmMarkerMap.get(googleMapMarker);
        GoogleMapMarker oldGmMarker = markerMap.get(marker);

        if (!oldGmMarker.hasSameFieldValues(googleMapMarker)) {
            MarkerOptions options = createMarkerOptions(googleMapMarker);
            marker.setOptions(options);
        }

        gmMarkerMap.put(googleMapMarker, marker);
        markerMap.put(marker, googleMapMarker);
    }

    public void setMarkerClickListener(MarkerClickListener listener) {
        markerClickListener = listener;
    }

    public void setMapMoveListener(MapMoveListener listener) {
        mapMoveListener = listener;
    }

    public void setMapClickListener(MapClickListener listener) {
        mapClickListener = listener;
    }

    public void setMarkerDragListener(MarkerDragListener listener) {
        markerDragListener = listener;
    }

    public void setInfoWindowClosedListener(InfoWindowClosedListener listener) {
        infoWindowClosedListener = listener;
    }

    private Marker addMarker(GoogleMapMarker googleMapMarker) {
        MarkerOptions options = createMarkerOptions(googleMapMarker);

        final Marker marker = Marker.create(options);
        marker.setMap(map);

        return marker;
    }

    private MarkerOptions createMarkerOptions(GoogleMapMarker googleMapMarker) {
        LatLng center = LatLng.create(googleMapMarker.getPosition().getLat(),
                googleMapMarker.getPosition().getLon());
        MarkerOptions options = MarkerOptions.create();
        options.setPosition(center);
        options.setTitle(googleMapMarker.getCaption());
        options.setDraggable(googleMapMarker.isDraggable());
        options.setOptimized(googleMapMarker.isOptimized());

        if (googleMapMarker.isAnimationEnabled()) {
            options.setAnimation(Animation.DROP);
        }

        if (googleMapMarker.getIconUrl() != null) {
            options.setIcon(googleMapMarker.getIconUrl());
        }
        return options;
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
        allowedBoundsCenter = LatLngBounds.create(
                LatLng.create(limitSW.getLat(), limitSW.getLon()),
                LatLng.create(limitNE.getLat(), limitNE.getLon()));
    }

    public void clearCenterBoundLimits() {
        allowedBoundsCenter = null;
    }

    public void setVisibleAreaBoundLimits(LatLon limitNE, LatLon limitSW) {
        allowedBoundsVisibleArea = LatLngBounds.create(
                LatLng.create(limitSW.getLat(), limitSW.getLon()),
                LatLng.create(limitNE.getLat(), limitNE.getLon()));
    }

    public void clearVisibleAreaBoundLimits() {
        allowedBoundsVisibleArea = null;
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

    public void setKmlLayers(Collection<GoogleMapKmlLayer> layers) {
        for (KmlLayer kmlLayer : kmlLayerMap.keySet()) {
            kmlLayer.setMap((GoogleMap) null);
        }
        kmlLayerMap.clear();

        for (GoogleMapKmlLayer gmLayer : layers) {
            KmlLayerOptions options = KmlLayerOptions.create();
            options.setClickable(gmLayer.isClickable());
            options.setPreserveViewport(gmLayer.isViewportPreserved());
            options.setSuppressInfoWindows(gmLayer
                    .isInfoWindowRenderingDisabled());

            KmlLayer kmlLayer = KmlLayer.create(gmLayer.getUrl(), options);
            kmlLayer.setMap(map);

            kmlLayerMap.put(kmlLayer, gmLayer);
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

    public void setMinZoom(double minZoom) {
        mapOptions.setMinZoom(minZoom);
        map.setOptions(mapOptions);
    }

    public void setMaxZoom(double maxZoom) {
        mapOptions.setMaxZoom(maxZoom);
        map.setOptions(mapOptions);
    }

    public GoogleMap getMap() {
        return map;
    }

    public void triggerResize() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                map.triggerResize();
                map.setZoom(zoom);
                map.setCenter(center);
            }
        };
        timer.schedule(20);
    }

    public void setInfoWindows(Collection<GoogleMapInfoWindow> infoWindows) {
        for (InfoWindow window : infoWindowMap.keySet()) {
            window.close();
        }
        infoWindowMap.clear();

        for (GoogleMapInfoWindow gmWindow : infoWindows) {
            InfoWindowOptions options = InfoWindowOptions.create();
            options.setContent(gmWindow.getContent());
            options.setDisableAutoPan(gmWindow.isAutoPanDisabled());
            if (gmWindow.getMaxWidth() != null) {
                options.setMaxWidth(gmWindow.getMaxWidth());
            }
            if (gmWindow.getPixelOffsetHeight() != null
                    && gmWindow.getPixelOffsetWidth() != null) {
                options.setPixelOffset(Size.create(
                        gmWindow.getPixelOffsetWidth(),
                        gmWindow.getPixelOffsetHeight()));
            }
            if (gmWindow.getPosition() != null) {
                options.setPosition(LatLng.create(gmWindow.getPosition()
                        .getLat(), gmWindow.getPosition().getLon()));
            }
            if (gmWindow.getzIndex() != null) {
                options.setZindex(gmWindow.getzIndex());
            }
            final InfoWindow window = InfoWindow.create(options);
            if (gmMarkerMap.containsKey(gmWindow.getAnchorMarker())) {
                window.open(map, gmMarkerMap.get(gmWindow.getAnchorMarker()));
            } else {
                window.open(map);
            }
            infoWindowMap.put(window, gmWindow);

            window.addCloseClickListener(new InfoWindow.CloseClickHandler() {

                @Override
                public void handle() {
                    if (infoWindowClosedListener != null) {
                        infoWindowClosedListener.infoWindowClosed(infoWindowMap
                                .get(window));
                    }
                }
            });

        }
    }

    public void fitToBounds(LatLon boundsNE, LatLon boundsSW) {
        LatLng ne = LatLng.create(boundsNE.getLat(), boundsNE.getLon());
        LatLng sw = LatLng.create(boundsSW.getLat(), boundsSW.getLon());

        LatLngBounds bounds = LatLngBounds.create(sw, ne);
        map.fitBounds(bounds);
        updateBounds(false);
    }

    public native void setVisualRefreshEnabled(boolean enabled)
    /*-{
        $wnd.google.maps.visualRefresh = enabled;
    }-*/;

    @Override
    public void onResize() {
        triggerResize();
    }

}