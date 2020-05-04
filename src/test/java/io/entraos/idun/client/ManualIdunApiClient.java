package io.entraos.idun.client;

import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;

import static org.slf4j.LoggerFactory.getLogger;

public class ManualIdunApiClient {
    private static final Logger log = getLogger(ManualIdunApiClient.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        if (args.length >= 3) {
            IdunApiClient apiClient = new IdunApiClient(args[0], args[1], args[2]);
            apiClient.login();
            log.info("AccessToken is: {}\n  expires: {}", apiClient.getAccessToken(), apiClient.getTokenExpiresAt());
            String sensorId = System.getProperty("sensorId");
            if (sensorId != null) {
                String sensorJson = apiClient.getSensor(sensorId);
                log.info("Sensor: {}", sensorJson);
            }
            Thread.sleep(30);
        } else {
            log.error("Need 3 arguments tenantId, clientId and clientSecret");
        }
    }
}
