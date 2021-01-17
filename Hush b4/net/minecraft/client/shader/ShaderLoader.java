// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.util.Map;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.util.JsonException;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;
import java.io.BufferedInputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.OpenGlHelper;

public class ShaderLoader
{
    private final ShaderType shaderType;
    private final String shaderFilename;
    private int shader;
    private int shaderAttachCount;
    
    private ShaderLoader(final ShaderType type, final int shaderId, final String filename) {
        this.shaderAttachCount = 0;
        this.shaderType = type;
        this.shader = shaderId;
        this.shaderFilename = filename;
    }
    
    public void attachShader(final ShaderManager manager) {
        ++this.shaderAttachCount;
        OpenGlHelper.glAttachShader(manager.getProgram(), this.shader);
    }
    
    public void deleteShader(final ShaderManager manager) {
        --this.shaderAttachCount;
        if (this.shaderAttachCount <= 0) {
            OpenGlHelper.glDeleteShader(this.shader);
            this.shaderType.getLoadedShaders().remove(this.shaderFilename);
        }
    }
    
    public String getShaderFilename() {
        return this.shaderFilename;
    }
    
    public static ShaderLoader loadShader(final IResourceManager resourceManager, final ShaderType type, final String filename) throws IOException {
        ShaderLoader shaderloader = type.getLoadedShaders().get(filename);
        if (shaderloader == null) {
            final ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + filename + type.getShaderExtension());
            final BufferedInputStream bufferedinputstream = new BufferedInputStream(resourceManager.getResource(resourcelocation).getInputStream());
            final byte[] abyte = toByteArray(bufferedinputstream);
            final ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
            bytebuffer.put(abyte);
            bytebuffer.position(0);
            final int i = OpenGlHelper.glCreateShader(type.getShaderMode());
            OpenGlHelper.glShaderSource(i, bytebuffer);
            OpenGlHelper.glCompileShader(i);
            if (OpenGlHelper.glGetShaderi(i, OpenGlHelper.GL_COMPILE_STATUS) == 0) {
                final String s = StringUtils.trim(OpenGlHelper.glGetShaderInfoLog(i, 32768));
                final JsonException jsonexception = new JsonException("Couldn't compile " + type.getShaderName() + " program: " + s);
                jsonexception.func_151381_b(resourcelocation.getResourcePath());
                throw jsonexception;
            }
            shaderloader = new ShaderLoader(type, i, filename);
            type.getLoadedShaders().put(filename, shaderloader);
        }
        return shaderloader;
    }
    
    protected static byte[] toByteArray(final BufferedInputStream p_177064_0_) throws IOException {
        byte[] abyte;
        try {
            abyte = IOUtils.toByteArray(p_177064_0_);
        }
        finally {
            p_177064_0_.close();
        }
        p_177064_0_.close();
        return abyte;
    }
    
    public enum ShaderType
    {
        VERTEX("VERTEX", 0, "vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER), 
        FRAGMENT("FRAGMENT", 1, "fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);
        
        private final String shaderName;
        private final String shaderExtension;
        private final int shaderMode;
        private final Map<String, ShaderLoader> loadedShaders;
        
        private ShaderType(final String name, final int ordinal, final String p_i45090_3_, final String p_i45090_4_, final int p_i45090_5_) {
            this.loadedShaders = (Map<String, ShaderLoader>)Maps.newHashMap();
            this.shaderName = p_i45090_3_;
            this.shaderExtension = p_i45090_4_;
            this.shaderMode = p_i45090_5_;
        }
        
        public String getShaderName() {
            return this.shaderName;
        }
        
        protected String getShaderExtension() {
            return this.shaderExtension;
        }
        
        protected int getShaderMode() {
            return this.shaderMode;
        }
        
        protected Map<String, ShaderLoader> getLoadedShaders() {
            return this.loadedShaders;
        }
    }
}
