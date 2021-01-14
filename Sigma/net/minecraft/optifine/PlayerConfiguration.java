package net.minecraft.optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfiguration {
    private PlayerItemModel[] playerItemModels = new PlayerItemModel[0];
    private boolean initialized = false;

    public void renderPlayerItems(ModelBiped modelBiped, AbstractClientPlayer player, float scale, float partialTicks) {
        if (initialized) {
            for (PlayerItemModel model : playerItemModels) {
                model.render(modelBiped, player, scale, partialTicks);
            }
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public PlayerItemModel[] getPlayerItemModels() {
        return playerItemModels;
    }

    public void addPlayerItemModel(PlayerItemModel playerItemModel) {
        playerItemModels = ((PlayerItemModel[]) Config.addObjectToArray(playerItemModels, playerItemModel));
    }
}
