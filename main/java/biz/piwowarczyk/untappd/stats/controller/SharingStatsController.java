package biz.piwowarczyk.untappd.stats.controller;

import biz.piwowarczyk.untappd.stats.generator.SharingStatsGenerator;
import biz.piwowarczyk.untappd.stats.model.request.SharingParams;
import biz.piwowarczyk.untappd.stats.model.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class SharingStatsController {

    @Autowired
    private SharingStatsGenerator sharingStatsGenerator;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @PostMapping("/sharing/stats")
    //@ExceptionHandler
    public List<SharingStat> sharingStats(@RequestBody SharingParams sharingParams) {
        return sharingStatsGenerator.generateStats(sharingParams);
    }

    @PostMapping("/sharing/stats/flourish")
    //@ExceptionHandler
    public List<FlatSharingStat> sharingStatsFlourish(@RequestBody SharingParams sharingParams) {

        AtomicInteger count = new AtomicInteger(0);

        return sharingStatsGenerator.generateStats(sharingParams)
                .stream()
                .map(s -> new FlatSharingStat(
                        String.valueOf(count.incrementAndGet()),
                        s.beer().img(),
                        s.beer().brewery().name(),
                        s.beer().name(),
                        s.beer().style(),
                        df.format(s.sharingRating().average()),
                        s.sharingRating().totalSharingCheckIns(),
                        Double.valueOf(s.beer().beerRaiting().rating()),
                        s.sharingRating().globalRatingDifference()
                )).collect(Collectors.toList());
    }

    @PostMapping("/sharing/user/stats")
    //@ExceptionHandler
    public List<UserStat> sharingUserStats(@RequestBody SharingParams sharingParams) {

        return sharingStatsGenerator.generateUserStats(sharingParams);
    }


    @PostMapping("/sharing/user/stats/flourish")
    //@ExceptionHandler
    public List<FlatUserStat> sharingUserStatsFlourish(@RequestBody SharingParams sharingParams) {

        AtomicInteger count = new AtomicInteger(0);

        return sharingStatsGenerator.generateUserStats(sharingParams).stream()
                .map(s -> new FlatUserStat(
                        count.incrementAndGet(),
                        s.user().avatar(),
                        s.user().name(),
                        df.format(s.sharingRating().average()),
                        s.sharingRating().totalSharingCheckIns()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping("/sharing/style/stats/flourish")
    //@ExceptionHandler
    public List<FlatStyleStat> sharingStyleStatsFlourish(@RequestBody SharingParams sharingParams) {

        return sharingStatsGenerator.generateStyleStats(sharingParams);
    }

    @PostMapping("/sharing/time/stats/flourish")
    //@ExceptionHandler
    public List<FlatTimePerCheck> sharingTimeStatsFlourish(@RequestBody SharingParams sharingParams) {

        return sharingStatsGenerator.generateTimePerCheckStats(sharingParams);
    }

}
