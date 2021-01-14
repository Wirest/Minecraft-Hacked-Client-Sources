package de.iotacb.client.manager;

import java.util.ArrayList;
import java.util.List;

import de.iotacb.client.gui.notification.Notification;
import net.minecraft.client.Minecraft;

public class NotificationManager {
	
	private final List<Notification> notificationsToDraw;
	private final List<Notification> notificationsToRemove;
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public NotificationManager() {
		this.notificationsToDraw = new ArrayList<Notification>();
		this.notificationsToRemove = new ArrayList<Notification>();
	}
	
	public void draw(final double yPos) {
		if (notificationsToDraw.size() > 0) {
			Notification noti = notificationsToDraw.get(0);
			noti.updateNotification();
			noti.drawNotification(yPos);
			if (noti.isRemove()) {
				notificationsToRemove.add(noti);
			}
		}
		notificationsToRemove.forEach(notif -> notificationsToDraw.remove(notif));
	}
	
	public List<Notification> getNotificationsToDraw() {
		return notificationsToDraw;
	}
	
	public List<Notification> getNotificationsToRemove() {
		return notificationsToRemove;
	}
	
	public void addNotification(final String title, final String message) {
		notificationsToDraw.add(new Notification(title, message));
	}
	
	public void clearQueue() {
		notificationsToDraw.clear();
	}

}
