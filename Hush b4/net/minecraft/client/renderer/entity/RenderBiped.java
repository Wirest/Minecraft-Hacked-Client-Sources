// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLiving;

public class RenderBiped<T extends EntityLiving> extends RenderLiving<T>
{
    private static final ResourceLocation DEFAULT_RES_LOC;
    protected ModelBiped modelBipedMain;
    protected float field_77070_b;
    
    static {
        DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");
    }
    
    public RenderBiped(final RenderManager renderManagerIn, final ModelBiped modelBipedIn, final float shadowSize) {
        this(renderManagerIn, modelBipedIn, shadowSize, 1.0f);
        this.addLayer(new LayerHeldItem(this));
    }
    
    public RenderBiped(final RenderManager renderManagerIn, final ModelBiped modelBipedIn, final float shadowSize, final float p_i46169_4_) {
        super(renderManagerIn, modelBipedIn, shadowSize);
        this.modelBipedMain = modelBipedIn;
        this.field_77070_b = p_i46169_4_;
        this.addLayer(new LayerCustomHead(modelBipedIn.bipedHead));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final T entity) {
        return RenderBiped.DEFAULT_RES_LOC;
    }
    
    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
}
