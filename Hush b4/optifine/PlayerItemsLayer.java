// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class PlayerItemsLayer implements LayerRenderer
{
    private RenderPlayer renderPlayer;
    
    public PlayerItemsLayer(final RenderPlayer p_i76_1_) {
        this.renderPlayer = null;
        this.renderPlayer = p_i76_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        this.renderEquippedItems(entitylivingbaseIn, scale, partialTicks);
    }
    
    protected void renderEquippedItems(final EntityLivingBase p_renderEquippedItems_1_, final float p_renderEquippedItems_2_, final float p_renderEquippedItems_3_) {
        if (Config.isShowCapes() && p_renderEquippedItems_1_ instanceof AbstractClientPlayer) {
            final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)p_renderEquippedItems_1_;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            final ModelBiped modelbiped = this.renderPlayer.getMainModel();
            PlayerConfigurations.renderPlayerItems(modelbiped, abstractclientplayer, p_renderEquippedItems_2_, p_renderEquippedItems_3_);
            GlStateManager.disableCull();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    public static void register(final Map p_register_0_) {
        final Set set = p_register_0_.keySet();
        boolean flag = false;
        for (final Object object : set) {
            final Object object2 = p_register_0_.get(object);
            if (object2 instanceof RenderPlayer) {
                final RenderPlayer renderplayer = (RenderPlayer)object2;
                ((RendererLivingEntity<EntityLivingBase>)renderplayer).addLayer(new PlayerItemsLayer(renderplayer));
                flag = true;
            }
        }
        if (!flag) {
            Config.warn("PlayerItemsLayer not registered");
        }
    }
}
