package io.jcervelin.ideas.geofinder.recommendations.usecases;

import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import io.jcervelin.ideas.geofinder.recommendations.models.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindPlaceByName {

    private final FoursquareAPI foursquareAPI;

    public List<Place> recommendations(int amount, String name) {
        final FoursquareModel foursquareModel = foursquareAPI.recommendations(amount,name);
        return foursquareModel.getResponse().getGroups()
                .stream()
                .map(group -> group.getItems()
                        .stream()
                        .map(item ->
                                Place.builder()
                                        .name(item.getVenue().getName())
                                        .fullAddress(String.join(", ",
                                                item.getVenue().getLocation().getFormattedAddress()))
                                        .build()
                        ).collect(Collectors.toList())
                ).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
