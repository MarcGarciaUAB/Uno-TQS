package org.uno.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartaTest {

  @Test
  void testCrearCartas(){
    Carta carta = new Carta(5, "Rojo");
    assertEquals("Rojo", carta.getColor());
    assertEquals(5, carta.getValor());

    Carta carta1 = new Carta("Block", "Rojo");
    assertEquals("Block", carta1.getEfecto());
    assertEquals("Rojo", carta1.getColor());
  }

  @Test
  void testGettersYSetters(){
    Carta carta = new Carta();
    carta.setColor("Rojo");
    carta.setValor(2);
    assertEquals(2, carta.getValor());
    assertEquals("Rojo", carta.getColor());
  }

  @Test
  void testComprobarIguales(){
    Carta carta1 = new Carta(5, "Rojo");
    Carta carta2 = new Carta(7, "Rojo");
    Carta carta3 = new Carta(5, "Azul");

    assertTrue(carta1.mismoColor(carta2));
    assertTrue(carta3.mismoValor(carta1));
    assertFalse(carta2.mismoColor(carta3));
  }
}

