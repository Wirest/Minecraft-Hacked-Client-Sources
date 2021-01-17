package me.slowly.client.util;

public class StringConversions {
	
	public static Object castNumber(String newValueText, Object currentValue)
	  {
	    if (newValueText.contains("."))
	    {
	      if (newValueText.toLowerCase().contains("f")) {
	        return Float.valueOf(Float.parseFloat(newValueText));
	      }
	      return Double.valueOf(Double.parseDouble(newValueText));
	    }
	    if (isNumeric(newValueText)) {
	      return Integer.valueOf(Integer.parseInt(newValueText));
	    }
	    return newValueText;
	  }
	  
	  public static boolean isNumeric(String text)
	  {
	    try
	    {
	      Integer.parseInt(text);
	      return true;
	    }
	    catch (NumberFormatException nfe) {}
	    return false;
	  }

}
