package com.ihl.client.comparator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;

import java.util.Comparator;

public class TileEntityDistanceComparator implements Comparator<TileEntity> {

    private EntityLivingBase central;

    public TileEntityDistanceComparator(EntityLivingBase central) {
        this.central = central;
    }

    @Override
    public int compare(TileEntity a, TileEntity b) {
        double c = central.getDistanceSq(a.getPos().add(0.5, -0.5, 0.5));
        double d = central.getDistanceSq(b.getPos().add(0.5, -0.5, 0.5));

        return c < d ? -1 : c == d ? 0 : 1;
    }

}
