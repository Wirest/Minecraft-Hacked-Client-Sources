package com.etb.client.gui.notification;

import java.util.ArrayList;

public class NotificationManager {

    private ArrayList<Notification> notifications = new ArrayList<>();


    public void sendClientMessage(String message, Notification.Type type) {
        notifications.add(new Notification(message, type));
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
}
