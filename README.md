# Google Maps Add-on for Vaadin 7

Enables Google Maps in Vaadin 7 projects via JavaScript API 3!

Get it from [Vaadin Directory](https://vaadin.com/directory#!addon/googlemaps-add-on).

Check out the demo @ [http://tapio.app.fi/googlemaps/](http://tapio.app.fi/googlemaps/). 

## Development notice ##

If you don't use dependency management (i.e. Maven or Ivy) you'll have to manually add `GWT-Maps-V3-Api` and `GWT-AjaxLoader` jars to your project. Without them you'll get errors like

	[ERROR] Unable to find 'com/google/gwt/maps/Maps.gwt.xml' on your classpath;` 
while trying to compile the widgetset.

## Usage notice ##

In order to use Google Maps API, you'll need to get an API key from [Google Developers Console](https://console.developers.google.com/). The key is not normally needed when developing in localhost.

## Supported Features ##

* Setting center & zoom.
* Limiting of center, bounding box and zoom.
* Setting visible map controls.
* Markers with custom captions and icons, togglable dragging, drop animation and optimized rendering.
* Info windows with HTML content, marker anchoring and z-index.
* Polygon and polyline overlays.
* KML layers from external URLs.
* Forcing the localization of the map.
* Toggling dragging of the map with mouse, keyboard shortcuts and scroll wheel zoom.


## Supported Events ##

* Map move
* Map click
* Marker drag
* Marker click
* Info window close