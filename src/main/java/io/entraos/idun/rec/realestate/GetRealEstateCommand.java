package io.entraos.idun.rec.realestate;

import io.entraos.idun.commands.IdunGetCommand;
import io.entraos.rec.domain.RealEstate;
import io.entraos.rec.mappers.RealEstateJsonMapper;

import java.net.URI;
import java.net.http.HttpResponse;

public class GetRealEstateCommand extends IdunGetCommand {
    private final String accessToken;
    private final String sensorUuid;

    public GetRealEstateCommand(URI baseURI, String accessToken, String sensorUuid) {
        super(baseURI,"RealEstate");
        this.accessToken = accessToken;
        this.sensorUuid = sensorUuid;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "json/realestate/" + sensorUuid;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }

    public RealEstate getRealEstate() {
        String json = null;
        HttpResponse<String> response = run();
        json = response.body();
        RealEstate realEstate = null;
        if (json != null) {
            realEstate = RealEstateJsonMapper.fromJson(json);
        }
        return realEstate;
    }
}
