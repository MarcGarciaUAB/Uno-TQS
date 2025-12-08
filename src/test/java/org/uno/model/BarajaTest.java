package org.uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class BarajaTest {

  private Baraja baraja;
  private Krupier mockKrupier;

  private Carta rojo5, azul7, negroChange;

  @BeforeEach
  void setUp() {
    mockKrupier = mock(Krupier.class);
    baraja = new Baraja(mockKrupier);

    rojo5 = new Carta(5, "Rojo");
    azul7 = new Carta(7, "Azul");
    negroChange = new Carta("Change", "Negro");
  }

  // Caja negra: creación
  @Test
  void testCrearBaraja() {
    assertNotNull(baraja);
    assertEquals(108, baraja.tamaño()); // 108 cartas
  }

  // Caja blanca: robar una carta
  @Test
  void testRobarUnaCarta() {
    int tamañoInicial = baraja.tamaño();
    Carta cartaRobada = baraja.robar();
    assertNotNull(cartaRobada);
    assertEquals(tamañoInicial - 1, baraja.tamaño());
  }

  // Data-driven: robar varias cartas
  @Test
  void testRobarVariasCartas() {
    int tamañoInicial = baraja.tamaño();
    int[] cantidades = {1, 5, 10, 108, 200};

    for (int n : cantidades) {
      Baraja copia = new Baraja(mockKrupier);
      List<Carta> robadas = copia.robar(n);
      assertEquals(Math.min(n, 108), robadas.size());
      assertEquals(108 - robadas.size(), copia.tamaño());
    }
  }

  // Caja negra: añadir cartas
  @Test
  void testAñadirCartas() {
    List<Carta> nuevas = List.of(rojo5, azul7, negroChange);
    int tamañoInicial = baraja.tamaño();
    baraja.añadirCartas(nuevas);
    assertEquals(tamañoInicial + 3, baraja.tamaño());
    assertTrue(baraja.getBaraja().contains(rojo5));
  }

  // Barajar usando mock
  @Test
  void testBarajar() {
    List<Carta> original = baraja.getBaraja();
    when(mockKrupier.barajar(original)).thenReturn(new ArrayList<>(original));
    baraja.barajar();
    // Statement coverage y mock object probado
    assertEquals(original.size(), baraja.tamaño());
  }

  // Path coverage: robar todas, robar 0, robar más que tamaño
  @Test
  void testPathCoverageRobar() {
    Baraja copia = new Baraja(mockKrupier);
    // robar 0
    List<Carta> cero = copia.robar(0);
    assertTrue(cero.isEmpty());

    // robar más que tamaño
    List<Carta> muchas = copia.robar(200);
    assertEquals(108, muchas.size());
    assertEquals(0, copia.tamaño());
  }
}
