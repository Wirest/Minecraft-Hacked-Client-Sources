// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import java.util.List;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityZombie;

public class RenderZombie extends RenderBiped<EntityZombie>
{
    private static final ResourceLocation zombieTextures;
    private static final ResourceLocation zombieVillagerTextures;
    private final ModelBiped field_82434_o;
    private final ModelZombieVillager zombieVillagerModel;
    private final List<LayerRenderer<EntityZombie>> field_177121_n;
    private final List<LayerRenderer<EntityZombie>> field_177122_o;
    
    static {
        zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
        zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    }
    
    public RenderZombie(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelZombie(), 0.5f, 1.0f);
        final LayerRenderer layerrenderer = this.layerRenderers.get(0);
        this.field_82434_o = this.modelBipedMain;
        this.zombieVillagerModel = new ModelZombieVillager();
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        final LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.field_177189_c = new ModelZombie(0.5f, true);
                this.field_177186_d = new ModelZombie(1.0f, true);
            }
        };
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(layerbipedarmor);
        this.field_177122_o = (List<LayerRenderer<EntityZombie>>)Lists.newArrayList((Iterable<?>)this.layerRenderers);
        if (layerrenderer instanceof LayerCustomHead) {
            ((RendererLivingEntity<EntityLivingBase>)this).removeLayer(layerrenderer);
            ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
        }
        ((RendererLivingEntity<EntityLivingBase>)this).removeLayer(layerbipedarmor);
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerVillagerArmor(this));
        this.field_177121_n = (List<LayerRenderer<EntityZombie>>)Lists.newArrayList((Iterable<?>)this.layerRenderers);
    }
    
    @Override
    public void doRender(final EntityZombie entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.func_82427_a(entity);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityZombie entity) {
        return entity.isVillager() ? RenderZombie.zombieVillagerTextures : RenderZombie.zombieTextures;
    }
    
    private void func_82427_a(final EntityZombie zombie) {
        if (zombie.isVillager()) {
            this.mainModel = this.zombieVillagerModel;
            this.layerRenderers = (List<LayerRenderer<T>>)this.field_177121_n;
        }
        else {
            this.mainModel = this.field_82434_o;
            this.layerRenderers = (List<LayerRenderer<T>>)this.field_177122_o;
        }
        this.modelBipedMain = (ModelBiped)this.mainModel;
    }
    
    @Override
    protected void rotateCorpse(final EntityZombie bat, final float p_77043_2_, float p_77043_3_, final float partialTicks) {
        if (bat.isConverting()) {
            p_77043_3_ += (float)(Math.cos(bat.ticksExisted * 3.25) * 3.141592653589793 * 0.25);
        }
        super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
    }
}
