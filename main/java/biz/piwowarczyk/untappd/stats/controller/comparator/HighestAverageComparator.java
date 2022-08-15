package biz.piwowarczyk.untappd.stats.controller.comparator;

import biz.piwowarczyk.untappd.stats.model.response.SharingStat;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class HighestAverageComparator implements Comparator<SharingStat> {
    @Override
    public int compare(SharingStat o1, SharingStat o2) {
        return o1.sharingRating().average().compareTo(o2.sharingRating().average());
    }
}
