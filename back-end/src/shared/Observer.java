package shared;

public interface Observer<T> {
    void  propertyChanged(T obj);
}
