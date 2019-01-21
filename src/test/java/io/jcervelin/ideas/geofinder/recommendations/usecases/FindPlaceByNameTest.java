package io.jcervelin.ideas.geofinder.recommendations.usecases;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jcervelin.ideas.geofinder.recommendations.models.Place;
import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import io.jcervelin.ideas.geofinder.recommendations.usecases.impl.FoursquareAPIImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FindPlaceByNameTest {

    @InjectMocks
    private FindPlaceByName target;

    @Mock
    private FoursquareAPIImpl foursquareAPI;

    private ObjectMapper objectMapper;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void findTwoRecommendationsByNameShouldReturn2Places() throws URISyntaxException, IOException {
        // GIVEN 2 recommendations provided by the foursquare API
        final Path pathTwoRecommendations = Paths.get(getClass().getResource("/json/twoRecommendations.json").toURI());
        final String jsonTwoRecommendations = new String(Files.readAllBytes(pathTwoRecommendations));
        final FoursquareModel foursquareModel = objectMapper.readValue(jsonTwoRecommendations,FoursquareModel.class);

        final List<Place> expectedResults = Arrays.asList(
                Place.builder().name("Hyde Park").fullAddress("Serpentine Rd (Park Ln), London, Greater London, W2 2TP, United Kingdom").build(),
                Place.builder().name("Hampstead Heath").fullAddress("E Heath Rd, London, Greater London, NW3 2PT, United Kingdom").build()
        );

        Mockito.doReturn(foursquareModel).when(foursquareAPI).recommendations(2,"London");
        final List<Place> places = target.recommendations(2,"London");
        assertThat(places.size()).isEqualTo(2);
        assertThat(places).containsExactlyInAnyOrderElementsOf(expectedResults);
    }

    //TODO change RuntimeException to a customised exception
    @Test
    public void find0RecommendationsByNameShouldReturnException() {
        // GIVEN 0 recommendations provided by the foursquare API
        Mockito.doThrow(new RuntimeException("No data found.")).when(foursquareAPI).recommendations(5,"Narnia,London");
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No data found.");

        // WHEN recommendations is called
        target.recommendations(5,"Narnia,London");

        // THEN it should return an exception
    }

    @Test
    public void findRecommendationsByNameShouldReturnExceptionIfReturnIsNull() {
        // GIVEN null provided by the foursquare API
        Mockito.doReturn(null).when(foursquareAPI).recommendations(5,"Narnia,London");

        // WHEN recommendations is called
        target.recommendations(5,"Narnia,London");

        // THEN it should return a friendly exception.
    }

}