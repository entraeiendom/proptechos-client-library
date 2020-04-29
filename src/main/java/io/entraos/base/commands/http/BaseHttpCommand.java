package io.entraos.base.commands.http;

import io.entraos.base.commands.BaseCommand;

public abstract class BaseHttpCommand<HttpResponse> extends BaseCommand {


    protected BaseHttpCommand(String groupKey) {
        super(groupKey);
    }

    protected BaseHttpCommand(String groupKey, int timeout) {
        super(groupKey,timeout);
    }

    protected abstract HttpResponse run();
}
