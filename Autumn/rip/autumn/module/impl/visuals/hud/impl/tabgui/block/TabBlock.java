package rip.autumn.module.impl.visuals.hud.impl.tabgui.block;

import java.util.Iterator;
import java.util.NoSuchElementException;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;

public class TabBlock implements Iterable {
   private int size = 0;
   private TabBlock.DoublyLinkedTab first;
   private TabBlock.DoublyLinkedTab current;

   public Tab getFirst() {
      return this.first.backingTab;
   }

   public Tab getCurrent() {
      return this.current.backingTab;
   }

   public Tab getLast() {
      return this.first.previous.backingTab;
   }

   public boolean hasNext() {
      return true;
   }

   public boolean hasPrevious() {
      return true;
   }

   public Tab getNext() {
      return this.current.next.backingTab;
   }

   public Tab getPrevious() {
      return this.current.previous.backingTab;
   }

   public void cycleToNext() {
      this.current = this.current.next;
   }

   public void cycleToPrevious() {
      this.current = this.current.previous;
   }

   public void restartIteration() {
      this.current = this.first;
   }

   public void appendTab(Tab tab) {
      TabBlock.DoublyLinkedTab wrapped = new TabBlock.DoublyLinkedTab(tab);
      if (this.first == null) {
         this.first = wrapped;
         this.first.next = this.first;
         this.first.previous = this.first;
         this.current = this.first;
      } else {
         wrapped.next = this.first;
         wrapped.previous = this.first.previous;
         this.first.previous.next = wrapped;
         this.first.previous = wrapped;
      }

      ++this.size;
   }

   public int sizeOf() {
      return this.size;
   }

   public Iterator iterator() {
      return new TabBlock.TabBlockIterator();
   }

   public Iterator linkedIterator() {
      return new TabBlock.LinkedTabBlockIterator();
   }

   public static class DoublyLinkedTab {
      private Tab backingTab;
      public TabBlock.DoublyLinkedTab next;
      public TabBlock.DoublyLinkedTab previous;

      public DoublyLinkedTab(Tab tab) {
         this.backingTab = tab;
      }

      public Tab getTab() {
         return this.backingTab;
      }
   }

   private class LinkedTabBlockIterator implements Iterator {
      private TabBlock.DoublyLinkedTab localCurrent;
      private int count;

      private LinkedTabBlockIterator() {
         this.localCurrent = TabBlock.this.first;
         this.count = 0;
      }

      public boolean hasNext() {
         return this.count < TabBlock.this.size;
      }

      public TabBlock.DoublyLinkedTab next() {
         if (this.hasNext()) {
            TabBlock.DoublyLinkedTab tab = this.localCurrent;
            this.localCurrent = this.localCurrent.next;
            ++this.count;
            return tab;
         } else {
            throw new NoSuchElementException();
         }
      }
   }

   private class TabBlockIterator implements Iterator {
      private TabBlock.DoublyLinkedTab localCurrent;
      private int count;

      private TabBlockIterator() {
         this.localCurrent = TabBlock.this.first;
         this.count = 0;
      }

      public boolean hasNext() {
         return this.count < TabBlock.this.size;
      }

      public Tab next() {
         if (this.hasNext()) {
            Tab tab = this.localCurrent.getTab();
            this.localCurrent = this.localCurrent.next;
            ++this.count;
            return tab;
         } else {
            throw new NoSuchElementException();
         }
      }
   }
}
