package saint.modstuff.modules;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.BossHealth;
import saint.eventstuff.events.DrawScreen;
import saint.modstuff.Module;
import saint.notificationstuff.Notification;
import saint.tabgui.TabGui;
import saint.utilities.ListenerUtil;
import saint.utilities.Logger;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;
import saint.valuestuff.Value;

public class HUD extends Module {
   private int selected;
   private int selectedMod;
   private boolean open;
   private float width;
   private float width2;
   private float height;
   private float bossy;
   public static Value arraylist = new Value("hud_arraylist", true);
   public static Value tabgui = new Value("hud_tabgui", true);
   public static Value tabguisaint = new Value("tabgui_saint", true);
   public static Value tabguiwolfram = new Value("tabgui_tf2", false);
   public static Value armor = new Value("hud_armor-status", true);
   public static Value potions = new Value("hud_potioneffects", true);
   public static Value tabguikeys = new Value("hud_tabguikeys", true);
   public static Value saint = new Value("hud_saint", true);

   public HUD() {
      super("HUD");
      this.setEnabled(true);
      Saint.getCommandManager().getContentList().add(new Command("hud", "<armorstatus/potioneffects/arraylist/tabgui>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("arraylist")) {
               HUD.arraylist.setValueState(!(Boolean)HUD.arraylist.getValueState());
               Logger.writeChat("HUD will " + ((Boolean)HUD.arraylist.getValueState() ? "now" : "no longer") + " display the arraylist.");
            } else if (message.split(" ")[1].equalsIgnoreCase("tabgui")) {
               HUD.tabgui.setValueState(!(Boolean)HUD.tabgui.getValueState());
               Logger.writeChat("HUD will " + ((Boolean)HUD.tabgui.getValueState() ? "now" : "no longer") + " display the tabgui.");
            } else if (message.split(" ")[1].equalsIgnoreCase("tabguikeys")) {
               HUD.tabguikeys.setValueState(!(Boolean)HUD.tabguikeys.getValueState());
               Logger.writeChat("Tab Gui will use the " + ((Boolean)HUD.tabgui.getValueState() ? "numpad" : "arrow") + " keys.");
            } else if (message.split(" ")[1].equalsIgnoreCase("armorstatus")) {
               HUD.armor.setValueState(!(Boolean)HUD.armor.getValueState());
               Logger.writeChat("HUD will " + ((Boolean)HUD.armor.getValueState() ? "now" : "no longer") + " display the armor status.");
            } else if (message.split(" ")[1].equalsIgnoreCase("potioneffects")) {
               HUD.potions.setValueState(!(Boolean)HUD.potions.getValueState());
               Logger.writeChat("HUD will " + ((Boolean)HUD.potions.getValueState() ? "now" : "no longer") + " display the potion effects.");
            } else {
               Logger.writeChat("Option not valid! Available options: armorstatus, potioneffects, arraylist, tabgui.");
            }

         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof BossHealth) {
         this.bossy = 14.0F;
      } else if (event instanceof DrawScreen) {
         if (!Saint.isLatestVersion()) {
            this.bossy = 26.0F;
         }

         ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
         new CopyOnWriteArrayList();
         LagDetector detector = (LagDetector)Saint.getModuleManager().getModuleUsingName("lagdetector");
         if ((Boolean)tabgui.getValueState()) {
            TabGui gui = Saint.getTabGUI();
            gui.drawGui(4, detector.getTime().hasReached(1000L) ? 14 : 4, 68);
         }

         RenderHelper.getNahrFont().drawString("FPS: §7" + mc.debug.replaceAll("fps", "").substring(0, 4), 2.0F, (float)((Boolean)tabgui.getValueState() ? (detector.getTime().hasReached(1000L) ? 95 : 85) : (detector.getTime().hasReached(1000L) ? 8 : -2)), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
         if (!Saint.isLatestVersion()) {
            GL11.glPushMatrix();
            GL11.glScaled(2.0D, 2.0D, 2.0D);
            mc.fontRendererObj.drawString("NOT LATEST VERSION!", scaledRes.getScaledWidth() / 4 - mc.fontRendererObj.getStringWidth("§cNOT LATEST VERSION!") / 2, 7, -14342875);
            mc.fontRendererObj.drawString("§cNOT LATEST VERSION!", scaledRes.getScaledWidth() / 4 - mc.fontRendererObj.getStringWidth("§cNOT LATEST VERSION!") / 2, 6, -2302756);
            GL11.glPopMatrix();
         }

         String version = "";
         if (mc.getCurrentServerData() != null) {
            version = String.valueOf(ListenerUtil.serverBrand).length() > 55 ? String.valueOf(ListenerUtil.serverBrand).substring(0, 55) + "..." : String.valueOf(ListenerUtil.serverBrand);
         } else {
            version = "Vanilla";
         }

         float y = (float)(scaledRes.getScaledHeight() - 30);
         RenderHelper.getNahrFont().drawString("Saint", (float)RenderHelper.getScaledRes().getScaledWidth() / 2.0F - RenderHelper.getNahrFont().getStringWidth("Saint") / 2.0F, this.bossy, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
         RenderHelper.getNahrFont().drawString("v" + Saint.getVersion(), (float)RenderHelper.getScaledRes().getScaledWidth() / 2.0F - RenderHelper.getNahrFont().getStringWidth("v" + Saint.getVersion()) / 2.0F, this.bossy + 8.0F, NahrFont.FontType.SHADOW_THIN, -6710887, -16777216);
         this.bossy = -2.0F;
         RenderHelper.getNahrFont().drawString("§fX: §7" + Math.round(mc.thePlayer.posX) + " §fY: §7" + Math.round(mc.thePlayer.posY) + " §fZ: §7" + Math.round(mc.thePlayer.posZ), 1.0F, (float)(RenderHelper.getScaledRes().getScaledHeight() - 25), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
         RenderHelper.getNahrFont().drawString("Server: §7" + version, 1.0F, (float)(RenderHelper.getScaledRes().getScaledHeight() - 15), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
         if ((Boolean)potions.getValueState()) {
            this.drawPotionEffects(scaledRes);
         }

         if ((Boolean)armor.getValueState()) {
            this.drawArmorStatus(scaledRes);
         }

         if ((Boolean)arraylist.getValueState()) {
            GL11.glEnable(3042);
            this.drawArraylist(scaledRes);
            GL11.glDisable(3042);
         }

         try {
            Saint.getNotificationManager().drawNotifications();
         } catch (Exception var8) {
         }
      }

   }

   private void drawArraylist(ScaledResolution scaledRes) {
      int y = -2;
      List mods = Saint.getModuleManager().getContentList();
      Collections.sort(mods, new Comparator() {
         public int compare(Module mod1, Module mod2) {
            if (RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod1.getTag())) > RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod2.getTag()))) {
               return -1;
            } else {
               return RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod1.getTag())) < RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod2.getTag())) ? 1 : 0;
            }
         }
      });
      int count = 0;
      List viablemods = new CopyOnWriteArrayList();
      Iterator var7 = mods.iterator();

      Module mod;
      while(var7.hasNext()) {
         mod = (Module)var7.next();
         if (mod.isVisible() && mod.isEnabled()) {
            viablemods.add(mod);
         }
      }

      var7 = viablemods.iterator();

      while(var7.hasNext()) {
         mod = (Module)var7.next();
         int xf = (int)((float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod.getTag())) - 2.0F);
         GL11.glPushMatrix();
         float width = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod.getTag()));
         float height = (float)((Boolean)saint.getValueState() ? y : scaledRes.getScaledHeight() - 17 - y);
         float widthc = (float)scaledRes.getScaledWidth() - 5.5F - RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod.getTag()));
         Module print = null;

         try {
            print = (Module)viablemods.get(count + 1);
         } catch (Exception var14) {
         }

         if (print != null) {
            RenderHelper.drawRect((float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod.getTag())) - 8.0F, height + 14.0F, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(print.getTag())) - 7.0F, height + 13.0F, -587202560);
         }

         RenderHelper.drawRect((float)scaledRes.getScaledWidth() - 3.5F, height + 4.0F, (float)(scaledRes.getScaledWidth() - 1), height + 13.0F, mod.getColor());
         RenderHelper.drawRect((float)scaledRes.getScaledWidth() - 1.0F, height + 4.0F, (float)(scaledRes.getScaledWidth() - 0), height + 14.0F, -587202560);
         RenderHelper.drawRect((float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod.getTag())) - 8.0F, height + 4.0F, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod.getTag())) - 7.0F, height + 13.0F, -587202560);
         RenderHelper.drawRect((float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(mod.getTag())) - 7.0F, height + 4.0F, (float)scaledRes.getScaledWidth() - 3.5F, height + 13.0F, 1610612736);
         print = null;

         try {
            print = (Module)viablemods.get(viablemods.size() - 1);
         } catch (Exception var15) {
         }

         if (print != null) {
            this.width2 = RenderHelper.getNahrFont().getStringWidth(StringUtils.stripControlCodes(print.getTag()));
         }

         RenderHelper.getNahrFont().drawString(mod.getTag(), widthc, height, NahrFont.FontType.SHADOW_THIN, mod.getColor(), -16777216);
         if (this.height < height) {
            this.height = height;
         }

         if (this.width < width) {
            this.width = width;
         }

         y = (int)((double)y + (double)mc.fontRendererObj.FONT_HEIGHT + 0.7D);
         ++count;
         GL11.glPopMatrix();
      }

      RenderHelper.drawRect((float)scaledRes.getScaledWidth() - this.width - 8.0F, 1.0F, (float)(scaledRes.getScaledWidth() - 0), 2.0F, -587202560);
      RenderHelper.drawRect((float)scaledRes.getScaledWidth() - this.width2 - 8.0F, this.height + (float)(viablemods.size() > 1 ? 13 : 11), (float)(scaledRes.getScaledWidth() - 1), this.height + (float)(viablemods.size() > 1 ? 14 : 12), -587202560);
      this.width = 0.0F;
      this.width2 = 0.0F;
      this.height = 0.0F;
   }

   private void drawPotionEffects(ScaledResolution scaledRes) {
      int y = 10;

      for(Iterator var4 = mc.thePlayer.getActivePotionEffects().iterator(); var4.hasNext(); y += mc.fontRendererObj.FONT_HEIGHT) {
         PotionEffect effect = (PotionEffect)var4.next();
         Potion potion = Potion.potionTypes[effect.getPotionID()];
         String name = I18n.format(potion.getName());
         if (effect.getAmplifier() == 1) {
            name = name + " II";
         } else if (effect.getAmplifier() == 2) {
            name = name + " III";
         } else if (effect.getAmplifier() == 3) {
            name = name + " IV";
         } else if (effect.getAmplifier() == 4) {
            name = name + " V";
         } else if (effect.getAmplifier() == 5) {
            name = name + " VI";
         } else if (effect.getAmplifier() == 6) {
            name = name + " VII";
         } else if (effect.getAmplifier() == 7) {
            name = name + " VIII";
         } else if (effect.getAmplifier() == 8) {
            name = name + " IX";
         } else if (effect.getAmplifier() == 9) {
            name = name + " X";
         } else if (effect.getAmplifier() > 10) {
            name = name + " X+";
         } else {
            name = name + " I";
         }

         name = name + "§f: §7" + Potion.getDurationString(effect);
         int color = Integer.MIN_VALUE;
         if (effect.getEffectName() == "potion.weither") {
            color = -16777216;
         } else if (effect.getEffectName() == "potion.weakness") {
            color = -9868951;
         } else if (effect.getEffectName() == "potion.waterBreathing") {
            color = -16728065;
         } else if (effect.getEffectName() == "potion.saturation") {
            color = -11179217;
         } else if (effect.getEffectName() == "potion.resistance") {
            color = -5658199;
         } else if (effect.getEffectName() == "potion.regeneration") {
            color = -1146130;
         } else if (effect.getEffectName() == "potion.poison") {
            color = -14513374;
         } else if (effect.getEffectName() == "potion.nightVision") {
            color = -6737204;
         } else if (effect.getEffectName() == "potion.moveSpeed") {
            color = -7876870;
         } else if (effect.getEffectName() == "potion.moveSlowdown") {
            color = -16741493;
         } else if (effect.getEffectName() == "potion.jump") {
            color = -5374161;
         } else if (effect.getEffectName() == "potion.invisibility") {
            color = -9404272;
         } else if (effect.getEffectName() == "potion.hunger") {
            color = -16744448;
         } else if (effect.getEffectName() == "potion.heal") {
            color = -65536;
         } else if (effect.getEffectName() == "potion.harm") {
            color = -3730043;
         } else if (effect.getEffectName() == "potion.fireResistance") {
            color = -29696;
         } else if (effect.getEffectName() == "potion.healthBoost") {
            color = -40121;
         } else if (effect.getEffectName() == "potion.digSpeed") {
            color = -989556;
         } else if (effect.getEffectName() == "potion.digSlowdown") {
            color = -5658199;
         } else if (effect.getEffectName() == "potion.damageBoost") {
            color = -7667712;
         } else if (effect.getEffectName() == "potion.confusion") {
            color = -5192482;
         } else if (effect.getEffectName() == "potion.blindness") {
            color = -8355712;
         } else if (effect.getEffectName() == "potion.absorption") {
            color = -23296;
         }

         int yPos = 1;

         Notification not;
         for(Iterator var10 = Saint.getNotificationManager().getContentList().iterator(); var10.hasNext(); yPos = (int)((float)yPos + RenderHelper.getNahrFont().getStringHeight(not.message))) {
            not = (Notification)var10.next();
         }

         RenderHelper.getNahrFont().drawString(name, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(name) + 18.0F, (float)(((Boolean)saint.getValueState() ? scaledRes.getScaledHeight() - y - 5 : y - 12) - (Saint.getModuleManager().getModuleUsingName("notifications").isEnabled() ? yPos : 1)), NahrFont.FontType.SHADOW_THIN, color, 1090519039);
      }

   }

   private void drawArmorStatus(ScaledResolution scaledRes) {
      if (mc.playerController.isNotCreative()) {
         int x = 15;
         GL11.glPushMatrix();

         for(int index = 3; index >= 0; --index) {
            ItemStack stack = mc.thePlayer.inventory.armorInventory[index];
            if (stack != null) {
               mc.getRenderItem().func_180450_b(stack, scaledRes.getScaledWidth() / 2 + x - 1, scaledRes.getScaledHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water) ? 65 : 55) - 2);
               mc.getRenderItem().func_175030_a(mc.fontRendererObj, stack, scaledRes.getScaledWidth() / 2 + x - 1, scaledRes.getScaledHeight() - (mc.thePlayer.isInsideOfMaterial(Material.water) ? 65 : 55) - 2);
               x += 18;
            }
         }

         GlStateManager.disableCull();
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
         GlStateManager.disableLighting();
         GlStateManager.disableCull();
         GlStateManager.clear(256);
         GL11.glPopMatrix();
      }

   }
}
