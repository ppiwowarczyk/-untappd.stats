package biz.piwowarczyk.untappd.stats.model.response;

public record FlatSharingStat(
        int lp,
        String img,
        String breweryName,
        String beerName,
        String beerStyle,
        String sharingAverage,
        int totalSharingCheckIns,
        Double globalRating,
        Double globalRatingDifference) {
}
