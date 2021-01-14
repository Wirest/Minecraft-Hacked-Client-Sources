package modification.modules.player;

import modification.enummerates.Category;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.TimeUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;

import java.util.concurrent.ThreadLocalRandom;

public final class InventoryManager
        extends Module {
    private final Value<Boolean> wait = new Value("Wait", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> autoArmor = new Value("Auto armor", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> cleanItems = new Value("Clean items", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> sortItems = new Value("Sort items", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> fakeInv = new Value("Fake inventory", Boolean.valueOf(false), this, new String[0]);
    private final Value<Boolean> intave = new Value("Intave", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> stand = new Value("Stand still", Boolean.valueOf(true), this, new String[]{"Fake inventory"});
    private final Value<Float> delay = new Value("Delay", Float.valueOf(150.0F), 0.0F, 300.0F, 0, this, new String[0]);
    private final Value<Float> waitDelay = new Value("Wait delay", Float.valueOf(50.0F), 20.0F, 120.0F, 0, this, new String[]{"Wait"});
    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil waitTimer = new TimeUtil();
    private boolean opened;

    public InventoryManager(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            this.tag = (((Boolean) this.fakeInv.value).booleanValue() ? "Fake inventory" : "Open inventory");
            Container localContainer = MC.thePlayer.inventoryContainer;
            int i = ((((Boolean) this.fakeInv.value).booleanValue()) && (MC.thePlayer.openContainer == localContainer)) || ((MC.currentScreen instanceof GuiInventory)) ? 1 : 0;
            if (i != 0) {
                if ((!((Boolean) this.wait.value).booleanValue()) || (this.waitTimer.reached(((Float) this.waitDelay.value).floatValue()))) {
                    Object localObject;
                    int m;
                    if (((Boolean) this.sortItems.value).booleanValue()) {
                        int j = Modification.ITEM_UTIL.bestSwordSlot(localContainer, 36, false);
                        if (j != 36) {
                            click(localContainer, j, 0, 2);
                            return;
                        }
                        localObject = localContainer.getSlot(36).getStack();
                        if ((localObject == null) || (!(((ItemStack) localObject).getItem() instanceof ItemSword))) {
                            m = Modification.ITEM_UTIL.bestToolSlot(localContainer, 36);
                            if (m != 36) {
                                click(localContainer, m, 0, 2);
                                return;
                            }
                        }
                    }
                    if (((Boolean) this.autoArmor.value).booleanValue()) {
                        int[] arrayOfInt = {1, 2, 0, 3};
                        for (int i1 : arrayOfInt) {
                            int i2 = i1 | 0x5;
                            ItemStack localItemStack = localContainer.getSlot(i2).getStack();
                            int i3 = Modification.ITEM_UTIL.bestArmorSlot(localContainer, Modification.ITEM_UTIL.bestArmorSlot(localContainer, i2, i1, false), i1, false);
                            if (i3 != i2) {
                                click(localContainer, localItemStack == null ? i3 : i2, 0, localItemStack == null ? 1 : 4);
                            }
                        }
                    }
                    int k;
                    if (((Boolean) this.cleanItems.value).booleanValue()) {
                        k = findItemToThrow(localContainer);
                        if (k != -1) {
                            click(localContainer, k, 0, 4);
                        }
                    }
                    if (((Boolean) this.sortItems.value).booleanValue()) {
                        for (k = 9; k < localContainer.getInventory().size(); k++) {
                            localObject = localContainer.getSlot(k).getStack();
                            if (localObject != null) {
                                Item[] arrayOfItem = {Items.fishing_rod, Items.bow, Items.golden_apple, Items.porkchop, Items.cooked_beef, Items.bread, Items.arrow};
                                for (??? =0; ??? <arrayOfItem.length; ???++){
                                    if ((((ItemStack) localObject).getItem() == arrayOfItem[ ? ??]) &&
                                    (!isItemInRightSlot((ItemStack) localObject, ? ?? |0x1, localContainer))){
                                        click(localContainer, k, ??? |0x1, 2);
                                    }
                                }
                            }
                        }
                    }
                }
                return;
            }
            this.waitTimer.reset();
        }
    }

    private boolean isItemInRightSlot(ItemStack paramItemStack, int paramInt, Container paramContainer) {
        return (paramContainer.getSlot(0x24 | paramInt).getStack() != null) && (paramContainer.getSlot(0x24 | paramInt).getStack().getItem() == paramItemStack.getItem());
    }

    private int findItemToThrow(Container paramContainer) {
        for (int i = 0; i < paramContainer.getInventory().size(); i++) {
            ItemStack localItemStack = paramContainer.getSlot(i).getStack();
            if (localItemStack != null) {
                if ((localItemStack.getItem() instanceof ItemSword)) {
                    if (i != Modification.ITEM_UTIL.bestSwordSlot(paramContainer, i, true)) {
                        return i;
                    }
                } else if ((localItemStack.getItem() instanceof ItemArmor)) {
                    if (i != Modification.ITEM_UTIL.bestArmorSlot(paramContainer, i, ((ItemArmor) localItemStack.getItem()).armorType, true)) {
                        return i;
                    }
                } else if ((localItemStack.getItem() instanceof ItemTool)) {
                    if (i != Modification.ITEM_UTIL.bestSameToolSlot(paramContainer, i)) {
                        return i;
                    }
                } else if (i != Modification.ITEM_UTIL.sameItem(paramContainer, i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void click(Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
        if (this.timer.reached(((Float) this.delay.value).floatValue() + ThreadLocalRandom.current().nextDouble(50.0D, 100.0D))) {
            if ((!((Boolean) this.fakeInv.value).booleanValue()) || (!((Boolean) this.stand.value).booleanValue()) || ((MC.thePlayer.moveForward == 0.0F) && (MC.thePlayer.moveStrafing == 0.0F))) {
                if (((Boolean) this.fakeInv.value).booleanValue()) {
                    MC.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                }
                if (((Boolean) this.intave.value).booleanValue()) {
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                } else {
                    MC.playerController.windowClick(paramContainer.windowId, paramInt1, paramInt2, paramInt3, MC.thePlayer);
                }
                if (((Boolean) this.fakeInv.value).booleanValue()) {
                    MC.getNetHandler().addToSendQueue(new C0DPacketCloseWindow(paramContainer.windowId));
                }
            }
            this.timer.reset();
        }
    }

    protected void onDeactivated() {
    }
}




