package net.minecraft.client.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.HashMap;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import info.sigmaclient.Client;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.module.impl.movement.Sprint;
import info.sigmaclient.module.impl.other.StreamerMode;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.optifine.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public abstract class AbstractClientPlayer extends EntityPlayer {
    private NetworkPlayerInfo field_175157_a;
    private ResourceLocation ofLocationCape = null;
    private static final String __OBFID = "CL_00000935";
    public AbstractClientPlayer(World worldIn, GameProfile p_i45074_2_) {
        super(worldIn, p_i45074_2_);
        String username = p_i45074_2_.getName();
        this.downloadCape(username);
    }

    public boolean func_175149_v() {
        NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getGameProfile().getId());
        return (var1 != null && var1.getGameType() == WorldSettings.GameType.SPECTATOR);
    }

    public boolean hasCape() {
        return this.func_175155_b() != null;
    }

    protected NetworkPlayerInfo func_175155_b() {
        if (this.field_175157_a == null) {
            this.field_175157_a = Minecraft.getMinecraft().getNetHandler().func_175102_a(this.getUniqueID());
        }

        return this.field_175157_a;
    }

    public boolean hasSkin() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 != null && var1.hasLocationSkin();
    }

    public ResourceLocation getLocationSkin() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        if(var1 == null || Client.getModuleManager().isEnabled(StreamerMode.class) && StreamerMode.spoofSkins)
        	return DefaultPlayerSkin.func_177334_a(this.getUniqueID());
        return var1.getLocationSkin();
    }

    public ResourceLocation getLocationCape() {
        if (!Config.isShowCapes()) {
            return null;
        } else if (this.ofLocationCape != null) {
            return this.ofLocationCape;
        } else {
            NetworkPlayerInfo var1 = this.func_175155_b();
            return var1 == null ? null : var1.getLocationCape();
        }
    }

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
        Object var3 = var2.getTexture(resourceLocationIn);

        if (var3 == null) {
            var3 = new ThreadDownloadImageData((File) null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[]{StringUtils.stripControlCodes(username)}), DefaultPlayerSkin.func_177334_a(func_175147_b(username)), new ImageBufferDownload());
            var2.loadTexture(resourceLocationIn, (ITextureObject) var3);
        }

        return (ThreadDownloadImageData) var3;
    }

    public static ResourceLocation getLocationSkin(String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }

    public String func_175154_l() {
        NetworkPlayerInfo var1 = this.func_175155_b();
        return var1 == null ? DefaultPlayerSkin.func_177332_b(this.getUniqueID()) : var1.getSkinType();
    }

    public float func_175156_o() {
        float var1 = 1.0F;

        if (this.capabilities.isFlying) {
            var1 *= 1.1F;
        }
        //TODO:UPDATEFOV
        IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        Module sprint = Client.getModuleManager().get(Sprint.class);
        boolean sprintXD = (Client.getModuleManager().get(Sprint.class).isEnabled() ? (PlayerUtil.isMoving() && getFoodStats().getFoodLevel() > 6 && ((Sprint.backward) || (!Sprint.backward && mc.thePlayer.moveForward>0))) : isSprinting());
        var1 = (float) ((double) var1 * (((sprintXD ? 0.13000000312924387 : 0.10000000149011612) / (double) this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
        if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1) || Float.isInfinite(var1)) {
            var1 = 1.0F;
        }

        if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
            int var3 = this.getItemInUseDuration();
            float var4 = (float) var3 / 20.0F;

            if (var4 > 1.0F) {
                var4 = 1.0F;
            } else {
                var4 *= var4;
            }

            var1 *= 1.0F - var4 * 0.15F;
        }
        return var1;
    }

    private void downloadCape(String username) {
        if (username != null && !username.isEmpty()) {
            username = StringUtils.stripControlCodes(username);
            String ofCapeUrl = "http://s.optifine.net/capes/" + username + ".png";
            MinecraftProfileTexture mpt = new MinecraftProfileTexture(ofCapeUrl, new HashMap());
            final ResourceLocation rl = new ResourceLocation("skins/" + mpt.getHash());
            IImageBuffer iib = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage parseUserSkin(BufferedImage var1) {
                    return AbstractClientPlayer.this.parseCape(var1);
                }

                public void func_152634_a() {
                    AbstractClientPlayer.this.ofLocationCape = rl;
                }
            };
            ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File) null, mpt.getUrl(), (ResourceLocation) null, iib);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureCape);
        }
    }

    private BufferedImage parseCape(BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        int srcWidth = img.getWidth();

        for (int srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, (ImageObserver) null);
        g.dispose();
        return imgNew;
    }
}
