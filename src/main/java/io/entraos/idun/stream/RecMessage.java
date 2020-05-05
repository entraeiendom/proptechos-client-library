package io.entraos.idun.stream;

public class RecMessage {
    private final String observationTime;


    public RecMessage(String observationTime) {
        this.observationTime = observationTime;
    }

    public String getObservationTime() {
        return observationTime;
    }

    @Override
    public String toString() {
        return "RecMessage{" +
                "observationTime='" + observationTime + '\'' +
                '}';
    }
}
