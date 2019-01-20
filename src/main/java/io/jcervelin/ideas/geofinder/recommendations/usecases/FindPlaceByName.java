package io.jcervelin.ideas.geofinder.recommendations.usecases;

import io.jcervelin.ideas.geofinder.recommendations.models.Place;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FindPlaceByName {
    public List<Place> recommendations(int amount, String london) {
        return Arrays.asList(new Place(),new Place());
    }
}
