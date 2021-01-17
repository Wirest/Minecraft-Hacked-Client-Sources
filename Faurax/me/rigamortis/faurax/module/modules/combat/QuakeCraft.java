package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.friends.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.client.entity.*;
import me.rigamortis.faurax.events.*;
import java.util.regex.*;

public class QuakeCraft extends Module implements CombatHelper, PlayerHelper
{
    private float oldPitch;
    private float oldYaw;
    public int ticks;
    public int delay;
    private EntityPlayer target;
    public int buffer;
    private Map<EntityPlayer, List<Vec3>> playerPositions;
    
    public QuakeCraft() {
        this.buffer = 10;
        this.playerPositions = new HashMap<EntityPlayer, List<Vec3>>();
        this.setName("QuakeCraft");
        this.setKey("");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.oldPitch = QuakeCraft.mc.thePlayer.rotationPitch;
            this.oldYaw = QuakeCraft.mc.thePlayer.rotationYaw;
            double targetWeight = Double.NEGATIVE_INFINITY;
            this.target = null;
            for (final Object o : QuakeCraft.mc.theWorld.playerEntities) {
            	EntityPlayer p = (EntityPlayer)o;
                if (!p.equals(QuakeCraft.mc.thePlayer) && !FriendManager.isFriend(p.getName()) && QuakeCraft.mc.thePlayer.canEntityBeSeen(p)) {
                    if (this.target == null) {
                        this.target = p;
                        targetWeight = this.getTargetWeight(p);
                    }
                    else {
                        if (this.getTargetWeight(p) <= targetWeight) {
                            continue;
                        }
                        this.target = p;
                        targetWeight = this.getTargetWeight(p);
                    }
                }
            }
            for (final EntityPlayer player : this.playerPositions.keySet()) {
                if (!QuakeCraft.mc.theWorld.playerEntities.contains(player)) {
                    this.playerPositions.remove(player);
                }
            }
            for (final Object o : QuakeCraft.mc.theWorld.playerEntities) {
            	EntityPlayer player = (EntityPlayer)o;
                this.playerPositions.putIfAbsent(player, new ArrayList<Vec3>());
                final List<Vec3> previousPositions = this.playerPositions.get(player);
                previousPositions.add(new Vec3(player.posX, player.posY, player.posZ));
                if (previousPositions.size() > this.buffer) {
                    int i = 0;
                    for (final Vec3 position : new ArrayList<Vec3>(previousPositions)) {
                        if (i < previousPositions.size() - this.buffer) {
                            previousPositions.remove(previousPositions.get(i));
                        }
                        ++i;
                    }
                }
            }
            if (this.target != null) {
                ++this.delay;
                final Entity simulated = this.predictPlayerMovement(this.target);
                final float[] rotations = this.getPlayerRotations(QuakeCraft.mc.thePlayer, simulated.posX, simulated.posY + this.target.getEyeHeight() - 0.1, simulated.posZ);
                QuakeCraft.mc.thePlayer.rotationYaw = rotations[0];
                QuakeCraft.mc.thePlayer.rotationPitch = rotations[1] + 1.0f;
                if (this.delay >= 7 && QuakeCraft.mc.thePlayer.experienceLevel == 0) {
                    QuakeCraft.mc.playerController.sendUseItem(QuakeCraft.mc.thePlayer, QuakeCraft.mc.theWorld, QuakeCraft.mc.thePlayer.inventory.getCurrentItem());
                    this.delay = 0;
                }
            }
        }
    }
    
    public double getTargetWeight(final EntityPlayer p) {
        double weight = -QuakeCraft.mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 200.0;
        }
        weight -= p.getDistanceToEntity(QuakeCraft.mc.thePlayer) / 5.0f;
        return weight;
    }
    
    private Entity predictPlayerMovement(final EntityPlayer target) {
        final int pingTicks = (int)Math.ceil(QuakeCraft.mc.getNetHandler().func_175102_a(QuakeCraft.mc.thePlayer.getUniqueID()).getResponseTime() / 50.0);
        return this.predictPlayerLocation(target, pingTicks);
    }
    
    public Entity predictPlayerLocation(final EntityPlayer player, final int ticks) {
        if (this.playerPositions.containsKey(player)) {
            final List<Vec3> previousPositions = this.playerPositions.get(player);
            if (previousPositions.size() > 1) {
                final Vec3 origin = previousPositions.get(0);
                final List<Vec3> deltas = new ArrayList<Vec3>();
                Vec3 previous = origin;
                for (final Vec3 position : previousPositions) {
                    deltas.add(new Vec3(position.xCoord - previous.xCoord, position.yCoord - previous.yCoord, position.zCoord - previous.zCoord));
                    previous = position;
                }
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                for (final Vec3 delta : deltas) {
                    x += delta.xCoord;
                    y += delta.yCoord;
                    z += delta.zCoord;
                }
                x /= deltas.size();
                y /= deltas.size();
                z /= deltas.size();
                final EntityPlayer simulated = new EntityOtherPlayerMP(QuakeCraft.mc.theWorld, player.getGameProfile());
                simulated.noClip = false;
                simulated.setPosition(player.posX, player.posY, player.posZ);
                for (int i = 0; i < ticks; ++i) {
                    simulated.moveEntity(x, y, z);
                }
                return simulated;
            }
        }
        return player;
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            QuakeCraft.mc.thePlayer.rotationPitch = this.oldPitch;
            QuakeCraft.mc.thePlayer.rotationYaw = this.oldYaw;
        }
    }
    
    public static boolean isOnSameTeam(final EntityPlayer e, final EntityPlayer e2) {
        return e.getDisplayName().getFormattedText().contains("§" + getTeamFromName(e)) && e2.getDisplayName().getFormattedText().contains("§" + getTeamFromName(e));
    }
    
    public static String getTeamFromName(final Entity e) {
        final Matcher m = Pattern.compile("§(.).*§r").matcher(e.getDisplayName().getFormattedText());
        if (m.find()) {
            return m.group(1);
        }
        return "f";
    }
    
    private final float[] getPlayerRotations(final Entity player, final double x, final double y, final double z) {
        final double deltaX = x - player.posX;
        final double deltaY = y - player.posY - player.getEyeHeight() - 0.1;
        final double deltaZ = z - player.posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        final double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        yawToEntity = wrapAngleTo180((float)yawToEntity);
        pitchToEntity = wrapAngleTo180((float)pitchToEntity);
        return new float[] { (float)yawToEntity, (float)pitchToEntity };
    }
    
    private static float wrapAngleTo180(float angle) {
        for (angle %= 360.0f; angle >= 180.0f; angle -= 360.0f) {}
        while (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
}
