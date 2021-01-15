// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.World;

import cf.euphoria.euphorical.Events.EventBBSet;
import cf.euphoria.euphorical.Utils.EntityUtils;
import net.minecraft.util.BlockPos;
import cf.euphoria.euphorical.Utils.NetUtils;
import cf.euphoria.euphorical.Utils.BlockUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import cf.euphoria.euphorical.Events.EventState;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Utils.ModeUtils;
import cf.euphoria.euphorical.Events.EventTick;
import net.minecraft.util.Timer;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Utils.TimeHelper;
import cf.euphoria.euphorical.Mod.Mod;

public class Phase extends Mod
{
    private TimeHelper timer;
    private int resetNext;
    
    public Phase() {
        super("Phase", Category.WORLD);
    }
    
    @Override
    public void onEnable() {
        this.timer = new TimeHelper();
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        Timer.timerSpeed = 1.0f;
        this.mc.thePlayer.noClip = false;
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        if (ModeUtils.phaseMode.equalsIgnoreCase("spider")) {
            this.setRenderName(String.format("%s %s", this.getModName(), "[Spider]"));
        }
        else if (ModeUtils.phaseMode.equalsIgnoreCase("vanilla")) {
            this.setRenderName(String.format("%s %s", this.getModName(), "[Vanilla]"));
        }
        else if (ModeUtils.phaseMode.equalsIgnoreCase("new")) {
            this.setRenderName(String.format("%s %s", this.getModName(), "[New]"));
        }
        else if (ModeUtils.phaseMode.equalsIgnoreCase("latest")) {
            this.setRenderName(String.format("%s %s", this.getModName(), "[Latest]"));
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        Timer.timerSpeed = 1.0f;
        if (ModeUtils.phaseMode.equalsIgnoreCase("vanilla") || ModeUtils.phaseMode.equalsIgnoreCase("spider")) {
            if (event.state == EventState.PRE) {
                if (ModeUtils.phaseMode.equalsIgnoreCase("spider")) {
                    if (this.timer.hasPassed(150.0) && this.mc.thePlayer.isCollidedHorizontally) {
                        float yaw = this.mc.thePlayer.rotationYaw;
                        if (this.mc.thePlayer.moveForward < 0.0f) {
                            yaw += 180.0f;
                        }
                        if (this.mc.thePlayer.moveStrafing > 0.0f) {
                            yaw -= 90.0f * ((this.mc.thePlayer.moveForward < 0.0f) ? -0.5f : ((this.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f));
                        }
                        if (this.mc.thePlayer.moveStrafing < 0.0f) {
                            yaw += 90.0f * ((this.mc.thePlayer.moveForward < 0.0f) ? -0.5f : ((this.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f));
                        }
                        final double horizontalMultiplier = 0.3;
                        final double xOffset = (float)Math.cos((yaw + 90.0f) * 3.141592653589793 / 180.0) * 0.3;
                        final double zOffset = (float)Math.sin((yaw + 90.0f) * 3.141592653589793 / 180.0) * 0.3;
                        double yOffset = 0.0;
                        for (int i = 0; i < 3; ++i) {
                            yOffset += 0.01;
                            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - yOffset, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + xOffset * i, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + zOffset * i, this.mc.thePlayer.onGround));
                        }
                    }
                }
                else if (ModeUtils.phaseMode.equalsIgnoreCase("vanilla")) {
                    final double mx2 = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
                    final double mz2 = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
                    final double x = this.mc.thePlayer.movementInput.moveForward * 2.6 * mx2 + this.mc.thePlayer.movementInput.moveStrafe * 2.6 * mz2;
                    final double z = this.mc.thePlayer.movementInput.moveForward * 2.6 * mz2 - this.mc.thePlayer.movementInput.moveStrafe * 2.6 * mx2;
                    if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && BlockUtils.isInsideBlock() && this.timer.hasPassed(150.0) && this.mc.thePlayer.isSneaking()) {
                        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z, false));
                        EntityUtils.blinkToPos(new double[] { this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ }, new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ), 1.0, new double[] { x, z });
                        this.timer.reset();
                    }
                }
                else if (!this.mc.thePlayer.isCollidedHorizontally) {
                    this.timer.reset();
                }
            }
        }
        else if (ModeUtils.phaseMode.equalsIgnoreCase("new") || ModeUtils.phaseMode.equalsIgnoreCase("latest")) {
            final double mx2 = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
            final double mz2 = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
            final double x = this.mc.thePlayer.movementInput.moveForward * 0.3 * mx2 + this.mc.thePlayer.movementInput.moveStrafe * 0.3 * mz2;
            final double z = this.mc.thePlayer.movementInput.moveForward * 0.3 * mz2 - this.mc.thePlayer.movementInput.moveStrafe * 0.3 * mx2;
            if (event.state == EventState.POST && this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && !BlockUtils.isInsideBlock() && this.timer.hasPassed(150.0)) {
                NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z, true));
                if (ModeUtils.phaseMode.equalsIgnoreCase("new")) {
                    for (int j = 0; j < 1; ++j) {
                        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, Double.MIN_VALUE, this.mc.thePlayer.posZ + z, true));
                    }
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z);
                }
                else if (ModeUtils.phaseMode.equalsIgnoreCase("latest")) {
                    EntityUtils.blinkToPos(new double[] { this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ }, new BlockPos(this.mc.thePlayer.posX + x, Double.MIN_VALUE, this.mc.thePlayer.posZ + z), 0.0, new double[] { 0.0, 0.0 });
                }
                this.timer.reset();
            }
        }
    }
    
    @EventTarget
    public void onBBSet(final EventBBSet event) {
        if ((ModeUtils.phaseMode.equalsIgnoreCase("vanilla") || ModeUtils.phaseMode.equalsIgnoreCase("new") || ModeUtils.phaseMode.equalsIgnoreCase("latest")) && !this.timer.hasPassed(150.0)) {
            return;
        }
        this.mc.thePlayer.noClip = true;
        if (event.pos.getY() > this.mc.thePlayer.posY + (BlockUtils.isInsideBlock() ? 0 : 1)) {
            event.boundingBox = null;
        }
        if (this.mc.thePlayer.isCollidedHorizontally && event.pos.getY() > this.mc.thePlayer.boundingBox.minY - 0.4) {
            event.boundingBox = null;
        }
    }
}
