package modification.modules.world;

import modification.enummerates.Category;
import modification.events.EventRender2D;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.extenders.Rotation;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.ColorUtil;
import modification.utilities.RotationUtil;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MovingObjectPosition;

import java.awt.*;

public final class Tower
        extends Module {
    private final Value<Boolean> silent = new Value("Silent", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> showBlocks = new Value("Show blocks", Boolean.valueOf(true), this, new String[0]);
    private final Value<String> mode = new Value("Mode", "Legit", new String[]{"Legit", "Motion"}, this, new String[0]);
    private int slot;

    public Tower(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.slot = MC.thePlayer.inventory.currentItem;
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            this.tag = (((Boolean) this.silent.value).booleanValue() ? "Silent" : "");
            int i = findBlock(MC.thePlayer.inventoryContainer);
            if (shouldPlace(i)) {
                if ((i != -1) && (this.slot != i)) {
                    MC.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.slot = i));
                }
                RotationUtil.currentRotation = new Rotation(RotationUtil.lastRotation.yaw, 88.0F);
                Object localObject = (String) this.mode.value;
                int k = -1;
                switch (((String) localObject).hashCode()) {
                    case 73298841:
                        if (((String) localObject).equals("Legit")) {
                            k = 0;
                        }
                        break;
                    case -1984451626:
                        if (((String) localObject).equals("Motion")) {
                            k = 1;
                        }
                        break;
                }
                switch (k) {
                    case 0:
                        if (MC.thePlayer.onGround) {
                            MC.thePlayer.jump();
                        }
                        break;
                    case 1:
                        MC.thePlayer.motionY = 0.4D;
                }
                localObject = ((Boolean) this.silent.value).booleanValue() ? MC.thePlayer.inventoryContainer.getSlot(this.slot | 0x24).getStack() : MC.thePlayer.getCurrentEquippedItem();
                MovingObjectPosition localMovingObjectPosition = Modification.RAY_TRACE_UTIL.rayTraceBlock(RotationUtil.currentRotation.yaw, RotationUtil.currentRotation.pitch);
                if ((localMovingObjectPosition != null) && (!MC.theWorld.isAirBlock(localMovingObjectPosition.getBlockPos())) && (MC.playerController.onPlayerRightClick(MC.thePlayer, MC.theWorld, (ItemStack) localObject, localMovingObjectPosition.getBlockPos(), localMovingObjectPosition.sideHit, localMovingObjectPosition.hitVec))) {
                    MC.thePlayer.swingItem();
                }
            }
        }
        if (((paramEvent instanceof EventRender2D)) && (((Boolean) this.showBlocks.value).booleanValue())) {
            EventRender2D localEventRender2D = (EventRender2D) paramEvent;
            int j = findBlockValue(MC.thePlayer.inventoryContainer);
            if (j > 0) {
                int m = -2 | 0x4;
                int n = -2 | 0x4;
                Modification.RENDER_UTIL.drawBorderedRect(m, n, MC.fontRendererObj.getStringWidth(Integer.toString(j).concat(" Blocks ")), MC.fontRendererObj.FONT_HEIGHT, 1, ColorUtil.BACKGROUND_DARKER, Color.BLACK.getRGB());
                MC.fontRendererObj.drawStringWithShadow(Integer.toString(j).concat(" Blocks "), m, n | 0x1, -1);
            }
        }
    }

    protected void onDeactivated() {
        if ((((Boolean) this.silent.value).booleanValue()) && (this.slot != MC.thePlayer.inventory.currentItem)) {
            MC.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(MC.thePlayer.inventory.currentItem));
        }
        RotationUtil.currentRotation = null;
    }

    private boolean shouldPlace(int paramInt) {
        ItemStack localItemStack = MC.thePlayer.getCurrentEquippedItem();
        if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
            return true;
        }
        return (((Boolean) this.silent.value).booleanValue()) && (paramInt != -1);
    }

    private int findBlock(Container paramContainer) {
        for (int i = 0; i < 9; i++) {
            ItemStack localItemStack = paramContainer.getSlot(i | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
                return i;
            }
        }
        return -1;
    }

    private int findBlockValue(Container paramContainer) {
        int i = 0;
        for (int j = 0; j < 9; j++) {
            ItemStack localItemStack = paramContainer.getSlot(j | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
                i |= localItemStack.stackSize;
            }
        }
        return i;
    }
}




