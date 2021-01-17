/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventRender;
import me.slowly.client.events.EventRenderBlock;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.BlockPos;

public class BlockESP
extends Mod {
    private static ArrayList<Integer> blockIds = new ArrayList();
    private ArrayList<BlockPos> toRender = new ArrayList();
    private Value<Double> blockLimit = new Value<Double>("BlockESP_Block Limit", 250.0, 10.0, 1000.0, 50.0);

    public BlockESP() {
        super("BlockESP", Mod.Category.RENDER, Colors.DARKORANGE.c);
    }

    @Override
    public void onEnable() {
        this.mc.renderGlobal.loadRenderers();
        this.toRender.clear();
        super.onEnable();
        ClientUtil.sendClientMessage("BlockESP Enable", ClientNotification.Type.SUCCESS);
    }
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("BlockESP Disable", ClientNotification.Type.ERROR);
    }

    @EventTarget
    public void onRenderBlock(EventRenderBlock event) {
        this.setColor(-6710887);
        BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
        if ((double)this.toRender.size() < this.blockLimit.getValueState() && !this.toRender.contains(pos) && blockIds.contains(new Integer(Block.getIdFromBlock(event.getBlock())))) {
            this.toRender.add(pos);
        }
        int i = 0;
        while (i < this.toRender.size()) {
            BlockPos pos_1 = this.toRender.get(i);
            int id = Block.getIdFromBlock(this.mc.theWorld.getBlockState(pos_1).getBlock());
            if (!blockIds.contains(id)) {
                this.toRender.remove(i);
            }
            ++i;
        }
    }

    @EventTarget
    public void onRender(EventRender event) {
        for (BlockPos pos : this.toRender) {
            this.renderBlock(pos);
        }
    }

    private void renderBlock(BlockPos pos) {
        this.mc.getRenderManager();
        double x = (double)pos.getX() - RenderManager.renderPosX;
        this.mc.getRenderManager();
        double y = (double)pos.getY() - RenderManager.renderPosY;
        this.mc.getRenderManager();
        double z = (double)pos.getZ() - RenderManager.renderPosZ;
        RenderUtil.drawSolidBlockESP(x, y, z, 0.0f, 0.5f, 1.0f, 0.25f);
    }

    public static ArrayList<Integer> getBlockIds() {
        return blockIds;
    }
}

