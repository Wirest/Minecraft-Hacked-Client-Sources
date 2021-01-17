// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformInt extends ShaderUniformBase
{
    private int value;
    
    public ShaderUniformInt(final String name) {
        super(name);
        this.value = -1;
    }
    
    @Override
    protected void onProgramChanged() {
        this.value = -1;
    }
    
    public void setValue(final int value) {
        if (this.getLocation() >= 0 && this.value != value) {
            ARBShaderObjects.glUniform1iARB(this.getLocation(), value);
            Shaders.checkGLError(this.getName());
            this.value = value;
        }
    }
    
    public int getValue() {
        return this.value;
    }
}
