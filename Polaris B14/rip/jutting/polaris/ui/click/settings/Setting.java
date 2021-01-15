/*     */ package rip.jutting.polaris.ui.click.settings;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Setting
/*     */ {
/*     */   private String name;
/*     */   private Module parent;
/*     */   private String mode;
/*     */   private String sval;
/*     */   private ArrayList<String> options;
/*     */   private boolean bval;
/*     */   private double dval;
/*     */   private double min;
/*     */   private double max;
/*  28 */   private boolean onlyint = false;
/*     */   
/*     */   public Setting(String name, Module parent, String sval, ArrayList<String> options)
/*     */   {
/*  32 */     this.name = name;
/*  33 */     this.parent = parent;
/*  34 */     this.sval = sval;
/*  35 */     this.options = options;
/*  36 */     this.mode = "Combo";
/*     */   }
/*     */   
/*     */   public Setting(String name, Module parent, boolean bval) {
/*  40 */     this.name = name;
/*  41 */     this.parent = parent;
/*  42 */     this.bval = bval;
/*  43 */     this.mode = "Check";
/*     */   }
/*     */   
/*     */   public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint) {
/*  47 */     this.name = name;
/*  48 */     this.parent = parent;
/*  49 */     this.dval = dval;
/*  50 */     this.min = min;
/*  51 */     this.max = max;
/*  52 */     this.onlyint = onlyint;
/*  53 */     this.mode = "Slider";
/*     */   }
/*     */   
/*     */   public String getName() {
/*  57 */     return this.name;
/*     */   }
/*     */   
/*     */   public Module getParentMod() {
/*  61 */     return this.parent;
/*     */   }
/*     */   
/*     */   public String getValString() {
/*  65 */     return this.sval;
/*     */   }
/*     */   
/*     */   public void setValString(String in) {
/*  69 */     this.sval = in;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getOptions() {
/*  73 */     return this.options;
/*     */   }
/*     */   
/*     */   public boolean getValBoolean() {
/*  77 */     return this.bval;
/*     */   }
/*     */   
/*     */   public void setValBoolean(boolean in) {
/*  81 */     this.bval = in;
/*     */   }
/*     */   
/*     */   public double getValDouble() {
/*  85 */     if (this.onlyint) {
/*  86 */       this.dval = ((int)this.dval);
/*     */     }
/*  88 */     return this.dval;
/*     */   }
/*     */   
/*     */   public void setValDouble(double in) {
/*  92 */     this.dval = in;
/*     */   }
/*     */   
/*     */   public double getMin() {
/*  96 */     return this.min;
/*     */   }
/*     */   
/*     */   public double getMax() {
/* 100 */     return this.max;
/*     */   }
/*     */   
/*     */   public boolean isCombo() {
/* 104 */     return this.mode.equalsIgnoreCase("Combo");
/*     */   }
/*     */   
/*     */   public boolean isCheck() {
/* 108 */     return this.mode.equalsIgnoreCase("Check");
/*     */   }
/*     */   
/*     */   public boolean isSlider() {
/* 112 */     return this.mode.equalsIgnoreCase("Slider");
/*     */   }
/*     */   
/*     */   public boolean onlyInt() {
/* 116 */     return this.onlyint;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\settings\Setting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */