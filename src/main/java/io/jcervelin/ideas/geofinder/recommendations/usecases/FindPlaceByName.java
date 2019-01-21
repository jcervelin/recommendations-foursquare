package io.jcervelin.ideas.geofinder.recommendations.usecases;

import io.jcervelin.ideas.geofinder.recommendations.models.Place;

import java.util.List;

public interface FindPlaceByName {
    List<Place> recommendations(int amount, String name);
}
