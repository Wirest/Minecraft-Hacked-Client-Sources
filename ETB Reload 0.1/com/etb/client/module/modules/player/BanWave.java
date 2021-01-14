package com.etb.client.module.modules.player;

import com.etb.client.Client;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.Printer;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 7/6/2019
 **/
public class BanWave extends Module {
    private String cmd = "/ban";
    private TimerUtil timer = new TimerUtil();
    private BooleanValue mass = new BooleanValue("Mass",false);
    private NumberValue<Integer> speed = new NumberValue("Speed", 3, 1, 10, 1);
    private float delay;
    private Queue<String> valid = new ArrayDeque<>();
    public BanWave() {
        super("BanWave", Category.PLAYER, new Color(0xFF6D84).getRGB());
        addValues(mass,speed);
    }
    @Override
    public void onEnable() {
        mc.thePlayer.sendQueue.getPlayerInfoMap().stream().map(o -> o.getGameProfile().getName()).filter(o -> !o.equals(mc.thePlayer.getName()) && !Client.INSTANCE.getFriendManager().isFriend(o)).forEach(o -> valid.add(o));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        if (mass.isEnabled()) {
            for (String nigga : valid) {
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(String.format(getCmd() + " %s #ETB", nigga)));
                valid.remove(nigga);
            }
            if (valid.isEmpty()) {
                this.timer.reset();
                this.valid.clear();
                Printer.print("Banwave finished!");
                toggle();
                return;
            }
        } else {
            if (this.timer.reach(1000 / speed.getValue().longValue())) {
                final String s = valid.poll();
                if (s == null) {
                    this.timer.reset();
                    this.valid.clear();
                    Printer.print("Banwave finished!");
                    toggle();
                    return;
                }
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(String.format(getCmd() + " %s #ETB", s)));
                this.timer.reset();
            }
        }
    }
    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
}

    }