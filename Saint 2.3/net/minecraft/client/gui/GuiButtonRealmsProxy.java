package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsButton;

public class GuiButtonRealmsProxy extends GuiButton {
   private RealmsButton field_154318_o;
   private static final String __OBFID = "CL_00001848";

   public GuiButtonRealmsProxy(RealmsButton p_i46321_1_, int p_i46321_2_, int p_i46321_3_, int p_i46321_4_, String p_i46321_5_) {
      super(p_i46321_2_, p_i46321_3_, p_i46321_4_, p_i46321_5_);
      this.field_154318_o = p_i46321_1_;
   }

   public GuiButtonRealmsProxy(RealmsButton p_i1090_1_, int p_i1090_2_, int p_i1090_3_, int p_i1090_4_, String p_i1090_5_, int p_i1090_6_, int p_i1090_7_) {
      super(p_i1090_2_, p_i1090_3_, p_i1090_4_, p_i1090_6_, p_i1090_7_, p_i1090_5_);
      this.field_154318_o = p_i1090_1_;
   }

   public int getId() {
      return super.id;
   }

   public boolean getEnabled() {
      return super.enabled;
   }

   public void setEnabled(boolean p_154313_1_) {
      super.enabled = p_154313_1_;
   }

   public void setText(String p_154311_1_) {
      super.displayString = p_154311_1_;
   }

   public int getButtonWidth() {
      return super.getButtonWidth();
   }

   public int getPositionY() {
      return super.yPosition;
   }

   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
      if (super.mousePressed(mc, mouseX, mouseY)) {
         this.field_154318_o.clicked(mouseX, mouseY);
      }

      return super.mousePressed(mc, mouseX, mouseY);
   }

   public void mouseReleased(int mouseX, int mouseY) {
      this.field_154318_o.released(mouseX, mouseY);
   }

   public void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
      this.field_154318_o.renderBg(mouseX, mouseY);
   }

   public RealmsButton getRealmsButton() {
      return this.field_154318_o;
   }

   public int getHoverState(boolean mouseOver) {
      return this.field_154318_o.getYImage(mouseOver);
   }

   public int func_154312_c(boolean p_154312_1_) {
      return super.getHoverState(p_154312_1_);
   }

   public int func_175232_g() {
      return this.height;
   }
}
