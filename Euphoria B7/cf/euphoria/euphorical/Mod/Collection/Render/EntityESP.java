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

public class EntityESP extends Mod
{
    public EntityESP() {
        super("Entity ESP", Category.RENDER);
        this.setName("EntityESP");
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
        for (final Object obj : this.mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)obj;
                if (entity == this.mc.thePlayer) {
                    continue;
                }
                if (!ModeUtils.isValidForESP(entity)) {
                    continue;
                }
                GL11.glLoadIdentity();
                this.mc.entityRenderer.orientCamera(event.partialTicks);
                final double posX = entity.posX - this.mc.getRenderManager().renderPosX;
                final double posY = entity.posY - this.mc.getRenderManager().renderPosY;
                final double posZ = entity.posZ - this.mc.getRenderManager().renderPosZ;
                if (entity.hurtTime > 5) {
                    RenderUtils.drawEntityESP(posX, posY, posZ, entity.width / 2.0f, entity.height, 1.0f, 0.0f, 0.0f, 0.2f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f);
                }
                else if (entity instanceof EntityPlayer && !Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                    RenderUtils.drawEntityESP(posX, posY, posZ, entity.width / 2.0f, entity.height, 1.0f, 1.0f, 1.0f, 0.2f, 1.0f, 1.0f, 1.0f, 0.5f, 1.0f);
                }
                else if ((entity instanceof EntityMob || entity instanceof EntitySlime) && !Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                    RenderUtils.drawEntityESP(posX, posY, posZ, entity.width / 2.0f, entity.height, 1.0f, 0.0f, 0.0f, 0.2f, 1.0f, 0.0f, 0.0f, 0.5f, 1.0f);
                }
                else if ((entity instanceof EntityCreature || entity instanceof EntityBat || entity instanceof EntitySquid || entity instanceof EntityVillager) && !Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                    RenderUtils.drawEntityESP(posX, posY, posZ, entity.width / 2.0f, entity.height, 0.0f, 1.0f, 0.3f, 0.2f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f);
                }
                else {
                    if (!Euphoria.getEuphoria().friendUtils.isFriend(entity.getName())) {
                        continue;
                    }
                    RenderUtils.drawEntityESP(posX, posY, posZ, entity.width / 2.0f, entity.height, 0.0f, 1.0f, 1.0f, 0.2f, 0.0f, 1.0f, 1.0f, 0.5f, 1.0f);
                }
            }
        }
    }
}
