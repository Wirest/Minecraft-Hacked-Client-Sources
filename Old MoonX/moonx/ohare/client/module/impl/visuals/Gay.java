package moonx.ohare.client.module.impl.visuals;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.*;
import moonx.ohare.client.event.impl.player.*;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.apache.commons.lang3.*;

import java.awt.*;

public class Gay extends Module {
    public EnumValue<mode> Mode = new EnumValue<>("Mode",mode.XD);

    private float FallStack;

    public Gay() {
        super("Gay", Category.VISUALS, new Color(0xA4A29E).getRGB());
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(Mode.getValue().name().toLowerCase()));
        if (Mode.getValue() == mode.XD) {
            if (event.isPre()) {
                if (getMc().thePlayer.getHeldItem() != null) {
                    getMc().thePlayer.renderArmPitch -= 18;
                }
            }
        }
        if (Mode.getValue() == mode.XDLOL) {
            if (event.isPre()) {
                if (getMc().thePlayer.getHeldItem() != null) {
                    getMc().thePlayer.renderArmYaw *= 1;
                }
            }
        }
    }


    @Override
    public void onEnable() {
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        FallStack = 0;
    }

    public enum mode {
        XD,XDLOL
    }
}
