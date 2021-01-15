/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiScreenResourcePacks;
/*    */ 
/*    */ public class ResourcePackListEntryFound extends ResourcePackListEntry
/*    */ {
/*    */   private final ResourcePackRepository.Entry field_148319_c;
/*    */   
/*    */   public ResourcePackListEntryFound(GuiScreenResourcePacks resourcePacksGUIIn, ResourcePackRepository.Entry p_i45053_2_)
/*    */   {
/* 11 */     super(resourcePacksGUIIn);
/* 12 */     this.field_148319_c = p_i45053_2_;
/*    */   }
/*    */   
/*    */   protected void func_148313_c()
/*    */   {
/* 17 */     this.field_148319_c.bindTexturePackIcon(this.mc.getTextureManager());
/*    */   }
/*    */   
/*    */   protected int func_183019_a()
/*    */   {
/* 22 */     return this.field_148319_c.func_183027_f();
/*    */   }
/*    */   
/*    */   protected String func_148311_a()
/*    */   {
/* 27 */     return this.field_148319_c.getTexturePackDescription();
/*    */   }
/*    */   
/*    */   protected String func_148312_b()
/*    */   {
/* 32 */     return this.field_148319_c.getResourcePackName();
/*    */   }
/*    */   
/*    */   public ResourcePackRepository.Entry func_148318_i()
/*    */   {
/* 37 */     return this.field_148319_c;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\ResourcePackListEntryFound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */