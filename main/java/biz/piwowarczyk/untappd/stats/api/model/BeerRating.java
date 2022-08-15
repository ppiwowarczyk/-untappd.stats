package biz.piwowarczyk.untappd.stats.api.model;

public record BeerRating(String totalCheckIns,
                         String monthlyCheckIns,
                         String uniqueCheckIns,
                         String rating) {
}
