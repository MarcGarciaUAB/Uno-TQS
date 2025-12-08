package org.uno.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;


public class BarajaTest {
  private List<Carta> barajada = new ArrayList<>();
  private Krupier mockKrupier;
  Baraja baraja;

  @BeforeEach
  void setUp() {
    mockKrupier = mock(Krupier.class);
    baraja = new Baraja(mockKrupier);
  }

  @Test
  void testCrearBaraja(){
    assertNotEquals(null, baraja);
  }

//TODO: pre/post condiciones que hagan esto.
  /*@Test
  void testBarajaInicialTiene108Cartas() {
    //cuatro cambia color, cuatro +4, cuatro 0, ocho 1-9 y ocho block, reverse y +2
    assertEquals(108, baraja.getNumeroCartas());
  }
*/
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
    //declaramos la baraja con las cartas específicas que queremos
    barajada = baraja.getBaraja();
    List<Carta> antes = baraja.getBaraja();
    //esto busca la posición de la carta que queremos, en este caso un +4
    Carta cartaEspecial = null;
    for (Carta c : barajada) {
      if ("Negro".equals(c.getColor()) && "+4".equals(c.getEfecto())) {
        cartaEspecial = c;
        break;
      }
    }
//la ponemos al principio
    if (cartaEspecial != null) {
      barajada.remove(cartaEspecial);
      barajada.add(0, cartaEspecial);
    }

    when(mockKrupier.barajar(antes)).thenReturn(barajada);
    baraja.barajar();

    List<Carta> despues = baraja.getBaraja();
//comprobamos que no es lo mismo
    assertNotEquals(antes, despues);
    Carta primera = baraja.robar();
    //nos aseguramos de que el mock ha barajado bien
    assertEquals("+4", primera.getEfecto());
    assertEquals("Negro", primera.getColor());
    //y que se ha creado correctamente con el tamaño adecuado
    assertEquals(107, baraja.tamaño());
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
