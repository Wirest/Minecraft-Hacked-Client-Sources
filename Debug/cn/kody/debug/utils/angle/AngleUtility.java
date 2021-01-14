/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.utils.angle;

import cn.kody.debug.utils.angle.Angle;
import java.util.Random;
import javax.vecmath.Vector3d;
import net.minecraft.entity.EntityLivingBase;

public class AngleUtility {
    private boolean aac;
    private float smooth;
    public static Random random;

    public AngleUtility(float smooth) {
        this.smooth = smooth;
        random = new Random();
    }
    
    public AngleUtility(boolean aac, float smooth) {
        this.aac = aac;
        this.smooth = smooth;
        random = new Random();
    }
    
    public Angle calculateAngle(Vector3d class1600, Vector3d class1601) {
	Angle class1602 = new Angle();
        class1600.x -= class1601.x;
        class1600.y -= class1601.y;
        class1600.z -= class1601.z;
        class1602.setYaw((float)(Math.atan2(class1600.z, class1600.x) * 57.29577951308232) - 90.0f);
        class1602.setPitch(-(float)(Math.atan2(class1600.y, Math.hypot(class1600.x, class1600.z)) * 57.29577951308232));
        return class1602.constrantAngle();
    }
    
    
    public Angle calculateAngle(Vector3d destination, Vector3d source, EntityLivingBase target) {
        Angle angles = new Angle();
        destination.x += (double)(this.aac ? AngleUtility.randomFloat(-0.75f, 0.75f) : 0.0f) - source.x;
        destination.y += (double)(this.aac ? AngleUtility.randomFloat(-0.25f, 0.5f) : 0.0f) - source.y;
        destination.z += (double)(this.aac ? AngleUtility.randomFloat(-0.75f, 0.75f) : 0.0f) - source.z;
        angles.setYaw((float)(Math.atan2(destination.z, destination.x) * 57.29577951308232) - 90.0f);
        double hypotenuse = Math.hypot(destination.x, destination.z);
        angles.setPitch(- (float)(Math.atan2(destination.y, hypotenuse) * 57.29577951308232));
        return angles.constrantAngle();
    }

    public static float angleDifference(float a, float b) {
        float c = Math.abs(a % 360.0f - b % 360.0f);
        c = Math.min(c, 360.0f - c);
        return c;
    }

    public Angle smoothAngle(Angle destination, Angle source) {
        Angle angles = new Angle(source.getYaw() - destination.getYaw(), source.getPitch() - destination.getPitch()).constrantAngle();
        angles.setYaw(source.getYaw() - angles.getYaw() / 100.0f * this.smooth);
        angles.setPitch(source.getPitch() - angles.getPitch() / 100.0f * this.smooth);
        return angles.constrantAngle();
    }

    public static float randomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }
}

