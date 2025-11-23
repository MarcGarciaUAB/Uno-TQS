package org.uno.model;

import java.util.List;
import java.util.ArrayList;

public class Mano {
  //es más fácil utilizar los métodos de List y ArrayList
  private List<Carta> mano;

  public Mano() {
    this.mano = new ArrayList<>();
  }

  public List<Carta> getMano() {
    return mano;
  }
  public int getNumeroCartas() {
    return mano.size();
  }

  public void añadirCarta(Carta carta) {
    mano.add(carta);
  }
  public void eliminarCarta(Carta carta) {
    mano.remove(carta);
  }

  public boolean tieneCartaJugable(Carta cartaMesa) {
    for (Carta c : mano) {

      //Carta especial, siempre se puede jugar
      if (c.getColor().equals("Negro")) {
        return true;
      }
      if (c.mismoColor(cartaMesa)) {
        return true;
      }
      if (c.mismoValor(cartaMesa)) {
        return true;
      }
    }
    return false;
  }

}
