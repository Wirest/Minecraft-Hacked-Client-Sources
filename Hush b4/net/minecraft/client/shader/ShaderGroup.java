// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.GL11;
import java.io.FileNotFoundException;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.IResource;
import java.io.InputStream;
import com.google.gson.JsonElement;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import com.google.common.base.Charsets;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.client.util.JsonException;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.util.vector.Matrix4f;
import java.util.Map;
import java.util.List;
import net.minecraft.client.resources.IResourceManager;

public class ShaderGroup
{
    private Framebuffer mainFramebuffer;
    private IResourceManager resourceManager;
    private String shaderGroupName;
    private final List<Shader> listShaders;
    private final Map<String, Framebuffer> mapFramebuffers;
    private final List<Framebuffer> listFramebuffers;
    private Matrix4f projectionMatrix;
    private int mainFramebufferWidth;
    private int mainFramebufferHeight;
    private float field_148036_j;
    private float field_148037_k;
    
    public ShaderGroup(final TextureManager p_i1050_1_, final IResourceManager p_i1050_2_, final Framebuffer p_i1050_3_, final ResourceLocation p_i1050_4_) throws JsonException, IOException, JsonSyntaxException {
        this.listShaders = (List<Shader>)Lists.newArrayList();
        this.mapFramebuffers = (Map<String, Framebuffer>)Maps.newHashMap();
        this.listFramebuffers = (List<Framebuffer>)Lists.newArrayList();
        this.resourceManager = p_i1050_2_;
        this.mainFramebuffer = p_i1050_3_;
        this.field_148036_j = 0.0f;
        this.field_148037_k = 0.0f;
        this.mainFramebufferWidth = p_i1050_3_.framebufferWidth;
        this.mainFramebufferHeight = p_i1050_3_.framebufferHeight;
        this.shaderGroupName = p_i1050_4_.toString();
        this.resetProjectionMatrix();
        this.parseGroup(p_i1050_1_, p_i1050_4_);
    }
    
    public void parseGroup(final TextureManager p_152765_1_, final ResourceLocation p_152765_2_) throws JsonException, IOException, JsonSyntaxException {
        final JsonParser jsonparser = new JsonParser();
        InputStream inputstream = null;
        try {
            final IResource iresource = this.resourceManager.getResource(p_152765_2_);
            inputstream = iresource.getInputStream();
            final JsonObject jsonobject = jsonparser.parse(IOUtils.toString(inputstream, Charsets.UTF_8)).getAsJsonObject();
            if (JsonUtils.isJsonArray(jsonobject, "targets")) {
                final JsonArray jsonarray = jsonobject.getAsJsonArray("targets");
                int i = 0;
                for (final JsonElement jsonelement : jsonarray) {
                    try {
                        this.initTarget(jsonelement);
                    }
                    catch (Exception exception1) {
                        final JsonException jsonexception1 = JsonException.func_151379_a(exception1);
                        jsonexception1.func_151380_a("targets[" + i + "]");
                        throw jsonexception1;
                    }
                    ++i;
                }
            }
            if (JsonUtils.isJsonArray(jsonobject, "passes")) {
                final JsonArray jsonarray2 = jsonobject.getAsJsonArray("passes");
                int j = 0;
                for (final JsonElement jsonelement2 : jsonarray2) {
                    try {
                        this.parsePass(p_152765_1_, jsonelement2);
                    }
                    catch (Exception exception2) {
                        final JsonException jsonexception2 = JsonException.func_151379_a(exception2);
                        jsonexception2.func_151380_a("passes[" + j + "]");
                        throw jsonexception2;
                    }
                    ++j;
                }
            }
        }
        catch (Exception exception3) {
            final JsonException jsonexception3 = JsonException.func_151379_a(exception3);
            jsonexception3.func_151381_b(p_152765_2_.getResourcePath());
            throw jsonexception3;
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
        IOUtils.closeQuietly(inputstream);
    }
    
    private void initTarget(final JsonElement p_148027_1_) throws JsonException {
        if (JsonUtils.isString(p_148027_1_)) {
            this.addFramebuffer(p_148027_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        }
        else {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_148027_1_, "target");
            final String s = JsonUtils.getString(jsonobject, "name");
            final int i = JsonUtils.getInt(jsonobject, "width", this.mainFramebufferWidth);
            final int j = JsonUtils.getInt(jsonobject, "height", this.mainFramebufferHeight);
            if (this.mapFramebuffers.containsKey(s)) {
                throw new JsonException(String.valueOf(s) + " is already defined");
            }
            this.addFramebuffer(s, i, j);
        }
    }
    
    private void parsePass(final TextureManager p_152764_1_, final JsonElement p_152764_2_) throws JsonException, IOException {
        final JsonObject jsonobject = JsonUtils.getJsonObject(p_152764_2_, "pass");
        final String s = JsonUtils.getString(jsonobject, "name");
        final String s2 = JsonUtils.getString(jsonobject, "intarget");
        final String s3 = JsonUtils.getString(jsonobject, "outtarget");
        final Framebuffer framebuffer = this.getFramebuffer(s2);
        final Framebuffer framebuffer2 = this.getFramebuffer(s3);
        if (framebuffer == null) {
            throw new JsonException("Input target '" + s2 + "' does not exist");
        }
        if (framebuffer2 == null) {
            throw new JsonException("Output target '" + s3 + "' does not exist");
        }
        final Shader shader = this.addShader(s, framebuffer, framebuffer2);
        final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "auxtargets", null);
        if (jsonarray != null) {
            int i = 0;
            for (final JsonElement jsonelement : jsonarray) {
                try {
                    final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonelement, "auxtarget");
                    final String s4 = JsonUtils.getString(jsonobject2, "name");
                    final String s5 = JsonUtils.getString(jsonobject2, "id");
                    final Framebuffer framebuffer3 = this.getFramebuffer(s5);
                    if (framebuffer3 == null) {
                        final ResourceLocation resourcelocation = new ResourceLocation("textures/effect/" + s5 + ".png");
                        try {
                            this.resourceManager.getResource(resourcelocation);
                        }
                        catch (FileNotFoundException var24) {
                            throw new JsonException("Render target or texture '" + s5 + "' does not exist");
                        }
                        p_152764_1_.bindTexture(resourcelocation);
                        final ITextureObject itextureobject = p_152764_1_.getTexture(resourcelocation);
                        final int j = JsonUtils.getInt(jsonobject2, "width");
                        final int k = JsonUtils.getInt(jsonobject2, "height");
                        final boolean flag = JsonUtils.getBoolean(jsonobject2, "bilinear");
                        if (flag) {
                            GL11.glTexParameteri(3553, 10241, 9729);
                            GL11.glTexParameteri(3553, 10240, 9729);
                        }
                        else {
                            GL11.glTexParameteri(3553, 10241, 9728);
                            GL11.glTexParameteri(3553, 10240, 9728);
                        }
                        shader.addAuxFramebuffer(s4, itextureobject.getGlTextureId(), j, k);
                    }
                    else {
                        shader.addAuxFramebuffer(s4, framebuffer3, framebuffer3.framebufferTextureWidth, framebuffer3.framebufferTextureHeight);
                    }
                }
                catch (Exception exception1) {
                    final JsonException jsonexception = JsonException.func_151379_a(exception1);
                    jsonexception.func_151380_a("auxtargets[" + i + "]");
                    throw jsonexception;
                }
                ++i;
            }
        }
        final JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonobject, "uniforms", null);
        if (jsonarray2 != null) {
            int l = 0;
            for (final JsonElement jsonelement2 : jsonarray2) {
                try {
                    this.initUniform(jsonelement2);
                }
                catch (Exception exception2) {
                    final JsonException jsonexception2 = JsonException.func_151379_a(exception2);
                    jsonexception2.func_151380_a("uniforms[" + l + "]");
                    throw jsonexception2;
                }
                ++l;
            }
        }
    }
    
    private void initUniform(final JsonElement p_148028_1_) throws JsonException {
        final JsonObject jsonobject = JsonUtils.getJsonObject(p_148028_1_, "uniform");
        final String s = JsonUtils.getString(jsonobject, "name");
        final ShaderUniform shaderuniform = this.listShaders.get(this.listShaders.size() - 1).getShaderManager().getShaderUniform(s);
        if (shaderuniform == null) {
            throw new JsonException("Uniform '" + s + "' does not exist");
        }
        final float[] afloat = new float[4];
        int i = 0;
        for (final JsonElement jsonelement : JsonUtils.getJsonArray(jsonobject, "values")) {
            try {
                afloat[i] = JsonUtils.getFloat(jsonelement, "value");
            }
            catch (Exception exception) {
                final JsonException jsonexception = JsonException.func_151379_a(exception);
                jsonexception.func_151380_a("values[" + i + "]");
                throw jsonexception;
            }
            ++i;
        }
        switch (i) {
            case 1: {
                shaderuniform.set(afloat[0]);
                break;
            }
            case 2: {
                shaderuniform.set(afloat[0], afloat[1]);
                break;
            }
            case 3: {
                shaderuniform.set(afloat[0], afloat[1], afloat[2]);
                break;
            }
            case 4: {
                shaderuniform.set(afloat[0], afloat[1], afloat[2], afloat[3]);
                break;
            }
        }
    }
    
    public Framebuffer getFramebufferRaw(final String p_177066_1_) {
        return this.mapFramebuffers.get(p_177066_1_);
    }
    
    public void addFramebuffer(final String p_148020_1_, final int p_148020_2_, final int p_148020_3_) {
        final Framebuffer framebuffer = new Framebuffer(p_148020_2_, p_148020_3_, true);
        framebuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.mapFramebuffers.put(p_148020_1_, framebuffer);
        if (p_148020_2_ == this.mainFramebufferWidth && p_148020_3_ == this.mainFramebufferHeight) {
            this.listFramebuffers.add(framebuffer);
        }
    }
    
    public void deleteShaderGroup() {
        for (final Framebuffer framebuffer : this.mapFramebuffers.values()) {
            framebuffer.deleteFramebuffer();
        }
        for (final Shader shader : this.listShaders) {
            shader.deleteShader();
        }
        this.listShaders.clear();
    }
    
    public Shader addShader(final String p_148023_1_, final Framebuffer p_148023_2_, final Framebuffer p_148023_3_) throws JsonException, IOException {
        final Shader shader = new Shader(this.resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
        this.listShaders.add(this.listShaders.size(), shader);
        return shader;
    }
    
    private void resetProjectionMatrix() {
        (this.projectionMatrix = new Matrix4f()).setIdentity();
        this.projectionMatrix.m00 = 2.0f / this.mainFramebuffer.framebufferTextureWidth;
        this.projectionMatrix.m11 = 2.0f / -this.mainFramebuffer.framebufferTextureHeight;
        this.projectionMatrix.m22 = -0.0020001999f;
        this.projectionMatrix.m33 = 1.0f;
        this.projectionMatrix.m03 = -1.0f;
        this.projectionMatrix.m13 = 1.0f;
        this.projectionMatrix.m23 = -1.0001999f;
    }
    
    public void createBindFramebuffers(final int width, final int height) {
        this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
        this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
        this.resetProjectionMatrix();
        for (final Shader shader : this.listShaders) {
            shader.setProjectionMatrix(this.projectionMatrix);
        }
        for (final Framebuffer framebuffer : this.listFramebuffers) {
            framebuffer.createBindFramebuffer(width, height);
        }
    }
    
    public void loadShaderGroup(final float partialTicks) {
        if (partialTicks < this.field_148037_k) {
            this.field_148036_j += 1.0f - this.field_148037_k;
            this.field_148036_j += partialTicks;
        }
        else {
            this.field_148036_j += partialTicks - this.field_148037_k;
        }
        this.field_148037_k = partialTicks;
        while (this.field_148036_j > 20.0f) {
            this.field_148036_j -= 20.0f;
        }
        for (final Shader shader : this.listShaders) {
            shader.loadShader(this.field_148036_j / 20.0f);
        }
    }
    
    public final String getShaderGroupName() {
        return this.shaderGroupName;
    }
    
    private Framebuffer getFramebuffer(final String p_148017_1_) {
        return (p_148017_1_ == null) ? null : (p_148017_1_.equals("minecraft:main") ? this.mainFramebuffer : this.mapFramebuffers.get(p_148017_1_));
    }
}
