/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
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
/*     */ public abstract class DnsMessage
/*     */   extends AbstractReferenceCounted
/*     */ {
/*     */   private List<DnsQuestion> questions;
/*     */   private List<DnsResource> answers;
/*     */   private List<DnsResource> authority;
/*     */   private List<DnsResource> additional;
/*     */   private final DnsHeader header;
/*     */   
/*     */   DnsMessage(int id)
/*     */   {
/*  40 */     this.header = newHeader(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DnsHeader header()
/*     */   {
/*  47 */     return this.header;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<DnsQuestion> questions()
/*     */   {
/*  54 */     if (this.questions == null) {
/*  55 */       return Collections.emptyList();
/*     */     }
/*  57 */     return Collections.unmodifiableList(this.questions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<DnsResource> answers()
/*     */   {
/*  64 */     if (this.answers == null) {
/*  65 */       return Collections.emptyList();
/*     */     }
/*  67 */     return Collections.unmodifiableList(this.answers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<DnsResource> authorityResources()
/*     */   {
/*  74 */     if (this.authority == null) {
/*  75 */       return Collections.emptyList();
/*     */     }
/*  77 */     return Collections.unmodifiableList(this.authority);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<DnsResource> additionalResources()
/*     */   {
/*  84 */     if (this.additional == null) {
/*  85 */       return Collections.emptyList();
/*     */     }
/*  87 */     return Collections.unmodifiableList(this.additional);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsMessage addAnswer(DnsResource answer)
/*     */   {
/*  98 */     if (this.answers == null) {
/*  99 */       this.answers = new LinkedList();
/*     */     }
/* 101 */     this.answers.add(answer);
/* 102 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsMessage addQuestion(DnsQuestion question)
/*     */   {
/* 113 */     if (this.questions == null) {
/* 114 */       this.questions = new LinkedList();
/*     */     }
/* 116 */     this.questions.add(question);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsMessage addAuthorityResource(DnsResource resource)
/*     */   {
/* 128 */     if (this.authority == null) {
/* 129 */       this.authority = new LinkedList();
/*     */     }
/* 131 */     this.authority.add(resource);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsMessage addAdditionalResource(DnsResource resource)
/*     */   {
/* 143 */     if (this.additional == null) {
/* 144 */       this.additional = new LinkedList();
/*     */     }
/* 146 */     this.additional.add(resource);
/* 147 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void deallocate() {}
/*     */   
/*     */ 
/*     */   public boolean release()
/*     */   {
/* 157 */     release(questions());
/* 158 */     release(answers());
/* 159 */     release(additionalResources());
/* 160 */     release(authorityResources());
/* 161 */     return super.release();
/*     */   }
/*     */   
/*     */   private static void release(List<?> resources) {
/* 165 */     for (Object resource : resources) {
/* 166 */       ReferenceCountUtil.release(resource);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 172 */     release(questions(), decrement);
/* 173 */     release(answers(), decrement);
/* 174 */     release(additionalResources(), decrement);
/* 175 */     release(authorityResources(), decrement);
/* 176 */     return super.release(decrement);
/*     */   }
/*     */   
/*     */   private static void release(List<?> resources, int decrement) {
/* 180 */     for (Object resource : resources) {
/* 181 */       ReferenceCountUtil.release(resource, decrement);
/*     */     }
/*     */   }
/*     */   
/*     */   public DnsMessage touch(Object hint)
/*     */   {
/* 187 */     touch(questions(), hint);
/* 188 */     touch(answers(), hint);
/* 189 */     touch(additionalResources(), hint);
/* 190 */     touch(authorityResources(), hint);
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   private static void touch(List<?> resources, Object hint) {
/* 195 */     for (Object resource : resources) {
/* 196 */       ReferenceCountUtil.touch(resource, hint);
/*     */     }
/*     */   }
/*     */   
/*     */   public DnsMessage retain()
/*     */   {
/* 202 */     retain(questions());
/* 203 */     retain(answers());
/* 204 */     retain(additionalResources());
/* 205 */     retain(authorityResources());
/* 206 */     super.retain();
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   private static void retain(List<?> resources) {
/* 211 */     for (Object resource : resources) {
/* 212 */       ReferenceCountUtil.retain(resource);
/*     */     }
/*     */   }
/*     */   
/*     */   public DnsMessage retain(int increment)
/*     */   {
/* 218 */     retain(questions(), increment);
/* 219 */     retain(answers(), increment);
/* 220 */     retain(additionalResources(), increment);
/* 221 */     retain(authorityResources(), increment);
/* 222 */     super.retain(increment);
/* 223 */     return this;
/*     */   }
/*     */   
/*     */   private static void retain(List<?> resources, int increment) {
/* 227 */     for (Object resource : resources) {
/* 228 */       ReferenceCountUtil.retain(resource, increment);
/*     */     }
/*     */   }
/*     */   
/*     */   public DnsMessage touch()
/*     */   {
/* 234 */     super.touch();
/* 235 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract DnsHeader newHeader(int paramInt);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */