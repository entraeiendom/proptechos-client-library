package io.entraos.idun.client;


import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.slf4j.LoggerFactory.getLogger;

public class IdunApiClient implements IdunApiService {
    private static final Logger log = getLogger(IdunApiClient.class);
    public static final String SCOPE = "https://en.proptechos.com/api/.default";

    private final String tenantId;
    private final String clientId;
    private final String clientSecret;
    private String accessToken;

    public IdunApiClient(String tenantId, String clientId, String clientSecret) {
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public void login()
            throws ExecutionException, InterruptedException, MalformedURLException {

        ConfidentialClientApplication app =
                ConfidentialClientApplication.builder(
                        clientId, ClientCredentialFactory.createFromSecret(clientSecret))
                        .authority("https://login.microsoftonline.com/" + tenantId + "/")
                        .build();

        ClientCredentialParameters clientCredentialParameters =
                ClientCredentialParameters.builder(Collections.singleton(SCOPE)).build();

        Future<IAuthenticationResult> future = app.acquireToken(clientCredentialParameters);

        accessToken = future.get().accessToken();
    }

    @Override
    public String fetchAccessToken() {
        try {
            login();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public String getSensor(String sensorUuid) {
        String sensorJson = null;

        return sensorJson;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, MalformedURLException {

        if (args.length == 3) {
            IdunApiClient apiClient = new IdunApiClient(args[0], args[1], args[2]);
            apiClient.login();
            log.info("AccessToken is: {}", apiClient.getAccessToken());
        } else {
            log.error("Need 3 arguments tenantId, clientId and clientSecret");
        }
    }
}
