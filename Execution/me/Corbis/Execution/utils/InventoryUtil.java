package me.Corbis.Execution.utils;


import net.minecraft.client.Minecraft;

public class InventoryUtil {
    static Minecraft mc = Minecraft.getMinecraft();
    public static void clickSlot(int slot, int mouseButton, boolean shiftClick) {
        mc.playerController.windowClick(mc.currentScreen == null ? 0 : mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, mc.thePlayer);
    }

    public static void clickSlot(int slot, boolean shiftClick) {
        mc.playerController.windowClick(mc.currentScreen == null ? 0 : mc.thePlayer.inventoryContainer.windowId, slot, 0, shiftClick ? 1 : 0, mc.thePlayer);
    }

    public static void doubleClickSlot(int slot) {
        mc.playerController.windowClick(mc.currentScreen == null ? 0 : mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, mc.thePlayer);
        mc.playerController.windowClick(mc.currentScreen == null ? 0 : mc.thePlayer.inventoryContainer.windowId, slot, 0, 6, mc.thePlayer);
        mc.playerController.windowClick(mc.currentScreen == null ? 0 : mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, mc.thePlayer);
    }
}
