package cedo.modules.unused;

import cedo.events.Event;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.elements.Draw;
import cedo.util.font.FontUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class OLDKeystrokes extends Module {

    NumberSetting corners = new NumberSetting("Corners", 4, 1, 15, 1);
    NumberSetting x = new NumberSetting("X", 10, 1, 900, 2);
    NumberSetting y = new NumberSetting("Y", 270, 1, 500, 2);
    BooleanSetting mouseButtons = new BooleanSetting("Mouse Buttons", true);

    public OLDKeystrokes() {
        super("Keystrokes", Keyboard.KEY_O, Category.RENDER);
        addSettings(corners, mouseButtons, x, y);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {

            int cornerVal = (int) corners.getValue();
            int wAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) ? 175 : 75);
            int aAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) ? 175 : 75);
            int sAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) ? 175 : 75);
            int dAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) ? 175 : 75);
            int spaceAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) ? 175 : 75);
            int leftMBAlpha = (Mouse.isButtonDown(0) ? 175 : 75);
            int rightMBAlpha = (Mouse.isButtonDown(1) ? 175 : 75);

            //W key
            Draw.color(new Color(0, 0, 0, wAlpha).getRGB());
            Draw.drawRoundedRect(x.getValue() + 40, y.getValue() - 35, 30, 30, cornerVal);

            //A key
            Draw.color(new Color(0, 0, 0, aAlpha).getRGB());
            Draw.drawRoundedRect(x.getValue() + 5, y.getValue(), 30, 30, cornerVal);

            //S key
            Draw.color(new Color(0, 0, 0, sAlpha).getRGB());
            Draw.drawRoundedRect(x.getValue() + 40, y.getValue(), 30, 30, cornerVal);

            //D Key
            Draw.color(new Color(0, 0, 0, dAlpha).getRGB());
            Draw.drawRoundedRect(x.getValue() + 75, y.getValue(), 30, 30, cornerVal);

            //space bar
            Draw.color(new Color(0, 0, 0, spaceAlpha).getRGB());
            Draw.drawRoundedRect(x.getValue() + 5, y.getValue() + 35, 100, 30, cornerVal);


            //Mouse Buttons
            if (mouseButtons.isEnabled()) {
                //Left
                Draw.color(new Color(0, 0, 0, leftMBAlpha).getRGB());
                Draw.drawRoundedRect(x.getValue() + 5, y.getValue() + 70, 45, 30, cornerVal);

                //Right
                Draw.color(new Color(0, 0, 0, rightMBAlpha).getRGB());
                Draw.drawRoundedRect(x.getValue() + 60, y.getValue() + 70, 45, 30, cornerVal);

                //Left
                FontUtil.clean.drawCenteredString("LMB", (float) x.getValue() + 27, (float) y.getValue() + 80, -1);

                //Right
                FontUtil.clean.drawCenteredString("RMB", (float) x.getValue() + 83, (float) y.getValue() + 80, -1);


            }
            //W Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()), (float) x.getValue() + 55, (float) y.getValue() - 25, -1);

            //A Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()), (float) x.getValue() + 20, (float) y.getValue() + 10, -1);

            //S Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()), (float) x.getValue() + 55, (float) y.getValue() + 10, -1);

            //D Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()), (float) x.getValue() + 90, (float) y.getValue() + 10, -1);

            //Space Bar
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindJump.getKeyCode()), (float) x.getValue() + 55, (float) y.getValue() + 45, -1);
    		
    		
    	
    	
    	
    	
    	
    	
    		/*
     	int wAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) ? 125 : 50);
		int aAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) ? 125 : 50);
		int sAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) ? 125 : 50);
		int dAlpha = (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) ? 125 : 50);

		Gui.drawRect(sr.getScaledWidth() - 29 - 29, sr.getScaledHeight() - 4 - 25 - 29, sr.getScaledWidth() - 4 - 29, sr.getScaledHeight() - 4 - 29, new Color(0, 0, 0, wAlpha).getRGB());
		Gui.drawRect(sr.getScaledWidth() - 29 - 29 - 29, sr.getScaledHeight() - 4 - 25, sr.getScaledWidth() - 4 - 29 - 29, sr.getScaledHeight() - 4, new Color(0, 0, 0, aAlpha).getRGB());
		Gui.drawRect(sr.getScaledWidth() - 29 - 29, sr.getScaledHeight() - 4 - 25, sr.getScaledWidth() - 4 - 29, sr.getScaledHeight() - 4, new Color(0, 0, 0, sAlpha).getRGB());
		Gui.drawRect(sr.getScaledWidth() - 29, sr.getScaledHeight() - 4 - 25, sr.getScaledWidth() - 4, sr.getScaledHeight() - 4, new Color(0, 0, 0, dAlpha).getRGB());

		font.drawString("W", sr.getScaledWidth() - 48, sr.getScaledHeight() - 49, 0xffffffff);
		font.drawString("A", sr.getScaledWidth() - 77, sr.getScaledHeight() - 20, 0xffffffff);
		font.drawString("S", sr.getScaledWidth() - 48.5, sr.getScaledHeight() - 20, 0xffffffff);
		font.drawString("D", sr.getScaledWidth() - 19, sr.getScaledHeight() - 20, 0xffffffff);
    		 */


        }
    }
}