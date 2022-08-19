package biz.piwowarczyk.untappd.stats.model.response;

public record FlatUserStat(
        int lp,
        String userAvatar,
        String userName,
        String average,
        int totalSharingCheckIns) {
}
