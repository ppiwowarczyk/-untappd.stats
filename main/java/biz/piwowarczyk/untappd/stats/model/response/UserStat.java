package biz.piwowarczyk.untappd.stats.model.response;

import biz.piwowarczyk.untappd.stats.api.model.User;

public record UserStat(User user, SharingRating sharingRating) {
}
