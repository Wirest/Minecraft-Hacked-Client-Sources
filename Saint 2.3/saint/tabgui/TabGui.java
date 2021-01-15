package saint.tabgui;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import saint.Saint;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.tabgui.items.Tab;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class TabGui {
   public String colorNormal = "§f";
   public int guiWidth;
   public int guiHeight = 0;
   public int tabHeight = 12;
   public static int selectedTab = 0;
   public static int selectedItem = 0;
   public static boolean mainMenu = true;
   public static ArrayList tabsList;
   private Minecraft mc;

   public TabGui(Minecraft minecraft) {
      this.mc = minecraft;
      tabsList = new ArrayList();
      Tab tabCombat = new Tab(this, "Combat");
      Iterator var4 = Saint.getModuleManager().getContentList().iterator();

      while(var4.hasNext()) {
         Module module = (Module)var4.next();
         if (module.getCategory() == ModManager.Category.COMBAT) {
            tabCombat.hacks.add(module);
         }
      }

      tabsList.add(tabCombat);
      Tab tabPlayer = new Tab(this, "Exploits");
      Iterator var5 = Saint.getModuleManager().getContentList().iterator();

      while(var5.hasNext()) {
         Module module = (Module)var5.next();
         if (module.getCategory() == ModManager.Category.EXPLOITS) {
            tabPlayer.hacks.add(module);
         }
      }

      tabsList.add(tabPlayer);
      Tab tabInvisible = new Tab(this, "Miscellaneous");
      Iterator var6 = Saint.getModuleManager().getContentList().iterator();

      while(var6.hasNext()) {
         Module module = (Module)var6.next();
         if (module.getCategory() == ModManager.Category.INVISIBLE) {
            tabInvisible.hacks.add(module);
         }
      }

      tabsList.add(tabInvisible);
      Tab tabMove = new Tab(this, "Movement");
      Iterator var7 = Saint.getModuleManager().getContentList().iterator();

      while(var7.hasNext()) {
         Module module = (Module)var7.next();
         if (module.getCategory() == ModManager.Category.MOVEMENT) {
            tabMove.hacks.add(module);
         }
      }

      tabsList.add(tabMove);
      Tab tabMovement = new Tab(this, "Player");
      Iterator var8 = Saint.getModuleManager().getContentList().iterator();

      while(var8.hasNext()) {
         Module module = (Module)var8.next();
         if (module.getCategory() == ModManager.Category.PLAYER) {
            tabMovement.hacks.add(module);
         }
      }

      tabsList.add(tabMovement);
      Tab tabRender = new Tab(this, "Render");
      Iterator var9 = Saint.getModuleManager().getContentList().iterator();

      while(var9.hasNext()) {
         Module module = (Module)var9.next();
         if (module.getCategory() == ModManager.Category.RENDER) {
            tabRender.hacks.add(module);
         }
      }

      tabsList.add(tabRender);
      Tab tabWorld = new Tab(this, "World");
      Iterator var10 = Saint.getModuleManager().getContentList().iterator();

      while(var10.hasNext()) {
         Module module = (Module)var10.next();
         if (module.getCategory() == ModManager.Category.WORLD) {
            tabWorld.hacks.add(module);
         }
      }

      tabsList.add(tabWorld);
      this.guiHeight = this.tabHeight + tabsList.size() * this.tabHeight;
   }

   public void drawGui(int posY, int posX, int width) {
      int x = posY;
      int y = posX;
      this.guiWidth = width;
      RenderHelper.drawRect((float)(posY - 1), (float)(posX - 1), (float)(posY + this.guiWidth), (float)(posX + this.guiHeight - 13), -1879048192);
      RenderHelper.drawRect((float)(posY - 2), (float)(posX - 1), (float)(posY + this.guiWidth + 1), (float)posX, -16777216);
      RenderHelper.drawRect((float)(posY - 2), (float)(posX - 1), (float)(posY - 1), (float)(posX + this.guiHeight - 12), -16777216);
      RenderHelper.drawRect((float)(posY - 2), (float)(posX + this.guiHeight - 13), (float)(posY + this.guiWidth + 1), (float)(posX + this.guiHeight - 12), -16777216);
      RenderHelper.drawRect((float)(posY + this.guiWidth), (float)(posX - 1), (float)(posY + this.guiWidth + 1), (float)(posX + this.guiHeight - 12), -16777216);
      int yOff = posX + 1;
      int[] var10000 = new int[]{34867, -13369498, -6723841, -10040065, -43691, 65382};

      for(int i = 0; i < tabsList.size(); ++i) {
         RenderHelper.drawRect((float)(x - 1), (float)(yOff - 1), (float)(x + this.guiWidth), (float)(y + this.tabHeight * i + 11), i == selectedTab ? -2145320961 : 0);
         RenderHelper.getNahrFont().drawString("§f" + ((Tab)tabsList.get(i)).tabName, (float)(x + 2), (float)(yOff - 4), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
         if (i == selectedTab && !mainMenu) {
            ((Tab)tabsList.get(i)).drawTabMenu(x + this.guiWidth + 2, yOff - 2);
         }

         yOff += this.tabHeight;
      }

   }

   public static void parseKeyUp() {
      if (mainMenu) {
         --selectedTab;
         if (selectedTab < 0) {
            selectedTab = tabsList.size() - 1;
         }
      } else {
         --selectedItem;
         if (selectedItem < 0) {
            selectedItem = ((Tab)tabsList.get(selectedTab)).hacks.size() - 1;
         }
      }

   }

   public static void parseKeyDown() {
      if (mainMenu) {
         ++selectedTab;
         if (selectedTab > tabsList.size() - 1) {
            selectedTab = 0;
         }
      } else {
         ++selectedItem;
         if (selectedItem > ((Tab)tabsList.get(selectedTab)).hacks.size() - 1) {
            selectedItem = 0;
         }
      }

   }

   public static void parseKeyLeft() {
      if (!mainMenu) {
         mainMenu = true;
      }

   }

   public static void parseKeyRight() {
      if (mainMenu) {
         mainMenu = false;
         selectedItem = 0;
      }

   }

   public static void parseKeyToggle() {
      if (!mainMenu) {
         int sel = selectedItem;
         ((Module)((Tab)tabsList.get(selectedTab)).hacks.get(sel)).toggle();
      }

   }
}
