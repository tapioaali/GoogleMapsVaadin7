package com.vaadin.tapio.googlemaps.demo;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class GoogleMapMenu extends CustomComponent {

    public enum LimitState {
        ENABLED, DISABLED;
    }

    private LimitState centerLimitEnabled = LimitState.DISABLED;
    private LimitState viewPortLimitEnabled = LimitState.DISABLED;
    private LimitState draggingEnabled = LimitState.ENABLED;

    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private GoogleMap map;

    @Override
    public DemoUI getUI() {
        return (DemoUI) super.getUI();
    }

    public GoogleMapMenu(GoogleMap map) {
        setPrimaryStyleName("valo-menu");
        setSizeUndefined();
        this.map = map;

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildModifyMapMenu());

        return menuContent;
    }

    private Component buildModifyMapMenu() {
        final CssLayout menuLayout = new CssLayout();
        menuLayout.setWidth("300px");
        Label title = new Label("Modify Map");
        title.addStyleName(ValoTheme.LABEL_LARGE);

        menuLayout.addStyleName("valo-menuitems");

        Button zoomInButton = new Button("Zoom in", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                int oldZoom = map.getZoom();
                int newZoom = oldZoom + 1;
                map.setZoom(newZoom);
                getUI().addToConsole(String.format("Zooming from %s to %s",
                        oldZoom, newZoom));
            }
        });
        zoomInButton.addStyleName(ValoTheme.MENU_ITEM);
        menuLayout.addComponent(zoomInButton);

        Button zoomOutButton = new Button("Zoom out",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        int oldZoom = map.getZoom();
                        int newZoom = oldZoom + 1;
                        map.setZoom(newZoom);
                        getUI().addToConsole(String.format(
                                "Zooming from %s to %s", oldZoom, newZoom));
                    }
                });
        zoomOutButton.addStyleName(ValoTheme.MENU_ITEM);
        menuLayout.addComponent(zoomOutButton);

        Button northButton = new Button("Move one degree to north",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        LatLon position = map.getCenter();
                        LatLon newPosition = new LatLon(position.getLat() - 1,
                                position.getLon());
                        map.setCenter(newPosition);
                        getUI().addToConsole(String.format(
                                "Moving from %s to %s", position, newPosition));
                    }
                });
        northButton.addStyleName(ValoTheme.MENU_ITEM);
        menuLayout.addComponent(northButton);

        Button eastButton = new Button("Move seven degrees to east",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        LatLon position = map.getCenter();
                        LatLon newPosition = new LatLon(position.getLat() + 7,
                                position.getLon());
                        map.setCenter(newPosition);
                        getUI().addToConsole(String.format(
                                "Moving from %s to %s", position, newPosition));
                    }
                });
        eastButton.addStyleName(ValoTheme.MENU_ITEM);
        menuLayout.addComponent(eastButton);

        Button limitCenterButton = new Button("Toggle center limit",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        Button b = event.getButton();
                        if (!b.getStyleName()
                                .contains(ValoTheme.BUTTON_DANGER)) {
                            map.setCenterBoundLimits(
                                    new LatLon(60.619324, 22.712753),
                                    new LatLon(60.373484, 21.945083));
                            map.setCenterBoundLimitsEnabled(true);
                            getUI().addToConsole(String.format(
                                    "Limiting center to (60.619324, 22.712753) & (60.373484, 21.945083)"));
                            b.addStyleName(ValoTheme.BUTTON_DANGER);
                        } else {
                            map.setCenterBoundLimitsEnabled(false);
                            getUI().addToConsole(
                                    String.format("Disabling center limit"));
                            b.removeStyleName(ValoTheme.BUTTON_DANGER);
                        }
                    }
                });
        limitCenterButton.addStyleName(ValoTheme.MENU_ITEM);
        menuLayout.addComponent(limitCenterButton);

        /*
         * Button limitCenterButton = new Button(
         * "Limit center between (60.619324, 22.712753), (60.373484, 21.945083)"
         * , new Button.ClickListener() {
         *
         * @Override public void buttonClick(ClickEvent event) {
         *
         * event.getButton().setEnabled(false); } });
         * buttonLayoutRow1.addComponent(limitCenterButton);
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
         * } });
         *
         * settingsItem.addItem("Edit Profile", new Command() {
         *
         * @Override public void menuSelected(final MenuItem selectedItem) {
         * ProfilePreferencesWindow.open(user, false); } });
         * settingsItem.addItem("Preferences", new Command() {
         *
         * @Override public void menuSelected(final MenuItem selectedItem) {
         * ProfilePreferencesWindow.open(user, true); } });
         * settingsItem.addSeparator(); settingsItem.addItem("Sign Out", new
         * Command() {
         *
         * @Override public void menuSelected(final MenuItem selectedItem) {
         * DashboardEventBus.post(new UserLoggedOutEvent()); } });
         */
        return menuLayout;
    }

    private Component buildTitle() {
        Label logo = new Label("Google Maps<br /><strong>Dashboard</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

}
