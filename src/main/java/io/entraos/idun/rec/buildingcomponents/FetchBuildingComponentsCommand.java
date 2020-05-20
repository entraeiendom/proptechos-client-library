package io.entraos.idun.rec.buildingcomponents;

import io.entraos.idun.commands.IdunGetCommand;

import java.net.URI;

public class FetchBuildingComponentsCommand extends IdunGetCommand {
    private final String accessToken;

    public FetchBuildingComponentsCommand(URI baseURI, String accessToken) {
        super(baseURI,"BuildingComponents");
        this.accessToken = accessToken;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "json/buildingcomponent?page=0&size=50" ;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }
}
