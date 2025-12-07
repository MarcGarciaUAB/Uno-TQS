package org.uno.controlador;

import org.uno.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

public class GameControllerTest {

    private Baraja mockBaraja;
    private Pila pila;
    private GameController controlador;
    private Mano jugador1;
    private Mano jugador2;

    @BeforeEach
    void setUp() {
      // Creamos un mock de Baraja para tener control total
      mockBaraja = mock(Baraja.class);

      pila = new Pila();

      // Manos iniciales, decidiremos que manos tienen
      jugador1 = new Mano();
      jugador2 = new Mano();

      // Inicializamos el controlador
      controlador = new GameController(mockBaraja, pila, List.of(jugador1, jugador2));
    }

    @Test
    void testJugadorPuedeJugarCartaCorrecta() {
      Carta carta = new Carta(5, "Rojo");
      jugador1.añadirCarta(carta);

      // Carta visible en la pila
      Carta cartaEnPila = new Carta(3, "Rojo");
      pila.jugarCarta(cartaEnPila);

      // Comprobamos que el jugador puede jugar la carta
      assertTrue(controlador.esCartaValida(carta, jugador1));
    }

    @Test
    void testJugadorNoPuedeJugarCartaIncorrecta() {
      Carta carta = new Carta(2, "Azul");
      jugador1.añadirCarta(carta);

      Carta cartaEnPila = new Carta(3, "Rojo");
      pila.jugarCarta(cartaEnPila);

      assertFalse(controlador.esCartaValida(carta, jugador1));
    }
  }