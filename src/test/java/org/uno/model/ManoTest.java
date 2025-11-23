package org.uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManoTest {
  @Test
  void testCrearMano() {
    Mano mano = new Mano();
    assertNotEquals(null, mano);
  }


  @Test
  void testAñadirCarta() {
    Mano mano = new Mano();
    Carta carta = new Carta(5, "Rojo");

    mano.añadirCarta(carta);

    assertEquals(1, mano.getNumeroCartas());
    assertTrue(mano.getMano().contains(carta));
  }

  @Test
  void testEliminarCarta() {
    Mano mano = new Mano();
    Carta carta = new Carta(7, "Azul");

    mano.añadirCarta(carta);
    mano.eliminarCarta(carta);

    assertEquals(0, mano.getNumeroCartas());
    assertFalse(mano.getMano().contains(carta));
  }
  @Test
  void testGetNumeroCartas() {
    Mano mano = new Mano();

    assertEquals(0, mano.getNumeroCartas());

    mano.añadirCarta(new Carta(2, "Verde"));
    mano.añadirCarta(new Carta(9, "Azul"));
    assertEquals(2, mano.getNumeroCartas());
  }



@Test
  void testTieneCartaJugablePorColor() {
    Mano mano = new Mano();
    Carta cartaMesa = new Carta(5, "Rojo");

    mano.añadirCarta(new Carta(3, "Rojo"));
    mano.añadirCarta(new Carta(7, "Azul"));

    //la primera carta debería ser jugable por color
    assertTrue(mano.tieneCartaJugable(cartaMesa));
  }

  @Test
  void testTieneCartaJugablePorValor() {
    Mano mano = new Mano();
    Carta cartaMesa = new Carta(5, "Verde");

    mano.añadirCarta(new Carta(5, "Azul"));

    //la carta debería ser jugable por valor
    assertTrue(mano.tieneCartaJugable(cartaMesa));
  }

  @Test
  void testNoTieneCartaJugable() {
    Mano mano = new Mano();
    Carta cartaMesa = new Carta(9, "Amarillo");

    mano.añadirCarta(new Carta(3, "Rojo"));
    mano.añadirCarta(new Carta(7, "Verde"));

    assertFalse(mano.tieneCartaJugable(cartaMesa));
  }

  @Test
  void testTieneCartaEspecial() {
    Mano mano = new Mano();
    Carta cartaMesa = new Carta(5, "Rojo");

    mano.añadirCarta(new Carta("Change", "Negro"));

    //una carta de efecto (en este caso cambio de color) SIEMPRE es jugable.
    assertTrue(mano.tieneCartaJugable(cartaMesa));
  }
}