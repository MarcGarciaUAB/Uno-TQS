package org.uno.controlador;

import org.uno.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class GameControllerTest {

    private Baraja mockBaraja;
    private Pila pila;
    private GameController controlador;
    private Mano jugador1;
    private Mano jugador2;
    private Mano jugador3;

    @BeforeEach
    void setUp() {
      // Creamos un mock de Baraja para tener control total
      mockBaraja = mock(Baraja.class);

      pila = new Pila();

      // Manos iniciales, decidiremos que manos tienen
      jugador1 = new Mano();
      jugador2 = new Mano();
      jugador3 = new Mano();

      // Inicializamos el controlador
      controlador = new GameController(mockBaraja, pila, List.of(jugador1, jugador2, jugador3));
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

    //no puede jugar una carta y la baraja está vacía, por lo tanto, hay que reutilizar la pila
    Carta carta2 = new Carta(1, "Verde");
    jugador1.añadirCarta(carta2);
    Carta cartaEnPila = new Carta(9, "Amarillo");
    pila.jugarCarta(cartaEnPila);

    // Simulamos baraja vacía
    when(mockBaraja.robar()).thenReturn(null);
    List<Carta> pilaCopiada = new ArrayList<>(pila.getPila());

    // Es la unica manera de poder mockear metodos void
    doAnswer(invocation -> {
      mockBaraja.añadirCartas(pilaCopiada);
      return null;
    }).when(mockBaraja).barajar();

    jugado = controlador.jugarTurno(jugador1);
    assertFalse(jugado);

    assertEquals(0, pila.getPila().size());

  }
  @Test
  void testSiguienteJugador() {
    // Turno inicial
    assertEquals(jugador1, controlador.getJugadorActual());

    // Avanzar turno
    controlador.siguienteJugador();
    assertEquals(jugador2, controlador.getJugadorActual());

    // Pasa al tercer jugador
    controlador.siguienteJugador();
    assertEquals(jugador3, controlador.getJugadorActual());
  }
  @Test
  void testReverseCambiaSentido() {
    Carta reverse = new Carta("Reverse", "Rojo");
    controlador.aplicarEfecto(reverse, jugador1);

    // El sentido se invierte, al avanzar el turno debería ir en sentido contrario
    controlador.siguienteJugador();
    //versión mejor: no debería saltar el turno de jugador 2, sigue en jugador 2. usamos un getter.
    assertFalse(controlador.getSentidoHorario());
  }

  @Test
  void testBlockSaltaTurno() {
    Carta block = new Carta("Block", "Rojo");
    controlador.aplicarEfecto(block, jugador1);

    //ya que la logica del juego salta el turno fuera de aplicar efecto, esta función solo salta un turno, es decir,
    //pasa a ser el jugador 2, no el 3, ya que fuera de esta función saltará de turno de nuevo.
    assertEquals(jugador2, controlador.getJugadorActual());
  }
  @Test
  void testMas2HaceRobar() {
    Carta mas2 = new Carta("+2", "Rojo");
    // Simulamos robo
    when(mockBaraja.robar()).thenReturn(new Carta(1,"Azul"), new Carta(2,"Verde"));
    controlador.aplicarEfecto(mas2, jugador1);
    // jugador2 debería tener 2 cartas
    assertEquals(2, jugador2.getNumeroCartas());
  }
  @Test
  void testMas4HaceRobar() {
    Carta mas4 = new Carta("+4", "Negro");
    when(mockBaraja.robar()).thenReturn(
        new Carta(1,"Azul"),
        new Carta(2,"Verde"),
        new Carta(3,"Rojo"),
        new Carta(4,"Amarillo")
    );

    controlador.aplicarEfecto(mas4, jugador1);

    assertEquals(4, jugador2.getNumeroCartas());
  }
}