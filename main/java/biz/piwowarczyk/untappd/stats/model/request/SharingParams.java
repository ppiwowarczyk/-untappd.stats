package biz.piwowarczyk.untappd.stats.model.request;

import java.util.List;

public record SharingParams(String venueId, String startSharing, String endSharing, List<String> sharingUserIds) {
}
