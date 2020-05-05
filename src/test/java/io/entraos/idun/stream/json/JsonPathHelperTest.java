package io.entraos.idun.stream.json;

import io.entraos.idun.stream.RecMessage;
import io.entraos.idun.stream.RecObservationMessage;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPathHelperTest {

    @Test
    public void buildRecMessageTest() {
        String recordJson = "{\"observation\":{\"value\":\"168.649993896484\",\"quantityKind\":\"Pressure\",\"sensorId\":\"1234-56789-101112\",\"observationTime\":\"2020-05-05T15:57:59Z\"}}";
        RecMessage recMessage = JsonPathHelper.buildRecMessage(recordJson);
        assertNotNull(recMessage);
        assertTrue(recMessage instanceof RecObservationMessage);
        RecObservationMessage observationMessage = (RecObservationMessage)recMessage;
        assertEquals("168.649993896484", observationMessage.getValue());
        assertEquals("Pressure", observationMessage.getQuantityKind());
        assertEquals("1234-56789-101112", observationMessage.getSensorId());
        assertEquals("2020-05-05T15:57:59Z", observationMessage.getObservationTime());

    }
}