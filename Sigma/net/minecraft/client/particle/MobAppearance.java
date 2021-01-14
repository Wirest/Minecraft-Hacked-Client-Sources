package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MobAppearance extends EntityFX {
    private EntityLivingBase field_174844_a;
    private static final String __OBFID = "CL_00002594";

    protected MobAppearance(World worldIn, double p_i46283_2_, double p_i46283_4_, double p_i46283_6_) {
        super(worldIn, p_i46283_2_, p_i46283_4_, p_i46283_6_, 0.0D, 0.0D, 0.0D);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.particleGravity = 0.0F;
        this.particleMaxAge = 30;
    }

    public int getFXLayer() {
        return 3;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (this.field_174844_a == null) {
            EntityGuardian var1 = new EntityGuardian(this.worldObj);
            var1.func_175465_cm();
            this.field_174844_a = var1;
        }
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
        if (this.field_174844_a != null) {
            RenderManager var9 = Minecraft.getMinecraft().getRenderManager();
            var9.func_178628_a(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
            float var10 = 0.42553192F;
            float var11 = ((float) this.particleAge + p_180434_3_) / (float) this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(770, 771);
            float var12 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var12, var12);
            GlStateManager.pushMatrix();
            float var13 = 0.05F + 0.5F * MathHelper.sin(var11 * (float) Math.PI);
            GlStateManager.color(1.0F, 1.0F, 1.0F, var13);
            GlStateManager.translate(0.0F, 1.8F, 0.0F);
            GlStateManager.rotate(180.0F - p_180434_2_.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(60.0F - 150.0F * var11 - p_180434_2_.rotationPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.4F, -1.5F);
            GlStateManager.scale(var10, var10, var10);
            this.field_174844_a.rotationYaw = this.field_174844_a.prevRotationYaw = 0.0F;
            this.field_174844_a.rotationYawHead = this.field_174844_a.prevRotationYawHead = 0.0F;
            var9.renderEntityWithPosYaw(this.field_174844_a, 0.0D, 0.0D, 0.0D, 0.0F, p_180434_3_);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }

    public static class Factory implements IParticleFactory {
        private static final String __OBFID = "CL_00002593";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            return new MobAppearance(worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}
