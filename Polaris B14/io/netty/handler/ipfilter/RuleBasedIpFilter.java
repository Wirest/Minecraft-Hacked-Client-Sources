/*    */ package io.netty.handler.ipfilter;
/*    */ 
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import java.net.InetSocketAddress;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ChannelHandler.Sharable
/*    */ public class RuleBasedIpFilter
/*    */   extends AbstractRemoteAddressFilter<InetSocketAddress>
/*    */ {
/*    */   private final IpFilterRule[] rules;
/*    */   
/*    */   public RuleBasedIpFilter(IpFilterRule... rules)
/*    */   {
/* 39 */     if (rules == null) {
/* 40 */       throw new NullPointerException("rules");
/*    */     }
/*    */     
/* 43 */     this.rules = rules;
/*    */   }
/*    */   
/*    */   protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception
/*    */   {
/* 48 */     for (IpFilterRule rule : this.rules) {
/* 49 */       if (rule == null) {
/*    */         break;
/*    */       }
/*    */       
/* 53 */       if (rule.matches(remoteAddress)) {
/* 54 */         return rule.ruleType() == IpFilterRuleType.ACCEPT;
/*    */       }
/*    */     }
/*    */     
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ipfilter\RuleBasedIpFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */