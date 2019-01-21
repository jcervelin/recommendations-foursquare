package io.jcervelin.ideas.geofinder.recommendations.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private String name;
    private String fullAddress;
}
