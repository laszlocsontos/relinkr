package io.relinkr.core.model;

import java.time.Instant;
import org.springframework.context.ApplicationEvent;

public class GenericApplicationEvent<S> extends ApplicationEvent {

    private final Instant instant;

    public GenericApplicationEvent(S source, Instant instant) {
        super(source);
        this.instant = instant;
    }

    @Override
    public S getSource() {
        return (S) super.getSource();
    }

    public final Instant getInstant() {
        return instant;
    }

}
