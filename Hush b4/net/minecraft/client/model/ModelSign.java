// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

public class ModelSign extends ModelBase
{
    public ModelRenderer signBoard;
    public ModelRenderer signStick;
    
    public ModelSign() {
        (this.signBoard = new ModelRenderer(this, 0, 0)).addBox(-12.0f, -14.0f, -1.0f, 24, 12, 2, 0.0f);
        (this.signStick = new ModelRenderer(this, 0, 14)).addBox(-1.0f, -2.0f, -1.0f, 2, 14, 2, 0.0f);
    }
    
    public void renderSign() {
        this.signBoard.render(0.0625f);
        this.signStick.render(0.0625f);
    }
}
