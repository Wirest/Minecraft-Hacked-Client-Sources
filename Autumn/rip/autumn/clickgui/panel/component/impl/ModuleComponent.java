package rip.autumn.clickgui.panel.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import rip.autumn.clickgui.ClickGuiScreen;
import rip.autumn.clickgui.panel.AnimationState;
import rip.autumn.clickgui.panel.Panel;
import rip.autumn.clickgui.panel.component.Component;
import rip.autumn.module.Module;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.ColorUtils;
import rip.autumn.utils.render.AnimationUtils;
import rip.autumn.utils.render.RenderUtils;

public final class ModuleComponent extends Component {
   private static final Color BACKGROUND_COLOR = new Color(23, 23, 23);
   public final List components = new ArrayList();
   private final Module module;
   private final List children = new ArrayList();
   private int opacity = 120;
   private int childrenHeight;
   private double scissorBoxHeight;
   private AnimationState state;

   public ModuleComponent(Module module, Panel parent, int x, int y, int width, int height) {
      super(parent, x, y, width, height);
      this.state = AnimationState.STATIC;
      this.module = module;
      int y2 = height;
      List options = module.getOptions();
      int i = 0;

      for(int optionsSize = options.size(); i < optionsSize; ++i) {
         Option option = (Option)options.get(i);
         if (option instanceof EnumOption) {
            this.children.add(new EnumOptionComponent((EnumOption)option, this.getPanel(), x, y + y2, width, height));
         } else if (option instanceof BoolOption) {
            this.children.add(new BoolOptionComponent((BoolOption)option, this.getPanel(), x, y + y2, width, height));
         } else if (option instanceof DoubleOption) {
            this.children.add(new NumberOptionComponent((DoubleOption)option, this.getPanel(), x, y + y2, width, 16));
         }

         y2 += height;
      }

      this.calculateChildrenHeight();
   }

   public double getOffset() {
      return this.scissorBoxHeight;
   }

   private void drawChildren(int mouseX, int mouseY) {
      int childY = 15;
      List children = this.children;
      int i = 0;

      for(int componentListSize = children.size(); i < componentListSize; ++i) {
         Component child = (Component)children.get(i);
         if (!child.isHidden()) {
            child.setY(this.getY() + childY);
            child.onDraw(mouseX, mouseY);
            childY += 15;
         }
      }

   }

   private int calculateChildrenHeight() {
      int height = 0;
      List children = this.children;
      int i = 0;

      for(int childrenSize = children.size(); i < childrenSize; ++i) {
         Component component = (Component)children.get(i);
         if (!component.isHidden()) {
            height = (int)((double)height + (double)component.getHeight() + component.getOffset());
         }
      }

      return height;
   }

   public void onDraw(int mouseX, int mouseY) {
      Panel parent = this.getPanel();
      int x = parent.getX() + this.getX();
      int y = parent.getY() + this.getY();
      int height = this.getHeight();
      int width = this.getWidth();
      boolean hovered = this.isMouseOver(mouseX, mouseY);
      this.handleScissorBox();
      this.childrenHeight = this.calculateChildrenHeight();
      if (hovered) {
         if (this.opacity < 200) {
            this.opacity += 5;
         }
      } else if (this.opacity > 120) {
         this.opacity -= 5;
      }

      int opacity = this.opacity;
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height) + this.getOffset(), ColorUtils.getColorWithOpacity(BACKGROUND_COLOR, 255 - opacity).getRGB());
      int color = this.module.isEnabled() ? ClickGuiScreen.getColor().getRGB() : (new Color(opacity, opacity, opacity)).getRGB();
      FONT_RENDERER.drawStringWithShadow(this.module.getLabel(), (float)x + 2.0F, (float)y + (float)height / 2.0F - 4.0F, color);
      if (this.scissorBoxHeight > 0.0D) {
         if (parent.state != AnimationState.RETRACTING) {
            RenderUtils.prepareScissorBox((float)x, (float)y, (float)(x + width), (float)((double)y + Math.min(this.scissorBoxHeight, parent.scissorBoxHeight) + (double)height));
         }

         this.drawChildren(mouseX, mouseY);
      }

   }

   public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
      if (this.scissorBoxHeight > 0.0D) {
         List componentList = this.children;
         int i = 0;

         for(int componentListSize = componentList.size(); i < componentListSize; ++i) {
            ((Component)componentList.get(i)).onMouseClick(mouseX, mouseY, mouseButton);
         }
      }

      if (this.isMouseOver(mouseX, mouseY)) {
         if (mouseButton == 0) {
            this.module.toggle();
         } else if (mouseButton == 1 && !this.children.isEmpty()) {
            if (this.scissorBoxHeight > 0.0D && (this.state == AnimationState.EXPANDING || this.state == AnimationState.STATIC)) {
               this.state = AnimationState.RETRACTING;
            } else if (this.scissorBoxHeight < (double)this.childrenHeight && (this.state == AnimationState.EXPANDING || this.state == AnimationState.STATIC)) {
               this.state = AnimationState.EXPANDING;
            }
         }
      }

   }

   public void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
      if (this.scissorBoxHeight > 0.0D) {
         List componentList = this.children;
         int i = 0;

         for(int componentListSize = componentList.size(); i < componentListSize; ++i) {
            ((Component)componentList.get(i)).onMouseRelease(mouseX, mouseY, mouseButton);
         }
      }

   }

   public void onKeyPress(int typedChar, int keyCode) {
      if (this.scissorBoxHeight > 0.0D) {
         List componentList = this.children;
         int i = 0;

         for(int componentListSize = componentList.size(); i < componentListSize; ++i) {
            ((Component)componentList.get(i)).onKeyPress(typedChar, keyCode);
         }
      }

   }

   private void handleScissorBox() {
      int childrenHeight = this.childrenHeight;
      switch(this.state) {
      case EXPANDING:
         if (this.scissorBoxHeight < (double)childrenHeight) {
            this.scissorBoxHeight = AnimationUtils.animate((double)childrenHeight, this.scissorBoxHeight, 0.05D);
         } else if (this.scissorBoxHeight >= (double)childrenHeight) {
            this.state = AnimationState.STATIC;
         }

         this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, (double)childrenHeight);
         break;
      case RETRACTING:
         if (this.scissorBoxHeight > 0.0D) {
            this.scissorBoxHeight = AnimationUtils.animate(0.0D, this.scissorBoxHeight, 0.05D);
         } else if (this.scissorBoxHeight <= 0.0D) {
            this.state = AnimationState.STATIC;
         }

         this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, (double)childrenHeight);
         break;
      case STATIC:
         if (this.scissorBoxHeight > 0.0D && this.scissorBoxHeight != (double)childrenHeight) {
            this.scissorBoxHeight = AnimationUtils.animate((double)childrenHeight, this.scissorBoxHeight, 0.05D);
         }

         this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, (double)childrenHeight);
      }

   }

   private double clamp(double a, double max) {
      if (a < 0.0D) {
         return 0.0D;
      } else {
         return a > max ? max : a;
      }
   }
}
