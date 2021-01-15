// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.client.renderer.RenderGlobal;
import me.CheerioFX.FusionX.utils.RenderUtils;
import java.awt.Color;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import me.CheerioFX.FusionX.events.Event3D;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class StorageESP extends Module
{
    public static boolean chest;
    public static boolean dispenser;
    public static boolean enderChest;
    
    static {
        StorageESP.chest = true;
        StorageESP.dispenser = false;
        StorageESP.enderChest = true;
    }
    
    public StorageESP() {
        super("ChestESP", 0, Category.RENDER);
    }
    
    @EventTarget
    private void onRender(final Event3D event) {
        for (final Object ent1 : StorageESP.mc.theWorld.loadedTileEntityList) {
            final TileEntity ent2 = (TileEntity)ent1;
            if ((ent2 instanceof TileEntityChest || ent2 instanceof TileEntityDispenser || ent2 instanceof TileEntityEnderChest) && (!(ent2 instanceof TileEntityChest) || StorageESP.chest) && (!(ent2 instanceof TileEntityDispenser) || StorageESP.dispenser) && (!(ent2 instanceof TileEntityEnderChest) || StorageESP.enderChest)) {
                if (FusionX.theClient.moduleManager.getModule(AutoChestSteal.class).getState()) {
                    return;
                }
                this.drawEsp(ent2, Event3D.getPartialTicks());
            }
        }
    }
    
    private void drawEsp(final TileEntity ent, final float pTicks) {
        final double x1 = ent.getPos().getX() - RenderManager.renderPosX;
        final double y1 = ent.getPos().getY() - RenderManager.renderPosY;
        final double z1 = ent.getPos().getZ() - RenderManager.renderPosZ;
        final float[] color = this.getColor(ent);
        AxisAlignedBB box = new AxisAlignedBB(x1, y1, z1, x1 + 1.0, y1 + 1.0, z1 + 1.0);
        if (ent instanceof TileEntityChest) {
            final TileEntityChest chest = TileEntityChest.class.cast(ent);
            if (chest.adjacentChestZPos != null) {
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 1.9375);
            }
            else if (chest.adjacentChestXPos != null) {
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 1.9375, y1 + 0.875, z1 + 0.9375);
            }
            else {
                if (chest.adjacentChestZPos != null || chest.adjacentChestXPos != null || chest.adjacentChestZNeg != null || chest.adjacentChestXNeg != null) {
                    return;
                }
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 0.9375);
            }
        }
        else if (ent instanceof TileEntityEnderChest) {
            box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 0.9375);
        }
        RenderUtils.filledBox(box, new Color(color[0], color[1], color[2]).getRGB() & 0x19FFFFFF, true);
        RenderGlobal.drawOutlinedBoundingBox(box, new Color(color[0], color[1], color[2]).getRGB());
    }
    
    private float[] getColor(final TileEntity ent) {
        if (ent instanceof TileEntityChest) {
            return new float[] { 1.0f, 1.0f, 0.0f };
        }
        if (ent instanceof TileEntityDispenser) {
            return new float[] { 0.29f, 0.29f, 0.29f };
        }
        if (ent instanceof TileEntityEnderChest) {
            return new float[] { 1.0f, 0.0f, 1.0f };
        }
        return new float[] { 1.0f, 1.0f, 1.0f };
    }
}
