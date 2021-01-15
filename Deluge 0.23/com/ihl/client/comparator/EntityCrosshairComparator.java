package com.ihl.client.comparator;

import com.ihl.client.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.Comparator;

public class EntityCrosshairComparator implements Comparator<Entity> {

    private EntityLivingBase central;

    public EntityCrosshairComparator(EntityLivingBase central) {
        this.central = central;
    }

    @Override
    public int compare(Entity a, Entity b) {
        double[] rot = EntityUtil.getRotationToEntity(a);
        double aA = EntityUtil.getRotationDifference(rot);

        double[] prev = EntityUtil.getRotationToEntity(b);
        double bA = EntityUtil.getRotationDifference(prev);

        return aA < bA ? -1 : aA > bA ? 1 : 0;
    }

}
