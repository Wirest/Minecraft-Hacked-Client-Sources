package cn.kody.debug.mod.mods.MOVEMENT;

import java.util.List;
import java.util.Timer;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import java.util.Arrays;
import com.darkmagician6.eventapi.types.EventType;

import cn.kody.debug.events.EventStep;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.PlayerUtil;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;

import com.darkmagician6.eventapi.EventTarget;

public class Step extends Mod
{
    public Value<String> mode;
    public Value<Boolean> time;
    public Value<Double> height;
    public Value<Double> delay;
    TimeHelper timer;
    boolean resetTimer;
    
    public Step() {
        super("Step", Category.MOVEMENT);
        this.mode = new Value<String>("Step", "Mode", 0);
        this.time = new Value<Boolean>("Step_Timer", false);
        this.height = new Value<Double>("Step_Height", 1.0, 1.0, 2.0, 0.5);
        this.delay = new Value<Double>("Step_Delay", 0.0, 0.0, 1000.0, 50.0);
        this.timer = new TimeHelper();
        this.mode.addValue("Vanilla");
        this.mode.addValue("NCP");
    }
    
    @Override
    public void onEnable() {
        this.resetTimer = false;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.stepHeight = 0.5f;
        }
        this.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
        if (this.mc.timer.timerSpeed < 1.0f && this.mc.thePlayer.onGround) {
            this.mc.timer.timerSpeed = 1.0f;
        }
    }
    
    @EventTarget
    public void onStep(EventStep event) {
        if (!PlayerUtil.isInWater()) {
            if (this.resetTimer) {
                boolean resetTimer;
                if (!this.resetTimer) {
                    resetTimer = true;
                }
                else {
                    resetTimer = false;
                }
                this.resetTimer = resetTimer;
                this.mc.timer.timerSpeed = 1.0f;
            }
            if (event.getEventType() == EventType.PRE) {
                if (!this.mc.thePlayer.onGround || !this.timer.isDelayComplete(this.delay.getValueState().longValue())) {
                    event.setHeight(this.mc.thePlayer.stepHeight = 0.5f);
                    return;
                }
                this.mc.thePlayer.stepHeight = this.height.getValueState().floatValue();
                event.setHeight(this.height.getValueState().floatValue());
            }
            else if (this.mode.isCurrentMode("NCP") && event.getHeight() > 0.5) {
                final double n = this.mc.thePlayer.getEntityBoundingBox().minY - this.mc.thePlayer.posY;
                if (n >= 0.625) {
                    if (this.time.getValueState()) {
                        net.minecraft.util.Timer timer = this.mc.timer;
                        final float n2 = 0.6f;
                        float n3;
                        if (n >= 1.0) {
                            n3 = Math.abs(1.0f - (float)n) * 0.33f;
                        }
                        else {
                            n3 = 0.0f;
                        }
                        timer.timerSpeed = n2 - n3;
                        if (this.mc.timer.timerSpeed <= 0.05f) {
                            this.mc.timer.timerSpeed = 0.05f;
                        }
                    }
                    this.resetTimer = true;
                    this.ncpStep(n);
                    this.timer.reset();
                }
            }
        }
    }
    
    void ncpStep(final double n) {
        final List<Double> list = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);
        final double posX = this.mc.thePlayer.posX;
        final double posZ = this.mc.thePlayer.posZ;
        double posY = this.mc.thePlayer.posY;
        if (n < 1.1) {
            double n2 = 0.42;
            double n3 = 0.75;
            if (n != 1.0) {
                n2 *= n;
                n3 *= n;
                if (n2 > 0.425) {
                    n2 = 0.425;
                }
                if (n3 > 0.78) {
                    n3 = 0.78;
                }
                if (n3 < 0.49) {
                    n3 = 0.49;
                }
            }
            if (n2 == 0.42) {
                n2 = 0.41999998688698;
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + n2, posZ, false));
            if (posY + n3 < posY + n) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + n3, posZ, false));
            }
            return;
        }
        if (n < 1.6) {
            int i = 0;
            while (i < list.size()) {
                posY += list.get(i);
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                ++i;
            }
        }
        else if (n < 2.1) {
            final double[] array = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869 };
            final int length = array.length;
            int j = 0;
            while (j < length) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
                ++j;
            }
        }
        else {
            final double[] array2 = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
            final int length2 = array2.length;
            int k = 0;
            while (k < length2) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array2[k], posZ, false));
                ++k;
            }
        }
    }
}
