package io.entraos.idun.client;

public interface IdunApiService {
//    String fetchAccessToken();

    String fetchAccessToken(String anything) throws RuntimeException;

    //Sensors
    String getSensorAsJson(String sensorUuid);

}
