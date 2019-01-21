package io.jcervelin.ideas.geofinder.recommendations.usecases.impl;

import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import io.jcervelin.ideas.geofinder.recommendations.usecases.FoursquareAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FoursquareAPIImpl implements FoursquareAPI {

    public Optional<FoursquareModel> recommendations(int amount, String name) {

        return Optional.empty();
    }
}
