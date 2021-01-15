package saint.modstuff.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;
import saint.valuestuff.Value;

public class NameTags extends Module {
   public Value armor = new Value("nametags_armor", true);

   public NameTags() {
      super("NameTags");
      this.setEnabled(true);
      Saint.getCommandManager().getContentList().add(new Command("nametags", "<armorstatus>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("armorstatus")) {
               NameTags.this.armor.setValueState(!(Boolean)NameTags.this.armor.getValueState());
               Logger.writeChat("Name Tags will " + ((Boolean)NameTags.this.armor.getValueState() ? "now" : "no longer") + " display the armor above the head.");
            } else {
               Logger.writeChat("Option not valid! Available options: armorstatus.");
            }

         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof RenderIn3D) {
         if (!Minecraft.isGuiEnabled()) {
            return;
         }

         RenderIn3D render = (RenderIn3D)event;
         Iterator var4 = mc.theWorld.playerEntities.iterator();

         while(var4.hasNext()) {
            EntityPlayer player = (EntityPlayer)var4.next();
            if (player != null && player != mc.thePlayer && player.isEntityAlive()) {
               double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)render.getPartialTicks() - RenderManager.renderPosX;
               double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)render.getPartialTicks() - RenderManager.renderPosY;
               double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)render.getPartialTicks() - RenderManager.renderPosZ;
               GL11.glPolygonOffset(-100000.0F, -100000.0F);
               this.renderNametag(player, posX, posY, posZ);
               GL11.glPolygonOffset(100000.0F, 100000.0F);
            }
         }
      }

   }

   private String getHealthColor(int health) {
      String color;
      if (health > 10) {
         color = "d";
      } else if ((double)health > 7.5D) {
         color = "2";
      } else if (health > 5) {
         color = "e";
      } else if ((double)health > 2.5D) {
         color = "6";
      } else {
         color = "4";
      }

      return color;
   }

   private boolean isPotting(EntityPlayer player, ItemStack stack) {
      if (stack == null) {
         return false;
      } else {
         return player.isUsingItem() && stack.getItem() instanceof ItemPotion;
      }
   }

   private int getNametagColor(EntityPlayer player) {
      int color = -1;
      if (Saint.getFriendManager().isFriend(player.getName())) {
         color = -22016;
      } else if (this.isPotting(player, player.getItemInUse())) {
         color = -1020415;
      } else if ((mc.thePlayer.isSneaking() || Saint.getModuleManager().getModuleUsingName("sneak") != null && Saint.getModuleManager().getModuleUsingName("sneak").isEnabled()) && !player.canEntityBeSeen(mc.thePlayer)) {
         color = -13447886;
      } else if (player.isSneaking()) {
         color = -262144;
      }

      return color;
   }

   private String getNametagName(EntityPlayer player) {
      String name = player.getDisplayName().getFormattedText();
      if (Saint.getFriendManager().isFriend(StringUtils.stripControlCodes(player.getName()))) {
         name = StringUtils.stripControlCodes(player.getName());
         name = StringUtils.stripControlCodes(Saint.getFriendManager().replaceNames(name, false));
      }

      name = StringUtils.stripControlCodes(name);
      double health = Math.ceil((double)(player.getHealth() + player.getAbsorptionAmount())) / 2.0D;
      String hearts = "";
      if (Math.floor(health) == health) {
         hearts = String.valueOf((int)Math.floor(health));
      } else {
         hearts = String.valueOf(health);
      }

      name = name + " §f§" + this.getHealthColor((int)health) + hearts;
      return name;
   }

   private float getNametagSize(EntityPlayer player) {
      float dist = mc.thePlayer.getDistanceToEntity(player) / 4.0F;
      return dist <= 2.0F ? 2.0F : dist;
   }

   protected void renderNametag(EntityPlayer player, double x, double y, double z) {
      String name = this.getNametagName(player);
      FontRenderer var12 = mc.fontRendererObj;
      float var13 = this.getNametagSize(player);
      float var14 = 0.016666668F * var13;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x, (float)y + player.height + 0.5F, (float)z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(-var14, -var14, var14);
      GL11.glDisable(2896);
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      Tessellator var15 = Tessellator.instance;
      byte var16 = 0;
      if (player.isSneaking()) {
         var16 = 4;
      }

      GL11.glDisable(3553);
      float var17 = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(Saint.getFriendManager().replaceNames(name, true))) / 2.0F;
      GL11.glPushMatrix();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      RenderHelper.drawBorderedRect((float)(-var12.getStringWidth(name) / 2 - 1), (float)(var16 - 5 + 3), (float)(-var12.getStringWidth(name) / 2) + RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(name)) + 1.0F, (float)(var16 - 5 + 14), 1.5F, -587202560, Integer.MIN_VALUE);
      RenderHelper.getNahrFont().drawString(name, (float)(-var12.getStringWidth(name) / 2), (float)(var16 - 5), NahrFont.FontType.SHADOW_THIN, this.getNametagColor(player), -16777216);
      if ((Boolean)this.armor.getValueState() && mc.thePlayer.getDistanceToEntity(player) <= 40.0F) {
         GL11.glScaled(1.3D, 1.3D, 1.3D);
         List items = new ArrayList();

         int offset;
         for(offset = 3; offset >= 0; --offset) {
            ItemStack stack = player.inventory.armorInventory[offset];
            if (stack != null) {
               items.add(stack);
            }
         }

         if (player.getCurrentEquippedItem() != null) {
            items.add(player.getCurrentEquippedItem());
         }

         offset = (int)(var17 - (float)((items.size() - 1) * 9) - 9.0F);
         int xPos = 0;
         Iterator var19 = items.iterator();

         while(var19.hasNext()) {
            ItemStack stack = (ItemStack)var19.next();
            GL11.glPushMatrix();
            GL11.glDepthMask(true);
            GlStateManager.clear(256);
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
            mc.getRenderItem().zLevel = -150.0F;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.func_179090_x();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.func_179098_w();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            mc.getRenderItem().zLevel = -100.0F;
            mc.getRenderItem().func_180450_b(stack, (int)(-var17 + (float)offset + (float)xPos), var16 - 20);
            Minecraft.getMinecraft().getRenderItem().func_175030_a(Minecraft.getMinecraft().fontRendererObj, stack, (int)(-var17 + (float)offset + (float)xPos), var16 - 20);
            mc.getRenderItem().zLevel = 0.0F;
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            NBTTagList enchants = stack.getEnchantmentTagList();
            GL11.glPushMatrix();
            GL11.glDisable(2896);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
               RenderHelper.getNahrFont().drawString("god", (-var17 + (float)offset + (float)xPos) * 2.0F, (float)((var16 - 20) * 2), NahrFont.FontType.SHADOW_THIN, -65536, -16777216);
            } else if (enchants != null) {
               int ency = 0;
               if (enchants.tagCount() >= 6) {
                  RenderHelper.getNahrFont().drawString("god", (-var17 + (float)offset + (float)xPos) * 2.0F, (float)((var16 - 20) * 2), NahrFont.FontType.SHADOW_THIN, -65536, -16777216);
               } else {
                  try {
                     for(int index = 0; index < enchants.tagCount(); ++index) {
                        short id = enchants.getCompoundTagAt(index).getShort("id");
                        short level = enchants.getCompoundTagAt(index).getShort("lvl");
                        if (Enchantment.field_180311_a[id] != null) {
                           Enchantment enc = Enchantment.field_180311_a[id];
                           String encName = enc.getTranslatedName(level).substring(0, 2).toLowerCase();
                           if (level > 99) {
                              encName = encName + "99+";
                           } else {
                              encName = encName + level;
                           }

                           RenderHelper.getNahrFont().drawString(encName, (-var17 + (float)offset + (float)xPos) * 2.0F + 2.0F, (float)((var16 - 20 + ency) * 2), NahrFont.FontType.SHADOW_THICK, -1, -16777216);
                           ency += mc.fontRendererObj.FONT_HEIGHT / 2 + 1;
                        }
                     }
                  } catch (Exception var27) {
                  }
               }
            }

            GL11.glEnable(2896);
            GL11.glPopMatrix();
            xPos += 18;
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
         }
      }

      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }
}
