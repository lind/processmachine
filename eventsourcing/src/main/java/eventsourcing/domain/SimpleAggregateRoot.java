package eventsourcing.domain;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eventsourcing.event.Event;
import eventsourcing.event.EventSourceIdentifier;

public class SimpleAggregateRoot extends AggregateRoot {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SimpleAggregateRoot(EventSourceIdentifier id) {
        super(id);
    }

    @Override
    protected void handle(Event event) {
        logger.debug("handle event: {}", event);

        Method method = ReflectionUtils.findMethod(this.getClass(), "handle", event.getClass());

        if (method == null) {
            throw new NullPointerException("No handle method for event: " + event.getClass().getSimpleName());
        }

        ReflectionUtils.invokeMethod(method, this, event);
    }
}
