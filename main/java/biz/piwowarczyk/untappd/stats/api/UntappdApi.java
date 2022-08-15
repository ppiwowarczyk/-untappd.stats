package biz.piwowarczyk.untappd.stats.api;

import biz.piwowarczyk.untappd.stats.api.model.BeerResponse;
import biz.piwowarczyk.untappd.stats.api.model.VenueResponse;

public interface UntappdApi {

    VenueResponse getVenue(String id);
    BeerResponse getBeer(String id);
}
