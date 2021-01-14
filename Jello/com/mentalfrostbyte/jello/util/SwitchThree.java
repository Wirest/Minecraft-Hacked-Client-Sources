 package com.mentalfrostbyte.jello.util;

 
 public class SwitchThree extends Value
 {
   private String name1;
   private String name2;
   private String name3;
   private String name;
   private String tag;
   private boolean value;
   
   public SwitchThree(String name1, String name2, String name3, boolean value, String tag)
   {
     super(name1);
     		 this.name1 = name1;
			 this.name2 = name2;
			 this.name3 = name3;
			 this.name = name1;
			 this.value = value;
			 this.tag = tag;
   }
   
   public String getName() {
     return this.name;
   }
   public void setName(String name){
	   this.name = name;
   }
   public String getTag() {
	   		return this.tag;
	      }
   
   public boolean isEnabled() {
     return this.value;
   }
   
   public void toggle() {
     this.value = (!this.value);
			if(name.equalsIgnoreCase(name1)){
				name = name2;
			}else if(name.equalsIgnoreCase(name2)){
				name = name3;
			}else if(name.equalsIgnoreCase(name3)){
				name = name1;
			}
   }
   
   public void set(boolean newb) {
     this.value = newb;
   }
 }
