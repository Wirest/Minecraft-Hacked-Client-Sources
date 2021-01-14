 package com.mentalfrostbyte.jello.util;

 
 public class SwitchTen extends Value
 {
   private String name1;
   private String name2;
   private String name3;
   private String name4;
   private String name5;
   private String name6;
   private String name7;
   private String name8;
   private String name9;
   private String name10;
   private String name;
   private String tag;
   private boolean value;
   
   public SwitchTen(String name1, String name2, String name3, String name4, String name5, String name6, String name7, String name8, String name9, String name10, boolean value, String tag)
   {
     super(name1);
     		 this.name1 = name1;
			 this.name2 = name2;
			 this.name3 = name3;
			 this.name4 = name4;
			 this.name5 = name5;
			 this.name6 = name6;
			 this.name7 = name7;
			 this.name8 = name8;
			 this.name9 = name9;
			 this.name10 = name10;
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
			if(name.equalsIgnoreCase(name1)){
				name = name2;
			}else if(name.equalsIgnoreCase(name2)){
				name = name3;
			}else if(name.equalsIgnoreCase(name3)){
				name = name4;
			}else if(name.equalsIgnoreCase(name4)){
				name = name5;
			}else if(name.equalsIgnoreCase(name5)){
				name = name6;
			}else if(name.equalsIgnoreCase(name6)){
				name = name7;
			}else if(name.equalsIgnoreCase(name7)){
				name = name8;
			}else if(name.equalsIgnoreCase(name8)){
				name = name9;
			}else if(name.equalsIgnoreCase(name9)){
				name = name10;
			}else if(name.equalsIgnoreCase(name10)){
				name = name1;
			}
   }
   
   public void set(boolean newb) {
     this.value = newb;
   }
 }
