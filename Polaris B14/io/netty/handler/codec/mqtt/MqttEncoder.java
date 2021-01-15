/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class MqttEncoder
/*     */   extends MessageToMessageEncoder<MqttMessage>
/*     */ {
/*  35 */   public static final MqttEncoder DEFAUL_ENCODER = new MqttEncoder();
/*     */   
/*  37 */   private static final byte[] EMPTY = new byte[0];
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, MqttMessage msg, List<Object> out) throws Exception
/*     */   {
/*  41 */     out.add(doEncode(ctx.alloc(), msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static ByteBuf doEncode(ByteBufAllocator byteBufAllocator, MqttMessage message)
/*     */   {
/*  54 */     switch (message.fixedHeader().messageType()) {
/*     */     case CONNECT: 
/*  56 */       return encodeConnectMessage(byteBufAllocator, (MqttConnectMessage)message);
/*     */     
/*     */     case CONNACK: 
/*  59 */       return encodeConnAckMessage(byteBufAllocator, (MqttConnAckMessage)message);
/*     */     
/*     */     case PUBLISH: 
/*  62 */       return encodePublishMessage(byteBufAllocator, (MqttPublishMessage)message);
/*     */     
/*     */     case SUBSCRIBE: 
/*  65 */       return encodeSubscribeMessage(byteBufAllocator, (MqttSubscribeMessage)message);
/*     */     
/*     */     case UNSUBSCRIBE: 
/*  68 */       return encodeUnsubscribeMessage(byteBufAllocator, (MqttUnsubscribeMessage)message);
/*     */     
/*     */     case SUBACK: 
/*  71 */       return encodeSubAckMessage(byteBufAllocator, (MqttSubAckMessage)message);
/*     */     
/*     */     case UNSUBACK: 
/*     */     case PUBACK: 
/*     */     case PUBREC: 
/*     */     case PUBREL: 
/*     */     case PUBCOMP: 
/*  78 */       return encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(byteBufAllocator, message);
/*     */     
/*     */     case PINGREQ: 
/*     */     case PINGRESP: 
/*     */     case DISCONNECT: 
/*  83 */       return encodeMessageWithOnlySingleByteFixedHeader(byteBufAllocator, message);
/*     */     }
/*     */     
/*  86 */     throw new IllegalArgumentException("Unknown message type: " + message.fixedHeader().messageType().value());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ByteBuf encodeConnectMessage(ByteBufAllocator byteBufAllocator, MqttConnectMessage message)
/*     */   {
/*  94 */     int payloadBufferSize = 0;
/*     */     
/*  96 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/*  97 */     MqttConnectVariableHeader variableHeader = message.variableHeader();
/*  98 */     MqttConnectPayload payload = message.payload();
/*  99 */     MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(variableHeader.name(), (byte)variableHeader.version());
/*     */     
/*     */ 
/*     */ 
/* 103 */     String clientIdentifier = payload.clientIdentifier();
/* 104 */     if (!MqttCodecUtil.isValidClientId(mqttVersion, clientIdentifier)) {
/* 105 */       throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + clientIdentifier);
/*     */     }
/* 107 */     byte[] clientIdentifierBytes = encodeStringUtf8(clientIdentifier);
/* 108 */     payloadBufferSize += 2 + clientIdentifierBytes.length;
/*     */     
/*     */ 
/* 111 */     String willTopic = payload.willTopic();
/* 112 */     byte[] willTopicBytes = willTopic != null ? encodeStringUtf8(willTopic) : EMPTY;
/* 113 */     String willMessage = payload.willMessage();
/* 114 */     byte[] willMessageBytes = willMessage != null ? encodeStringUtf8(willMessage) : EMPTY;
/* 115 */     if (variableHeader.isWillFlag()) {
/* 116 */       payloadBufferSize += 2 + willTopicBytes.length;
/* 117 */       payloadBufferSize += 2 + willMessageBytes.length;
/*     */     }
/*     */     
/* 120 */     String userName = payload.userName();
/* 121 */     byte[] userNameBytes = userName != null ? encodeStringUtf8(userName) : EMPTY;
/* 122 */     if (variableHeader.hasUserName()) {
/* 123 */       payloadBufferSize += 2 + userNameBytes.length;
/*     */     }
/*     */     
/* 126 */     String password = payload.password();
/* 127 */     byte[] passwordBytes = password != null ? encodeStringUtf8(password) : EMPTY;
/* 128 */     if (variableHeader.hasPassword()) {
/* 129 */       payloadBufferSize += 2 + passwordBytes.length;
/*     */     }
/*     */     
/*     */ 
/* 133 */     byte[] protocolNameBytes = mqttVersion.protocolNameBytes();
/* 134 */     int variableHeaderBufferSize = 2 + protocolNameBytes.length + 4;
/* 135 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 136 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/* 137 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 138 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 139 */     writeVariableLengthInt(buf, variablePartSize);
/*     */     
/* 141 */     buf.writeShort(protocolNameBytes.length);
/* 142 */     buf.writeBytes(protocolNameBytes);
/*     */     
/* 144 */     buf.writeByte(variableHeader.version());
/* 145 */     buf.writeByte(getConnVariableHeaderFlag(variableHeader));
/* 146 */     buf.writeShort(variableHeader.keepAliveTimeSeconds());
/*     */     
/*     */ 
/* 149 */     buf.writeShort(clientIdentifierBytes.length);
/* 150 */     buf.writeBytes(clientIdentifierBytes, 0, clientIdentifierBytes.length);
/* 151 */     if (variableHeader.isWillFlag()) {
/* 152 */       buf.writeShort(willTopicBytes.length);
/* 153 */       buf.writeBytes(willTopicBytes, 0, willTopicBytes.length);
/* 154 */       buf.writeShort(willMessageBytes.length);
/* 155 */       buf.writeBytes(willMessageBytes, 0, willMessageBytes.length);
/*     */     }
/* 157 */     if (variableHeader.hasUserName()) {
/* 158 */       buf.writeShort(userNameBytes.length);
/* 159 */       buf.writeBytes(userNameBytes, 0, userNameBytes.length);
/*     */     }
/* 161 */     if (variableHeader.hasPassword()) {
/* 162 */       buf.writeShort(passwordBytes.length);
/* 163 */       buf.writeBytes(passwordBytes, 0, passwordBytes.length);
/*     */     }
/* 165 */     return buf;
/*     */   }
/*     */   
/*     */   private static int getConnVariableHeaderFlag(MqttConnectVariableHeader variableHeader) {
/* 169 */     int flagByte = 0;
/* 170 */     if (variableHeader.hasUserName()) {
/* 171 */       flagByte |= 0x80;
/*     */     }
/* 173 */     if (variableHeader.hasPassword()) {
/* 174 */       flagByte |= 0x40;
/*     */     }
/* 176 */     if (variableHeader.isWillRetain()) {
/* 177 */       flagByte |= 0x20;
/*     */     }
/* 179 */     flagByte |= (variableHeader.willQos() & 0x3) << 3;
/* 180 */     if (variableHeader.isWillFlag()) {
/* 181 */       flagByte |= 0x4;
/*     */     }
/* 183 */     if (variableHeader.isCleanSession()) {
/* 184 */       flagByte |= 0x2;
/*     */     }
/* 186 */     return flagByte;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ByteBuf encodeConnAckMessage(ByteBufAllocator byteBufAllocator, MqttConnAckMessage message)
/*     */   {
/* 192 */     ByteBuf buf = byteBufAllocator.buffer(4);
/* 193 */     buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
/* 194 */     buf.writeByte(2);
/* 195 */     buf.writeByte(0);
/* 196 */     buf.writeByte(message.variableHeader().connectReturnCode().byteValue());
/*     */     
/* 198 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ByteBuf encodeSubscribeMessage(ByteBufAllocator byteBufAllocator, MqttSubscribeMessage message)
/*     */   {
/* 204 */     int variableHeaderBufferSize = 2;
/* 205 */     int payloadBufferSize = 0;
/*     */     
/* 207 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 208 */     MqttMessageIdVariableHeader variableHeader = message.variableHeader();
/* 209 */     MqttSubscribePayload payload = message.payload();
/*     */     
/* 211 */     for (MqttTopicSubscription topic : payload.topicSubscriptions()) {
/* 212 */       String topicName = topic.topicName();
/* 213 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 214 */       payloadBufferSize += 2 + topicNameBytes.length;
/* 215 */       payloadBufferSize++;
/*     */     }
/*     */     
/* 218 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 219 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/*     */     
/* 221 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 222 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 223 */     writeVariableLengthInt(buf, variablePartSize);
/*     */     
/*     */ 
/* 226 */     int messageId = variableHeader.messageId();
/* 227 */     buf.writeShort(messageId);
/*     */     
/*     */ 
/* 230 */     for (MqttTopicSubscription topic : payload.topicSubscriptions()) {
/* 231 */       String topicName = topic.topicName();
/* 232 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 233 */       buf.writeShort(topicNameBytes.length);
/* 234 */       buf.writeBytes(topicNameBytes, 0, topicNameBytes.length);
/* 235 */       buf.writeByte(topic.qualityOfService().value());
/*     */     }
/*     */     
/* 238 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ByteBuf encodeUnsubscribeMessage(ByteBufAllocator byteBufAllocator, MqttUnsubscribeMessage message)
/*     */   {
/* 244 */     int variableHeaderBufferSize = 2;
/* 245 */     int payloadBufferSize = 0;
/*     */     
/* 247 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 248 */     MqttMessageIdVariableHeader variableHeader = message.variableHeader();
/* 249 */     MqttUnsubscribePayload payload = message.payload();
/*     */     
/* 251 */     for (String topicName : payload.topics()) {
/* 252 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 253 */       payloadBufferSize += 2 + topicNameBytes.length;
/*     */     }
/*     */     
/* 256 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 257 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/*     */     
/* 259 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 260 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 261 */     writeVariableLengthInt(buf, variablePartSize);
/*     */     
/*     */ 
/* 264 */     int messageId = variableHeader.messageId();
/* 265 */     buf.writeShort(messageId);
/*     */     
/*     */ 
/* 268 */     for (String topicName : payload.topics()) {
/* 269 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 270 */       buf.writeShort(topicNameBytes.length);
/* 271 */       buf.writeBytes(topicNameBytes, 0, topicNameBytes.length);
/*     */     }
/*     */     
/* 274 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ByteBuf encodeSubAckMessage(ByteBufAllocator byteBufAllocator, MqttSubAckMessage message)
/*     */   {
/* 280 */     int variableHeaderBufferSize = 2;
/* 281 */     int payloadBufferSize = message.payload().grantedQoSLevels().size();
/* 282 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 283 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/* 284 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 285 */     buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
/* 286 */     writeVariableLengthInt(buf, variablePartSize);
/* 287 */     buf.writeShort(message.variableHeader().messageId());
/* 288 */     for (Iterator i$ = message.payload().grantedQoSLevels().iterator(); i$.hasNext();) { int qos = ((Integer)i$.next()).intValue();
/* 289 */       buf.writeByte(qos);
/*     */     }
/*     */     
/* 292 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ByteBuf encodePublishMessage(ByteBufAllocator byteBufAllocator, MqttPublishMessage message)
/*     */   {
/* 298 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 299 */     MqttPublishVariableHeader variableHeader = message.variableHeader();
/* 300 */     ByteBuf payload = message.payload().duplicate();
/*     */     
/* 302 */     String topicName = variableHeader.topicName();
/* 303 */     byte[] topicNameBytes = encodeStringUtf8(topicName);
/*     */     
/* 305 */     int variableHeaderBufferSize = 2 + topicNameBytes.length + (mqttFixedHeader.qosLevel().value() > 0 ? 2 : 0);
/*     */     
/* 307 */     int payloadBufferSize = payload.readableBytes();
/* 308 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 309 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/*     */     
/* 311 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 312 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 313 */     writeVariableLengthInt(buf, variablePartSize);
/* 314 */     buf.writeShort(topicNameBytes.length);
/* 315 */     buf.writeBytes(topicNameBytes);
/* 316 */     if (mqttFixedHeader.qosLevel().value() > 0) {
/* 317 */       buf.writeShort(variableHeader.messageId());
/*     */     }
/* 319 */     buf.writeBytes(payload);
/*     */     
/* 321 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ByteBuf encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(ByteBufAllocator byteBufAllocator, MqttMessage message)
/*     */   {
/* 327 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 328 */     MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader)message.variableHeader();
/* 329 */     int msgId = variableHeader.messageId();
/*     */     
/* 331 */     int variableHeaderBufferSize = 2;
/* 332 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variableHeaderBufferSize);
/* 333 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variableHeaderBufferSize);
/* 334 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 335 */     writeVariableLengthInt(buf, variableHeaderBufferSize);
/* 336 */     buf.writeShort(msgId);
/*     */     
/* 338 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ByteBuf encodeMessageWithOnlySingleByteFixedHeader(ByteBufAllocator byteBufAllocator, MqttMessage message)
/*     */   {
/* 344 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 345 */     ByteBuf buf = byteBufAllocator.buffer(2);
/* 346 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 347 */     buf.writeByte(0);
/*     */     
/* 349 */     return buf;
/*     */   }
/*     */   
/*     */   private static int getFixedHeaderByte1(MqttFixedHeader header) {
/* 353 */     int ret = 0;
/* 354 */     ret |= header.messageType().value() << 4;
/* 355 */     if (header.isDup()) {
/* 356 */       ret |= 0x8;
/*     */     }
/* 358 */     ret |= header.qosLevel().value() << 1;
/* 359 */     if (header.isRetain()) {
/* 360 */       ret |= 0x1;
/*     */     }
/* 362 */     return ret;
/*     */   }
/*     */   
/*     */   private static void writeVariableLengthInt(ByteBuf buf, int num) {
/*     */     do {
/* 367 */       int digit = num % 128;
/* 368 */       num /= 128;
/* 369 */       if (num > 0) {
/* 370 */         digit |= 0x80;
/*     */       }
/* 372 */       buf.writeByte(digit);
/* 373 */     } while (num > 0);
/*     */   }
/*     */   
/*     */   private static int getVariableLengthInt(int num) {
/* 377 */     int count = 0;
/*     */     do {
/* 379 */       num /= 128;
/* 380 */       count++;
/* 381 */     } while (num > 0);
/* 382 */     return count;
/*     */   }
/*     */   
/*     */   private static byte[] encodeStringUtf8(String s) {
/* 386 */     return s.getBytes(CharsetUtil.UTF_8);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */