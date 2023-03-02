package io.jcervelin.ideas.geofinder.recommendations.gateways.https;

import io.jcervelin.ideas.geofinder.recommendations.models.Place;
import io.jcervelin.ideas.geofinder.recommendations.usecases.FindPlaceByName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Api(value = "/api/recommendations", description = "Recommendation system using Foursquare API")
public class RecommendationsController {

    private final FindPlaceByName findPlaceByName;

    @GetMapping
    @ApiOperation("Retorna lista de recomendacoes fornecidas pelo foursquare")
    public ResponseEntity<List<Place>> getRecommendations(@RequestParam(defaultValue = "10") String limit, @RequestParam String name) {
        final List<Place> places = findPlaceByName.recommendations(Integer.valueOf(limit), name);
        return new ResponseEntity<>(places, HttpStatus.OK);
    }
}
