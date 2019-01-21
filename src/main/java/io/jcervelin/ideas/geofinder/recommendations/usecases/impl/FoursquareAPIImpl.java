package io.jcervelin.ideas.geofinder.recommendations.usecases.impl;

import io.jcervelin.ideas.geofinder.recommendations.exceptions.NoDataFoundException;
import io.jcervelin.ideas.geofinder.recommendations.exceptions.TechnicalFaultException;
import io.jcervelin.ideas.geofinder.recommendations.gateways.FoursquareClient;
import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import io.jcervelin.ideas.geofinder.recommendations.usecases.FoursquareAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class FoursquareAPIImpl implements FoursquareAPI {

    private final FoursquareClient foursquareClient;

    @Value("${foursquare.clientID}")
    private String clientID;

    @Value("${foursquare.clientSecret}")
    private String clientSecret;

    public Optional<FoursquareModel> recommendations(int amount, String name) {
        final String nowFormatted = DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .format(LocalDate.now());
        try {
            if (clientID == null || clientSecret == null) {
                throw new TechnicalFaultException("Credentials not found.");
            }
            return foursquareClient.findRecommendationsByName(
                    amount,
                    name,
                    clientID,
                    clientSecret,
                    nowFormatted);
        } catch (HttpClientErrorException e) {
            throw new NoDataFoundException(format("No Data Found: [%s]", e.getMessage()));
        } catch (Exception e) {
            throw new TechnicalFaultException(format("Error with the Foursquare API: [%s]",e.getMessage()),e);
        }
    }
}
