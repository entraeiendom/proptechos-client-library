package io.entraos.idun.rec.floor;

import io.entraos.idun.commands.IdunGetCommand;
import io.entraos.rec.domain.Floor;
import io.entraos.rec.mappers.FloorJsonMapper;

import java.net.URI;
import java.net.http.HttpResponse;

public class GetFloorCommand extends IdunGetCommand {
    private final String accessToken;
    private final String sensorUuid;

    public GetFloorCommand(URI baseURI, String accessToken, String sensorUuid) {
        super(baseURI,"Sensors");
        this.accessToken = accessToken;
        this.sensorUuid = sensorUuid;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "buildingcomponent/" + sensorUuid;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }

    public Floor getFloor() {
        String json = null;
        HttpResponse<String> response = run();
        json = response.body();
        Floor floor = null;
        if (json != null) {
            floor = FloorJsonMapper.fromJson(json);
        }
        return floor;
    }
}
