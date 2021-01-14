package cn.kody.debug.mod.mods.RENDER;

import java.util.Iterator;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventRender;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Trajectory extends Mod
{
    private List<Vec3> cords;
    private float offset;
    public static Value<Double> delay;
    public TimeHelper timer;
    
    public Trajectory() {
        super("Trajectory", Category.RENDER);
        this.cords = new ArrayList<Vec3>();
        this.offset = 0.0f;
        this.timer = new TimeHelper();
    }
    
    @Override
    public void onDisable() {
        this.cords.clear();
    }
    
    @EventTarget
    public void onUpdate(final EventPacket eventPacket) {
        if (!eventPacket.isOutgoing() && eventPacket.packet instanceof S2APacketParticles) {
            final S2APacketParticles s2APacketParticles = (S2APacketParticles)eventPacket.packet;
            if (s2APacketParticles.getParticleType() == EnumParticleTypes.FLAME) {
                this.cords.add(new Vec3(s2APacketParticles.getXCoordinate(), s2APacketParticles.getYCoordinate(), s2APacketParticles.getZCoordinate()));
            }
        }
    }
    
    @EventTarget
    public void onUpdate3D(final EventRender class1170) {
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glBegin(3);
        for (final Vec3 class1171 : this.cords) {
            final double xCoord = class1171.xCoord;
            this.mc.getRenderManager();
            final double n = xCoord - RenderManager.renderPosX;
            final double yCoord = class1171.yCoord;
            this.mc.getRenderManager();
            final double n2 = yCoord - RenderManager.renderPosY;
            final double zCoord = class1171.zCoord;
            this.mc.getRenderManager();
            final double n3 = zCoord - RenderManager.renderPosZ;
            GL11.glColor4f(0.0f, 1.0f, 1.0f, 1.0f);
            GL11.glVertex3d(n, n2, n3);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        if (this.offset < 10.0f) {
            this.offset += 0.1f;
        }
        else {
            this.offset = 0.0f;
        }
        if (this.timer.delay(Trajectory.delay.getValueState() * 1000.0)) {
            this.cords.clear();
            this.timer.reset();
        }
    }
    
    public void enableGL3D(final float n) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(n);
    }
    
    public void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawBox(final AxisAlignedBB class2036) {
        if (class2036 == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.maxY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.maxY, class2036.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.minZ);
        GL11.glVertex3d(class2036.minX, class2036.minY, class2036.maxZ);
        GL11.glVertex3d(class2036.maxX, class2036.minY, class2036.maxZ);
        GL11.glEnd();
    }
    
    static {
        Trajectory.delay = new Value<Double>("Trajectory_Delay", 3.0, 0.5, 10.0, 0.5);
    }
}
