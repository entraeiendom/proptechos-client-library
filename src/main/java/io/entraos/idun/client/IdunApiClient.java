package io.entraos.idun.client;


import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import io.entraos.idun.rec.sensor.GetSensorCommand;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.*;

import static org.slf4j.LoggerFactory.getLogger;


public class IdunApiClient implements IdunApiService {
    private static final Logger log = getLogger(IdunApiClient.class);
    public static final String SCOPE = "https://en.proptechos.com/api/.default";
    public static final String API_URL = "https://en.proptechos.com/api/";
    public static final URI BASE_URI = URI.create(API_URL);

    private final String tenantId;
    private final String clientId;
    private final String clientSecret;
    private String accessToken;
    private int tokenTimeToLiveSeconds;
    private Date tokenExpiresAt;
    private ConfidentialClientApplication msalApp;
    private ClientCredentialParameters clientCredentialParameters;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> scheduledFuture;

    public IdunApiClient(String tenantId, String clientId, String clientSecret) {
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        setupClient();
    }

    void setupClient() {
        try {
            msalApp =
                    ConfidentialClientApplication.builder(
                            clientId, ClientCredentialFactory.createFromSecret(clientSecret))
                            .authority("https://login.microsoftonline.com/" + tenantId + "/")
                            .build();

            clientCredentialParameters =
                    ClientCredentialParameters.builder(Collections.singleton(SCOPE)).build();
            scheduledExecutorService =
                    Executors.newScheduledThreadPool(1);

            scheduledFuture =
                    scheduledExecutorService.scheduleAtFixedRate(() -> {
                                System.out.println("Executed!");
                            },
                            1,
                            5,
                            TimeUnit.SECONDS);

        } catch (MalformedURLException e) {
            log.warn("Failed to setup MSAL application for tenantId: {}. Reason: {}", tenantId, e.getMessage());
            throw new RuntimeException("Failed to setup MSAL application for tenantId: " + tenantId, e);
        }
    }

    public String login() {
        try {
            Future<IAuthenticationResult> future = msalApp.acquireToken(clientCredentialParameters);
            tokenExpiresAt = future.get().expiresOnDate();
            accessToken = future.get().accessToken();
        } catch (ExecutionException e) {
            log.warn("Failed to login. ClientId: {} tenantId: {}. Reason: {}", clientId, tenantId, e.getMessage());
            throw new RuntimeException("Failed to login. ClientId: " + clientId + " TenantId: " + tenantId, e);
        } catch (InterruptedException e) {
            log.warn("Interrupted during login. ClientId: {} tenantId: {}. Reason: {}", clientId, tenantId, e.getMessage());
            throw new RuntimeException("Interrupted during login. ClientId: " + clientId + " TenantId: " + tenantId, e);
        }
        return accessToken;
    }

    @Override
    public String fetchAccessToken(String anything) {
        String token4Bearer = login();

        return token4Bearer;
    }

    public String getSensor(String sensorUuid) {
        String sensorJson = null;
        GetSensorCommand getSensorCommand = new GetSensorCommand(BASE_URI, accessToken, sensorUuid);
        sensorJson = getSensorCommand.getAsJson();
        return sensorJson;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Instant getTokenExpiresAt() {
        return tokenExpiresAt.toInstant();
    }


}
