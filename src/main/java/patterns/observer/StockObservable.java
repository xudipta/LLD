package patterns.observer;

import patterns.observer.observers.NotificationObserver;

public interface StockObservable {
    void add(NotificationObserver obj);
    void remove(NotificationObserver obj);
    void notifySubscriber();
    void setData(int data);


}
