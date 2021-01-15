/*     */ package ch.qos.logback.core.joran.spi;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.action.Action;
/*     */ import ch.qos.logback.core.joran.action.ImplicitAction;
/*     */ import ch.qos.logback.core.joran.event.BodyEvent;
/*     */ import ch.qos.logback.core.joran.event.EndEvent;
/*     */ import ch.qos.logback.core.joran.event.StartEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
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
/*     */ 
/*     */ public class Interpreter
/*     */ {
/*  68 */   private static List<Action> EMPTY_LIST = new Vector(0);
/*     */   
/*     */ 
/*     */   private final RuleStore ruleStore;
/*     */   
/*     */ 
/*     */   private final InterpretationContext interpretationContext;
/*     */   
/*     */ 
/*     */   private final ArrayList<ImplicitAction> implicitActions;
/*     */   
/*     */ 
/*     */   private final CAI_WithLocatorSupport cai;
/*     */   
/*     */ 
/*     */   private ElementPath elementPath;
/*     */   
/*     */ 
/*     */   Locator locator;
/*     */   
/*     */   EventPlayer eventPlayer;
/*     */   
/*     */   Stack<List<Action>> actionListStack;
/*     */   
/*  92 */   ElementPath skip = null;
/*     */   
/*     */   public Interpreter(Context context, RuleStore rs, ElementPath initialElementPath) {
/*  95 */     this.cai = new CAI_WithLocatorSupport(context, this);
/*  96 */     this.ruleStore = rs;
/*  97 */     this.interpretationContext = new InterpretationContext(context, this);
/*  98 */     this.implicitActions = new ArrayList(3);
/*  99 */     this.elementPath = initialElementPath;
/* 100 */     this.actionListStack = new Stack();
/* 101 */     this.eventPlayer = new EventPlayer(this);
/*     */   }
/*     */   
/*     */   public EventPlayer getEventPlayer() {
/* 105 */     return this.eventPlayer;
/*     */   }
/*     */   
/*     */   public void setInterpretationContextPropertiesMap(Map<String, String> propertiesMap)
/*     */   {
/* 110 */     this.interpretationContext.setPropertiesMap(propertiesMap);
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public InterpretationContext getExecutionContext() {
/* 117 */     return getInterpretationContext();
/*     */   }
/*     */   
/*     */   public InterpretationContext getInterpretationContext() {
/* 121 */     return this.interpretationContext;
/*     */   }
/*     */   
/*     */   public void startDocument() {}
/*     */   
/*     */   public void startElement(StartEvent se)
/*     */   {
/* 128 */     setDocumentLocator(se.getLocator());
/* 129 */     startElement(se.namespaceURI, se.localName, se.qName, se.attributes);
/*     */   }
/*     */   
/*     */ 
/*     */   private void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */   {
/* 135 */     String tagName = getTagName(localName, qName);
/* 136 */     this.elementPath.push(tagName);
/*     */     
/* 138 */     if (this.skip != null)
/*     */     {
/* 140 */       pushEmptyActionList();
/* 141 */       return;
/*     */     }
/*     */     
/* 144 */     List<Action> applicableActionList = getApplicableActionList(this.elementPath, atts);
/* 145 */     if (applicableActionList != null) {
/* 146 */       this.actionListStack.add(applicableActionList);
/* 147 */       callBeginAction(applicableActionList, tagName, atts);
/*     */     }
/*     */     else {
/* 150 */       pushEmptyActionList();
/* 151 */       String errMsg = "no applicable action for [" + tagName + "], current ElementPath  is [" + this.elementPath + "]";
/*     */       
/* 153 */       this.cai.addError(errMsg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void pushEmptyActionList()
/*     */   {
/* 161 */     this.actionListStack.add(EMPTY_LIST);
/*     */   }
/*     */   
/*     */   public void characters(BodyEvent be)
/*     */   {
/* 166 */     setDocumentLocator(be.locator);
/*     */     
/* 168 */     String body = be.getText();
/* 169 */     List<Action> applicableActionList = (List)this.actionListStack.peek();
/*     */     
/* 171 */     if (body != null) {
/* 172 */       body = body.trim();
/* 173 */       if (body.length() > 0)
/*     */       {
/* 175 */         callBodyAction(applicableActionList, body);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void endElement(EndEvent endEvent) {
/* 181 */     setDocumentLocator(endEvent.locator);
/* 182 */     endElement(endEvent.namespaceURI, endEvent.localName, endEvent.qName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void endElement(String namespaceURI, String localName, String qName)
/*     */   {
/* 189 */     List<Action> applicableActionList = (List)this.actionListStack.pop();
/*     */     
/* 191 */     if (this.skip != null) {
/* 192 */       if (this.skip.equals(this.elementPath)) {
/* 193 */         this.skip = null;
/*     */       }
/* 195 */     } else if (applicableActionList != EMPTY_LIST) {
/* 196 */       callEndAction(applicableActionList, getTagName(localName, qName));
/*     */     }
/*     */     
/*     */ 
/* 200 */     this.elementPath.pop();
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 204 */     return this.locator;
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator l) {
/* 208 */     this.locator = l;
/*     */   }
/*     */   
/*     */   String getTagName(String localName, String qName) {
/* 212 */     String tagName = localName;
/*     */     
/* 214 */     if ((tagName == null) || (tagName.length() < 1)) {
/* 215 */       tagName = qName;
/*     */     }
/*     */     
/* 218 */     return tagName;
/*     */   }
/*     */   
/*     */   public void addImplicitAction(ImplicitAction ia) {
/* 222 */     this.implicitActions.add(ia);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   List<Action> lookupImplicitAction(ElementPath elementPath, Attributes attributes, InterpretationContext ec)
/*     */   {
/* 232 */     int len = this.implicitActions.size();
/*     */     
/* 234 */     for (int i = 0; i < len; i++) {
/* 235 */       ImplicitAction ia = (ImplicitAction)this.implicitActions.get(i);
/*     */       
/* 237 */       if (ia.isApplicable(elementPath, attributes, ec)) {
/* 238 */         List<Action> actionList = new ArrayList(1);
/* 239 */         actionList.add(ia);
/*     */         
/* 241 */         return actionList;
/*     */       }
/*     */     }
/*     */     
/* 245 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   List<Action> getApplicableActionList(ElementPath elementPath, Attributes attributes)
/*     */   {
/* 252 */     List<Action> applicableActionList = this.ruleStore.matchActions(elementPath);
/*     */     
/*     */ 
/* 255 */     if (applicableActionList == null) {
/* 256 */       applicableActionList = lookupImplicitAction(elementPath, attributes, this.interpretationContext);
/*     */     }
/*     */     
/*     */ 
/* 260 */     return applicableActionList;
/*     */   }
/*     */   
/*     */   void callBeginAction(List<Action> applicableActionList, String tagName, Attributes atts)
/*     */   {
/* 265 */     if (applicableActionList == null) {
/* 266 */       return;
/*     */     }
/*     */     
/* 269 */     Iterator<Action> i = applicableActionList.iterator();
/* 270 */     while (i.hasNext()) {
/* 271 */       Action action = (Action)i.next();
/*     */       
/*     */       try
/*     */       {
/* 275 */         action.begin(this.interpretationContext, tagName, atts);
/*     */       } catch (ActionException e) {
/* 277 */         this.skip = this.elementPath.duplicate();
/* 278 */         this.cai.addError("ActionException in Action for tag [" + tagName + "]", e);
/*     */       } catch (RuntimeException e) {
/* 280 */         this.skip = this.elementPath.duplicate();
/* 281 */         this.cai.addError("RuntimeException in Action for tag [" + tagName + "]", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void callBodyAction(List<Action> applicableActionList, String body) {
/* 287 */     if (applicableActionList == null) {
/* 288 */       return;
/*     */     }
/* 290 */     Iterator<Action> i = applicableActionList.iterator();
/*     */     
/* 292 */     while (i.hasNext()) {
/* 293 */       Action action = (Action)i.next();
/*     */       try {
/* 295 */         action.body(this.interpretationContext, body);
/*     */       } catch (ActionException ae) {
/* 297 */         this.cai.addError("Exception in end() methd for action [" + action + "]", ae);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void callEndAction(List<Action> applicableActionList, String tagName)
/*     */   {
/* 305 */     if (applicableActionList == null) {
/* 306 */       return;
/*     */     }
/*     */     
/*     */ 
/* 310 */     Iterator<Action> i = applicableActionList.iterator();
/*     */     
/* 312 */     while (i.hasNext()) {
/* 313 */       Action action = (Action)i.next();
/*     */       
/*     */       try
/*     */       {
/* 317 */         action.end(this.interpretationContext, tagName);
/*     */       }
/*     */       catch (ActionException ae)
/*     */       {
/* 321 */         this.cai.addError("ActionException in Action for tag [" + tagName + "]", ae);
/*     */       }
/*     */       catch (RuntimeException e) {
/* 324 */         this.cai.addError("RuntimeException in Action for tag [" + tagName + "]", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public RuleStore getRuleStore() {
/* 330 */     return this.ruleStore;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\Interpreter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */