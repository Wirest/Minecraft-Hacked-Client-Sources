// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class EntityPickupFX extends EntityFX
{
    private Entity field_174840_a;
    private Entity field_174843_ax;
    private int age;
    private int maxAge;
    private float field_174841_aA;
    private RenderManager field_174842_aB;
    
    public EntityPickupFX(final World worldIn, final Entity p_i1233_2_, final Entity p_i1233_3_, final float p_i1233_4_) {
        super(worldIn, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
        this.field_174842_aB = Minecraft.getMinecraft().getRenderManager();
        this.field_174840_a = p_i1233_2_;
        this.field_174843_ax = p_i1233_3_;
        this.maxAge = 3;
        this.field_174841_aA = p_i1233_4_;
    }
    
    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float f = (this.age + partialTicks) / this.maxAge;
        f *= f;
        final double d0 = this.field_174840_a.posX;
        final double d2 = this.field_174840_a.posY;
        final double d3 = this.field_174840_a.posZ;
        final double d4 = this.field_174843_ax.lastTickPosX + (this.field_174843_ax.posX - this.field_174843_ax.lastTickPosX) * partialTicks;
        final double d5 = this.field_174843_ax.lastTickPosY + (this.field_174843_ax.posY - this.field_174843_ax.lastTickPosY) * partialTicks + this.field_174841_aA;
        final double d6 = this.field_174843_ax.lastTickPosZ + (this.field_174843_ax.posZ - this.field_174843_ax.lastTickPosZ) * partialTicks;
        double d7 = d0 + (d4 - d0) * f;
        double d8 = d2 + (d5 - d2) * f;
        double d9 = d3 + (d6 - d3) * f;
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        d7 -= EntityPickupFX.interpPosX;
        d8 -= EntityPickupFX.interpPosY;
        d9 -= EntityPickupFX.interpPosZ;
        this.field_174842_aB.renderEntityWithPosYaw(this.field_174840_a, (float)d7, (float)d8, (float)d9, this.field_174840_a.rotationYaw, partialTicks);
    }
    
    @Override
    public void onUpdate() {
        ++this.age;
        if (this.age == this.maxAge) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
}
