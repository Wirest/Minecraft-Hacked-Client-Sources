package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import me.xatzdevelopments.xatz.client.events.Event;
import me.xatzdevelopments.xatz.client.events.EventRender3D;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.util.MathHelper;

public class NameTagsPlus extends Module {
	 private TimeHelper potionrefreshtime;
	 private final RenderItem itemRender;

    public NameTagsPlus() {
    	  
		super("NameTags+", Keyboard.KEY_NONE, Category.RENDER, "test");
		this.itemRender = new RenderItem(mc.renderEngine, mc.getModelManager());
    }
    
    public ModSetting[] getModSettings() {
    	SliderSetting<Number> nametagscale = new SliderSetting<Number>("Scale", ClientSettings.nametagscale, 0.5, 6, 0.00, ValueFormat.DECIMAL);
    	return new ModSetting[] { nametagscale};
    }

    @Override
    public void onDisable() {
        
        super.onDisable();
    }

    @Override
    public void onEnable() {
        
        super.onEnable();
    }

    @Override
    public void onUpdate(Event event) {
    	  if (event instanceof EventRender3D) {
              if (!Minecraft.isGuiEnabled()) {
                  return;
              }
              final EventRender3D render = (EventRender3D)event;
              for (final EntityPlayer player : mc.theWorld.playerEntities) {
                  if (player != null && player != mc.thePlayer) {
                      if (!player.isEntityAlive()) {
                          continue;
                      }
                      final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * render.getPartialTicks() - RenderManager.renderPosX;
                      final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * render.getPartialTicks() - RenderManager.renderPosY;
                      final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * render.getPartialTicks() - RenderManager.renderPosZ;
                      GL11.glPolygonOffset(-1000000.0f, -1000000.0f);
                      this.renderNametag(player, posX, posY, posZ);
                      GL11.glPolygonOffset(1000000.0f, 1000000.0f);
                  }
              }
    	  }
        super.onUpdate();
    }
    
    private int getNametagColor(final EntityPlayer player) {
        int color = 16777215;
        if (Xatz.getFriendsMananger().isFriend(player.getName())) {
            color = 52479;
        }
        else if (player.isInvisible()) {
            color = -13312;
        }
        
        else if (player.isPotionActive(22) || player.isPotionActive(11)) {
            color = -23296;
        }
        else if ((mc.thePlayer.isSneaking() || !player.canEntityBeSeen(mc.thePlayer))) {
            color = -16711936;
        }
        else if (player.isSneaking()) {
            color = -262144;
        }
        return color;
    }
    
    void handleEntityEffect(final S1DPacketEntityEffect packetIn) {
        if (this.potionrefreshtime.hasReached(800.0f)) {
            this.handleEntityEffect(packetIn);
            this.potionrefreshtime.reset();
        }
    }
    
    private String getNametagName(final EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (Xatz.getFriendsMananger().isFriend(StringUtils.stripControlCodes(player.getName()))) {
            name = StringUtils.stripControlCodes(player.getName());
            name = StringUtils.stripControlCodes(Xatz.getFriendsMananger().replaceNames(name, false));
        }
        if (this.getNametagColor(player) != -1) {
            name = StringUtils.stripControlCodes(name);
        }
       
            final int health = Math.round(20.0f * (player.getHealth() / player.getMaxHealth()));
            String hearts = "";
            if (health >= 1 && health <= 2) {
                hearts = "0.5 - LOW";
            }
            else if (health >= 1 && health <= 2) {
                hearts = "1 - LOW";
            }
            else if (health >= 2 && health <= 3) {
                hearts = "1.5 - LOW";
            }
            else if (health >= 3 && health <= 4) {
                hearts = "2";
            }
            else if (health >= 4 && health <= 5) {
                hearts = "2.5";
            }
            else if (health >= 5 && health <= 6) {
                hearts = "3";
            }
            else if (health >= 6 && health <= 7) {
                hearts = "3.5";
            }
            else if (health >= 7 && health <= 8) {
                hearts = "4";
            }
            else if (health >= 8 && health <= 9) {
                hearts = "4.5";
            }
            else if (health >= 9 && health <= 10) {
                hearts = "5";
            }
            else if (health >= 10 && health <= 11) {
                hearts = "5.5";
            }
            else if (health >= 11 && health <= 12) {
                hearts = "6";
            }
            else if (health >= 12 && health <= 13) {
                hearts = "6.5";
            }
            else if (health >= 13 && health <= 14) {
                hearts = "7";
            }
            else if (health >= 14 && health <= 15) {
                hearts = "7.5";
            }
            else if (health >= 15 && health <= 16) {
                hearts = "8";
            }
            else if (health >= 16 && health <= 17) {
                hearts = "8.5";
            }
            else if (health >= 17 && health <= 18) {
                hearts = "9";
            }
            else if (health >= 18 && health <= 19) {
                hearts = "9.5";
            }
            else if (health >= 19 && health <= 20) {
                hearts = "10";
            }
            else if (health == 0) {
                hearts = "0.5 - LOW";
            }
            name = String.valueOf(name) + " §f| §" + this.getHealthColor(health) + hearts;
        
        return name;
    }
    
    private String getHealthColor(final int health) {
        String color;
        if (health > 15) {
            color = "2";
        }
        else if (health > 10) {
            color = "e";
        }
        else if (health > 5) {
            color = "6";
        }
        else {
            color = "4";
        }
        return color;
    }
    
    private float getNametagSize(final EntityPlayer player) {
        final float dist = (float) (mc.thePlayer.getDistanceToEntity(player) / ClientSettings.nametagscale);
        return (dist <= 2.0f) ? 2.0f : dist;
    }
    
    protected void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        final String name = this.getNametagName(player);
        final FontRenderer var12 = mc.fontRendererObj;
        final float var13 = this.getNametagSize(player);
        final float var14 = 0.016666668f * var13;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + player.height + 0.5f, (float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-RenderManager.getPlayerViewY(), 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.getPlayerViewX(), 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var14, -var14, var14);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        final Tessellator var15 = Tessellator.getInstance();
        byte var16 = 0;
        if (player.isSneaking()) {
            var16 = 4;
        }
        GL11.glDisable(3553);
        GL11.glPushMatrix();
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        worldRenderer.startDrawingQuads();
        final int var17 = var12.getStringWidth(Xatz.getFriendsMananger().replaceNames(name, true)) / 2;
        GL11.glColor4d(0.0, 0.0, 0.0, 0.4);
        worldRenderer.addVertex(-var17 - 4, -2 + var16, 0.0);
        worldRenderer.addVertex(-var17 - 4, 9 + var16, 0.0);
        worldRenderer.addVertex(var17 + 4, 9 + var16, 0.0);
        worldRenderer.addVertex(var17 + 4, -2 + var16, 0.0);
        var15.draw();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        var12.drawStringWithShadow(name, (float)(-var12.getStringWidth(name) / 2), var16, this.getNametagColor(player));
        if (mc.thePlayer.getDistanceToEntity(player) < 90) {
            final List<ItemStack> items = new ArrayList<ItemStack>();
            if (player.getCurrentEquippedItem() != null) {
                items.add(player.getCurrentEquippedItem());
            }
            for (int index = 3; index >= 0; --index) {
                final ItemStack stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    items.add(stack);
                }
            }
            final int offset = var17 - (items.size() - 1) * 9 - 9;
            int xPos = 0;
            for (final ItemStack stack2 : items) {
                GL11.glPushMatrix();
                GlStateManager.clear(256);
                GL11.glDisable(2896);
                this.itemRender.zLevel = -100.0f;
                this.itemRender.renderItemAndEffectIntoGUI(stack2, -var17 + offset + xPos, var16 - 20);
                Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack2, -var17 + offset + xPos, var16 - 20);
                this.itemRender.zLevel = 0.0f;
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.disableCull();
                GlStateManager.clear(256);
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
                final NBTTagList enchants = stack2.getEnchantmentTagList();
                GL11.glPushMatrix();
                GL11.glDisable(2896);
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                if (stack2.getItem() == Items.golden_apple && stack2.hasEffect()) {
                    mc.fontRendererObj.drawStringWithShadow("god", (float)((-var17 + offset + xPos) * 2), (float)((var16 - 20) * 2), -65536);
                }
                else if (enchants != null) {
                    int ency = 0;
                    if (enchants.tagCount() >= 6) {
                        mc.fontRendererObj.drawStringWithShadow("god", (float)((-var17 + offset + xPos) * 2), (float)((var16 - 20) * 2), -65536);
                    }
                    else {
                        try {
                            for (int index2 = 0; index2 < enchants.tagCount(); ++index2) {
                                final short id = enchants.getCompoundTagAt(index2).getShort("id");
                                final short level = enchants.getCompoundTagAt(index2).getShort("lvl");
                                if (Enchantment.getEnchantmentslist()[id] != null) {
                                    final Enchantment enc = Enchantment.getEnchantmentslist()[id];
                                    String encName = enc.getTranslatedName(level).substring(0, 1).toLowerCase();
                                    if (level > 99) {
                                        encName = "§c" + encName + "99+";
                                    }
                                    else {
                                        encName = "§c" + encName + level;
                                    }
                                    mc.fontRendererObj.drawStringWithShadow(encName, (float)((-var17 + offset + xPos) * 2), (float)((var16 - 20 + ency) * 2), -5592406);
                                    ency += mc.fontRendererObj.FONT_HEIGHT / 2 + 1;
                                }
                            }
                        }
                        catch (Exception ex) {}
                    }
                }
                GL11.glEnable(2896);
                GL11.glPopMatrix();
                xPos += 18;
            }
        }
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    
}