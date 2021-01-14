package info.sigmaclient.management.notifications;

import java.util.List;

import info.sigmaclient.Client;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class NotificationRenderer implements INotificationRenderer {

    @Override
    public void draw(List<INotification> notifications) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        float y = (float) (scaledRes.getScaledHeight()) - (notifications.size() * (22));
        for (INotification notification : notifications) {
            Notification not = (Notification) notification;
            not.translate.interpolate(not.getTarX(), y, 12, 8);
            int s = scaledRes.getScaleFactor();
            float subHeaderWidth = (Client.fm.getFont("SFM 8").getWidth(not.getSubtext()));
            float headerWidth = (Client.fm.getFont("SFB 11").getWidth(not.getHeader()));
            float x = scaledRes.getScaledWidth() - 20 - (headerWidth > subHeaderWidth ? headerWidth : subHeaderWidth);

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor((int) not.translate.getX() * s, (int) (scaledRes.getScaledWidth() - not.translate.getY() * s), scaledRes.getScaledWidth() * s, (int) ((not.translate.getY() + 50) * s));
            RenderingUtil.rectangle(x, not.translate.getY(), scaledRes.getScaledWidth(), not.translate.getY() + (22) - 2, Colors.getColor(0, 200));

            for (int i = 0; i < 11; i++) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 10, (not.translate.getY() + 13), 0);
                GlStateManager.rotate(270, 0, 0, 90);
                RenderingUtil.drawCircle(0, 0, 11 - i, 3, getColor(not.getType()));
                GlStateManager.popMatrix();
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 10, (not.translate.getY() + 13), 0);
            GlStateManager.rotate(270, 0, 0, 90);
            RenderingUtil.drawCircle(0, 0, 11, 3, Colors.getColor(0));
            GlStateManager.popMatrix();

            RenderingUtil.rectangle(x + 9.6, (not.translate.getY() + 5), x + 10.3, not.translate.getY() + 13, Colors.getColor(0));
            RenderingUtil.rectangle(x + 9.6, (not.translate.getY() + 15), x + 10.3, not.translate.getY() + 17, Colors.getColor(0));

            Client.fm.getFont("SFB 11").drawStringWithShadow(not.getHeader(), (x + 18), (not.translate.getY() + 3), -1);
            Client.fm.getFont("SFM 8").drawStringWithShadow(not.getSubtext(), x + 20, not.translate.getY() + 13, -1);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();

            if (not.checkTime() >= not.getDisplayTime() + not.getStart()) {
                not.setTarX(scaledRes.getScaledWidth() + 500);
                if (not.translate.getX() >= scaledRes.getScaledWidth()) {
                    notifications.remove(notification);
                }
            }
            y += (22);
        }
    }

    private int getColor(Notifications.Type type) {
        int color = 0;
        switch (type) {
            case INFO:
                color = Colors.getColor(64, 131, 214);
                break;
            case NOTIFY:
                color = Colors.getColor(242, 206, 87);
                break;
            case WARNING:
                color = Colors.getColor(226, 74, 74);
                break;
            default:
                break;
        }
        return color;
    }

}
