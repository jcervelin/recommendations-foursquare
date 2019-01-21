package io.jcervelin.ideas.geofinder.recommendations.usecases;

import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;

public interface FoursquareAPI {
    FoursquareModel recommendations(int amount, String name);
}
