// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import net.minecraft.util.MovingObjectPosition;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import me.nico.hush.Client;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class RayCast
{
    private Minecraft mc;
    
    public RayCast() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public Entity raycast(final Entity entiy) {
        final Entity var2 = Minecraft.thePlayer;
        final Vec3 var3 = entiy.getPositionVector().add(new Vec3(0.0, entiy.getEyeHeight(), 0.0));
        final Vec3 var4 = Minecraft.thePlayer.getPositionVector().add(new Vec3(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0));
        Vec3 var5 = null;
        final float var6 = 1.0f;
        final AxisAlignedBB a = Minecraft.thePlayer.getEntityBoundingBox().addCoord(var3.xCoord - var4.xCoord, var3.yCoord - var4.yCoord, var3.zCoord - var4.zCoord).expand(var6, var6, var6);
        final List var7 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, a);
        double var8 = Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble() + 0.5;
        Entity b = null;
        for (int var9 = 0; var9 < var7.size(); ++var9) {
            final Entity var10 = var7.get(var9);
            if (var10.canBeCollidedWith()) {
                final float var11 = var10.getCollisionBorderSize();
                final AxisAlignedBB var12 = var10.getEntityBoundingBox().expand(var11, var11, var11);
                final MovingObjectPosition var13 = var12.calculateIntercept(var4, var3);
                if (var12.isVecInside(var4)) {
                    if (0.0 < var8 || var8 == 0.0) {
                        b = var10;
                        var5 = ((var13 == null) ? var4 : var13.hitVec);
                        var8 = 0.0;
                    }
                }
                else if (var13 != null) {
                    final double var14 = var4.distanceTo(var13.hitVec);
                    if (var14 < var8 || var8 == 0.0) {
                        b = var10;
                        var5 = var13.hitVec;
                        var8 = var14;
                    }
                }
            }
        }
        return b;
    }
}
