package org.uno.vista;

import org.uno.controlador.GameController;
import org.uno.model.*;

import java.util.List;
import java.util.Scanner;

public class GameUI {
  private GameController controlador;
  private Scanner sc = new Scanner(System.in);

  public GameUI(GameController controlador){
    this.controlador = controlador;
  }

  public void iniciarPartida() {
    while (!controlador.finJuego()) {
      Mano jugador = controlador.getJugadorActual();
      String nombreJugador = esHumano(jugador) ? "Jugador1" : "Bot " + (controlador.getJugadores().indexOf(jugador));

      mostrarEstado(jugador, nombreJugador);

      if (esHumano(jugador)) {
        mostrarCartasDeCadaJugador();
        turnoHumano(jugador);
      } else {
        System.out.println(nombreJugador + " está jugando...");
        boolean jugoCarta = controlador.jugarTurno(jugador); // devuelve true si jugó una carta

        // Averiguar qué carta jugó o si robó
        if (jugoCarta) {
          Carta ultima = controlador.getUltimaCarta();
          System.out.println(nombreJugador + " ha jugado: " + ultima);
        } else {
          System.out.println(nombreJugador + " no pudo jugar y robó una carta.");
        }
      }

      // Sleep entre turnos
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      // Avanzamos al siguiente turno
      controlador.siguienteJugador();
    }

    System.out.println("\n¡Fin del juego! Ganador: " + obtenerGanador());
  }

  private boolean esHumano(Mano jugador) {
    return controlador.getJugadores().indexOf(jugador) == 0;
  }

  private void mostrarEstado(Mano jugador, String nombre) {
    System.out.println("\nTurno de: " + nombre);
    System.out.println("Carta en la pila: " + controlador.getUltimaCarta());
    System.out.println("Color en juego: " + controlador.getColorActual());
    if (esHumano(jugador)) mostrarMano(jugador);
  }

  private void mostrarMano(Mano jugador) {
    System.out.println("Tu mano:");
    int i = 1;
    for (Carta c : jugador.getMano()) {
      System.out.println(i++ + ": " + c);
    }
  }

  private void mostrarCartasDeCadaJugador() {
    System.out.println("\nNúmero de cartas de cada jugador:");
    List<Mano> jugadores = controlador.getJugadores();
    for (int i = 0; i < jugadores.size(); i++) {
      String nombre = (i == 0) ? "Jugador1" : "Bot " + i;
      System.out.println(nombre + ": " + jugadores.get(i).getNumeroCartas());
    }
  }

  private void turnoHumano(Mano jugador) {
    boolean turnoValido = false;

    while (!turnoValido) {
      System.out.println("Selecciona carta (número) o 0 para robar:");
      int opcion = sc.nextInt();
      sc.nextLine(); // limpiar buffer

      if (opcion == 0) {
        Carta robada = controlador.robarCarta(jugador);
        if (robada != null) {
          System.out.println("Has robado: " + robada);
        } else {
          System.out.println("No hay cartas para robar.");
        }
        turnoValido = true; // termina el turno después de robar

      } else {
        if (opcion < 1 || opcion > jugador.getNumeroCartas()) {
          System.out.println("Número inválido, intenta de nuevo.");
          continue;
        }

        Carta cartaSeleccionada = jugador.getMano().get(opcion - 1);
        String color = null;

        // Solo pedimos color si la carta es negra
        if ("Negro".equals(cartaSeleccionada.getColor())) {
          System.out.println("Elige color: Rojo, Azul, Verde, Amarillo");
          color = sc.nextLine();
        }

        boolean exito = controlador.jugarCarta(jugador, opcion - 1, color);
        if (exito) {
          System.out.println("Has jugado: " + cartaSeleccionada);
          turnoValido = true; // movimiento válido, termina el turno
        } else {
          System.out.println("Movimiento inválido. Intenta de nuevo.");
        }
      }
    }
  }

  private String obtenerGanador() {
    List<Mano> jugadores = controlador.getJugadores();
    for (int i = 0; i < jugadores.size(); i++) {
      if (jugadores.get(i).getNumeroCartas() == 0) {
        return i == 0 ? "Jugador1" : "Bot " + i;
      }
    }
    return "Nadie"; // en caso extremo
  }
}
