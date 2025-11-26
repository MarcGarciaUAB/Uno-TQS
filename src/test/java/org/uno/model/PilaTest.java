package org.uno.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PilaTest {
  Pila pila;

  @BeforeEach
  void setUp() {
    pila = new Pila();
  }

  @Test
  void testCrearPila() {
    assertNotEquals(null, pila);
  }

  @Test
  void testJugarCarta() {
    Carta cartaJugada = new Carta(5, "Rojo");
    pila.jugarCarta(cartaJugada);
    assertEquals(cartaJugada, pila.ultimaCarta());
  }
}
