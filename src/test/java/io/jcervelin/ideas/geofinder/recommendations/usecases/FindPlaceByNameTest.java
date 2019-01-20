package io.jcervelin.ideas.geofinder.recommendations.usecases;

import io.jcervelin.ideas.geofinder.recommendations.models.Place;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FindPlaceByNameTest {

    @InjectMocks
    private FindPlaceByName target;

    @Test
    public void findTwoRecommendationsByName() {
        final List<Place> places = target.recommendations(2,"London");
        assertThat(places.size()).isEqualTo(2);
    }

}