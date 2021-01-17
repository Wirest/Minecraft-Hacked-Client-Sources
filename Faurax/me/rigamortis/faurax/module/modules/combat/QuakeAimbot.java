package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import net.minecraft.entity.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class QuakeAimbot extends Module implements CombatHelper, PlayerHelper
{
    public int projectileSpeed;
    public int delay;
    
    public QuakeAimbot() {
        this.setName("QuakeAimbot");
        this.setKey("RSHIFT");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            final Entity entity = Client.getClientHelper().getBestEntity(110.0, 60.0f, true, 10, 1, 1, 1, 1);
            if (entity != null) {
                QuakeAimbot.mc.thePlayer.rotationYaw = this.getPlayerRotations(entity)[0];
                QuakeAimbot.mc.thePlayer.rotationPitch = this.getPlayerRotations(entity)[1];
            }
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        this.isToggled();
    }
    
    private final float[] getPlayerRotations(final Entity entity) {
        final double xDelta = entity.posX - entity.lastTickPosX;
        final double zDelta = entity.posZ - entity.lastTickPosZ;
        double d = QuakeAimbot.mc.thePlayer.getDistanceToEntity(entity);
        d -= d % 2.0;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 2.0 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 2.0 * zDelta * (sprint ? 1.25 : 1.0);
        final double x = entity.posX + xMulti - QuakeAimbot.mc.thePlayer.posX;
        final double z = entity.posZ + zMulti - QuakeAimbot.mc.thePlayer.posZ;
        final double y = QuakeAimbot.mc.thePlayer.posY + QuakeAimbot.mc.thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = QuakeAimbot.mc.thePlayer.getDistanceToEntity(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final float pitch = (float)Math.toDegrees(Math.atan2(y, dist));
        return new float[] { yaw, pitch };
    }
}
