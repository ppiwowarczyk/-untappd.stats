package biz.piwowarczyk.untappd.stats.api.model;

import org.springframework.http.HttpStatus;

public record Error(HttpStatus httpStatus, String description) {
}
