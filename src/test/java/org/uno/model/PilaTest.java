package org.uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PilaTest {

  private Pila pila;
  private Carta rojo5, azul7, negroChange;

  @BeforeEach
  void setUp() {
    pila = new Pila();
    rojo5 = new Carta(5, "Rojo");
    azul7 = new Carta(7, "Azul");
    negroChange = new Carta("Change", "Negro");
  }

  // --------------------------------------------------------
  // --- Caja negra: jugar carta y consultar última carta
  // --------------------------------------------------------
  @Test
  void testJugarCarta() {
    pila.jugarCarta(rojo5);
    assertEquals(rojo5, pila.ultimaCarta());

    pila.jugarCarta(azul7);
    assertEquals(azul7, pila.ultimaCarta());
  }

  // --------------------------------------------------------
  // --- Precondición: jugarCarta(null)
  // --------------------------------------------------------
  @Test
  void testPrecondicionJugarCarta() {
    assertThrows(AssertionError.class, () -> pila.jugarCarta(null));
  }

  // --------------------------------------------------------
  // --- Caja negra: vaciar pila
  // --------------------------------------------------------
  @Test
  void testVaciar() {
    pila.jugarCarta(rojo5);
    pila.vaciar();

    assertTrue(pila.getPila().isEmpty());
    assertNull(pila.ultimaCarta());
  }

  // --------------------------------------------------------
  // --- Data-driven / loop testing: jugar varias cartas
  // --------------------------------------------------------
  @Test
  void testDataDrivenJugarVariasCartas() {
    Carta[] cartas = {rojo5, azul7, negroChange};

    for (Carta c : cartas) {
      pila.jugarCarta(c);
      assertEquals(c, pila.ultimaCarta());
    }

    assertEquals(cartas.length, pila.getPila().size());
  }

  // --------------------------------------------------------
  // --- Path coverage: vacía, una carta, varias cartas
  // --------------------------------------------------------
  @Test
  void testPathCoverageUltimaCarta() {
    Pila vacia = new Pila();
    assertNull(vacia.ultimaCarta());

    Pila una = new Pila();
    una.jugarCarta(rojo5);
    assertEquals(rojo5, una.ultimaCarta());

    Pila varias = new Pila();
    varias.jugarCarta(rojo5);
    varias.jugarCarta(azul7);
    assertEquals(azul7, varias.ultimaCarta());
  }
}
