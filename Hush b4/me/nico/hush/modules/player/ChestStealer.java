// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class ChestStealer extends Module
{
    public TimeHelper instantdelay;
    public TimeHelper stealdelay;
    public TimeHelper testdelay;
    public static String mode;
    
    static {
        ChestStealer.mode = "Normal";
    }
    
    public ChestStealer() {
        super("ChestStealer", "ChestStealer", 14620696, 48, Category.PLAYER);
        this.instantdelay = new TimeHelper();
        this.stealdelay = new TimeHelper();
        this.testdelay = new TimeHelper();
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Instant");
        mode.add("Normal");
        mode.add("Slow");
        Client.instance.settingManager.rSetting(new Setting("Mode", "ChestStealerMode", this, "Normal", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setName("ChestStealer");
        if (!this.isEnabled()) {
            return;
        }
        if (Client.instance.settingManager.getSettingByName("ChestStealerMode").getValString().equalsIgnoreCase("Slow")) {
            this.setDisplayname("ChestStealer");
            this.Slow();
        }
        else if (Client.instance.settingManager.getSettingByName("ChestStealerMode").getValString().equalsIgnoreCase("Normal")) {
            this.setDisplayname("ChestStealer");
            this.Normal();
        }
        else if (Client.instance.settingManager.getSettingByName("ChestStealerMode").getValString().equalsIgnoreCase("Instant")) {
            this.setDisplayname("ChestStealer");
            this.Instant();
        }
    }
    
    private void Instant() {
        final Minecraft mc = ChestStealer.mc;
        if (Minecraft.thePlayer.openContainer != null) {
            final Minecraft mc2 = ChestStealer.mc;
            if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                final Minecraft mc3 = ChestStealer.mc;
                final ContainerChest chest = (ContainerChest)Minecraft.thePlayer.openContainer;
                if (!this.isChestEmpty(chest)) {
                    for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                        if (chest.getLowerChestInventory().getStackInSlot(i) != null && TimeHelper.hasReached(0L)) {
                            final PlayerControllerMP playerController = ChestStealer.mc.playerController;
                            final int windowId = chest.windowId;
                            final int slotId = i;
                            final int mouseButtonClicked = 0;
                            final int mode = 1;
                            final Minecraft mc4 = ChestStealer.mc;
                            playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                            TimeHelper.reset();
                        }
                    }
                }
                else {
                    final Minecraft mc5 = ChestStealer.mc;
                    Minecraft.thePlayer.closeScreen();
                }
            }
        }
    }
    
    private void Slow() {
        final Minecraft mc = ChestStealer.mc;
        if (Minecraft.thePlayer.openContainer != null) {
            final Minecraft mc2 = ChestStealer.mc;
            if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                final Minecraft mc3 = ChestStealer.mc;
                final ContainerChest chest = (ContainerChest)Minecraft.thePlayer.openContainer;
                if (!this.isChestEmpty(chest)) {
                    for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                        if (chest.getLowerChestInventory().getStackInSlot(i) != null && TimeHelper.hasReached(150L)) {
                            final PlayerControllerMP playerController = ChestStealer.mc.playerController;
                            final int windowId = chest.windowId;
                            final int slotId = i;
                            final int mouseButtonClicked = 0;
                            final int mode = 1;
                            final Minecraft mc4 = ChestStealer.mc;
                            playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                            TimeHelper.reset();
                        }
                    }
                }
                else {
                    final Minecraft mc5 = ChestStealer.mc;
                    Minecraft.thePlayer.closeScreen();
                }
            }
        }
    }
    
    public void Normal() {
        if (!this.isEnabled()) {
            return;
        }
        final Minecraft mc = ChestStealer.mc;
        if (Minecraft.thePlayer.openContainer != null) {
            final Minecraft mc2 = ChestStealer.mc;
            if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                final Minecraft mc3 = ChestStealer.mc;
                final ContainerChest chest = (ContainerChest)Minecraft.thePlayer.openContainer;
                if (!this.isChestEmpty(chest)) {
                    for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                        if (chest.getLowerChestInventory().getStackInSlot(i) != null && TimeHelper.hasReached(50L)) {
                            final PlayerControllerMP playerController = ChestStealer.mc.playerController;
                            final int windowId = chest.windowId;
                            final int slotId = i;
                            final int mouseButtonClicked = 0;
                            final int mode = 1;
                            final Minecraft mc4 = ChestStealer.mc;
                            playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                            TimeHelper.reset();
                        }
                    }
                }
                else {
                    final Minecraft mc5 = ChestStealer.mc;
                    Minecraft.thePlayer.closeScreen();
                }
            }
        }
    }
    
    public boolean isChestEmpty(final ContainerChest chest) {
        for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
            if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                return false;
            }
        }
        return true;
    }
    
    public void Test() {
        final Minecraft mc = ChestStealer.mc;
        if (Minecraft.thePlayer.openContainer != null) {
            final Minecraft mc2 = ChestStealer.mc;
            if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                final Minecraft mc3 = ChestStealer.mc;
                final ContainerChest chest = (ContainerChest)Minecraft.thePlayer.openContainer;
                if (!this.isChestEmpty2(chest)) {
                    for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                        if (chest.getLowerChestInventory().getStackInSlot(i) != null && TimeHelper.hasReached(75L)) {
                            final PlayerControllerMP playerController = ChestStealer.mc.playerController;
                            final int windowId = chest.windowId;
                            final int slotId = i;
                            final int mouseButtonClicked = 0;
                            final int mode = 1;
                            final Minecraft mc4 = ChestStealer.mc;
                            playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                            TimeHelper.reset();
                        }
                    }
                }
                else {
                    final Minecraft mc5 = ChestStealer.mc;
                    Minecraft.thePlayer.closeScreen();
                }
            }
        }
    }
    
    public boolean isChestEmpty2(final ContainerChest chest) {
        for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
            if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
