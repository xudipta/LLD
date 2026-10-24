package observerdesignpattern;

import observerdesignpattern.observers.EmailNotificationObserverImpl;
import observerdesignpattern.observers.MobileNotificationObserverImpl;
import observerdesignpattern.observers.NotificationObserver;

public class Entrypoint {
    public static void main(String[] args){
        PhoneObservableImpl phoneObservable = new PhoneObservableImpl();

        NotificationObserver ob1 = new EmailNotificationObserverImpl("abc@test.com", phoneObservable);
        NotificationObserver ob2 = new EmailNotificationObserverImpl("efg@test.com", phoneObservable);
        NotificationObserver ob3 = new MobileNotificationObserverImpl("1234567890", phoneObservable);

        phoneObservable.add(ob1);
        phoneObservable.add(ob2);
        phoneObservable.add(ob3);

        try {
            Thread.sleep(2000);
            phoneObservable.setData(10);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }



    }
}
