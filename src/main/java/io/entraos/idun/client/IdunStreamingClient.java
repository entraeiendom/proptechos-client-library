package io.entraos.idun.client;

import io.entraos.idun.stream.KafkaSSLConsumer;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

public class IdunStreamingClient {
    private static final Logger log = getLogger(IdunStreamingClient.class);
    public final static int NUM_THREADS = 1;

    private final String topic;
    private final String bootstrapServers;
    private final String saslJaasConfig;

    public IdunStreamingClient(String topic, String bootstrapServers, String saslJaasConfig) {
        this.topic = topic;
        this.bootstrapServers = bootstrapServers;
        this.saslJaasConfig = saslJaasConfig;
    }

    void setupClient() {
        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++){
            executorService.execute(new KafkaSSLConsumer(topic, bootstrapServers, saslJaasConfig));
        }
    }
}
