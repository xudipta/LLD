package observer_design_pattern.observers;

import observer_design_pattern.StockObservable;

public class MobileNotificationObserverImpl implements NotificationObserver{

    String mob;
    StockObservable observable;

    public MobileNotificationObserverImpl(String mob, StockObservable observable){
        this.mob = mob;
        this.observable = observable;
    }

    @Override
    public void update() {
        sendSMS("SMS Notification sent",mob);
    }

    public void sendSMS(String msg, String mob){
        System.out.println(msg +" : "+mob);
    }
}
