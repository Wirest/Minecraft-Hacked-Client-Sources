package rip.autumn.module.impl.visuals.hud.impl.tabgui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.cursor.TabCursor;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.render.RenderUtils;

public class TabHandler {
   protected final Minecraft mc;
   private final Map globalKeySubscriberRegistry;
   private final Map specificKeySubscriberRegistry;
   private final Queue unsubscriptionQueue;
   private final int renderPosX;
   private final int renderPosY;
   private final int defaultTabWidth;
   private final int defaultTabHeight;
   private final int horizontalTabSpacing;
   private final int verticalTabSpacing;
   private final Stopwatch scissorBoxStopwatch;
   private final TabCursor currentCursor;
   private boolean defaultKeyListening;
   private boolean wasListeningCancelled;
   private int scissorBoxWidth;
   private int totalHeight;
   private int treeDepth;
   private int previousTreeDepth;
   private TabHandler.ScissorBoxState scissorBoxState;
   private TabBlock currentBlock;

   private TabHandler(int renderPosX, int renderPosY, int defaultTabWidth, int defaultTabHeight, int horizontalTabSpacing, int verticalTabSpacing, TabCursor currentCursor) {
      this.mc = Minecraft.getMinecraft();
      this.globalKeySubscriberRegistry = Maps.newHashMap();
      this.specificKeySubscriberRegistry = Maps.newHashMap();
      this.unsubscriptionQueue = Lists.newLinkedList();
      this.scissorBoxStopwatch = new Stopwatch();
      this.defaultKeyListening = true;
      this.scissorBoxState = TabHandler.ScissorBoxState.EXPANDING;
      this.renderPosX = renderPosX;
      this.renderPosY = renderPosY;
      this.defaultTabWidth = defaultTabWidth;
      this.defaultTabHeight = defaultTabHeight;
      this.horizontalTabSpacing = horizontalTabSpacing;
      this.verticalTabSpacing = verticalTabSpacing;
      this.currentCursor = currentCursor;
   }

   public static TabHandler.Builder builder() {
      return new TabHandler.Builder();
   }

   public void ascendTabTree() {
      this.currentBlock.restartIteration();
      this.currentBlock = ((Tab)this.currentBlock.getCurrent().findParent().get()).getContainerTabBlock();
      this.scissorBoxState = TabHandler.ScissorBoxState.CONTRACTING;
      this.previousTreeDepth = this.treeDepth--;
   }

   public void descendTabTree() {
      this.currentBlock = (TabBlock)this.currentBlock.getCurrent().findChildren().get();
      this.scissorBoxState = TabHandler.ScissorBoxState.EXPANDING;
      if (this.scissorBoxWidth > 0) {
         this.scissorBoxWidth = 0;
      }

      this.previousTreeDepth = this.treeDepth++;
   }

   public void shiftUpTab() {
      this.currentBlock.cycleToPrevious();
   }

   public void shiftDownTab() {
      this.currentBlock.cycleToNext();
   }

   public boolean canAscendTree() {
      return this.currentBlock.getCurrent().findParent().isPresent() && this.scissorBoxWidth >= this.defaultTabWidth;
   }

   public boolean canDescendTree() {
      return this.currentBlock.getCurrent().findChildren().isPresent() && ((TabBlock)this.currentBlock.getCurrent().findChildren().get()).sizeOf() > 0;
   }

   public Tab getCurrentTab() {
      return this.currentBlock.getCurrent();
   }

   public void setCurrentTabs(Collection tabs) {
      Optional found = Optional.empty();
      Iterator var3 = tabs.iterator();
      if (var3.hasNext()) {
         Tab tab1 = (Tab)var3.next();
         found = Optional.of(tab1);
      }

      this.currentBlock = ((Tab)found.get()).getContainerTabBlock();
      TabBlock var10001 = this.currentBlock;
      tabs.forEach(var10001::appendTab);
      this.assignTabPositions(this.currentBlock, this.renderPosX, this.renderPosY);
      this.currentBlock.forEach((tab) -> {
         this.totalHeight += tab.getHeight();
      });
   }

   private void assignTabPositions(TabBlock block, int startPosX, int startPosY) {
      if (block.sizeOf() != 0) {
         Iterator it = block.linkedIterator();

         while(it.hasNext()) {
            TabBlock.DoublyLinkedTab tab = (TabBlock.DoublyLinkedTab)it.next();
            Optional parent = tab.getTab().findParent();
            int width = Math.max(this.defaultTabWidth, this.getWidestOf(block));
            if (tab.getTab() == block.getFirst()) {
               tab.getTab().setDimensionAndPos((Integer)parent.map((value) -> {
                  return value.getPosX() + value.getWidth() + this.horizontalTabSpacing;
               }).orElse(startPosX), (Integer)parent.map(Tab::getPosY).orElse(startPosY), width, this.defaultTabHeight);
            } else {
               tab.getTab().setDimensionAndPos((Integer)parent.map((value) -> {
                  return value.getPosX() + value.getWidth() + this.horizontalTabSpacing;
               }).orElse(startPosX), tab.previous.getTab().getPosY() + tab.previous.getTab().getHeight() + this.verticalTabSpacing, width, this.defaultTabHeight);
            }

            if (tab.getTab().findChildren().isPresent()) {
               this.assignTabPositions((TabBlock)tab.getTab().findChildren().get(), this.renderPosX, this.renderPosY);
            }
         }

      }
   }

   public int getWidestOf(TabBlock block) {
      int widestTabWidth = 0;
      Iterator it = block.linkedIterator();

      while(it.hasNext()) {
         TabBlock.DoublyLinkedTab tab = (TabBlock.DoublyLinkedTab)it.next();
         int tabWidth = tab.getTab().getTabWidth();
         if (tabWidth > widestTabWidth) {
            widestTabWidth = tabWidth;
         }
      }

      return widestTabWidth;
   }

   private void doForCurrentTreeBranch(Consumer action, boolean oneAhead) {
      try {
         TabBlock workingBlock = this.currentBlock;
         if (oneAhead && workingBlock.getCurrent().findChildren().isPresent()) {
            workingBlock = (TabBlock)workingBlock.getCurrent().findChildren().get();
         }

         while(true) {
            this.doForAllInBlock(workingBlock, action);
            if (!workingBlock.getFirst().findParent().isPresent()) {
               break;
            }

            workingBlock = ((Tab)workingBlock.getFirst().findParent().get()).getContainerTabBlock();
         }
      } catch (RuntimeException var4) {
      }

   }

   private void doForAllInBlock(TabBlock block, Consumer action) {
      Iterator var3 = block.iterator();

      while(var3.hasNext()) {
         Tab tab = (Tab)var3.next();
         action.accept(tab);
      }

   }

   public void invokeCurrent() {
      this.currentBlock.getCurrent().doInvocation();
   }

   public void doKeyInput(int key) {
      LinkedList specificActions;
      if ((specificActions = (LinkedList)this.specificKeySubscriberRegistry.get(key)) != null) {
         int i = 0;

         for(int specificActionsSize = specificActions.size(); i < specificActionsSize; ++i) {
            TabHandler.TabAction action = (TabHandler.TabAction)specificActions.get(i);
            action.invoke(key);
         }
      }

      Collection tabActionsRef = this.globalKeySubscriberRegistry.values();
      Iterator var9 = tabActionsRef.iterator();

      while(var9.hasNext()) {
         LinkedList tabActions = (LinkedList)var9.next();
         int i = 0;

         for(int tabActionsSize = tabActions.size(); i < tabActionsSize; ++i) {
            ((TabHandler.TabAction)tabActions.get(i)).invoke(key);
         }
      }

      var9 = this.unsubscriptionQueue.iterator();

      while(var9.hasNext()) {
         Tab tab = (Tab)var9.next();
         this.globalKeySubscriberRegistry.remove(tab);
      }

      this.unsubscriptionQueue.clear();
      if (this.defaultKeyListening && !this.wasListeningCancelled) {
         if (key == 200) {
            this.shiftUpTab();
         } else if (key == 208) {
            this.shiftDownTab();
         } else if (key == 203) {
            if (this.canAscendTree()) {
               this.ascendTabTree();
            }
         } else if (key == 205) {
            if (this.canDescendTree()) {
               this.descendTabTree();
            }
         } else if (key == 28) {
            this.invokeCurrent();
         }
      }

      this.wasListeningCancelled = false;
   }

   private void handleScissorBoxing() {
      if (this.scissorBoxStopwatch.elapsed(15L)) {
         float translationRateMultiplier = 1.0F + Math.max(0.0F, (50.0F - (float)Minecraft.getDebugFPS()) / 10.0F);
         if (this.scissorBoxState == TabHandler.ScissorBoxState.EXPANDING) {
            int currentBlockWidth = Math.max(this.defaultTabWidth, this.getWidestOf(this.currentBlock));
            this.scissorBoxWidth = (int)Math.min((float)this.scissorBoxWidth + 8.0F * translationRateMultiplier, (float)currentBlockWidth);
         } else if (this.scissorBoxState == TabHandler.ScissorBoxState.CONTRACTING) {
            this.scissorBoxWidth = (int)Math.max((float)this.scissorBoxWidth - 10.0F * translationRateMultiplier, 0.0F);
         }

         this.scissorBoxStopwatch.reset();
      }

   }

   public void doTabRendering() {
      int currentBlockWidth = Math.max(this.defaultTabWidth, this.getWidestOf(this.currentBlock));
      if (this.scissorBoxState != TabHandler.ScissorBoxState.STATIC) {
         GL11.glEnable(3089);
         if (this.scissorBoxState == TabHandler.ScissorBoxState.EXPANDING) {
            RenderUtils.prepareScissorBox((float)(this.renderPosX - 1), (float)this.renderPosY, (float)(this.currentBlock.getLast().getPosX() + this.scissorBoxWidth + 1), (float)(this.mc.displayHeight - this.renderPosY));
         } else {
            RenderUtils.prepareScissorBox((float)(this.renderPosX - 1), (float)this.renderPosY, (float)(this.currentBlock.getCurrent().getPosX() + currentBlockWidth + this.scissorBoxWidth + 1), (float)(this.mc.displayHeight - this.renderPosY));
         }

         this.handleScissorBoxing();
      }

      this.doForCurrentTreeBranch(Tab::renderTabBack, this.scissorBoxState == TabHandler.ScissorBoxState.CONTRACTING);
      this.currentCursor.renderOn(this.currentBlock.getCurrent());
      Tab workingTab = this.currentBlock.getCurrent();

      while(workingTab.findParent().isPresent()) {
         workingTab = (Tab)workingTab.findParent().get();
         this.currentCursor.renderStaticOn(workingTab);
      }

      this.doForCurrentTreeBranch(Tab::renderTabFront, this.scissorBoxState == TabHandler.ScissorBoxState.CONTRACTING);
      if (this.scissorBoxState != TabHandler.ScissorBoxState.STATIC) {
         if (this.scissorBoxState == TabHandler.ScissorBoxState.EXPANDING) {
            if (this.previousTreeDepth < this.treeDepth && this.scissorBoxWidth == currentBlockWidth) {
               this.scissorBoxState = TabHandler.ScissorBoxState.STATIC;
            }
         } else if (this.scissorBoxState == TabHandler.ScissorBoxState.CONTRACTING) {
            if (this.previousTreeDepth < this.treeDepth && this.scissorBoxWidth == 0) {
               this.scissorBoxState = TabHandler.ScissorBoxState.STATIC;
            } else if (this.previousTreeDepth > this.treeDepth && this.scissorBoxWidth == 0) {
               this.scissorBoxWidth = currentBlockWidth;
               this.scissorBoxState = TabHandler.ScissorBoxState.STATIC;
            }
         }

         GL11.glDisable(3089);
      }

   }

   public boolean isInDirectTree(Tab tab) {
      Tab workingTab = this.currentBlock.getCurrent();
      boolean isInTree = false;

      while(true) {
         if (workingTab == tab) {
            isInTree = true;
         }

         if (!workingTab.findParent().isPresent()) {
            return isInTree;
         }

         workingTab = (Tab)workingTab.findParent().get();
      }
   }

   public void enableDefaultKeyListening() {
      if (!this.defaultKeyListening) {
         this.wasListeningCancelled = true;
      }

      this.defaultKeyListening = true;
   }

   public void disableDefaultKeyListening() {
      this.defaultKeyListening = false;
   }

   public void subscribeActionToKey(Tab tab, int key, BiConsumer action) {
      ((LinkedList)this.specificKeySubscriberRegistry.computeIfAbsent(key, (list) -> {
         return Lists.newLinkedList();
      })).add(new TabHandler.TabAction(tab, action));
   }

   public void unsubscribeSpecificActions(Tab tab) {
      Iterator var2 = this.specificKeySubscriberRegistry.values().iterator();

      while(var2.hasNext()) {
         LinkedList list = (LinkedList)var2.next();
         int i = 0;

         for(int listSize = list.size(); i < listSize; ++i) {
            TabHandler.TabAction action = (TabHandler.TabAction)list.get(i);
            if (action.getTab() == tab) {
               list.remove(action);
            }
         }
      }

   }

   private static class TabAction {
      private final Tab tab;
      private final BiConsumer action;

      TabAction(Tab tab, BiConsumer action) {
         this.tab = tab;
         this.action = action;
      }

      void invoke(int key) {
         this.action.accept(key, this.tab);
      }

      public Tab getTab() {
         return this.tab;
      }
   }

   public static class Builder {
      private int renderPosX;
      private int renderPosY;
      private int defaultTabWidth;
      private int defaultTabHeight;
      private int horizontalTabSpacing;
      private int verticalTabSpacing;
      private TabCursor currentCursor;

      protected Builder() {
      }

      public TabHandler.Builder renderCoordinates(int renderPosX, int renderPosY) {
         this.renderPosX = renderPosX;
         this.renderPosY = renderPosY;
         return this;
      }

      public TabHandler.Builder tabDimensions(int defaultTabWidth, int defaultTabHeight) {
         this.defaultTabWidth = defaultTabWidth;
         this.defaultTabHeight = defaultTabHeight;
         return this;
      }

      public TabHandler.Builder tabSpacing(int horizontalTabSpacing, int verticalTabSpacing) {
         this.horizontalTabSpacing = horizontalTabSpacing;
         this.verticalTabSpacing = verticalTabSpacing;
         return this;
      }

      public TabHandler.Builder tabCursor(TabCursor cursor) {
         this.currentCursor = cursor;
         return this;
      }

      public TabHandler build() {
         return new TabHandler(this.renderPosX, this.renderPosY, this.defaultTabWidth, this.defaultTabHeight, this.horizontalTabSpacing, this.verticalTabSpacing, this.currentCursor);
      }
   }

   private static enum ScissorBoxState {
      EXPANDING,
      CONTRACTING,
      STATIC;
   }
}
