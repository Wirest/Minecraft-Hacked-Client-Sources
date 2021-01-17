package me.slowly.client.ui.scriptmenu.elements;

public class UIElement {
   public int ELEMENT_ID;
   public static int ELEMENT_COUNT;

   public UIElement() {
      ++ELEMENT_COUNT;
      this.ELEMENT_ID = ELEMENT_COUNT;
   }
}
