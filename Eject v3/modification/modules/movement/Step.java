package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public final class Step
        extends Module {
    public final Value<String> mode = new Value("Mode", "Intave", new String[]{"Intave", "Vanilla"}, this, new String[0]);

    public Step(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            switch ((String) this.mode.value) {
                case "Intave":
                    if (MC.thePlayer.isCollidedHorizontally) {
                        MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.42D, MC.thePlayer.posZ, false));
                        MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.75D, MC.thePlayer.posZ, false));
                        MC.thePlayer.stepHeight = 1.0F;
                    }
                    break;
                case "Vanilla":
                    MC.thePlayer.stepHeight = 3.0F;
            }
            this.tag = ((String) this.mode.value);
        }
    }

    protected void onDeactivated() {
        MC.thePlayer.stepHeight = 0.6F;
    }
}




