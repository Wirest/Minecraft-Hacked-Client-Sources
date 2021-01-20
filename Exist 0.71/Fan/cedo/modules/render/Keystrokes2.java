package cedo.modules.render;

import cedo.events.Event;
import cedo.events.listeners.EventRenderGUI;
import cedo.events.listeners.EventTick;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.elements.Draw;
import cedo.util.font.FontUtil;
import cedo.util.render.CircleManager;
import cedo.util.render.Stencil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Keystrokes2 extends Module {

    public static CircleManager Wcircles = new CircleManager();
    public static CircleManager Acircles = new CircleManager();
    public static CircleManager Scircles = new CircleManager();
    public static CircleManager Dcircles = new CircleManager();
    public static CircleManager Lcircles = new CircleManager();
    public static CircleManager Rcircles = new CircleManager();
    public static CircleManager SPACEcircles = new CircleManager();
    NumberSetting x = new NumberSetting("X", 42, 1, 200, 2),
            y = new NumberSetting("Y", 142, 1, 500, 2),
            corners = new NumberSetting("Corners", 2, 0, 10, 1);
    BooleanSetting mouseButtons = new BooleanSetting("Mouse Buttons", true);

    public Keystrokes2() {
        super("Keystrokes", Keyboard.KEY_O, Category.RENDER);
        addSettings(x, y, corners, mouseButtons);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {


            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) && !SPACEcircles.wasHeld) {
                SPACEcircles.addCircle(x.getValue(), y.getValue() + 51, 60, 5, mc.gameSettings.keyBindJump.getKeyCode());
                SPACEcircles.wasHeld = true;
            } else {
                if (!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()))
                    SPACEcircles.wasHeld = false;
            }


            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && !Wcircles.wasHeld) {
                Wcircles.addCircle(x.getValue() + 0.5, y.getValue(), 26, 5, mc.gameSettings.keyBindForward.getKeyCode());
                Wcircles.wasHeld = true;
            } else {
                if (!Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()))
                    Wcircles.wasHeld = false;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) && !Acircles.wasHeld) {
                Acircles.addCircle(x.getValue() - 25, y.getValue() + 25, 26, 5, mc.gameSettings.keyBindLeft.getKeyCode());
                Acircles.wasHeld = true;
            } else {
                if (!Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()))
                    Acircles.wasHeld = false;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) && !Scircles.wasHeld) {
                Scircles.addCircle(x.getValue() + 0.5, y.getValue() + 25, 26, 5, mc.gameSettings.keyBindBack.getKeyCode());
                Scircles.wasHeld = true;
            } else {
                if (!Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()))
                    Scircles.wasHeld = false;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) && !Dcircles.wasHeld) {
                Dcircles.addCircle(x.getValue() + 25.5, y.getValue() + 25, 26, 5, mc.gameSettings.keyBindRight.getKeyCode());
                Dcircles.wasHeld = true;
            } else {
                if (!Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()))
                    Dcircles.wasHeld = false;
            }
            if (Mouse.isButtonDown(0) && !Lcircles.wasHeld) {
                Lcircles.addCircle(x.getValue() - 18.5, y.getValue() + 75, 35, 5, mc.gameSettings.keyBindAttack.getKeyCode());
                Lcircles.wasHeld = true;
            } else {
                if (!Mouse.isButtonDown(0))
                    Lcircles.wasHeld = false;
            }
            if (Mouse.isButtonDown(1) && !Rcircles.wasHeld) {
                Rcircles.addCircle(x.getValue() + 20, y.getValue() + 75, 35, 5, mc.gameSettings.keyBindUseItem.getKeyCode());
                Rcircles.wasHeld = true;
            } else {
                if (!Mouse.isButtonDown(1))
                    Rcircles.wasHeld = false;
            }


            int keyStrokeX = (int) (x.getValue() - 37);
            int keyStrokeY = (int) y.getValue() - 12;


            // The regular rects

            //TODO: Try This out with Rounded rects and also make a universal value to change the width and height of the entire thing


            //W key

            Draw.color(0xb2000000);
            Draw.drawRoundedRect(x.getValue() - 11, y.getValue() - 11.5, 23, 23, (float) corners.getValue());
            //Gui.drawRect(keyStrokeX + 26.5f - 1, keyStrokeY , keyStrokeX + 35 + 15.5f - 1, keyStrokeY + 25 - 1 , 0xb2000000);

            //A Key
            Draw.color(0xb2000000);
            Draw.drawRoundedRect(x.getValue() - 36, y.getValue() + 14, 23, 23, (float) corners.getValue());
            //Gui.drawRect(keyStrokeX, keyStrokeY + 26.5f - 1 , keyStrokeX + 25 - 1, keyStrokeY + 30 + 5 + 15.5f - 1 , 0xb2000000);


            //S Key
            Draw.color(0xb2000000);
            Draw.drawRoundedRect(x.getValue() - 11, y.getValue() + 14, 23, 23, (float) corners.getValue());

            //Gui.drawRect(keyStrokeX + 51/2f, keyStrokeY + 26.5f - 1 , keyStrokeX + 25 + 51/2f - 1, keyStrokeY + 30 + 5 + 15.5f - 1 , 0xb2000000);


            //D Key
            Draw.color(0xb2000000);
            Draw.drawRoundedRect(x.getValue() + 14, y.getValue() + 14, 23, 23, (float) corners.getValue());

            //Gui.drawRect(keyStrokeX + 51/2f+ 51/2f, keyStrokeY + 26.5f - 1 , keyStrokeX + 25 + 51/2f+ 51/2f - 1, keyStrokeY + 30 + 5 + 15.5f - 1 , 0xb2000000);

            //Space button
            Draw.color(0xb2000000);
            Draw.drawRoundedRect(x.getValue() - 37, y.getValue() + 39, 75, 24, (float) corners.getValue());
            //Gui.drawRect(keyStrokeX,keyStrokeY + 26.5f + 51/2f - 1, keyStrokeX + 74/2f + 38, keyStrokeY + 26.5f + 51/2f + 24 - 1 , 0xb2000000);


            if (mouseButtons.isEnabled()) {
                //L Mouse Button
                Draw.color(0xb2000000);
                Draw.drawRoundedRect(x.getValue() - 37, y.getValue() + 65, 37, 23, (float) corners.getValue());
                //Gui.drawRect(keyStrokeX, 25.5 + keyStrokeY + 26.5f + 51/2f - 1, keyStrokeX + 74/2f, 25.5 + keyStrokeY + 26.5f + 51/2f + 24 - 1 , 0xb2000000);


                //Right mouse button
                Draw.color(0xb2000000);
                Draw.drawRoundedRect(x.getValue() + 1.5, y.getValue() + 65, 37, 23, (float) corners.getValue());
                //	Gui.drawRect(keyStrokeX + 77/2f, 25.5 + keyStrokeY + 26.5f + 51/2f - 1 , keyStrokeX + 74/2f + 76/2f, 25.5 + keyStrokeY + 26.5f + 51/2f + 24 - 1 , 0xb2000000);
            }


            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();


            Stencil.write(false);
            Draw.drawRoundedRect(x.getValue() - 11, y.getValue() - 11.5, 23, 23, (float) corners.getValue());
            //Gui.drawRect(keyStrokeX + 26.5f - 1, keyStrokeY , keyStrokeX + 35 + 15.5f - 1, keyStrokeY + 25 - 1 , 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Wcircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            Draw.drawRoundedRect(x.getValue() - 36, y.getValue() + 14, 23, 23, (float) corners.getValue());
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Acircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            Draw.drawRoundedRect(x.getValue() - 11, y.getValue() + 14, 23, 23, (float) corners.getValue());
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Scircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            Draw.drawRoundedRect(x.getValue() + 14, y.getValue() + 14, 23, 23, (float) corners.getValue());
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Dcircles.drawCircles();
            Stencil.dispose();


            Stencil.write(false);
            Draw.drawRoundedRect(x.getValue() - 37, y.getValue() + 39, 75, 24, (float) corners.getValue());
            Stencil.erase(true);
            GlStateManager.enableBlend();
            SPACEcircles.drawCircles();
            Stencil.dispose();


            if (mouseButtons.isEnabled()) {
                Stencil.write(false);
                Draw.drawRoundedRect(x.getValue() - 37, y.getValue() + 65, 37, 23, (float) corners.getValue());
                Stencil.erase(true);
                GlStateManager.enableBlend();
                Lcircles.drawCircles();
                Stencil.dispose();

                Stencil.write(false);
                Draw.drawRoundedRect(x.getValue() + 1.5, y.getValue() + 65, 37, 23, (float) corners.getValue());
                Stencil.erase(true);
                GlStateManager.enableBlend();
                Rcircles.drawCircles();
                Stencil.dispose();
            }

            //W Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()), (float) x.getValue(), (float) y.getValue() - 5, -1);

            //A Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()), (float) x.getValue() - 25, (float) y.getValue() + 20, -1);

            //S Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()), (float) x.getValue(), (float) y.getValue() + 20, -1);

            //D Key
            FontUtil.clean.drawCenteredString(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()), (float) x.getValue() + 25, (float) y.getValue() + 20, -1);

            //Space Key
            FontUtil.clean.drawCenteredString("Space", (float) x.getValue(), (float) y.getValue() + 46, -1);

            if (mouseButtons.isEnabled()) {
                //LMB
                FontUtil.clean.drawCenteredString("LMB", (float) x.getValue() - 19, (float) y.getValue() + 72, -1);

                //RMB
                FontUtil.clean.drawCenteredString("RMB", (float) x.getValue() + 19, (float) y.getValue() + 72, -1);
            }


        }
        if (e instanceof EventTick) {
            Wcircles.runCircles();
            Acircles.runCircles();
            Scircles.runCircles();
            Dcircles.runCircles();
            SPACEcircles.runCircles();
            Lcircles.runCircles();
            Rcircles.runCircles();
        }

    }

}
