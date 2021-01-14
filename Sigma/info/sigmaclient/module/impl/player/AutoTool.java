/**
 * Time: 1:14:15 AM
 * Date: Jan 2, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import net.minecraft.util.BlockPos;

public class AutoTool extends Module {
    public AutoTool(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
        if (!mc.gameSettings.keyBindAttack.getIsKeyPressed()) {
            return;
        }
        if (mc.objectMouseOver == null) {
            return;
        }
        BlockPos pos = mc.objectMouseOver.getBlockPos();
        if (pos == null) {
            return;
        }
        BlockUtils.updateTool(pos);
    }
}