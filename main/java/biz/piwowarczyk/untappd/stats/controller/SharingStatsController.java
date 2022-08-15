package biz.piwowarczyk.untappd.stats.controller;

import biz.piwowarczyk.untappd.stats.api.UntappdApi;
import biz.piwowarczyk.untappd.stats.api.model.Beer;
import biz.piwowarczyk.untappd.stats.api.model.CheckIn;
import biz.piwowarczyk.untappd.stats.api.model.VenueResponse;
import biz.piwowarczyk.untappd.stats.model.SharingRating;
import biz.piwowarczyk.untappd.stats.model.SharingStat;
import biz.piwowarczyk.untappd.stats.model.request.SharingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/sharing/stats")
    //@ExceptionHandler
    public List<SharingStat> sharingStats(@RequestBody SharingParams sharingParams) {

        VenueResponse venue = untappdApi.getVenue(sharingParams.venueId());

        Map<String, List<CheckIn>> beerRatings = venue.response().checkInList().stream()
                .filter(checkIn -> sharingParams.sharingUserIds().contains(checkIn.user().id()))
                .filter(s -> isCheckInDoneDuringSharing(s, sharingParams.startSharing(), sharingParams.endSharing()))
                .collect(groupingBy(CheckIn::beerId));

        List<SharingStat> sharingStats = beerRatings.entrySet().stream()
                .map(s -> mapToShearingStat(s.getKey(), s.getValue()))
                .sorted(highestAverageComparator.reversed())
                .collect(Collectors.toList());

        return sharingStats;
    }

    private boolean isCheckInDoneDuringSharing(CheckIn checkIn, String startSharingParam, String endSharingParam) {

        LocalDateTime checkInDate = LocalDateTime.parse(checkIn.dateTime(), dateTimeFormatter);
        LocalDateTime startSharing = LocalDateTime.parse(startSharingParam, dateTimeFormatter);
        LocalDateTime endSharing = LocalDateTime.parse(endSharingParam, dateTimeFormatter);

        return checkInDate.isAfter(startSharing) && checkInDate.isBefore(endSharing);
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
