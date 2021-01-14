package cn.kody.debug.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import com.darkmagician6.eventapi.types.EventType;

import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.mods.COMBAT.KillAura;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;

public class AntiAim extends Mod
{
    public Value<String> yaw;
    public Value<String> pitch;
    public Value<Boolean> sneak;
    float[] lastAngles;
    public static float rotationPitch;
    private boolean fake;
    private boolean fake1;
    private boolean shouldsneak;
    TimeHelper fakeJitter;
    
    public AntiAim() {
        super("AntiAim", "Anti Aim", Category.PLAYER);
        this.yaw = new Value<String>("AntiAim", "Yaw", 0);
        this.pitch = new Value<String>("AntiAim", "Pitch", 0);
        this.sneak = new Value<Boolean>("AntiAim_Sneak", true);
        this.fakeJitter = new TimeHelper();
        this.yaw.addValue("FakeJitter");
        this.yaw.addValue("Reverse");
        this.yaw.addValue("Jitter");
        this.yaw.addValue("Lisp");
        this.yaw.addValue("SpinSlow");
        this.yaw.addValue("SpinFast");
        this.yaw.addValue("Sideways");
        this.yaw.addValue("FakeHead");
        this.yaw.addValue("Freestanding");
        this.pitch.addValue("HalfDown");
        this.pitch.addValue("Normal");
        this.pitch.addValue("Zero");
        this.pitch.addValue("Up");
        this.pitch.addValue("Stutter");
        this.pitch.addValue("Reverse");
    }
    
    @Override
    public void onDisable() {
        this.fake1 = true;
        this.lastAngles = null;
        this.shouldsneak = false;
        AntiAim.rotationPitch = 0.0f;
        this.mc.thePlayer.renderYawOffset = this.mc.thePlayer.rotationYaw;
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        this.fake1 = true;
        this.lastAngles = null;
        this.shouldsneak = false;
        AntiAim.rotationPitch = 0.0f;
        super.onEnable();
    }
    
    @EventTarget
    public void onMotion(final EventPreMotion eventMotion) {
        if (eventMotion.getEventType() == EventType.PRE && this.sneak.getValueState()) {
            if (this.shouldsneak) {
                boolean shouldsneak;
                if (!this.shouldsneak) {
                    shouldsneak = true;
                }
                else {
                    shouldsneak = false;
                }
                this.shouldsneak = shouldsneak;
            }
            if (this.shouldsneak) {
                this.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        if (eventMotion.getEventType() == EventType.PRE && !this.mc.gameSettings.keyBindUseItem.pressed && KillAura.target == null) {
            if (this.lastAngles == null) {
                this.lastAngles = new float[] { this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch };
            }
            boolean fake;
            if (!this.fake) {
                fake = true;
            }else {
                fake = false;
            }
            this.fake = fake;
            if (this.yaw.isCurrentMode("Jitter")) {
                final float n;
                eventMotion.setYaw(n = this.lastAngles[0] + 90.0f);
                this.lastAngles = new float[] { n, this.lastAngles[1] };
                this.updateAngles(n, this.lastAngles[1]);
                this.mc.thePlayer.renderYawOffset = n;
                this.mc.thePlayer.prevRenderYawOffset = n;
            }
            else if (this.yaw.isCurrentMode("Lisp")) {
                final float yaw = this.lastAngles[0] + 150000.0f;
                this.lastAngles = new float[] { yaw, this.lastAngles[1] };
                eventMotion.setYaw(yaw);
                this.updateAngles(yaw, this.lastAngles[1]);
            }
            else if (this.yaw.isCurrentMode("Reverse")) {
                final float yaw2 = this.mc.thePlayer.rotationYaw + 180.0f;
                this.lastAngles = new float[] { yaw2, this.lastAngles[1] };
                eventMotion.setYaw(yaw2);
                this.updateAngles(yaw2, this.lastAngles[1]);
            }
            else if (this.yaw.isCurrentMode("Sideways")) {
                final float yaw3 = this.mc.thePlayer.rotationYaw - 90.0f;
                this.lastAngles = new float[] { yaw3, this.lastAngles[1] };
                eventMotion.setYaw(yaw3);
                this.updateAngles(yaw3, this.lastAngles[1]);
            }
            else if (this.yaw.isCurrentMode("FakeJitter")) {
                if (this.fakeJitter.delay(350.0)) {
                    boolean fake2;
                    if (!this.fake1) {
                        fake2 = true;
                    }
                    else {
                        fake2 = false;
                    }
                    this.fake1 = fake2;
                    this.fakeJitter.reset();
                }
                final float rotationYaw = this.mc.thePlayer.rotationYaw;
                int n2;
                if (this.fake1) {
                    n2 = 90;
                }
                else {
                    n2 = -90;
                }
                final float yaw4 = rotationYaw + n2;
                this.lastAngles = new float[] { yaw4, this.lastAngles[1] };
                eventMotion.setYaw(yaw4);
                this.updateAngles(yaw4, this.lastAngles[1]);
            }
            else if (this.yaw.isCurrentMode("FakeHead")) {
                if (this.fakeJitter.delay(1100.0)) {
                    boolean fake3;
                    if (!this.fake1) {
                        fake3 = true;
                    }
                    else {
                        fake3 = false;
                    }
                    this.fake1 = fake3;
                    this.fakeJitter.reset();
                }
                final float rotationYaw2 = this.mc.thePlayer.rotationYaw;
                int n3;
                if (this.fake1) {
                    n3 = 90;
                }
                else {
                    n3 = -90;
                }
                final float yaw5 = rotationYaw2 + n3;
                if (this.fake1) {
                    this.fake1 = false;
                }
                this.lastAngles = new float[] { yaw5, this.lastAngles[1] };
                eventMotion.setYaw(yaw5);
                this.updateAngles(yaw5, this.lastAngles[1]);
            }
            else if (this.yaw.isCurrentMode("Freestanding")) {
                final float yaw6 = (float)(this.mc.thePlayer.rotationYaw + 5.0f + Math.random() * 175.0);
                this.lastAngles = new float[] { yaw6, this.lastAngles[1] };
                eventMotion.setYaw(yaw6);
                this.updateAngles(yaw6, this.lastAngles[1]);
            }
            else if (this.yaw.isCurrentMode("SpinFast")) {
                final float yaw7 = this.lastAngles[0] + 45.0f;
                this.lastAngles = new float[] { yaw7, this.lastAngles[1] };
                eventMotion.setYaw(yaw7);
                this.updateAngles(yaw7, this.lastAngles[1]);
            }
            else if (this.yaw.isCurrentMode("SpinSlow")) {
                final float yaw8 = this.lastAngles[0] + 10.0f;
                this.lastAngles = new float[] { yaw8, this.lastAngles[1] };
                eventMotion.setYaw(yaw8);
                this.updateAngles(yaw8, this.lastAngles[1]);
            }
            if (this.pitch.isCurrentMode("HalfDown")) {
                final float pitch = 90.0f;
                this.lastAngles = new float[] { this.lastAngles[0], pitch };
                eventMotion.setPitch(pitch);
                this.updateAngles(this.lastAngles[0], pitch);
            }
            else if (this.pitch.isCurrentMode("Normal")) {
                this.updateAngles(this.lastAngles[0], 0.0f);
            }
            else if (this.pitch.isCurrentMode("Reverse")) {
                final float pitch2 = this.mc.thePlayer.rotationPitch + 180.0f;
                this.lastAngles = new float[] { this.lastAngles[0], pitch2 };
                eventMotion.setPitch(pitch2);
                this.updateAngles(this.lastAngles[0], pitch2);
            }
            else if (this.pitch.isCurrentMode("Stutter")) {
                float n4;
                if (this.fake) {
                    n4 = 90.0f;
                    eventMotion.setPitch(n4);
                }
                else {
                    n4 = -45.0f;
                    eventMotion.setPitch(n4);
                }
                this.lastAngles = new float[] { this.lastAngles[0], n4 };
                this.updateAngles(this.lastAngles[0], n4);
            }
            else if (this.pitch.isCurrentMode("Up")) {
                this.lastAngles = new float[] { this.lastAngles[0], -90.0f };
                eventMotion.setPitch(-90.0f);
                this.updateAngles(this.lastAngles[0], -90.0f);
            }
            else if (this.pitch.isCurrentMode("Zero")) {
                this.lastAngles = new float[] { this.lastAngles[0], -179.0f };
                eventMotion.setPitch(-180.0f);
                this.updateAngles(this.lastAngles[0], -179.0f);
            }
            }
            
        }
    
    public void updateAngles(final float n, final float rotationPitch) {
        if (this.mc.gameSettings.thirdPersonView != 0) {
            AntiAim.rotationPitch = rotationPitch;
            this.mc.thePlayer.rotationYawHead = n;
            this.mc.thePlayer.renderYawOffset = n;
        }
    }
}
