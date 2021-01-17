package com.jagrosh.discordipc.entities;

import java.util.function.Consumer;

public class Callback {
   private final Runnable success;
   private final Consumer failure;

   public Callback() {
      this((Runnable)null, (Consumer)null);
   }

   public Callback(Runnable success) {
      this(success, (Consumer)null);
   }

   public Callback(Consumer failure) {
      this((Runnable)null, failure);
   }

   public Callback(Runnable success, Consumer failure) {
      this.success = success;
      this.failure = failure;
   }

   public boolean isEmpty() {
      return this.success == null && this.failure == null;
   }

   public void succeed() {
      if (this.success != null) {
         this.success.run();
      }

   }

   public void fail(String message) {
      if (this.failure != null) {
         this.failure.accept(message);
      }

   }
}
