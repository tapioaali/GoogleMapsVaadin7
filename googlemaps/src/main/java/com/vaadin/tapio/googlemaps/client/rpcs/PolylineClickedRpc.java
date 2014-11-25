package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

public interface PolylineClickedRpc extends ServerRpc 
{
	public void polylineClicked(long polylineId, LatLon position);
}
