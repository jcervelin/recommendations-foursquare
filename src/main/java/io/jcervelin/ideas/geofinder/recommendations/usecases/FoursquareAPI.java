package io.jcervelin.ideas.geofinder.recommendations.usecases;

import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;

import java.util.Optional;

public interface FoursquareAPI {
    Optional<FoursquareModel> recommendations(int amount, String name);
}
