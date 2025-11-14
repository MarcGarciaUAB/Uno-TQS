package org.uno.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartaTest {
  @Test
  void testCrearCarta(){
    Carta carta = new Carta(5, "Rojo");
    assertEquals("Rojo", carta.getColor());
    assertEquals(5, carta.getValor());
  }
}
