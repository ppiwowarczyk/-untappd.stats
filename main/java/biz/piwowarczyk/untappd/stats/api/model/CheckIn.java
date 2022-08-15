package biz.piwowarczyk.untappd.stats.api.model;

public record CheckIn(String id, String dateTime, String rating, String beerId, String breweryId, User user) {
}

