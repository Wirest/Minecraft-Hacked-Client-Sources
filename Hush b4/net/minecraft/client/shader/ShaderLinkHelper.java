// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import java.io.IOException;
import net.minecraft.client.util.JsonException;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderLinkHelper
{
    private static final Logger logger;
    private static ShaderLinkHelper staticShaderLinkHelper;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public static void setNewStaticShaderLinkHelper() {
        ShaderLinkHelper.staticShaderLinkHelper = new ShaderLinkHelper();
    }
    
    public static ShaderLinkHelper getStaticShaderLinkHelper() {
        return ShaderLinkHelper.staticShaderLinkHelper;
    }
    
    public void deleteShader(final ShaderManager p_148077_1_) {
        p_148077_1_.getFragmentShaderLoader().deleteShader(p_148077_1_);
        p_148077_1_.getVertexShaderLoader().deleteShader(p_148077_1_);
        OpenGlHelper.glDeleteProgram(p_148077_1_.getProgram());
    }
    
    public int createProgram() throws JsonException {
        final int i = OpenGlHelper.glCreateProgram();
        if (i <= 0) {
            throw new JsonException("Could not create shader program (returned program ID " + i + ")");
        }
        return i;
    }
    
    public void linkProgram(final ShaderManager manager) throws IOException {
        manager.getFragmentShaderLoader().attachShader(manager);
        manager.getVertexShaderLoader().attachShader(manager);
        OpenGlHelper.glLinkProgram(manager.getProgram());
        final int i = OpenGlHelper.glGetProgrami(manager.getProgram(), OpenGlHelper.GL_LINK_STATUS);
        if (i == 0) {
            ShaderLinkHelper.logger.warn("Error encountered when linking program containing VS " + manager.getVertexShaderLoader().getShaderFilename() + " and FS " + manager.getFragmentShaderLoader().getShaderFilename() + ". Log output:");
            ShaderLinkHelper.logger.warn(OpenGlHelper.glGetProgramInfoLog(manager.getProgram(), 32768));
        }
    }
}
