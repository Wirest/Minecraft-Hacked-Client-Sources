// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityMinecart;

public class RenderMinecart<T extends EntityMinecart> extends Render<T>
{
    private static final ResourceLocation minecartTextures;
    protected ModelBase modelMinecart;
    
    static {
        minecartTextures = new ResourceLocation("textures/entity/minecart.png");
    }
    
    public RenderMinecart(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.modelMinecart = new ModelMinecart();
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final T entity, double x, double y, double z, float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        long i = entity.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        final float f = (((i >> 16 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float f2 = (((i >> 20 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float f3 = (((i >> 24 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        GlStateManager.translate(f, f2, f3);
        final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        final double d4 = 0.30000001192092896;
        final Vec3 vec3 = entity.func_70489_a(d0, d2, d3);
        float f4 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        if (vec3 != null) {
            Vec3 vec4 = entity.func_70495_a(d0, d2, d3, d4);
            Vec3 vec5 = entity.func_70495_a(d0, d2, d3, -d4);
            if (vec4 == null) {
                vec4 = vec3;
            }
            if (vec5 == null) {
                vec5 = vec3;
            }
            x += vec3.xCoord - d0;
            y += (vec4.yCoord + vec5.yCoord) / 2.0 - d2;
            z += vec3.zCoord - d3;
            Vec3 vec6 = vec5.addVector(-vec4.xCoord, -vec4.yCoord, -vec4.zCoord);
            if (vec6.lengthVector() != 0.0) {
                vec6 = vec6.normalize();
                entityYaw = (float)(Math.atan2(vec6.zCoord, vec6.xCoord) * 180.0 / 3.141592653589793);
                f4 = (float)(Math.atan(vec6.yCoord) * 73.0);
            }
        }
        GlStateManager.translate((float)x, (float)y + 0.375f, (float)z);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-f4, 0.0f, 0.0f, 1.0f);
        final float f5 = entity.getRollingAmplitude() - partialTicks;
        float f6 = entity.getDamage() - partialTicks;
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f5 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0f * entity.getRollingDirection(), 1.0f, 0.0f, 0.0f);
        }
        final int j = entity.getDisplayTileOffset();
        final IBlockState iblockstate = entity.getDisplayTile();
        if (iblockstate.getBlock().getRenderType() != -1) {
            GlStateManager.pushMatrix();
            this.bindTexture(TextureMap.locationBlocksTexture);
            final float f7 = 0.75f;
            GlStateManager.scale(f7, f7, f7);
            GlStateManager.translate(-0.5f, (j - 8) / 16.0f, 0.5f);
            this.func_180560_a(entity, partialTicks, iblockstate);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.bindEntityTexture(entity);
        }
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelMinecart.render(entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final T entity) {
        return RenderMinecart.minecartTextures;
    }
    
    protected void func_180560_a(final T minecart, final float partialTicks, final IBlockState state) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, minecart.getBrightness(partialTicks));
        GlStateManager.popMatrix();
    }
}
