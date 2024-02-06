package observerdesignpattern;

import observerdesignpattern.observers.NotificationObserver;

public interface StockObservable {
    void add(NotificationObserver obj);
    void remove(NotificationObserver obj);
    void notifySubscriber();
    void setData(int data);


}
