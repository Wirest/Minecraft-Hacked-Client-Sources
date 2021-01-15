/*     */ package ch.qos.logback.core.joran.spi;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.action.Action;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ public class SimpleRuleStore
/*     */   extends ContextAwareBase
/*     */   implements RuleStore
/*     */ {
/*  34 */   static String KLEENE_STAR = "*";
/*     */   
/*     */ 
/*  37 */   HashMap<ElementSelector, List<Action>> rules = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public SimpleRuleStore(Context context)
/*     */   {
/*  43 */     setContext(context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRule(ElementSelector elementSelector, Action action)
/*     */   {
/*  51 */     action.setContext(this.context);
/*     */     
/*  53 */     List<Action> a4p = (List)this.rules.get(elementSelector);
/*     */     
/*  55 */     if (a4p == null) {
/*  56 */       a4p = new ArrayList();
/*  57 */       this.rules.put(elementSelector, a4p);
/*     */     }
/*     */     
/*  60 */     a4p.add(action);
/*     */   }
/*     */   
/*     */   public void addRule(ElementSelector elementSelector, String actionClassName) {
/*  64 */     Action action = null;
/*     */     try
/*     */     {
/*  67 */       action = (Action)OptionHelper.instantiateByClassName(actionClassName, Action.class, this.context);
/*     */     }
/*     */     catch (Exception e) {
/*  70 */       addError("Could not instantiate class [" + actionClassName + "]", e);
/*     */     }
/*  72 */     if (action != null) {
/*  73 */       addRule(elementSelector, action);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Action> matchActions(ElementPath elementPath)
/*     */   {
/*     */     List<Action> actionList;
/*     */     
/*     */ 
/*     */ 
/*  87 */     if ((actionList = fullPathMatch(elementPath)) != null)
/*  88 */       return actionList;
/*  89 */     if ((actionList = suffixMatch(elementPath)) != null)
/*  90 */       return actionList;
/*  91 */     if ((actionList = prefixMatch(elementPath)) != null)
/*  92 */       return actionList;
/*  93 */     if ((actionList = middleMatch(elementPath)) != null) {
/*  94 */       return actionList;
/*     */     }
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   List<Action> fullPathMatch(ElementPath elementPath)
/*     */   {
/* 101 */     for (ElementSelector selector : this.rules.keySet()) {
/* 102 */       if (selector.fullPathMatch(elementPath))
/* 103 */         return (List)this.rules.get(selector);
/*     */     }
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   List<Action> suffixMatch(ElementPath elementPath)
/*     */   {
/* 110 */     int max = 0;
/* 111 */     ElementSelector longestMatchingElementSelector = null;
/*     */     
/* 113 */     for (ElementSelector selector : this.rules.keySet()) {
/* 114 */       if (isSuffixPattern(selector)) {
/* 115 */         int r = selector.getTailMatchLength(elementPath);
/* 116 */         if (r > max) {
/* 117 */           max = r;
/* 118 */           longestMatchingElementSelector = selector;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 123 */     if (longestMatchingElementSelector != null) {
/* 124 */       return (List)this.rules.get(longestMatchingElementSelector);
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isSuffixPattern(ElementSelector p)
/*     */   {
/* 131 */     return (p.size() > 1) && (p.get(0).equals(KLEENE_STAR));
/*     */   }
/*     */   
/*     */   List<Action> prefixMatch(ElementPath elementPath) {
/* 135 */     int max = 0;
/* 136 */     ElementSelector longestMatchingElementSelector = null;
/*     */     
/* 138 */     for (ElementSelector selector : this.rules.keySet()) {
/* 139 */       String last = selector.peekLast();
/* 140 */       if (isKleeneStar(last)) {
/* 141 */         int r = selector.getPrefixMatchLength(elementPath);
/*     */         
/* 143 */         if ((r == selector.size() - 1) && (r > max)) {
/* 144 */           max = r;
/* 145 */           longestMatchingElementSelector = selector;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 150 */     if (longestMatchingElementSelector != null) {
/* 151 */       return (List)this.rules.get(longestMatchingElementSelector);
/*     */     }
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isKleeneStar(String last)
/*     */   {
/* 158 */     return KLEENE_STAR.equals(last);
/*     */   }
/*     */   
/*     */   List<Action> middleMatch(ElementPath path)
/*     */   {
/* 163 */     int max = 0;
/* 164 */     ElementSelector longestMatchingElementSelector = null;
/*     */     
/* 166 */     for (ElementSelector selector : this.rules.keySet()) {
/* 167 */       String last = selector.peekLast();
/* 168 */       String first = null;
/* 169 */       if (selector.size() > 1) {
/* 170 */         first = selector.get(0);
/*     */       }
/* 172 */       if ((isKleeneStar(last)) && (isKleeneStar(first))) {
/* 173 */         List<String> copyOfPartList = selector.getCopyOfPartList();
/* 174 */         if (copyOfPartList.size() > 2) {
/* 175 */           copyOfPartList.remove(0);
/* 176 */           copyOfPartList.remove(copyOfPartList.size() - 1);
/*     */         }
/*     */         
/* 179 */         int r = 0;
/* 180 */         ElementSelector clone = new ElementSelector(copyOfPartList);
/* 181 */         if (clone.isContainedIn(path)) {
/* 182 */           r = clone.size();
/*     */         }
/* 184 */         if (r > max) {
/* 185 */           max = r;
/* 186 */           longestMatchingElementSelector = selector;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 191 */     if (longestMatchingElementSelector != null) {
/* 192 */       return (List)this.rules.get(longestMatchingElementSelector);
/*     */     }
/* 194 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 200 */     String TAB = "  ";
/*     */     
/* 202 */     StringBuilder retValue = new StringBuilder();
/*     */     
/* 204 */     retValue.append("SimpleRuleStore ( ").append("rules = ").append(this.rules).append("  ").append(" )");
/*     */     
/*     */ 
/* 207 */     return retValue.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\SimpleRuleStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */