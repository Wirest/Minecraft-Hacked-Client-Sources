package info.sigmaclient.management.waypoints;

import net.minecraft.util.Vec3;

/**
 * Created by Arithmo on 5/8/2017 at 2:47 PM.
 */
public class Waypoint {

    private String name;
    private Vec3 vec3;

    private int color;

    private String address;

    public Waypoint(String name, Vec3 vec3, int color, String address) {
        this.name = name;
        this.vec3 = vec3;
        this.color = color;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Vec3 getVec3() {
        return vec3;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVec3(Vec3 vec3) {
        this.vec3 = vec3;
    }

    public void setVec3(double x, double y, double z) {
        this.vec3 = new Vec3(x, y, z);
    }

    public int getColor() {
        return color;
    }

    public String getAddress() {
        return address;
    }

}
