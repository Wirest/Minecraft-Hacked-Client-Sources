package rip.autumn.module.impl.visuals.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class Component {
   protected static final Minecraft mc = Minecraft.getMinecraft();
   protected final HUDMod parent;

   public Component(HUDMod parent) {
      this.parent = parent;
   }

   public HUDMod getParent() {
      return this.parent;
   }

   public abstract void draw(ScaledResolution var1);
}
