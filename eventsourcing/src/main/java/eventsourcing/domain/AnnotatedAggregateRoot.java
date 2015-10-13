package eventsourcing.domain;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eventsourcing.event.Event;
import eventsourcing.event.EventHandler;
import eventsourcing.event.EventSourceIdentifier;

public class AnnotatedAggregateRoot<T extends EventSourceIdentifier> extends AggregateRoot<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<Class<?>, EventHandlerMethod> eventHandlerMethodMap;

    public AnnotatedAggregateRoot(T id) {
        super(id);
        eventHandlerMethodMap = findAllEventHandlers(this);
    }

    @Override
    protected void handle(Event event) {
        logger.debug("handle event: {}", event);
        EventHandlerMethod handlerMethod = eventHandlerMethodMap.get(event.getClass());

        if (handlerMethod == null) {
            throw new NullPointerException("Class " + this.getClass().getSimpleName() + " has no handle method for "
                    + "event: " + event.getClass().getSimpleName());
        }
        handlerMethod.handleEvent(event);
    }

    private Map<Class<?>, EventHandlerMethod> findAllEventHandlers(Object source) {
        Map<Class<?>, EventHandlerMethod> eventHandlers = new HashMap<>();
        Class<?> clazz = source.getClass();

        for (Method method : clazz.getMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);

            if (null != annotation) {
                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method
                            + "has @Eventhandler annotation and requires " + parameterTypes.length
                            + " number of arguments. Only one argument is allowed.");
                }

                Class<?> eventType = parameterTypes[0];
                if (eventHandlers.containsKey(eventType)) {
                    throw new IllegalStateException("Class " + source.getClass()
                            + " has more than one method annotated with @EventHandler with parametertype " + eventType
                            + "!");
                }

                // Force?
                method.setAccessible(true);
                eventHandlers.put(eventType, new EventHandlerMethod(source, method));
            }
        }
        return eventHandlers;
    }

    private class EventHandlerMethod {

        private final Object target;
        private final Method method;

        public EventHandlerMethod(Object target, Method method) {
            if (target == null) {
                throw new NullPointerException("HandlerMethod target cannot be null.");
            }
            if (method == null) {
                throw new NullPointerException("HandlerMethod method cannot be null.");
            }
            this.target = target;
            this.method = method;
        }

        public void handleEvent(Object event) {
            ReflectionUtils.invokeMethod(method, target, event);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            EventHandlerMethod that = (EventHandlerMethod) o;

            if (!method.equals(that.method)) {
                return false;
            }
            if (!target.equals(that.target)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = target.hashCode();
            result = 31 * result + method.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "EventHandlerMethod{" + "target=" + target + ", method=" + method + '}';
        }


    }
}
