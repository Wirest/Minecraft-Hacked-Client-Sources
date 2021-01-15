package com.ihl.client.comparator;

import net.minecraft.entity.EntityLivingBase;

import java.util.Comparator;

public class EntityHealthComparator implements Comparator<EntityLivingBase> {

    @Override
    public int compare(EntityLivingBase a, EntityLivingBase b) {
        double c = a.getHealth();
        double d = b.getHealth();

        return c < d ? -1 : c == d ? 0 : 1;
    }

}
