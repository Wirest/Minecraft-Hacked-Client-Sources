package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RestrictedValue<T extends Number> extends Value<T> {
	
	private T min;
	public T max;
	
	public RestrictedValue(String name, T defaultValue, T min, T max) {
		super(name, defaultValue);
		this.min = min;
		this.max = max;
	}
	
	public T getMin() {
		return min;
	}

	public T getMax() {
		return max;
	}

    public static void increase(RestrictedValue value){
        Class clas = value.getGenericClass();

        if(clas.getName().toLowerCase().contains("double")){
            Double d = (Double) value.getValue();
            d += 0.1;
            if(!(d > (Double) value.getMax())) value.setValue(round(d, 1));
        }else if(clas.getName().toLowerCase().contains("float")){
            Float f = (Float) value.getValue();
            f += 0.1f;
            if(!(f > (Float) value.getMax())) value.setValue(round(f, 1));
        }else if(clas.getName().toLowerCase().contains("int")){
            Integer i = (Integer) value.getValue();
            i += 1;
            if(!(i > (Integer) value.getMax())) value.setValue(i);
        }
    }

    public static void descrease(RestrictedValue value){
        Class clas = value.getGenericClass();

        if(clas.getName().toLowerCase().contains("double")){
            Double d = (Double) value.getValue();
            d -= 0.1;
            if(!(d < (Double) value.getMin())) value.setValue(round(d, 1));
        }else if(clas.getName().toLowerCase().contains("float")){
            Float f = (Float) value.getValue();
            f -= 0.1f;
            if(!(f < (Float) value.getMin())) value.setValue(round(f, 1));
        }else if(clas.getName().toLowerCase().contains("int")){
            Integer i = (Integer) value.getValue();
            i -= 1;
            if(!(i < (Integer) value.getMin())) value.setValue(i);
        }
    }

    public static void fromNumber(Number number, RestrictedValue value){
        Class clas = value.getGenericClass();
        if(clas.getName().toLowerCase().contains("double")){
            Double d = number.doubleValue();
            if(!(d < (Double) value.getMin())) value.setValue(round(d, 1));
        }else if(clas.getName().toLowerCase().contains("float")){
            Float f = number.floatValue();
            f -= 0.1f;
            if(!(f < (Float) value.getMin())) value.setValue(round(f, 1));
        }else if(clas.getName().toLowerCase().contains("int")){
            Integer i = (int) number.doubleValue();
            i -= 1;
            if(!(i < (Integer) value.getMin())) value.setValue(i);
        }
    }

    public static Number fromString(String string, RestrictedValue value){
        try{
            Class clas = value.getGenericClass();

            if(clas.getName().toLowerCase().contains("double")){
                Double d = Double.parseDouble(string);
                return d;
            }else if(clas.getName().toLowerCase().contains("float")){
                Float d = Float.parseFloat(string);
                return d;
            }else if(clas.getName().toLowerCase().contains("int")){
                Integer d = Integer.parseInt(string);
                return d;
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

}
