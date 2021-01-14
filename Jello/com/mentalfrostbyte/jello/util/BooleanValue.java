 package com.mentalfrostbyte.jello.util;

 
 public class BooleanValue extends Value
 {
   private String name;
   private boolean value;
   
   public BooleanValue(String name, boolean value)
   {
/* 12 */     super(name);
/* 13 */     this.name = name;
/* 14 */     this.value = value;
   }
   public boolean getValue(){
	   return value;
   }
   public String getName() {
/* 18 */     return this.name;
   }
   
   public boolean isEnabled() {
/* 22 */     return this.value;
   }
   
   public void toggle() {
/* 26 */     this.value = (!this.value);
   }
   
   public void set(boolean newb) {
/* 30 */     this.value = newb;
   }
 }
