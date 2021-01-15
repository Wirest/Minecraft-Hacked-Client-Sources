/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class DisconnectedRealmsScreen extends RealmsScreen
/*    */ {
/*    */   private String title;
/*    */   private IChatComponent reason;
/*    */   private List<String> lines;
/*    */   private final RealmsScreen parent;
/*    */   private int textHeight;
/*    */   
/*    */   public DisconnectedRealmsScreen(RealmsScreen p_i45742_1_, String p_i45742_2_, IChatComponent p_i45742_3_)
/*    */   {
/* 16 */     this.parent = p_i45742_1_;
/* 17 */     this.title = getLocalizedString(p_i45742_2_);
/* 18 */     this.reason = p_i45742_3_;
/*    */   }
/*    */   
/*    */   public void init()
/*    */   {
/* 23 */     Realms.setConnectedToRealms(false);
/* 24 */     buttonsClear();
/* 25 */     this.lines = fontSplit(this.reason.getFormattedText(), width() - 50);
/* 26 */     this.textHeight = (this.lines.size() * fontLineHeight());
/* 27 */     buttonsAdd(newButton(0, width() / 2 - 100, height() / 2 + this.textHeight / 2 + fontLineHeight(), getLocalizedString("gui.back")));
/*    */   }
/*    */   
/*    */   public void keyPressed(char p_keyPressed_1_, int p_keyPressed_2_)
/*    */   {
/* 32 */     if (p_keyPressed_2_ == 1)
/*    */     {
/* 34 */       Realms.setScreen(this.parent);
/*    */     }
/*    */   }
/*    */   
/*    */   public void buttonClicked(RealmsButton p_buttonClicked_1_)
/*    */   {
/* 40 */     if (p_buttonClicked_1_.id() == 0)
/*    */     {
/* 42 */       Realms.setScreen(this.parent);
/*    */     }
/*    */   }
/*    */   
/*    */   public void render(int p_render_1_, int p_render_2_, float p_render_3_)
/*    */   {
/* 48 */     renderBackground();
/* 49 */     drawCenteredString(this.title, width() / 2, height() / 2 - this.textHeight / 2 - fontLineHeight() * 2, 11184810);
/* 50 */     int i = height() / 2 - this.textHeight / 2;
/*    */     
/* 52 */     if (this.lines != null)
/*    */     {
/* 54 */       for (String s : this.lines)
/*    */       {
/* 56 */         drawCenteredString(s, width() / 2, i, 16777215);
/* 57 */         i += fontLineHeight();
/*    */       }
/*    */     }
/*    */     
/* 61 */     super.render(p_render_1_, p_render_2_, p_render_3_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\realms\DisconnectedRealmsScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */