package org.uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManoTest {
  Mano mano;
  Carta cincoRojo;
  Carta sieteAzul;
  Carta cincoVerde;
  Carta especial;

  @BeforeEach
  void setUp() {
    mano = new Mano();
    cincoRojo = new Carta(5, "Rojo");
    sieteAzul = new Carta(7, "Azul");
    cincoVerde = new Carta(5, "Verde");
    especial = new Carta("Change", "Negro");
  }

  @Test
  void testCrearMano() {
    assertNotEquals(null, mano);
  }

  @Test
  void testAñadirCarta() {
    mano.añadirCarta(cincoRojo);

    assertEquals(1, mano.getNumeroCartas());
    assertTrue(mano.getMano().contains(cincoRojo));
  }

  @Test
  void testEliminarCarta() {
    mano.añadirCarta(sieteAzul);
    mano.eliminarCarta(sieteAzul);

    assertEquals(0, mano.getNumeroCartas());
    assertFalse(mano.getMano().contains(sieteAzul));
  }
  @Test
  void testGetNumeroCartas() {
    assertEquals(0, mano.getNumeroCartas());

    mano.añadirCarta(cincoRojo);
    mano.añadirCarta(cincoVerde);
    assertEquals(2, mano.getNumeroCartas());
  }


@Test
  void testTieneCartaJugablePorColor() {
    Carta cartaMesa = new Carta(3, "Rojo");
    String color = "Rojo";

    mano.añadirCarta(cincoRojo);
    mano.añadirCarta(sieteAzul);
    //la primera carta debería ser jugable por color
    assertTrue(mano.tieneCartaJugable(cartaMesa, color));
  }

  @Test
  void testTieneCartaJugablePorValor() {
    Carta cartaMesa = cincoVerde;

    mano.añadirCarta(cincoRojo);
    String color = "Rojo";

    //la carta debería ser jugable por valor
    assertTrue(mano.tieneCartaJugable(cartaMesa, color));
  }

  @Test
  void testNoTieneCartaJugable() {
    Carta cartaMesa = new Carta(9, "Amarillo");
    String color = "Amarillo";

    mano.añadirCarta(cincoRojo);
    mano.añadirCarta(sieteAzul);

    assertFalse(mano.tieneCartaJugable(cartaMesa, color));
  }

  @Test
  void testTieneCartaEspecial() {
    Carta cartaMesa = cincoRojo;
    String color = "Rojo";

    mano.añadirCarta(especial);

    //una carta de efecto (en este caso cambio de color) SIEMPRE es jugable.
    assertTrue(mano.tieneCartaJugable(cartaMesa, color));
  }
}