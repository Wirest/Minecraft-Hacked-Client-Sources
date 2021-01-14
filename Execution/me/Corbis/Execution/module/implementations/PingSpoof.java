package me.Corbis.Execution.module.implementations;

import com.mojang.realmsclient.client.Ping;
import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class PingSpoof extends Module {
    TimeHelper timer = new TimeHelper();
    private List<C00PacketKeepAlive> packetList = new ArrayList<>();
    public PingSpoof(){
        super("PingSpoof", Keyboard.KEY_NONE, Category.EXPLOIT);

        Execution.instance.settingsManager.rSetting(new Setting("PingSpoof Delay", this, 500, 0, 3000, true));
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event){
        final long delay = (long) Execution.instance.settingsManager.getSettingByName("PingSpoof Delay").getValDouble();



        if (this.packetList.size() > 10) {
            this.packetList.clear();
        }



        if (event.getPacket() instanceof C00PacketKeepAlive && mc.thePlayer.isEntityAlive()) {
            C00PacketKeepAlive packet = (C00PacketKeepAlive) event.getPacket();
            this.packetList.add(packet);
            event.setCancelled(true);

            if (this.timer.hasReached(delay)) {
                this.timer.reset();
            }
        }

        if (this.timer.hasReached(delay) && !this.packetList.isEmpty()) {
            C00PacketKeepAlive packet = this.packetList.get(0);
            if (packet != null && packetList.contains(packet)) {
                mc.getNetHandler().getNetworkManager().sendPacket(packet);
                this.packetList.remove(packet);
                this.timer.reset();
            }
        }
    }
}
