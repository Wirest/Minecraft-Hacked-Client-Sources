package org.m0jang.crystal.Mod;

import com.darkmagician6.eventapi.EventManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class Module {
   private final List subModuleList;
   private boolean hasSubModule;
   private String name;
   private String renderName;
   private Value mode;
   private int bind;
   private Category category;
   private boolean isEnabled;
   protected Minecraft mc;
   TimeHelper antiNoise;

   public Module(String name, Category cat, Value mode) {
      this(name, cat, false);
      this.mode = mode;
   }

   public Module(String name, Category cat, boolean hasSubModule, SubModule... subModules) {
      this.mc = Minecraft.getMinecraft();
      this.antiNoise = new TimeHelper();
      this.subModuleList = new ArrayList();
      this.name = name;
      this.bind = 0;
      this.category = cat;
      this.name = name;
      this.renderName = name;
      this.hasSubModule = hasSubModule;
      if (hasSubModule) {
         SubModule defaultSubModule = null;
         SubModule[] var9 = subModules;
         int var8 = subModules.length;

         for(int var7 = 0; var7 < var8; ++var7) {
            SubModule item = var9[var7];
            if (defaultSubModule == null) {
               defaultSubModule = item;
            }

            this.addSubModule(item);
         }

         List submodnames = new ArrayList();
         if (!this.subModuleList.isEmpty()) {
            Iterator var12 = this.subModuleList.iterator();

            while(var12.hasNext()) {
               SubModule mod = (SubModule)var12.next();
               submodnames.add(mod.getName());
            }
         }

         this.mode = new Value(this.name, String.class, "Mode", defaultSubModule.getName(), (String[])submodnames.toArray(new String[submodnames.size()]));
      }
   }

   public void toggle() {
      this.setEnabled(!this.isEnabled);
      Crystal.INSTANCE.getConfig().saveMods();
   }

   public void onEnable() {
      EventManager.register(this);
      if (this.hasSubModule) {
         this.updateSubModule();
      }

   }

   public void onDisable() {
      EventManager.unregister(this);
      if (this.hasSubModule) {
         this.updateSubModule();
      }

   }

   public void setEnabled(boolean enabled) {
      if (enabled) {
         this.isEnabled = true;
         this.onEnable();
         if (Minecraft.theWorld != null && this.antiNoise.hasPassed(100.0D)) {
            Minecraft.thePlayer.playSound("random.click", 0.5F, 1.0F);
            this.antiNoise.reset();
         }
      } else {
         this.isEnabled = false;
         this.onDisable();
         if (Minecraft.theWorld != null && this.antiNoise.hasPassed(100.0D)) {
            Minecraft.thePlayer.playSound("random.click", 0.4F, 0.8F);
            this.antiNoise.reset();
         }
      }

   }

   private void addSubModule(SubModule subMod) {
      if (!this.doesSubModuleExist(subMod.getName())) {
         System.out.println("Loaded SubModule [" + subMod.getParentModName() + ", " + subMod.getName() + "]");
         this.subModuleList.add(subMod);
      }

   }

   private boolean doesSubModuleExist(String submoduleName) {
      Iterator var3 = this.subModuleList.iterator();

      while(var3.hasNext()) {
         SubModule subModule = (SubModule)var3.next();
         if (subModule.getName().equalsIgnoreCase(submoduleName)) {
            return true;
         }
      }

      return false;
   }

   public boolean setSubModule(String subMod) {
      if (!this.doesSubModuleExist(subMod)) {
         return false;
      } else {
         Iterator var3 = this.subModuleList.iterator();

         while(var3.hasNext()) {
            SubModule subModule = (SubModule)var3.next();
            if (subModule.getName().equalsIgnoreCase(subMod)) {
               this.mode.setSelectedOption(subModule.getName());
            }
         }

         this.updateSubModule();
         return true;
      }
   }

   public void updateSubModule() {
      if (this.hasSubModule) {
         Iterator var2 = this.subModuleList.iterator();

         SubModule subModule;
         while(var2.hasNext()) {
            subModule = (SubModule)var2.next();

            try {
               subModule.setEnabled(false);
            } catch (Exception var4) {
               ;
            }
         }

         if (this.doesSubModuleExist(this.mode.getSelectedOption())) {
            if (this.isEnabled) {
               var2 = this.subModuleList.iterator();

               while(var2.hasNext()) {
                  subModule = (SubModule)var2.next();
                  if (subModule.getName().equalsIgnoreCase(this.mode.getSelectedOption())) {
                     subModule.setEnabled(true);
                     break;
                  }
               }

            }
         }
      }
   }

   public String getCurrnetSubModule() {
      return this.mode.getSelectedOption();
   }

   public boolean isHasSubModule() {
      return this.hasSubModule;
   }

   public String getName() {
      return this.name;
   }

   public String getRenderName() {
      return this.renderName;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setRenderName(String renderName) {
      this.renderName = renderName;
   }

   public Value getMode() {
      return this.mode;
   }

   public int getBind() {
      return this.bind;
   }

   public void setBind(int bind) {
      this.bind = bind;
   }

   public Category getCategory() {
      return this.category;
   }

   public void setCategory(Category category) {
      this.category = category;
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public Minecraft getMc() {
      return this.mc;
   }

   public void setMc(Minecraft mc) {
      this.mc = mc;
   }
}
