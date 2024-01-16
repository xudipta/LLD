package observer_design_pattern.observers;

import observer_design_pattern.StockObservable;

public class EmailNotificationObserverImpl implements NotificationObserver{

    StockObservable observable;
    String email;
    public EmailNotificationObserverImpl(String email, StockObservable obj){
        this.email= email;
        this.observable= obj;
    }
    @Override
    public void update() {
        sendEmail("Email notification Sent",email);
    }
    public void sendEmail(String msg, String email){
        System.out.println(msg +" : "+email);
    }
}
