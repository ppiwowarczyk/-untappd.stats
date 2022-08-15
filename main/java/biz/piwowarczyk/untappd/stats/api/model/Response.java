package biz.piwowarczyk.untappd.stats.api.model;

public record Response<T>(T response, Error error) {
}
