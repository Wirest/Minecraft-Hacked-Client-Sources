package info.sigmaclient.management.notifications;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Notifications {

    private static Notifications instance = new Notifications();

    private List<INotification> notifications = new CopyOnWriteArrayList<>();
    private NotificationRenderer renderer = new NotificationRenderer();

    public List<INotification> getNotifications() {
        return notifications;
    }

    private Notifications() {
        Notifications.instance = this;
    }

    public static Notifications getManager() {
        return Notifications.instance;
    }

    public void post(String header, String subtext) {
        this.post(header, subtext, 2500);
    }


    public void post(String header, String subtext, Type type) {
        this.post(header, subtext, 2500, type);
    }

    public void post(String header, String subtext, long displayTime) {
        this.post(header, subtext, displayTime, Type.INFO);
    }

    public void post(String header, String subtext, long displayTime, Type type) {
        this.notifications.add(new Notification(header, subtext, displayTime, type));
    }

    public void updateAndRender() {
        if (notifications.isEmpty()) {
            return;
        }
        renderer.draw(notifications);
    }

    public enum Type {
        NOTIFY, WARNING, INFO
    }

}
