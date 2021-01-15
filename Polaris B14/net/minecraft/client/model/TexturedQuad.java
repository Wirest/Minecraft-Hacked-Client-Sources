/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class TexturedQuad
/*    */ {
/*    */   public PositionTextureVertex[] vertexPositions;
/*    */   public int nVertices;
/*    */   private boolean invertNormal;
/*    */   
/*    */   public TexturedQuad(PositionTextureVertex[] vertices)
/*    */   {
/* 16 */     this.vertexPositions = vertices;
/* 17 */     this.nVertices = vertices.length;
/*    */   }
/*    */   
/*    */   public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight)
/*    */   {
/* 22 */     this(vertices);
/* 23 */     float f = 0.0F / textureWidth;
/* 24 */     float f1 = 0.0F / textureHeight;
/* 25 */     vertices[0] = vertices[0].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV1 / textureHeight + f1);
/* 26 */     vertices[1] = vertices[1].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV1 / textureHeight + f1);
/* 27 */     vertices[2] = vertices[2].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV2 / textureHeight - f1);
/* 28 */     vertices[3] = vertices[3].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV2 / textureHeight - f1);
/*    */   }
/*    */   
/*    */   public void flipFace()
/*    */   {
/* 33 */     PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];
/*    */     
/* 35 */     for (int i = 0; i < this.vertexPositions.length; i++)
/*    */     {
/* 37 */       apositiontexturevertex[i] = this.vertexPositions[(this.vertexPositions.length - i - 1)];
/*    */     }
/*    */     
/* 40 */     this.vertexPositions = apositiontexturevertex;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void draw(WorldRenderer renderer, float scale)
/*    */   {
/* 49 */     Vec3 vec3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
/* 50 */     Vec3 vec31 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
/* 51 */     Vec3 vec32 = vec31.crossProduct(vec3).normalize();
/* 52 */     float f = (float)vec32.xCoord;
/* 53 */     float f1 = (float)vec32.yCoord;
/* 54 */     float f2 = (float)vec32.zCoord;
/*    */     
/* 56 */     if (this.invertNormal)
/*    */     {
/* 58 */       f = -f;
/* 59 */       f1 = -f1;
/* 60 */       f2 = -f2;
/*    */     }
/*    */     
/* 63 */     renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
/*    */     
/* 65 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 67 */       PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
/* 68 */       renderer.pos(positiontexturevertex.vector3D.xCoord * scale, positiontexturevertex.vector3D.yCoord * scale, positiontexturevertex.vector3D.zCoord * scale).tex(positiontexturevertex.texturePositionX, positiontexturevertex.texturePositionY).normal(f, f1, f2).endVertex();
/*    */     }
/*    */     
/* 71 */     Tessellator.getInstance().draw();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\TexturedQuad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */