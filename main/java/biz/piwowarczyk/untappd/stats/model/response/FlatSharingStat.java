package biz.piwowarczyk.untappd.stats.model.response;

public record FlatSharingStat(
        String lp,
        String img,
        String breweryName,
        String beerName,
        String beerStyle,
        String sharingAverage,
        int totalSharingCheckIns,
        Double globalRating,
        Double globalRatingDifference) {
}
