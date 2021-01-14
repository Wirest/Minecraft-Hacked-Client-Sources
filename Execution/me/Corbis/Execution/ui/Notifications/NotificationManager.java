package me.Corbis.Execution.ui.Notifications;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {

    LinkedBlockingQueue<Notification> notifications = new LinkedBlockingQueue<>();


    public void show(Notification notificationIn){
        this.notifications.add(notificationIn);
    }

    public void render(){
        if(notifications.isEmpty())
            return;
        for(Notification notification : notifications){
            notification.draw();
        }

    }



}
