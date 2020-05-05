package io.entraos.idun.stream.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.entraos.idun.stream.RecMessage;
import io.entraos.idun.stream.RecObservationMessage;

public class JsonPathHelper {

    public static RecMessage buildRecMessage(String recordJson) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(recordJson);
        String sensorId = JsonPath.read(document, "$.observation.sensorId");
        String observationTime = JsonPath.read(document, "$.observation.observationTime");
        String value = JsonPath.read(document, "$.observation.value");
        String quantityKind = JsonPath.read(document, "$.observation.quantityKind");
        return new RecObservationMessage(sensorId, observationTime, value, quantityKind);
    }
}
