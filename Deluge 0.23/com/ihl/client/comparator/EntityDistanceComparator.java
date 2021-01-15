package com.ihl.client.comparator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.Comparator;

public class EntityDistanceComparator implements Comparator<Entity> {

    private EntityLivingBase central;

    public EntityDistanceComparator(EntityLivingBase central) {
        this.central = central;
    }

    @Override
    public int compare(Entity a, Entity b) {
        double c = central.getDistanceToEntity(a);
        double d = central.getDistanceToEntity(b);

        return c < d ? -1 : c == d ? 0 : 1;
    }

}
