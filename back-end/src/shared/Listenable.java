package shared;

/**
 *
 * @param <T>
 */
public interface Listenable<T> {
    void addListener(ListListener<T> listener);
    void removeListener(ListListener<T> listener);
}
