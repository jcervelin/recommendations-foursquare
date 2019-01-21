package io.jcervelin.ideas.geofinder.recommendations.usecases;

import io.jcervelin.ideas.geofinder.recommendations.exceptions.NoDataFoundException;
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

    public static final String NO_DATA_FOUND_FOR_THE_SEARCH_NEAR_AND_LIMIT = "No Data Found for the search: near [%s] and limit [%s].";
    private final FoursquareAPI foursquareAPI;

    /**
     * Method responsible for receiving the limit of the recommendations received and the name.
     * This method converts the Foursquare's model to the Place model, which has only name and full address.
     * @param amount is the limit of the recommendations received
     * @param name is the name given by the client to search on foursquare API
     * @return a list of Places with name and full address.
     */
    public List<Place> recommendations(int amount, String name) {
        final FoursquareModel foursquareModel = foursquareAPI.recommendations(amount,name)
                .orElseThrow(() -> new NoDataFoundException(String.format(NO_DATA_FOUND_FOR_THE_SEARCH_NEAR_AND_LIMIT,name,amount)));
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
