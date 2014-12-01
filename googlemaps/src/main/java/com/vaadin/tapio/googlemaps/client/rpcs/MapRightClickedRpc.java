package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

public interface MapRightClickedRpc extends ServerRpc
{	
	public void mapRightClicked(LatLon position);
}
