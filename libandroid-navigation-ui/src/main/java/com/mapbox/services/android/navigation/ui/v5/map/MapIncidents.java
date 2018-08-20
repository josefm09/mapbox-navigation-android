package com.mapbox.services.android.navigation.ui.v5.map;

class MapIncidents {

  private static final String INCIDENTS_LAYER_ID = "incidents";
  private static final String INCIDENTS_TILE_SET_ID = "mapbox.mapbox-incidents-v1";
  private final MapLayerInteractor mapLayerInteractor;

  MapIncidents(MapLayerInteractor mapLayerInteractor) {
    this.mapLayerInteractor = mapLayerInteractor;
  }

  void updateIncidentsVisibility(boolean isVisible) {
    mapLayerInteractor.updateLayerVisibility(isVisible, INCIDENTS_TILE_SET_ID, INCIDENTS_LAYER_ID);
  }

  boolean incidentsVisible() {
    return mapLayerInteractor.isLayerVisibile(INCIDENTS_TILE_SET_ID, INCIDENTS_LAYER_ID);
  }
}
