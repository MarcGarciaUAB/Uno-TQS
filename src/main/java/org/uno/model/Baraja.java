package org.uno.model;

import java.util.ArrayList;
import java.util.List;

public class Baraja {

  private List<Carta> baraja;

  public Baraja() {
    this.baraja = new ArrayList<>();
    crearBaraja();
  }

  public int getNumeroCartas() {
    return baraja.size();
  }

  public List<Carta> getBaraja() {
    return baraja;
  }

  private void crearBaraja() {
    String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};

    for (String color : colores) {
      // Un solo 0
      baraja.add(new Carta(0, color));

      // Dos copias del 1 al 9
      for (int i = 1; i <= 9; i++) {
        baraja.add(new Carta(i, color));
        baraja.add(new Carta(i, color));
      }

      // Dos Block, dos Reverse, dos +2
      baraja.add(new Carta("Block", color));
      baraja.add(new Carta("Block", color));

      baraja.add(new Carta("Reverse", color));
      baraja.add(new Carta("Reverse", color));

      baraja.add(new Carta("+2", color));
      baraja.add(new Carta("+2", color));
    }

    // Cartas especiales
    for (int i = 0; i < 4; i++) {
      baraja.add(new Carta("Change", "Negro"));
      baraja.add(new Carta("+4", "Negro"));
    }
  }

  public void barajar() {
    //TODO: implementar con mock
  }

  public int tamaño() {
    return baraja.size();
  }

  public Carta robar() {
    if (baraja.isEmpty()) return null;
    return baraja.remove(0);
  }

  public List<Carta> robar(int n) {
    List<Carta> robadas = new ArrayList<>();
    for (int i = 0; i < n && !baraja.isEmpty(); i++) {
      robadas.add(robar());
    }
    return robadas;
  }
}