package com.vaadin.tapio.googlemaps.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.maps.client.MapImpl;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
import com.google.gwt.maps.client.base.Size;
import com.google.gwt.maps.client.events.center.CenterChangeMapEvent;
import com.google.gwt.maps.client.events.center.CenterChangeMapHandler;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.events.closeclick.CloseClickMapEvent;
import com.google.gwt.maps.client.events.closeclick.CloseClickMapHandler;
import com.google.gwt.maps.client.events.dragend.DragEndMapEvent;
import com.google.gwt.maps.client.events.dragend.DragEndMapHandler;
import com.google.gwt.maps.client.events.idle.IdleMapEvent;
import com.google.gwt.maps.client.events.idle.IdleMapHandler;
import com.google.gwt.maps.client.layers.KmlLayer;
import com.google.gwt.maps.client.layers.KmlLayerOptions;
import com.google.gwt.maps.client.mvc.MVCArray;
import com.google.gwt.maps.client.overlays.Animation;
import com.google.gwt.maps.client.overlays.Circle;
import com.google.gwt.maps.client.overlays.CircleOptions;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.overlays.Polygon;
import com.google.gwt.maps.client.overlays.PolygonOptions;
import com.google.gwt.maps.client.overlays.Polyline;
import com.google.gwt.maps.client.overlays.PolylineOptions;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.layers.GoogleMapKmlLayer;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapCircle;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

public class GoogleMapWidget extends FlowPanel implements RequiresResize {

    public static final String CLASSNAME = "googlemap";
    private MapWidget map;
    private MapImpl mapImpl;

    private MapOptions mapOptions;
    private Map<Marker, GoogleMapMarker> markerMap = new HashMap<Marker, GoogleMapMarker>();
    private Map<GoogleMapMarker, Marker> gmMarkerMap = new HashMap<GoogleMapMarker, Marker>();
    private Map<Polygon, GoogleMapPolygon> polygonMap = new HashMap<Polygon, GoogleMapPolygon>();
    private Map<Polyline, GoogleMapPolyline> polylineMap = new HashMap<Polyline, GoogleMapPolyline>();
    private Map<InfoWindow, GoogleMapInfoWindow> infoWindowMap = new HashMap<InfoWindow, GoogleMapInfoWindow>();
    private Map<Circle, GoogleMapCircle> circleMap = new HashMap<Circle, GoogleMapCircle>();
    private Map<KmlLayer, GoogleMapKmlLayer> kmlLayerMap = new HashMap<KmlLayer, GoogleMapKmlLayer>();
    private MarkerClickListener markerClickListener = null;
    private MarkerDragListener markerDragListener = null;
    private InfoWindowClosedListener infoWindowClosedListener = null;

    private MapMoveListener mapMoveListener = null;
    private LatLngBounds allowedBoundsCenter = null;
    private LatLngBounds allowedBoundsVisibleArea = null;

    private MapClickListener mapClickListener = null;

    private LatLng center = null;
    private int zoom = 0;
    private boolean forceBoundUpdate = false;

    public GoogleMapWidget() {
        setStyleName(CLASSNAME);
    }

    public void initMap(LatLon center, int zoom, String mapTypeId) {
        this.center = LatLng.newInstance(center.getLat(), center.getLon());
        this.zoom = zoom;

        mapOptions = MapOptions.newInstance();
        mapOptions.setMapTypeId(MapTypeId.fromValue(mapTypeId.toLowerCase()));
        mapOptions.setCenter(this.center);
        mapOptions.setZoom(this.zoom);

        mapImpl = MapImpl.newInstance(getElement(), mapOptions);

        map = MapWidget.newInstance(mapImpl);

        // always when center has changed, check that it does not go out from
        // the given bounds
        mapImpl.addCenterChangeHandler(new CenterChangeMapHandler() {
            @Override
            public void onEvent(CenterChangeMapEvent event) {
                forceBoundUpdate = checkVisibleAreaBoundLimits();
                forceBoundUpdate = checkCenterBoundLimits();
            }
        });

        // do all updates when the map has stopped moving
        mapImpl.addIdleHandler(new IdleMapHandler() {
            @Override
            public void onEvent(IdleMapEvent event) {
                updateBounds(forceBoundUpdate);
            }
        });

        mapImpl.addClickHandler(new ClickMapHandler() {
            @Override
            public void onEvent(ClickMapEvent event) {
                if (mapClickListener != null) {
                    LatLon position = new LatLon(event.getMouseEvent().getLatLng().getLatitude(), event
                        .getMouseEvent().getLatLng().getLongitude());
                    mapClickListener.mapClicked(position);
                }
            }
        });
    }

    private boolean checkVisibleAreaBoundLimits() {
        if (allowedBoundsVisibleArea == null) {
            return false;
        }
        double newCenterLat = map.getCenter().getLatitude();
        double newCenterLng = map.getCenter().getLongitude();

        LatLng mapNE = map.getBounds().getNorthEast();
        LatLng mapSW = map.getBounds().getSouthWest();

        LatLng limitNE = allowedBoundsVisibleArea.getNorthEast();
        LatLng limitSW = allowedBoundsVisibleArea.getSouthWest();

        double mapWidth = mapNE.getLongitude() - mapSW.getLongitude();
        double mapHeight = mapNE.getLatitude() - mapSW.getLatitude();

        double maxWidth = limitNE.getLongitude() - limitSW.getLongitude();
        double maxHeight = limitNE.getLatitude() - limitSW.getLatitude();

        if (mapWidth > maxWidth) {
            newCenterLng = allowedBoundsVisibleArea.getCenter().getLongitude();
        } else if (mapNE.getLongitude() > limitNE.getLongitude()) {
            newCenterLng -= (mapNE.getLongitude() - limitNE.getLongitude());
        } else if (mapSW.getLongitude() < limitSW.getLongitude()) {
            newCenterLng += (limitSW.getLongitude() - mapSW.getLongitude());
        }

        if (mapHeight > maxHeight) {
            newCenterLat = allowedBoundsVisibleArea.getCenter().getLatitude();
        } else if (mapNE.getLatitude() > limitNE.getLatitude()) {
            newCenterLat -= (mapNE.getLatitude() - limitNE.getLatitude());
        } else if (mapSW.getLatitude() < limitSW.getLatitude()) {
            newCenterLat += (limitSW.getLatitude() - mapSW.getLatitude());
        }

        if (newCenterLat != map.getCenter().getLatitude()
                || newCenterLng != map.getCenter().getLongitude()) {
            setCenter(new LatLon(newCenterLat, newCenterLng));
            return true;
        }

        return false;
    }

    private void updateBounds(boolean forceUpdate) {
        if (forceUpdate || zoom != map.getZoom() || center == null
                || center.getLatitude() != map.getCenter().getLatitude()
                || center.getLongitude() != map.getCenter().getLongitude()) {
            zoom = map.getZoom();
            center = map.getCenter();
            mapOptions.setZoom(zoom);
            mapOptions.setCenter(center);

            if (mapMoveListener != null) {
                mapMoveListener.mapMoved(map.getZoom(), new LatLon(map
                        .getCenter().getLatitude(), map.getCenter().getLongitude()), new LatLon(
                        map.getBounds().getNorthEast().getLatitude(), map.getBounds()
                                .getNorthEast().getLongitude()), new LatLon(map
                        .getBounds().getSouthWest().getLatitude(), map.getBounds()
                        .getSouthWest().getLongitude()));
            }
        }
    }

    private boolean checkCenterBoundLimits() {
        LatLng center = map.getCenter();
        if (allowedBoundsCenter == null || allowedBoundsCenter.contains(center)) {
            return false;
        }

        double lat = center.getLatitude();
        double lng = center.getLongitude();

        LatLng nortEast = allowedBoundsCenter.getNorthEast();
        LatLng southWest = allowedBoundsCenter.getSouthWest();
        if (lat > nortEast.getLatitude()) {
            lat = nortEast.getLatitude();
        }
        if (lng > nortEast.getLongitude()) {
            lng = nortEast.getLongitude();
        }
        if (lat < southWest.getLatitude()) {
            lat = southWest.getLatitude();
        }
        if (lng < southWest.getLongitude()) {
            lng = southWest.getLongitude();
        }

        setCenter(new LatLon(lat, lng));
        return true;
    }

    public boolean isMapInitiated() {
        return !(map == null);
    }

    public void setCenter(LatLon center) {
        this.center = LatLng.newInstance(center.getLat(), center.getLon());
        mapOptions.setZoom(map.getZoom());
        mapOptions.setCenter(this.center);
        map.panTo(this.center);
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
        mapOptions.setZoom(this.zoom);
        map.setZoom(this.zoom);
    }

    public void setMarkers(Collection<GoogleMapMarker> markers) {

        // clear removed markers
        for (Marker marker : markerMap.keySet()) {
            GoogleMapMarker gMapMarker = markerMap.get(marker);
            if (!markers.contains(gMapMarker)) {
                marker.close();
                gmMarkerMap.remove(gMapMarker);
                markerMap.remove(marker);
            }
        }

        for (GoogleMapMarker googleMapMarker : markers) {
            if (!gmMarkerMap.containsKey(googleMapMarker)) {

                final Marker marker = addMarker(googleMapMarker);
                markerMap.put(marker, googleMapMarker);
                gmMarkerMap.put(googleMapMarker, marker);

                marker.addClickHandler(new ClickMapHandler() {
                    @Override
                    public void onEvent(ClickMapEvent event) {
                        if (markerClickListener != null) {
                            markerClickListener.markerClicked(markerMap
                                .get(marker));
                        }
                    }
                });

                marker.addDragEndHandler(new DragEndMapHandler() {
                    @Override
                    public void onEvent(DragEndMapEvent event) {
                        GoogleMapMarker gMarker = markerMap.get(marker);
                        LatLon oldPosition = gMarker.getPosition();
                        gMarker.setPosition(new LatLon(marker.getPosition()
                            .getLatitude(), marker.getPosition().getLongitude()));

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

        final Marker marker = Marker.newInstance(options);
        marker.setMap(map);

        return marker;
    }

    private MarkerOptions createMarkerOptions(GoogleMapMarker googleMapMarker) {
        LatLng center = LatLng.newInstance(
            googleMapMarker.getPosition().getLat(),
            googleMapMarker.getPosition().getLon());
        MarkerOptions options = MarkerOptions.newInstance();
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
        return map.getCenter().getLatitude();
    }

    public double getLongitude() {
        return map.getCenter().getLongitude();
    }

    public void setCenterBoundLimits(LatLon limitNE, LatLon limitSW) {
        allowedBoundsCenter = LatLngBounds.newInstance(
                LatLng.newInstance(limitSW.getLat(), limitSW.getLon()),
                LatLng.newInstance(limitNE.getLat(), limitNE.getLon()));
    }

    public void clearCenterBoundLimits() {
        allowedBoundsCenter = null;
    }

    public void setVisibleAreaBoundLimits(LatLon limitNE, LatLon limitSW) {
        allowedBoundsVisibleArea = LatLngBounds.newInstance(
            LatLng.newInstance(limitSW.getLat(), limitSW.getLon()),
            LatLng.newInstance(limitNE.getLat(), limitNE.getLon()));
    }

    public void clearVisibleAreaBoundLimits() {
        allowedBoundsVisibleArea = null;
    }

    public void setPolygonOverlays(Set<GoogleMapPolygon> polyOverlays) {
        for (Polygon polygon : polygonMap.keySet()) {
            polygon.setMap(null);
        }
        polygonMap.clear();

        for (GoogleMapPolygon overlay : polyOverlays) {
            MVCArray<LatLng> points = MVCArray.newInstance();
            for (LatLon latLon : overlay.getCoordinates()) {
                LatLng latLng = LatLng.newInstance(latLon.getLat(),
                    latLon.getLon());
                points.push(latLng);
            }

            PolygonOptions options = PolygonOptions.newInstance();
            options.setFillColor(overlay.getFillColor());
            options.setFillOpacity(overlay.getFillOpacity());
            options.setGeodesic(overlay.isGeodesic());
            options.setStrokeColor(overlay.getStrokeColor());
            options.setStrokeOpacity(overlay.getStrokeOpacity());
            options.setStrokeWeight(overlay.getStrokeWeight());
            options.setZindex(overlay.getzIndex());

            Polygon polygon = Polygon.newInstance(options);
            polygon.setPath(points);
            polygon.setMap(map);
            polygonMap.put(polygon, overlay);
        }

    }

    public void setPolylineOverlays(Set<GoogleMapPolyline> polylineOverlays) {
        for (Polyline polyline : polylineMap.keySet()) {
            polyline.setMap(null);
        }
        polylineMap.clear();

        for (GoogleMapPolyline overlay : polylineOverlays) {
            MVCArray<LatLng> points = MVCArray.newInstance();
            for (LatLon latLon : overlay.getCoordinates()) {
                LatLng latLng = LatLng.newInstance(latLon.getLat(),
                    latLon.getLon());
                points.push(latLng);
            }

            PolylineOptions options = PolylineOptions.newInstance();
            options.setGeodesic(overlay.isGeodesic());
            options.setStrokeColor(overlay.getStrokeColor());
            options.setStrokeOpacity(overlay.getStrokeOpacity());
            options.setStrokeWeight(overlay.getStrokeWeight());
            options.setZindex(overlay.getzIndex());

            Polyline polyline = Polyline.newInstance(options);
            polyline.setPath(points);
            polyline.setMap(map);

            polylineMap.put(polyline, overlay);
        }
    }
    
    
    public void setCircleOverlays(Set<GoogleMapCircle> circleOverlays) {
        for (Circle circle : circleMap.keySet()) {
        	circle.setMap(null);
        }
        circleMap.clear();

        for (GoogleMapCircle overlay : circleOverlays) {
            LatLon latLon = overlay.getPosition();
            LatLng latLng = LatLng.newInstance(latLon.getLat(), latLon.getLon());

            CircleOptions options = CircleOptions.newInstance();
            options.setFillColor(overlay.getFillColor());
            options.setFillOpacity(overlay.getFillOpacity());
            options.setStrokeColor(overlay.getStrokeColor());
            options.setStrokeOpacity(overlay.getStrokeOpacity());
            options.setStrokeWeight(overlay.getStrokeWeight());
            options.setRadius(overlay.getRadius());
            options.setCenter(latLng);

            Circle circle = Circle.newInstance(options);
            circle.setMap(map);

            circleMap.put(circle, overlay);
        }
    }
    

    public void setKmlLayers(Collection<GoogleMapKmlLayer> layers) {
        for (KmlLayer kmlLayer : kmlLayerMap.keySet()) {
            kmlLayer.setMap(null);
        }
        kmlLayerMap.clear();

        for (GoogleMapKmlLayer gmLayer : layers) {
            KmlLayerOptions options = KmlLayerOptions.newInstance();
            options.setClickable(gmLayer.isClickable());
            options.setPreserveViewport(gmLayer.isViewportPreserved());
            options.setSuppressInfoWindows(gmLayer
                    .isInfoWindowRenderingDisabled());

            KmlLayer kmlLayer = KmlLayer.newInstance(gmLayer.getUrl(), options);
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
        mapOptions.setScrollWheel(scrollWheelEnabled);
        map.setOptions(mapOptions);
    }

    public void setMinZoom(int minZoom) {
        mapOptions.setMinZoom(minZoom);
        map.setOptions(mapOptions);
    }

    public void setMaxZoom(int maxZoom) {
        mapOptions.setMaxZoom(maxZoom);
        map.setOptions(mapOptions);
    }

    public MapWidget getMap() {
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
            InfoWindowOptions options = InfoWindowOptions.newInstance();
            String contents = gmWindow.getContent();

            // wrap the contents inside a div if there's a defined width or
            // height
            if (gmWindow.getHeight() != null || gmWindow.getWidth() != null) {
                StringBuffer contentWrapper = new StringBuffer("<div style=\"");
                if (gmWindow.getWidth() != null) {
                    contentWrapper.append("width:");
                    contentWrapper.append(gmWindow.getWidth());
                    contentWrapper.append(";");
                }
                if (gmWindow.getHeight() != null) {
                    contentWrapper.append("height:");
                    contentWrapper.append(gmWindow.getHeight());
                    contentWrapper.append(";");
                }
                contentWrapper.append("\" >");
                contentWrapper.append(contents);
                contentWrapper.append("</div>");
                contents = contentWrapper.toString();
            }

            options.setContent(contents);
            options.setDisableAutoPan(gmWindow.isAutoPanDisabled());
            if (gmWindow.getMaxWidth() != null) {
                options.setMaxWidth(gmWindow.getMaxWidth());
            }
            if (gmWindow.getPixelOffsetHeight() != null
                    && gmWindow.getPixelOffsetWidth() != null) {
                options.setPixelOffet(Size.newInstance(
                    gmWindow.getPixelOffsetWidth(),
                    gmWindow.getPixelOffsetHeight()));
            }
            if (gmWindow.getPosition() != null) {
                options.setPosition(LatLng.newInstance(gmWindow.getPosition()
                        .getLat(), gmWindow.getPosition().getLon()));
            }
            if (gmWindow.getzIndex() != null) {
                options.setZindex(gmWindow.getzIndex());
            }
            final InfoWindow window = InfoWindow.newInstance(options);
            if (gmMarkerMap.containsKey(gmWindow.getAnchorMarker())) {
                window.open(map, gmMarkerMap.get(gmWindow.getAnchorMarker()));
            } else {
                window.open(map);
            }
            infoWindowMap.put(window, gmWindow);

            window.addCloseClickHandler(new CloseClickMapHandler() {
                @Override
                public void onEvent(CloseClickMapEvent event) {
                    if (infoWindowClosedListener != null) {
                        infoWindowClosedListener.infoWindowClosed(infoWindowMap
                            .get(window));
                    }
                }
            });
        }
    }

    public void fitToBounds(LatLon boundsNE, LatLon boundsSW) {
        LatLng ne = LatLng.newInstance(boundsNE.getLat(), boundsNE.getLon());
        LatLng sw = LatLng.newInstance(boundsSW.getLat(), boundsSW.getLon());

        LatLngBounds bounds = LatLngBounds.newInstance(sw, ne);
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