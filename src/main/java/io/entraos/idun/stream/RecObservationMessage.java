package io.entraos.idun.stream;

public class RecObservationMessage extends RecMessage {
    private final String sensorId;
    private final String value;
    private String quantityKind;
    public RecObservationMessage(String sensorId, String observationTime, String value, String quantityKind) {
        super(observationTime);
        this.sensorId = sensorId;
        this.value = value;
        this.quantityKind = quantityKind;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getValue() {
        return value;
    }

    public String getQuantityKind() {
        return quantityKind;
    }

    public void setQuantityKind(String quantityKind) {
        this.quantityKind = quantityKind;
    }
}
