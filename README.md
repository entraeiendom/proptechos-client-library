# proptechos-client-library
Access to Idun aka ProptechOs via API and Stream

## Streaming Consumer

The parameters for topic, bootstrapServers and saslJaasConfig must be obtained from [Idun](https://idunrealestate.com/implementation/)

```java
final int NUM_THREADS = 1;
final String topic = "idun-enprod-eventhub-recipient-<customer>"
final String bootstrapServers="<host>servicebus.windows.net:9093";
final String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"<azure-primary-secret>\";"
RecMessageListener messageListener = recMessage -> log.info("Message Received: {}", recMessage);
RecMessageConsumer streamingConsumer = new KafkaSSLConsumer(topic, bootstrapServers, saslJaasConfig);
streamingConsumer.setMessageListener(messageListener);

final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
executorService.execute(streamingConsumer);
```
## Manual Test

In you IDE run
```
ManualIdunApiClient <tennantid> <clientid> <clientsecret> -DsensorId="<your-sensor-id>"
```


