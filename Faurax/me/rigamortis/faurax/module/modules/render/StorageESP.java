package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import org.lwjgl.opengl.*;
import java.util.*;
import net.minecraft.block.*;
import com.darkmagician6.eventapi.*;

public class StorageESP extends Module implements RenderHelper, WorldHelper, CombatHelper
{
    public StorageESP() {
        this.setName("StorageESP");
        this.setType(ModuleType.RENDER);
        this.setKey("B");
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void renderWorld(final EventRenderWorld event) {
        if (this.isToggled()) {
            for (final Object o : StorageESP.mc.theWorld.loadedTileEntityList) {
                final TileEntity tileEntity = (TileEntity)o;
                final double n = tileEntity.getPos().getX();
                StorageESP.mc.getRenderManager();
                final double x = n - RenderManager.renderPosX;
                final double n2 = tileEntity.getPos().getY();
                StorageESP.mc.getRenderManager();
                final double y = n2 - RenderManager.renderPosY;
                final double n3 = tileEntity.getPos().getZ();
                StorageESP.mc.getRenderManager();
                final double z = n3 - RenderManager.renderPosZ;
                if (tileEntity instanceof TileEntityFurnace) {
                    StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, 1717987071);
                    StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1717986848);
                }
                if (tileEntity instanceof TileEntityHopper) {
                    StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, -2004317953);
                    StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), -2004318176);
                }
                if (tileEntity instanceof TileEntityDropper) {
                    StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, -2004317953);
                    StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), -2004318176);
                }
                if (tileEntity instanceof TileEntityDispenser) {
                    StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, -2004317953);
                    StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), -2004318176);
                }
                if (tileEntity instanceof TileEntityEnderChest) {
                    StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, 294134527);
                    StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 294134304);
                }
                if (tileEntity instanceof TileEntityBrewingStand) {
                    StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, 288585727);
                    StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 288585504);
                }
                if (tileEntity instanceof TileEntityChest) {
                    final TileEntityChest tileChest = (TileEntityChest)tileEntity;
                    final Block chest = StorageESP.mc.theWorld.getBlockState(tileChest.getPos()).getBlock();
                    final Block border = StorageESP.mc.theWorld.getBlockState(new BlockPos(tileChest.getPos().getX(), tileChest.getPos().getY(), tileChest.getPos().getZ() - 1)).getBlock();
                    final Block border2 = StorageESP.mc.theWorld.getBlockState(new BlockPos(tileChest.getPos().getX(), tileChest.getPos().getY(), tileChest.getPos().getZ() + 1)).getBlock();
                    final Block border3 = StorageESP.mc.theWorld.getBlockState(new BlockPos(tileChest.getPos().getX() - 1, tileChest.getPos().getY(), tileChest.getPos().getZ())).getBlock();
                    final Block border4 = StorageESP.mc.theWorld.getBlockState(new BlockPos(tileChest.getPos().getX() + 1, tileChest.getPos().getY(), tileChest.getPos().getZ())).getBlock();
                    if (chest == Blocks.chest && border != Blocks.chest) {
                        if (border2 == Blocks.chest) {
                            StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 2.0), 1.5f, -2006576743);
                            StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 2.0), -2006576864);
                        }
                        else if (border4 == Blocks.chest) {
                            StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 2.0, y + 1.0, z + 1.0), 1.5f, -2006576743);
                            StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 2.0, y + 1.0, z + 1.0), -2006576864);
                        }
                        else if (border4 == Blocks.chest) {
                            StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, -2006576743);
                            StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), -2006576864);
                        }
                        else if (border != Blocks.chest && border2 != Blocks.chest && border3 != Blocks.chest && border4 != Blocks.chest) {
                            StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, -2006576743);
                            StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), -2006576864);
                        }
                    }
                    if (chest != Blocks.trapped_chest || border == Blocks.trapped_chest) {
                        continue;
                    }
                    if (border2 == Blocks.trapped_chest) {
                        StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 2.0), 1.5f, 1997603071);
                        StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 2.0), 1997602848);
                    }
                    else if (border4 == Blocks.trapped_chest) {
                        StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 2.0, y + 1.0, z + 1.0), 1.5f, 1997603071);
                        StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 2.0, y + 1.0, z + 1.0), 1997602848);
                    }
                    else if (border4 == Blocks.trapped_chest) {
                        StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, 1997603071);
                        StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1997602848);
                    }
                    else {
                        if (border == Blocks.trapped_chest || border2 == Blocks.trapped_chest || border3 == Blocks.trapped_chest || border4 == Blocks.trapped_chest) {
                            continue;
                        }
                        StorageESP.guiUtils.drawBoundingBoxESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1.5f, 1997603071);
                        StorageESP.guiUtils.drawFilledBBESP(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 1997602848);
                    }
                }
            }
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
