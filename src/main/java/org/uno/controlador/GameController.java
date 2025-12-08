package org.uno.controlador;

import org.uno.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Baraja baraja;
    private Pila pila;
    private List<Mano> jugadores;
    private int turnoActual = 0;
    private boolean sentidoHorario = true;


  public GameController(Baraja baraja, Pila pila, List<Mano> jugadores) {
      this.baraja = baraja;
      this.pila = pila;
      this.jugadores = jugadores;
    }

  public boolean getSentidoHorario(){return this.sentidoHorario;}

  public Mano getJugadorActual() {
    return jugadores.get(turnoActual);
  }
  public void siguienteJugador() {
    if (sentidoHorario) {
      //si llega al final, vuelve a 0
      turnoActual = (turnoActual + 1) % jugadores.size();
    } else {
      //para los cambios de sentido
      turnoActual = (turnoActual - 1 + jugadores.size()) % jugadores.size();
    }
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
    if (robada == null) {
      List<Carta> cartasPila = new ArrayList<>(pila.getPila());
      if (!cartasPila.isEmpty()) {
        // Se añaden las cartas de la pila a la baraja
        baraja.añadirCartas(cartasPila);
        pila.vaciar();
        baraja.barajar();
        robada = baraja.robar();
      }
    }
    if (robada != null)
      jugador.añadirCarta(robada);
    return false;
  }

  public void aplicarEfecto(Carta carta, Mano jugador) {
    if (carta.getEfecto() == null) return;

    switch (carta.getEfecto()) {

      case "Reverse":
        sentidoHorario = !sentidoHorario;
        break;

      case "Block":
        this.siguienteJugador();
        break;

      case "+2":
        siguienteJugador();
        jugadores.get(turnoActual).añadirCarta(baraja.robar());
        jugadores.get(turnoActual).añadirCarta(baraja.robar());
        break;
    }
  }


}