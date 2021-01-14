package cedo.modules.render;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.animations.Direction;
import cedo.ui.animations.impl.DecelerateAnimation;
import cedo.ui.animations.impl.SmoothStepAnimation;
import cedo.ui.elements.Draw;
import cedo.ui.elements.Rectangle;
import cedo.util.ColorManager;
import cedo.util.Logger;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import cedo.util.random.MathUtils;
import cedo.util.render.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SuppressWarnings("rawtypes")
public class TargetHUD extends Module {
    private ModeSetting targetHudMode = new ModeSetting("Mode", "Astolfo", "Astolfo", "Moon");

    private final NumberSetting xValue = new NumberSetting("X", 30, 0,85,1),
      yValue = new NumberSetting("Y", 40,0,50,1),

    //Color Settings
    red = new NumberSetting("Red", 0,0,255,1),
    green = new NumberSetting("Green", 0,0,255,1),
    blue = new NumberSetting("Blue", 0,0,255,1);


    //Animations
    DecelerateAnimation barAnimation;
    float targetPrevHealth = 0;
    private EntityLivingBase lastTarget;

    public TargetHUD() {
        super("TargetHUD", Keyboard.KEY_O, Category.RENDER);
        addSettings(targetHudMode, xValue, yValue, red,green,blue);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {
            this.setSuffix(targetHudMode.getSelected());

            //Color Math
            float red = (float) (this.red.getValue() / 255);
            float green = (float) (this.green.getValue() / 255);
            float blue = (float) (this.blue.getValue() / 255);

            int color = new Color(red,green,blue,1).getRGB();
            MinecraftFontRenderer font = Fan.getClientFont("Big", false);

            int x = (int) (xValue.getValue() * 10);
            int y = (int) (yValue.getValue() * 10);

            EntityLivingBase target = Fan.killaura.target;

            if(target != null){
                float healthDiff = targetPrevHealth - target.getHealth();
                if(target != lastTarget) {
                    healthDiff = 0;
                    barAnimation = null;
                }
                if (barAnimation != null && barAnimation.isDone()) barAnimation = null;
                if (healthDiff != 0) { //The moment health changes
                    if (barAnimation == null || barAnimation.isDone()) {
                        barAnimation = new DecelerateAnimation(900, healthDiff, Direction.BACKWARDS);
                    } else {
                        barAnimation = new DecelerateAnimation(900, barAnimation.getOutput() / barAnimation.getEndPoint() + healthDiff, Direction.BACKWARDS);
                    }
                }
                switch (targetHudMode.getSelected()){
                    case "Astolfo":

                        //Rectangle Color
                        Draw.color(new Color(0.1f,0.1f,0.1f,0.9f).getRGB());
                        //Rectangle
                        Gui.drawRect2(x, y, 155,60,new Color(0.1f,0.1f,0.1f,0.9f).getRGB());

                        //Name
                        font.drawStringWithShadow(target.getName(), x + 31,y + 5, 0xffffffff);

                        //Health
                        GL11.glPushMatrix();
                        GlStateManager.translate(x,y,1);
                        GL11.glScalef(2,2,2);
                        GlStateManager.translate(-x,-y,1);
                        mc.fontRendererObj.drawStringWithShadow(MathUtils.round((target.getHealth() / 2.0f), 1) + " \u2764",
                                x + 16,y + 10,new Color(color).darker().getRGB());
                        GL11.glPopMatrix();

                        GlStateManager.color(1,1,1,1);
                        GuiInventory.drawEntityOnScreen(x + 16,y + 55, 25, target.rotationYaw, -target.rotationPitch, target);

                        int xHealthbar = 30;
                        int yHealthbar = 46;

                        //Background rect
                        Gui.drawRect2(x + xHealthbar,y + yHealthbar,120,10, new Color(color).darker().darker().darker().getRGB());

                        //Delayed Bar
                        Gui.drawRect2(x + xHealthbar,y + yHealthbar,(target.getHealth() + (barAnimation != null ? barAnimation.getOutput() : 0)) / target.getMaxHealth() * 120,10, new Color(color).darker().getRGB());

                        //Health Bar
                        Gui.drawRect2(x + xHealthbar,y + yHealthbar,target.getHealth() / target.getMaxHealth() * 120, 10, new Color(color).getRGB());

                        for (int index = 1; index < 5; index++) {

                            if (target.getEquipmentInSlot(index) == null)
                                continue;

                            Nametags.renderItem(target.getEquipmentInSlot(index), (int) (x + 80 + font.getStringWidth(target.getName())), (int) (y + (index * -11.5) + 36));
                        }

                        break;


                    case "Moon":

                        break;

                }
                targetPrevHealth = target.getHealth();
                lastTarget = target;
            } else {
                targetPrevHealth = 0;
                //barAnimation = null;
                lastTarget = null;
            }
        }
    }
    private void renderPlayer2d(final double n, final double n2, final float n3, final float n4, final int n5, final int n6, final int n7, final int n8, final float n9, final float n10, final AbstractClientPlayer abstractClientPlayer) {
        mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
        GL11.glEnable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawScaledCustomSizeModalRect((int)n, (int)n2, n3, n4, n5, n6, n7, n8, n9, n10);
        GL11.glDisable(3042);
    }
}
