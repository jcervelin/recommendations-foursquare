package io.jcervelin.ideas.geofinder.recommendations.models.foursquare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private List<String> formattedAddress;
}