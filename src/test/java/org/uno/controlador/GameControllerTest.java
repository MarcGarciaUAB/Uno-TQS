package org.uno.controlador;

import org.uno.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

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

  @Test
  void testJugarTurno() {
      //puede jugar la carta
    Carta carta = new Carta(5, "Rojo");
    jugador1.añadirCarta(carta);
    pila.jugarCarta(new Carta(3, "Rojo"));

    boolean jugado = controlador.jugarTurno(jugador1);

    assertTrue(jugado);
    assertEquals(0, jugador1.getNumeroCartas());

    //no puede jugar una carta, por lo tanto debe robar
    Carta carta1 = new Carta(2, "Azul");
    jugador1.añadirCarta(carta1);
    pila.jugarCarta(new Carta(5, "Rojo"));

    Carta robada = new Carta(7, "Rojo");
    when(mockBaraja.robar()).thenReturn(robada);

    jugado = controlador.jugarTurno(jugador1);

    assertFalse(jugado);
    assertTrue(jugador1.getMano().contains(robada));
    assertEquals(2, jugador1.getNumeroCartas());
  }

}