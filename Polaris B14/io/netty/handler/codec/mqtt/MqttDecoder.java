/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ public class MqttDecoder
/*     */   extends ReplayingDecoder<DecoderState>
/*     */ {
/*     */   private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;
/*     */   private MqttFixedHeader mqttFixedHeader;
/*     */   private Object variableHeader;
/*     */   private Object payload;
/*     */   private int bytesRemainingInVariablePart;
/*     */   private final int maxBytesInMessage;
/*     */   
/*     */   static enum DecoderState
/*     */   {
/*  46 */     READ_FIXED_HEADER, 
/*  47 */     READ_VARIABLE_HEADER, 
/*  48 */     READ_PAYLOAD, 
/*  49 */     BAD_MESSAGE;
/*     */     
/*     */ 
/*     */ 
/*     */     private DecoderState() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MqttDecoder()
/*     */   {
/*  60 */     this(8092);
/*     */   }
/*     */   
/*     */   public MqttDecoder(int maxBytesInMessage) {
/*  64 */     super(DecoderState.READ_FIXED_HEADER);
/*  65 */     this.maxBytesInMessage = maxBytesInMessage;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
/*     */   {
/*  70 */     switch ((DecoderState)state()) {
/*     */     case READ_FIXED_HEADER: 
/*  72 */       this.mqttFixedHeader = decodeFixedHeader(buffer);
/*  73 */       this.bytesRemainingInVariablePart = this.mqttFixedHeader.remainingLength();
/*  74 */       checkpoint(DecoderState.READ_VARIABLE_HEADER);
/*     */     case READ_VARIABLE_HEADER: 
/*     */       try
/*     */       {
/*  78 */         if (this.bytesRemainingInVariablePart > this.maxBytesInMessage) {
/*  79 */           throw new DecoderException("too large message: " + this.bytesRemainingInVariablePart + " bytes");
/*     */         }
/*  81 */         Result<?> decodedVariableHeader = decodeVariableHeader(buffer, this.mqttFixedHeader);
/*  82 */         this.variableHeader = decodedVariableHeader.value;
/*  83 */         this.bytesRemainingInVariablePart -= decodedVariableHeader.numberOfBytesConsumed;
/*  84 */         checkpoint(DecoderState.READ_PAYLOAD);
/*     */       }
/*     */       catch (Exception cause) {
/*  87 */         out.add(invalidMessage(cause));
/*  88 */         return;
/*     */       }
/*     */     case READ_PAYLOAD: 
/*     */       try {
/*  92 */         Result<?> decodedPayload = decodePayload(buffer, this.mqttFixedHeader.messageType(), this.bytesRemainingInVariablePart, this.variableHeader);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */         this.payload = decodedPayload.value;
/*  99 */         this.bytesRemainingInVariablePart -= decodedPayload.numberOfBytesConsumed;
/* 100 */         if (this.bytesRemainingInVariablePart != 0) {
/* 101 */           throw new DecoderException("non-zero remaining payload bytes: " + this.bytesRemainingInVariablePart + " (" + this.mqttFixedHeader.messageType() + ')');
/*     */         }
/*     */         
/*     */ 
/* 105 */         checkpoint(DecoderState.READ_FIXED_HEADER);
/* 106 */         MqttMessage message = MqttMessageFactory.newMessage(this.mqttFixedHeader, this.variableHeader, this.payload);
/* 107 */         this.mqttFixedHeader = null;
/* 108 */         this.variableHeader = null;
/* 109 */         this.payload = null;
/* 110 */         out.add(message);
/*     */       }
/*     */       catch (Exception cause) {
/* 113 */         out.add(invalidMessage(cause));
/* 114 */         return;
/*     */       }
/*     */     
/*     */ 
/*     */     case BAD_MESSAGE: 
/* 119 */       buffer.skipBytes(actualReadableBytes());
/* 120 */       break;
/*     */     }
/*     */     
/*     */     
/* 124 */     throw new Error();
/*     */   }
/*     */   
/*     */   private MqttMessage invalidMessage(Throwable cause)
/*     */   {
/* 129 */     checkpoint(DecoderState.BAD_MESSAGE);
/* 130 */     return MqttMessageFactory.newInvalidMessage(cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static MqttFixedHeader decodeFixedHeader(ByteBuf buffer)
/*     */   {
/* 140 */     short b1 = buffer.readUnsignedByte();
/*     */     
/* 142 */     MqttMessageType messageType = MqttMessageType.valueOf(b1 >> 4);
/* 143 */     boolean dupFlag = (b1 & 0x8) == 8;
/* 144 */     int qosLevel = (b1 & 0x6) >> 1;
/* 145 */     boolean retain = (b1 & 0x1) != 0;
/*     */     
/* 147 */     int remainingLength = 0;
/* 148 */     int multiplier = 1;
/*     */     
/* 150 */     int loops = 0;
/*     */     short digit;
/* 152 */     do { digit = buffer.readUnsignedByte();
/* 153 */       remainingLength += (digit & 0x7F) * multiplier;
/* 154 */       multiplier *= 128;
/* 155 */       loops++;
/* 156 */     } while (((digit & 0x80) != 0) && (loops < 4));
/*     */     
/*     */ 
/* 159 */     if ((loops == 4) && ((digit & 0x80) != 0)) {
/* 160 */       throw new DecoderException("remaining length exceeds 4 digits (" + messageType + ')');
/*     */     }
/* 162 */     MqttFixedHeader decodedFixedHeader = new MqttFixedHeader(messageType, dupFlag, MqttQoS.valueOf(qosLevel), retain, remainingLength);
/*     */     
/* 164 */     return MqttCodecUtil.validateFixedHeader(MqttCodecUtil.resetUnusedFields(decodedFixedHeader));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Result<?> decodeVariableHeader(ByteBuf buffer, MqttFixedHeader mqttFixedHeader)
/*     */   {
/* 174 */     switch (mqttFixedHeader.messageType()) {
/*     */     case CONNECT: 
/* 176 */       return decodeConnectionVariableHeader(buffer);
/*     */     
/*     */     case CONNACK: 
/* 179 */       return decodeConnAckVariableHeader(buffer);
/*     */     
/*     */     case SUBSCRIBE: 
/*     */     case UNSUBSCRIBE: 
/*     */     case SUBACK: 
/*     */     case UNSUBACK: 
/*     */     case PUBACK: 
/*     */     case PUBREC: 
/*     */     case PUBCOMP: 
/*     */     case PUBREL: 
/* 189 */       return decodeMessageIdVariableHeader(buffer);
/*     */     
/*     */     case PUBLISH: 
/* 192 */       return decodePublishVariableHeader(buffer, mqttFixedHeader);
/*     */     
/*     */ 
/*     */     case PINGREQ: 
/*     */     case PINGRESP: 
/*     */     case DISCONNECT: 
/* 198 */       return new Result(null, 0);
/*     */     }
/* 200 */     return new Result(null, 0);
/*     */   }
/*     */   
/*     */   private static Result<MqttConnectVariableHeader> decodeConnectionVariableHeader(ByteBuf buffer) {
/* 204 */     Result<String> protoString = decodeString(buffer);
/* 205 */     int numberOfBytesConsumed = protoString.numberOfBytesConsumed;
/*     */     
/* 207 */     byte protocolLevel = buffer.readByte();
/* 208 */     numberOfBytesConsumed++;
/*     */     
/* 210 */     MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel((String)protoString.value, protocolLevel);
/*     */     
/* 212 */     int b1 = buffer.readUnsignedByte();
/* 213 */     numberOfBytesConsumed++;
/*     */     
/* 215 */     Result<Integer> keepAlive = decodeMsbLsb(buffer);
/* 216 */     numberOfBytesConsumed += keepAlive.numberOfBytesConsumed;
/*     */     
/* 218 */     boolean hasUserName = (b1 & 0x80) == 128;
/* 219 */     boolean hasPassword = (b1 & 0x40) == 64;
/* 220 */     boolean willRetain = (b1 & 0x20) == 32;
/* 221 */     int willQos = (b1 & 0x18) >> 3;
/* 222 */     boolean willFlag = (b1 & 0x4) == 4;
/* 223 */     boolean cleanSession = (b1 & 0x2) == 2;
/*     */     
/* 225 */     MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(mqttVersion.protocolName(), mqttVersion.protocolLevel(), hasUserName, hasPassword, willRetain, willQos, willFlag, cleanSession, ((Integer)keepAlive.value).intValue());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */     return new Result(mqttConnectVariableHeader, numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */   private static Result<MqttConnAckVariableHeader> decodeConnAckVariableHeader(ByteBuf buffer) {
/* 239 */     buffer.readUnsignedByte();
/* 240 */     byte returnCode = buffer.readByte();
/* 241 */     int numberOfBytesConsumed = 2;
/* 242 */     MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.valueOf(returnCode));
/*     */     
/* 244 */     return new Result(mqttConnAckVariableHeader, 2);
/*     */   }
/*     */   
/*     */   private static Result<MqttMessageIdVariableHeader> decodeMessageIdVariableHeader(ByteBuf buffer) {
/* 248 */     Result<Integer> messageId = decodeMessageId(buffer);
/* 249 */     return new Result(MqttMessageIdVariableHeader.from(((Integer)messageId.value).intValue()), messageId.numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Result<MqttPublishVariableHeader> decodePublishVariableHeader(ByteBuf buffer, MqttFixedHeader mqttFixedHeader)
/*     */   {
/* 257 */     Result<String> decodedTopic = decodeString(buffer);
/* 258 */     if (!MqttCodecUtil.isValidPublishTopicName((String)decodedTopic.value)) {
/* 259 */       throw new DecoderException("invalid publish topic name: " + (String)decodedTopic.value + " (contains wildcards)");
/*     */     }
/* 261 */     int numberOfBytesConsumed = decodedTopic.numberOfBytesConsumed;
/*     */     
/* 263 */     int messageId = -1;
/* 264 */     if (mqttFixedHeader.qosLevel().value() > 0) {
/* 265 */       Result<Integer> decodedMessageId = decodeMessageId(buffer);
/* 266 */       messageId = ((Integer)decodedMessageId.value).intValue();
/* 267 */       numberOfBytesConsumed += decodedMessageId.numberOfBytesConsumed;
/*     */     }
/* 269 */     MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader((String)decodedTopic.value, messageId);
/*     */     
/* 271 */     return new Result(mqttPublishVariableHeader, numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */   private static Result<Integer> decodeMessageId(ByteBuf buffer) {
/* 275 */     Result<Integer> messageId = decodeMsbLsb(buffer);
/* 276 */     if (!MqttCodecUtil.isValidMessageId(((Integer)messageId.value).intValue())) {
/* 277 */       throw new DecoderException("invalid messageId: " + messageId.value);
/*     */     }
/* 279 */     return messageId;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Result<?> decodePayload(ByteBuf buffer, MqttMessageType messageType, int bytesRemainingInVariablePart, Object variableHeader)
/*     */   {
/* 296 */     switch (messageType) {
/*     */     case CONNECT: 
/* 298 */       return decodeConnectionPayload(buffer, (MqttConnectVariableHeader)variableHeader);
/*     */     
/*     */     case SUBSCRIBE: 
/* 301 */       return decodeSubscribePayload(buffer, bytesRemainingInVariablePart);
/*     */     
/*     */     case SUBACK: 
/* 304 */       return decodeSubackPayload(buffer, bytesRemainingInVariablePart);
/*     */     
/*     */     case UNSUBSCRIBE: 
/* 307 */       return decodeUnsubscribePayload(buffer, bytesRemainingInVariablePart);
/*     */     
/*     */     case PUBLISH: 
/* 310 */       return decodePublishPayload(buffer, bytesRemainingInVariablePart);
/*     */     }
/*     */     
/*     */     
/* 314 */     return new Result(null, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Result<MqttConnectPayload> decodeConnectionPayload(ByteBuf buffer, MqttConnectVariableHeader mqttConnectVariableHeader)
/*     */   {
/* 321 */     Result<String> decodedClientId = decodeString(buffer);
/* 322 */     String decodedClientIdValue = (String)decodedClientId.value;
/* 323 */     MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(mqttConnectVariableHeader.name(), (byte)mqttConnectVariableHeader.version());
/*     */     
/* 325 */     if (!MqttCodecUtil.isValidClientId(mqttVersion, decodedClientIdValue)) {
/* 326 */       throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + decodedClientIdValue);
/*     */     }
/* 328 */     int numberOfBytesConsumed = decodedClientId.numberOfBytesConsumed;
/*     */     
/* 330 */     Result<String> decodedWillTopic = null;
/* 331 */     Result<String> decodedWillMessage = null;
/* 332 */     if (mqttConnectVariableHeader.isWillFlag()) {
/* 333 */       decodedWillTopic = decodeString(buffer, 0, 32767);
/* 334 */       numberOfBytesConsumed += decodedWillTopic.numberOfBytesConsumed;
/* 335 */       decodedWillMessage = decodeAsciiString(buffer);
/* 336 */       numberOfBytesConsumed += decodedWillMessage.numberOfBytesConsumed;
/*     */     }
/* 338 */     Result<String> decodedUserName = null;
/* 339 */     Result<String> decodedPassword = null;
/* 340 */     if (mqttConnectVariableHeader.hasUserName()) {
/* 341 */       decodedUserName = decodeString(buffer);
/* 342 */       numberOfBytesConsumed += decodedUserName.numberOfBytesConsumed;
/*     */     }
/* 344 */     if (mqttConnectVariableHeader.hasPassword()) {
/* 345 */       decodedPassword = decodeString(buffer);
/* 346 */       numberOfBytesConsumed += decodedPassword.numberOfBytesConsumed;
/*     */     }
/*     */     
/* 349 */     MqttConnectPayload mqttConnectPayload = new MqttConnectPayload((String)decodedClientId.value, decodedWillTopic != null ? (String)decodedWillTopic.value : null, decodedWillMessage != null ? (String)decodedWillMessage.value : null, decodedUserName != null ? (String)decodedUserName.value : null, decodedPassword != null ? (String)decodedPassword.value : null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 356 */     return new Result(mqttConnectPayload, numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Result<MqttSubscribePayload> decodeSubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart)
/*     */   {
/* 362 */     List<MqttTopicSubscription> subscribeTopics = new ArrayList();
/* 363 */     int numberOfBytesConsumed = 0;
/* 364 */     while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
/* 365 */       Result<String> decodedTopicName = decodeString(buffer);
/* 366 */       numberOfBytesConsumed += decodedTopicName.numberOfBytesConsumed;
/* 367 */       int qos = buffer.readUnsignedByte() & 0x3;
/* 368 */       numberOfBytesConsumed++;
/* 369 */       subscribeTopics.add(new MqttTopicSubscription((String)decodedTopicName.value, MqttQoS.valueOf(qos)));
/*     */     }
/* 371 */     return new Result(new MqttSubscribePayload(subscribeTopics), numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Result<MqttSubAckPayload> decodeSubackPayload(ByteBuf buffer, int bytesRemainingInVariablePart)
/*     */   {
/* 377 */     List<Integer> grantedQos = new ArrayList();
/* 378 */     int numberOfBytesConsumed = 0;
/* 379 */     while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
/* 380 */       int qos = buffer.readUnsignedByte() & 0x3;
/* 381 */       numberOfBytesConsumed++;
/* 382 */       grantedQos.add(Integer.valueOf(qos));
/*     */     }
/* 384 */     return new Result(new MqttSubAckPayload(grantedQos), numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Result<MqttUnsubscribePayload> decodeUnsubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart)
/*     */   {
/* 390 */     List<String> unsubscribeTopics = new ArrayList();
/* 391 */     int numberOfBytesConsumed = 0;
/* 392 */     while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
/* 393 */       Result<String> decodedTopicName = decodeString(buffer);
/* 394 */       numberOfBytesConsumed += decodedTopicName.numberOfBytesConsumed;
/* 395 */       unsubscribeTopics.add(decodedTopicName.value);
/*     */     }
/* 397 */     return new Result(new MqttUnsubscribePayload(unsubscribeTopics), numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Result<ByteBuf> decodePublishPayload(ByteBuf buffer, int bytesRemainingInVariablePart)
/*     */   {
/* 403 */     ByteBuf b = buffer.readSlice(bytesRemainingInVariablePart).retain();
/* 404 */     return new Result(b, bytesRemainingInVariablePart);
/*     */   }
/*     */   
/*     */   private static Result<String> decodeString(ByteBuf buffer) {
/* 408 */     return decodeString(buffer, 0, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   private static Result<String> decodeAsciiString(ByteBuf buffer) {
/* 412 */     Result<String> result = decodeString(buffer, 0, Integer.MAX_VALUE);
/* 413 */     String s = (String)result.value;
/* 414 */     for (int i = 0; i < s.length(); i++) {
/* 415 */       if (s.charAt(i) > '') {
/* 416 */         return new Result(null, result.numberOfBytesConsumed);
/*     */       }
/*     */     }
/* 419 */     return new Result(s, result.numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */   private static Result<String> decodeString(ByteBuf buffer, int minBytes, int maxBytes) {
/* 423 */     Result<Integer> decodedSize = decodeMsbLsb(buffer);
/* 424 */     int size = ((Integer)decodedSize.value).intValue();
/* 425 */     int numberOfBytesConsumed = decodedSize.numberOfBytesConsumed;
/* 426 */     if ((size < minBytes) || (size > maxBytes)) {
/* 427 */       buffer.skipBytes(size);
/* 428 */       numberOfBytesConsumed += size;
/* 429 */       return new Result(null, numberOfBytesConsumed);
/*     */     }
/* 431 */     ByteBuf buf = buffer.readBytes(size);
/* 432 */     numberOfBytesConsumed += size;
/* 433 */     return new Result(buf.toString(CharsetUtil.UTF_8), numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */   private static Result<Integer> decodeMsbLsb(ByteBuf buffer) {
/* 437 */     return decodeMsbLsb(buffer, 0, 65535);
/*     */   }
/*     */   
/*     */   private static Result<Integer> decodeMsbLsb(ByteBuf buffer, int min, int max) {
/* 441 */     short msbSize = buffer.readUnsignedByte();
/* 442 */     short lsbSize = buffer.readUnsignedByte();
/* 443 */     int numberOfBytesConsumed = 2;
/* 444 */     int result = msbSize << 8 | lsbSize;
/* 445 */     if ((result < min) || (result > max)) {
/* 446 */       result = -1;
/*     */     }
/* 448 */     return new Result(Integer.valueOf(result), 2);
/*     */   }
/*     */   
/*     */   private static final class Result<T>
/*     */   {
/*     */     private final T value;
/*     */     private final int numberOfBytesConsumed;
/*     */     
/*     */     Result(T value, int numberOfBytesConsumed) {
/* 457 */       this.value = value;
/* 458 */       this.numberOfBytesConsumed = numberOfBytesConsumed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */