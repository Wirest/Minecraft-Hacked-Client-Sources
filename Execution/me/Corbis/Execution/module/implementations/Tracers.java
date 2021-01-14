package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event3D;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {

    public Tracers(){
        super("Tracers", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void on3D(Event3D event){
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityLivingBase){
                EntityLivingBase entity = (EntityLivingBase)   e;
                if(canRender(entity)){
                    float partialTicks = mc.timer.renderPartialTicks;
                    final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
                    final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
                    final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
                    drawLine(new double[]{0}, x, y, z, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                }
            }
        }
    }

    private void drawLine(double[] color, double x, double y, double z, double playerX, double playerY, double playerZ) {


        GlStateManager.color(255, 255, 255, 255);

        GL11.glLineWidth(5);
        GL11.glBegin(1);
        GL11.glVertex3d(playerX, playerY, playerZ);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(1,1,1,1);
    }

    public boolean canRender(EntityLivingBase player) {
        if(player == mc.thePlayer)
            return false;
        if ((player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager)) {
            if (player instanceof EntityPlayer && !Execution.instance.moduleManager.getModuleByName("Players").isEnabled)
                return false;
            if (player instanceof EntityAnimal && !Execution.instance.moduleManager.getModuleByName("Mobs").isEnabled)
                return false;
            if (player instanceof EntityMob && !Execution.instance.moduleManager.getModuleByName("Mobs").isEnabled)
                return false;
            if (player instanceof EntityVillager && !Execution.instance.moduleManager.getModuleByName("Villagers").isEnabled)
                return false;

        }
        if(player instanceof EntityPlayer) {
            if (AntiBot.isBot((EntityPlayer) player))
                return false;
        }
        if (mc.thePlayer.isOnSameTeam(player) && Execution.instance.moduleManager.getModuleByName("Teams").isEnabled)
            return false;
        if (player.isInvisible() && !Execution.instance.moduleManager.getModuleByName("Invisibles").isEnabled)
            return false;
        if(player.isEntityAlive()) {
            return true;
        }else {
            return false;
        }
    }


}
