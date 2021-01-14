package optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PlayerItemsLayer
        implements LayerRenderer {
    private RenderPlayer renderPlayer = null;

    public PlayerItemsLayer(RenderPlayer paramRenderPlayer) {
        this.renderPlayer = paramRenderPlayer;
    }

    public static void register(Map paramMap) {
        Set localSet = paramMap.keySet();
        int i = 0;
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            Object localObject1 = localIterator.next();
            Object localObject2 = paramMap.get(localObject1);
            if ((localObject2 instanceof RenderPlayer)) {
                RenderPlayer localRenderPlayer = (RenderPlayer) localObject2;
                localRenderPlayer.addLayer(new PlayerItemsLayer(localRenderPlayer));
                i = 1;
            }
        }
        if (i == 0) {
            Config.warn("PlayerItemsLayer not registered");
        }
    }

    public void doRenderLayer(EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7) {
        renderEquippedItems(paramEntityLivingBase, paramFloat7, paramFloat3);
    }

    protected void renderEquippedItems(EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2) {
        if ((Config.isShowCapes()) && ((paramEntityLivingBase instanceof AbstractClientPlayer))) {
            AbstractClientPlayer localAbstractClientPlayer = (AbstractClientPlayer) paramEntityLivingBase;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            ModelPlayer localModelPlayer = this.renderPlayer.getMainModel();
            PlayerConfigurations.renderPlayerItems(localModelPlayer, localAbstractClientPlayer, paramFloat1, paramFloat2);
            GlStateManager.disableCull();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}




