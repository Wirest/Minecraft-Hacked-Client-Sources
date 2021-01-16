/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 */
package me.razerboy420.weepcraft.util;

import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ShaderUtil {
    Framebuffer entityFBO;
    Framebuffer overlayFBO;
    public int shaderProgramID;
    public int vertexShaderID;
    public int fragmentShaderID;
    public int diffuseSamperUniformID;
    public int texelSizeUniformID;

    public ShaderUtil() {
        this.entityFBO = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, false);
        this.overlayFBO = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, false);
        this.shaderProgramID = -1;
        this.vertexShaderID = -1;
        this.fragmentShaderID = -1;
        this.diffuseSamperUniformID = -1;
        this.texelSizeUniformID = -1;
    }

    private int createShader(String shaderCode, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB((int)shaderType);
            if (shader == 0) {
                return 0;
            }
        }
        catch (Exception exc) {
            ARBShaderObjects.glDeleteObjectARB((int)shader);
            throw exc;
        }
        ARBShaderObjects.glShaderSourceARB((int)shader, (CharSequence)shaderCode);
        ARBShaderObjects.glCompileShaderARB((int)shader);
        if (ARBShaderObjects.glGetObjectParameteriARB((int)shader, (int)35713) == 0) {
            throw new RuntimeException("Error creating shader: " + ShaderUtil.getLogInfo(shader));
        }
        return shader;
    }

    public void s() {
        if (this.shaderProgramID == -1) {
            this.shaderProgramID = ARBShaderObjects.glCreateProgramObjectARB();
            try {
                if (this.vertexShaderID == -1) {
                    String vertexShaderCode = "#version 120 \nvoid main() { \ngl_TexCoord[0] = gl_MultiTexCoord0; \ngl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; \n}";
                    this.vertexShaderID = this.createShader(vertexShaderCode, 35633);
                }
                if (this.fragmentShaderID == -1) {
                    String fragmentShaderCode = "#version 120 \nuniform sampler2D DiffuseSamper; \nuniform vec2 TexelSize; \nvoid main(){ \nvec4 centerCol = texture2D(DiffuseSamper, gl_TexCoord[0].st); \nif(centerCol.r == 1.0f && centerCol.g == 1.0f && centerCol.b == 1.0f) { \ngl_FragColor = vec4(0, 0, 0, 0); \nreturn; \n} \nvec4 colAvg = vec4(0, 0, 0, 0); \nfor(int xo = -7; xo < 7; xo++) { \nfor(int yo = -7; yo < 7; yo++) { \nvec4 currCol = texture2D(DiffuseSamper, gl_TexCoord[0].st + vec2(xo * TexelSize.x, yo * TexelSize.y)); \nif(currCol.r != 0.0F || currCol.g != 0.0F || currCol.b != 0.0F) { \ncolAvg += vec4(1, 0, 0, max(0, (6.0f - sqrt(xo*xo*1.0f + yo*yo*1.0f)) / 2.0F)); \n} \n} \n} \ncolAvg.a /= 64.0F; \ngl_FragColor = colAvg; \n}";
                    this.fragmentShaderID = this.createShader(fragmentShaderCode, 35632);
                }
            }
            catch (Exception ex) {
                this.shaderProgramID = -1;
                this.vertexShaderID = -1;
                this.fragmentShaderID = -1;
                ex.printStackTrace();
            }
            if (this.shaderProgramID != -1) {
                ARBShaderObjects.glAttachObjectARB((int)this.shaderProgramID, (int)this.vertexShaderID);
                ARBShaderObjects.glAttachObjectARB((int)this.shaderProgramID, (int)this.fragmentShaderID);
                ARBShaderObjects.glLinkProgramARB((int)this.shaderProgramID);
                if (ARBShaderObjects.glGetObjectParameteriARB((int)this.shaderProgramID, (int)35714) == 0) {
                    System.err.println(ShaderUtil.getLogInfo(this.shaderProgramID));
                    return;
                }
                ARBShaderObjects.glValidateProgramARB((int)this.shaderProgramID);
                if (ARBShaderObjects.glGetObjectParameteriARB((int)this.shaderProgramID, (int)35715) == 0) {
                    System.err.println(ShaderUtil.getLogInfo(this.shaderProgramID));
                    return;
                }
                ARBShaderObjects.glUseProgramObjectARB((int)0);
                this.diffuseSamperUniformID = ARBShaderObjects.glGetUniformLocationARB((int)this.shaderProgramID, (CharSequence)"DiffuseSamper");
                this.texelSizeUniformID = ARBShaderObjects.glGetUniformLocationARB((int)this.shaderProgramID, (CharSequence)"TexelSize");
            }
        }
    }

    public void clear() {
        this.overlayFBO.bindFramebuffer(false);
        GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glClear((int)16640);
        this.entityFBO.bindFramebuffer(false);
        GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glClear((int)16640);
    }

    public void bind(int shaderProgramID) {
        ARBShaderObjects.glUseProgramObjectARB((int)shaderProgramID);
    }

    public void upload() {
        ARBShaderObjects.glUniform1iARB((int)this.diffuseSamperUniformID, (int)0);
        GL13.glActiveTexture((int)33984);
        GL11.glEnable((int)3553);
        this.entityFBO.bindFramebufferTexture();
    }

    public void uploadTexel() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        FloatBuffer texelSizeBuffer = BufferUtils.createFloatBuffer((int)2);
        texelSizeBuffer.position(0);
        texelSizeBuffer.put(1.0f / (float)sr.getScaledWidth());
        texelSizeBuffer.put(1.0f / (float)sr.getScaledHeight());
        texelSizeBuffer.flip();
        ARBShaderObjects.glUniform2ARB((int)this.texelSizeUniformID, (FloatBuffer)texelSizeBuffer);
    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB((int)obj, (int)ARBShaderObjects.glGetObjectParameteriARB((int)obj, (int)35716));
    }
}

