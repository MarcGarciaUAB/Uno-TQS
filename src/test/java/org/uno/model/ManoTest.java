package org.uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ManoTest {

  private Mano mano;
  private Carta rojo5, azul7, verde5, negroChange;

  @BeforeEach
  void setUp() {
    mano = new Mano();
    rojo5 = new Carta(5, "Rojo");
    azul7 = new Carta(7, "Azul");
    verde5 = new Carta(5, "Verde");
    negroChange = new Carta("Change", "Negro");
  }

  // --------------------------------------------------------
  // --- Caja negra: añadir y eliminar carta
  // --------------------------------------------------------
  @Test
  void testAñadirYEliminarCarta() {
    mano.añadirCarta(rojo5);
    mano.añadirCarta(azul7);
    assertEquals(2, mano.getNumeroCartas());
    assertTrue(mano.getMano().contains(rojo5));

    mano.eliminarCarta(rojo5);
    assertEquals(1, mano.getNumeroCartas());
    assertFalse(mano.getMano().contains(rojo5));
  }

  // --------------------------------------------------------
  // --- Precondiciones: añadirCarta(), eliminarCarta()
  // --------------------------------------------------------
  @Test
  void testPrecondicionesAñadirEliminar() {
    assertThrows(AssertionError.class, () -> mano.añadirCarta(null));
    assertThrows(AssertionError.class, () -> mano.eliminarCarta(null));
  }

  // --------------------------------------------------------
  // --- Precondición: tieneCartaJugable() con cartaMesa null
  // --------------------------------------------------------
  @Test
  void testPrecondicionTieneCartaJugable() {
    assertThrows(AssertionError.class, () -> mano.tieneCartaJugable(null, "Rojo"));
  }

  // --------------------------------------------------------
  // --- Carta jugable: por color, valor y efecto especial
  // --------------------------------------------------------
  @Test
  void testTieneCartaJugable() {
    Carta cartaMesa = new Carta(5, "Rojo");
    String colorActual = "Rojo";

    mano.añadirCarta(rojo5);
    mano.añadirCarta(azul7);
    mano.añadirCarta(negroChange);

    // por color
    assertTrue(mano.tieneCartaJugable(cartaMesa, colorActual));

    // por valor
    cartaMesa = verde5;
    colorActual = "Azul";
    assertTrue(mano.tieneCartaJugable(cartaMesa, colorActual));

    // por efecto especial
    cartaMesa = azul7;
    colorActual = "Verde";
    assertTrue(mano.tieneCartaJugable(cartaMesa, colorActual));
  }

  // --------------------------------------------------------
  // --- Data-driven / loop testing
  // --------------------------------------------------------
  @Test
  void testDataDrivenTieneCartaJugable() {
    Carta[] cartasMesa = {rojo5, azul7, verde5};
    String[] colores = {"Rojo", "Azul", "Verde"};

    for (Carta mesa : cartasMesa) {
      for (String color : colores) {
        Mano m = new Mano();
        m.añadirCarta(rojo5);
        m.añadirCarta(negroChange);
        assertTrue(m.tieneCartaJugable(mesa, color)); // negroChange siempre jugable
      }
    }
  }

  // --------------------------------------------------------
  // --- Path coverage: vacía, una carta, varias cartas
  // --------------------------------------------------------
  @Test
  void testPathCoverage() {
    Mano vacia = new Mano();
    assertFalse(vacia.tieneCartaJugable(rojo5, "Rojo"));

    Mano una = new Mano();
    una.añadirCarta(azul7);
    assertFalse(una.tieneCartaJugable(rojo5, "Rojo"));

    Mano varias = new Mano();
    varias.añadirCarta(rojo5);
    varias.añadirCarta(azul7);
    assertTrue(varias.tieneCartaJugable(rojo5, "Rojo"));
  }
}
