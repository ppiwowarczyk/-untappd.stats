package biz.piwowarczyk.untappd.stats.api.model;

public record CheckIn(String id, String rating, String beerId, String breweryId, biz.piwowarczyk.untappd.stats.api.model.User user) {
}

