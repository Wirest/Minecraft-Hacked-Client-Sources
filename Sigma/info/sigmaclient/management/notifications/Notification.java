package info.sigmaclient.management.notifications;

import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification implements INotification {

    private String header, subtext;
    private long start, displayTime;
    private Notifications.Type type;
    private float x, tarX, y;
    public Translate translate;

    @Override
    public long getLast() {
        return last;
    }

    @Override
    public void setLast(long last) {
        this.last = last;
    }

    private long last;

    protected Notification(String header, String subtext, long displayTime, Notifications.Type type) {
        this.header = header;
        this.subtext = subtext;
        this.start = System.currentTimeMillis();
        this.displayTime = displayTime;
        this.type = type;
        this.last = System.currentTimeMillis();
        ScaledResolution XD = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        y = XD.getScaledHeight() + 20;
        this.x = XD.getScaledWidth();
        float subHeaderWidth = (Client.fm.getFont("SFM 8").getWidth(subtext));
        float headerWidth = (Client.fm.getFont("SFB 11").getWidth(header));
        this.tarX = XD.getScaledWidth() - 20 - (headerWidth > subHeaderWidth ? headerWidth : subHeaderWidth);
        translate = new Translate(XD.getScaledWidth(), y);
        //Minecraft.getMinecraft().thePlayer.playSound("random.successful_hit",1,1f);
    }

    @Override
    public long checkTime() {
        return System.currentTimeMillis() - this.getDisplayTime();
    }

    @Override
    public String getHeader() {
        return this.header;
    }

    @Override
    public String getSubtext() {
        return this.subtext;
    }

    @Override
    public long getStart() {
        return this.start;
    }

    @Override
    public long getDisplayTime() {
        return this.displayTime;
    }

    @Override
    public Notifications.Type getType() {
        return this.type;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public float getTarX() {
        return tarX;
    }

    @Override
    public float getTarY() {
        return 0;
    }

    @Override
    public void setTarX(int x) {
        this.tarX = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public float getY() {
        return this.y;
    }

}
