package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.friends.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.client.entity.*;
import me.rigamortis.faurax.events.*;
import java.util.regex.*;

public class CopsAndCrims extends Module implements CombatHelper, PlayerHelper
{
    private float oldPitch;
    private float oldYaw;
    public int ticks;
    public int lookDelay;
    private EntityPlayer target;
    public int buffer;
    private Map<EntityPlayer, List<Vec3>> playerPositions;
    public static Value delay;
    public static Value noSpread;
    public static Value RCS;
    public static Value silent;
    public static Value autoShoot;
    public static Value FOV;
    public static Value bone;
    public static Value rcsHorizontal;
    public static Value rcsVertical;
    
    static {
        CopsAndCrims.delay = new Value("CopsAndCrims", Float.TYPE, "Delay", 7.0f, 0.0f, 35.0f);
        CopsAndCrims.noSpread = new Value("CopsAndCrims", Boolean.TYPE, "noSpread", true);
        CopsAndCrims.RCS = new Value("CopsAndCrims", Boolean.TYPE, "RCS", false);
        CopsAndCrims.silent = new Value("CopsAndCrims", Boolean.TYPE, "Silent", true);
        CopsAndCrims.autoShoot = new Value("CopsAndCrims", Boolean.TYPE, "AutoShoot", true);
        CopsAndCrims.FOV = new Value("CopsAndCrims", Float.TYPE, "FOV", 360.0f, 1.0f, 360.0f);
        CopsAndCrims.bone = new Value("CopsAndCrims", String.class, "Bone", "Head", new String[] { "Head", "Neck", "Chest", "Jimmies", "Legs", "Feet" });
        CopsAndCrims.rcsHorizontal = new Value("CopsAndCrims", Float.TYPE, "HRecoil", 0.1f, 0.1f, 2.0f);
        CopsAndCrims.rcsVertical = new Value("CopsAndCrims", Float.TYPE, "VRecoil", 0.5f, 0.1f, 2.0f);
    }
    
    public CopsAndCrims() {
        this.buffer = 10;
        this.playerPositions = new HashMap<EntityPlayer, List<Vec3>>();
        this.setName("CopsAndCrims");
        this.setKey("");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.delay);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.noSpread);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.RCS);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.silent);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.autoShoot);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.FOV);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.bone);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.rcsHorizontal);
        Client.getValues();
        ValueManager.values.add(CopsAndCrims.rcsVertical);
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket e) {
        if (this.isToggled() && CopsAndCrims.RCS.getBooleanValue() && e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            ++this.ticks;
        }
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (CopsAndCrims.silent.getBooleanValue()) {
                this.oldPitch = CopsAndCrims.mc.thePlayer.rotationPitch;
                this.oldYaw = CopsAndCrims.mc.thePlayer.rotationYaw;
            }
            double targetWeight = Double.NEGATIVE_INFINITY;
            this.target = null;
            for (final Object o : CopsAndCrims.mc.theWorld.playerEntities) {
            	EntityPlayer p = (EntityPlayer)o;
                if (!p.equals(CopsAndCrims.mc.thePlayer) && !FriendManager.isFriend(p.getName()) && p.ticksExisted >= 40 && !p.isInvisible() && CopsAndCrims.mc.thePlayer.canEntityBeSeen(p) && !isOnSameTeam(p, CopsAndCrims.mc.thePlayer) && Client.getClientHelper().isVisibleFOV(p, CopsAndCrims.mc.thePlayer, CopsAndCrims.FOV.getFloatValue())) {
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
                if (!CopsAndCrims.mc.theWorld.playerEntities.contains(player)) {
                    this.playerPositions.remove(player);
                }
            }
            for (final Object o : CopsAndCrims.mc.theWorld.playerEntities) {
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
                if (CopsAndCrims.RCS.getBooleanValue() && this.ticks >= 30) {
                    this.ticks = 0;
                }
                ++this.lookDelay;
                final Entity simulated = this.predictPlayerMovement(this.target);
                float offset = 0.0f;
                if (CopsAndCrims.bone.getSelectedOption().equalsIgnoreCase("Head")) {
                    offset = -0.2f;
                }
                if (CopsAndCrims.bone.getSelectedOption().equalsIgnoreCase("Neck")) {
                    offset = 0.1f;
                }
                if (CopsAndCrims.bone.getSelectedOption().equalsIgnoreCase("Chest")) {
                    offset = 0.4f;
                }
                if (CopsAndCrims.bone.getSelectedOption().equalsIgnoreCase("Jimmies")) {
                    offset = 0.85f;
                }
                if (CopsAndCrims.bone.getSelectedOption().equalsIgnoreCase("Legs")) {
                    offset = 1.0f;
                }
                if (CopsAndCrims.bone.getSelectedOption().equalsIgnoreCase("Feet")) {
                    offset = 1.5f;
                }
                final float[] rotations = this.getPlayerRotations(CopsAndCrims.mc.thePlayer, simulated.posX, simulated.posY + this.target.getEyeHeight() - offset, simulated.posZ);
                if (CopsAndCrims.RCS.getBooleanValue()) {
                    CopsAndCrims.mc.thePlayer.rotationYaw = rotations[0];
                    CopsAndCrims.mc.thePlayer.rotationPitch = rotations[1] + CopsAndCrims.rcsVertical.getFloatValue() * this.ticks;
                    if (this.ticks >= 10) {
                        CopsAndCrims.mc.thePlayer.rotationYaw = rotations[0] - CopsAndCrims.rcsHorizontal.getFloatValue() * this.ticks;
                    }
                    if (this.ticks >= 20) {
                        CopsAndCrims.mc.thePlayer.rotationYaw = rotations[0] + CopsAndCrims.rcsHorizontal.getFloatValue() * this.ticks;
                    }
                }
                else {
                    CopsAndCrims.mc.thePlayer.rotationYaw = rotations[0];
                    CopsAndCrims.mc.thePlayer.rotationPitch = rotations[1];
                }
                if (this.lookDelay >= CopsAndCrims.delay.getFloatValue()) {
                    if (CopsAndCrims.noSpread.getBooleanValue()) {
                        CopsAndCrims.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(CopsAndCrims.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    if (CopsAndCrims.autoShoot.getBooleanValue()) {
                        CopsAndCrims.mc.playerController.sendUseItem(CopsAndCrims.mc.thePlayer, CopsAndCrims.mc.theWorld, CopsAndCrims.mc.thePlayer.inventory.getCurrentItem());
                    }
                    if (CopsAndCrims.noSpread.getBooleanValue()) {
                        CopsAndCrims.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(CopsAndCrims.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                    }
                    this.lookDelay = 0;
                }
            }
            else {
                --this.ticks;
                if (this.ticks <= 0) {
                    this.ticks = 0;
                }
            }
        }
    }
    
    public double getTargetWeight(final EntityPlayer p) {
        double weight = -CopsAndCrims.mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 200.0;
        }
        weight -= p.getDistanceToEntity(CopsAndCrims.mc.thePlayer) / 5.0f;
        return weight;
    }
    
    private Entity predictPlayerMovement(final EntityPlayer target) {
        final int pingTicks = (int)Math.ceil(CopsAndCrims.mc.getNetHandler().func_175102_a(CopsAndCrims.mc.thePlayer.getUniqueID()).getResponseTime() / 50.0);
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
                final EntityPlayer simulated = new EntityOtherPlayerMP(CopsAndCrims.mc.theWorld, player.getGameProfile());
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
        if (this.isToggled() && CopsAndCrims.silent.getBooleanValue()) {
            CopsAndCrims.mc.thePlayer.rotationPitch = this.oldPitch;
            CopsAndCrims.mc.thePlayer.rotationYaw = this.oldYaw;
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
