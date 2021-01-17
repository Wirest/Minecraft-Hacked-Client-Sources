// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfiguration
{
    private PlayerItemModel[] playerItemModels;
    private boolean initialized;
    
    public PlayerConfiguration() {
        this.playerItemModels = new PlayerItemModel[0];
        this.initialized = false;
    }
    
    public void renderPlayerItems(final ModelBiped p_renderPlayerItems_1_, final AbstractClientPlayer p_renderPlayerItems_2_, final float p_renderPlayerItems_3_, final float p_renderPlayerItems_4_) {
        if (this.initialized) {
            for (int i = 0; i < this.playerItemModels.length; ++i) {
                final PlayerItemModel playeritemmodel = this.playerItemModels[i];
                playeritemmodel.render(p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_, p_renderPlayerItems_4_);
            }
        }
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public void setInitialized(final boolean p_setInitialized_1_) {
        this.initialized = p_setInitialized_1_;
    }
    
    public PlayerItemModel[] getPlayerItemModels() {
        return this.playerItemModels;
    }
    
    public void addPlayerItemModel(final PlayerItemModel p_addPlayerItemModel_1_) {
        this.playerItemModels = (PlayerItemModel[])Config.addObjectToArray(this.playerItemModels, p_addPlayerItemModel_1_);
    }
}
