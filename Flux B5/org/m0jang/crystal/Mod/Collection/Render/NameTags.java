package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Combat.AntiBots;
import org.m0jang.crystal.Utils.MathUtils;
import org.m0jang.crystal.Utils.RenderUtils;

public class NameTags extends Module {
   public boolean armor = true;
   public double scale = 0.1D;

   public NameTags() {
      super("NameTags", Category.Render, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget(4)
   private void onRender3DEvent(EventRender3D event) {
      if (Minecraft.theWorld != null) {
         boolean wasBobbing = Minecraft.gameSettings.viewBobbing;
         Iterator var4 = Minecraft.theWorld.loadedEntityList.iterator();

         while(var4.hasNext()) {
            Object o = var4.next();
            Entity ent = (Entity)o;
            if (ent != Minecraft.thePlayer && !ent.isInvisible() && ent instanceof EntityPlayer) {
               double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
               double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
               double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
               this.renderNameTag((EntityPlayer)ent, posX, posY, posZ, event.getPartialTicks());
            }
         }

         Minecraft.gameSettings.viewBobbing = wasBobbing;
      }
   }

   private int getDisplayColour(EntityPlayer player) {
      int colour = -5592406;
      if (Crystal.INSTANCE.friendManager.isFriend(player.getName())) {
         return -11157267;
      } else {
         if (player.isInvisible()) {
            colour = -1113785;
         } else if (player.isSneaking()) {
            colour = -5592406;
         }

         return colour;
      }
   }

   private double interpolate(double previous, double current, float delta) {
      return previous + (current - previous) * (double)delta;
   }

   private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
      double tempY = y + (player.isSneaking() ? 0.5D : 0.7D);
      Entity camera = this.mc.getRenderViewEntity();
      double originalPositionX = camera.posX;
      double originalPositionY = camera.posY;
      double originalPositionZ = camera.posZ;
      camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
      camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
      camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
      double distance = camera.getDistance(x + this.mc.getRenderManager().viewerPosX, y + this.mc.getRenderManager().viewerPosY, z + this.mc.getRenderManager().viewerPosZ);
      int width = Fonts.segoe18.getStringWidth(this.getDisplayName(player)) / 2;
      double scale = 0.0018D + 0.003000000026077032D * distance;
      if (distance <= 8.0D) {
         scale = 0.02D;
      }

      GlStateManager.pushMatrix();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.enablePolygonOffset();
      GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
      GlStateManager.disableLighting();
      GlStateManager.translate((float)x, (float)tempY + 1.4F, (float)z);
      this.mc.getRenderManager();
      GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      float var10001 = Minecraft.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
      this.mc.getRenderManager();
      GlStateManager.rotate(RenderManager.playerViewX, var10001, 0.0F, 0.0F);
      GlStateManager.scale(-scale, -scale, scale);
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      RenderUtils.drawBorderedRectReliant((float)(-width - 2), (float)(-(Fonts.segoe18.FONT_HEIGHT + 1)), (float)width + 2.0F, 1.5F, 1.6F, 1996488704, 1426063360);
      GlStateManager.enableAlpha();
      Fonts.segoe18.drawStringWithShadow(this.getDisplayName(player), (float)(-width), (float)(-(Fonts.segoe18.FONT_HEIGHT - 1)), this.getDisplayColour(player));
      GlStateManager.pushMatrix();
      int xOffset = 0;

      int index;
      ItemStack stack;
      for(index = 3; index >= 0; --index) {
         stack = player.inventory.armorInventory[index];
         if (stack != null) {
            xOffset -= 8;
         }
      }

      ItemStack armourStack;
      if (player.getCurrentEquippedItem() != null) {
         xOffset -= 8;
         armourStack = player.getCurrentEquippedItem().copy();
         if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
            armourStack.stackSize = 1;
         }

         this.renderItemStack(armourStack, xOffset, -26);
         xOffset += 16;
      }

      for(index = 3; index >= 0; --index) {
         stack = player.inventory.armorInventory[index];
         if (stack != null) {
            armourStack = stack.copy();
            if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
               armourStack.stackSize = 1;
            }

            this.renderItemStack(armourStack, xOffset, -26);
            xOffset += 16;
         }
      }

      GlStateManager.popMatrix();
      camera.posX = originalPositionX;
      camera.posY = originalPositionY;
      camera.posZ = originalPositionZ;
      GlStateManager.enableDepth();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.disablePolygonOffset();
      GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
      GlStateManager.popMatrix();
   }

   private void renderItemStack(ItemStack stack, int x, int y) {
      GlStateManager.pushMatrix();
      GlStateManager.depthMask(true);
      GlStateManager.clear(256);
      RenderHelper.enableStandardItemLighting();
      this.mc.getRenderItem().zLevel = -150.0F;
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableAlpha();
      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.enableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableAlpha();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      this.mc.getRenderItem().renderItemAboveHead(stack, x, y);
      this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);
      this.mc.getRenderItem().zLevel = 0.0F;
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableCull();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.disableLighting();
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      GlStateManager.disableDepth();
      this.renderEnchantmentText(stack, x, y);
      GlStateManager.enableDepth();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      GlStateManager.popMatrix();
   }

   private void renderEnchantmentText(ItemStack stack, int x, int y) {
      int enchantmentY = y - 24;
      if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
         this.mc.fontRendererObj.drawString("god", x * 2, enchantmentY, -43177);
      } else {
         int color = -5592406;
         int sharpnessLevel;
         int knockbackLevel;
         int fireAspectLevel;
         if (stack.getItem() instanceof ItemArmor) {
            sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
            fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
            int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
            int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            int featherFallingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack);
            int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharpnessLevel > 0) {
               this.mc.fontRendererObj.drawString("pr" + sharpnessLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (unbreakingLevel > 0) {
               this.mc.fontRendererObj.drawString("un" + unbreakingLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (knockbackLevel > 0) {
               this.mc.fontRendererObj.drawString("pp" + knockbackLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (fireAspectLevel > 0) {
               this.mc.fontRendererObj.drawString("bp" + fireAspectLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (fireProtectionLevel > 0) {
               this.mc.fontRendererObj.drawString("fp" + fireProtectionLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (thornsLevel > 0) {
               this.mc.fontRendererObj.drawString("tho" + thornsLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (featherFallingLevel > 0) {
               this.mc.fontRendererObj.drawString("ff" + featherFallingLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }
         }

         if (stack.getItem() instanceof ItemBow) {
            sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            if (sharpnessLevel > 0) {
               this.mc.fontRendererObj.drawString("po" + sharpnessLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (knockbackLevel > 0) {
               this.mc.fontRendererObj.drawString("pu" + knockbackLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (fireAspectLevel > 0) {
               this.mc.fontRendererObj.drawString("fl" + fireAspectLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }
         }

         if (stack.getItem() instanceof ItemPickaxe) {
            sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
            if (sharpnessLevel > 0) {
               this.mc.fontRendererObj.drawString("ef" + sharpnessLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (knockbackLevel > 0) {
               this.mc.fontRendererObj.drawString("fo" + knockbackLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }
         }

         if (stack.getItem() instanceof ItemAxe) {
            sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            if (sharpnessLevel > 0) {
               this.mc.fontRendererObj.drawString("sh" + sharpnessLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (knockbackLevel > 0) {
               this.mc.fontRendererObj.drawString("fa" + knockbackLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (fireAspectLevel > 0) {
               this.mc.fontRendererObj.drawString("ef" + fireAspectLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }
         }

         if (stack.getItem() instanceof ItemSword) {
            sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            if (sharpnessLevel > 0) {
               this.mc.fontRendererObj.drawString("sh" + sharpnessLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (knockbackLevel > 0) {
               this.mc.fontRendererObj.drawString("kn" + knockbackLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }

            if (fireAspectLevel > 0) {
               this.mc.fontRendererObj.drawString("fa" + fireAspectLevel, x * 2, enchantmentY, color);
               enchantmentY += 8;
            }
         }

         if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
            this.mc.fontRendererObj.drawStringWithShadow("god", (float)(x * 2), (float)enchantmentY, -3977919);
         }
      }

   }

   private String getDisplayName(EntityLivingBase entity) {
      String drawTag = entity.getDisplayName().getFormattedText();
      if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
         drawTag = drawTag.replace(entity.getDisplayName().getFormattedText(), "\247b" + entity.getName());
      }

      String colorString = "\247";
      double health = MathUtils.roundToPlace((double)entity.getHealth(), 2);
      if (health >= 12.0D) {
         colorString = colorString + "2";
      } else if (health >= 4.0D) {
         colorString = colorString + "6";
      } else {
         colorString = colorString + "4";
      }

      drawTag = drawTag + " " + colorString + health;
      return drawTag;
   }

   private void renderLivingLabel(Entity entity, double p_147906_3_, double p_147906_5_, double p_147906_7_) {
      if (!Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() || !((AntiBots)Crystal.INSTANCE.getMods().get(AntiBots.class)).isNPC((EntityLivingBase)entity)) {
         float dist = Minecraft.thePlayer.getDistanceToEntity(entity);
         float scale = 0.02672F;
         float factor = (float)(dist <= 8.0F ? 8.0D * this.scale : (double)dist * this.scale);
         scale *= factor;
         int tagLength = this.mc.fontRendererObj.getStringWidth(entity.getDisplayName().getFormattedText() + " " + MathUtils.roundToPlace((double)((EntityPlayer)entity).getHealth(), 2)) / 2;
         String drawTag = entity.getDisplayName().getFormattedText();
         if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
            drawTag = drawTag.replace(entity.getDisplayName().getFormattedText(), "\247b" + entity.getName());
         }

         String colorString = "\247";
         double health = MathUtils.roundToPlace((double)((EntityPlayer)entity).getHealth(), 2);
         if (health >= 12.0D) {
            colorString = colorString + "2";
         } else if (health >= 4.0D) {
            colorString = colorString + "6";
         } else {
            colorString = colorString + "4";
         }

         drawTag = drawTag + " " + colorString + health;
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)p_147906_3_ + 0.0F, (float)p_147906_5_ + entity.height + 0.5F, (float)p_147906_7_);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         RenderManager var10000 = this.mc.renderManager;
         GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         var10000 = this.mc.renderManager;
         GlStateManager.rotate(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
         GlStateManager.scale(-scale, -scale, scale);
         GlStateManager.disableLighting();
         GlStateManager.depthMask(false);
         GlStateManager.disableDepth();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         Tessellator var15 = Tessellator.getInstance();
         WorldRenderer var16 = var15.getWorldRenderer();
         byte y = 0;
         GlStateManager.disableTexture2D();
         var16.startDrawingQuads();
         var16.func_178960_a(0.0F, 0.0F, 0.0F, 0.5F);
         var16.addVertex((double)(-tagLength - 1), (double)(-1 + y), 0.0D);
         var16.addVertex((double)(-tagLength - 1), (double)(8 + y), 0.0D);
         var16.addVertex((double)(tagLength + 1), (double)(8 + y), 0.0D);
         var16.addVertex((double)(tagLength + 1), (double)(-1 + y), 0.0D);
         var15.draw();
         GlStateManager.enableTexture2D();
         this.mc.fontRendererObj.drawString(drawTag, -tagLength, y, -1);
         GlStateManager.enableDepth();
         GlStateManager.depthMask(true);
         this.mc.fontRendererObj.drawString(drawTag, -tagLength, y, -1);
         GlStateManager.enableLighting();
         GlStateManager.disableBlend();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.popMatrix();
      }
   }
}
