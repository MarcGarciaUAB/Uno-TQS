package org.uno.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


import java.util.List;


Myser
public class BarajaTest {
  Baraja baraja;

  @BeforeEach
  void setUp() {
    baraja = new Baraja();
  }

  @Test
  void testCrearBaraja(){
    assertNotEquals(null, baraja);
  }


  @Test
  void testBarajaInicialTiene108Cartas() {
    //cuatro cambia color, cuatro +4, cuatro 0, ocho 1-9 y ocho block, reverse y +2
    assertEquals(108, baraja.getNumeroCartas());
  }

  @Test
  void testBarajaTieneColoresCorrectos() {
    List<Carta> cartas = baraja.getBaraja();

    //lista.stream().anyMatch(condición) busca cualquier item en la lista que cumpla la condición

    assertTrue(cartas.stream().anyMatch(c -> c.mismoColor(new Carta(0, "Rojo"))));
    assertTrue(cartas.stream().anyMatch(c -> c.mismoColor(new Carta(0, "Azul"))));
    assertTrue(cartas.stream().anyMatch(c -> c.mismoColor(new Carta(0, "Verde"))));
    assertTrue(cartas.stream().anyMatch(c -> c.mismoColor(new Carta(0, "Amarillo"))));
    assertTrue(cartas.stream().anyMatch(c -> c.mismoColor(new Carta(0, "Negro"))));
  }

  @Test
  void testBarajar() {
    List<Carta> antes = List.copyOf(baraja.getBaraja());
    baraja.barajar();

    List<Carta> despues = baraja.getBaraja();

    assertNotEquals(antes, despues);
  }

  @Test
  void testRobarUnaCarta() {
    int tamañoInicial = baraja.tamaño();
    Carta robada = baraja.robar();

    assertNotNull(robada);
    assertEquals(tamañoInicial - 1, baraja.tamaño());
  }

  @Test
  void testRobarVariasCartas() {
    //para robar la mano inicial, +2 y +4
    int tamañoInicial = baraja.tamaño();
    List<Carta> robadas = baraja.robar(7);

    assertEquals(7, robadas.size());
    assertEquals(tamañoInicial - 7, baraja.tamaño());
  }

}
