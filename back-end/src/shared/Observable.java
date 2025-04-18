package shared;

public interface Observable<T> {
    /**
     * 添加观察者。
     * @param observer 观察者。
     */
    void addObserver(Observer<T> observer);

    /**
     * 移除观察者。
     * @param observer 要移除的观察者。
     */
    void removeObserver(Observer<T> observer);

    /**
     * 暂时压制对观察者的通知。
     */
    default void suppressNotify(){}

    /**
     * 恢复对观察者的通知，不对暂停期间的改变进行任何通知。
     */
    default void resumeNotifyAndForget(){}

    /**
     * 恢复对观察者的通知，对于暂停期间的改变，进行一次性通知。
     */
    default void resumeAndNotifyOnceOnChanged(){}
}
