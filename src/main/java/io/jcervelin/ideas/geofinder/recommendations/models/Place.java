package io.jcervelin.ideas.geofinder.recommendations.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Place {
    private String name;
    private String fullAddress;
}
