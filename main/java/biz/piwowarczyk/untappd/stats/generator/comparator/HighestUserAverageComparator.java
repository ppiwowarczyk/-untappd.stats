package biz.piwowarczyk.untappd.stats.generator.comparator;

import biz.piwowarczyk.untappd.stats.model.response.SharingStat;
import biz.piwowarczyk.untappd.stats.model.response.UserStat;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class HighestUserAverageComparator implements Comparator<UserStat> {
    @Override
    public int compare(UserStat o1, UserStat o2) {
        return o1.sharingRating().average().compareTo(o2.sharingRating().average());
    }
}
