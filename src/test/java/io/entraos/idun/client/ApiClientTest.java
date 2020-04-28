package io.entraos.idun.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.mockito.Mockito.*;

/**
 * Test using cirquit breaker
 */
public class ApiClientTest {

    private IdunApiService service;

    @Before
    public void setUp() {
        service = mock(IdunApiService.class);
    }

    @Test
    public void whenCircuitBreakerIsUsed_thenItWorksAsExpected() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                // Percentage of failures to start short-circuit
                .failureRateThreshold(20)
                // Min number of call attempts
                .ringBufferSizeInClosedState(5)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("my");
        Function<String, String> decorated = CircuitBreaker.decorateFunction(circuitBreaker, service::fetchAccessToken);

        when(service.fetchAccessToken(anyString())).thenThrow(new RuntimeException());

        for (int i = 0; i < 10; i++) {
            try {
                decorated.apply("ehi");
            } catch (Exception ignore) {
            }
        }

        verify(service, times(5)).fetchAccessToken(anyString());
    }
}
