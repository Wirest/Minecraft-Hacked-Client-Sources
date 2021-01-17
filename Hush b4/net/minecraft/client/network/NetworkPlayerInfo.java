// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.base.Objects;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import com.mojang.authlib.GameProfile;

public class NetworkPlayerInfo
{
    private final GameProfile gameProfile;
    private WorldSettings.GameType gameType;
    private int responseTime;
    private boolean playerTexturesLoaded;
    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    private String skinType;
    private IChatComponent displayName;
    private int field_178873_i;
    private int field_178870_j;
    private long field_178871_k;
    private long field_178868_l;
    private long field_178869_m;
    
    public NetworkPlayerInfo(final GameProfile p_i46294_1_) {
        this.playerTexturesLoaded = false;
        this.field_178873_i = 0;
        this.field_178870_j = 0;
        this.field_178871_k = 0L;
        this.field_178868_l = 0L;
        this.field_178869_m = 0L;
        this.gameProfile = p_i46294_1_;
    }
    
    public NetworkPlayerInfo(final S38PacketPlayerListItem.AddPlayerData p_i46295_1_) {
        this.playerTexturesLoaded = false;
        this.field_178873_i = 0;
        this.field_178870_j = 0;
        this.field_178871_k = 0L;
        this.field_178868_l = 0L;
        this.field_178869_m = 0L;
        this.gameProfile = p_i46295_1_.getProfile();
        this.gameType = p_i46295_1_.getGameMode();
        this.responseTime = p_i46295_1_.getPing();
        this.displayName = p_i46295_1_.getDisplayName();
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
    
    protected void setGameType(final WorldSettings.GameType p_178839_1_) {
        this.gameType = p_178839_1_;
    }
    
    protected void setResponseTime(final int p_178838_1_) {
        this.responseTime = p_178838_1_;
    }
    
    public boolean hasLocationSkin() {
        return this.locationSkin != null;
    }
    
    public String getSkinType() {
        return (this.skinType == null) ? DefaultPlayerSkin.getSkinType(this.gameProfile.getId()) : this.skinType;
    }
    
    public ResourceLocation getLocationSkin() {
        if (this.locationSkin == null) {
            this.loadPlayerTextures();
        }
        return Objects.firstNonNull(this.locationSkin, DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
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
                Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, new SkinManager.SkinAvailableCallback() {
                    @Override
                    public void skinAvailable(final MinecraftProfileTexture.Type p_180521_1_, final ResourceLocation location, final MinecraftProfileTexture profileTexture) {
                        switch (p_180521_1_) {
                            case SKIN: {
                                NetworkPlayerInfo.access$0(NetworkPlayerInfo.this, location);
                                NetworkPlayerInfo.access$1(NetworkPlayerInfo.this, profileTexture.getMetadata("model"));
                                if (NetworkPlayerInfo.this.skinType == null) {
                                    NetworkPlayerInfo.access$1(NetworkPlayerInfo.this, "default");
                                    break;
                                }
                                break;
                            }
                            case CAPE: {
                                NetworkPlayerInfo.access$3(NetworkPlayerInfo.this, location);
                                break;
                            }
                        }
                    }
                }, true);
            }
        }
    }
    
    public void setDisplayName(final IChatComponent displayNameIn) {
        this.displayName = displayNameIn;
    }
    
    public IChatComponent getDisplayName() {
        return this.displayName;
    }
    
    public int func_178835_l() {
        return this.field_178873_i;
    }
    
    public void func_178836_b(final int p_178836_1_) {
        this.field_178873_i = p_178836_1_;
    }
    
    public int func_178860_m() {
        return this.field_178870_j;
    }
    
    public void func_178857_c(final int p_178857_1_) {
        this.field_178870_j = p_178857_1_;
    }
    
    public long func_178847_n() {
        return this.field_178871_k;
    }
    
    public void func_178846_a(final long p_178846_1_) {
        this.field_178871_k = p_178846_1_;
    }
    
    public long func_178858_o() {
        return this.field_178868_l;
    }
    
    public void func_178844_b(final long p_178844_1_) {
        this.field_178868_l = p_178844_1_;
    }
    
    public long func_178855_p() {
        return this.field_178869_m;
    }
    
    public void func_178843_c(final long p_178843_1_) {
        this.field_178869_m = p_178843_1_;
    }
    
    static /* synthetic */ void access$0(final NetworkPlayerInfo networkPlayerInfo, final ResourceLocation locationSkin) {
        networkPlayerInfo.locationSkin = locationSkin;
    }
    
    static /* synthetic */ void access$1(final NetworkPlayerInfo networkPlayerInfo, final String skinType) {
        networkPlayerInfo.skinType = skinType;
    }
    
    static /* synthetic */ void access$3(final NetworkPlayerInfo networkPlayerInfo, final ResourceLocation locationCape) {
        networkPlayerInfo.locationCape = locationCape;
    }
}
