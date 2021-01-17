package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.events.EventRender;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientFont;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.friendmanager.FriendManager;
import me.slowly.client.value.Value;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class NameTags extends Mod {
   private Value armor = new Value("NameTags_Armor", true);

   public NameTags() {
      super("NameTags", Mod.Category.RENDER, Colors.ORANGE.c);
   }

   @EventTarget
   public void onEvent(EventRender event) {
      this.setColor(Colors.YELLOW.c);
      Iterator var3 = this.mc.theWorld.playerEntities.iterator();

      while(true) {
         EntityPlayer entity;
         do {
            do {
               if (!var3.hasNext()) {
                  return;
               }

               entity = (EntityPlayer)var3.next();
            } while(entity == this.mc.thePlayer);
         } while(entity.isInvisible());

         String name = ClientUtil.removeColorCode(entity.getDisplayName().getFormattedText());
         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glDisable(2929);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.disableLighting();
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3553);
         float partialTicks = this.mc.timer.renderPartialTicks;
         this.mc.getRenderManager();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
         this.mc.getRenderManager();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
         this.mc.getRenderManager();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
         y += (double)(this.mc.thePlayer.getDistanceToEntity(entity) * 0.02F);
         if (Double.isNaN((double)entity.getHealth()) || Double.isInfinite((double)entity.getHealth())) {
            entity.setHealth(20.0F);
         }

         BigDecimal bigDecimal = new BigDecimal((double)entity.getHealth());
         bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
         double HEALTH = bigDecimal.doubleValue();
         int COLOR = -1;
         int COLOR1;
         if (HEALTH > 20.0D) {
            COLOR1 = -65292;
         } else if (HEALTH >= 10.0D) {
            COLOR1 = -16711936;
         } else if (HEALTH >= 3.0D) {
            COLOR1 = -23296;
         } else {
            COLOR1 = -65536;
         }

         boolean isFriend = FriendManager.isFriend(entity);
         String alias = "";
         if (isFriend) {
            alias = FriendManager.getFriend(entity.getName()).getAlias();
         }

         String USERNAME = (isFriend ? name.replace(entity.getName(), alias) : name) + " " + HEALTH;
         float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
         float SCALE = Math.min(Math.max(1.2F * DISTANCE * 0.15F, 1.25F), 6.0F) * 0.02F;
         GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(this.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
         GL11.glScalef(-SCALE, -SCALE, SCALE);
         Tessellator tesselator = Tessellator.getInstance();
         WorldRenderer worldRenderer = tesselator.getWorldRenderer();
         ClientFont fontRenderer = ClientUtil.getClientfont();
         String CLIENT_USER = "Slowly Gang Member";
         int STRING_WIDTH = (int)(fontRenderer.getStringWidth(USERNAME) / 2.0F);
         int STRING_WIDTH_CLIENTUSER = (int)(fontRenderer.getStringWidth(CLIENT_USER) / 2.0F);
         int i = 0;
                if (Client.getInstance().getIrcChat().isClientUser(entity.getName())) {
                    RenderHelper.drawBorderedRect(-STRING_WIDTH_CLIENTUSER - 1.0f, -13.0f, STRING_WIDTH_CLIENTUSER + 2.0f, -2.0f, 1.0f, Colors.BLACK.c, ClientUtil.reAlpha(Colors.BLACK.c, 0.5f));
                    this.mc.fontRendererObj.drawStringWithShadow("¡ì3[IRC]" + CLIENT_USER, -fontRenderer.getStringWidth(CLIENT_USER) / 2.0f, -fontRenderer.getStringHeight(CLIENT_USER) / 32.0f - 11.0f, -1);
                    i = -10;
                }
                RenderHelper.drawBorderedRect(-STRING_WIDTH - 1.0f, -2.0f, STRING_WIDTH + 2.0f, 9.0f, 1.0f, Colors.BLACK.c, ClientUtil.reAlpha(Colors.BLACK.c, 0.5f));
                GL11.glEnable(3553);
                String s = entity.getDisplayName().getFormattedText();
                if (FriendManager.isFriend(entity)) {
                    s = s.replace(entity.getName(), "¡ì3[F]" + FriendManager.getFriend(entity.getName()).getAlias());
                }
                

                this.mc.fontRendererObj.drawStringWithShadow(ModManager.getModByName("StreamMode").isEnabled() ? "Protected" : s, -fontRenderer.getStringWidth(USERNAME) / 2.0F, -fontRenderer.getStringHeight(name) / 32.0F, -1);
                this.mc.fontRendererObj.drawStringWithShadow(String.valueOf(HEALTH), fontRenderer.getStringWidth(USERNAME) / 2.0F - fontRenderer.getStringWidth(String.valueOf(HEALTH)), -fontRenderer.getStringHeight(name) / 32.0F + 0.5F, COLOR1);
         if (((Boolean)this.armor.getValueState()).booleanValue()) {
            if (entity.getCurrentArmor(0) != null && entity.getCurrentArmor(0).getItem() instanceof ItemArmor) {
               this.renderItem(entity.getCurrentArmor(0), 26, i, 0);
            }

            if (entity.getCurrentArmor(1) != null && entity.getCurrentArmor(1).getItem() instanceof ItemArmor) {
               this.renderItem(entity.getCurrentArmor(1), 13, i, 0);
            }

            if (entity.getCurrentArmor(2) != null && entity.getCurrentArmor(2).getItem() instanceof ItemArmor) {
               this.renderItem(entity.getCurrentArmor(2), 0, i, 0);
            }

            if (entity.getCurrentArmor(3) != null && entity.getCurrentArmor(3).getItem() instanceof ItemArmor) {
               this.renderItem(entity.getCurrentArmor(3), -13, i, 0);
            }

            if (entity.getHeldItem() != null) {
               this.renderItem(entity.getHeldItem(), -26, i, 0);
            }
         }

         GL11.glEnable(2929);
         GlStateManager.disableBlend();
         GL11.glDisable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }
   }

   public void renderItem(ItemStack item, int xPos, int yPos, int zPos) {
      GL11.glPushMatrix();
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GL11.glBlendFunc(770, 771);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      IBakedModel ibakedmodel = this.mc.getRenderItem().getItemModelMesher().getItemModel(item);
      this.mc.getRenderItem().textureManager.bindTexture(TextureMap.locationBlocksTexture);
      GlStateManager.scale(16.0F, 16.0F, 0.0F);
      GL11.glTranslated((double)(((float)xPos - 7.85F) / 16.0F), (double)((float)(-5 + yPos) / 16.0F), (double)((float)zPos / 16.0F));
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.disableLighting();
      ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
      if (ibakedmodel.isBuiltInRenderer()) {
         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableRescaleNormal();
         TileEntityItemStackRenderer.instance.renderByItem(item);
      } else {
         this.mc.getRenderItem().renderModel(ibakedmodel, -1, item);
      }

      GlStateManager.enableAlpha();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableLighting();
      GlStateManager.popMatrix();
   }
   @Override
   public void onDisable() {
       super.onDisable();
       ClientUtil.sendClientMessage("NameTags Disable", ClientNotification.Type.ERROR);
   }
   public void onEnable() {
   	super.isEnabled();
       ClientUtil.sendClientMessage("NameTags Enable", ClientNotification.Type.SUCCESS);
   }
}

