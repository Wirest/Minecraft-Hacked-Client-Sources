package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event3D;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NameTags extends Module {
    UnicodeFontRenderer ufr;
    public NameTags(){
        super("NameTags", Keyboard.KEY_NONE, Category.RENDER);
    }



    @EventTarget
    public void on3D(Event3D event){
        if(ufr == null) {
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 2, 2);

        }
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityLivingBase){
                EntityLivingBase entity = (EntityLivingBase) e;
                if(entity instanceof EntityPlayer){
                    EntityPlayer player = (EntityPlayer) entity;
                    double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * this.mc.timer.renderPartialTicks
                            - this.mc.getRenderManager().renderPosX;
                    double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * this.mc.timer.renderPartialTicks
                            - this.mc.getRenderManager().renderPosY;
                    double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * this.mc.timer.renderPartialTicks
                            - this.mc.getRenderManager().renderPosZ;


                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y + 2.5D, z);
                    GL11.glScalef(-0.03f, -0.03f, -0.03f);

                    GL11.glRotated(-this.mc.getRenderManager().playerViewY, 0.0d, 1.0d, 0.0d);
                    GL11.glRotated(this.mc.getRenderManager().playerViewX, 1.0d, 0.0d, 0.0d);
                    GlStateManager.disableDepth();
                    float width = ufr.getStringWidth(player.getName());
                    float progress = player.getHealth() / player.getMaxHealth();

                    Color color = Color.WHITE;
                    if(player.getHealth() > 15){
                        color = Color.GREEN;
                    }else if(player.getHealth() > 7 && player.getHealth() <= 15){
                        color = Color.YELLOW;
                    }else if(player.getHealth() <= 7){
                        color = Color.RED;
                    }
                    Gui.drawRect(-width / 2 - 5, -2, width / 2 + 5, ufr.FONT_HEIGHT + 2, new Color(0, 0, 0, 80).getRGB());
                    Gui.drawRect(-width / 2 - 5, ufr.FONT_HEIGHT + 1,  -width / 2 - 5 + (width / 2 + 5 - -width / 2 + 5) * progress, ufr.FONT_HEIGHT + 2, color.getRGB());
                    ufr.drawCenteredString(player.getName(), 0, -1, 0xFFFFFFFF);
                    GL11.glTranslated(-x, -(y + 2.5D), -z);
                    GL11.glScalef(1.0f,1.0f,1.0f);
                    GlStateManager.enableDepth();
                    GL11.glPopMatrix();



                }
            }
        }
    }

}
