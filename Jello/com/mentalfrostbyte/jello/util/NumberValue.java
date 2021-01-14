 package com.mentalfrostbyte.jello.util;
 
 import java.math.BigDecimal;
import java.math.RoundingMode;




 
 public class NumberValue extends Value
 {
   private String name;
   private double value;
   private double max;
   private double min;
   private double increment;

   public float hover = 1;
   public float lastHover = 1;
   
   public NumberValue(String name, double value, double min, double max, double increment)
   {
/* 16 */     super(name);
/* 17 */     this.name = name;
/* 18 */     this.value = value;
/* 19 */     this.max = max;
/* 20 */     this.min = min;
/* 21 */     this.increment = increment;
   }
   
   public String getName() {
/* 25 */     return this.name;
   }
   
   public double getValue() {
/* 29 */     return roundToPlace(this.value, 2);
   }
   
   public double getIncrement() {
/* 33 */     return this.increment;
   }
   
   public double getMin() {
/* 37 */     return this.min;
   }
   
   public double getMax() {
/* 41 */     return this.max;
   }
   
   public void setMax(double max) {
	    this.max = max;
	      }
   
   public void increase() {
/* 45 */     if (roundToPlace(this.value, 2) < this.max) {
/* 46 */       this.value += this.increment;
     }
/* 48 */     
   }
   
   public void decrease() {
/* 53 */     if (roundToPlace(this.value, 2) > this.min) {
/* 54 */       this.value -= this.increment;
     }
   }
   
   public void setVal(double newv) {
/* 59 */     newv = clamp_number(newv, this.min, this.max);
/* 60 */     newv = Math.round(newv * (1.0D / this.increment)) / (1.0D / this.increment);
/* 61 */     this.value = newv;
   }
   
   public static double clamp_number(double value, double min, double max) {
/* 65 */     value = Math.max(min, value);
/* 66 */     value = Math.min(max, value);
/* 67 */     return value;
   }
 
 public static double roundToPlace(double value, int places)
 {
   if (places < 0) {
     throw new IllegalArgumentException();
   }
   BigDecimal bd = new BigDecimal(value);
   bd = bd.setScale(places, RoundingMode.HALF_UP);
   return bd.doubleValue();
 }
 }


