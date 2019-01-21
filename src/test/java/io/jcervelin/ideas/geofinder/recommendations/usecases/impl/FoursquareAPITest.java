package io.jcervelin.ideas.geofinder.recommendations.usecases.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jcervelin.ideas.geofinder.recommendations.exceptions.TechnicalFaultException;
import io.jcervelin.ideas.geofinder.recommendations.gateways.FoursquareClient;
import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FoursquareAPITest {

    @InjectMocks
    private FoursquareAPIImpl target;

    @Mock
    private FoursquareClient foursquareClient;

    private ObjectMapper objectMapper;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    @Test
    public void recommendationsShouldReturn2PlacesFromFoursquareClient() throws IOException, URISyntaxException {

        // GIVEN a client ready to reply with mocks
        ReflectionTestUtils.setField(target, "clientID","juliano");
        ReflectionTestUtils.setField(target, "clientSecret","1234");
        final Path pathTwoRecommendations = Paths.get(getClass().getResource("/json/twoRecommendations.json").toURI());
        final String jsonTwoRecommendations = new String(Files.readAllBytes(pathTwoRecommendations));
        final FoursquareModel foursquareModel = objectMapper.readValue(jsonTwoRecommendations,FoursquareModel.class);
        final String stringNow = DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .format(LocalDate.now());

        doReturn(Optional.of(foursquareModel)).when(foursquareClient).findRecommendationsByName(
                2,
                "London",
                "juliano",
                "1234",
                stringNow
        );

        // WHEN recommendations is called
        final Optional<FoursquareModel> models = target.recommendations(2, "London");

        // THEN it should be caled only once.
        verify(foursquareClient,times(1)).findRecommendationsByName(eq(2),
                eq("London"),
                eq("juliano"),
                eq("1234"),
                stringCaptor.capture());

        // The version should be set up correctly
        final String versionDate = stringCaptor.getValue();

        assertThat(versionDate).isEqualTo(stringNow);

        assertThat(models).isNotEmpty();
    }

    @Test
    public void recommendationsShouldReturnTechnicalFaultWhenTheClientOrInternetIsNotAvailable() throws IOException, URISyntaxException {

        // GIVEN a client ready to reply with mocks
        final String stringNow = DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .format(LocalDate.now());
        ReflectionTestUtils.setField(target, "clientID","juliano");
        ReflectionTestUtils.setField(target, "clientSecret","1234");

        UnknownHostException unknownHostException = new UnknownHostException("https://api.foursquare.com");

        doThrow(unknownHostException).when(foursquareClient).findRecommendationsByName(
                2,
                "London",
                "juliano",
                "1234",
                stringNow
        );

        thrown.expect(TechnicalFaultException.class);
        thrown.expectMessage("Error with the Foursquare API");
        // WHEN recommendations is called
        target.recommendations(2, "London");

        // THEN it should Return TechnicalFault.
    }
}