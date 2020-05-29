package io.entraos.idun.rec.floor;

import io.entraos.idun.commands.IdunGetCommand;

import java.net.URI;

public class FetchFloorsCommand extends IdunGetCommand {
    private final String accessToken;

    private final String buildingUuid;

    public FetchFloorsCommand(URI baseURI, String accessToken, String buildingUuid) {
        super(baseURI, "Floor");
        this.accessToken = accessToken;
        this.buildingUuid = buildingUuid;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "json/realestatecomponent/" + buildingUuid + "/storey";
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }
}
