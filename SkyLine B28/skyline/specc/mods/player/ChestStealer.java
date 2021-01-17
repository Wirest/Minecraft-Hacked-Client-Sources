package skyline.specc.mods.player;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.S30PacketWindowItems;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.utils.MathUtil;

public class ChestStealer extends Module {

    private S30PacketWindowItems packet;
    private boolean shouldEmptyChest;
    private float delay;
    private int currentSlot;
    private int[] whitelist;
    private int random;

    public ChestStealer() {
        super(new ModData("ChestSteal ", Keyboard.KEY_B, new Color(220, 156, 53)), ModType.OTHER);
        random = (int) MathUtil.getRandomInRange(80F, 120F);
        delay = random;
        whitelist = new int[]{54};
    }

    private int getNextSlot(Container container) {
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getInventory().get(i) != null) {
                return i;
            }
        }
        return -1;
    }

    public boolean isContainerEmpty(Container container) {
        boolean temp = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }

    @EventListener
    public void onUpdate(EventPlayerUpdate event) {
        if (event.getType() == EventType.PRE) {
            try {
                if (!mc.inGameHasFocus && packet != null 
                	&& mc.thePlayer.openContainer.windowId == packet.func_148911_c() 
                	&& mc.currentScreen instanceof GuiChest 
                	&& GuiChest.lowerChestInventory.getName().contains("Chest")) {
                    if (!isContainerEmpty(mc.thePlayer.openContainer) 
                    	&& !GuiChest.lowerChestInventory.getName().contains("§") 
                    	&& !GuiChest.lowerChestInventory.getName().contains("&") 
                    	&& !GuiChest.lowerChestInventory.getName().contains(" ")
                    	&& !GuiChest.lowerChestInventory.getName().contains("Game")
                    	&& !GuiChest.lowerChestInventory.getName().contains("Lobby")) {
                        int slotId = getNextSlot(mc.thePlayer.openContainer);
                        if (delay >= 2.25F) {
                            mc.playerController.windowClick(p.openContainer.windowId, slotId, 0, 1, mc.thePlayer);
                            delay = 0;
                        }
                        ++delay;
                    } else if (!GuiChest.lowerChestInventory.getName().contains("§") 
                    	&& !GuiChest.lowerChestInventory.getName().contains("&") 
                    	&& !GuiChest.lowerChestInventory.getName().contains(" ")) {
                      p.closeScreen();
                      packet = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @EventListener
    public void onPacket(EventPacket event) {
        if (event.getType() == EventPacket.EventPacketType.RECEIVE && event.getPacket() instanceof S30PacketWindowItems) {
            packet = (S30PacketWindowItems) event.getPacket();
        }
    }
}