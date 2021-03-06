package io.entraos.idun.stream;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.metrics.Meter;
import org.slf4j.Logger;

import java.util.*;

import static io.entraos.idun.stream.json.JsonPathHelper.buildRecMessage;
import static org.slf4j.LoggerFactory.getLogger;

public class KafkaSSLConsumer implements RecMessageConsumer {
    private static final Logger log = getLogger(KafkaSSLConsumer.class);
    public static final String METRIC_NAME = "IDUN-" + KafkaSSLConsumer.class.getName();

    //Each consumer needs a unique client ID per thread
    private final List<RecMessageListener> listeners;
    private static int id = 0;
    public static final String DEFAULT_GROUP_ID = "$Default";
    public static final int DEFAULT_TIMEOUT_MS = 60000;
    public static final String DEFAULT_SECURITY_PROTOCOL = "SASL_SSL";
    public static final String DEFAULT_SASL_MECHANISM = "PLAIN";
    private final String topic;
    private final String bootstrapServers;
    private final String saslJaasConfig;
    private Meter meter;

    public KafkaSSLConsumer(String topic, String bootstrapServers, String saslJaasConfig){
        this.topic = topic;
        this.bootstrapServers = bootstrapServers;
        this.saslJaasConfig = saslJaasConfig;
        listeners = new ArrayList<>();
        Random rand = new Random();
        id = rand.nextInt(10000);
    }

    public KafkaSSLConsumer(String topic, String bootstrapServers, String saslJaasConfig, Meter meter){
        this(topic,bootstrapServers,saslJaasConfig);
        this.meter = meter;
    }



    public void run (){
        final Consumer<Long, String> consumer = createConsumer();
        log.info("Polling: {} ", topic);

        try {
            while (consumer != null) {
                final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
                for(ConsumerRecord<Long, String> cr : consumerRecords) {
                   notifyListeners(cr);
                   if (meter != null) {
                       meter.mark();
                   }
                }
                consumer.commitAsync();
            }
        } catch (CommitFailedException e) {
            log.info("CommitFailedException: {}", e.getMessage(), e);
        } finally {
            if (consumer != null) {
                consumer.close();
            }
        }
    }

    void notifyListeners(ConsumerRecord<Long, String> consumerRecord) {
        //TODO parse json
        // {"observation":{"value":"168.649993896484","quantityKind":"Pressure","sensorId":"7352a083-2540-4f70-bc48-0200d92fb5dc","observationTime":"2020-05-05T15:57:59Z"}
        String recordJson = consumerRecord.value();
        RecMessage message = buildRecMessage(recordJson);
        for (RecMessageListener listener : listeners) {
            listener.onMessage(message);
        }
    }

    Consumer<Long, String> createConsumer() {
        Consumer<Long, String> consumer;
        try {
            final Properties properties = new Properties();
            synchronized (KafkaSSLConsumer.class) {
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "IdunKafkaSSLConsumer#" + id);
                id++;
            }
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put( "bootstrap.servers", bootstrapServers);
            properties.put("group.id", DEFAULT_GROUP_ID );
            properties.put("request.timeout.ms",DEFAULT_TIMEOUT_MS);
            properties.put("security.protocol", DEFAULT_SECURITY_PROTOCOL);
            properties.put("sasl.mechanism", DEFAULT_SASL_MECHANISM);
            properties.put("sasl.jaas.config", saslJaasConfig);
            String propsMsg = "Create KafkaConsumer with these properties:\n";
            for (Object key : properties.keySet()) {
                propsMsg += "key: " + key + ", value: " + properties.get(key) + "\n";
            }
            log.info("Create KafkaConsumer with these properties: {}", propsMsg);
            consumer = new KafkaConsumer<>(properties);

            // Subscribe to the topic.
            consumer.subscribe(Collections.singletonList(topic));

        } catch (RuntimeException e) {
            log.info("Failed to subscribe to topic: {}. Reason {}", topic, e.getMessage());
            consumer = null;
        }
        return consumer;
    }

    @Override
    public void setMessageListener(RecMessageListener listener) throws RecMessageException {
        listeners.add(listener);
    }
}
