package rip.autumn.module.impl.visuals;

import java.awt.Color;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.render.RenderGuiEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.combat.AuraMod;
import rip.autumn.utils.ColorUtils;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.render.AnimationUtils;
import rip.autumn.utils.render.RenderUtils;

@Label("TargetHUD")
@Category(ModuleCategory.VISUALS)
public final class TargetHUDMod extends Module {
   private static final Color COLOR = new Color(0, 0, 0, 180);
   private final Stopwatch animationStopwatch = new Stopwatch();
   private EntityOtherPlayerMP target;
   private double healthBarWidth;
   private double hudHeight;

   @Listener(RenderGuiEvent.class)
   public final void onRenderGui(RenderGuiEvent event) {
      AuraMod aura = (AuraMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(AuraMod.class);
      float scaledWidth = (float)event.getScaledResolution().getScaledWidth();
      float scaledHeight = (float)event.getScaledResolution().getScaledHeight();
      if (aura.getTarget() != null && aura.isEnabled()) {
         if (aura.getTarget() instanceof EntityOtherPlayerMP) {
            this.target = (EntityOtherPlayerMP)aura.getTarget();
            float width = 140.0F;
            float height = 40.0F;
            float xOffset = 40.0F;
            float x = scaledWidth / 2.0F - 70.0F;
            float y = scaledHeight / 2.0F + 80.0F;
            float health = this.target.getHealth();
            double hpPercentage = (double)(health / this.target.getMaxHealth());
            hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0D, 1.0D);
            double hpWidth = 92.0D * hpPercentage;
            int healthColor = ColorUtils.getHealthColor(this.target.getHealth(), this.target.getMaxHealth()).getRGB();
            String healthStr = String.valueOf((float)((int)this.target.getHealth()) / 2.0F);
            if (this.animationStopwatch.elapsed(15L)) {
               this.healthBarWidth = AnimationUtils.animate(hpWidth, this.healthBarWidth, 0.3529999852180481D);
               this.hudHeight = AnimationUtils.animate(40.0D, this.hudHeight, 0.10000000149011612D);
               this.animationStopwatch.reset();
            }

            GL11.glEnable(3089);
            RenderUtils.prepareScissorBox(x, y, x + 140.0F, (float)((double)y + this.hudHeight));
            Gui.drawRect((double)x, (double)y, (double)(x + 140.0F), (double)(y + 40.0F), COLOR.getRGB());
            Gui.drawRect((double)(x + 40.0F), (double)(y + 15.0F), (double)(x + 40.0F) + this.healthBarWidth, (double)(y + 25.0F), healthColor);
            mc.fontRendererObj.drawStringWithShadow(healthStr, x + 40.0F + 46.0F - (float)mc.fontRendererObj.getStringWidth(healthStr) / 2.0F, y + 16.0F, -1);
            mc.fontRendererObj.drawStringWithShadow(this.target.getName(), x + 40.0F, y + 2.0F, -1);
            GuiInventory.drawEntityOnScreen((int)(x + 13.333333F), (int)(y + 40.0F), 20, this.target.rotationYaw, this.target.rotationPitch, this.target);
            GL11.glDisable(3089);
         }
      } else {
         this.healthBarWidth = 92.0D;
         this.hudHeight = 0.0D;
         this.target = null;
      }

   }
}
