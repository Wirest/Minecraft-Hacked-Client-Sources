// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformFloat4 extends ShaderUniformBase
{
    private float[] values;
    
    public ShaderUniformFloat4(final String name) {
        super(name);
        this.values = new float[4];
    }
    
    @Override
    protected void onProgramChanged() {
        this.values[0] = 0.0f;
        this.values[1] = 0.0f;
        this.values[2] = 0.0f;
        this.values[3] = 0.0f;
    }
    
    public void setValue(final float f0, final float f1, final float f2, final float f3) {
        if (this.getLocation() >= 0 && (this.values[0] != f0 || this.values[1] != f1 || this.values[2] != f2 || this.values[3] != f3)) {
            ARBShaderObjects.glUniform4fARB(this.getLocation(), f0, f1, f2, f3);
            Shaders.checkGLError(this.getName());
            this.values[0] = f0;
            this.values[1] = f1;
            this.values[2] = f2;
            this.values[3] = f3;
        }
    }
    
    public float[] getValues() {
        return this.values;
    }
}
