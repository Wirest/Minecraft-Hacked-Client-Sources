package me.memewaredevs.client.module.render;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.Render3DEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.*;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.function.Consumer;

public class BlockESP extends Module {
    public BlockESP() {
        super("Block ESP", 0, Module.Category.RENDER);
        this.addBoolean("Chests", true);
        this.addBoolean("Ender Chests", true);
        this.addBoolean("Dispensers", true);
        this.addBoolean("Droppers", true);
        this.addBoolean("Breakable Cakes", true);
    }

    @Handler
    public Consumer<Render3DEvent> eventConsumer0 = event -> {
        if (getBool("Breakable Cakes")) {
            final int radius = 5;
            for (int x = -radius; x < radius; ++x) {
                for (int y2 = radius; y2 > -radius; --y2) {
                    for (int z = -radius; z < radius; ++z) {
                        final int xPos = (int) this.mc.thePlayer.posX + x;
                        final int yPos = (int) this.mc.thePlayer.posY + y2;
                        final int zPos = (int) this.mc.thePlayer.posZ + z;
                        final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                        final Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
                        if (block == Blocks.cake) {
                            RenderUtils.box(blockPos.getX() - 0.5, blockPos.getY(),
                                    blockPos.getZ() - 0.5, blockPos.getX() + 0.5,
                                    blockPos.getY() + 1, blockPos.getZ() + 0.5, new Color(0, 0, 0, 21));
                        }
                    }
                }
            }
        }
        for (TileEntity entity : this.mc.theWorld.loadedTileEntityList) {
            if (entity instanceof TileEntityChest && !getBool("Chests")) {
                continue;
            }
            else if (entity instanceof TileEntityDropper && !getBool("Droppers")) {
                continue;
            }
            else if (entity instanceof TileEntityDispenser && !getBool("Dispensers")) {
                continue;
            }
            else if (entity instanceof TileEntityEnderChest && !getBool("Ender Chests")) {
                continue;
            }
            RenderUtils.drawEsp(entity, new Color(0, 0, 0, 21).getRGB());
        }
    };

}
