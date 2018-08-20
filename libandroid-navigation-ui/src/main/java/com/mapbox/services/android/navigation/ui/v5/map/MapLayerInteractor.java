package com.mapbox.services.android.navigation.ui.v5.map;

import android.graphics.Bitmap;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

class MapLayerInteractor {

  private MapboxMap mapboxMap;

  MapLayerInteractor(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
  }

  void addLayer(Layer layer) {
    mapboxMap.addLayer(layer);
  }

  Layer retrieveLayerFromId(String layerId) {
    return mapboxMap.getLayerAs(layerId);
  }

  void addLayerImage(String imageName, Bitmap image) {
    mapboxMap.addImage(imageName, image);
  }

  void updateLayerVisibility(boolean isVisible, String tileSetIdentifier, String layerIdentifier) {
    List<String> sourceIdentifiers = findSourceIdentifiersFor(tileSetIdentifier);
    List<Layer> layers = mapboxMap.getLayers();
    updateLayerVisibility(layerIdentifier, sourceIdentifiers, layers, isVisible);
  }

  boolean isLayerVisibile(String tileSetIdentifier, String layerIdentifier) {
    List<String> sourceIdentifiers = findSourceIdentifiersFor(tileSetIdentifier);
    List<Layer> layers = mapboxMap.getLayers();
    return isLayerVisible(layerIdentifier, sourceIdentifiers, layers);
  }

  private List<String> findSourceIdentifiersFor(String tileSetIdentifier) {
    List<String> sourceIdentifiers = new ArrayList<>();
    List<Source> sources = mapboxMap.getSources();
    for (Source source : sources) {
      boolean isVectorSource = source instanceof VectorSource;
      boolean isValidSource = isVectorSource
        && ((VectorSource) source).getUrl() != null
        && ((VectorSource) source).getUrl().contains(tileSetIdentifier);
      if (isValidSource) {
        sourceIdentifiers.add(source.getId());
      }
    }
    return sourceIdentifiers;
  }

  private void updateLayerVisibility(String layerIdentifier, List<String> sourceIdentifiers,
                                     List<Layer> layers, boolean isVisible) {
    for (Layer layer : layers) {
      if (!(layer instanceof LineLayer)) {
        continue;
      }
      Timber.d("***** ***** ***** *****");
      String sourceIdentifier = ((LineLayer) layer).getSourceLayer();
      Timber.d("sourceIdentifier %s", sourceIdentifier);
      String layerId = layer.getId();
      Timber.d("layerId %s", layerId);
      if (sourceIdentifiers.contains(sourceIdentifier) && layerId.contains(layerIdentifier)) {
        layer.setProperties(visibility(isVisible ? VISIBLE : NONE));
      }
    }
  }

  private boolean isLayerVisible(String layerIdentifier, List<String> sourceIdentifiers, List<Layer> layers) {
    for (Layer layer : layers) {
      if (!(layer instanceof LineLayer)) {
        continue;
      }
      String sourceIdentifier = ((LineLayer) layer).getSourceLayer();
      String layerId = layer.getId();
      if (sourceIdentifiers.contains(sourceIdentifier) && layerId.contains(layerIdentifier)) {
        return layer.getVisibility().value.equals(VISIBLE);
      }
    }
    return false;
  }
}
