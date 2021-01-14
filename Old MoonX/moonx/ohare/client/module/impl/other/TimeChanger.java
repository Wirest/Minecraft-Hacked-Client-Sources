package moonx.ohare.client.module.impl.other;

import com.google.common.eventbus.Subscribe;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.game.TickEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.awt.*;

/**
 * made by oHare for oHareWare
 *
 * @since 7/19/2019
 **/
public class TimeChanger extends Module {
    public NumberValue<Long> time = new NumberValue<>("Time", 18400L, 0L, 24000L, 100L);
    public TimeChanger() {
        super("TimeChanger", Category.OTHER, new Color(0x8D9D3C).getRGB());
        setRenderLabel("Time Changer");
        setDescription("Change client-side time.");
    }
}
