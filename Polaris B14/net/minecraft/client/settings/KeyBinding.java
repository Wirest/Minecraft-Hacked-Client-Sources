/*     */ package net.minecraft.client.settings;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.IntHashMap;
/*     */ 
/*     */ public class KeyBinding implements Comparable<KeyBinding>
/*     */ {
/*  12 */   private static final List<KeyBinding> keybindArray = ;
/*  13 */   private static final IntHashMap<KeyBinding> hash = new IntHashMap();
/*  14 */   private static final Set<String> keybindSet = Sets.newHashSet();
/*     */   
/*     */   private final String keyDescription;
/*     */   
/*     */   private final int keyCodeDefault;
/*     */   private final String keyCategory;
/*     */   private int keyCode;
/*     */   public boolean pressed;
/*     */   private int pressTime;
/*     */   
/*     */   public static void onTick(int keyCode)
/*     */   {
/*  26 */     if (keyCode != 0)
/*     */     {
/*  28 */       KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);
/*     */       
/*  30 */       if (keybinding != null)
/*     */       {
/*  32 */         keybinding.pressTime += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setKeyBindState(int keyCode, boolean pressed)
/*     */   {
/*  39 */     if (keyCode != 0)
/*     */     {
/*  41 */       KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);
/*     */       
/*  43 */       if (keybinding != null)
/*     */       {
/*  45 */         keybinding.pressed = pressed;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void unPressAllKeys()
/*     */   {
/*  52 */     for (KeyBinding keybinding : keybindArray)
/*     */     {
/*  54 */       keybinding.unpressKey();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void resetKeyBindingArrayAndHash()
/*     */   {
/*  60 */     hash.clearMap();
/*     */     
/*  62 */     for (KeyBinding keybinding : keybindArray)
/*     */     {
/*  64 */       hash.addKey(keybinding.keyCode, keybinding);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Set<String> getKeybinds()
/*     */   {
/*  70 */     return keybindSet;
/*     */   }
/*     */   
/*     */   public KeyBinding(String description, int keyCode, String category)
/*     */   {
/*  75 */     this.keyDescription = description;
/*  76 */     this.keyCode = keyCode;
/*  77 */     this.keyCodeDefault = keyCode;
/*  78 */     this.keyCategory = category;
/*  79 */     keybindArray.add(this);
/*  80 */     hash.addKey(keyCode, this);
/*  81 */     keybindSet.add(category);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isKeyDown()
/*     */   {
/*  89 */     return this.pressed;
/*     */   }
/*     */   
/*     */   public String getKeyCategory()
/*     */   {
/*  94 */     return this.keyCategory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPressed()
/*     */   {
/* 103 */     if (this.pressTime == 0)
/*     */     {
/* 105 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 109 */     this.pressTime -= 1;
/* 110 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void unpressKey()
/*     */   {
/* 116 */     this.pressTime = 0;
/* 117 */     this.pressed = false;
/*     */   }
/*     */   
/*     */   public String getKeyDescription()
/*     */   {
/* 122 */     return this.keyDescription;
/*     */   }
/*     */   
/*     */   public int getKeyCodeDefault()
/*     */   {
/* 127 */     return this.keyCodeDefault;
/*     */   }
/*     */   
/*     */   public int getKeyCode()
/*     */   {
/* 132 */     return this.keyCode;
/*     */   }
/*     */   
/*     */   public void setKeyCode(int keyCode)
/*     */   {
/* 137 */     this.keyCode = keyCode;
/*     */   }
/*     */   
/*     */   public int compareTo(KeyBinding p_compareTo_1_)
/*     */   {
/* 142 */     int i = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));
/*     */     
/* 144 */     if (i == 0)
/*     */     {
/* 146 */       i = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
/*     */     }
/*     */     
/* 149 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\settings\KeyBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */