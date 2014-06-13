# Google Maps Add-on for Vaadin 7

**Remember to get an API key from [https://code.google.com/apis/console/](https://code.google.com/apis/console/). It is normally not required when developing in localhost.**

If you use Google Maps API for Business, check out [https://developers.google.com/maps/documentation/business/clientside/auth](https://developers.google.com/maps/documentation/business/clientside/auth).

Grab it from [Vaadin Directory](https://vaadin.com/directory#addon/googlemaps-add-on:vaadin). Sources of the demo can be from project [googlemaps-demo](https://github.com/tjkaal/GoogleMapsVaadin7/tree/master/googlemaps-demo).

Check out demo @ [http://tapio.app.com/googlemaps/](http://tapio.app.com/googlemaps/). 

As always, remember to compile your widget set and add either 

    widgetset = "org.example.mapsapp.yourwidgetset"

to your `VaadinServletConfiguration` or

	<init-param>
		<param-name>widgetset</param-name>
		<param-value>org.example.mapsapp.yourwidgetset</param-value>
	</init-param>

to web.xml.

## Supported features ##

- Setting center & zoom
- Setting visible map controls
- Limiting maximum & minimum zoom
- Adding & dragging makers
- Adding info windows with size settings
- Adding polygon overlays
- Adding polyline overlays
- Adding KML layers
- Limiting of the center bounds
- Limiting of the visible area bounds
- Enabling/disabling map dragging, keyboard shortcuts and scroll wheel zoom
- Setting language of the map
- Enabling/disabling the new visual style of Google Maps

## Supported events ##
- Map move
- Marker click
- Marker drag
- Map click
- Info window close

## Currently not supported, planned features ##
- Server-side controls for Street View.
- Support for circles.
- Click events for every map overlay (polylines, polygons, circles).
- API for creating extensions for the map, providing an easy way to add support for new features like:
	- Libraries including Drawing, Geometry, Visualization and Weather
	- Services like Geocoder. Directions and Distance Matrix
	
	
Notice that these are not in any order and there's high possibility that some of them will never be implemented.

## Development notice ##

If you want to use the add-on without Maven/Ivy, grab **GWT-Maps-V3-Api** from [https://github.com/branflake2267/GWT-Maps-V3-Api](https://github.com/branflake2267/GWT-Maps-V3-Api).
