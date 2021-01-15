package me.onlyeli.ice.events;

import java.awt.Color;
import java.util.ArrayList;

import me.onlyeli.ice.values.Value;

public class ModData {

	private String name;
	private Color color;
	private boolean state = false;

	private ArrayList<Value> values = new ArrayList<Value>();

	public ModData(String name, Color color){
		this.name = name;
		this.color = color;
	}

	/**
	 * @return the values
	 */
	public ArrayList<Value> getValues(){
		return values;
	}
	
	public void setValues(ArrayList<Value> values){
		for(Value value : values){
			for(Value value1 : this.values){
				if(value.getName().equalsIgnoreCase(value1.getName())){
					value1.setValue(value.getValue());
				}
			}
		}
	}
	
	/**
	 * @return the color
	 */
	public Color getColor(){
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color){
		this.color = color;
	}

	/**
	 * @return the state
	 */
	public boolean getState(){
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(boolean state){
		this.state = state;
	}

	/**
	 * @return the name
	 */
	public String getName(){
		return name;
	}

}
