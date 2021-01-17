package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod;

import java.awt.Color;
import java.util.ArrayList;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.util.file.KeyBind;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.SkyLine;

public class ModData {

	private String name;
	private KeyBind bind;
	private Color color;
	private boolean state = false;
	private boolean visible = true;

	private ArrayList<Value> values = new ArrayList<Value>();

	public ModData(String name, int bind, Color color){
		this.name = name;
		this.bind = new KeyBind(this.name, bind);
		this.color = color;
	}

	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public boolean isVisible(){
		return this.visible;
	}
	
	public ArrayList<Value> getValues(){
		return values;
	}
	
	public Object getValue(Value parent, Value value){
		if(parent.getGenericClass().equals(Integer.class) && value.getValue().getClass().equals(Double.class)){
			return ((Double) value.getValue()).intValue();
		}else if(parent.getGenericClass().equals(Double.class) && value.getValue().getClass().equals(Double.class)){
			return ((Double) value.getValue()).doubleValue();
		}else if(parent.getGenericClass().equals(Float.class) && value.getValue().getClass().equals(Double.class)){
			return ((Double) value.getValue()).floatValue();
		}else{
			return value.getValue();
		}
	}
	
	public void setValues(ArrayList<Value> values){
		for(Value value : values){
			for(Value value1 : this.values){
				if(value.getName().equalsIgnoreCase(value1.getName())){
					value1.setValue(getValue(value1, value));

					if(value1.getName().equalsIgnoreCase("mode")){
						Module module = SkyLine.getManagers().getModuleManager().getModuleFromName(this.getName());
						
						ModMode mode = null;
						
						for(ModMode m : module.getModes()){
							if(m.getName().equalsIgnoreCase((String) value1.getValue())){
								mode = m;
							}
						}
						
						if(mode != null) module.setMode(mode);
					}
				}
			}
		}
	}
	public KeyBind getKeyBind(){
		return bind;
	}

	public void setKeybind(KeyBind bind){
		this.bind = bind;
	}

	public Color getColor(){
		return color;
	}

	public void setColor(Color color){
		this.color = color;
	}

	public String getName(){
		return name;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
	
}