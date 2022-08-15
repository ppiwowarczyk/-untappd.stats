package biz.piwowarczyk.untappd.stats.model;

import biz.piwowarczyk.untappd.stats.api.model.Beer;

public record SharingStat(Beer beer, SharingRating sharingRating) {
}
