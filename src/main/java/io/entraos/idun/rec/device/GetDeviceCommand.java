package io.entraos.idun.rec.device;

import io.entraos.idun.commands.IdunGetCommand;
import io.entraos.rec.domain.Device;
import io.entraos.rec.mappers.DeviceJsonMapper;

import java.net.URI;
import java.net.http.HttpResponse;

public class GetDeviceCommand extends IdunGetCommand {
    private final String accessToken;
    private final String sensorUuid;

    public GetDeviceCommand(URI baseURI, String accessToken, String sensorUuid) {
        super(baseURI,"Devices");
        this.accessToken = accessToken;
        this.sensorUuid = sensorUuid;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "device/" + sensorUuid;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }

    public Device getDevice() {
        String json = null;
        HttpResponse<String> response = run();
        json = response.body();
        Device device = null;
        if (json != null) {
            device = DeviceJsonMapper.fromJson(json);
        }
        return device;
    }
}
