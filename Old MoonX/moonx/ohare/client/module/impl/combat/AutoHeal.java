package moonx.ohare.client.module.impl.combat;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * made by oHare for HTB V4
 *
 * @since 5/1/2019
 **/
public class AutoHeal extends Module {
    private int slot = -1;
    private int currentslot = -1;
    public static boolean healing = false;
    private final TimerUtil timer = new TimerUtil();

    private final BooleanValue soups = new BooleanValue("Soups","Soup PvP", false);
    private final BooleanValue potions = new BooleanValue("Potions","Use potions", true);
    private final BooleanValue jumpPot = new BooleanValue("Jump","Auto Jump", false);
    private final BooleanValue regenpots = new BooleanValue("Regen Pots","Use Regen Pots", true);
    private final BooleanValue speedpots = new BooleanValue("Speed Pots","Use Speed Pots", true);
    private final NumberValue<Integer> health = new NumberValue<>("Health", 10, 1, 20, 1);
    private final NumberValue<Integer> delay = new NumberValue<>("Delay", 5, 5, 20, 1);
    public static boolean doSoup;

    public AutoHeal() {
        super("AutoHeal", Category.COMBAT, new Color(0, 255, 255, 255).getRGB());
        setDescription("Automatically throws potions for you");
        setRenderLabel("Auto Heal");
    }

    @Override
    public void onDisable() {
        currentslot = -1;
        healing = false;
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (!isBlockUnder() || !isCloseEnough()) return;
        int eatables = 0;
        if (potions.isEnabled())
        {
            eatables += getPotionCount();
            if (regenpots.isEnabled())
                eatables += getRegenCount();
            if (speedpots.isEnabled()) {
                eatables += getSpeedCount();
            }
        }
        StringBuilder suffix = new StringBuilder();
        if (potions.isEnabled()) suffix.append(eatables);
        if (potions.isEnabled() && soups.isEnabled()) suffix.append("-");
        if (soups.isEnabled()) suffix.append(getCount());
        setSuffix(suffix.toString().isEmpty() ? null:suffix.toString());
        if (soups.isEnabled()) {
            int soupSlot = this.getSoupSlot();
            if(event.isPre()) {
                if(shouldHeal() && soupSlot != -1) {
                    doSoup = true;
                }
            } else if(doSoup) {
                doSoup = false;
                getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, soupSlot, 5, 2, getMc().thePlayer);
                getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(5));
                getMc().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getMc().thePlayer.inventory.getCurrentItem()));
                getMc().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem));
            }
        }
        if (potions.isEnabled()) {
            if (eatables > 0) {
                if (event.isPre()) {
                    update();
                    if (slot == -1) {
                        healing = false;
                        return;
                    }
                    boolean up = jumpPot.getValue() && getMc().thePlayer.onGround;
                    if (getMc().thePlayer.inventory.mainInventory[slot] != null && getMc().thePlayer.inventory.mainInventory[slot].getItem() == Items.potionitem) {
                        if (up) getMc().thePlayer.jump();
                        event.setPitch(up ? -90 : 90);
                    }
                } else {
                    if (slot == -1) return;
                    if (!healing) return;
                    if (getMc().thePlayer.inventory.mainInventory[slot] == null) return;
                    int packetSlot = slot;
                    if (slot < 9) {
                        currentslot = getMc().thePlayer.inventory.currentItem;
                        highlight(slot);
                        getMc().getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(getMc().thePlayer.getHeldItem()));
                        highlight(getMc().thePlayer.inventory.currentItem);
                        highlight(currentslot);
                    } else {
                        getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(8));
                        getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, slot, 8, 2, getMc().thePlayer);
                        getMc().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getMc().thePlayer.inventory.mainInventory[slot]));
                        getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, packetSlot, 8, 2, getMc().thePlayer);
                        getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem));
                    }
                    timer.reset();
                    healing = false;
                }
            } else {
                healing = false;
            }
        }
    }

    private void update() {
        for (int i = 0; i < 36; ++i) {
            if (getMc().thePlayer != null && getMc().theWorld != null && getMc().thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = getMc().thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();
                if (item instanceof ItemPotion && potions.isEnabled()) {
                    final ItemPotion potion = (ItemPotion) item;
                    if (potion.getEffects(is) != null) {
                        for (final PotionEffect o : potion.getEffects(is)) {
                            final PotionEffect effect = o;
                            if (effect.getPotionID() == Potion.heal.id && shouldHeal() || timer.reach(delay.getValue() * 50) && effect.getPotionID() == Potion.moveSpeed.id && speedpots.isEnabled() && !getMc().thePlayer.isPotionActive(Potion.moveSpeed) || shouldHeal() && (effect.getPotionID() == Potion.regeneration.id && regenpots.isEnabled() && !getMc().thePlayer.isPotionActive(Potion.regeneration)) && ItemPotion.isSplash(is.getItemDamage())) {
                                slot = i;
                                healing = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private int getPotionCount() {
        int count = 0;
        for (Slot s : getMc().thePlayer.inventoryContainer.inventorySlots)
            if (s.getHasStack()) {

                ItemStack is = s.getStack();
                if ((is.getItem() instanceof ItemPotion)) {

                    ItemPotion ip = (ItemPotion) is.getItem();
                    if (ItemPotion.isSplash(is.getMetadata())) {

                        boolean hasHealing = false;
                        for (PotionEffect pe : ip.getEffects(is))
                            if (pe.getPotionID() == Potion.heal.getId()) {

                                hasHealing = true;
                                break;
                            }
                        if (hasHealing) {
                            count++;
                        }
                    }
                }
            }
        return count;
    }

    private int getSoupSlot() {
        int itemSlot = -1;

        for(int i = 9; i < 45; ++i) {
            if(getMc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if(item instanceof ItemSoup) {
                    itemSlot = i;
                }
            }
        }

        return itemSlot;
    }
    private int getRegenCount() {
        int count = 0;
        for (Slot s : getMc().thePlayer.inventoryContainer.inventorySlots)
            if (s.getHasStack()) {

                ItemStack is = s.getStack();
                if ((is.getItem() instanceof ItemPotion)) {

                    ItemPotion ip = (ItemPotion) is.getItem();
                    if (ItemPotion.isSplash(is.getMetadata())) {

                        boolean hasHealing = false;
                        for (PotionEffect pe : ip.getEffects(is))
                            if (pe.getPotionID() == Potion.regeneration.getId()) {

                                hasHealing = true;
                                break;
                            }
                        if (hasHealing) {
                            count++;
                        }
                    }
                }
            }
        return count;
    }

    private int getSpeedCount() {
        int count = 0;
        for (Slot s : getMc().thePlayer.inventoryContainer.inventorySlots)
            if (s.getHasStack()) {

                ItemStack is = s.getStack();
                if ((is.getItem() instanceof ItemPotion)) {

                    ItemPotion ip = (ItemPotion) is.getItem();
                    if (ItemPotion.isSplash(is.getMetadata())) {

                        boolean hasSpeed = false;
                        for (PotionEffect pe : ip.getEffects(is))
                            if (pe.getPotionID() == Potion.moveSpeed.getId()) {
                                hasSpeed = true;
                                break;
                            }
                        if (hasSpeed) {
                            count++;
                        }
                    }
                }
            }
        return count;
    }

    private boolean isBlockUnder() {
        for (int i = (int) (getMc().thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(getMc().thePlayer.posX, i, getMc().thePlayer.posZ);
            if (getMc().theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
    private int getCount() {
        int counter = 0;

        for(int i = 9; i < 45; ++i) {
            if(getMc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if(item instanceof ItemSoup) {
                    ++counter;
                }
            }
        }

        return counter;
    }
    private void highlight(int slot) {
        getMc().thePlayer.inventory.currentItem = slot;
        getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
    }

    private boolean isCloseEnough() {
        for (double i = getMc().thePlayer.posY; i > 0;i-= 0.001) {
            if (getMc().theWorld.getBlockState(new BlockPos(getMc().thePlayer.posX,i,getMc().thePlayer.posZ)).getBlock() instanceof BlockAir) continue;
            return getMc().thePlayer.posY - i <= 1.5;
        }
        return false;
    }

    private boolean shouldHeal() {
        return getMc().thePlayer.getHealth() <= health.getValue() && timer.reach(delay.getValue() * 50);
    }
}
