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

public class ApiClient {
    private static final Logger log = getLogger(ApiClient.class);
    public static final String SCOPE = "https://en.proptechos.com/api/.default";

    private String accessToken;

    public void login(String tenantId, String clientId, String clientSecret)
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

    public String getAccessToken() {
        return accessToken;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, MalformedURLException {
        ApiClient apiClient = new ApiClient();
        if (args.length == 3) {
            apiClient.login(args[0], args[1], args[2]);
            log.info("AccessToken is: {}", apiClient.getAccessToken());
        } else {
            log.error("Need 3 arguments tenantId, clientId and clientSecret");
        }
    }
}
