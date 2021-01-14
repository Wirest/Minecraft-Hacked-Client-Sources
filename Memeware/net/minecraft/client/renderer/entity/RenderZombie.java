package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderZombie extends RenderBiped {
    private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    private final ModelBiped field_82434_o;
    private final ModelZombieVillager zombieVillagerModel;
    private final List field_177121_n;
    private final List field_177122_o;
    private static final String __OBFID = "CL_00001037";

    public RenderZombie(RenderManager p_i46127_1_) {
        super(p_i46127_1_, new ModelZombie(), 0.5F, 1.0F);
        LayerRenderer var2 = (LayerRenderer) this.field_177097_h.get(0);
        this.field_82434_o = this.modelBipedMain;
        this.zombieVillagerModel = new ModelZombieVillager();
        this.addLayer(new LayerHeldItem(this));
        LayerBipedArmor var3 = new LayerBipedArmor(this) {
            private static final String __OBFID = "CL_00002429";

            protected void func_177177_a() {
                this.field_177189_c = new ModelZombie(0.5F, true);
                this.field_177186_d = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(var3);
        this.field_177122_o = Lists.newArrayList(this.field_177097_h);

        if (var2 instanceof LayerCustomHead) {
            this.func_177089_b(var2);
            this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
        }

        this.func_177089_b(var3);
        this.addLayer(new LayerVillagerArmor(this));
        this.field_177121_n = Lists.newArrayList(this.field_177097_h);
    }

    public void func_180579_a(EntityZombie p_180579_1_, double p_180579_2_, double p_180579_4_, double p_180579_6_, float p_180579_8_, float p_180579_9_) {
        this.func_82427_a(p_180579_1_);
        super.doRender(p_180579_1_, p_180579_2_, p_180579_4_, p_180579_6_, p_180579_8_, p_180579_9_);
    }

    protected ResourceLocation func_180578_a(EntityZombie p_180578_1_) {
        return p_180578_1_.isVillager() ? zombieVillagerTextures : zombieTextures;
    }

    private void func_82427_a(EntityZombie p_82427_1_) {
        if (p_82427_1_.isVillager()) {
            this.mainModel = this.zombieVillagerModel;
            this.field_177097_h = this.field_177121_n;
        } else {
            this.mainModel = this.field_82434_o;
            this.field_177097_h = this.field_177122_o;
        }

        this.modelBipedMain = (ModelBiped) this.mainModel;
    }

    protected void rotateCorpse(EntityZombie p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        if (p_77043_1_.isConverting()) {
            p_77043_3_ += (float) (Math.cos((double) p_77043_1_.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_) {
        return this.func_180578_a((EntityZombie) p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_180579_a((EntityZombie) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        this.rotateCorpse((EntityZombie) p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_180579_a((EntityZombie) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_180578_a((EntityZombie) p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_180579_a((EntityZombie) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
