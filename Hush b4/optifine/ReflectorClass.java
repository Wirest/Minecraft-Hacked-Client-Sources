// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class ReflectorClass
{
    private String targetClassName;
    private boolean checked;
    private Class targetClass;
    
    public ReflectorClass(final String p_i81_1_) {
        this(p_i81_1_, false);
    }
    
    public ReflectorClass(final String p_i82_1_, final boolean p_i82_2_) {
        this.targetClassName = null;
        this.checked = false;
        this.targetClass = null;
        this.targetClassName = p_i82_1_;
        if (!p_i82_2_) {
            this.getTargetClass();
        }
    }
    
    public ReflectorClass(final Class p_i83_1_) {
        this.targetClassName = null;
        this.checked = false;
        this.targetClass = null;
        this.targetClass = p_i83_1_;
        this.targetClassName = p_i83_1_.getName();
        this.checked = true;
    }
    
    public Class getTargetClass() {
        if (this.checked) {
            return this.targetClass;
        }
        this.checked = true;
        try {
            this.targetClass = Class.forName(this.targetClassName);
        }
        catch (ClassNotFoundException var2) {
            Config.log("(Reflector) Class not present: " + this.targetClassName);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return this.targetClass;
    }
    
    public boolean exists() {
        return this.getTargetClass() != null;
    }
    
    public String getTargetClassName() {
        return this.targetClassName;
    }
    
    public boolean isInstance(final Object p_isInstance_1_) {
        return this.getTargetClass() != null && this.getTargetClass().isInstance(p_isInstance_1_);
    }
    
    public ReflectorField makeField(final String p_makeField_1_) {
        return new ReflectorField(this, p_makeField_1_);
    }
    
    public ReflectorMethod makeMethod(final String p_makeMethod_1_) {
        return new ReflectorMethod(this, p_makeMethod_1_);
    }
    
    public ReflectorMethod makeMethod(final String p_makeMethod_1_, final Class[] p_makeMethod_2_) {
        return new ReflectorMethod(this, p_makeMethod_1_, p_makeMethod_2_);
    }
    
    public ReflectorMethod makeMethod(final String p_makeMethod_1_, final Class[] p_makeMethod_2_, final boolean p_makeMethod_3_) {
        return new ReflectorMethod(this, p_makeMethod_1_, p_makeMethod_2_, p_makeMethod_3_);
    }
}
