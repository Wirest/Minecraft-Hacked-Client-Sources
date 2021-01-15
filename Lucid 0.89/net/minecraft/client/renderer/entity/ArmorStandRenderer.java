package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;

public class ArmorStandRenderer extends RendererLivingEntity
{
    /**
     * A constant instance of the armor stand texture, wrapped inside a ResourceLocation wrapper.
     */
    public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");

    public ArmorStandRenderer(RenderManager p_i46195_1_)
    {
        super(p_i46195_1_, new ModelArmorStand(), 0.0F);
        LayerBipedArmor var2 = new LayerBipedArmor(this)
        {
            @Override
			protected void func_177177_a()
            {
                this.field_177189_c = new ModelArmorStandArmor(0.5F);
                this.field_177186_d = new ModelArmorStandArmor(1.0F);
            }
        };
        this.addLayer(var2);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerCustomHead(this.func_177100_a().bipedHead));
    }

    /**
     * Provides an instance of the armor stand texture.
     *  
     * @param entityObj An instance of the EntityArmorStand, although this does not appear to be used.
     */
    protected ResourceLocation getArmorStandTexture(EntityArmorStand entityObj)
    {
        return TEXTURE_ARMOR_STAND;
    }

    public ModelArmorStand func_177100_a()
    {
        return (ModelArmorStand)super.getMainModel();
    }

    protected void func_177101_a(EntityArmorStand p_177101_1_, float p_177101_2_, float p_177101_3_, float p_177101_4_)
    {
        GlStateManager.rotate(180.0F - p_177101_3_, 0.0F, 1.0F, 0.0F);
    }

    protected boolean func_177099_b(EntityArmorStand p_177099_1_)
    {
        return p_177099_1_.getAlwaysRenderNameTag();
    }

    /**
     * Test if the entity name must be rendered
     */
    @Override
	protected boolean canRenderName(EntityLivingBase targetEntity)
    {
        return this.func_177099_b((EntityArmorStand)targetEntity);
    }

    @Override
	protected void rotateCorpse(EntityLivingBase bat, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        this.func_177101_a((EntityArmorStand)bat, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    @Override
	public ModelBase getMainModel()
    {
        return this.func_177100_a();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getArmorStandTexture((EntityArmorStand)entity);
    }

    @Override
	protected boolean canRenderName(Entity entity)
    {
        return this.func_177099_b((EntityArmorStand)entity);
    }
}
