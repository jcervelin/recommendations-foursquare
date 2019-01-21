package io.jcervelin.ideas.geofinder.recommendations.models.foursquare;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoursquareModel {
    private Response response;
}


