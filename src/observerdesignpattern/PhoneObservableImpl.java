package observerdesignpattern;

import observerdesignpattern.observers.NotificationObserver;

import java.util.ArrayList;
import java.util.List;

public class PhoneObservableImpl implements StockObservable{

    List<NotificationObserver> observers = new ArrayList<>();
    int stockCount=0;
    @Override
    public void add(NotificationObserver obj) {
        observers.add(obj);
    }

    @Override
    public void remove(NotificationObserver obj) {
        observers.remove(obj);
    }

    @Override
    public void notifySubscriber() {
        for(NotificationObserver obj: observers){
            obj.update();
        }
    }

    @Override
    public void setData(int t) {
        if(this.stockCount == 0) {
            notifySubscriber();
        }
        this.stockCount += t;
    }
}
