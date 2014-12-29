package net.onamap.server.guice;
public class GuicyInterfaceImpl implements GuicyInterface {
 
   public String get() {
      return GuicyInterfaceImpl.class.getName();
   }
}