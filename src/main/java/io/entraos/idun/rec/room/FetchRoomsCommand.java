package io.entraos.idun.rec.room;

import io.entraos.idun.commands.IdunGetCommand;

import java.net.URI;

public class FetchRoomsCommand extends IdunGetCommand {
    private final String accessToken;

    private final String buildingUuid;

    public FetchRoomsCommand(URI baseURI, String accessToken, String buildingUuid) {
        super(baseURI, "Floor");
        this.accessToken = accessToken;
        this.buildingUuid = buildingUuid;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "json/rooms?building_ids=" + buildingUuid ;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }
}
