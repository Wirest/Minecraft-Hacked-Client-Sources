package cn.kody.debug.mod.mods.COMBAT;

import cn.kody.debug.events.EventPostMotion;
import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.time.TimerUtil;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Mouse;

public class AutoHeal
extends Mod {
    private int slot = -1;
    private int currentslot = -1;
    public static boolean healing = false;
    public static boolean doSoup;
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil timer3 = new TimerUtil();
    private boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff;
    public static boolean potting;
    private final TimerUtil timer2 = new TimerUtil();
    private Value<Boolean> potions = new Value<Boolean>("AutoHeal_Potions", true);
    private Value<Boolean> soup = new Value<Boolean>("AutoHeal_Soup", false);
    private Value<Boolean> jumpPotions = new Value<Boolean>("AutoHeal_JumpPotions", false);
    private Value<Boolean> rangepots = new Value<Boolean>("AutoHeal_RegenPotions", false);
    private Value<Boolean> speedpots = new Value<Boolean>("AutoHeal_SpeedPotions", true);
    private Value<Boolean> eatApples = new Value<Boolean>("AutoHeal_Apples", true);
    public Value<Boolean> eatHeads = new Value<Boolean>("AutoHeal_Heads", true);
    private Value<Double> health = new Value<Double>("AutoHeal_Health", 10.0, 1.0, 20.0, 1.0);
    private Value<Double> delay = new Value<Double>("AutoHeal_Delay", 5.0, 5.0, 20.0, 1.0);

    public AutoHeal() {
        super("AutoHeal", "Auto Heal", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        doingStuff = false;
        this.eatingApple = false;
        this.switched = -1;
        this.timer2.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.currentslot = -1;
        doSoup = false;
        healing = false;
        doingStuff = false;
        if (this.eatingApple) {
            this.repairItemPress();
            this.repairItemSwitch();
        }
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.soup.getValueState().booleanValue()) {
            int soupSlot = this.getSoupSlot();
            if (this.timer.sleep(this.delay.getValueState().intValue()) && (double)Minecraft.thePlayer.getHealth() < this.health.getValueState() && soupSlot != -1) {
                doSoup = true;
            }
        }
        int eatables = 0;
        if (this.potions.getValueState().booleanValue()) {
            eatables += this.getPotionCount();
            if (this.rangepots.getValueState().booleanValue()) {
                eatables += this.getRegenCount();
            }
        }
        if (eatables > 0) {
            boolean up;
            this.update();
            if (!this.shouldHeal()) {
                healing = false;
                return;
            }
            if (this.slot == -1) {
                healing = false;
                return;
            }
            boolean bl = up = this.jumpPotions.getValueState() != false && Minecraft.thePlayer.onGround;
            if (Minecraft.thePlayer.inventory.mainInventory[this.slot] != null && Minecraft.thePlayer.inventory.mainInventory[this.slot].getItem() == Items.potionitem) {
                if (up) {
                    Minecraft.thePlayer.jump();
                }
                event.setPitch(up ? -90.0f : 90.0f);
            }
            healing = true;
        } else {
            healing = false;
        }
        if (this.eatApples.getValueState().booleanValue() || this.eatHeads.getValueState().booleanValue()) {
            if (Minecraft.thePlayer == null) {
                return;
            }
            InventoryPlayer inventory = Minecraft.thePlayer.inventory;
            if (inventory == null) {
                return;
            }
            doingStuff = false;
            if (!Mouse.isButtonDown((int)0) && !Mouse.isButtonDown((int)1)) {
                KeyBinding useItem = this.mc.gameSettings.keyBindUseItem;
                if (!this.timer2.reach(this.delay.getValueState().intValue() * 50)) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                    return;
                }
                if (Minecraft.thePlayer.capabilities.isCreativeMode || Minecraft.thePlayer.isPotionActive(Potion.regeneration) || (double)Minecraft.thePlayer.getHealth() >= this.health.getValueState()) {
                    this.timer.reset();
                    if (this.eatingApple) {
                        this.eatingApple = false;
                        this.repairItemPress();
                        this.repairItemSwitch();
                    }
                    return;
                }
                for (int i = 0; i < 2; ++i) {
                    boolean doEatHeads;
                    int slot;
                    boolean bl = doEatHeads = i != 0;
                    if (doEatHeads) {
                        if (!this.eatHeads.getValueState().booleanValue()) {
                            continue;
                        }
                    } else if (!this.eatApples.getValueState().booleanValue()) {
                        this.eatingApple = false;
                        this.repairItemPress();
                        this.repairItemSwitch();
                        continue;
                    }
                    if ((slot = doEatHeads ? this.getItemFromHotbar(397) : this.getItemFromHotbar(322)) == -1) continue;
                    int tempSlot = inventory.currentItem;
                    doingStuff = true;
                    if (doEatHeads) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                        this.timer.reset();
                        continue;
                    }
                    inventory.currentItem = slot;
                    useItem.pressed = true;
                    if (this.eatingApple) continue;
                    this.eatingApple = true;
                    this.switched = tempSlot;
                }
            }
        }
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
        int[] pots = new int[]{-1, -1, -1};
        if (this.speedpots.getValueState().booleanValue()) {
            pots[1] = 1;
        }
        if (this.timer.reach(200L) && potting) {
            potting = false;
        }
        int spoofSlot = this.getBestSpoofSlot();
        if (this.speedpots.getValueState().booleanValue()) {
            for (int i = 0; i < pots.length; ++i) {
                if (pots[i] == -1 || pots[i] != 1 || !this.timer3.reach(1000L) || Minecraft.thePlayer.isPotionActive(pots[i])) continue;
                this.getBestPot(spoofSlot, pots[i]);
            }
        }
        if (this.soup.getValueState().booleanValue()) {
            int soupSlot = this.getSoupSlot();
            if (doSoup) {
                doSoup = false;
                this.swap(soupSlot, 5);
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(5));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
            }
        }
        int eatables = 0;
        if (this.potions.getValueState().booleanValue()) {
            eatables += this.getPotionCount();
            if (this.rangepots.getValueState().booleanValue()) {
                eatables += this.getRegenCount();
            }
        }
        if (eatables > 0) {
            if (this.slot == -1) {
                return;
            }
            if (!healing) {
                return;
            }
            if (!this.shouldHeal()) {
                return;
            }
            if (Minecraft.thePlayer.inventory.mainInventory[this.slot] == null) {
                return;
            }
            int packetSlot = this.slot;
            if (this.slot < 9) {
                this.currentslot = Minecraft.thePlayer.inventory.currentItem;
                this.highlight(this.slot);
                this.mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.getHeldItem()));
                this.highlight(Minecraft.thePlayer.inventory.currentItem);
                this.highlight(this.currentslot);
            } else {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(8));
                Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, this.slot, 8, 2, Minecraft.thePlayer);
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.mainInventory[this.slot]));
                Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, packetSlot, 8, 2, Minecraft.thePlayer);
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
            }
            this.timer.reset();
        }
    }

    int getBestSpoofSlot() {
        int spoofSlot = 5;
        for (int i = 36; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            }
            if (!(Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion)) continue;
            spoofSlot = i - 36;
            break;
        }
        return spoofSlot;
    }

    void getBestPot(int hotbarSlot, int potID) {
        for (int i = 9; i < 45; ++i) {
            boolean up;
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiInventory) || !((is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemPotion)) continue;
            ItemPotion pot = (ItemPotion)is.getItem();
            if (pot.getEffects(is).isEmpty()) {
                return;
            }
            PotionEffect effect = pot.getEffects(is).get(0);
            int potionID = effect.getPotionID();
            if (potionID != potID || !ItemPotion.isSplash(is.getItemDamage()) || !this.isBestPot(pot, is)) continue;
            if (36 + hotbarSlot != i) {
                this.swap(i, hotbarSlot);
            }
            this.timer3.reset();
            boolean canpot = true;
            int oldSlot = Minecraft.thePlayer.inventory.currentItem;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(hotbarSlot));
            boolean bl = up = this.jumpPotions.getValueState() != false && Minecraft.thePlayer.onGround;
            if (up) {
                Minecraft.thePlayer.jump();
            }
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft.thePlayer.rotationYaw, up ? -90.0f : 90.0f, Minecraft.thePlayer.onGround));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
            potting = true;
            break;
        }
    }

    boolean isBestPot(ItemPotion potion, ItemStack stack) {
        if (potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1) {
            return false;
        }
        PotionEffect effect = potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            ItemPotion pot;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemPotion) || (pot = (ItemPotion)is.getItem()).getEffects(is) == null) continue;
            Iterator<PotionEffect> iterator = pot.getEffects(is).iterator();
            while (iterator.hasNext()) {
                PotionEffect o;
                PotionEffect effects = o = iterator.next();
                int id = effects.getPotionID();
                int ampl = effects.getAmplifier();
                int dur = effects.getDuration();
                if (id != potionID || !ItemPotion.isSplash(is.getItemDamage())) continue;
                if (ampl > amplifier) {
                    return false;
                }
                if (ampl != amplifier || dur <= duration) continue;
                return false;
            }
        }
        return true;
    }

    private void update() {
        for (int i = 0; i < 36; ++i) {
            ItemPotion potion;
            Item item;
            ItemStack is;
            if (Minecraft.thePlayer == null || this.mc.theWorld == null || Minecraft.thePlayer.inventory.mainInventory[i] == null || !((item = (is = Minecraft.thePlayer.inventory.mainInventory[i]).getItem()) instanceof ItemPotion) || !this.potions.getValueState().booleanValue() || (potion = (ItemPotion)item).getEffects(is) == null) continue;
            for (PotionEffect o : potion.getEffects(is)) {
                PotionEffect effect = o;
                if (effect.getPotionID() != Potion.heal.id && (effect.getPotionID() != Potion.regeneration.id || !this.rangepots.getValueState().booleanValue() || Minecraft.thePlayer.isPotionActive(Potion.regeneration) || !ItemPotion.isSplash(is.getItemDamage()))) continue;
                this.slot = i;
            }
        }
    }

    private void swap(int slot, int hotbarSlot) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarSlot, 2, Minecraft.thePlayer);
    }

    private int getCount() {
        int counter = 0;
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemSoup)) continue;
            ++counter;
        }
        return counter;
    }

    private int getSoupSlot() {
        int itemSlot = -1;
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemSoup)) continue;
            itemSlot = i;
        }
        return itemSlot;
    }

    private int getPotionCount() {
        int count = 0;
        for (Slot s : Minecraft.thePlayer.inventoryContainer.inventorySlots) {
            ItemStack is;
            if (!s.getHasStack() || !((is = s.getStack()).getItem() instanceof ItemPotion)) continue;
            ItemPotion ip = (ItemPotion)is.getItem();
            if (!ItemPotion.isSplash(is.getMetadata())) continue;
            boolean hasHealing = false;
            for (PotionEffect pe : ip.getEffects(is)) {
                if (pe.getPotionID() != Potion.heal.getId()) continue;
                hasHealing = true;
                break;
            }
            if (!hasHealing) continue;
            ++count;
        }
        return count;
    }

    private int getRegenCount() {
        int count = 0;
        for (Slot s : Minecraft.thePlayer.inventoryContainer.inventorySlots) {
            ItemStack is;
            if (!s.getHasStack() || !((is = s.getStack()).getItem() instanceof ItemPotion)) continue;
            ItemPotion ip = (ItemPotion)is.getItem();
            if (!ItemPotion.isSplash(is.getMetadata())) continue;
            boolean hasHealing = false;
            for (PotionEffect pe : ip.getEffects(is)) {
                if (pe.getPotionID() != Potion.regeneration.getId()) continue;
                hasHealing = true;
                break;
            }
            if (!hasHealing) continue;
            ++count;
        }
        return count;
    }

    private void highlight(int slot) {
        Minecraft.thePlayer.inventory.currentItem = slot;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
    }

    private boolean shouldHeal() {
        return (double)Minecraft.thePlayer.getHealth() <= this.health.getValueState() && this.timer.reach(this.delay.getValueState().intValue() * 50);
    }

    private void repairItemPress() {
        KeyBinding keyBindUseItem;
        if (this.mc.gameSettings != null && (keyBindUseItem = this.mc.gameSettings.keyBindUseItem) != null) {
            keyBindUseItem.pressed = false;
        }
    }

    private void repairItemSwitch() {
        EntityPlayerSP p = Minecraft.thePlayer;
        if (p == null) {
            return;
        }
        InventoryPlayer inventory = p.inventory;
        if (inventory == null) {
            return;
        }
        int switched = this.switched;
        if (switched == -1) {
            return;
        }
        inventory.currentItem = switched;
        this.switched = switched = -1;
    }

    private int getItemFromHotbar(int id) {
        for (int i = 0; i < 9; ++i) {
            ItemStack is;
            Item item;
            if (Minecraft.thePlayer.inventory.mainInventory[i] == null || Item.getIdFromItem(item = (is = Minecraft.thePlayer.inventory.mainInventory[i]).getItem()) != id) continue;
            return i;
        }
        return -1;
    }

    static {
        doingStuff = false;
    }
}

