// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMob;
import cf.euphoria.euphorical.Euphoria;
import net.minecraft.entity.player.EntityPlayer;
import cf.euphoria.euphorical.Utils.RenderUtils;
import org.lwjgl.opengl.GL11;
import cf.euphoria.euphorical.Utils.ModeUtils;
import net.minecraft.entity.EntityLivingBase;
import cf.euphoria.euphorical.Events.EventRender3D;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Tracers extends Mod
{
    public Tracers() {
        super("Tracers", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
    	boolean bobing = mc.gameSettings.viewBobbing;
    	mc.gameSettings.viewBobbing = false;
        for (final Object obj : this.mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)obj;
                if (entity == this.mc.thePlayer) {
                    continue;
                }
                if (!ModeUtils.isValidForTracers(entity)) {
                    continue;
                }
                GL11.glLoadIdentity();
                this.mc.entityRenderer.orientCamera(event.partialTicks);
                final double posX = entity.posX - this.mc.getRenderManager().renderPosX;
                final double posY = entity.posY - this.mc.getRenderManager().renderPosY;
                final double posZ = entity.posZ - this.mc.getRenderManager().renderPosZ;
                if (entity.hurtTime > 5) {
                    RenderUtils.drawTracerLine(posX, posY, posZ, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f);
                }
                else if (entity instanceof EntityPlayer && !Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                    RenderUtils.drawTracerLine(posX, posY, posZ, 1.0f, 1.0f, 1.0f, 0.5f, 1.0f);
                }
                else if ((entity instanceof EntityMob || entity instanceof EntitySlime) && !Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                    RenderUtils.drawTracerLine(posX, posY, posZ, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f);
                }
                else if ((entity instanceof EntityCreature || entity instanceof EntityBat || entity instanceof EntitySquid || entity instanceof EntityVillager) && !Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                    RenderUtils.drawTracerLine(posX, posY, posZ, 0.0f, 1.0f, 0.3f, 0.5f, 1.0f);
                }
                else {
                    if (!Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                        continue;
                    }
                    RenderUtils.drawTracerLine(posX, posY, posZ, 0.0f, 1.0f, 1.0f, 0.5f, 1.0f);
                }
            }
        }
        mc.gameSettings.viewBobbing = bobing;
    }
}
