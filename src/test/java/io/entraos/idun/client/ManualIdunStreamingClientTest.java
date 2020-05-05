package io.entraos.idun.client;

import io.entraos.idun.stream.KafkaSSLConsumer;
import io.entraos.idun.stream.RecMessageConsumer;
import io.entraos.idun.stream.RecMessageListener;
import org.slf4j.Logger;

import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.entraos.idun.client.IdunStreamingClient.NUM_THREADS;
import static org.slf4j.LoggerFactory.getLogger;

public class ManualIdunStreamingClientTest {
    private static final Logger log = getLogger(ManualIdunStreamingClientTest.class);

    public static void main(String... args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader("src/test/resources/consumer.config"));
        String topic = properties.getProperty("topic");
        String bootstrapServers = properties.getProperty("bootstrap.servers");
        String saslJaasConfig = properties.getProperty("sasl.jaas.config");
        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        //Properties from -D
        if (System.getProperty("topic") != null) {
            topic = System.getProperty("topic");
        }
        if (System.getProperty("bootstrap.servers") != null) {
            bootstrapServers = System.getProperty("bootstrap.servers");
        }
        if (System.getProperty("sasl.jaas.config") != null) {
            saslJaasConfig = System.getProperty("sasl.jaas.config");
        }
        RecMessageListener messageListener = recMessage -> log.info("Message Received: {}", recMessage);
        RecMessageConsumer streamingConsumer = new KafkaSSLConsumer(topic, bootstrapServers, saslJaasConfig);
        streamingConsumer.setMessageListener(messageListener);



        for (int i = 0; i < NUM_THREADS; i++){
            executorService.execute(streamingConsumer);
        }
    }

}