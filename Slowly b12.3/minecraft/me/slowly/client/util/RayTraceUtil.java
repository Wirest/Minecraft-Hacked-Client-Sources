/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Vector3f
 */
package me.slowly.client.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;

public class RayTraceUtil {
    protected Minecraft mc = Minecraft.getMinecraft();
    private float startX;
    private float startY;
    private float startZ;
    private float endX;
    private float endY;
    private float endZ;
    private static final float MAX_STEP = 0.1f;
    private ArrayList<Vector3f> positions = new ArrayList();
    private EntityLivingBase entity;

    public RayTraceUtil(EntityLivingBase entity) {
        this.startX = (float)this.mc.thePlayer.posX;
        this.startY = (float)this.mc.thePlayer.posY + 1.0f;
        this.startZ = (float)this.mc.thePlayer.posZ;
        this.endX = (float)entity.posX;
        this.endY = (float)entity.posY + entity.height / 2.0f;
        this.endZ = (float)entity.posZ;
        this.entity = entity;
        this.positions.clear();
        this.addPositions();
    }

    private void addPositions() {
        float diffX = this.endX - this.startX;
        float diffY = this.endY - this.startY;
        float diffZ = this.endZ - this.startZ;
        float currentX = 0.0f;
        float currentY = 1.0f;
        float currentZ = 0.0f;
        int steps = (int)Math.max(Math.abs(diffX) / 0.1f, Math.max(Math.abs(diffY) / 0.1f, Math.abs(diffZ) / 0.1f));
        int i = 0;
        while (i <= steps) {
            this.positions.add(new Vector3f(currentX, currentY, currentZ));
            currentX += diffX / (float)steps;
            currentY += diffY / (float)steps;
            currentZ += diffZ / (float)steps;
            ++i;
        }
    }

    private boolean isInBox(Vector3f point, EntityLivingBase target) {
        boolean z;
        AxisAlignedBB box = target.getEntityBoundingBox();
        double posX = this.mc.thePlayer.posX + (double)point.x;
        double posY = this.mc.thePlayer.posY + (double)point.y;
        double posZ = this.mc.thePlayer.posZ + (double)point.z;
        boolean x = posX >= box.minX - 0.25 && posX <= box.maxX + 0.25;
        boolean y = posY >= box.minY && posY <= box.maxY;
        boolean bl = z = posZ >= box.minZ - 0.25 && posZ <= box.maxZ + 0.25;
        if (x && z && y) {
            return true;
        }
        return false;
    }

    public ArrayList<Vector3f> getPositions() {
        return this.positions;
    }

    public EntityLivingBase getEntity() {
        ArrayList possibleEntities = new ArrayList();
        double dist = this.mc.thePlayer.getDistanceToEntity(this.entity);
        EntityLivingBase entity = this.entity;
        for (Object o : this.mc.theWorld.loadedEntityList) {
            EntityLivingBase e;
            if (!(o instanceof EntityLivingBase) || (double)this.mc.thePlayer.getDistanceToEntity(e = (EntityLivingBase)o) >= dist || this.mc.thePlayer == e) continue;
            for (Vector3f vec : this.getPositions()) {
                if (!this.isInBox(vec, e) || this.mc.thePlayer.getDistanceToEntity(e) >= this.mc.thePlayer.getDistanceToEntity(entity)) continue;
                entity = e;
            }
        }
        return entity;
    }
}

