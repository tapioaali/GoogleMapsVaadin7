package com.vaadin.tapio.googlemaps.demo;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.demo.events.OpenInfoWindowOnMarkerClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Google Maps UI for testing and demoing.
 */
@Theme("valo")
public class DemoUI extends UI {

    private GoogleMap googleMap;
    private GoogleMapMarker kakolaMarker = new GoogleMapMarker(
            "DRAGGABLE: Kakolan vankila", new LatLon(60.44291, 22.242415), true,
            null);
    private GoogleMapInfoWindow kakolaInfoWindow = new GoogleMapInfoWindow(
            "Kakola used to be a provincial prison.", kakolaMarker);
    private final String apiKey = "";

    private GoogleMapConsole console = new GoogleMapConsole();

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "com.vaadin.tapio.googlemaps.demo.DemoWidgetset")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        addStyleName(ValoTheme.UI_WITH_MENU);

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        setContent(tabs);

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setCaption("The map");
        tabs.addTab(mainLayout, "The map");
        tabs.addTab(new Label("An another tab"), "The other tab");

        googleMap = new GoogleMap(null, null, null);
        googleMap.setCenter(new LatLon(60.440963, 22.25122));
        googleMap.setZoom(10);
        googleMap.setSizeFull();
        kakolaMarker.setAnimationEnabled(false);
        googleMap.addMarker(kakolaMarker);
        googleMap.addMarker("DRAGGABLE: Paavo Nurmi Stadion",
                new LatLon(60.442423, 22.26044), true,
                "VAADIN/1377279006_stadium.png");
        googleMap.addMarker("NOT DRAGGABLE: Iso-Heikkilä",
                new LatLon(60.450403, 22.230399), false, null);
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);

        kakolaInfoWindow.setWidth("400px");
        kakolaInfoWindow.setHeight("500px");

        GoogleMapMenu menu = new GoogleMapMenu(googleMap);
        mainLayout.addComponent(menu);

        VerticalLayout mapLayout = new VerticalLayout();
        mapLayout.setSizeFull();
        mainLayout.addComponent(mapLayout);
        mainLayout.setExpandRatio(mapLayout, 1.0f);

        mapLayout.addComponent(googleMap);
        mapLayout.setExpandRatio(googleMap, 1.0f);

        console.setHeight("350px");
        mapLayout.addComponent(console);

        OpenInfoWindowOnMarkerClickListener infoWindowOpener = new OpenInfoWindowOnMarkerClickListener(
                googleMap, kakolaMarker, kakolaInfoWindow);
        googleMap.addMarkerClickListener(infoWindowOpener);

        googleMap.addMarkerClickListener(new MarkerClickListener() {
            @Override
            public void markerClicked(GoogleMapMarker clickedMarker) {
                String log = String.format("Marker \"%s\" at (%s, %s) clicked.",
                        clickedMarker.getCaption(),
                        clickedMarker.getPosition().getLat(),
                        clickedMarker.getPosition().getLon());
                console.addItem(log);
            }
        });

        googleMap.addMapMoveListener(new MapMoveListener() {
            @Override
            public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE,
                    LatLon boundsSW) {
                String log = String.format(
                        "Map moved to (%s, %s), zoom %s, boundsNE: (%s, %s), boundsSW: (%s, %s).",
                        center.getLat(), center.getLon(), zoomLevel,
                        boundsNE.getLat(), boundsNE.getLon(), boundsSW.getLat(),
                        boundsSW.getLon());
                console.addItem(log);
            }
        });

        googleMap.addMapClickListener(new MapClickListener() {
            @Override
            public void mapClicked(LatLon position) {
                String log = String.format("Map click to (%s, %s).",
                        position.getLat(), position.getLon());
                console.addItem(log);
            }
        });

        googleMap.addMarkerDragListener(new MarkerDragListener() {
            @Override
            public void markerDragged(GoogleMapMarker draggedMarker,
                    LatLon oldPosition) {
                String log = String.format(
                        "Marker %s dragged from (%s, %s) to (%s, %s)",
                        draggedMarker.getCaption(), oldPosition.getLat(),
                        oldPosition.getLon(),
                        draggedMarker.getPosition().getLat(),
                        draggedMarker.getPosition().getLon());
                console.addItem(log);
            }
        });

        googleMap.addInfoWindowClosedListener(new InfoWindowClosedListener() {

            @Override
            public void infoWindowClosed(GoogleMapInfoWindow window) {
                String log = String.format("InfoWindow \"%s\" closed.",
                        window.getContent());
                console.addItem(log);
            }
        });
        /*
         * googleMap.addMapTypeChangedListener(new MapTypeIdChangedListener() {
         *
         * @Override public void mapTypeIdChanged(GoogleMapTypeId newMapTypeId)
         * { Label consoleEntry = new Label( "Map type changed to " +
         * newMapTypeId.getName()); consoleLayout.addComponent(consoleEntry, 0);
         * } });
         *
         * Button moveCenterButton = new Button(
         * "Move over Luonnonmaa (60.447737, 21.991668), zoom 12", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.setCenter(new LatLon(60.447737, 21.991668));
         * googleMap.setZoom(12); } });
         * buttonLayoutRow1.addComponent(moveCenterButton);
         *
         * Button limitCenterButton = new Button(
         * "Limit center between (60.619324, 22.712753), (60.373484, 21.945083)"
         * , new Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.setCenterBoundLimits( new LatLon(60.619324, 22.712753), new
         * LatLon(60.373484, 21.945083)); event.getButton().setEnabled(false); }
         * }); buttonLayoutRow1.addComponent(limitCenterButton);
         *
         * Button limitVisibleAreaButton = new Button(
         * "Limit visible area between (60.494439, 22.397835), (60.373484, 21.945083)"
         * , new Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.setVisibleAreaBoundLimits( new LatLon(60.494439,
         * 22.397835), new LatLon(60.420632, 22.138626));
         * event.getButton().setEnabled(false); } });
         * buttonLayoutRow1.addComponent(limitVisibleAreaButton);
         *
         * Button zoomToBoundsButton = new Button("Zoom to bounds", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.fitToBounds( new LatLon(60.45685853323144,
         * 22.320034754486073), new LatLon(60.4482979242303,
         * 22.27887893936156));
         *
         * } }); buttonLayoutRow1.addComponent(zoomToBoundsButton); Button
         * addPolyOverlayButton = new Button("Add overlay over Luonnonmaa", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * ArrayList<LatLon> points = new ArrayList<LatLon>(); points.add(new
         * LatLon(60.484715, 21.923706)); points.add(new LatLon(60.446636,
         * 21.941387)); points.add(new LatLon(60.422496, 21.99546));
         * points.add(new LatLon(60.427326, 22.06464)); points.add(new
         * LatLon(60.446467, 22.064297));
         *
         * GoogleMapPolygon overlay = new GoogleMapPolygon(points, "#ae1f1f",
         * 0.8, "#194915", 0.5, 3); googleMap.addPolygonOverlay(overlay);
         * event.getButton().setEnabled(false); } });
         * buttonLayoutRow2.addComponent(addPolyOverlayButton);
         *
         * Button addPolyLineButton = new Button(
         * "Draw line from Turku to Raisio", new Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * ArrayList<LatLon> points = new ArrayList<LatLon>(); points.add(new
         * LatLon(60.448118, 22.253738)); points.add(new LatLon(60.455144,
         * 22.24198)); points.add(new LatLon(60.460222, 22.211939));
         * points.add(new LatLon(60.488224, 22.174602)); points.add(new
         * LatLon(60.486025, 22.169195));
         *
         * GoogleMapPolyline overlay = new GoogleMapPolyline( points, "#d31717",
         * 0.8, 10); googleMap.addPolyline(overlay);
         * event.getButton().setEnabled(false); } });
         * buttonLayoutRow2.addComponent(addPolyLineButton); Button
         * addPolyLineButton2 = new Button( "Draw line from Turku to Raisio2",
         * new Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * ArrayList<LatLon> points2 = new ArrayList<LatLon>(); points2.add(new
         * LatLon(60.448118, 22.253738)); points2.add(new LatLon(60.486025,
         * 22.169195)); GoogleMapPolyline overlay2 = new GoogleMapPolyline(
         * points2, "#d31717", 0.8, 10); googleMap.addPolyline(overlay2);
         * event.getButton().setEnabled(false); } });
         * buttonLayoutRow2.addComponent(addPolyLineButton2); Button
         * changeToTerrainButton = new Button("Change to terrain map", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.setMapType(GoogleMapTypeId.TERRAIN);
         * event.getButton().setEnabled(false); } });
         * buttonLayoutRow2.addComponent(changeToTerrainButton);
         *
         * Button changeControls = new Button("Remove street view control", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.removeControl(GoogleMapControl.StreetView);
         * event.getButton().setEnabled(false); } });
         * buttonLayoutRow2.addComponent(changeControls);
         *
         * Button addInfoWindowButton = new Button(
         * "Add InfoWindow to Kakola marker", new Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.openInfoWindow(kakolaInfoWindow); } });
         * buttonLayoutRow2.addComponent(addInfoWindowButton);
         *
         * Button moveMarkerButton = new Button("Move kakola marker", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * kakolaMarker.setPosition(new LatLon(60.3, 22.242415));
         * googleMap.addMarker(kakolaMarker); } });
         * buttonLayoutRow2.addComponent(moveMarkerButton);
         *
         * Button addKmlLayerButton = new Button("Add KML layer", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         * googleMap.addKmlLayer(new GoogleMapKmlLayer(
         * "http://maps.google.it/maps/" +
         * "ms?authuser=0&ie=UTF8&hl=it&oe=UTF8&msa=0&" +
         * "output=kml&msid=212897908682884215672.0004ecbac547d2d635ff5")); }
         * }); buttonLayoutRow2.addComponent(addKmlLayerButton);
         *
         * Button clearMarkersButton = new Button("Remove all markers", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent clickEvent) {
         * googleMap.clearMarkers(); } });
         * buttonLayoutRow2.addComponent(clearMarkersButton);
         *
         * Button trafficLayerButton = new Button("Toggle Traffic Layer", new
         * Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent clickEvent) {
         * googleMap.setTrafficLayerVisible(
         * !googleMap.isTrafficLayerVisible()); } });
         * buttonLayoutRow2.addComponent(trafficLayerButton);
         */
    }

    public void addToConsole(String text) {
        console.addItem(text);
    }
}