package org.uno.controlador;

import org.uno.model.*;

import java.util.List;

public class GameController {
    private Baraja baraja;
    private Pila pila;
    private List<Mano> jugadores;

    public GameController(Baraja baraja, Pila pila, List<Mano> jugadores) {
      this.baraja = baraja;
      this.pila = pila;
      this.jugadores = jugadores;
    }

    public boolean esCartaValida(Carta carta, Mano jugador) {
      Carta cartaEnPila = pila.ultimaCarta();
      if (jugador.tieneCartaJugable(cartaEnPila)) {
        return carta.mismoColor(cartaEnPila) || carta.mismoValor(cartaEnPila);
      }
      else
        return false;
    }

  public boolean jugarTurno(Mano jugador) {
    for (Carta c : jugador.getMano()) {
      if (esCartaValida(c, jugador)) {
        jugador.eliminarCarta(c);
        pila.jugarCarta(c);
        aplicarEfecto(c, jugador); //si es block, +2, etc. aplicar el efecto especial
        return true;
      }
    }
    // Si no podia jugar, roba una carta
    Carta robada = baraja.robar();
    if (robada != null) {
      jugador.añadirCarta(robada);
    }
    return false;
  }

}