package nivia.managers;

import nivia.Pandora;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class PropertyManager {
	
    public static ArrayList<Property> properties = new ArrayList<Property>();
    
    public static ArrayList<Property> getProperties() {
    	return properties;
    }

    public static Property getPropertybyName(String name){
    	return getProperties().stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }   
    public ArrayList<Property> getPropertiesFromModule(Module module){
    	ArrayList<Property> array = new ArrayList<>();
    	properties.stream().filter(p -> p.getModule() == module).forEach(array::add);
    	
    	return array;   	
    }
    
	public void addProperty(Property p){
		if(!properties.contains(p))
			properties.add(p);
	}
	
	public void removeProperty(Property p){
		if(properties.contains(p))
			properties.remove(p);
	}
	
    public static class Property<Type> {    	
    	
    	private final String name;
    	private Module module;
    	public boolean onClickGui;
    	public Type value, defaultvalue;
    	public Property(Module module,String name, Type value) {
    		this.module = module;
    		this.name = name;
    		this.value = value;
    		this.defaultvalue = value;		
    		onClickGui = true;
    		Pandora.getPropertyManager().properties.add(this);
    	}
    	public Property(Module module,String name, Type value, boolean onGui) {
    		this.module = module;
    		this.name = name;
    		this.value = value;
    		this.defaultvalue = value;		
    		onClickGui = onGui;
    		Pandora.getPropertyManager().properties.add(this);
    	}
    	
    	public Type getDefaultValue() {
    		return defaultvalue;
    	}	
    	public String getName() {
    		return name;
    	}
    	public void reset(){
    		this.value = this.defaultvalue;
            Logger.logSetMessage(this.getModule().getName(), this.getName(), this);
        }
    	public Module getModule() {
    		return this.module;
    		}
        public void setModule(Module mod) { module = mod; }
    	}
    public static class DoubleProperty extends Property {
    	
		public double min, max;
		public double increase;
		public boolean colorSlider;
		
		public DoubleProperty(Module module , String name, double value, double min, double max, double inc){
			super(module, name, value);
			this.min = min;
			this.max = max;
			this.increase = inc;
			this.value = this.defaultvalue = value;
			colorSlider = false;
		}
		public DoubleProperty(Module module , String name, double value, double min, double max){
			super(module, name, value);
			this.min = min;
			this.max = max;
			this.increase = 0;
			this.value = this.defaultvalue = value;
			colorSlider = false;
		}
		public DoubleProperty(Module module, String name, double value, double min, double max, boolean showOnGui) {
			super(module, name, value);
			this.min = min;
			this.max = max;
			this.increase = 0;
			this.value = this.defaultvalue = value;
			this.onClickGui = showOnGui;
			colorSlider = false;
		}
		public DoubleProperty(Module module, String name, double value, double min, double max, boolean showOnGui, boolean ColorSlider) {
			super(module, name, value);
			this.min = min;
			this.max = max;
			this.increase = 0;
			this.value = this.defaultvalue = value;
			this.onClickGui = showOnGui;
			colorSlider = ColorSlider;
		}
		public void setValue(double newValue){
			if(newValue > this.max) newValue = this.max;
			if(newValue < this.min) newValue = this.min;
			this.value = newValue;
		}
		public double getValue(){
			return (double) this.value;
		}
	}

    public static class Manager<T> {

    	private List<T> contents = new ArrayList<T>();
    	
    	public List<T> getContents() {
    		return contents;
    	}
    	
    	public void addContent(T content){
    		
    		this.contents.add(content);
    	}
    	
    	public void removeContent(T content){
    		this.contents.remove(content);
    	}
    	
    	public boolean hasContent(T content){
    		return this.contents.contains(content);
    	}   	   	    	  			  	
    }
    
  }