 package com.mentalfrostbyte.jello.util;

 
 public class SwitchUtils extends Value
 {
   private String name1;
   private String name2;
   private String name;
   private String tag;
   private boolean value;
   
   public SwitchUtils(String name1, String name2, boolean value, String tag)
   {
     super(name1);
     this.name1 = name1;
			 this.name2 = name2;
			 this.name = name1;
     this.value = value;
			 this.tag = tag;
   }
   public void setName(String name) {
	     this.name = name;
	   }
   public String getName() {
     return this.name;
   }
   public String getTag() {
	   		return this.tag;
	      }
   
   public boolean isEnabled() {
     return this.value;
   }
   
   public void toggle() {
     this.value = (!this.value);
			if(name.equals(name1)){
				name = name2;
			}else{
				name = name1;
			}
   }
   
   public void set(boolean newb) {
     this.value = newb;
   }
 }
