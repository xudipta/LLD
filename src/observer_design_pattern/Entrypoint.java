package observer_design_pattern;

import observer_design_pattern.observers.EmailNotificationObserverImpl;
import observer_design_pattern.observers.MobileNotificationObserverImpl;
import observer_design_pattern.observers.NotificationObserver;

import java.util.concurrent.TimeUnit;

public class Entrypoint {
    public static void main(String[] args) throws InterruptedException {
        PhoneObservableImpl phoneObservable = new PhoneObservableImpl();

        NotificationObserver ob1 = new EmailNotificationObserverImpl("abc@test.com", phoneObservable);
        NotificationObserver ob2 = new EmailNotificationObserverImpl("efg@test.com", phoneObservable);
        NotificationObserver ob3 = new MobileNotificationObserverImpl("1234567890", phoneObservable);

        phoneObservable.add(ob1);
        phoneObservable.add(ob2);
        phoneObservable.add(ob3);

        Thread.sleep(2000);

        phoneObservable.setData(10);

    }
}
