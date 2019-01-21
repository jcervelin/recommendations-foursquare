package io.jcervelin.ideas.geofinder.recommendations.gateways;

import io.jcervelin.ideas.geofinder.recommendations.configs.feign.FeignConfig;
import io.jcervelin.ideas.geofinder.recommendations.models.foursquare.FoursquareModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "https://api.foursquare.com/v2/venues",url = "https://api.foursquare.com/v2/venues",configuration = FeignConfig.class)
public interface FoursquareClient {
    @GetMapping(value="/explore")
    Optional<FoursquareModel> findRecommendationsByName(@RequestParam("limit") int amount,
                                                        @RequestParam("near") String name,
                                                        @RequestParam("client_id") String clientID,
                                                        @RequestParam("client_secret") String clientSecret,
                                                        @RequestParam("v") String versionDate
    );
}
