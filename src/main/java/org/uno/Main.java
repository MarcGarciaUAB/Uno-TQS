package org.uno;

import org.uno.controlador.GameController;
import org.uno.model.*;
import org.uno.vista.GameUI;

import java.util.Collections;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    Krupier k = new Krupier() {
      @Override
      public List<Carta> barajar(List<Carta> baraja) {
        Collections.shuffle(baraja);
        return baraja;
      }
    };
    Baraja baraja = new Baraja(k);
    Pila pila = new Pila();
    Mano jugador1 = new Mano();
    Mano jugador2 = new Mano();
    Mano jugador3 = new Mano();
    baraja.barajar();
    // Repartir 7 cartas a cada jugador
    for (int i = 0; i < 7; i++) {
      jugador1.añadirCarta(baraja.robar());
      jugador2.añadirCarta(baraja.robar());
      jugador3.añadirCarta(baraja.robar());
    }

    // Carta inicial en pila
    Carta inicial = baraja.robar();

    GameController controlador = new GameController(baraja, pila, List.of(jugador1, jugador2, jugador3));
    controlador.iniciarPila(inicial);
    GameUI ui = new GameUI(controlador);
    ui.iniciarPartida();
  }
}
