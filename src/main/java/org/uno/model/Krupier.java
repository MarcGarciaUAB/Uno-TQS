package org.uno.model;

import java.util.List;

public interface Krupier {
  public default List<Carta> barajar(List<Carta> baraja){
    //aquí se barajaría de manera no-determinista.
    //implementamos Mockito para evitarlo
    return baraja;
  }
}
