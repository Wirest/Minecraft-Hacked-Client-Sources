package VenusClient.online.Ui.notification;

import java.util.ArrayList;

public class NotificationManager {
    private ArrayList<Notification> notifications = new ArrayList<>();

    public void addNotification(String message, int stayTime) {
        notifications.add(new Notification(message, stayTime));
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
}
