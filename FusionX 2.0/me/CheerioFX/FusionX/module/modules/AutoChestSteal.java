// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.client.renderer.RenderGlobal;
import me.CheerioFX.FusionX.utils.RenderUtils;
import java.awt.Color;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.utils.BlockUtils2;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import me.CheerioFX.FusionX.events.Event3D;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.utils.Wrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.inventory.GuiChest;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.TimerUtils;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import me.CheerioFX.FusionX.module.Module;

public class AutoChestSteal extends Module
{
    public static boolean chest;
    public static boolean dispenser;
    public static boolean enderChest;
    public static ArrayList<BlockPos> invalid;
    public Long delay;
    private final TimerUtils timer;
    boolean doneSteal;
    
    static {
        AutoChestSteal.chest = true;
        AutoChestSteal.dispenser = false;
        AutoChestSteal.enderChest = true;
        AutoChestSteal.invalid = new ArrayList<BlockPos>();
    }
    
    public AutoChestSteal() {
        super("AutoChestSteal", 0, Category.OTHER);
        this.delay = 2L;
        this.timer = new TimerUtils();
    }
    
    @Override
    public void setup() {
    }
    
    @Override
    public void onEnable() {
        this.doneSteal = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.doneSteal = true;
        AutoChestSteal.invalid.clear();
        super.onDisable();
    }
    
    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index <= chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
    
    @EventTarget
    public void onEventCalled(final EventPreMotionUpdates event) {
        if (Wrapper.mc.currentScreen instanceof GuiChest) {
            final GuiChest chest = (GuiChest)Wrapper.mc.currentScreen;
            if (this.isChestEmpty(chest) || this.isInventoryFull()) {
                Wrapper.mc.thePlayer.closeScreen();
                this.doneSteal = true;
            }
            for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                if (stack != null && this.timer.hasTimeElapsed(this.delay * 10L, false)) {
                    Wrapper.mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, Wrapper.mc.thePlayer);
                    this.timer.reset();
                }
            }
        }
        else if (this.timer.hasTimeElapsed(this.delay * 150L, false)) {
            this.doneSteal = true;
            this.timer.reset();
        }
    }
    
    @EventTarget
    private void onRender(final Event3D event) {
        for (final Object ent1 : AutoChestSteal.mc.theWorld.loadedTileEntityList) {
            final TileEntity ent2 = (TileEntity)ent1;
            if ((ent2 instanceof TileEntityChest || ent2 instanceof TileEntityDispenser || ent2 instanceof TileEntityEnderChest) && (!(ent2 instanceof TileEntityChest) || AutoChestSteal.chest) && (!(ent2 instanceof TileEntityDispenser) || AutoChestSteal.dispenser) && (!(ent2 instanceof TileEntityEnderChest) || AutoChestSteal.enderChest)) {
                this.drawEsp(ent2, Event3D.getPartialTicks());
            }
        }
    }
    
    private void drawEsp(final TileEntity ent, final float pTicks) {
        final BlockPos entPos = ent.getPos();
        if (BlockUtils2.getDistanceToBlock(entPos) < 4.0 && !AutoChestSteal.invalid.contains(entPos) && !FusionX.theClient.moduleManager.getModule(Flight.class).getState() && this.doneSteal) {
            BlockUtils2.rcActionLegit(entPos);
            AutoChestSteal.invalid.add(entPos);
            this.doneSteal = false;
            return;
        }
        if (AutoChestSteal.invalid.contains(entPos)) {
            return;
        }
        final double x1 = entPos.getX() - RenderManager.renderPosX;
        final double y1 = entPos.getY() - RenderManager.renderPosY;
        final double z1 = entPos.getZ() - RenderManager.renderPosZ;
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
