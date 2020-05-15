package io.entraos.base.commands.http;

import io.entraos.base.commands.BaseCommand;

import java.net.URI;

public abstract class BaseHttpCommand<HttpResponse> extends BaseCommand {


    private final URI baseUri;
    protected HttpResponse response;

    protected BaseHttpCommand(URI baseUri, String groupKey) {
        this(baseUri, groupKey, BaseCommand.DEFAULT_TIMEOUT);
    }

    protected BaseHttpCommand(URI baseUri, String groupKey, int timeout) {
        super(groupKey,timeout);
        this.baseUri = baseUri;
    }
    protected URI getBaseUri() {
        return baseUri;
    }

    protected abstract HttpResponse run();

    protected abstract URI buildUri();

    protected abstract String buildAuthorization();

    public HttpResponse getResponse() {
        return response;
    }
}
