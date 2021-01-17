package me.rigamortis.faurax.utils;

import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.pathfinder.*;
import java.util.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.pathfinding.*;

public class PathFind
{
    private Minecraft mc;
    public EntityPlayer pos;
    public PathFinder pathFinder;
    
    public PathFind(final String name) {
        this.mc = Minecraft.getMinecraft();
        this.pathFinder = new PathFinder(new WalkNodeProcessor());
        for (final Object i : this.mc.theWorld.loadedEntityList) {
            if (i instanceof EntityPlayer && i != null) {
                final EntityPlayer player = (EntityPlayer)i;
                if (!player.getName().contains(name)) {
                    continue;
                }
                this.pos = player;
            }
        }
        if (this.pos != null) {
            this.move();
            final float[] rot = this.getRotationTo(this.pos.getPositionVector());
            final EntityPlayerSP thePlayer = this.mc.thePlayer;
            thePlayer.rotationYaw += angleDifference(rot[0], this.mc.thePlayer.rotationYaw);
            final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
            thePlayer2.rotationPitch += angleDifference(rot[1], this.mc.thePlayer.rotationPitch);
        }
    }
    
    public void move() {
        if (this.mc.thePlayer.getDistance(this.pos.posX + 0.5, this.pos.posY + 0.5, this.pos.posZ + 0.5) > 0.3) {
            final PathEntity pe = this.pathFinder.func_176188_a(this.mc.theWorld, this.mc.thePlayer, this.pos, 40.0f);
            if (pe != null && pe.getCurrentPathLength() > 1) {
                final PathPoint point = pe.getPathPointFromIndex(1);
                final float[] rot = this.getRotationTo(new Vec3(point.xCoord + 0.5, point.yCoord + 0.5, point.zCoord + 0.5));
                this.mc.thePlayer.rotationYaw = rot[0];
                final EntityPlayerSP thePlayer = this.mc.thePlayer;
                final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                final double n = 0.0;
                thePlayer2.motionZ = n;
                thePlayer.motionX = n;
                final double offset = 0.26;
                final double newx = Math.sin(this.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f) * offset;
                final double newz = Math.cos(this.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f) * offset;
                final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
                thePlayer3.motionX -= newx;
                final EntityPlayerSP thePlayer4 = this.mc.thePlayer;
                thePlayer4.motionZ += newz;
                if (this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedHorizontally) {
                    this.mc.thePlayer.jump();
                }
            }
        }
    }
    
    public static float angleDifference(final float to, final float from) {
        return ((to - from) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public float[] getRotationTo(final Vec3 pos) {
        final double xD = this.mc.thePlayer.posX - pos.xCoord;
        final double yD = this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight() - pos.yCoord;
        final double zD = this.mc.thePlayer.posZ - pos.zCoord;
        final double yaw = Math.atan2(zD, xD);
        final double pitch = Math.atan2(yD, Math.sqrt(Math.pow(xD, 2.0) + Math.pow(zD, 2.0)));
        return new float[] { (float)Math.toDegrees(yaw) + 90.0f, (float)Math.toDegrees(pitch) };
    }
}
