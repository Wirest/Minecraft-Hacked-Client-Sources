package modification.modules.visuals;

import modification.enummerates.Category;
import modification.events.EventRender3D;
import modification.extenders.Module;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.ColorUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public final class BlockOverlay
        extends Module {
    public BlockOverlay(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if (((paramEvent instanceof EventRender3D)) && (MC.objectMouseOver != null) && (MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)) {
            BlockPos localBlockPos = MC.objectMouseOver.getBlockPos();
            if (!MC.theWorld.isAirBlock(localBlockPos)) {
                Block localBlock = MC.theWorld.getBlockState(localBlockPos).getBlock();
                Modification.RENDER_UTIL.glSetColor(ColorUtil.MAIN_COLOR);
                AxisAlignedBB localAxisAlignedBB = localBlock.getSelectedBoundingBox(MC.theWorld, localBlockPos).expand(0.004D, 0.004D, 0.004D);
                RenderGlobal.func_181561_a(localAxisAlignedBB);
            }
        }
    }

    protected void onDeactivated() {
    }
}




