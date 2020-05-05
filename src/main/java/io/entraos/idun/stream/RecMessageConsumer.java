package io.entraos.idun.stream;

public interface RecMessageConsumer extends Runnable {

    void setMessageListener(RecMessageListener listener) throws RecMessageException;
}
