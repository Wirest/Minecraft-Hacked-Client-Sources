package me.Corbis.Execution.Replay;

import me.Corbis.Execution.Replay.Point;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;

public class ReplayLivingBase {

    EntityLivingBase base;
    ArrayList<Point> points;

    public ReplayLivingBase(EntityLivingBase base, ArrayList<Point> points) {
        this.base = base;
        this.points = points;
    }

    public EntityLivingBase getBase() {
        return base;
    }

    public void setBase(EntityLivingBase base) {
        this.base = base;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }
}
