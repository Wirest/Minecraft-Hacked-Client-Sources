package net.minecraft.client.shader;

import com.google.common.collect.Maps;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader {
    private final ShaderLoader.ShaderType shaderType;
    private final String shaderFilename;
    private int shader;
    private int shaderAttachCount = 0;
    private static final String __OBFID = "CL_00001043";

    private ShaderLoader(ShaderLoader.ShaderType type, int shaderId, String filename) {
        this.shaderType = type;
        this.shader = shaderId;
        this.shaderFilename = filename;
    }

    public void attachShader(ShaderManager manager) {
        ++this.shaderAttachCount;
        OpenGlHelper.glAttachShader(manager.getProgram(), this.shader);
    }

    public void deleteShader(ShaderManager manager) {
        --this.shaderAttachCount;

        if (this.shaderAttachCount <= 0) {
            OpenGlHelper.glDeleteShader(this.shader);
            this.shaderType.getLoadedShaders().remove(this.shaderFilename);
        }
    }

    public String getShaderFilename() {
        return this.shaderFilename;
    }

    public static ShaderLoader loadShader(IResourceManager resourceManager, ShaderLoader.ShaderType type, String filename) throws IOException {
        ShaderLoader var3 = (ShaderLoader) type.getLoadedShaders().get(filename);

        if (var3 == null) {
            ResourceLocation var4 = new ResourceLocation("shaders/program/" + filename + type.getShaderExtension());
            BufferedInputStream var5 = new BufferedInputStream(resourceManager.getResource(var4).getInputStream());
            byte[] var6 = func_177064_a(var5);
            ByteBuffer var7 = BufferUtils.createByteBuffer(var6.length);
            var7.put(var6);
            var7.position(0);
            int var8 = OpenGlHelper.glCreateShader(type.getShaderMode());
            OpenGlHelper.glShaderSource(var8, var7);
            OpenGlHelper.glCompileShader(var8);

            if (OpenGlHelper.glGetShaderi(var8, OpenGlHelper.GL_COMPILE_STATUS) == 0) {
                String var9 = StringUtils.trim(OpenGlHelper.glGetShaderInfoLog(var8, 32768));
                JsonException var10 = new JsonException("Couldn\'t compile " + type.getShaderName() + " program: " + var9);
                var10.func_151381_b(var4.getResourcePath());
                throw var10;
            }

            var3 = new ShaderLoader(type, var8, filename);
            type.getLoadedShaders().put(filename, var3);
        }

        return var3;
    }

    protected static byte[] func_177064_a(BufferedInputStream p_177064_0_) throws IOException {
        byte[] var1;

        try {
            var1 = IOUtils.toByteArray(p_177064_0_);
        } finally {
            p_177064_0_.close();
        }

        return var1;
    }

    public static enum ShaderType {
        VERTEX("VERTEX", 0, "vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER),
        FRAGMENT("FRAGMENT", 1, "fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);
        private final String shaderName;
        private final String shaderExtension;
        private final int shaderMode;
        private final Map loadedShaders = Maps.newHashMap();

        private static final ShaderLoader.ShaderType[] $VALUES = new ShaderLoader.ShaderType[]{VERTEX, FRAGMENT};
        private static final String __OBFID = "CL_00001044";

        private ShaderType(String p_i45090_1_, int p_i45090_2_, String p_i45090_3_, String p_i45090_4_, int p_i45090_5_) {
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

        protected Map getLoadedShaders() {
            return this.loadedShaders;
        }
    }
}
