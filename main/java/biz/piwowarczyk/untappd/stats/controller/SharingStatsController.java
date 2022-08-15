package biz.piwowarczyk.untappd.stats.controller;

import biz.piwowarczyk.untappd.stats.api.UntappdApi;
import biz.piwowarczyk.untappd.stats.api.model.Beer;
import biz.piwowarczyk.untappd.stats.api.model.CheckIn;
import biz.piwowarczyk.untappd.stats.api.model.VenueResponse;
import biz.piwowarczyk.untappd.stats.model.SharingRating;
import biz.piwowarczyk.untappd.stats.model.SharingStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RestController
public class SharingStatsController {

    @Autowired
    private UntappdApi untappdApi;
    @Autowired
    private HighestAverageComparator highestAverageComparator;

    @RequestMapping("/sharing/stats")
    //@ExceptionHandler
    public List<SharingStat> sharingStats(@RequestParam(value = "id") String venueId) {

        List<String> sharingUserIds = Arrays.asList("droho", "piwowarczykp");

        VenueResponse venue = untappdApi.getVenue(venueId);

        Map<String, List<CheckIn>> beerRatings = venue.response().checkInList().stream()
                .filter(checkIn -> sharingUserIds.contains(checkIn.user().id()))
                .collect(groupingBy(CheckIn::beerId));

        List<SharingStat> sharingStats = beerRatings.entrySet().stream()
                .map(s -> mapToShearingStat(s.getKey(), s.getValue()))
                .sorted(highestAverageComparator.reversed())
                .collect(Collectors.toList());

        return sharingStats;
    }

    private Double makeAverageOfRating(List<CheckIn> value) {

        return value.stream()
                .map(CheckIn::rating)
                .mapToDouble(s -> Double.valueOf(s))
                .average()
                .orElse(0.0);
    }

    private SharingStat mapToShearingStat(String key, List<CheckIn> checkInList) {

        Beer beer = untappdApi.getBeer(key).response();
        Double sharingRatingAverage = makeAverageOfRating(checkInList);

        SharingRating sharingRating = new SharingRating(
                sharingRatingAverage,
                sharingRatingAverage - Double.valueOf(beer.beerRaiting().rating()),
                checkInList.size()
        );

        return new SharingStat(beer, sharingRating);
    }
}
