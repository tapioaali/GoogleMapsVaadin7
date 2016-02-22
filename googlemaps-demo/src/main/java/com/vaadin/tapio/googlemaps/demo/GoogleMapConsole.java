package com.vaadin.tapio.googlemaps.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class GoogleMapConsole extends CustomComponent {

    CssLayout consoleLayout = new CssLayout();

    public GoogleMapConsole() {
        Panel console = new Panel();
        console.setHeight("100px");
        consoleLayout.setSizeFull();
        console.setContent(consoleLayout);
        setCompositionRoot(console);
    }

    public void addItem(String item) {
        SimpleDateFormat stf = new SimpleDateFormat("k:M");
        Date now = Calendar.getInstance().getTime();
        String withDate = "<b>" + stf.format(now) + "</b> " + item;
        consoleLayout.addComponent(new Label(withDate, ContentMode.HTML));
    }
}
