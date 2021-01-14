package net.minecraft.client.network;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;

public class NetworkPlayerInfo {
    private final GameProfile gameProfile;
    private WorldSettings.GameType gameType;

    /**
     * Player response time to server in milliseconds
     */
    private int responseTime;
    private boolean playerTexturesLoaded = false;
    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    private String skinType;
    private IChatComponent displayName;
    private int lastHealth = 0;
    private int displayHealth = 0;
    private long lastHealthTime = 0L;
    private long healthBlinkTime = 0L;
    private long renderVisibilityId = 0L;
    private static final String __OBFID = "CL_00000888";

    public NetworkPlayerInfo(GameProfile p_i46294_1_) {
        this.gameProfile = p_i46294_1_;
    }

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData p_i46295_1_) {
        this.gameProfile = p_i46295_1_.func_179962_a();
        this.gameType = p_i46295_1_.func_179960_c();
        this.responseTime = p_i46295_1_.func_179963_b();
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    protected void setGameType(WorldSettings.GameType p_178839_1_) {
        this.gameType = p_178839_1_;
    }

    protected void setResponseTime(int p_178838_1_) {
        this.responseTime = p_178838_1_;
    }

    public boolean hasLocationSkin() {
        return this.locationSkin != null;
    }

    public String getSkinType() {
        return this.skinType == null ? DefaultPlayerSkin.func_177332_b(this.gameProfile.getId()) : this.skinType;
    }

    public ResourceLocation getLocationSkin() {
        if (this.locationSkin == null) {
            this.loadPlayerTextures();
        }

        return (ResourceLocation) Objects.firstNonNull(this.locationSkin, DefaultPlayerSkin.func_177334_a(this.gameProfile.getId()));
    }

    public ResourceLocation getLocationCape() {
        if (this.locationCape == null) {
            this.loadPlayerTextures();
        }

        return this.locationCape;
    }

    public ScorePlayerTeam getPlayerTeam() {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(this.getGameProfile().getName());
    }

    protected void loadPlayerTextures() {
        synchronized (this) {
            if (!this.playerTexturesLoaded) {
                this.playerTexturesLoaded = true;
                Minecraft.getMinecraft().getSkinManager().func_152790_a(this.gameProfile, new SkinManager.SkinAvailableCallback() {
                    private static final String __OBFID = "CL_00002619";

                    public void skinAvailable(Type p_180521_1_, ResourceLocation p_180521_2_, MinecraftProfileTexture p_180521_3_) {
                        switch (NetworkPlayerInfo.SwitchType.field_178875_a[p_180521_1_.ordinal()]) {
                            case 1:
                                NetworkPlayerInfo.this.locationSkin = p_180521_2_;
                                NetworkPlayerInfo.this.skinType = p_180521_3_.getMetadata("model");

                                if (NetworkPlayerInfo.this.skinType == null) {
                                    NetworkPlayerInfo.this.skinType = "default";
                                }

                                break;

                            case 2:
                                NetworkPlayerInfo.this.locationCape = p_180521_2_;
                        }
                    }
                }, true);
            }
        }
    }

    public void setDisplayName(IChatComponent p_178859_1_) {
        this.displayName = p_178859_1_;
    }

    public IChatComponent getDisplayName() {
        return this.displayName;
    }

    public int getLastHealth() {
        return this.lastHealth;
    }

    public void setLastHealth(int p_178836_1_) {
        this.lastHealth = p_178836_1_;
    }

    public int getDisplayHealth() {
        return this.displayHealth;
    }

    public void setDisplayHealth(int p_178857_1_) {
        this.displayHealth = p_178857_1_;
    }

    public long getLastHealthTime() {
        return this.lastHealthTime;
    }

    public void getLastHealthTime(long p_178846_1_) {
        this.lastHealthTime = p_178846_1_;
    }

    public long getHealthBlinkTime() {
        return this.healthBlinkTime;
    }

    public void setHealthBlinkTime(long p_178844_1_) {
        this.healthBlinkTime = p_178844_1_;
    }

    public long getRenderVisibilityId() {
        return this.renderVisibilityId;
    }

    public void setRenderVisibilityId(long p_178843_1_) {
        this.renderVisibilityId = p_178843_1_;
    }

    static final class SwitchType {
        static final int[] field_178875_a = new int[Type.values().length];
        private static final String __OBFID = "CL_00002618";

        static {
            try {
                field_178875_a[Type.SKIN.ordinal()] = 1;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_178875_a[Type.CAPE.ordinal()] = 2;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
