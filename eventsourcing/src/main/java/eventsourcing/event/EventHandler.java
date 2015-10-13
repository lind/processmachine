package eventsourcing.event;

import java.lang.annotation.*;

/**
 * Annotation for event handling methods in aggregate using {@link eventsourcing.domain
 * .AnnotatedAggregateRoot}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
}
