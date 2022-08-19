package biz.piwowarczyk.untappd.stats.generator.mapper;

import biz.piwowarczyk.untappd.stats.api.model.CheckIn;
import biz.piwowarczyk.untappd.stats.api.model.User;
import biz.piwowarczyk.untappd.stats.model.response.SharingRating;
import biz.piwowarczyk.untappd.stats.model.response.SharingStat;
import biz.piwowarczyk.untappd.stats.model.response.UserStat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class UserStatMapper implements Function<Map.Entry<User, List<CheckIn>>, UserStat> {
    @Override
    public UserStat apply(Map.Entry<User, List<CheckIn>> userListEntry) {
        return new UserStat(
                userListEntry.getKey(),
                new SharingRating(
                        makeAverageOfRating(userListEntry.getValue()),
                        // TODO change model here we do not need global difference or calculate
                        0.0,
                        userListEntry.getValue().size()
                )
        );
    }

    private Double makeAverageOfRating(List<CheckIn> value) {

        return value.stream()
                .map(CheckIn::rating)
                .mapToDouble(s -> Double.valueOf(s))
                .average()
                .orElse(0.0);
    }
}
