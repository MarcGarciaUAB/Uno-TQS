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
    return jugador.tieneCartaJugable(cartaEnPila);
  }
}