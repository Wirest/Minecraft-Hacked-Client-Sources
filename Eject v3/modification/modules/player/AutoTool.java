package modification.modules.player;

import modification.enummerates.Category;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.interfaces.Event;
import modification.main.Modification;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public final class AutoTool
        extends Module {
    public int slot;
    private boolean changedItem;

    public AutoTool(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.slot = MC.thePlayer.inventory.currentItem;
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            if (MC.gameSettings.keyBindAttack.isKeyDown()) {
                if ((MC.objectMouseOver != null) && (MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)) {
                    BlockPos localBlockPos = MC.objectMouseOver.getBlockPos();
                    if (!MC.theWorld.isAirBlock(localBlockPos)) {
                        Block localBlock = MC.theWorld.getBlockState(localBlockPos).getBlock();
                        int i = Modification.ITEM_UTIL.findHotbarItem(MC.thePlayer.inventoryContainer, localBlock);
                        if ((i != -1) && (MC.thePlayer.inventory.currentItem != i)) {
                            this.slot = MC.thePlayer.inventory.currentItem;
                            MC.thePlayer.inventory.currentItem = i;
                            this.changedItem = true;
                        }
                    }
                }
                return;
            }
            if (this.changedItem) {
                onDeactivated();
                this.changedItem = false;
            }
        }
    }

    protected void onDeactivated() {
        MC.thePlayer.inventory.currentItem = this.slot;
    }
}




