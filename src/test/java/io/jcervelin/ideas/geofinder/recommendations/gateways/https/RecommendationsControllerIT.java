package io.jcervelin.ideas.geofinder.recommendations.gateways.https;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.jcervelin.ideas.geofinder.recommendations.RecommendationsApplication;
import io.jcervelin.ideas.geofinder.recommendations.models.ErrorResponse;
import io.jcervelin.ideas.geofinder.recommendations.models.Place;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RecommendationsApplication.class}, webEnvironment = RANDOM_PORT)
@ComponentScan(basePackages = {"io.jcervelin.ideas.geofinder.recommendations"})
public class RecommendationsControllerIT {

    @ClassRule
    public static WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.options().port(8089).httpsPort(8443)
                    .notifier(new ConsoleNotifier(true)));

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webAppContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getRecommendationsShouldReturn2Recommendations() throws Exception {
        // GIVEN
        final Path pathTwoRecommendations = Paths.get(getClass().getResource("/json/twoRecommendations.json").toURI());
        final String jsonTwoRecommendations = new String(Files.readAllBytes(pathTwoRecommendations));

        final Place placeHyde = Place.builder().name("Hyde Park").fullAddress("Serpentine Rd (Park Ln), London, Greater London, W2 2TP, United Kingdom").build();
        final Place placeHampstead = Place.builder().name("Hampstead Heath").fullAddress("E Heath Rd, London, Greater London, NW3 2PT, United Kingdom").build();
        final String stringNow = DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .format(LocalDate.now());

        wireMockRule.stubFor(WireMock.get(WireMock.urlEqualTo("/explore?limit=2&near=London&client_id=juliano&client_secret=12345&v="+stringNow))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonTwoRecommendations.getBytes(Charset.forName("UTF-8")))));

        // WHEN the endpoint is called
        final MvcResult mvcResult = mockMvc.perform(get("/api/recommendations?limit=2&name=London").characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        final String content = new String(mvcResult
                .getResponse().getContentAsByteArray());

        final List<Place> places = objectMapper.readValue(content, new TypeReference<List<Place>>() {
        });

        Assertions.assertThat(places).containsExactlyInAnyOrder(placeHampstead, placeHyde);
    }

    @Test
    public void getRecommendationsShouldReturnNoDataFoundException() throws Exception {
        // WHEN
        final Path pathTwoRecommendations = Paths.get(getClass().getResource("/json/error400.json").toURI());
        final String jsonTwoRecommendations = new String(Files.readAllBytes(pathTwoRecommendations));

        final ErrorResponse errorExpected = new ErrorResponse();
        errorExpected.setCode(400);
        errorExpected.setMessage("No Data Found: [400 Bad Request]");
        errorExpected.setStatus(HttpStatus.BAD_REQUEST);

        final String stringNow = DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .format(LocalDate.now());

        wireMockRule.stubFor(WireMock.get(
                WireMock.urlEqualTo("/explore?limit=3&near=Narnia%2CLondon&client_id=juliano&client_secret=12345&v="+stringNow))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(400)
                        .withBody(jsonTwoRecommendations.getBytes(Charset.forName("UTF-8")))));

        // WHEN the endpoint is called
        final MvcResult mvcResult = mockMvc.perform(
                get("/api/recommendations?limit=3&name=Narnia%2CLondon").characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andReturn();

        // THEN
        final String content = new String(mvcResult
                .getResponse().getContentAsByteArray());

        final ErrorResponse result = objectMapper.readValue(content, ErrorResponse.class);

        Assertions.assertThat(errorExpected).isEqualTo(result);
    }

    @Test
    public void getRecommendationsShouldReturnTechnicalFault() throws Exception {
        // WHEN
        final Path pathTwoRecommendations = Paths.get(getClass().getResource("/json/error500.json").toURI());
        final String jsonTwoRecommendations = new String(Files.readAllBytes(pathTwoRecommendations));

        final ErrorResponse errorExpected = new ErrorResponse();
        errorExpected.setCode(500);
        errorExpected.setMessage("Error with the Foursquare API: [500 Server Error]");
        errorExpected.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        final String stringNow = DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .format(LocalDate.now());

        wireMockRule.stubFor(WireMock.get(
                WireMock.urlEqualTo("/explore?limit=3&near=Narnia%2CLondon&client_id=juliano&client_secret=12345&v="+stringNow))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(500)
                        .withBody(jsonTwoRecommendations.getBytes(Charset.forName("UTF-8")))));

        // WHEN the endpoint is called
        final MvcResult mvcResult = mockMvc.perform(
                get("/api/recommendations?limit=3&name=Narnia%2CLondon").characterEncoding("utf-8"))
                .andExpect(status().isInternalServerError())
                .andReturn();

        // THEN
        final String content = new String(mvcResult
                .getResponse().getContentAsByteArray());

        final ErrorResponse result = objectMapper.readValue(content, ErrorResponse.class);

        Assertions.assertThat(errorExpected).isEqualTo(result);
    }
}