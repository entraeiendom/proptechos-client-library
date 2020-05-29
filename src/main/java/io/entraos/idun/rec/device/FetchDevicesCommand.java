package io.entraos.idun.rec.device;

import io.entraos.idun.commands.IdunGetCommand;

import java.net.URI;

public class FetchDevicesCommand extends IdunGetCommand {
    private final String accessToken;

    public static final int DEFAULT_PAGE_NUM = 0;
    public static final int DEFAULT_FETCH_SIZE = 5000;
    private final int pageNum;
    private final int fetchSize;

    public FetchDevicesCommand(URI baseURI, String accessToken) {
        this(baseURI,accessToken, DEFAULT_PAGE_NUM, DEFAULT_FETCH_SIZE);
    }

    public FetchDevicesCommand(URI baseURI, String accessToken, int pageNum, int fetchSize) {
        super(baseURI,"Devices");
        this.accessToken = accessToken;
        this.pageNum = pageNum;
        this.fetchSize = fetchSize;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "json/device?page=" + pageNum +"&size=" + fetchSize ;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }
}
