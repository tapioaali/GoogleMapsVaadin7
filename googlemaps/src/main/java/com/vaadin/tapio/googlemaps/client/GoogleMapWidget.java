package com.vaadin.tapio.googlemaps.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    protected MapWidget map;
    protected MapImpl mapImpl;

    protected MapOptions mapOptions;
    protected Map<Marker, GoogleMapMarker> markerMap = new HashMap<Marker, GoogleMapMarker>();
    protected Map<GoogleMapMarker, Marker> gmMarkerMap = new HashMap<GoogleMapMarker, Marker>();
    protected Map<Polygon, GoogleMapPolygon> polygonMap = new HashMap<Polygon, GoogleMapPolygon>();
    protected Map<Polyline, GoogleMapPolyline> polylineMap = new HashMap<Polyline, GoogleMapPolyline>();
    protected Map<InfoWindow, GoogleMapInfoWindow> infoWindowMap = new HashMap<InfoWindow, GoogleMapInfoWindow>();
    protected Map<KmlLayer, GoogleMapKmlLayer> kmlLayerMap = new HashMap<KmlLayer, GoogleMapKmlLayer>();
    protected MarkerClickListener markerClickListener = null;
    protected MarkerDragListener markerDragListener = null;
    protected InfoWindowClosedListener infoWindowClosedListener = null;

    protected MapMoveListener mapMoveListener = null;
    protected LatLngBounds allowedBoundsCenter = null;
    protected LatLngBounds allowedBoundsVisibleArea = null;

    protected MapClickListener mapClickListener = null;

    protected boolean forceBoundUpdate = false;
    protected boolean mapOptionsChanged = false;
    protected boolean panningNeeded = false;

    public GoogleMapWidget() {
        setStyleName(CLASSNAME);
    }

    public void initMap(LatLon center, int zoom, String mapTypeId) {

        mapOptions = MapOptions.newInstance();
        mapOptions.setMapTypeId(MapTypeId.fromValue(mapTypeId.toLowerCase()));
        mapOptions.setCenter(LatLng.newInstance(center.getLat(),
                center.getLon()));
        mapOptions.setZoom(zoom);

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
                    LatLon position = new LatLon(event.getMouseEvent()
                            .getLatLng().getLatitude(), event.getMouseEvent()
                            .getLatLng().getLongitude());
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

        LatLng newCenter = LatLng.newInstance(newCenterLat, newCenterLng);
        if (!newCenter.equals(map.getCenter())) {
            moveCenterTo(newCenter);
            return true;
        }

        return false;
    }

    protected void moveCenterTo(LatLng position) {
        if (!map.getCenter().equals(position)) {
            map.setCenter(position);
        }
    }

    private void updateBounds(boolean forceUpdate) {
        int zoom = mapOptions.getZoom();
        LatLng center = mapOptions.getCenter();

        if (forceUpdate || zoom != map.getZoom() || center == null
                || !center.equals(map.getCenter())) {
            zoom = map.getZoom();
            center = map.getCenter();
            mapOptions.setZoom(zoom);
            mapOptions.setCenter(center);
            mapOptionsChanged = true;

            if (mapMoveListener != null) {
                mapMoveListener.mapMoved(map.getZoom(), new LatLon(map
                        .getCenter().getLatitude(), map.getCenter()
                        .getLongitude()), new LatLon(map.getBounds()
                        .getNorthEast().getLatitude(), map.getBounds()
                        .getNorthEast().getLongitude()), new LatLon(map
                        .getBounds().getSouthWest().getLatitude(), map
                        .getBounds().getSouthWest().getLongitude()));
            }
        }
        updateOptionsAndPanning();
    }

    public void updateOptionsAndPanning() {
        if (panningNeeded) {
            map.panTo(mapOptions.getCenter());
            map.setZoom(mapOptions.getZoom());
            panningNeeded = false;
        }
        if (mapOptionsChanged) {
            map.setOptions(mapOptions);
            mapOptionsChanged = false;
        }
    }

    private boolean checkCenterBoundLimits() {
        LatLng center = map.getCenter();
        if (allowedBoundsCenter == null || allowedBoundsCenter.contains(center)) {
            return false;
        }

        double lat = center.getLatitude();
        double lng = center.getLongitude();

        LatLng northEast = allowedBoundsCenter.getNorthEast();
        LatLng southWest = allowedBoundsCenter.getSouthWest();
        if (lat > northEast.getLatitude()) {
            lat = northEast.getLatitude();
        }
        if (lng > northEast.getLongitude()) {
            lng = northEast.getLongitude();
        }
        if (lat < southWest.getLatitude()) {
            lat = southWest.getLatitude();
        }
        if (lng < southWest.getLongitude()) {
            lng = southWest.getLongitude();
        }

        LatLng newCenter = LatLng.newInstance(lat, lng);
        moveCenterTo(newCenter);
        return true;
    }

    public void setCenter(LatLng center) {
        if (map.getCenter().equals(center)) {
            return;
        }

        mapOptions.setCenter(center);
        mapOptionsChanged = true;
        panningNeeded = true;
    }

    private List<GoogleMapMarker> getRemovedMarkers(
            Collection<GoogleMapMarker> newMarkers) {
        List<GoogleMapMarker> result = new ArrayList<GoogleMapMarker>();

        for (GoogleMapMarker oldMarker : gmMarkerMap.keySet()) {
            if (!newMarkers.contains(oldMarker)) {
                result.add(oldMarker);
            }
        }
        return result;
    }

    private void removeMarkers(List<GoogleMapMarker> markers) {
        for (GoogleMapMarker gmarker : markers) {

            Marker marker = gmMarkerMap.get(gmarker);
            marker.close();
            marker.setMap((MapWidget) null);

            markerMap.remove(marker);
            gmMarkerMap.remove(gmarker);
        }
    }

    public void setMarkers(Collection<GoogleMapMarker> markers) {
        if (markers.size() == markerMap.size()
                && markerMap.values().containsAll(markers)) {
            return;
        }

        List<GoogleMapMarker> removedMarkers = getRemovedMarkers(markers);
        removeMarkers(removedMarkers);

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
                                .getLatitude(), marker.getPosition()
                                .getLongitude()));

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
        LatLng center = LatLng.newInstance(googleMapMarker.getPosition()
                .getLat(), googleMapMarker.getPosition().getLon());
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

    public void setZoom(int zoom) {
        if (mapOptions.getZoom() == zoom) {
            return;
        }
        mapOptions.setZoom(zoom);
        mapOptionsChanged = true;
        panningNeeded = true;
    }

    public LatLng getCenter() {
        return map.getCenter();
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
        if (polygonMap.size() == polyOverlays.size()
                && polygonMap.values().containsAll(polyOverlays)) {
            return;
        }

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
        if (polylineOverlays.size() == polylineMap.size()
                && polylineMap.values().containsAll(polylineOverlays)) {
            return;
        }

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

    public void setKmlLayers(Collection<GoogleMapKmlLayer> layers) {

        // no update needed if old layers match
        if (kmlLayerMap.size() == layers.size()
                && kmlLayerMap.values().containsAll(layers)) {
            return;
        }

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
        MapTypeId id = MapTypeId.fromValue(mapTypeId.toLowerCase());
        if (id == mapOptions.getMapTypeId()) {
            return;
        }
        mapOptions.setMapTypeId(MapTypeId.fromValue(mapTypeId.toLowerCase()));
        mapOptionsChanged = true;
    }

    public void setControls(Set<GoogleMapControl> controls) {

        // check if there's been a real change in selected controls
        Set<GoogleMapControl> currentControls = new HashSet<GoogleMapControl>();
        if (mapOptions.getMapTypeControl()) {
            currentControls.add(GoogleMapControl.MapType);
        }
        if (mapOptions.getOverviewMapControl()) {
            currentControls.add(GoogleMapControl.OverView);
        }
        if (mapOptions.getPanControl()) {
            currentControls.add(GoogleMapControl.Pan);
        }
        if (mapOptions.getRotateControl()) {
            currentControls.add(GoogleMapControl.Rotate);
        }
        if (mapOptions.getScaleControl()) {
            currentControls.add(GoogleMapControl.Scale);
        }
        if (mapOptions.getStreetViewControl()) {
            currentControls.add(GoogleMapControl.StreetView);
        }
        if (mapOptions.getZoomControl()) {
            currentControls.add(GoogleMapControl.Zoom);
        }

        if (controls.size() == currentControls.size()
                && currentControls.containsAll(controls)) {
            return;
        }

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
        mapOptionsChanged = true;

    }

    public void setDraggable(boolean draggable) {
        if (mapOptions.getDraggable() == draggable) {
            return;
        }

        mapOptions.setDraggable(draggable);
        mapOptionsChanged = true;
    }

    public void setKeyboardShortcutsEnabled(boolean keyboardShortcutsEnabled) {
        if (mapOptions.getKeyboardShortcuts() == keyboardShortcutsEnabled) {
            return;
        }
        mapOptions.setKeyboardShortcuts(keyboardShortcutsEnabled);
        mapOptionsChanged = true;
    }

    public void setScrollWheelEnabled(boolean scrollWheelEnabled) {
        if (mapOptions.getScrollWheel() == scrollWheelEnabled) {
            return;
        }
        mapOptions.setScrollWheel(scrollWheelEnabled);
        mapOptionsChanged = true;
    }

    public void setMinZoom(int minZoom) {
        if (mapOptions.getMinZoom() == minZoom) {
            return;
        }
        mapOptions.setMinZoom(minZoom);
        mapOptionsChanged = true;
    }

    public void setMaxZoom(int maxZoom) {
        if (mapOptions.getMaxZoom() == maxZoom) {
            return;
        }
        mapOptions.setMaxZoom(maxZoom);
        mapOptionsChanged = true;
    }

    public MapWidget getMap() {
        return map;
    }

    public void triggerResize() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                map.triggerResize();
                map.setZoom(mapOptions.getZoom());
                map.setCenter(mapOptions.getCenter());
            }
        };
        timer.schedule(20);
    }

    public void setInfoWindows(Collection<GoogleMapInfoWindow> infoWindows) {
        // update only if info windows have really changed
        if (infoWindowMap.size() == infoWindows.size()
                && infoWindowMap.values().containsAll(infoWindows)) {
            return;
        }

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
        if (map.getBounds().equals(bounds)) {
            return;
        }
        map.fitBounds(bounds);
        updateBounds(false);
    }

    @Override
    public void onResize() {
        triggerResize();
    }

}