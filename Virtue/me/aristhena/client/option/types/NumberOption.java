// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.option.types;

import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import me.aristhena.client.module.Module;
import me.aristhena.client.option.Option;

public class NumberOption extends Option<Number>
{
    private double increment;
    private double min;
    private double max;
    
    public NumberOption(final String id, final String name, final Number value, final Module module) {
        super(id, name, value, module);
    }
    
    public void setValue(final String value) {
        Field[] declaredFields;
        for (int length = (declaredFields = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Op.class) && field.getName().equalsIgnoreCase(this.getId())) {
                try {
                    if (field.getType().isAssignableFrom(Float.TYPE)) {
                        super.setValue(Float.parseFloat(value));
                        field.setFloat(this.getModule(), Float.parseFloat(value));
                    }
                    else if (field.getType().isAssignableFrom(Double.TYPE)) {
                        super.setValue(Double.parseDouble(value));
                        field.setDouble(this.getModule(), Double.parseDouble(value));
                    }
                    else if (field.getType().isAssignableFrom(Long.TYPE)) {
                        super.setValue(Math.round(Double.parseDouble(value)));
                        field.setLong(this.getModule(), Math.round(Double.parseDouble(value)));
                    }
                    else if (field.getType().isAssignableFrom(Integer.TYPE)) {
                        super.setValue((int)Math.round(Double.parseDouble(value)));
                        field.setInt(this.getModule(), (int)Math.round(Double.parseDouble(value)));
                    }
                    else if (field.getType().isAssignableFrom(Short.TYPE)) {
                        super.setValue((short)Math.round(Double.parseDouble(value)));
                        field.setShort(this.getModule(), (short)Math.round(Double.parseDouble(value)));
                    }
                    else if (field.getType().isAssignableFrom(Byte.TYPE)) {
                        super.setValue((byte)Math.round(Double.parseDouble(value)));
                        field.setByte(this.getModule(), (byte)Math.round(Double.parseDouble(value)));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
    @Override
    public void setValue(final Number value) {
        super.setValue(value);
        Field[] declaredFields;
        for (int length = (declaredFields = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Op.class) && field.getName().equalsIgnoreCase(this.getId())) {
                try {
                    if (field.getType().isAssignableFrom(Float.TYPE)) {
                        field.setFloat(this.getModule(), value.floatValue());
                    }
                    else if (field.getType().isAssignableFrom(Double.TYPE)) {
                        field.setDouble(this.getModule(), value.doubleValue());
                    }
                    else if (field.getType().isAssignableFrom(Long.TYPE)) {
                        field.setLong(this.getModule(), value.longValue());
                    }
                    else if (field.getType().isAssignableFrom(Integer.TYPE)) {
                        field.setLong(this.getModule(), value.intValue());
                    }
                    else if (field.getType().isAssignableFrom(Short.TYPE)) {
                        field.setLong(this.getModule(), value.shortValue());
                    }
                    else if (field.getType().isAssignableFrom(Byte.TYPE)) {
                        field.setLong(this.getModule(), value.byteValue());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public double getIncrement() {
        return this.increment;
    }
    
    public void setMin(final double min) {
        this.min = min;
    }
    
    public void setMax(final double max) {
        this.max = max;
    }
    
    public void setIncrement(final double increment) {
        this.increment = increment;
    }
    
    public void increment() {
        this.setValue(Math.min(this.getValue().doubleValue() + this.increment, this.max));
    }
    
    public void deincrement() {
        this.setValue(Math.max(this.getValue().doubleValue() - this.increment, this.min));
    }
}
