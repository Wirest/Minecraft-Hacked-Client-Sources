// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.PathFinder.GotoAI;
import net.minecraft.entity.item.EntityItem;
import java.util.ArrayList;
import me.CheerioFX.FusionX.module.Module;

public class SuperMagnet extends Module
{
    public ArrayList<EntityItem> unloadedEntities;
    private GotoAI ai;
    
    public SuperMagnet() {
        super("SuperMagnet", 22, Category.SERVER);
        this.unloadedEntities = new ArrayList<EntityItem>();
    }
    
    public boolean isAutoToggle() {
        return FusionX.theClient.setmgr.getSetting(this, "AutoToggle").getValBoolean();
    }
    
    public int getItemLimit() {
        return FusionX.theClient.setmgr.getSetting(this, "ItemLimit").getValInt();
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("ItemLimit", this, 5.0, 1.0, 100.0, true));
        FusionX.theClient.setmgr.rSetting(new Setting("AutoToggle", this, true));
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onUpdate() {
        this.unloadedEntities.clear();
        super.onUpdate();
    }
    
    @Override
    public void onDisable() {
        this.unloadedEntities.clear();
        super.onDisable();
    }
    
    @EventTarget
    public void onPreMotionUpdates(final EventPreMotionUpdates event) {
        final int counter = 0;
        for (final Object entity : SuperMagnet.mc.theWorld.loadedEntityList) {
            if (this.unloadedEntities.size() > this.getItemLimit()) {
                break;
            }
            if (this.unloadedEntities.contains(entity)) {
                return;
            }
            if (!(entity instanceof EntityItem) || this.unloadedEntities.contains(entity)) {
                continue;
            }
            this.unloadedEntities.add((EntityItem)entity);
            this.tpToEntity((EntityItem)entity);
        }
        if (this.isAutoToggle()) {
            this.toggleModule();
        }
    }
    
    public void tpToEntity(final EntityItem entity) {
        final double oldPosX = SuperMagnet.mc.thePlayer.posX;
        final double oldPosY = SuperMagnet.mc.thePlayer.posY;
        final double oldPosZ = SuperMagnet.mc.thePlayer.posZ;
        (this.ai = new GotoAI(entity.getPosition())).update("tpback");
        if (this.ai.isDone() || this.ai.isFailed()) {
            this.ai.isFailed();
            this.disable();
        }
        SuperMagnet.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(oldPosX, oldPosY, oldPosZ, true));
    }
    
    private void disable() {
        this.ai.stop();
        this.ai = null;
    }
}
