package info.sigmaclient.management.notifications;

public interface INotification {

    String getHeader();

    String getSubtext();

    long getStart();

    long getDisplayTime();

    Notifications.Type getType();

    float getX();

    float getTarX();

    float getTarY();

    void setX(int x);

    void setTarX(int x);

    void setY(int y);

    long checkTime();

    float getY();

    long getLast();

    void setLast(long last);

}
