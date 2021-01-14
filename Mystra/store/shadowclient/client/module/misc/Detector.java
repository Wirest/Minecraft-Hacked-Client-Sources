package store.shadowclient.client.module.misc;

import java.util.Arrays;
import java.util.List;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Detector extends Module {

    private final List<String> hackMatches = Arrays.asList("hack", "report", "cheat",
            "aura", "speed", "record", "what client", "ban", "bhop", "bunny hop", "hax");

    private final List<String> noUMatches = Arrays.asList("gay", "fag", "stupid",
            "retard", "idiot", "skid", "loser", "cheater", "retard",
            "kys", "neck your self", "delete self", "cunt", "suck", "kill yourself");

	public Detector() {
		super("Detector", 0, Category.MISC);
		
		Shadow.instance.settingsManager.rSetting(new Setting("Cheating Warning", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Auto Disable", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Unblock", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Strict", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Chat", this, true));

	}

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (getMc().thePlayer == null || getMc().theWorld == null) {
            return;
        }
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
                final Vec3 tppos = new Vec3(packet.getX(), packet.getY(), packet.getY());
                double dist = tppos.squareDistanceTo(getMc().thePlayer.getPositionVector());
                if (Shadow.instance.settingsManager.getSettingByName("Unblock").getValBoolean()) {
                    getMc().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                if (Shadow.instance.settingsManager.getSettingByName("Auto Disable").getValBoolean() && (!(getMc().theWorld.getBlockState(new BlockPos(getMc().thePlayer.posX, getMc().thePlayer.posY - 1.0, getMc().thePlayer.posZ)).getBlock() instanceof BlockAir) || !(getMc().theWorld.getBlockState(new BlockPos(getMc().thePlayer.posX, getMc().thePlayer.posY - 2.0, getMc().thePlayer.posZ)).getBlock() instanceof BlockAir))) {
                    if (Shadow.instance.moduleManager.getModuleByName("Speed").isToggled() || Shadow.instance.moduleManager.getModuleByName("Fly").isToggled() || Shadow.instance.moduleManager.getModuleByName("LongJump").isToggled()) {
                    	Shadow.instance.moduleManager.getModuleByName("Speed").setState(false);
                    	Shadow.instance.moduleManager.getModuleByName("Fly").setState(false);
                    	Shadow.instance.moduleManager.getModuleByName("LongJump").setState(false);
                        Shadow.addChatMessage("Lag back/Teleport! turned off some mods");
                        return;
                    }
                }
            } else if (Shadow.instance.settingsManager.getSettingByName("Chat").getValBoolean() && event.getPacket() instanceof S02PacketChat) {
                final S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
                final String message = chatPacket.getChatComponent().getUnformattedText();
                if (message.toLowerCase().contains(getMc().getSession().getUsername().toLowerCase()) || !Shadow.instance.settingsManager.getSettingByName("Strict").getValBoolean()) {
                    if (Shadow.instance.settingsManager.getSettingByName("Cheating Warning").getValBoolean()) {
                        for (String hackMatch : hackMatches) {
                            if (!message.contains(hackMatch)) {
                                continue;
                            }
                            Shadow.addChatMessage("Suspected of cheating! turn it down buddy!");
                            break;
                        }
                    }
                }
        }
    }
}
