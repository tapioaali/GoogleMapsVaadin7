package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

public interface MapDbClickedRpc extends ServerRpc 
{
	public void mapDbClicked(LatLon position);
}