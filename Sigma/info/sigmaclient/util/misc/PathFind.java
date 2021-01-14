/**
 * Time: 10:01:07 PM
 * Date: Jan 4, 2017
 * Creator: cool1
 */
package info.sigmaclient.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathFind {
    public EntityPlayer pos;
    public PathFinder pathFinder;
    private float yaw;
    public static float fakeYaw;
    public static float fakePitch;

    public PathFind(final String name) {
        this.pathFinder = new PathFinder(new WalkNodeProcessor());
        for (final Object i : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (i instanceof EntityPlayer && i != null) {
                final EntityPlayer player = (EntityPlayer) i;
                if (!player.getName().contains(name)) {
                    continue;
                }
                this.pos = player;
            }
        }
        if (this.pos != null) {
            this.move();
            final float[] rot = this.getRotationTo(this.pos.getPositionVector());
            PathFind.fakeYaw = rot[0];
            PathFind.fakePitch = rot[1];
        }
    }

    public void move() {
        if (Minecraft.getMinecraft().thePlayer.getDistance(this.pos.posX + 0.5, this.pos.posY + 0.5, this.pos.posZ + 0.5) > 0.3) {
            final PathEntity pe = this.pathFinder.func_176188_a(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer, this.pos, 40.0f);
            if (pe != null && pe.getCurrentPathLength() > 1) {
                final PathPoint point = pe.getPathPointFromIndex(1);
                final float[] rot = this.getRotationTo(new Vec3(point.xCoord + 0.5, point.yCoord + 0.5, point.zCoord + 0.5));
                this.yaw = rot[0];
                final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                final EntityPlayerSP player2 = Minecraft.getMinecraft().thePlayer;
                final double n = 0.0;
                player2.motionZ = n;
                player.motionX = n;
                final double offset = 0.26;
                final double newx = Math.sin(this.yaw * 3.1415927f / 180.0f) * offset;
                final double newz = Math.cos(this.yaw * 3.1415927f / 180.0f) * offset;
                final EntityPlayerSP player3 = Minecraft.getMinecraft().thePlayer;
                player3.motionX -= newx;
                final EntityPlayerSP player4 = Minecraft.getMinecraft().thePlayer;
                player4.motionZ += newz;
                if (Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.isCollidedHorizontally) {
                    Minecraft.getMinecraft().thePlayer.jump();
                }
            }
        }
    }

    public static float angleDifference(final float to, final float from) {
        return ((to - from) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public float[] getRotationTo(final Vec3 pos) {
        final double xD = Minecraft.getMinecraft().thePlayer.posX - pos.xCoord;
        final double yD = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - pos.yCoord;
        final double zD = Minecraft.getMinecraft().thePlayer.posZ - pos.zCoord;
        final double yaw = Math.atan2(zD, xD);
        final double pitch = Math.atan2(yD, Math.sqrt(Math.pow(xD, 2.0) + Math.pow(zD, 2.0)));
        return new float[]{(float) Math.toDegrees(yaw) + 90.0f, (float) Math.toDegrees(pitch)};
    }
}

