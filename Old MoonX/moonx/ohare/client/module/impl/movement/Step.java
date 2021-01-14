package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.StepEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.*;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Step extends Module {
    private NumberValue<Float> height = new NumberValue<>("Height", 1.0f, 1.0f, 9f, 0.5f);
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 300, 0, 2000, 1);
    private BooleanValue smooth = new BooleanValue("Smooth", "Smooth", true);
    public EnumValue<mode> Mode = new EnumValue<>("Mode", mode.VANILLA);
    private TimerUtil time = new TimerUtil();
    private boolean hasStep;

    public Step() {
        super("Step", Category.MOVEMENT, new Color(102, 255, 51, 255).getRGB());
        setDescription("Automatically step up blocks");
    }

    public enum mode {
        VANILLA, NCP, SPARTAN, DEV
    }

    @Override
    public void onDisable() {
        if (getMc().thePlayer != null) {
            getMc().thePlayer.stepHeight = 0.6f;
        }
    }

    @Handler
    public void onStep(StepEvent event) {
        if (getMc().theWorld == null || getMc().thePlayer == null || Moonx.INSTANCE.getModuleManager().getModule("speed").isEnabled() || Moonx.INSTANCE.getModuleManager().getModule("flight").isEnabled() || Moonx.INSTANCE.getModuleManager().getModule("longjump").isEnabled())
            return;
        if (!getMc().thePlayer.isInLiquid()&&Mode.getValue() != mode.VANILLA) {
            if (event.isPre()) {
                if (getMc().thePlayer.isCollidedVertically && !getMc().gameSettings.keyBindJump.isKeyDown() && !getMc().thePlayer.isOnLadder() && time.reach(delay.getValue())) {
                    event.setHeight(height.getValue());
                } else {
                    event.setHeight(0.6f);
                }
            }
        }

        double rheight = getMc().thePlayer.getEntityBoundingBox().minY - getMc().thePlayer.posY;
        boolean canStep = rheight >= 0.625;
        if (canStep && Mode.getValue() != mode.VANILLA) {
            hasStep = true;
            switch (Mode.getValue()) {
                case SPARTAN:
                    spartanStep(rheight);
                    break;
                case NCP:
                    ncpStep(rheight);
                    break;
                default:
                    break;
            }
            time.reset();
        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(Mode.getValue().name().toLowerCase()));
        if (Mode.getValue() == mode.VANILLA)
            getMc().thePlayer.stepHeight = height.getValue();
        if (time.reach(80) && hasStep && smooth.isEnabled() && event.isPre() && Mode.getValue() != mode.VANILLA) {
            getMc().timer.timerSpeed = 1F;
            hasStep = false;
        }
    }

    private void spartanStep(double height) {
        double value = 0.5;
        for (int i = 0; i < height * 2; ++i) {
            getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + (value * i), getMc().thePlayer.posZ, true));
        }
    }

    private void ncpStep(double height) {
        double posX = getMc().thePlayer.posX;
        double posZ = getMc().thePlayer.posZ;
        double y = getMc().thePlayer.posY;
        if (height < 1.1) {
            if (smooth.isEnabled()) {
                getMc().timer.timerSpeed = 0.4F;
            }
            double first = 0.42f;
            double second = first + 0.3333;
            if (height != 1) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            if (first == 0.42) first = 0.425;
            getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
            if (y + second < y + height)
                getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
        } else if (height < 1.6) {
            if (smooth.isEnabled()) {
                getMc().timer.timerSpeed = 0.35F;
            }
            float[] heights = {0.425111231f, 0.821111231f, 0.699111231f, 0.599111231f, 1.022111231f};
            for (double off : heights) {
                getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        } else if (height < 2.1) {
            if (smooth.isEnabled()) {
                getMc().timer.timerSpeed = 0.22F;
            }
            float[] heights = {0.425111231f, 0.821111231f, 0.699111231f, 0.599111231f, 1.022111231f, 1.372111231f, 1.652111231f, 1.869111231f};
            for (double off : heights) {
                getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        } else {
            if (smooth.isEnabled()) {
                getMc().timer.timerSpeed = 0.21F;
            }
            float[] heights = {0.425111231f, 0.821111231f, 0.699111231f, 0.599111231f, 1.022111231f, 1.372111231f, 1.652111231f, 1.869111231f, 2.01895111231f, 1.9052111231f};
            for (double off : heights) {
                getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        }
    }
}