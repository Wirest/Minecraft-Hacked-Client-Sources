package nivia.utils;

import nivia.Pandora;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Notification {

    private String title;
    private String message;
    private Mode mode;
    private int showTime;
    private TimeHelper timer;
    private TimeHelper animTimer;
    private int anim;
    public static final List<Notification> notifications = new CopyOnWriteArrayList();

    public Notification(String title, String message, int showTime, Mode mode) {
        this.title = title;
        this.message = message;
        this.mode = mode;
        this.showTime = showTime;
        this.timer = new TimeHelper();
        this.timer.setLastMS();
        this.animTimer = new TimeHelper();
        this.animTimer.setLastMS();
        this.anim = Wrapper.getScaledRes().getScaledWidth();
        if (Pandora.getModManager().getModState("Notifications"))
        	notifications.add(this);
      //  else
         //   Wrapper.addChat(message);
    }

    public Notification(String message, int showTime, Mode mode) {
        this.title = null;
        this.message = message;
        this.mode = mode;
        this.showTime = showTime * 3;
        this.timer = new TimeHelper();
        this.timer.setLastMS();
        this.animTimer = new TimeHelper();
        this.animTimer.setLastMS();
        this.anim = Wrapper.getScaledRes().getScaledWidth();
        if (Pandora.getModManager().getModState("Notifications"))
        	notifications.add(this);
      //  else
        ///    Wrapper.addChat(message);
    }



    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getShowTime() {
        return showTime;
    }

    public boolean shouldRender() {
        if (timer.isDelayComplete(getShowTime()))
            return false;
        else
            return true;
    }

    public Mode getMode() {
        return mode;
    }

    public int getAnim() {
        return anim;
    }

    public void animateAnimation(boolean back) {
        if (animTimer.isDelayComplete(2)) {
            if (back) {
                this.anim -= 2;
            } else {
                this.anim += 2;
            }
            animTimer.setLastMS();
        }
    }

    public enum Mode {
        INFO, WARNING;
    }

}
