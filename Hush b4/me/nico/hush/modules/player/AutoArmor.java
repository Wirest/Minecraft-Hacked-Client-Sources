// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.item.ItemStack;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import me.nico.hush.utils.ArmorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class AutoArmor extends Module
{
    private int[] chestplate;
    private int[] leggings;
    private int[] boots;
    private int[] helmet;
    private int delay;
    private boolean best;
    public static String mode;
    
    static {
        AutoArmor.mode = "OpenInv";
    }
    
    public AutoArmor() {
        super("AutoArmor", "AutoArmor", 7728438, 0, Category.PLAYER);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("OpenInv");
        mode.add("Bypass");
        Client.instance.settingManager.rSetting(new Setting("Mode", "AutoArmorMode", this, "OpenInv", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setName("AutoArmor");
        if (!this.isEnabled()) {
            return;
        }
        if (Client.instance.settingManager.getSettingByName("AutoArmorMode").getValString().equalsIgnoreCase("OpenInv")) {
            this.setDisplayname("AutoArmor");
            this.OpenInv();
        }
        else if (Client.instance.settingManager.getSettingByName("AutoArmorMode").getValString().equalsIgnoreCase("Bypass")) {
            this.setDisplayname("AutoArmor");
            this.Bypass();
        }
    }
    
    public void OpenInv() {
        if (!this.isEnabled()) {
            return;
        }
        if (AutoArmor.mc.currentScreen instanceof GuiInventory) {
            this.autoArmor();
            this.betterArmor();
        }
    }
    
    public void autoArmor() {
        if (this.best) {
            return;
        }
        int item = -1;
        ++this.delay;
        if (this.delay >= 10) {
            final Minecraft mc = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots;
                for (int length = (boots = this.boots).length, i = 0; i < length; ++i) {
                    final int id = boots[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            final Minecraft mc2 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[1] == null) {
                int[] leggings;
                for (int length = (leggings = this.leggings).length, i = 0; i < length; ++i) {
                    final int id = leggings[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            final Minecraft mc3 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[2] == null) {
                int[] chestplate;
                for (int length = (chestplate = this.chestplate).length, i = 0; i < length; ++i) {
                    final int id = chestplate[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            final Minecraft mc4 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[3] == null) {
                int[] helmet;
                for (int length = (helmet = this.helmet).length, i = 0; i < length; ++i) {
                    final int id = helmet[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (item != -1) {
                final PlayerControllerMP playerController = AutoArmor.mc.playerController;
                final int windowId = 0;
                final int slotId = item;
                final int mouseButtonClicked = 0;
                final int mode = 1;
                final Minecraft mc5 = AutoArmor.mc;
                playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                this.delay = 0;
            }
        }
    }
    
    public void betterArmor() {
        if (!this.best) {
            return;
        }
        ++this.delay;
        if (this.delay >= 10) {
            final Minecraft mc = AutoArmor.mc;
            if (Minecraft.thePlayer.openContainer != null) {
                final Minecraft mc2 = AutoArmor.mc;
                if (Minecraft.thePlayer.openContainer.windowId != 0) {
                    return;
                }
            }
            boolean switchArmor = false;
            int item = -1;
            final Minecraft mc3 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[0] == null) {
                int[] array;
                for (int j = (array = this.boots).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(0, this.boots)) {
                item = 8;
                switchArmor = true;
            }
            final Minecraft mc4 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[3] == null) {
                int[] array;
                for (int j = (array = this.helmet).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(3, this.helmet)) {
                item = 5;
                switchArmor = true;
            }
            final Minecraft mc5 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[1] == null) {
                int[] array;
                for (int j = (array = this.leggings).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(1, this.leggings)) {
                item = 7;
                switchArmor = true;
            }
            final Minecraft mc6 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[2] == null) {
                int[] array;
                for (int j = (array = this.chestplate).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(2, this.chestplate)) {
                item = 6;
                switchArmor = true;
            }
            boolean b = false;
            final Minecraft mc7 = AutoArmor.mc;
            ItemStack[] stackArray;
            for (int k = (stackArray = Minecraft.thePlayer.inventory.mainInventory).length, l = 0; l < k; ++l) {
                final ItemStack stack = stackArray[l];
                if (stack == null) {
                    b = true;
                    break;
                }
            }
            switchArmor = (switchArmor && !b);
            if (item != -1) {
                final PlayerControllerMP playerController = AutoArmor.mc.playerController;
                final int windowId = 0;
                final int slotId = item;
                final int mouseButtonClicked = 0;
                final int mode = switchArmor ? 4 : 1;
                final Minecraft mc8 = AutoArmor.mc;
                playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                this.delay = 0;
            }
        }
    }
    
    public void Bypass() {
        if (!AutoArmor.mc.gameSettings.keyBindForward.pressed && !AutoArmor.mc.gameSettings.keyBindBack.pressed && !AutoArmor.mc.gameSettings.keyBindLeft.pressed && !AutoArmor.mc.gameSettings.keyBindRight.pressed && !AutoArmor.mc.gameSettings.keyBindJump.pressed && !AutoArmor.mc.gameSettings.keyBindSneak.pressed) {
            this.autoArmor();
            this.betterArmor();
        }
    }
    
    public void autoArmor1() {
        if (this.best) {
            return;
        }
        int item = -1;
        ++this.delay;
        if (this.delay >= 10) {
            final Minecraft mc = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots;
                for (int length = (boots = this.boots).length, i = 0; i < length; ++i) {
                    final int id = boots[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            final Minecraft mc2 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[1] == null) {
                int[] leggings;
                for (int length = (leggings = this.leggings).length, i = 0; i < length; ++i) {
                    final int id = leggings[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            final Minecraft mc3 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[2] == null) {
                int[] chestplate;
                for (int length = (chestplate = this.chestplate).length, i = 0; i < length; ++i) {
                    final int id = chestplate[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            final Minecraft mc4 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[3] == null) {
                int[] helmet;
                for (int length = (helmet = this.helmet).length, i = 0; i < length; ++i) {
                    final int id = helmet[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (item != -1) {
                final PlayerControllerMP playerController = AutoArmor.mc.playerController;
                final int windowId = 0;
                final int slotId = item;
                final int mouseButtonClicked = 0;
                final int mode = 1;
                final Minecraft mc5 = AutoArmor.mc;
                playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                this.delay = 0;
            }
        }
    }
    
    public void betterArmor1() {
        if (!this.best) {
            return;
        }
        ++this.delay;
        if (this.delay >= 10) {
            final Minecraft mc = AutoArmor.mc;
            if (Minecraft.thePlayer.openContainer != null) {
                final Minecraft mc2 = AutoArmor.mc;
                if (Minecraft.thePlayer.openContainer.windowId != 0) {
                    return;
                }
            }
            boolean switchArmor = false;
            int item = -1;
            final Minecraft mc3 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[0] == null) {
                int[] array;
                for (int j = (array = this.boots).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(0, this.boots)) {
                item = 8;
                switchArmor = true;
            }
            final Minecraft mc4 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[3] == null) {
                int[] array;
                for (int j = (array = this.helmet).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(3, this.helmet)) {
                item = 5;
                switchArmor = true;
            }
            final Minecraft mc5 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[1] == null) {
                int[] array;
                for (int j = (array = this.leggings).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(1, this.leggings)) {
                item = 7;
                switchArmor = true;
            }
            final Minecraft mc6 = AutoArmor.mc;
            if (Minecraft.thePlayer.inventory.armorInventory[2] == null) {
                int[] array;
                for (int j = (array = this.chestplate).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(2, this.chestplate)) {
                item = 6;
                switchArmor = true;
            }
            boolean b = false;
            final Minecraft mc7 = AutoArmor.mc;
            ItemStack[] stackArray;
            for (int k = (stackArray = Minecraft.thePlayer.inventory.mainInventory).length, l = 0; l < k; ++l) {
                final ItemStack stack = stackArray[l];
                if (stack == null) {
                    b = true;
                    break;
                }
            }
            switchArmor = (switchArmor && !b);
            if (item != -1) {
                final PlayerControllerMP playerController = AutoArmor.mc.playerController;
                final int windowId = 0;
                final int slotId = item;
                final int mouseButtonClicked = 0;
                final int mode = switchArmor ? 4 : 1;
                final Minecraft mc8 = AutoArmor.mc;
                playerController.windowClick(windowId, slotId, mouseButtonClicked, mode, Minecraft.thePlayer);
                this.delay = 0;
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        this.chestplate = new int[] { 311, 307, 315, 303, 299 };
        this.leggings = new int[] { 312, 308, 316, 304, 300 };
        this.boots = new int[] { 313, 309, 317, 305, 301 };
        this.helmet = new int[] { 310, 306, 314, 302, 298 };
        this.delay = 0;
        this.best = true;
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
