package nivia.modules.render;

import org.lwjgl.opengl.GL11;
import nivia.events.EventTarget;
import nivia.events.events.Event2D;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Notification;
import nivia.utils.Notification.Mode;
import nivia.utils.Wrapper;

import java.awt.*;
import java.util.StringTokenizer;

public class Notifications extends Module {

	public Notifications() {
		super("Notifications", 0, 0, Category.RENDER, "Displays notifications instead of adding chat messages.",
				new String[] { "notif", "bulletin", "bulletins" }, false);
	}

	@EventTarget
	public void onRender(Event2D e) {
        GL11.glPushMatrix();
      //  GL11.glEnable(3042);
       // GL11.glBlendFunc(770, 771);
      //  GL11.glDisable(3553);
     //   GL11.glEnable(2848);
        int y = Wrapper.getScaledRes().getScaledHeight() - 20;

        for (Notification not : Notification.notifications) {
            if (not.shouldRender()) {

                if (not.getAnim() >= Wrapper.getScaledRes().getScaledWidth() - 180)
                    not.animateAnimation(true);
            } else {
                not.animateAnimation(false);
                if (not.getAnim() > Wrapper.getScaledRes().getScaledWidth()) {
                    Notification.notifications.remove(not);
                    y += 50;
                }
            }
            y -= 50;
        	//Helper.get2DUtils().drawHLine(not.getAnim(), y, 1, y + 42);
            Helper.get2DUtils().drawBorderedRect(not.getAnim(), y, Wrapper.getScaledRes().getScaledWidth(), y + 42, 1,
                    new Color(40, 40, 50).getRGB(), new Color(80, 80, 90).getRGB());
           // Helper.get2DUtils().drawBorderedRect(Wrapper.getScaledRes().getScaledWidth() - 1, y, not.getAnim(), y + 42, 1,
            //        -1, -1);
            //Helper.get2DUtils().drawBorderedRect(not.getAnim() + 60, y + 11, not.getAnim() + 120, y + 12, 0, Helper.colorUtils().RGBtoHEX(20, 20, 20, 180));
            //Helper.get2DUtils().drawHl
            if (not.getMode() == Mode.WARNING) {
                GL11.glColor4f(1, 1, 1, 1);
               // ResourceLocation loc = new ResourceLocation("textures/pascalhook/warning.png");
                //Wrapper.drawIcon(loc,not.getAnim() + 3f, y + 3.5f, not.getAnim() + 3f + 35, y + 3.5f + 35);
                if (not.getTitle() != null)
                    mc.fontRendererObj.drawStringWithShadow(not.getTitle(),
                            (not.getAnim() + 90) - (mc.fontRendererObj.getStringWidth(not.getTitle()) / 2), y + 2,
                            new Color(255, 60, 0).getRGB());
            } else if (not.getMode() == Mode.INFO) {
                GL11.glColor4f(1, 1, 1, 1);
               // ResourceLocation loc = new ResourceLocation("textures/pascalhook/information.png");
               // Wrapper.drawIcon(loc,not.getAnim() + 3f, y + 3.5f, not.getAnim() + 3f + 35, y + 3.5f + 35);

                if (not.getTitle() != null)
                    mc.fontRendererObj.drawStringWithShadow(not.getTitle(),
                            (not.getAnim() + 90) - (mc.fontRendererObj.getStringWidth(not.getTitle()) / 2), y + 2,
                            new Color(0, 180, 255).getRGB());
            }
            int y2 = y;
            if (not.getTitle() != null)
                y2 = y + 10;
            for (String line : splitIntoLine(not.getMessage(), 29)) {
            	//33
                mc.fontRendererObj.drawStringWithShadow(line, (float) (not.getAnim() + 43), y2 + 4, -1);
                y2 += mc.fontRendererObj.FONT_HEIGHT + 2;
            }

        }
       // GL11.glDisable(2848);
      //  GL11.glEnable(3553);
      //  GL11.glEnable(2929);
        // GL11.glDepthMask(true);
      //  GL11.glDisable(3042);
        GL11.glColor4f(255, 255, 255, 255);
        GL11.glPopMatrix();
	}
	
	public String[] splitIntoLine(String input, int maxCharInLine) {

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while (word.length() > maxCharInLine) {
                output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
                word = word.substring(maxCharInLine - lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        // output.split();
        // return output.toString();
        return output.toString().split("\n");
    }

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
