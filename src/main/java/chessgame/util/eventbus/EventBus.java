package chessgame.util.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus<B> {
    private static final Object LISTENER_LOCK = new Object();
    private static final Object EXCEPTION_LISTENER_LOCK = new Object();

    private final List<ExceptionListener> exceptionListenerList = new ArrayList<>();

    private final Map<String, List<EventListener<? extends B>>> listenerMap = new HashMap<>();
    private final List<EventListener<B>> universalListenerList = new ArrayList<>();

    public void registerExceptionListener(ExceptionListener listener) {
        synchronized (EXCEPTION_LISTENER_LOCK) {
            exceptionListenerList.add(listener);
        }
    }

    public void registerEventListener(EventListener<B> listener) {
        synchronized (LISTENER_LOCK) {
            this.universalListenerList.add(listener);
        }
    }

    public <E extends B> void registerEventListener(Class<E> eventType, EventListener<E> listener) {
        synchronized (LISTENER_LOCK) {
            String key = eventType.getName();
            if (!listenerMap.containsKey(key)) {
                List<EventListener<? extends B>> listenerList = new ArrayList<>();
                listenerList.add(listener);
                listenerMap.put(key, listenerList);
            } else {
                listenerMap.get(key).add(listener);
            }
        }
    }

    public void unregisterExceptionListener(ExceptionListener listener) {
        synchronized (EXCEPTION_LISTENER_LOCK) {
            exceptionListenerList.remove(listener);
        }
    }

    public void unregisterEventListener(EventListener<B> listener) {
        synchronized (LISTENER_LOCK) {
            universalListenerList.remove(listener);
        }
    }

    public <E extends B> void unregisterEventListener(Class<E> eventType, EventListener<E> listener) {
        synchronized (LISTENER_LOCK) {
            listenerMap.get(eventType.getName()).remove(listener);
        }
    }

    public <E extends B> void broadcastEvent(E event) {
        Thread executor = new Thread(() -> {
            try {
                synchronized (LISTENER_LOCK) {
                    for (EventListener<B> listener : universalListenerList) {
                        listener.onEvent(event);
                    }
                    List<EventListener<? extends B>> listeners = listenerMap.get(event.getClass().getName());
                    for (EventListener<? extends B> listener : listeners) {
                        ((EventListener<E>) listener).onEvent(event);
                    }
                }
            } catch (Exception e) {
                synchronized (EXCEPTION_LISTENER_LOCK) {
                    for (ExceptionListener listener : exceptionListenerList) {
                        try {
                            listener.onException(e);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        });
        executor.start();
    }
}
