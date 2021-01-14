package splash.client.modules.combat;

import com.sun.jna.platform.win32.WinNT;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventMove;
import splash.utilities.player.BlockUtils;

/**
 * Author: Ice
 * Created: 22:26, 13-Jun-20
 * Project: Client
 */
public class Regen extends Module {

    public Regen() {
        super("Regen", "Auto heals.", ModuleCategory.COMBAT);
    }

    @Collect
    public void onSend(EventPacketSend eventPacketSend) {
        if (eventPacketSend.getSentPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer) eventPacketSend.getSentPacket();

            if (mc.thePlayer.onGround || BlockUtils.isOnLadder(mc.thePlayer) || BlockUtils.isInLiquid(mc.thePlayer) || BlockUtils.isOnLiquid(mc.thePlayer)) {
                if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.ticksExisted % 2 == 0) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(player.isOnGround()));
                }
            }
        }
    }

    @Collect
    public void onMotion(EventMove eventMove) {
        if (mc.thePlayer.onGround || BlockUtils.isOnLadder(mc.thePlayer) || BlockUtils.isInLiquid(mc.thePlayer) || BlockUtils.isOnLiquid(mc.thePlayer)) {
            if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth()) {
                for (int i = 0; i < mc.thePlayer.getMaxHealth() - mc.thePlayer.getHealth(); i++) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                }
            }
        }
    }
}
