package biz.piwowarczyk.untappd.stats.generator;

import biz.piwowarczyk.untappd.stats.api.UntappdApi;
import biz.piwowarczyk.untappd.stats.api.model.CheckIn;
import biz.piwowarczyk.untappd.stats.api.model.User;
import biz.piwowarczyk.untappd.stats.api.model.VenueResponse;
import biz.piwowarczyk.untappd.stats.generator.comparator.HighestAverageComparator;
import biz.piwowarczyk.untappd.stats.generator.comparator.HighestUserAverageComparator;
import biz.piwowarczyk.untappd.stats.generator.comparator.StyleValueComparator;
import biz.piwowarczyk.untappd.stats.generator.mapper.ShearingStatMapper;
import biz.piwowarczyk.untappd.stats.generator.mapper.UserStatMapper;
import biz.piwowarczyk.untappd.stats.model.request.SharingParams;
import biz.piwowarczyk.untappd.stats.model.response.FlatStyleStat;
import biz.piwowarczyk.untappd.stats.model.response.FlatTimePerCheck;
import biz.piwowarczyk.untappd.stats.model.response.SharingStat;
import biz.piwowarczyk.untappd.stats.model.response.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class SharingStatsGenerator {

    @Autowired
    private UntappdApi untappdApi;
    @Autowired
    private HighestAverageComparator highestAverageComparator;
    @Autowired
    private HighestUserAverageComparator highestUserAverageComparator;
    @Autowired
    private StyleValueComparator styleValueComparator;
    @Autowired
    private ShearingStatMapper shearingStatMapper;
    @Autowired
    private UserStatMapper userStatMapper;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<FlatTimePerCheck> generateTimePerCheckStats(SharingParams sharingParams) {

        VenueResponse venue = untappdApi.getVenue(sharingParams.venueId());

        List<FlatTimePerCheck> list = new ArrayList<>();

        List<CheckIn> collect = venue.response().checkInList().stream()
                .filter(checkIn -> sharingParams.sharingUserIds().contains(checkIn.user().id()))
                .filter(s -> isCheckInDoneDuringSharing(s, sharingParams.startSharing(), sharingParams.endSharing()))
                .collect(Collectors.toList());

        // create range
        long minutesOffset = 30;
        LocalDateTime currentSharing = LocalDateTime.parse(sharingParams.startSharing(), dateTimeFormatter);
        LocalDateTime endSharing = LocalDateTime.parse(sharingParams.endSharing(), dateTimeFormatter);

        while (currentSharing.isBefore(endSharing)) {

            LocalDateTime periodStart = currentSharing;
            LocalDateTime periodEnd = currentSharing.plusMinutes(minutesOffset);

            List<CheckIn> collect1 = collect.stream()
                    .filter(checkIn -> sharingParams.sharingUserIds().contains(checkIn.user().id()))
                    .filter(s -> isCheckInDoneDuringSharing(s, periodStart, periodEnd))
                    .collect(Collectors.toList());

            String label = new StringBuilder()
                    .append(currentSharing.format(dateTimeFormatter))
                    .append(" to ")
                    .append(currentSharing.plusMinutes(minutesOffset).format(dateTimeFormatter)).toString();


            list.add(new FlatTimePerCheck(label, collect1.size()));

            currentSharing = currentSharing.plusMinutes(minutesOffset);
        }

        return list;
    }

    public List<SharingStat> generateStats(SharingParams sharingParams) {

        VenueResponse venue = untappdApi.getVenue(sharingParams.venueId());

        Map<String, List<CheckIn>> beerRatings = venue.response().checkInList().stream()
                .filter(checkIn -> sharingParams.sharingUserIds().contains(checkIn.user().id()))
                .filter(s -> isCheckInDoneDuringSharing(s, sharingParams.startSharing(), sharingParams.endSharing()))
                .collect(groupingBy(CheckIn::beerId));

        List<SharingStat> sharingStats = beerRatings.entrySet().stream()
                .map(s -> shearingStatMapper.apply(s))
                .sorted(highestAverageComparator.reversed())
                .collect(Collectors.toList());

        return sharingStats;
    }

    public List<UserStat> generateUserStats(SharingParams sharingParams) {

        VenueResponse venue = untappdApi.getVenue(sharingParams.venueId());

        Map<User, List<CheckIn>> userListMap = venue.response().checkInList().stream()
                .filter(checkIn -> sharingParams.sharingUserIds().contains(checkIn.user().id()))
                .filter(s -> isCheckInDoneDuringSharing(s, sharingParams.startSharing(), sharingParams.endSharing()))
                .collect(groupingBy(CheckIn::user));

        List<UserStat> userStats = userListMap.entrySet().stream()
                .map(s -> userStatMapper.apply(s))
                .sorted(highestUserAverageComparator.reversed())
                .collect(Collectors.toList());

        return userStats;
    }

    public List<FlatStyleStat> generateStyleStats(SharingParams sharingParams) {

        List<FlatStyleStat> flatStyleStat = this.generateStats(sharingParams)
                .stream()
                .collect(groupingBy(s -> s.beer().style()))
                .entrySet()
                .stream()
                .map(s -> new FlatStyleStat(s.getKey(), s.getValue().size()))
                .sorted(styleValueComparator.reversed())
                .collect(Collectors.toList());
        return flatStyleStat;
    }

    private boolean isCheckInDoneDuringSharing(CheckIn checkIn, String startSharingParam, String endSharingParam) {

        LocalDateTime checkInDate = LocalDateTime.parse(checkIn.dateTime(), dateTimeFormatter);
        LocalDateTime startSharing = LocalDateTime.parse(startSharingParam, dateTimeFormatter);
        LocalDateTime endSharing = LocalDateTime.parse(endSharingParam, dateTimeFormatter);

        return checkInDate.isAfter(startSharing) && checkInDate.isBefore(endSharing);
    }

    private boolean isCheckInDoneDuringSharing(CheckIn checkIn, LocalDateTime startSharingParam, LocalDateTime endSharingParam) {

        LocalDateTime checkInDate = LocalDateTime.parse(checkIn.dateTime(), dateTimeFormatter);
        return checkInDate.isAfter(startSharingParam) && checkInDate.isBefore(endSharingParam);
    }
}
