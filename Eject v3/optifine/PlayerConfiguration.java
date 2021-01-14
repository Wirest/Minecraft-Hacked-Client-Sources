package optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfiguration {
    private PlayerItemModel[] playerItemModels = new PlayerItemModel[0];
    private boolean initialized = false;

    public void renderPlayerItems(ModelBiped paramModelBiped, AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2) {
        if (this.initialized) {
            for (int i = 0; i < this.playerItemModels.length; i++) {
                PlayerItemModel localPlayerItemModel = this.playerItemModels[i];
                localPlayerItemModel.render(paramModelBiped, paramAbstractClientPlayer, paramFloat1, paramFloat2);
            }
        }
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean paramBoolean) {
        this.initialized = paramBoolean;
    }

    public PlayerItemModel[] getPlayerItemModels() {
        return this.playerItemModels;
    }

    public void addPlayerItemModel(PlayerItemModel paramPlayerItemModel) {
        this.playerItemModels = ((PlayerItemModel[]) (PlayerItemModel[]) Config.addObjectToArray(this.playerItemModels, paramPlayerItemModel));
    }
}




