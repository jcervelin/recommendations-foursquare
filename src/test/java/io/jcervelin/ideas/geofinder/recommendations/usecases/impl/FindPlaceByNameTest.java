package io.jcervelin.ideas.geofinder.recommendations.usecases.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jcervelin.ideas.geofinder.recommendations.exceptions.NoDataFoundException;
import io.jcervelin.ideas.geofinder.recommendations.models.Place;
import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.Response;
import io.jcervelin.ideas.geofinder.recommendations.usecases.impl.FindPlaceByNameImpl;
import io.jcervelin.ideas.geofinder.recommendations.usecases.impl.FoursquareAPIImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class FindPlaceByNameTest {

    @InjectMocks
    private FindPlaceByNameImpl target;

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

        doReturn(Optional.of(foursquareModel)).when(foursquareAPI).recommendations(2,"London");

        // WHEN recommendations is called
        final List<Place> places = target.recommendations(2,"London");

        // THEN it should return 2 places
        assertThat(places.size()).isEqualTo(2);
        assertThat(places).containsExactlyInAnyOrderElementsOf(expectedResults);
    }

    @Test
    public void find0RecommendationsByNameShouldReturnException() {
        // GIVEN 0 recommendations provided by the foursquare API
        doThrow(new NoDataFoundException("No data found.")).when(foursquareAPI).recommendations(5,"Narnia,London");
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No data found.");

        // WHEN recommendations is called
        target.recommendations(5,"Narnia,London");

        // THEN it should return an exception
    }

    @Test
    public void findRecommendationsByNameShouldReturnExceptionIfReturnIsNull() {
        // GIVEN null provided by the foursquare API
        doReturn(Optional.empty()).when(foursquareAPI).recommendations(5,"Narnia,London");
        thrown.expect(NoDataFoundException.class);
        thrown.expectMessage("No Data Found for the search: near [Narnia,London] and limit [5].");

        // WHEN recommendations is called
        target.recommendations(5,"Narnia,London");

        // THEN it should return a friendly exception.
    }

    @Test
    public void findRecommendationsByNameWithNullListShouldReturnException() {
        // GIVEN a foursquareModel with a null list inside of it provided by the foursquare API
        FoursquareModel foursquareModel = FoursquareModel.builder()
                .response(Response.builder().groups(null).build())
                .build();
        doReturn(Optional.of(foursquareModel)).when(foursquareAPI).recommendations(5,"Narnia,London");
        thrown.expect(NoDataFoundException.class);
        thrown.expectMessage("No Data Found for the search: near [Narnia,London] and limit [5].");

        // WHEN recommendations is called
        target.recommendations(5, "Narnia,London");

        // THEN it should return a friendly exception.
    }

    @Test
    public void findRecommendationsByNameWithItemsInDifferentGroupsListShouldReturnException() throws URISyntaxException, IOException {
        // GIVEN 3 recommendations provided by the foursquare API in different groups
        final Path threeRecommendationsInDifferentGroups = Paths.get(getClass().getResource("/json/threeRecommendationsInDifferentGroups.json").toURI());
        final String jsonThreeRecommendationsInDifferentGroups = new String(Files.readAllBytes(threeRecommendationsInDifferentGroups));
        final FoursquareModel foursquareModel = objectMapper.readValue(jsonThreeRecommendationsInDifferentGroups,FoursquareModel.class);

        final List<Place> expectedResults = Arrays.asList(
                Place.builder().name("Hyde Park").fullAddress("Serpentine Rd (Park Ln), London, Greater London, W2 2TP, United Kingdom").build(),
                Place.builder().name("Hampstead Heath").fullAddress("E Heath Rd, London, Greater London, NW3 2PT, United Kingdom").build(),
                Place.builder().name("Palace").fullAddress("Mock Street, London, Greater London, W2 2TP, United Kingdom").build()
        );

        doReturn(Optional.of(foursquareModel)).when(foursquareAPI).recommendations(3,"London");

        // WHEN recommendations is called
        final List<Place> places = target.recommendations(3,"London");

        // THEN it should return 3 places
        assertThat(places.size()).isEqualTo(3);
        assertThat(places).containsExactlyInAnyOrderElementsOf(expectedResults);
    }
}