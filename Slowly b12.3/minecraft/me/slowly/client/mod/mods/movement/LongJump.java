/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventPostMotion;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;

public class LongJump
extends Mod {
    private Value<String> mode = new Value("LongJump", "Mode", 0);
    private Value<Double> guardianboost = new Value<Double>("LongJump_Guardian", 5.0, 2.0, 8.0, 0.5);
    private TimeHelper timer = new TimeHelper();
    private boolean jump;
    private boolean speedTick;
    private boolean inair = false;
    private double posY;
    private boolean wet;
    private boolean reachmax = false;
    private int groundTicks;
    private int airTicks;
    private int lastHDistance;
    private boolean isSpeeding;
    private double accelrate;

    public LongJump() {
        super("LongJump", Mod.Category.MOVEMENT, Colors.YELLOW.c);
        this.mode.mode.add("OldAAC");
        this.mode.mode.add("NCP");
        this.mode.mode.add("OtherNCP");
        this.mode.mode.add("MineSecure");
        this.mode.mode.add("Guardian");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.groundTicks = 0;
        ClientUtil.sendClientMessage("LongJump Enable", ClientNotification.Type.SUCCESS);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
        ClientUtil.sendClientMessage("LongJump Disable", ClientNotification.Type.ERROR);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setColor(Colors.AQUA.c);
        if (this.mode.isCurrentMode("OldAAC")) {
            this.mc.gameSettings.keyBindForward.pressed = false;
            if (this.mc.thePlayer.onGround) {
                this.jump = true;
            }
            if (this.mc.thePlayer.onGround && this.timer.isDelayComplete(500L)) {
                this.mc.thePlayer.motionY = 0.42;
                PlayerUtil.toFwd(2.3);
                this.timer.reset();
            } else if (!this.mc.thePlayer.onGround && this.jump) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
                this.jump = false;
            }
        } else if (this.mode.isCurrentMode("NCP")) {
            if (PlayerUtil.MovementInput() && this.mc.thePlayer.fallDistance < 1.0f) {
                float direction = this.mc.thePlayer.rotationYaw;
                float x = (float)Math.cos((double)(direction + 90.0f) * 3.141592653589793 / 180.0);
                float z = (float)Math.sin((double)(direction + 90.0f) * 3.141592653589793 / 180.0);
                if (this.mc.thePlayer.isCollidedVertically && PlayerUtil.MovementInput() && this.mc.gameSettings.keyBindJump.pressed) {
                    this.mc.thePlayer.motionX = x * 0.29f;
                    this.mc.thePlayer.motionZ = z * 0.29f;
                }
                if (this.mc.thePlayer.motionY == 0.33319999363422365 && PlayerUtil.MovementInput()) {
                    this.mc.thePlayer.motionX = (double)x * 1.261;
                    this.mc.thePlayer.motionZ = (double)z * 1.261;
                }
            }
        } else if (this.mode.isCurrentMode("OtherNCP")) {
            EntityPlayerSP player = this.mc.thePlayer;
            if (!PlayerUtil.MovementInput()) {
                return;
            }
            if (this.mc.thePlayer.onGround) {
                this.lastHDistance = 0;
            }
            float direction = this.mc.thePlayer.rotationYaw + (float)(this.mc.thePlayer.moveForward < 0.0f ? 180 : 0) + (this.mc.thePlayer.moveStrafing > 0.0f ? -90.0f * (this.mc.thePlayer.moveForward > 0.0f ? 0.5f : (this.mc.thePlayer.moveForward < 0.0f ? -0.5f : 1.0f)) : 0.0f) - (this.mc.thePlayer.moveStrafing < 0.0f ? -90.0f * (this.mc.thePlayer.moveForward > 0.0f ? 0.5f : (this.mc.thePlayer.moveForward < 0.0f ? -0.5f : 1.0f)) : 0.0f);
            float xDir = (float)Math.cos((double)(direction + 90.0f) * 3.141592653589793 / 180.0);
            float zDir = (float)Math.sin((double)(direction + 90.0f) * 3.141592653589793 / 180.0);
            if (!this.mc.thePlayer.isCollidedVertically) {
                this.isSpeeding = true;
                this.groundTicks = 0;
                if (!this.mc.thePlayer.isCollidedVertically) {
                    if (this.mc.thePlayer.motionY == -0.07190068807140403) {
                        player.motionY *= 0.3499999940395355;
                    } else if (this.mc.thePlayer.motionY == -0.10306193759436909) {
                        player.motionY *= 0.550000011920929;
                    } else if (this.mc.thePlayer.motionY == -0.13395038817442878) {
                        player.motionY *= 0.6700000166893005;
                    } else if (this.mc.thePlayer.motionY == -0.16635183030382) {
                        player.motionY *= 0.6899999976158142;
                    } else if (this.mc.thePlayer.motionY == -0.19088711097794803) {
                        player.motionY *= 0.7099999785423279;
                    } else if (this.mc.thePlayer.motionY == -0.21121925191528862) {
                        player.motionY *= 0.20000000298023224;
                    } else if (this.mc.thePlayer.motionY == -0.11979897632390576) {
                        player.motionY *= 0.9300000071525574;
                    } else if (this.mc.thePlayer.motionY == -0.18758479151225355) {
                        player.motionY *= 0.7200000286102295;
                    } else if (this.mc.thePlayer.motionY == -0.21075983825251726) {
                        player.motionY *= 0.7599999904632568;
                    }
                    if (this.mc.thePlayer.motionY < -0.2 && this.mc.thePlayer.motionY > -0.24) {
                        player.motionY *= 0.7;
                    }
                    if (this.mc.thePlayer.motionY < -0.25 && this.mc.thePlayer.motionY > -0.32) {
                        player.motionY *= 0.8;
                    }
                    if (this.mc.thePlayer.motionY < -0.35 && this.mc.thePlayer.motionY > -0.8) {
                        player.motionY *= 0.98;
                    }
                    if (this.mc.thePlayer.motionY < -0.8 && this.mc.thePlayer.motionY > -1.6) {
                        player.motionY *= 0.99;
                    }
                }
                this.mc.timer.timerSpeed = 0.8f;
                double[] speedVals = new double[]{0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761, 0.402178, 0.399611, 0.39706, 0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625, 0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345, 0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315, 0.312, 0.309, 0.307, 0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291, 0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273, 0.271, 0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249, 0.247, 0.245, 0.243, 0.241, 0.239, 0.237};
                if (this.mc.gameSettings.keyBindForward.pressed) {
                    try {
                        this.mc.thePlayer.motionX = (double)xDir * speedVals[this.airTicks - 1] * 3.0;
                        this.mc.thePlayer.motionZ = (double)zDir * speedVals[this.airTicks - 1] * 3.0;
                    }
                    catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                } else {
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.thePlayer.motionZ = 0.0;
                }
            } else {
                this.mc.timer.timerSpeed = 1.0f;
                this.airTicks = 0;
                player.motionX /= 13.0;
                player.motionZ /= 13.0;
                if (this.groundTicks == 1) {
                    this.updatePosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                    this.updatePosition(this.mc.thePlayer.posX + 0.0624, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                    this.updatePosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.419, this.mc.thePlayer.posZ);
                    this.updatePosition(this.mc.thePlayer.posX + 0.0624, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                    this.updatePosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.419, this.mc.thePlayer.posZ);
                } else if (this.groundTicks > 2) {
                    this.groundTicks = 0;
                    this.mc.thePlayer.motionX = (double)xDir * 0.3;
                    this.mc.thePlayer.motionZ = (double)zDir * 0.3;
                    this.mc.thePlayer.motionY = 0.42399999499320984;
                }
            }
        } else if (this.mode.isCurrentMode("MineSecure")) {
            if (this.mc.thePlayer.onGround && PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
                this.mc.timer.timerSpeed = 1.0f;
                this.mc.thePlayer.motionY = 0.54;
            } else if (PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
                PlayerUtil.setSpeed(3.0);
            }
            if (!PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
            }
        } else if (this.mode.isCurrentMode("Guardian")) {
            if (this.mc.gameSettings.keyBindForward.pressed && this.mc.thePlayer.onGround && this.timer.isDelayComplete(500L)) {
                this.mc.thePlayer.motionY = 0.41764345;
                PlayerUtil.toFwd(this.guardianboost.getValueState());
                this.timer.reset();
            }
            if (!PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
            }
        }
    }

    @EventTarget
    public void onPostMotion(EventPostMotion event) {
        if (this.mc.thePlayer.onGround) {
            ++this.groundTicks;
            this.airTicks = 0;
        } else {
            ++this.airTicks;
        }
    }

    public void updatePosition(double x, double y, double z) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, this.mc.thePlayer.onGround));
    }
}

