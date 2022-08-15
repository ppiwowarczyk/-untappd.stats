package biz.piwowarczyk.untappd.stats.api.model;

import java.util.List;

public record Venue(String name, List<CheckIn> checkInList) {

}
