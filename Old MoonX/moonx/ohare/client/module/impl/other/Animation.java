package moonx.ohare.client.module.impl.other;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.TickEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * made by oHare for MoonX
 *
 * @since 9/8/2019
 **/
public class Animation extends Module {
    public EnumValue<mode> Mode = new EnumValue<>("Mode",mode.LUCKY);
    public NumberValue<Float> X = new NumberValue<>("X", 0.0F, -2.0F, 2.0F, 0.1F);
    public NumberValue<Float> Y = new NumberValue<>("Y", 0.0F, -2.0F, 2.0F, 0.1F);
    public NumberValue<Float> ZOOM = new NumberValue<>("Zoom",0.0F, -2.0F, 2.0F, 0.1F);
    public NumberValue<Float> SCALE = new NumberValue<>("Scale", 2.0F, -4.0F, 4.0F, 0.1F);
    public Animation() {
        super("Animation", Category.OTHER, new Color(0xFF4BAA).getRGB());
        setRenderLabel("Animation");
    }
    @Handler
    public void onTick(TickEvent event) {
        setSuffix(StringUtils.capitalize(Mode.getValue().toString().toLowerCase()));
    }
    public enum mode {
        OLD,NORMAL,HIDE,SLIDE,LUCKY,EXHIBOBO,OHARE, WIZZARD, LENNOX, CUSTOM
    }
}
