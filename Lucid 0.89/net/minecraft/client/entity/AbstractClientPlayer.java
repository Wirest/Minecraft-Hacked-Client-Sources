package net.minecraft.client.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.HashMap;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import me.aristhena.lucid.eventapi.events.SprintEvent;
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
import net.minecraft.src.Config;
import net.minecraft.src.PlayerConfigurations;
import net.minecraft.src.Reflector;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public abstract class AbstractClientPlayer extends EntityPlayer
{
    public NetworkPlayerInfo playerInfo;
    private ResourceLocation ofLocationCape = null;
    
    public AbstractClientPlayer(World worldIn, GameProfile playerProfile)
    {
	super(worldIn, playerProfile);
	String username = playerProfile.getName();
	this.downloadCape(username);
	PlayerConfigurations.getPlayerConfiguration(this);
    }
    
    /**
     * Returns true if the player is in spectator mode.
     */
    @Override
    public boolean isSpectator()
    {
	NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
	return var1 != null && var1.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
    
    /**
     * Checks if this instance of AbstractClientPlayer has any associated player data.
     */
    public boolean hasPlayerInfo()
    {
	return this.getPlayerInfo() != null;
    }
    
    protected NetworkPlayerInfo getPlayerInfo()
    {
	if (this.playerInfo == null)
	{
	    this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
	}
	
	return this.playerInfo;
    }
    
    /**
     * Returns true if the player has an associated skin.
     */
    public boolean hasSkin()
    {
	NetworkPlayerInfo var1 = this.getPlayerInfo();
	return var1 != null && var1.hasLocationSkin();
    }
    
    /**
     * Returns true if the player instance has an associated skin.
     */
    public ResourceLocation getLocationSkin()
    {
	NetworkPlayerInfo var1 = this.getPlayerInfo();
	return var1 == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : var1.getLocationSkin();
    }
    
    public ResourceLocation getLocationCape()
    {
	if (!Config.isShowCapes())
	{
	    return null;
	}
	else if (this.ofLocationCape != null)
	{
	    return this.ofLocationCape;
	}
	else
	{
	    NetworkPlayerInfo var1 = this.getPlayerInfo();
	    return var1 == null ? null : var1.getLocationCape();
	}
    }
    
    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username)
    {
	TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
	Object var3 = var2.getTexture(resourceLocationIn);
	
	if (var3 == null)
	{
	    var3 = new ThreadDownloadImageData((File) null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] { StringUtils.stripControlCodes(username) }), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
	    var2.loadTexture(resourceLocationIn, (ITextureObject) var3);
	}
	
	return (ThreadDownloadImageData) var3;
    }
    
    /**
     * Returns true if the username has an associated skin.
     *  
     * @param username The username of the player being checked.
     */
    public static ResourceLocation getLocationSkin(String username)
    {
	return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }
    
    public String getSkinType()
    {
	NetworkPlayerInfo var1 = this.getPlayerInfo();
	return var1 == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID()) : var1.getSkinType();
    }
    
    public float getFovModifier()
    {
	float var1 = 1.0F;
	
	if (this.capabilities.isFlying)
	{
	    var1 *= 1.1F;
	}
	
	IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
	var1 = (float) (var1 * ((var2.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
	
	if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1) || Float.isInfinite(var1))
	{
	    var1 = 1.0F;
	}
	
	if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow)
	{
	    int var3 = this.getItemInUseDuration();
	    float var4 = var3 / 20.0F;
	    
	    if (var4 > 1.0F)
	    {
		var4 = 1.0F;
	    }
	    else
	    {
		var4 *= var4;
	    }
	    
	    var1 *= 1.0F - var4 * 0.15F;
	}
	return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[] { this, Float.valueOf(var1) }) : var1;
    }
    
    private void downloadCape(String username)
    {
	if (username != null && !username.isEmpty())
	{
	    username = StringUtils.stripControlCodes(username);
	    String ofCapeUrl = "http://s.optifine.net/capes/" + username + ".png";
	    MinecraftProfileTexture mpt = new MinecraftProfileTexture(ofCapeUrl, new HashMap());
	    final ResourceLocation rl = new ResourceLocation("capeof/" + mpt.getHash());
	    TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
	    ITextureObject tex = textureManager.getTexture(rl);
	    
	    if (tex != null && tex instanceof ThreadDownloadImageData)
	    {
		ThreadDownloadImageData iib = (ThreadDownloadImageData) tex;
		
		if (iib.imageFound != null)
		{
		    if (iib.imageFound.booleanValue())
		    {
			this.ofLocationCape = rl;
		    }
		    
		    return;
		}
	    }
	    
	    IImageBuffer iib1 = new IImageBuffer()
	    {
		ImageBufferDownload ibd = new ImageBufferDownload();
		
		@Override
		public BufferedImage parseUserSkin(BufferedImage var1)
		{
		    return AbstractClientPlayer.this.parseCape(var1);
		}
		
		@Override
		public void skinAvailable()
		{
		    AbstractClientPlayer.this.ofLocationCape = rl;
		}
	    };
	    ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File) null, mpt.getUrl(), (ResourceLocation) null, iib1);
	    textureManager.loadTexture(rl, textureCape);
	}
    }
    
    private BufferedImage parseCape(BufferedImage img)
    {
	int imageWidth = 64;
	int imageHeight = 32;
	int srcWidth = img.getWidth();
	
	for (int srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageHeight *= 2)
	{
	    imageWidth *= 2;
	}
	
	BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
	Graphics g = imgNew.getGraphics();
	g.drawImage(img, 0, 0, (ImageObserver) null);
	g.dispose();
	return imgNew;
    }
    
    public void setSprintingGay(boolean sprinting)
    {
	SprintEvent event = new SprintEvent(sprinting);
	event.call();
	
	sprinting = event.sprint;
	
	super.setSprinting(sprinting);
	IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
	
	if (var2.getModifier(sprintingSpeedBoostModifierUUID) != null)
	{
	    var2.removeModifier(sprintingSpeedBoostModifier);
	}
	
	if (sprinting)
	{
	    var2.applyModifier(sprintingSpeedBoostModifier);
	}
    }
}
