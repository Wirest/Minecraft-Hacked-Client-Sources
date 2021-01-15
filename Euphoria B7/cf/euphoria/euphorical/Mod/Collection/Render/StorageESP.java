// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import cf.euphoria.euphorical.Utils.RenderUtils;
import net.minecraft.tileentity.TileEntityChest;
import cf.euphoria.euphorical.Events.EventRender3D;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class StorageESP extends Mod
{
    public StorageESP() {
        super("Storage ESP", Category.RENDER);
        this.setName("StorageESP");
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
        for (final Object obj : this.mc.theWorld.loadedTileEntityList) {
            if (obj instanceof TileEntityChest) {
                final TileEntityChest chest = (TileEntityChest)obj;
                final double posX = chest.getPos().getX() - this.mc.getRenderManager().renderPosX;
                final double posY = chest.getPos().getY() - this.mc.getRenderManager().renderPosY;
                final double posZ = chest.getPos().getZ() - this.mc.getRenderManager().renderPosZ;
                RenderUtils.drawBlockESP(posX, posY, posZ, 1.0f, 1.0f, 0.0f, 0.11f, 1.0f, 1.0f, 0.0f, 0.5f, 1.0f);
            }
            else if (obj instanceof TileEntityEnderChest) {
                final TileEntityEnderChest enderchest = (TileEntityEnderChest)obj;
                final double posX = enderchest.getPos().getX() - this.mc.getRenderManager().renderPosX;
                final double posY = enderchest.getPos().getY() - this.mc.getRenderManager().renderPosY;
                final double posZ = enderchest.getPos().getZ() - this.mc.getRenderManager().renderPosZ;
                RenderUtils.drawBlockESP(posX, posY, posZ, 1.0f, 0.0f, 1.0f, 0.11f, 1.0f, 0.0f, 1.0f, 0.5f, 1.0f);
            }
            else {
                if (!(obj instanceof TileEntityDispenser)) {
                    continue;
                }
                final TileEntityDispenser dispenser = (TileEntityDispenser)obj;
                final double posX = dispenser.getPos().getX() - this.mc.getRenderManager().renderPosX;
                final double posY = dispenser.getPos().getY() - this.mc.getRenderManager().renderPosY;
                final double posZ = dispenser.getPos().getZ() - this.mc.getRenderManager().renderPosZ;
                RenderUtils.drawBlockESP(posX, posY, posZ, 0.0f, 0.0f, 0.0f, 0.11f, 0.0f, 0.0f, 0.0f, 0.5f, 1.0f);
            }
        }
    }
}
