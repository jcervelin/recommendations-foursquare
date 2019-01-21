package io.jcervelin.ideas.geofinder.recommendations.usecases.impl;

import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FoursquareAPITest {

    @InjectMocks
    private FoursquareAPIImpl target;

    @Test
    public void recommendationsShouldReturn2PlacesFromFoursquareClient() {
        final Optional<FoursquareModel> models = target.recommendations(2, "London");
        assertThat(models).isNotEmpty();
    }
}