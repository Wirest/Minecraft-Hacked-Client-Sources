package moonx.ohare.client.module.impl.visuals;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.render.HurtcamEvent;
import moonx.ohare.client.module.Module;

import java.awt.*;

public class NoHurtCam extends Module {

    public NoHurtCam() {
        super("Hurt Cam", Category.VISUALS, new Color(0xA4A29E).getRGB());
    }
    @Handler
    public void Hurtcam(HurtcamEvent event) {
        event.setCanceled(true);
    }
}
