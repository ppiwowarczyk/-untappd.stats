package biz.piwowarczyk.untappd.stats.generator.mapper;

import biz.piwowarczyk.untappd.stats.api.UntappdApi;
import biz.piwowarczyk.untappd.stats.api.model.Beer;
import biz.piwowarczyk.untappd.stats.api.model.CheckIn;
import biz.piwowarczyk.untappd.stats.model.response.SharingRating;
import biz.piwowarczyk.untappd.stats.model.response.SharingStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ShearingStatMapper implements Function<Map.Entry<String, List<CheckIn>>, SharingStat> {

    @Autowired
    private UntappdApi untappdApi;

    @Override
    public SharingStat apply(Map.Entry<String, List<CheckIn>> stringListEntry) {
        Beer beer = untappdApi.getBeer(stringListEntry.getKey()).response();
        Double sharingRatingAverage = makeAverageOfRating(stringListEntry.getValue());

        SharingRating sharingRating = new SharingRating(
                sharingRatingAverage,
                sharingRatingAverage - Double.valueOf(beer.beerRaiting().rating()),
                stringListEntry.getValue().size()
        );

        return new SharingStat(beer, sharingRating);
    }

    private Double makeAverageOfRating(List<CheckIn> value) {

        return value.stream()
                .map(CheckIn::rating)
                .mapToDouble(s -> Double.valueOf(s))
                .average()
                .orElse(0.0);
    }
}
