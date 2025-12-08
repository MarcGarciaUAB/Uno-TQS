package org.uno.model;

import java.util.ArrayList;
import java.util.List;

public class Pila {

  private List<Carta> pila;

  // Invariante: pila != null
  private void Invariante() {
    assert pila != null : "Invariante: pila no puede ser null";
  }

  public Pila() {
    this.pila = new ArrayList<>();
    Invariante();
  }

  public List<Carta> getPila() {
    return new ArrayList<>(pila);
  }

  public void jugarCarta(Carta carta) {
    assert carta != null : "Precondición: carta no null";
    pila.add(carta);
    Invariante();
    assert carta.equals(ultimaCarta()) : "Postcondición: carta es última en pila";
  }

  public void vaciar() {
    pila.clear();
    Invariante();
    assert pila.isEmpty() : "Postcondición: pila vacía";
  }

  public Carta ultimaCarta() {
    if (pila.isEmpty()) return null;
    return pila.get(pila.size() - 1);
  }
}
