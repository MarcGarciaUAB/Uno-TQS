package org.uno.model;

import java.util.ArrayList;
import java.util.List;

public class Mano {

  private List<Carta> mano;

  // Invariante: mano != null
  private void Invariante() {
    assert mano != null : "Invariante: mano no puede ser null";
  }

  public Mano() {
    this.mano = new ArrayList<>();
    Invariante();
  }

  public List<Carta> getMano() {
    return new ArrayList<>(mano);
  }

  public int getNumeroCartas() {
    return mano.size();
  }

  public void añadirCarta(Carta carta) {
    assert carta != null : "Precondicion: carta no null";
    mano.add(carta);
    Invariante();
    assert mano.contains(carta) : "Postcondicion: carta añadida";
  }

  public void eliminarCarta(Carta carta) {
    assert carta != null : "Precondicion: carta no null";
    mano.remove(carta);
    Invariante();
    assert !mano.contains(carta) : "Postcondicion: carta eliminada";
  }

   ////////////////////////////////////////////////////////////////////////////////////
   // Test de caja negra / blanca: loops y condiciones.                               /
   // Devuelve true si hay carta jugable por color, valor o efecto especial.          /
   // Loop testing: loop simple (for), loop anidado? (condición if dentro de loop)    /
   ////////////////////////////////////////////////////////////////////////////////////

  public boolean tieneCartaJugable(Carta cartaMesa, String colorActual) {
    assert cartaMesa != null : "Precondicion: cartaMesa no null";

    for (Carta c : mano) { // loop simple
      // loop anidado con condiciones
      if ("Negro".equals(c.getColor())) return true; // efecto especial siempre jugable
      if (c.getColor().equals(colorActual)) return true;
      if (c.mismoValor(cartaMesa)) return true;
    }
    return false;
  }
}
