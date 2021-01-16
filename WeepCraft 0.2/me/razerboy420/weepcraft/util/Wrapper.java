/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.modules.graphics.Crosshair;
import me.razerboy420.weepcraft.settings.EnumColor;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.RainbowUtil;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class Wrapper {
    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return Wrapper.mc().player;
    }

    public static WorldClient getWorld() {
        return Wrapper.mc().world;
    }

    public static ArrayList<EntityPlayer> getLoadedPlayers() {
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        for (Object o : Wrapper.getWorld().loadedEntityList) {
            if (!(o instanceof EntityPlayer)) continue;
            EntityPlayer p = (EntityPlayer)o;
            list.add(p);
        }
        return list;
    }

    public static ArrayList<Object> getEntities(Object type) {
        ArrayList<Object> all = new ArrayList<Object>();
        for (Object o : Wrapper.getWorld().loadedEntityList) {
            if (!o.getClass().equals(type)) continue;
            all.add(o);
        }
        return all;
    }

    protected static NetworkPlayerInfo getPlayerInfo(EntityPlayer e) {
        NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(e.getUniqueID());
        return playerInfo;
    }

    public static GameSettings getSettings() {
        return Wrapper.mc().gameSettings;
    }

    public static void sendPacketDirect(Packet packet) {
        NetHandlerPlayClient.netManager.sendPacket(packet);
    }

    public static double[] moveLooking(float yawOffset) {
        float dir = Wrapper.getPlayer().rotationYaw + yawOffset;
        float xD = (float)Math.cos((double)((dir += Wrapper.getPlayer().moveStrafing < 0.0f ? 90.0f * (Wrapper.getPlayer().moveForward > 0.0f ? 0.5f : (Wrapper.getPlayer().moveForward < 0.0f ? -0.5f : 1.0f)) : (Wrapper.getPlayer().moveStrafing > 0.0f ? -90.0f * (Wrapper.getPlayer().moveForward > 0.0f ? 0.5f : (Wrapper.getPlayer().moveForward < 0.0f ? -0.5f : 1.0f)) : (Wrapper.getPlayer().moveForward < 0.0f ? 180.0f : 0.0f))) + 90.0f) * 3.141592653589793 / 180.0);
        float zD = (float)Math.sin((double)(dir + 90.0f) * 3.141592653589793 / 180.0);
        return new double[]{xD, zD};
    }

    public static PlayerControllerMP getPlayerController() {
        return Wrapper.mc().playerController;
    }

    public static FontRenderer fr() {
        return Wrapper.mc().fontRendererObj;
    }

    public static FontRenderer clientFont() {
        return Wrapper.mc().fontRendererObj;
    }

    public static boolean isPlayerMoving() {
        if (Wrapper.getPlayer().moveForward == 0.0f && Wrapper.getPlayer().moveStrafing == 0.0f) {
            return false;
        }
        return true;
    }

    public static void drawOutlineRect(float drawX, float drawY, float drawWidth, float drawHeight, int color) {
        Gui.drawRect(drawX, drawY, drawWidth, drawY + 1.0f, color);
        Gui.drawRect(drawX, drawY + 2.0f, drawX + 2.0f, drawHeight, color);
        Gui.drawRect(drawWidth, drawY, drawWidth + 2.0f, drawHeight, color);
        Gui.drawRect(drawX + 2.0f, drawHeight - 2.0f, drawWidth, drawHeight, color);
    }

    public static void drawCrosshair(float x, float y) {
        Wrapper.drawBorderRect(x - Crosshair.gap.value.floatValue() - Crosshair.size.value.floatValue() - Crosshair.thickness.value.floatValue(), y - Crosshair.thickness.value.floatValue(), x - Crosshair.gap.value.floatValue() - Crosshair.thickness.value.floatValue(), y + Crosshair.thickness.value.floatValue(), -16777216, RainbowUtil.getColorViaHue(Crosshair.color.value.floatValue()).getRGB(), Crosshair.outline.value.floatValue());
        Wrapper.drawBorderRect(x + Crosshair.gap.value.floatValue() + Crosshair.thickness.value.floatValue(), y - Crosshair.thickness.value.floatValue(), x + Crosshair.gap.value.floatValue() + Crosshair.size.value.floatValue() + Crosshair.thickness.value.floatValue(), y + Crosshair.thickness.value.floatValue(), -16777216, RainbowUtil.getColorViaHue(Crosshair.color.value.floatValue()).getRGB(), Crosshair.outline.value.floatValue());
        Wrapper.drawBorderRect(x - Crosshair.thickness.value.floatValue(), y - Crosshair.gap.value.floatValue() - Crosshair.size.value.floatValue() - Crosshair.thickness.value.floatValue(), x + Crosshair.thickness.value.floatValue(), y - Crosshair.gap.value.floatValue() - Crosshair.thickness.value.floatValue(), -16777216, RainbowUtil.getColorViaHue(Crosshair.color.value.floatValue()).getRGB(), Crosshair.outline.value.floatValue());
        Wrapper.drawBorderRect(x - Crosshair.thickness.value.floatValue(), y + Crosshair.gap.value.floatValue() + Crosshair.thickness.value.floatValue(), x + Crosshair.thickness.value.floatValue(), y + Crosshair.gap.value.floatValue() + Crosshair.size.value.floatValue() + Crosshair.thickness.value.floatValue(), -16777216, RainbowUtil.getColorViaHue(Crosshair.color.value.floatValue()).getRGB(), Crosshair.outline.value.floatValue());
    }

    public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double blocks) {
        return Wrapper.getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + (blocks += (double)inPlayer.height), inPlayer.posZ));
    }

    public static String getBlockName(int block) {
        return new ItemStack(Block.getBlockById(block)).getDisplayName();
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return Wrapper.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static void windowClick(int windowID, int slotID, int mouseButton, ClickType type) {
        Wrapper.getPlayerController().windowClick(windowID, slotID, mouseButton, type, Wrapper.getPlayer());
    }

    public static void swap(int slot, int hotbarNum) {
        Wrapper.getPlayerController().windowClick(Wrapper.getPlayer().inventoryContainer.windowId, slot, hotbarNum, ClickType.SWAP, Wrapper.getPlayer());
    }

    public static void rapecpu() {
        Wrapper.mc().gameSettings.thirdPersonView = 1;
        for (Object o : Wrapper.getWorld().loadedEntityList) {
            if (!(o instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = (EntityLivingBase)o;
            entity.setArrowCountInEntity(999999999);
        }
    }

    public static void tellPlayer(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("\u00a78[" + ColorUtil.getColor(Weepcraft.primaryColor) + "W" + ColorUtil.getColor(Weepcraft.secondaryColor) + "C" + "\u00a78]\u00a7r: " + ColorUtil.getColor(Weepcraft.normalColor) + message));
    }

    public static void tellPlayerIRC(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("\u00a78[" + ColorUtil.getColor(Weepcraft.primaryColor) + "IR" + ColorUtil.getColor(Weepcraft.secondaryColor) + "C" + "\u00a78]\u00a7r: " + ColorUtil.getColor(Weepcraft.normalColor) + message));
    }

    public static Block getBlockAtPos(BlockPos inBlockPos) {
        BlockPos currentPos = inBlockPos;
        IBlockState s = Wrapper.getWorld().getBlockState(currentPos);
        return s.getBlock();
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
    }

    public static Block getBlock(int x, int y, int z) {
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static void sendPacket(Packet packet) {
        Wrapper.getPlayer().connection.sendPacket(packet);
    }

    public static void drawBorderRect(float left, float top, float right, float bottom, int bcolor, int icolor, float f) {
        Gui.drawRect(left + f, top + f, right - f, bottom - f, icolor);
        Gui.drawRect(left, top, left + f, bottom, bcolor);
        Gui.drawRect(left + f, top, right, top + f, bcolor);
        Gui.drawRect(left + f, bottom - f, right, bottom, bcolor);
        Gui.drawRect(right - f, top + f, right, bottom - f, bcolor);
    }
}

