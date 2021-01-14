package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventMovement;
import modification.events.EventPostMotion;
import modification.events.EventPreMotion;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.TimeUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Speed
        extends Module {
    private final Value<String> mode = new Value("Mode", "Sprint jump", new String[]{"Sprint jump", "Ground", "Teleport", "Bhop"}, this, new String[0]);
    private final TimeUtil timer = new TimeUtil();
    private int count;
    private int stage;

    public Speed(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.count = 0;
        this.stage = 1;
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            this.tag = ((String) this.mode.value);
        }
        Object localObject;
        if ((paramEvent instanceof EventPreMotion)) {
            localObject = (String) this.mode.value;
            int i = -1;
            switch (((String) localObject).hashCode()) {
                case 2141373863:
                    if (((String) localObject).equals("Ground")) {
                        i = 0;
                    }
                    break;
                case 1257294420:
                    if (((String) localObject).equals("Sprint jump")) {
                        i = 1;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    if (MC.thePlayer.onGround) {
                        MC.thePlayer.jump();
                    } else {
                        MC.thePlayer.motionY = -10.0D;
                        MC.thePlayer.posY = ((int) MC.thePlayer.posY);
                        MC.thePlayer.cameraPitch = 0.0F;
                    }
                    break;
                case 1:
                    if (MC.thePlayer.moveForward > 0.0F) {
                        if (MC.thePlayer.onGround) {
                            MC.thePlayer.jump();
                            this.count |= 0x1;
                        }
                        if ((this.count >= 3) && (!MC.thePlayer.onGround)) {
                            MC.thePlayer.motionX *= 1.03D;
                            MC.thePlayer.motionZ *= 1.03D;
                            this.count = 0;
                        }
                    }
                    break;
            }
        }
        if (((paramEvent instanceof EventPostMotion)) && (((String) this.mode.value).equals("Teleport"))) {
            if (MC.thePlayer.onGround) {
                MC.thePlayer.jump();
            } else if ((MC.thePlayer.fallDistance >= 0.5D) && (MC.thePlayer.fallDistance <= 0.8D)) {
                Modification.ENTITY_UTIL.portPlayer(1.0D, false, 0.0D);
            }
        }
        if (((paramEvent instanceof EventMovement)) && (((String) this.mode.value).equals("Bhop"))) {
            localObject = (EventMovement) paramEvent;
            BigDecimal localBigDecimal1 = new BigDecimal(MC.thePlayer.posY - (int) MC.thePlayer.posY).setScale(3, RoundingMode.HALF_UP);
            BigDecimal localBigDecimal2 = new BigDecimal(0.943D).setScale(4, RoundingMode.HALF_UP);
            if (localBigDecimal1.doubleValue() == localBigDecimal2.doubleValue()) {
                MC.thePlayer.motionX = (-Math.sin(Math.toRadians(MC.thePlayer.rotationYaw)) * 0.5D);
                MC.thePlayer.motionZ = (Math.cos(Math.toRadians(MC.thePlayer.rotationYaw)) * 0.5D);
            }
            if (MC.thePlayer.onGround) {
                MC.thePlayer.jump();
                MC.thePlayer.motionY = 0.4D;
                ((EventMovement) localObject).motionY = 0.4D;
            }
        }
    }

    protected void onDeactivated() {
    }
}




