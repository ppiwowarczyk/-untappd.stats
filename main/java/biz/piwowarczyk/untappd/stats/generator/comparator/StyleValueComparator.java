package biz.piwowarczyk.untappd.stats.generator.comparator;

import biz.piwowarczyk.untappd.stats.model.response.FlatStyleStat;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class StyleValueComparator implements Comparator<FlatStyleStat> {
    @Override
    public int compare(FlatStyleStat o1, FlatStyleStat o2) {
        return Integer.compare(o1.numberOfBeers(), o2.numberOfBeers());
    }
}
