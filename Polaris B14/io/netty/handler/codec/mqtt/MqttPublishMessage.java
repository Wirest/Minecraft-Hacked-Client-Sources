/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MqttPublishMessage
/*     */   extends MqttMessage
/*     */   implements ByteBufHolder
/*     */ {
/*     */   public MqttPublishMessage(MqttFixedHeader mqttFixedHeader, MqttPublishVariableHeader variableHeader, ByteBuf payload)
/*     */   {
/*  32 */     super(mqttFixedHeader, variableHeader, payload);
/*     */   }
/*     */   
/*     */   public MqttPublishVariableHeader variableHeader()
/*     */   {
/*  37 */     return (MqttPublishVariableHeader)super.variableHeader();
/*     */   }
/*     */   
/*     */   public ByteBuf payload()
/*     */   {
/*  42 */     return content();
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/*  47 */     ByteBuf data = (ByteBuf)super.payload();
/*  48 */     if (data.refCnt() <= 0) {
/*  49 */       throw new IllegalReferenceCountException(data.refCnt());
/*     */     }
/*  51 */     return data;
/*     */   }
/*     */   
/*     */   public MqttPublishMessage copy()
/*     */   {
/*  56 */     return new MqttPublishMessage(fixedHeader(), variableHeader(), content().copy());
/*     */   }
/*     */   
/*     */   public MqttPublishMessage duplicate()
/*     */   {
/*  61 */     return new MqttPublishMessage(fixedHeader(), variableHeader(), content().duplicate());
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  66 */     return content().refCnt();
/*     */   }
/*     */   
/*     */   public MqttPublishMessage retain()
/*     */   {
/*  71 */     content().retain();
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public MqttPublishMessage retain(int increment)
/*     */   {
/*  77 */     content().retain(increment);
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public MqttPublishMessage touch()
/*     */   {
/*  83 */     content().touch();
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public MqttPublishMessage touch(Object hint)
/*     */   {
/*  89 */     content().touch(hint);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/*  95 */     return content().release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 100 */     return content().release(decrement);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttPublishMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */