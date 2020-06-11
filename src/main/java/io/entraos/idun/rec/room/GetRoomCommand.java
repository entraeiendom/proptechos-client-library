package io.entraos.idun.rec.room;

import io.entraos.idun.commands.IdunGetCommand;
import io.entraos.rec.domain.Room;
import io.entraos.rec.mappers.RoomJsonMapper;

import java.net.URI;
import java.net.http.HttpResponse;

public class GetRoomCommand extends IdunGetCommand {
    private final String accessToken;
    private final String roomUuid;

    public GetRoomCommand(URI baseURI, String accessToken, String roomUuid) {
        super(baseURI,"Sensors");
        this.accessToken = accessToken;
        this.roomUuid = roomUuid;
    }

    @Override
    protected URI buildUri() {
        String fullUrl = getBaseUri().toString() + "json/room/" + roomUuid;
        URI fullUri = URI.create(fullUrl);
        return fullUri;
    }

    @Override
    protected String buildAuthorization() {
        return "Bearer " + accessToken;
    }

    public Room getRoom() {
        String json = null;
        HttpResponse<String> response = run();
        json = response.body();
        Room room = null;
        if (json != null) {
            room = RoomJsonMapper.fromJson(json);
        }
        return room;
    }
}
