package observer_design_pattern;

import observer_design_pattern.observers.NotificationObserver;

public interface StockObservable {
    void add(NotificationObserver obj);
    void remove(NotificationObserver obj);
    void notifySubscriber();
    void setData(int data);


}
