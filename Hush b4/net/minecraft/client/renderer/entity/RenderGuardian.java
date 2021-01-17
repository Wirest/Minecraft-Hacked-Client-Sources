// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityGuardian;

public class RenderGuardian extends RenderLiving<EntityGuardian>
{
    private static final ResourceLocation GUARDIAN_TEXTURE;
    private static final ResourceLocation GUARDIAN_ELDER_TEXTURE;
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE;
    int field_177115_a;
    
    static {
        GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
        GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");
        GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    }
    
    public RenderGuardian(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGuardian(), 0.5f);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }
    
    @Override
    public boolean shouldRender(final EntityGuardian livingEntity, final ICamera camera, final double camX, final double camY, final double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.hasTargetedEntity()) {
            final EntityLivingBase entitylivingbase = livingEntity.getTargetedEntity();
            if (entitylivingbase != null) {
                final Vec3 vec3 = this.func_177110_a(entitylivingbase, entitylivingbase.height * 0.5, 1.0f);
                final Vec3 vec4 = this.func_177110_a(livingEntity, livingEntity.getEyeHeight(), 1.0f);
                if (camera.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(vec4.xCoord, vec4.yCoord, vec4.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private Vec3 func_177110_a(final EntityLivingBase entityLivingBaseIn, final double p_177110_2_, final float p_177110_4_) {
        final double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * p_177110_4_;
        final double d2 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * p_177110_4_;
        final double d3 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * p_177110_4_;
        return new Vec3(d0, d2, d3);
    }
    
    @Override
    public void doRender(final EntityGuardian entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a()) {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        final EntityLivingBase entitylivingbase = entity.getTargetedEntity();
        if (entitylivingbase != null) {
            final float f = entity.func_175477_p(partialTicks);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(RenderGuardian.GUARDIAN_BEAM_TEXTURE);
            GL11.glTexParameterf(3553, 10242, 10497.0f);
            GL11.glTexParameterf(3553, 10243, 10497.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            final float f2 = 240.0f;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f2);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            final float f3 = entity.worldObj.getTotalWorldTime() + partialTicks;
            final float f4 = f3 * 0.5f % 1.0f;
            final float f5 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + f5, (float)z);
            final Vec3 vec3 = this.func_177110_a(entitylivingbase, entitylivingbase.height * 0.5, partialTicks);
            final Vec3 vec4 = this.func_177110_a(entity, f5, partialTicks);
            Vec3 vec5 = vec3.subtract(vec4);
            final double d0 = vec5.lengthVector() + 1.0;
            vec5 = vec5.normalize();
            final float f6 = (float)Math.acos(vec5.yCoord);
            final float f7 = (float)Math.atan2(vec5.zCoord, vec5.xCoord);
            GlStateManager.rotate((1.5707964f + -f7) * 57.295776f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(f6 * 57.295776f, 1.0f, 0.0f, 0.0f);
            final int i = 1;
            final double d2 = f3 * 0.05 * (1.0 - (i & 0x1) * 2.5);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            final float f8 = f * f;
            final int j = 64 + (int)(f8 * 240.0f);
            final int k = 32 + (int)(f8 * 192.0f);
            final int l = 128 - (int)(f8 * 64.0f);
            final double d3 = i * 0.2;
            final double d4 = d3 * 1.41;
            final double d5 = 0.0 + Math.cos(d2 + 2.356194490192345) * d4;
            final double d6 = 0.0 + Math.sin(d2 + 2.356194490192345) * d4;
            final double d7 = 0.0 + Math.cos(d2 + 0.7853981633974483) * d4;
            final double d8 = 0.0 + Math.sin(d2 + 0.7853981633974483) * d4;
            final double d9 = 0.0 + Math.cos(d2 + 3.9269908169872414) * d4;
            final double d10 = 0.0 + Math.sin(d2 + 3.9269908169872414) * d4;
            final double d11 = 0.0 + Math.cos(d2 + 5.497787143782138) * d4;
            final double d12 = 0.0 + Math.sin(d2 + 5.497787143782138) * d4;
            final double d13 = 0.0 + Math.cos(d2 + 3.141592653589793) * d3;
            final double d14 = 0.0 + Math.sin(d2 + 3.141592653589793) * d3;
            final double d15 = 0.0 + Math.cos(d2 + 0.0) * d3;
            final double d16 = 0.0 + Math.sin(d2 + 0.0) * d3;
            final double d17 = 0.0 + Math.cos(d2 + 1.5707963267948966) * d3;
            final double d18 = 0.0 + Math.sin(d2 + 1.5707963267948966) * d3;
            final double d19 = 0.0 + Math.cos(d2 + 4.71238898038469) * d3;
            final double d20 = 0.0 + Math.sin(d2 + 4.71238898038469) * d3;
            final double d21 = 0.0;
            final double d22 = 0.4999;
            final double d23 = -1.0f + f4;
            final double d24 = d0 * (0.5 / d3) + d23;
            worldrenderer.pos(d13, d0, d14).tex(0.4999, d24).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d13, 0.0, d14).tex(0.4999, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d15, 0.0, d16).tex(0.0, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d15, d0, d16).tex(0.0, d24).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d17, d0, d18).tex(0.4999, d24).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d17, 0.0, d18).tex(0.4999, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d19, 0.0, d20).tex(0.0, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d19, d0, d20).tex(0.0, d24).color(j, k, l, 255).endVertex();
            double d25 = 0.0;
            if (entity.ticksExisted % 2 == 0) {
                d25 = 0.5;
            }
            worldrenderer.pos(d5, d0, d6).tex(0.5, d25 + 0.5).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d7, d0, d8).tex(1.0, d25 + 0.5).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d11, d0, d12).tex(1.0, d25).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d9, d0, d10).tex(0.5, d25).color(j, k, l, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    protected void preRenderCallback(final EntityGuardian entitylivingbaseIn, final float partialTickTime) {
        if (entitylivingbaseIn.isElder()) {
            GlStateManager.scale(2.35f, 2.35f, 2.35f);
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityGuardian entity) {
        return entity.isElder() ? RenderGuardian.GUARDIAN_ELDER_TEXTURE : RenderGuardian.GUARDIAN_TEXTURE;
    }
}
