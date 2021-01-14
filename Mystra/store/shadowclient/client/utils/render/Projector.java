package store.shadowclient.client.utils.render;

import java.nio.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import org.lwjgl.*;
import org.lwjgl.util.glu.*;
import net.minecraft.entity.*;
import org.lwjgl.util.vector.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import org.apache.logging.log4j.*;

public class Projector
{
    private static final FloatBuffer MODEL_VIEW_MATRIX;
    private static final FloatBuffer PROJECTION_MATRIX;
    private static final IntBuffer VIEWPORT;
    private static final Logger logger;
    
    public Vector2f project(final float n, final float n2, final float n3) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        init();
        final Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        final float n4 = (float)interpolate(renderViewEntity.prevPosX, renderViewEntity.posX);
        final float n5 = (float)interpolate(renderViewEntity.prevPosY, renderViewEntity.posY);
        final float n6 = (float)interpolate(renderViewEntity.prevPosZ, renderViewEntity.posZ);
        final FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        if (GLU.gluProject(n - n4, n2 - n5, n3 - n6, Projector.MODEL_VIEW_MATRIX, Projector.PROJECTION_MATRIX, Projector.VIEWPORT, floatBuffer)) {
            return new Vector2f(floatBuffer.get(0) / scaledResolution.getScaleFactor(), (Display.getHeight() - floatBuffer.get(1)) / scaledResolution.getScaleFactor());
        }
        Projector.logger.error("Failed to project coordinates " + n + ", " + n2 + ", " + n3);
        return new Vector2f(0.0f, 0.0f);
    }
    
    public Vector2f project(final Entity entity, final Vector3f vector3f) {
        return this.project((float)interpolate(entity.prevPosX, entity.posX) + vector3f.x, (float)interpolate(entity.prevPosY, entity.posY) + vector3f.y, (float)interpolate(entity.prevPosZ, entity.posZ) + vector3f.z);
    }
    
    public Vector2f project(final Entity entity) {
        return this.project((float)interpolate(entity.prevPosX, entity.posX), (float)interpolate(entity.prevPosY, entity.posY), (float)interpolate(entity.prevPosZ, entity.posZ));
    }
    
    public Vector2f[] projectFull(final Entity entity) {
        return this.projectFull(new Vector3f((float)interpolate(entity.prevPosX, entity.posX), (float)interpolate(entity.posY, entity.posY), (float)interpolate(entity.prevPosZ, entity.posZ)), entity.getEntityBoundingBox());
    }
    
    public Vector2f[] projectFull(final Vector3f vector3f, final AxisAlignedBB axisAlignedBB) {
        final Vector2f[] array = new Vector2f[8];
        final float x = vector3f.x;
        final float y = vector3f.y;
        final float z = vector3f.z;
        final float n = (float)axisAlignedBB.minX;
        final float n2 = (float)axisAlignedBB.minY;
        final float n3 = (float)axisAlignedBB.minZ;
        final float n4 = (float)axisAlignedBB.maxX;
        final float n5 = (float)axisAlignedBB.maxY;
        final float n6 = (float)axisAlignedBB.maxZ;
        final float n7 = (n4 - n) / 2.0f;
        final float n8 = n5 - n2;
        final float n9 = (n6 - n3) / 2.0f;
        array[0] = this.project(x + n7, y + n8, z + n9);
        array[1] = this.project(x - n7, y, z + n9);
        array[2] = this.project(x - n7, y + n8, z + n9);
        array[3] = this.project(x + n7, y, z + n7);
        array[4] = this.project(x + n7, y + n8, z - n9);
        array[5] = this.project(x - n7, y, z - n9);
        array[6] = this.project(x - n7, y + n8, z - n9);
        array[7] = this.project(x + n7, y, z - n9);
        return array;
    }
    
    public static void init() {
        GlStateManager.getFloat(2982, Projector.MODEL_VIEW_MATRIX);
        GlStateManager.getFloat(2983, Projector.PROJECTION_MATRIX);
        GL11.glGetInteger(2978, Projector.VIEWPORT);
    }
    
    public static double interpolate(final double n, final double n2) {
        return n + (n2 - n) * Minecraft.getMinecraft().timer.renderPartialTicks;
    }
    
    static {
        MODEL_VIEW_MATRIX = BufferUtils.createFloatBuffer(16);
        PROJECTION_MATRIX = BufferUtils.createFloatBuffer(16);
        VIEWPORT = BufferUtils.createIntBuffer(16);
        logger = LogManager.getLogger((Class)Projector.class);
    }
}
