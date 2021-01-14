package rip.autumn.module.impl.visuals.hud.impl.tabgui.cursor.cursors;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import rip.autumn.core.Autumn;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.cursor.TabCursor;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.render.Palette;

public class QueuedTabCursor implements TabCursor {
   private final Deque renderQueue = Lists.newLinkedList();
   private float translationRateMultiplier = 1.0F;
   private final float translationRate = 3.5F;
   private static int currentColor;

   public void renderOn(Tab tab) {
      if (!this.renderQueue.isEmpty() && ((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab().getPosX() == tab.getPosX() && tab.getContainerTabBlock().sizeOf() != 1) {
         if (((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab() == ((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab().getContainerTabBlock().getFirst() && tab == ((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab().getContainerTabBlock().getLast() && Keyboard.getEventKey() != 208) {
            this.renderQueue.push(new QueuedTabCursor.RenderAction(((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab(), tab, QueuedTabCursor.State.CYCLEUP));
         } else if (((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab() == ((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab().getContainerTabBlock().getLast() && tab == ((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab().getContainerTabBlock().getFirst() && Keyboard.getEventKey() != 200) {
            this.renderQueue.push(new QueuedTabCursor.RenderAction(((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab(), tab, QueuedTabCursor.State.CYCLEDOWN));
         } else if (tab.getPosY() < ((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab().getPosY()) {
            this.renderQueue.push(new QueuedTabCursor.RenderAction(((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab(), tab, QueuedTabCursor.State.UP));
         } else if (tab.getPosY() > ((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab().getPosY()) {
            this.renderQueue.push(new QueuedTabCursor.RenderAction(((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).getTargetTab(), tab, QueuedTabCursor.State.DOWN));
         }
      } else {
         this.renderQueue.clear();
         this.renderQueue.push(new QueuedTabCursor.RenderAction(tab, tab, QueuedTabCursor.State.NEW));
         ((QueuedTabCursor.RenderAction)this.renderQueue.getLast()).render();
      }

      this.translationRateMultiplier = 1.0F + Math.max(0.0F, (50.0F - (float)Minecraft.getDebugFPS()) / 10.0F);
      if (((QueuedTabCursor.RenderAction)this.renderQueue.getLast()).isComplete() && this.renderQueue.size() > 1) {
         this.renderQueue.removeLast();
      }

      if (((QueuedTabCursor.RenderAction)this.renderQueue.getFirst()).isComplete()) {
         this.renderStaticOn(tab);
      }

      if (!((QueuedTabCursor.RenderAction)this.renderQueue.getLast()).isComplete()) {
         ((QueuedTabCursor.RenderAction)this.renderQueue.getLast()).render();
      }

   }

   public void renderStaticOn(Tab tab) {
      Color c = (Color)((HUDMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(HUDMod.class)).color.getValue();
      currentColor = Palette.fade(c).getRGB();
      Gui.drawRect((double)((float)tab.getPosX() + 0.5F), (double)((float)tab.getPosY() + 0.5F), (double)((float)(tab.getPosX() + tab.getWidth()) - 0.5F), (double)((float)(tab.getPosY() + tab.getHeight()) - 0.5F), currentColor);
   }

   private static enum State {
      NEW,
      UP,
      DOWN,
      CYCLEUP,
      CYCLEDOWN;
   }

   private class RenderAction {
      private final Tab previousTab;
      private final Tab targetTab;
      private final Stopwatch animationStopwatch = new Stopwatch();
      private final QueuedTabCursor.State state;
      private boolean complete;
      private float animationOffset = 0.0F;

      public RenderAction(Tab previousTab, Tab targetTab, QueuedTabCursor.State state) {
         this.previousTab = previousTab;
         this.targetTab = targetTab;
         this.state = state;
      }

      public QueuedTabCursor.State getState() {
         return this.state;
      }

      public boolean isComplete() {
         return this.complete;
      }

      public Tab getTargetTab() {
         return this.targetTab;
      }

      public void render() {
         if (this.state.equals(QueuedTabCursor.State.NEW)) {
            this.complete = true;
         } else if (this.state.equals(QueuedTabCursor.State.CYCLEUP)) {
            if (this.animationStopwatch.elapsed(15L)) {
               this.animationOffset = Math.max((float)(-this.previousTab.getHeight()), this.animationOffset - 3.5F * QueuedTabCursor.this.translationRateMultiplier);
               this.animationStopwatch.reset();
            }

            Gui.drawRect((double)((float)this.previousTab.getPosX() + 0.5F), (double)((float)this.previousTab.getPosY() + 0.5F), (double)((float)(this.previousTab.getPosX() + this.previousTab.getWidth()) - 0.5F), (double)((float)(this.previousTab.getPosY() + this.previousTab.getHeight()) + this.animationOffset - 0.5F), QueuedTabCursor.currentColor);
            Gui.drawRect((double)((float)this.targetTab.getPosX() + 0.5F), (double)((float)(this.targetTab.getPosY() + this.targetTab.getHeight()) + this.animationOffset + 0.5F), (double)((float)(this.targetTab.getPosX() + this.targetTab.getWidth()) - 0.5F), (double)((float)(this.targetTab.getPosY() + this.targetTab.getHeight()) - 0.5F), QueuedTabCursor.currentColor);
            if (-this.animationOffset == (float)this.targetTab.getHeight()) {
               this.complete = true;
            }
         } else if (this.state.equals(QueuedTabCursor.State.CYCLEDOWN)) {
            if (this.animationStopwatch.elapsed(15L)) {
               this.animationOffset = Math.min((float)this.previousTab.getHeight(), this.animationOffset + 3.5F * QueuedTabCursor.this.translationRateMultiplier);
               this.animationStopwatch.reset();
            }

            Gui.drawRect((double)((float)this.previousTab.getPosX() + 0.5F), (double)((float)this.previousTab.getPosY() + this.animationOffset + 0.5F), (double)((float)(this.previousTab.getPosX() + this.previousTab.getWidth()) - 0.5F), (double)((float)(this.previousTab.getPosY() + this.previousTab.getHeight()) - 0.5F), QueuedTabCursor.currentColor);
            Gui.drawRect((double)((float)this.targetTab.getPosX() + 0.5F), (double)((float)this.targetTab.getPosY() + 0.5F), (double)((float)(this.targetTab.getPosX() + this.targetTab.getWidth()) - 0.5F), (double)((float)this.targetTab.getPosY() + this.animationOffset - 0.5F), QueuedTabCursor.currentColor);
            if (this.animationOffset == (float)this.targetTab.getHeight()) {
               this.complete = true;
            }
         } else if (this.state.equals(QueuedTabCursor.State.UP)) {
            if (this.animationStopwatch.elapsed(15L)) {
               this.animationOffset = Math.max((float)(-this.previousTab.getHeight()), this.animationOffset - 3.5F * QueuedTabCursor.this.translationRateMultiplier);
               this.animationStopwatch.reset();
            }

            Gui.drawRect((double)((float)this.previousTab.getPosX() + 0.5F), (double)((float)this.previousTab.getPosY() + this.animationOffset + 0.5F), (double)((float)(this.previousTab.getPosX() + this.previousTab.getWidth()) - 0.5F), (double)((float)(this.previousTab.getPosY() + this.previousTab.getHeight()) + this.animationOffset - 0.5F), QueuedTabCursor.currentColor);
            if ((float)this.previousTab.getPosY() + this.animationOffset == (float)this.targetTab.getPosY()) {
               this.complete = true;
            }
         } else if (this.state.equals(QueuedTabCursor.State.DOWN)) {
            if (this.animationStopwatch.elapsed(15L)) {
               this.animationOffset = Math.min((float)this.previousTab.getHeight(), this.animationOffset + 3.5F * QueuedTabCursor.this.translationRateMultiplier);
               this.animationStopwatch.reset();
            }

            Gui.drawRect((double)((float)this.previousTab.getPosX() + 0.5F), (double)((float)this.previousTab.getPosY() + this.animationOffset + 0.5F), (double)((float)(this.previousTab.getPosX() + this.previousTab.getWidth()) - 0.5F), (double)((float)(this.previousTab.getPosY() + this.previousTab.getHeight()) + this.animationOffset - 0.5F), QueuedTabCursor.currentColor);
            if ((float)this.previousTab.getPosY() + this.animationOffset == (float)this.targetTab.getPosY()) {
               this.complete = true;
            }
         }

      }
   }
}
