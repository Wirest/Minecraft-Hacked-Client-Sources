package dev.astroclient.client.feature.impl.visuals;

import dev.astroclient.client.event.impl.render.EventRender3D;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.render.Render3DUtil;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

import java.awt.*;

@Toggleable(label = "ChestESP", category = Category.VISUALS, hidden = true)
public class ChestESP extends ToggleableFeature {

    public ColorProperty colorProperty = new ColorProperty("Color", true, new Color(89, 128, 230));
    public NumberProperty<Float> opacity = new NumberProperty<>("Opacity", true, 1F, .05F, .05F, 1F);

    @Subscribe
    public void onEvent(EventRender3D e) {
        for (Object o : mc.theWorld.loadedTileEntityList) {
            if (o instanceof TileEntityChest) {
                TileEntityLockable storage = (TileEntityLockable) o;
                this.drawESPOnStorage(storage, (double) storage.getPos().getX(), (double) storage.getPos().getY(), (double) storage.getPos().getZ());
            }
        }
    }

    private void drawESPOnStorage(TileEntityLockable storage, double x, double y, double z) {
        if (!storage.isLocked()) {
            TileEntityChest chest = (TileEntityChest) storage;
            Vec3 vec;
            Vec3 vec2;
            if (chest.adjacentChestZNeg != null) {
                vec = new Vec3(x + 0.0625D, y, z - 0.9375D);
                vec2 = new Vec3(x + 0.9375D, y + 0.875D, z + 0.9375D);
            } else if (chest.adjacentChestXNeg != null) {
                vec = new Vec3(x + 0.9375D, y, z + 0.0625D);
                vec2 = new Vec3(x - 0.9375D, y + 0.875D, z + 0.9375D);
            } else {
                if (chest.adjacentChestXPos != null || chest.adjacentChestZPos != null) {
                    return;
                }

                vec = new Vec3(x + 0.0625D, y, z + 0.0625D);
                vec2 = new Vec3(x + 0.9375D, y + 0.875D, z + 0.9375D);
            }

            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);

            mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);

            GL11.glColor4d(colorProperty.getColor().getRed() / 255.0F, colorProperty.getColor().getGreen() / 255.0F, colorProperty.getColor().getBlue() / 255.0F, opacity.getValue());

            Render3DUtil.drawBoundingBox(new AxisAlignedBB(vec.xCoord - mc.getRenderManager().renderPosX, vec.yCoord - mc.getRenderManager().renderPosY, vec.zCoord - mc.getRenderManager().renderPosZ, vec2.xCoord - mc.getRenderManager().renderPosX, vec2.yCoord - mc.getRenderManager().renderPosY, vec2.zCoord - mc.getRenderManager().renderPosZ));

            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glColor4f(1f, 1f, 1f, 1f);
        }
    }
}
