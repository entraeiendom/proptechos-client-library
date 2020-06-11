package io.entraos.idun.rec.room;

import io.entraos.idun.commands.IdunGetCommand;

import java.net.URI;

public class FetchRoomsCommand extends IdunGetCommand {
    private final String accessToken;

    private final String buildingUuid;
    public static final int DEFAULT_PAGE_NUM = 0;
    public static final int DEFAULT_FETCH_SIZE = 5000;
    private final int pageNum;
    private final int fetchSize;

    public FetchRoomsCommand(URI baseURI, String accessToken, String buildingUuid) {
        this(baseURI, accessToken,buildingUuid, DEFAULT_PAGE_NUM, DEFAULT_FETCH_SIZE);
    }

    public FetchRoomsCommand(URI baseURI, String accessToken, String buildingUuid, int pageNum, int fetchSize) {
        super(baseURI, "Room");
        this.accessToken = accessToken;
        this.pageNum = pageNum;
        this.fetchSize = fetchSize;
        this.buildingUuid = buildingUuid;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "json/room?building_ids=" + buildingUuid + "&page=" + pageNum + "&size=" + fetchSize ;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }
}
