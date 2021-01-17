package me.slowly.client.ui.notifiactions;


import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ClientNotification
{
    private String message;
    private TimeHelper timer;
    private double lastY;
    private double posY;
    private double width;
    private double height;
    private double animationX;
    private int color;
    private int imageWidth;
    private ResourceLocation image;
    private long stayTime;
    Minecraft mc = Minecraft.getMinecraft();
    
    public ClientNotification(final String message, final Type type) {
        this.message = message;
        (this.timer = new TimeHelper()).reset();
        final UnicodeFontRenderer font = Client.getInstance().getFontManager().consolasbold14;
        this.width = font.getStringWidth(message) + 35;
        this.height = 20.0;
        this.animationX = this.width;
        this.stayTime = 2000L;
        this.imageWidth = 13;
        this.posY = -1.0;
        this.image = new ResourceLocation("slowly/icon/notification/" + type.name().toLowerCase() + ".png");
        if (type.equals(Type.INFO)) {
            this.color = -9667959;
        }
        else if (type.equals(Type.ERROR)) {
            this.color = FlatColors.RED.c;
        }
        else if (type.equals(Type.SUCCESS)) {
            this.color = FlatColors.GREEN.c;
        }
        else if (type.equals(Type.WARNING)) {
            this.color = FlatColors.YELLOW.c;
        }
    }
    
    public void draw(final double getY, final double lastY) {
        this.lastY = lastY;
        this.animationX = RenderUtil.getAnimationState(this.animationX, this.isFinished() ? this.width : 0.0, Math.max(this.isFinished() ? 200 : 30, Math.abs(this.animationX - (this.isFinished() ? this.width : 0.0)) * 5.0));
        if (this.posY == -1.0) {
            this.posY = getY;
        }
        else {
            this.posY = RenderUtil.getAnimationState(this.posY, getY, 200.0);
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int x1 = (int)(res.getScaledWidth() - this.width + this.animationX);
        final int x2 = (int)(res.getScaledWidth() + this.animationX);
        final int y1 = (int)this.posY;
        final int y2 = (int)(y1 + this.height);
        Gui.drawRect(x1, y1, x2, y2, this.color);
        Gui.drawRect(x1, y2, x2, y2 + 0.5f, this.color);
        Gui.drawRect(x1, y2, x2, y2 + 0.5f, ClientUtil.reAlpha(1, 0.5f));
        Gui.drawRect(x1, y1, (int)(x1 + this.height), y2, ClientUtil.reAlpha(-1, 0.1f));
        RenderUtil.drawImage(this.image, (int)(x1 + (this.height - this.imageWidth) / 2.0), y1 + (int)((this.height - this.imageWidth) / 2.0), this.imageWidth, this.imageWidth);
        final UnicodeFontRenderer font = Client.getInstance().getFontManager().consolasbold14;
        font.drawCenteredString(this.message, (float)(x1 + this.width / 2.0) + 10.0f, (float)(y1 + this.height / 3.5), -1);
    }
    
    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }
    
    private boolean isFinished() {
        return this.timer.isDelayComplete(this.stayTime) && this.posY == this.lastY;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public enum Type
    {
        SUCCESS("SUCCESS", 0), 
        INFO("INFO", 1), 
        WARNING("WARNING", 2), 
        ERROR("ERROR", 3);
        
        private Type(final String s, final int n) {
        }
    }
}
