package biz.piwowarczyk.untappd.stats.api;

import biz.piwowarczyk.untappd.stats.UntappdConfig;
import biz.piwowarczyk.untappd.stats.api.model.BeerResponse;
import biz.piwowarczyk.untappd.stats.api.model.Response;
import biz.piwowarczyk.untappd.stats.api.model.VenueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UntappdScrapperApi implements UntappdApi {

    @Autowired
    private UntappdConfig untappdConfig;

    @Override
    public VenueResponse getVenue(String id) {

        RestTemplate restTemplate = new RestTemplate();

        String venueUrl = new StringBuffer()
                //.append(untappdConfig.getUrl())
                .append("http://localhost:8081/")
                .append("venue?id=")
                .append(id)
                .toString();

        ResponseEntity<VenueResponse> entityVenueResponse = restTemplate.getForEntity(venueUrl, VenueResponse.class);
        return entityVenueResponse.getBody();
    }

    @Override
    public BeerResponse getBeer(String id) {
        RestTemplate restTemplate = new RestTemplate();

        String venueUrl = new StringBuffer()
                //.append(untappdConfig.getUrl())
                .append("http://localhost:8081/")
                .append("beer?id=")
                .append(id)
                .toString();

        ResponseEntity<BeerResponse> entityBeerResponse = restTemplate.getForEntity(venueUrl, BeerResponse.class);
        return entityBeerResponse.getBody();
    }
}
