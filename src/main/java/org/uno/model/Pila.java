package org.uno.model;

import java.util.ArrayList;
import java.util.List;

public class Pila {

  private List<Carta> pila;

  public Pila() {
    this.pila = new ArrayList<>();
  }

  public void jugarCarta(Carta carta){
    pila.add(carta);
  }

  public Carta ultimaCarta(){
    return pila.getLast();
  }
}
