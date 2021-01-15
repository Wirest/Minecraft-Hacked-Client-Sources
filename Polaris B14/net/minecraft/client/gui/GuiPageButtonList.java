/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.IntHashMap;
/*     */ 
/*     */ public class GuiPageButtonList extends GuiListExtended
/*     */ {
/*  13 */   private final List<GuiEntry> field_178074_u = Lists.newArrayList();
/*  14 */   private final IntHashMap<Gui> field_178073_v = new IntHashMap();
/*  15 */   private final List<GuiTextField> field_178072_w = Lists.newArrayList();
/*     */   private final GuiListEntry[][] field_178078_x;
/*     */   private int field_178077_y;
/*     */   private GuiResponder field_178076_z;
/*     */   private Gui field_178075_A;
/*     */   
/*     */   public GuiPageButtonList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn, GuiResponder p_i45536_7_, GuiListEntry[]... p_i45536_8_)
/*     */   {
/*  23 */     super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
/*  24 */     this.field_178076_z = p_i45536_7_;
/*  25 */     this.field_178078_x = p_i45536_8_;
/*  26 */     this.field_148163_i = false;
/*  27 */     func_178069_s();
/*  28 */     func_178055_t();
/*     */   }
/*     */   
/*     */   private void func_178069_s() {
/*     */     GuiListEntry[][] arrayOfGuiListEntry;
/*  33 */     int j = (arrayOfGuiListEntry = this.field_178078_x).length; for (int i = 0; i < j; i++) { GuiListEntry[] aguipagebuttonlist$guilistentry = arrayOfGuiListEntry[i];
/*     */       
/*  35 */       for (int i = 0; i < aguipagebuttonlist$guilistentry.length; i += 2)
/*     */       {
/*  37 */         GuiListEntry guipagebuttonlist$guilistentry = aguipagebuttonlist$guilistentry[i];
/*  38 */         GuiListEntry guipagebuttonlist$guilistentry1 = i < aguipagebuttonlist$guilistentry.length - 1 ? aguipagebuttonlist$guilistentry[(i + 1)] : null;
/*  39 */         Gui gui = func_178058_a(guipagebuttonlist$guilistentry, 0, guipagebuttonlist$guilistentry1 == null);
/*  40 */         Gui gui1 = func_178058_a(guipagebuttonlist$guilistentry1, 160, guipagebuttonlist$guilistentry == null);
/*  41 */         GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui1);
/*  42 */         this.field_178074_u.add(guipagebuttonlist$guientry);
/*     */         
/*  44 */         if ((guipagebuttonlist$guilistentry != null) && (gui != null))
/*     */         {
/*  46 */           this.field_178073_v.addKey(guipagebuttonlist$guilistentry.func_178935_b(), gui);
/*     */           
/*  48 */           if ((gui instanceof GuiTextField))
/*     */           {
/*  50 */             this.field_178072_w.add((GuiTextField)gui);
/*     */           }
/*     */         }
/*     */         
/*  54 */         if ((guipagebuttonlist$guilistentry1 != null) && (gui1 != null))
/*     */         {
/*  56 */           this.field_178073_v.addKey(guipagebuttonlist$guilistentry1.func_178935_b(), gui1);
/*     */           
/*  58 */           if ((gui1 instanceof GuiTextField))
/*     */           {
/*  60 */             this.field_178072_w.add((GuiTextField)gui1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_178055_t()
/*     */   {
/*  69 */     this.field_178074_u.clear();
/*     */     
/*  71 */     for (int i = 0; i < this.field_178078_x[this.field_178077_y].length; i += 2)
/*     */     {
/*  73 */       GuiListEntry guipagebuttonlist$guilistentry = this.field_178078_x[this.field_178077_y][i];
/*  74 */       GuiListEntry guipagebuttonlist$guilistentry1 = i < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][(i + 1)] : null;
/*  75 */       Gui gui = (Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b());
/*  76 */       Gui gui1 = guipagebuttonlist$guilistentry1 != null ? (Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry1.func_178935_b()) : null;
/*  77 */       GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui1);
/*  78 */       this.field_178074_u.add(guipagebuttonlist$guientry);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_181156_c(int p_181156_1_)
/*     */   {
/*  84 */     if (p_181156_1_ != this.field_178077_y)
/*     */     {
/*  86 */       int i = this.field_178077_y;
/*  87 */       this.field_178077_y = p_181156_1_;
/*  88 */       func_178055_t();
/*  89 */       func_178060_e(i, p_181156_1_);
/*  90 */       this.amountScrolled = 0.0F;
/*     */     }
/*     */   }
/*     */   
/*     */   public int func_178059_e()
/*     */   {
/*  96 */     return this.field_178077_y;
/*     */   }
/*     */   
/*     */   public int func_178057_f()
/*     */   {
/* 101 */     return this.field_178078_x.length;
/*     */   }
/*     */   
/*     */   public Gui func_178056_g()
/*     */   {
/* 106 */     return this.field_178075_A;
/*     */   }
/*     */   
/*     */   public void func_178071_h()
/*     */   {
/* 111 */     if (this.field_178077_y > 0)
/*     */     {
/* 113 */       func_181156_c(this.field_178077_y - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_178064_i()
/*     */   {
/* 119 */     if (this.field_178077_y < this.field_178078_x.length - 1)
/*     */     {
/* 121 */       func_181156_c(this.field_178077_y + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public Gui func_178061_c(int p_178061_1_)
/*     */   {
/* 127 */     return (Gui)this.field_178073_v.lookup(p_178061_1_);
/*     */   }
/*     */   
/*     */   private void func_178060_e(int p_178060_1_, int p_178060_2_) {
/*     */     GuiListEntry[] arrayOfGuiListEntry;
/* 132 */     int j = (arrayOfGuiListEntry = this.field_178078_x[p_178060_1_]).length; for (int i = 0; i < j; i++) { GuiListEntry guipagebuttonlist$guilistentry = arrayOfGuiListEntry[i];
/*     */       
/* 134 */       if (guipagebuttonlist$guilistentry != null)
/*     */       {
/* 136 */         func_178066_a((Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b()), false);
/*     */       }
/*     */     }
/*     */     
/* 140 */     j = (arrayOfGuiListEntry = this.field_178078_x[p_178060_2_]).length; for (i = 0; i < j; i++) { GuiListEntry guipagebuttonlist$guilistentry1 = arrayOfGuiListEntry[i];
/*     */       
/* 142 */       if (guipagebuttonlist$guilistentry1 != null)
/*     */       {
/* 144 */         func_178066_a((Gui)this.field_178073_v.lookup(guipagebuttonlist$guilistentry1.func_178935_b()), true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_178066_a(Gui p_178066_1_, boolean p_178066_2_)
/*     */   {
/* 151 */     if ((p_178066_1_ instanceof GuiButton))
/*     */     {
/* 153 */       ((GuiButton)p_178066_1_).visible = p_178066_2_;
/*     */     }
/* 155 */     else if ((p_178066_1_ instanceof GuiTextField))
/*     */     {
/* 157 */       ((GuiTextField)p_178066_1_).setVisible(p_178066_2_);
/*     */     }
/* 159 */     else if ((p_178066_1_ instanceof GuiLabel))
/*     */     {
/* 161 */       ((GuiLabel)p_178066_1_).visible = p_178066_2_;
/*     */     }
/*     */   }
/*     */   
/*     */   private Gui func_178058_a(GuiListEntry p_178058_1_, int p_178058_2_, boolean p_178058_3_)
/*     */   {
/* 167 */     return (p_178058_1_ instanceof GuiLabelEntry) ? func_178063_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiLabelEntry)p_178058_1_, p_178058_3_) : (p_178058_1_ instanceof EditBoxEntry) ? func_178068_a(this.width / 2 - 155 + p_178058_2_, 0, (EditBoxEntry)p_178058_1_) : (p_178058_1_ instanceof GuiButtonEntry) ? func_178065_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiButtonEntry)p_178058_1_) : (p_178058_1_ instanceof GuiSlideEntry) ? func_178067_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiSlideEntry)p_178058_1_) : null;
/*     */   }
/*     */   
/*     */   public void func_181155_a(boolean p_181155_1_)
/*     */   {
/* 172 */     for (GuiEntry guipagebuttonlist$guientry : this.field_178074_u)
/*     */     {
/* 174 */       if ((guipagebuttonlist$guientry.field_178029_b instanceof GuiButton))
/*     */       {
/* 176 */         ((GuiButton)guipagebuttonlist$guientry.field_178029_b).enabled = p_181155_1_;
/*     */       }
/*     */       
/* 179 */       if ((guipagebuttonlist$guientry.field_178030_c instanceof GuiButton))
/*     */       {
/* 181 */         ((GuiButton)guipagebuttonlist$guientry.field_178030_c).enabled = p_181155_1_;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent)
/*     */   {
/* 188 */     boolean flag = super.mouseClicked(mouseX, mouseY, mouseEvent);
/* 189 */     int i = getSlotIndexFromScreenCoords(mouseX, mouseY);
/*     */     
/* 191 */     if (i >= 0)
/*     */     {
/* 193 */       GuiEntry guipagebuttonlist$guientry = getListEntry(i);
/*     */       
/* 195 */       if ((this.field_178075_A != guipagebuttonlist$guientry.field_178028_d) && (this.field_178075_A != null) && ((this.field_178075_A instanceof GuiTextField)))
/*     */       {
/* 197 */         ((GuiTextField)this.field_178075_A).setFocused(false);
/*     */       }
/*     */       
/* 200 */       this.field_178075_A = guipagebuttonlist$guientry.field_178028_d;
/*     */     }
/*     */     
/* 203 */     return flag;
/*     */   }
/*     */   
/*     */   private GuiSlider func_178067_a(int p_178067_1_, int p_178067_2_, GuiSlideEntry p_178067_3_)
/*     */   {
/* 208 */     GuiSlider guislider = new GuiSlider(this.field_178076_z, p_178067_3_.func_178935_b(), p_178067_1_, p_178067_2_, p_178067_3_.func_178936_c(), p_178067_3_.func_178943_e(), p_178067_3_.func_178944_f(), p_178067_3_.func_178942_g(), p_178067_3_.func_178945_a());
/* 209 */     guislider.visible = p_178067_3_.func_178934_d();
/* 210 */     return guislider;
/*     */   }
/*     */   
/*     */   private GuiListButton func_178065_a(int p_178065_1_, int p_178065_2_, GuiButtonEntry p_178065_3_)
/*     */   {
/* 215 */     GuiListButton guilistbutton = new GuiListButton(this.field_178076_z, p_178065_3_.func_178935_b(), p_178065_1_, p_178065_2_, p_178065_3_.func_178936_c(), p_178065_3_.func_178940_a());
/* 216 */     guilistbutton.visible = p_178065_3_.func_178934_d();
/* 217 */     return guilistbutton;
/*     */   }
/*     */   
/*     */   private GuiTextField func_178068_a(int p_178068_1_, int p_178068_2_, EditBoxEntry p_178068_3_)
/*     */   {
/* 222 */     GuiTextField guitextfield = new GuiTextField(p_178068_3_.func_178935_b(), this.mc.fontRendererObj, p_178068_1_, p_178068_2_, 150, 20);
/* 223 */     guitextfield.setText(p_178068_3_.func_178936_c());
/* 224 */     guitextfield.func_175207_a(this.field_178076_z);
/* 225 */     guitextfield.setVisible(p_178068_3_.func_178934_d());
/* 226 */     guitextfield.func_175205_a(p_178068_3_.func_178950_a());
/* 227 */     return guitextfield;
/*     */   }
/*     */   
/*     */   private GuiLabel func_178063_a(int p_178063_1_, int p_178063_2_, GuiLabelEntry p_178063_3_, boolean p_178063_4_)
/*     */   {
/*     */     GuiLabel guilabel;
/*     */     GuiLabel guilabel;
/* 234 */     if (p_178063_4_)
/*     */     {
/* 236 */       guilabel = new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1);
/*     */     }
/*     */     else
/*     */     {
/* 240 */       guilabel = new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, 150, 20, -1);
/*     */     }
/*     */     
/* 243 */     guilabel.visible = p_178063_3_.func_178934_d();
/* 244 */     guilabel.func_175202_a(p_178063_3_.func_178936_c());
/* 245 */     guilabel.setCentered();
/* 246 */     return guilabel;
/*     */   }
/*     */   
/*     */   public void func_178062_a(char p_178062_1_, int p_178062_2_)
/*     */   {
/* 251 */     if ((this.field_178075_A instanceof GuiTextField))
/*     */     {
/* 253 */       GuiTextField guitextfield = (GuiTextField)this.field_178075_A;
/*     */       
/* 255 */       if (!GuiScreen.isKeyComboCtrlV(p_178062_2_))
/*     */       {
/* 257 */         if (p_178062_2_ == 15)
/*     */         {
/* 259 */           guitextfield.setFocused(false);
/* 260 */           int k = this.field_178072_w.indexOf(this.field_178075_A);
/*     */           
/* 262 */           if (GuiScreen.isShiftKeyDown())
/*     */           {
/* 264 */             if (k == 0)
/*     */             {
/* 266 */               k = this.field_178072_w.size() - 1;
/*     */             }
/*     */             else
/*     */             {
/* 270 */               k--;
/*     */             }
/*     */           }
/* 273 */           else if (k == this.field_178072_w.size() - 1)
/*     */           {
/* 275 */             k = 0;
/*     */           }
/*     */           else
/*     */           {
/* 279 */             k++;
/*     */           }
/*     */           
/* 282 */           this.field_178075_A = ((Gui)this.field_178072_w.get(k));
/* 283 */           guitextfield = (GuiTextField)this.field_178075_A;
/* 284 */           guitextfield.setFocused(true);
/* 285 */           int l = guitextfield.yPosition + this.slotHeight;
/* 286 */           int i1 = guitextfield.yPosition;
/*     */           
/* 288 */           if (l > this.bottom)
/*     */           {
/* 290 */             this.amountScrolled += l - this.bottom;
/*     */           }
/* 292 */           else if (i1 < this.top)
/*     */           {
/* 294 */             this.amountScrolled = i1;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 299 */           guitextfield.textboxKeyTyped(p_178062_1_, p_178062_2_);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 304 */         String s = GuiScreen.getClipboardString();
/* 305 */         String[] astring = s.split(";");
/* 306 */         int i = this.field_178072_w.indexOf(this.field_178075_A);
/* 307 */         int j = i;
/*     */         String[] arrayOfString1;
/* 309 */         int j = (arrayOfString1 = astring).length; for (int i = 0; i < j; i++) { String s1 = arrayOfString1[i];
/*     */           
/* 311 */           ((GuiTextField)this.field_178072_w.get(j)).setText(s1);
/*     */           
/* 313 */           if (j == this.field_178072_w.size() - 1)
/*     */           {
/* 315 */             j = 0;
/*     */           }
/*     */           else
/*     */           {
/* 319 */             j++;
/*     */           }
/*     */           
/* 322 */           if (j == i) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GuiEntry getListEntry(int index)
/*     */   {
/* 336 */     return (GuiEntry)this.field_178074_u.get(index);
/*     */   }
/*     */   
/*     */   public int getSize()
/*     */   {
/* 341 */     return this.field_178074_u.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getListWidth()
/*     */   {
/* 349 */     return 400;
/*     */   }
/*     */   
/*     */   protected int getScrollBarX()
/*     */   {
/* 354 */     return super.getScrollBarX() + 32;
/*     */   }
/*     */   
/*     */   public static class EditBoxEntry extends GuiPageButtonList.GuiListEntry
/*     */   {
/*     */     private final Predicate<String> field_178951_a;
/*     */     
/*     */     public EditBoxEntry(int p_i45534_1_, String p_i45534_2_, boolean p_i45534_3_, Predicate<String> p_i45534_4_)
/*     */     {
/* 363 */       super(p_i45534_2_, p_i45534_3_);
/* 364 */       this.field_178951_a = ((Predicate)Objects.firstNonNull(p_i45534_4_, Predicates.alwaysTrue()));
/*     */     }
/*     */     
/*     */     public Predicate<String> func_178950_a()
/*     */     {
/* 369 */       return this.field_178951_a;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GuiButtonEntry extends GuiPageButtonList.GuiListEntry
/*     */   {
/*     */     private final boolean field_178941_a;
/*     */     
/*     */     public GuiButtonEntry(int p_i45535_1_, String p_i45535_2_, boolean p_i45535_3_, boolean p_i45535_4_)
/*     */     {
/* 379 */       super(p_i45535_2_, p_i45535_3_);
/* 380 */       this.field_178941_a = p_i45535_4_;
/*     */     }
/*     */     
/*     */     public boolean func_178940_a()
/*     */     {
/* 385 */       return this.field_178941_a;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GuiEntry implements GuiListExtended.IGuiListEntry
/*     */   {
/* 391 */     private final Minecraft field_178031_a = Minecraft.getMinecraft();
/*     */     private final Gui field_178029_b;
/*     */     private final Gui field_178030_c;
/*     */     private Gui field_178028_d;
/*     */     
/*     */     public GuiEntry(Gui p_i45533_1_, Gui p_i45533_2_)
/*     */     {
/* 398 */       this.field_178029_b = p_i45533_1_;
/* 399 */       this.field_178030_c = p_i45533_2_;
/*     */     }
/*     */     
/*     */     public Gui func_178022_a()
/*     */     {
/* 404 */       return this.field_178029_b;
/*     */     }
/*     */     
/*     */     public Gui func_178021_b()
/*     */     {
/* 409 */       return this.field_178030_c;
/*     */     }
/*     */     
/*     */     public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
/*     */     {
/* 414 */       func_178017_a(this.field_178029_b, y, mouseX, mouseY, false);
/* 415 */       func_178017_a(this.field_178030_c, y, mouseX, mouseY, false);
/*     */     }
/*     */     
/*     */     private void func_178017_a(Gui p_178017_1_, int p_178017_2_, int p_178017_3_, int p_178017_4_, boolean p_178017_5_)
/*     */     {
/* 420 */       if (p_178017_1_ != null)
/*     */       {
/* 422 */         if ((p_178017_1_ instanceof GuiButton))
/*     */         {
/* 424 */           func_178024_a((GuiButton)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
/*     */         }
/* 426 */         else if ((p_178017_1_ instanceof GuiTextField))
/*     */         {
/* 428 */           func_178027_a((GuiTextField)p_178017_1_, p_178017_2_, p_178017_5_);
/*     */         }
/* 430 */         else if ((p_178017_1_ instanceof GuiLabel))
/*     */         {
/* 432 */           func_178025_a((GuiLabel)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void func_178024_a(GuiButton p_178024_1_, int p_178024_2_, int p_178024_3_, int p_178024_4_, boolean p_178024_5_)
/*     */     {
/* 439 */       p_178024_1_.yPosition = p_178024_2_;
/*     */       
/* 441 */       if (!p_178024_5_)
/*     */       {
/* 443 */         p_178024_1_.drawButton(this.field_178031_a, p_178024_3_, p_178024_4_);
/*     */       }
/*     */     }
/*     */     
/*     */     private void func_178027_a(GuiTextField p_178027_1_, int p_178027_2_, boolean p_178027_3_)
/*     */     {
/* 449 */       p_178027_1_.yPosition = p_178027_2_;
/*     */       
/* 451 */       if (!p_178027_3_)
/*     */       {
/* 453 */         p_178027_1_.drawTextBox();
/*     */       }
/*     */     }
/*     */     
/*     */     private void func_178025_a(GuiLabel p_178025_1_, int p_178025_2_, int p_178025_3_, int p_178025_4_, boolean p_178025_5_)
/*     */     {
/* 459 */       p_178025_1_.field_146174_h = p_178025_2_;
/*     */       
/* 461 */       if (!p_178025_5_)
/*     */       {
/* 463 */         p_178025_1_.drawLabel(this.field_178031_a, p_178025_3_, p_178025_4_);
/*     */       }
/*     */     }
/*     */     
/*     */     public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
/*     */     {
/* 469 */       func_178017_a(this.field_178029_b, p_178011_3_, 0, 0, true);
/* 470 */       func_178017_a(this.field_178030_c, p_178011_3_, 0, 0, true);
/*     */     }
/*     */     
/*     */     public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
/*     */     {
/* 475 */       boolean flag = func_178026_a(this.field_178029_b, p_148278_2_, p_148278_3_, p_148278_4_);
/* 476 */       boolean flag1 = func_178026_a(this.field_178030_c, p_148278_2_, p_148278_3_, p_148278_4_);
/* 477 */       return (flag) || (flag1);
/*     */     }
/*     */     
/*     */     private boolean func_178026_a(Gui p_178026_1_, int p_178026_2_, int p_178026_3_, int p_178026_4_)
/*     */     {
/* 482 */       if (p_178026_1_ == null)
/*     */       {
/* 484 */         return false;
/*     */       }
/* 486 */       if ((p_178026_1_ instanceof GuiButton))
/*     */       {
/* 488 */         return func_178023_a((GuiButton)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
/*     */       }
/*     */       
/*     */ 
/* 492 */       if ((p_178026_1_ instanceof GuiTextField))
/*     */       {
/* 494 */         func_178018_a((GuiTextField)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
/*     */       }
/*     */       
/* 497 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     private boolean func_178023_a(GuiButton p_178023_1_, int p_178023_2_, int p_178023_3_, int p_178023_4_)
/*     */     {
/* 503 */       boolean flag = p_178023_1_.mousePressed(this.field_178031_a, p_178023_2_, p_178023_3_);
/*     */       
/* 505 */       if (flag)
/*     */       {
/* 507 */         this.field_178028_d = p_178023_1_;
/*     */       }
/*     */       
/* 510 */       return flag;
/*     */     }
/*     */     
/*     */     private void func_178018_a(GuiTextField p_178018_1_, int p_178018_2_, int p_178018_3_, int p_178018_4_)
/*     */     {
/* 515 */       p_178018_1_.mouseClicked(p_178018_2_, p_178018_3_, p_178018_4_);
/*     */       
/* 517 */       if (p_178018_1_.isFocused())
/*     */       {
/* 519 */         this.field_178028_d = p_178018_1_;
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
/*     */     {
/* 525 */       func_178016_b(this.field_178029_b, x, y, mouseEvent);
/* 526 */       func_178016_b(this.field_178030_c, x, y, mouseEvent);
/*     */     }
/*     */     
/*     */     private void func_178016_b(Gui p_178016_1_, int p_178016_2_, int p_178016_3_, int p_178016_4_)
/*     */     {
/* 531 */       if (p_178016_1_ != null)
/*     */       {
/* 533 */         if ((p_178016_1_ instanceof GuiButton))
/*     */         {
/* 535 */           func_178019_b((GuiButton)p_178016_1_, p_178016_2_, p_178016_3_, p_178016_4_);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void func_178019_b(GuiButton p_178019_1_, int p_178019_2_, int p_178019_3_, int p_178019_4_)
/*     */     {
/* 542 */       p_178019_1_.mouseReleased(p_178019_2_, p_178019_3_);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GuiLabelEntry extends GuiPageButtonList.GuiListEntry
/*     */   {
/*     */     public GuiLabelEntry(int p_i45532_1_, String p_i45532_2_, boolean p_i45532_3_)
/*     */     {
/* 550 */       super(p_i45532_2_, p_i45532_3_);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GuiListEntry
/*     */   {
/*     */     private final int field_178939_a;
/*     */     private final String field_178937_b;
/*     */     private final boolean field_178938_c;
/*     */     
/*     */     public GuiListEntry(int p_i45531_1_, String p_i45531_2_, boolean p_i45531_3_)
/*     */     {
/* 562 */       this.field_178939_a = p_i45531_1_;
/* 563 */       this.field_178937_b = p_i45531_2_;
/* 564 */       this.field_178938_c = p_i45531_3_;
/*     */     }
/*     */     
/*     */     public int func_178935_b()
/*     */     {
/* 569 */       return this.field_178939_a;
/*     */     }
/*     */     
/*     */     public String func_178936_c()
/*     */     {
/* 574 */       return this.field_178937_b;
/*     */     }
/*     */     
/*     */     public boolean func_178934_d()
/*     */     {
/* 579 */       return this.field_178938_c;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface GuiResponder
/*     */   {
/*     */     public abstract void func_175321_a(int paramInt, boolean paramBoolean);
/*     */     
/*     */     public abstract void onTick(int paramInt, float paramFloat);
/*     */     
/*     */     public abstract void func_175319_a(int paramInt, String paramString);
/*     */   }
/*     */   
/*     */   public static class GuiSlideEntry extends GuiPageButtonList.GuiListEntry
/*     */   {
/*     */     private final GuiSlider.FormatHelper field_178949_a;
/*     */     private final float field_178947_b;
/*     */     private final float field_178948_c;
/*     */     private final float field_178946_d;
/*     */     
/*     */     public GuiSlideEntry(int p_i45530_1_, String p_i45530_2_, boolean p_i45530_3_, GuiSlider.FormatHelper p_i45530_4_, float p_i45530_5_, float p_i45530_6_, float p_i45530_7_)
/*     */     {
/* 601 */       super(p_i45530_2_, p_i45530_3_);
/* 602 */       this.field_178949_a = p_i45530_4_;
/* 603 */       this.field_178947_b = p_i45530_5_;
/* 604 */       this.field_178948_c = p_i45530_6_;
/* 605 */       this.field_178946_d = p_i45530_7_;
/*     */     }
/*     */     
/*     */     public GuiSlider.FormatHelper func_178945_a()
/*     */     {
/* 610 */       return this.field_178949_a;
/*     */     }
/*     */     
/*     */     public float func_178943_e()
/*     */     {
/* 615 */       return this.field_178947_b;
/*     */     }
/*     */     
/*     */     public float func_178944_f()
/*     */     {
/* 620 */       return this.field_178948_c;
/*     */     }
/*     */     
/*     */     public float func_178942_g()
/*     */     {
/* 625 */       return this.field_178946_d;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiPageButtonList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */