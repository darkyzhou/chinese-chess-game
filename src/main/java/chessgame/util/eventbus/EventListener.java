package chessgame.util.eventbus;

public interface EventListener<T> {
    void onEvent(T event);
}
