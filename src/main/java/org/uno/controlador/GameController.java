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
    private String colorActual = null;


  public GameController(Baraja baraja, Pila pila, List<Mano> jugadores) {
      this.baraja = baraja;
      this.pila = pila;
      this.jugadores = jugadores;
    }

  public boolean getSentidoHorario(){return this.sentidoHorario;}

  public Mano getJugadorActual() {
    return jugadores.get(turnoActual);
  }

  public String getColorActual() {
    return colorActual;
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
    if (!jugador.tieneCartaJugable(cartaEnPila)) return false;

    //hay que mirar primero si ha habido cambio de color
    if (colorActual != null && !colorActual.isEmpty()) {
      return carta.getColor().equals(colorActual) || carta.mismoValor(cartaEnPila);
    }
    //si no ha habido cambio de color se usa el de la pila (deberia ser el mismo que la pila pero el primer turno es null)
    return carta.mismoColor(cartaEnPila) || carta.mismoValor(cartaEnPila);
  }


  public void cambiarColor(String color) {
    this.colorActual = color;
  }

  public boolean jugarTurno(Mano jugador) {
    for (Carta c : jugador.getMano()) {
      if (esCartaValida(c, jugador)) {
        jugador.eliminarCarta(c);
        pila.jugarCarta(c);
        aplicarEfecto(c, jugador); //si es block, +2, etc. aplicar el efecto especial
        //si la carta es negra hay que pedirsela al jugador
        if (!c.getColor().equals("Negro")) {
          colorActual = c.getColor();
        }

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

      case "+4":
        siguienteJugador();
        for (int i = 0; i < 4; i++) {
          jugadores.get(turnoActual).añadirCarta(baraja.robar());
        }
        break;
    }

  }
  public String elegirColorBot(Mano jugador) {
    int r = 0, b = 0, g = 0, y = 0;

    for (Carta c : jugador.getMano()) {
      switch (c.getColor()) {
        case "Rojo":     r++; break;
        case "Azul":     b++; break;
        case "Verde":    g++; break;
        case "Amarillo": y++; break;
      }
    }
    //Math.max calcula el maximo entre dos elementos
    int max = Math.max(Math.max(r, b), Math.max(g, y));

    if (max == r) return "Rojo";
    if (max == b) return "Azul";
    if (max == g) return "Verde";
    return "Amarillo";
  }


}