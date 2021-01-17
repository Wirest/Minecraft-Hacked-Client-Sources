// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.model.ModelBiped;

public class LayerBipedArmor extends LayerArmorBase<ModelBiped>
{
    public LayerBipedArmor(final RendererLivingEntity<?> rendererIn) {
        super(rendererIn);
    }
    
    @Override
    protected void initArmor() {
        this.field_177189_c = new ModelBiped(0.5f);
        this.field_177186_d = new ModelBiped(1.0f);
    }
    
    @Override
    protected void func_177179_a(final ModelBiped p_177179_1_, final int p_177179_2_) {
        this.func_177194_a(p_177179_1_);
        switch (p_177179_2_) {
            case 1: {
                p_177179_1_.bipedRightLeg.showModel = true;
                p_177179_1_.bipedLeftLeg.showModel = true;
                break;
            }
            case 2: {
                p_177179_1_.bipedBody.showModel = true;
                p_177179_1_.bipedRightLeg.showModel = true;
                p_177179_1_.bipedLeftLeg.showModel = true;
                break;
            }
            case 3: {
                p_177179_1_.bipedBody.showModel = true;
                p_177179_1_.bipedRightArm.showModel = true;
                p_177179_1_.bipedLeftArm.showModel = true;
                break;
            }
            case 4: {
                p_177179_1_.bipedHead.showModel = true;
                p_177179_1_.bipedHeadwear.showModel = true;
                break;
            }
        }
    }
    
    protected void func_177194_a(final ModelBiped p_177194_1_) {
        p_177194_1_.setInvisible(false);
    }
}
