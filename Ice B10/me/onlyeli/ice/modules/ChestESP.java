package me.onlyeli.ice.modules;

import me.onlyeli.ice.ui.*;
import me.onlyeli.ice.*;
import net.minecraft.client.*;
import net.minecraft.tileentity.*;
import me.onlyeli.ice.utils.*;
import java.util.*;

import org.lwjgl.input.Keyboard;

public class ChestESP extends Module
{
    public ChestESP() {
        super("ChestESP", Keyboard.KEY_NONE, Category.RENDER);
    }
    
    @Override
    public void onRender() {
        if (!this.isToggled()) {
            return;
        }
        for (final Object v : Minecraft.getMinecraft().theWorld.loadedTileEntityList) {
            if (v instanceof TileEntityChest) {
                final TileEntityChest chest = (TileEntityChest)v;
                final int x = chest.getPos().getX();
                final int y = chest.getPos().getY();
                final int z = chest.getPos().getZ();
                final double xPos = x - Minecraft.getMinecraft().renderManager.renderPosX;
                final double yPos = y - Minecraft.getMinecraft().renderManager.renderPosY;
                final double zPos = z - Minecraft.getMinecraft().renderManager.renderPosZ;
                final Random chestrandom = new Random();
                RenderUtils.drawBlockESP(xPos, yPos, zPos, 1.0f, 0.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.5f);
                RenderUtils.drawOutlinedBlockESP(xPos, yPos, zPos, 0.0f, 0.0f, 0.0f, 1.0f, 1.5f);
            }
        }
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
}
