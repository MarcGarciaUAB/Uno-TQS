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

  // Invariante: turnoActual válido y jugadores no vacío
  private void checkInvariante() {
    assert jugadores != null && !jugadores.isEmpty() : "Invariante: jugadores no vacío";
    assert turnoActual >= 0 && turnoActual < jugadores.size() : "Invariante: turnoActual válido";
  }

  public GameController(Baraja baraja, Pila pila, List<Mano> jugadores) {
    assert baraja != null && pila != null && jugadores != null && !jugadores.isEmpty() : "Pre: baraja, pila y jugadores no null";
    this.baraja = baraja;
    this.pila = pila;
    this.jugadores = jugadores;
    checkInvariante();
  }

  public boolean getSentidoHorario() { return this.sentidoHorario; }
  public Mano getJugadorActual() { return jugadores.get(turnoActual); }
  public List<Mano> getJugadores() { return jugadores; }
  public Carta getUltimaCarta() { return pila.ultimaCarta(); }
  public String getColorActual() { return colorActual; }

  public void siguienteJugador() {
    if (sentidoHorario) turnoActual = (turnoActual + 1) % jugadores.size();
    else turnoActual = (turnoActual - 1 + jugadores.size()) % jugadores.size();
    checkInvariante();
  }

  public void iniciarPila(Carta cartaInicial) {
    assert cartaInicial != null : "Pre: cartaInicial no null";
    pila.jugarCarta(cartaInicial);

    if (!"Negro".equals(cartaInicial.getColor())) colorActual = cartaInicial.getColor();
    else {
      int rand = (int) (Math.random() * 4);
      colorActual = switch (rand) {
        case 0 -> "Rojo";
        case 1 -> "Azul";
        case 2 -> "Verde";
        default -> "Amarillo";
      };
    }
    assert colorActual != null : "Post: colorActual definido";
  }

  public boolean esCartaValida(Carta carta, Mano jugador) {
    assert carta != null && jugador != null : "Pre: carta y jugador no null";
    Carta cartaEnPila = pila.ultimaCarta();
    if ("Negro".equals(carta.getColor())) return true;

    if (!jugador.tieneCartaJugable(cartaEnPila, this.getColorActual())) return false;

    if (colorActual != null && !colorActual.isEmpty())
      return carta.getColor().equals(colorActual) || carta.mismoValor(cartaEnPila);

    return carta.mismoColor(cartaEnPila) || carta.mismoValor(cartaEnPila);
  }

  public boolean jugarCarta(Mano jugador, int indiceCarta, String colorElegido){
    Carta c = jugador.getMano().get(indiceCarta);
    if (!esCartaValida(c, jugador)) return false;

    jugador.eliminarCarta(c);
    pila.jugarCarta(c);

    colorActual = "Negro".equals(c.getColor()) && colorElegido != null ? colorElegido : c.getColor();

    aplicarEfecto(c, jugador);
    return true;
  }

  public Carta robarCarta(Mano jugador) {
    Carta robada = baraja.robar();
    if (robada == null && !pila.getPila().isEmpty()) {
      reiniciarPila();
      robada = baraja.robar();
    }
    if (robada != null) jugador.añadirCarta(robada);
    return robada;
  }

  public void cambiarColor(String color) {
    assert color != null : "Pre: color no null";
    colorActual = color;
    assert colorActual.equals(color) : "Post: colorActual cambiado";
  }

  private void reiniciarPila() {
    List<Carta> cartasPila = new ArrayList<>(pila.getPila());
    if (cartasPila.size() <= 1) return;

    Carta ultima = pila.ultimaCarta();
    cartasPila.remove(ultima);
    baraja.añadirCartas(cartasPila);
    pila.vaciar();
    pila.jugarCarta(ultima);

    if (!"Negro".equals(ultima.getColor())) colorActual = ultima.getColor();
  }

  public boolean jugarTurno(Mano jugador) {
    for (Carta c : jugador.getMano()) {  // loop simple
      if (esCartaValida(c, jugador)) {
        jugador.eliminarCarta(c);
        pila.jugarCarta(c);
        aplicarEfecto(c, jugador);
        colorActual = c.getColor().equals("Negro") ? elegirColorBot(jugador) : c.getColor();
        return true;
      }
    }
    Carta robada = baraja.robar();
    if (robada == null && !pila.getPila().isEmpty()) reiniciarPila();
    if (robada != null) jugador.añadirCarta(robada);
    return false;
  }

  public void aplicarEfecto(Carta carta, Mano jugador) {
    if (carta.getEfecto() == null) return;

    switch (carta.getEfecto()) {
      case "Reverse" -> sentidoHorario = !sentidoHorario;
      case "Block" -> siguienteJugador();
      case "+2" -> {
        siguienteJugador();
        for (int i = 0; i < 2; i++) jugadores.get(turnoActual).añadirCarta(baraja.robar());
      }
      case "+4" -> {
        siguienteJugador();
        for (int i = 0; i < 4; i++) jugadores.get(turnoActual).añadirCarta(baraja.robar());
      }
    }
    checkInvariante();
  }

  public Mano procesarTurnoPartida() {
    Mano jugador = getJugadorActual();
    jugarTurno(jugador);
    if (jugador.getNumeroCartas() == 0) return jugador;
    siguienteJugador();
    return null;
  }

  public boolean finJuego() {
    for (Mano jugador : jugadores) {
      if (jugador.getNumeroCartas() == 0) return true;
    }
    return false;
  }

  public String elegirColorBot(Mano jugador) {
    int r = 0, b = 0, g = 0, y = 0;
    for (Carta c : jugador.getMano()) {
      switch (c.getColor()) {
        case "Rojo" -> r++;
        case "Azul" -> b++;
        case "Verde" -> g++;
        case "Amarillo" -> y++;
      }
    }
    int max = Math.max(Math.max(r, b), Math.max(g, y));
    if (max == r) return "Rojo";
    if (max == b) return "Azul";
    if (max == g) return "Verde";
    return "Amarillo";
  }
}
