package biz.piwowarczyk.untappd.stats.model.response;

import biz.piwowarczyk.untappd.stats.api.model.Beer;

public record SharingStat(Beer beer, SharingRating sharingRating) {
}
