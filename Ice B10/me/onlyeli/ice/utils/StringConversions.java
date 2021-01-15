package me.onlyeli.ice.utils;

public class StringConversions {
	public static Object castNumber(String newValueText, Object currentValue) {
		if (newValueText.contains(".")) {
			if (newValueText.toLowerCase().contains("f")) {
				return Float.parseFloat(newValueText);
			} else {
				return Double.parseDouble(newValueText);
			}
		} else {
			if (isNumeric(newValueText)){
				return Integer.parseInt(newValueText);
			}
		}
		return newValueText;
	}

	public static boolean isNumeric(String text) {
		try{
			Integer.parseInt(text);
			return true;
		}catch (NumberFormatException nfe){
			return false;
		}
	}
}
