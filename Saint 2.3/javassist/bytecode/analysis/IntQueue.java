package javassist.bytecode.analysis;

import java.util.NoSuchElementException;

class IntQueue {
   private IntQueue.Entry head;
   private IntQueue.Entry tail;

   void add(int value) {
      IntQueue.Entry entry = new IntQueue.Entry(value);
      if (this.tail != null) {
         this.tail.next = entry;
      }

      this.tail = entry;
      if (this.head == null) {
         this.head = entry;
      }

   }

   boolean isEmpty() {
      return this.head == null;
   }

   int take() {
      if (this.head == null) {
         throw new NoSuchElementException();
      } else {
         int value = this.head.value;
         this.head = this.head.next;
         if (this.head == null) {
            this.tail = null;
         }

         return value;
      }
   }

   private static class Entry {
      private IntQueue.Entry next;
      private int value;

      private Entry(int value) {
         this.value = value;
      }

      // $FF: synthetic method
      Entry(int x0, Object x1) {
         this(x0);
      }
   }
}
