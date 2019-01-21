package io.jcervelin.ideas.geofinder.recommendations.gateways;

import io.jcervelin.ideas.geofinder.recommendations.configs.feign.FeignConfig;
import io.jcervelin.ideas.geofinder.recommendations.exceptions.TechnicalFaultException;
import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

@FeignClient(name = "${foursquare.url}",url = "${foursquare.url}",configuration = FeignConfig.class, fallback = FoursquareClient.FoursquareFallback.class)
public interface FoursquareClient {
    @GetMapping(value="${foursquare.path}")
    Optional<FoursquareModel> findRecommendationsByName(@RequestParam("limit") int amount,
                                                        @RequestParam("near") String name,
                                                        @RequestParam("client_id") String clientID,
                                                        @RequestParam("client_secret") String clientSecret,
                                                        @RequestParam("v") String versionDate
    ) throws IOException;

    @Component
    class FoursquareFallback implements FoursquareClient {
        public Optional<FoursquareModel> findRecommendationsByName(@RequestParam("limit") int amount,
                                                            @RequestParam("near") String name,
                                                            @RequestParam("client_id") String clientID,
                                                            @RequestParam("client_secret") String clientSecret,
                                                            @RequestParam("v") String versionDate
        ){
            throw new TechnicalFaultException("NOK Circuit opened.");
        }

    }
}
