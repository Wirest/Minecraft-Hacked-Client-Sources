/*    */ package rip.jutting.polaris.module;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.EventManager;
/*    */ 
/*    */ public class Module
/*    */ {
/* 11 */   protected static Minecraft mc = ;
/*    */   public String name;
/*    */   public String displayName;
/*    */   public int key;
/*    */   private Category category;
/*    */   private boolean toggled;
/*    */   public int anim;
/*    */   private int color;
/* 19 */   List<Module> mods = new ArrayList();
/*    */   public boolean visible;
/*    */   
/*    */   public Module(String name, int key, Category category)
/*    */   {
/* 24 */     this.name = name;
/* 25 */     this.key = key;
/* 26 */     this.category = category;
/* 27 */     this.toggled = false;
/* 28 */     setup();
/* 29 */     this.anim = 0;
/*    */   }
/*    */   
/*    */   public void onEnable() {
/* 33 */     if (this.anim != 0) {
/* 34 */       this.anim = 0;
/*    */     }
/* 36 */     setColor(Polaris.getRandomRGB(0.5D, 1.0D, 1.0F));
/*    */     
/* 38 */     Polaris.instance.eventManager.register(this);
/*    */   }
/*    */   
/* 41 */   public void onDisable() { Polaris.instance.eventManager.unregister(this);
/* 42 */     if (this.anim != 0) {
/* 43 */       this.anim = 0;
/*    */     }
/*    */   }
/*    */   
/*    */   public void onToggle() {
/* 48 */     if (this.anim != 0) {
/* 49 */       this.anim = 0;
/*    */     }
/* 51 */     setColor(Polaris.getRandomRGB(0.35D, 1.0D, 1.0F));
/*    */   }
/*    */   
/*    */   public void toggle() {
/* 55 */     this.toggled = (!this.toggled);
/* 56 */     onToggle();
/* 57 */     if (this.toggled) {
/* 58 */       onEnable();
/*    */     } else
/* 60 */       onDisable();
/*    */   }
/*    */   
/* 63 */   public Module module(String mod) { return Polaris.instance.moduleManager.getModuleByName(mod); }
/*    */   
/*    */   public rip.jutting.polaris.ui.click.settings.Setting setting(String set) {
/* 66 */     return Polaris.instance.settingsManager.getSettingByName(set);
/*    */   }
/*    */   
/* 69 */   public String getName() { return this.name; }
/*    */   
/*    */   public void setName(String name) {
/* 72 */     this.name = name;
/*    */   }
/*    */   
/* 75 */   public void setColor(int color) { this.color = color; }
/*    */   
/*    */   public int getColor() {
/* 78 */     return this.color;
/*    */   }
/*    */   
/* 81 */   public int getKey() { return this.key; }
/*    */   
/*    */   public void setKey(int key) {
/* 84 */     this.key = key;
/*    */   }
/*    */   
/* 87 */   public Category getCategory() { return this.category; }
/*    */   
/*    */   public void setCategory(Category category) {
/* 90 */     this.category = category;
/*    */   }
/*    */   
/* 93 */   public boolean isToggled() { return this.toggled; }
/*    */   
/*    */   public String getDisplayName() {
/* 96 */     return this.displayName == null ? this.name : this.displayName;
/*    */   }
/*    */   
/* 99 */   public void setDisplayName(String displayName) { this.displayName = displayName; }
/*    */   
/*    */   public void setup() {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */