/*     */ package net.minecraft.client.gui.spectator;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiSpectator;
/*     */ import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class SpectatorMenu
/*     */ {
/*  15 */   private static final ISpectatorMenuObject field_178655_b = new EndSpectatorObject(null);
/*  16 */   private static final ISpectatorMenuObject field_178656_c = new MoveMenuObject(-1, true);
/*  17 */   private static final ISpectatorMenuObject field_178653_d = new MoveMenuObject(1, true);
/*  18 */   private static final ISpectatorMenuObject field_178654_e = new MoveMenuObject(1, false);
/*  19 */   public static final ISpectatorMenuObject field_178657_a = new ISpectatorMenuObject()
/*     */   {
/*     */     public void func_178661_a(SpectatorMenu menu) {}
/*     */     
/*     */ 
/*     */     public IChatComponent getSpectatorName()
/*     */     {
/*  26 */       return new ChatComponentText("");
/*     */     }
/*     */     
/*     */     public void func_178663_a(float p_178663_1_, int alpha) {}
/*     */     
/*     */     public boolean func_178662_A_()
/*     */     {
/*  33 */       return false;
/*     */     }
/*     */   };
/*     */   private final ISpectatorMenuRecipient field_178651_f;
/*  37 */   private final List<SpectatorDetails> field_178652_g = Lists.newArrayList();
/*  38 */   private ISpectatorMenuView field_178659_h = new BaseSpectatorGroup();
/*  39 */   private int field_178660_i = -1;
/*     */   private int field_178658_j;
/*     */   
/*     */   public SpectatorMenu(ISpectatorMenuRecipient p_i45497_1_)
/*     */   {
/*  44 */     this.field_178651_f = p_i45497_1_;
/*     */   }
/*     */   
/*     */   public ISpectatorMenuObject func_178643_a(int p_178643_1_)
/*     */   {
/*  49 */     int i = p_178643_1_ + this.field_178658_j * 6;
/*  50 */     return (i >= 0) && (i < this.field_178659_h.func_178669_a().size()) ? (ISpectatorMenuObject)com.google.common.base.Objects.firstNonNull((ISpectatorMenuObject)this.field_178659_h.func_178669_a().get(i), field_178657_a) : p_178643_1_ == 8 ? field_178655_b : p_178643_1_ == 7 ? field_178654_e : i < this.field_178659_h.func_178669_a().size() ? field_178653_d : (this.field_178658_j > 0) && (p_178643_1_ == 0) ? field_178656_c : field_178657_a;
/*     */   }
/*     */   
/*     */   public List<ISpectatorMenuObject> func_178642_a()
/*     */   {
/*  55 */     List<ISpectatorMenuObject> list = Lists.newArrayList();
/*     */     
/*  57 */     for (int i = 0; i <= 8; i++)
/*     */     {
/*  59 */       list.add(func_178643_a(i));
/*     */     }
/*     */     
/*  62 */     return list;
/*     */   }
/*     */   
/*     */   public ISpectatorMenuObject func_178645_b()
/*     */   {
/*  67 */     return func_178643_a(this.field_178660_i);
/*     */   }
/*     */   
/*     */   public ISpectatorMenuView func_178650_c()
/*     */   {
/*  72 */     return this.field_178659_h;
/*     */   }
/*     */   
/*     */   public void func_178644_b(int p_178644_1_)
/*     */   {
/*  77 */     ISpectatorMenuObject ispectatormenuobject = func_178643_a(p_178644_1_);
/*     */     
/*  79 */     if (ispectatormenuobject != field_178657_a)
/*     */     {
/*  81 */       if ((this.field_178660_i == p_178644_1_) && (ispectatormenuobject.func_178662_A_()))
/*     */       {
/*  83 */         ispectatormenuobject.func_178661_a(this);
/*     */       }
/*     */       else
/*     */       {
/*  87 */         this.field_178660_i = p_178644_1_;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_178641_d()
/*     */   {
/*  94 */     this.field_178651_f.func_175257_a(this);
/*     */   }
/*     */   
/*     */   public int func_178648_e()
/*     */   {
/*  99 */     return this.field_178660_i;
/*     */   }
/*     */   
/*     */   public void func_178647_a(ISpectatorMenuView p_178647_1_)
/*     */   {
/* 104 */     this.field_178652_g.add(func_178646_f());
/* 105 */     this.field_178659_h = p_178647_1_;
/* 106 */     this.field_178660_i = -1;
/* 107 */     this.field_178658_j = 0;
/*     */   }
/*     */   
/*     */   public SpectatorDetails func_178646_f()
/*     */   {
/* 112 */     return new SpectatorDetails(this.field_178659_h, func_178642_a(), this.field_178660_i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class EndSpectatorObject
/*     */     implements ISpectatorMenuObject
/*     */   {
/*     */     public void func_178661_a(SpectatorMenu menu)
/*     */     {
/* 123 */       menu.func_178641_d();
/*     */     }
/*     */     
/*     */     public IChatComponent getSpectatorName()
/*     */     {
/* 128 */       return new ChatComponentText("Close menu");
/*     */     }
/*     */     
/*     */     public void func_178663_a(float p_178663_1_, int alpha)
/*     */     {
/* 133 */       Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
/* 134 */       Gui.drawModalRectWithCustomSizedTexture(0, 0, 128.0F, 0.0F, 16, 16, 256.0F, 256.0F);
/*     */     }
/*     */     
/*     */     public boolean func_178662_A_()
/*     */     {
/* 139 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static class MoveMenuObject implements ISpectatorMenuObject
/*     */   {
/*     */     private final int field_178666_a;
/*     */     private final boolean field_178665_b;
/*     */     
/*     */     public MoveMenuObject(int p_i45495_1_, boolean p_i45495_2_)
/*     */     {
/* 150 */       this.field_178666_a = p_i45495_1_;
/* 151 */       this.field_178665_b = p_i45495_2_;
/*     */     }
/*     */     
/*     */     public void func_178661_a(SpectatorMenu menu)
/*     */     {
/* 156 */       menu.field_178658_j = this.field_178666_a;
/*     */     }
/*     */     
/*     */     public IChatComponent getSpectatorName()
/*     */     {
/* 161 */       return this.field_178666_a < 0 ? new ChatComponentText("Previous Page") : new ChatComponentText("Next Page");
/*     */     }
/*     */     
/*     */     public void func_178663_a(float p_178663_1_, int alpha)
/*     */     {
/* 166 */       Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
/*     */       
/* 168 */       if (this.field_178666_a < 0)
/*     */       {
/* 170 */         Gui.drawModalRectWithCustomSizedTexture(0, 0, 144.0F, 0.0F, 16, 16, 256.0F, 256.0F);
/*     */       }
/*     */       else
/*     */       {
/* 174 */         Gui.drawModalRectWithCustomSizedTexture(0, 0, 160.0F, 0.0F, 16, 16, 256.0F, 256.0F);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean func_178662_A_()
/*     */     {
/* 180 */       return this.field_178665_b;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\spectator\SpectatorMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */