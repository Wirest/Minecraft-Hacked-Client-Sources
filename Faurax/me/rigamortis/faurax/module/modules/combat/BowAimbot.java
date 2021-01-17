package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import net.minecraft.entity.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class BowAimbot extends Module implements CombatHelper, PlayerHelper
{
    private int projectileSpeed;
    private float oldPitch;
    private float oldYaw;
    
    public BowAimbot() {
        this.setName("BowAimbot");
        this.setKey("");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.oldPitch = BowAimbot.mc.thePlayer.rotationPitch;
            this.oldYaw = BowAimbot.mc.thePlayer.rotationYaw;
            if (BowAimbot.mc.thePlayer.isUsingItem()) {
                final Entity entity = Client.getClientHelper().getBestEntity(45.0, 360.0f, true, 1, 1, 1, 0, 0);
                if (entity != null) {
                    BowAimbot.mc.thePlayer.rotationYaw = this.getPlayerRotations(entity)[0];
                    BowAimbot.mc.thePlayer.rotationPitch = this.getPlayerRotations(entity)[1];
                    this.setModInfo(" §7" + entity.getName());
                }
                else {
                    this.setModInfo(" §7None");
                }
            }
        }
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            BowAimbot.mc.thePlayer.rotationPitch = this.oldPitch;
            BowAimbot.mc.thePlayer.rotationYaw = this.oldYaw;
        }
    }
    
    private final float[] getPlayerRotations(final Entity entity) {
        final double xDelta = entity.posX - entity.lastTickPosX;
        final double zDelta = entity.posZ - entity.lastTickPosZ;
        double d = BowAimbot.mc.thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        final double x = entity.posX + xMulti - BowAimbot.mc.thePlayer.posX;
        final double z = entity.posZ + zMulti - BowAimbot.mc.thePlayer.posZ;
        final double y = BowAimbot.mc.thePlayer.posY + BowAimbot.mc.thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = BowAimbot.mc.thePlayer.getDistanceToEntity(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final float pitch = (float)Math.toDegrees(Math.atan2(y, dist));
        return new float[] { yaw, pitch };
    }
}
