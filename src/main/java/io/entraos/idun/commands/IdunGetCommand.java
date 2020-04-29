package io.entraos.idun.commands;

import io.entraos.base.commands.BaseCommand;
import io.entraos.base.commands.http.BaseHttpCommand;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

public class IdunGetCommand extends BaseHttpCommand {

    protected HttpClient client;
    private HttpRequest httpRequest;
    private Function<HttpRequest, HttpResponse> decorated;
    private CircuitBreaker circuitBreaker;
    private URI baseUri;

    protected IdunGetCommand(URI baseUri, String groupKey) {
        this(baseUri, groupKey, BaseCommand.DEFAULT_TIMEOUT);
    }

    protected IdunGetCommand(URI baseUri, String groupKey, int timeout) {
        super(baseUri, groupKey, timeout);
        client = HttpClient.newBuilder().build();
        initializeCircuitBreaker();
        this.baseUri = baseUri;

    }

    private void initializeCircuitBreaker() {
        /*
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                // Percentage of failures to start short-circuit
                .failureRateThreshold(20)
                // Min number of call attempts
                .ringBufferSizeInClosedState(5)
                .build();

         */
        CircuitBreakerConfig config = CircuitBreakerConfig.ofDefaults();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        circuitBreaker = registry.circuitBreaker(getGroupKey());
    }

    //https://www.baeldung.com/java-9-http-client
    //https://gssachdeva.wordpress.com/2015/09/02/java-8-lambda-expression-for-design-patterns-command-design-pattern/
    public String getAsJson() {
        String json = null;
        HttpResponse<String> response = run();
        json = response.body();
        return json;
    }

    //Should we impose String body on HttpCommands?
    @Override
    protected HttpResponse<String> run() {
        httpRequest = HttpRequest.newBuilder()
                .header("Authorization", buildAuthorization())
                .uri(buildUri())
                .GET()
                .build();
        decorated = CircuitBreaker.decorateFunction(circuitBreaker, httpRequest -> {
            try {
                return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });

        HttpResponse<String> response = decorated.apply(httpRequest);

        return response;
    }

    @Override
    protected URI buildUri() {
        return baseUri;
    }

    @Override
    protected String buildAuthorization() {
        return "";
    }
}
