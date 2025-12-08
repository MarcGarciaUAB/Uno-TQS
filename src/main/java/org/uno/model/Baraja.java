package org.uno.model;

import java.util.ArrayList;
import java.util.List;

public class Baraja {

  private Krupier krupier;
  private List<Carta> baraja;

  // Invariante: baraja != null
  private void Invariante() {
    assert baraja != null : "Invariante: baraja no puede ser null";
  }

  public Baraja(Krupier k) {
    this.krupier = k;
    this.baraja = new ArrayList<>();
    crearBaraja();
    Invariante();
  }

  public int getNumeroCartas() {
    return baraja.size();
  }

  public List<Carta> getBaraja() {
    return new ArrayList<>(baraja);
  }

  public void añadirCartas(List<Carta> cartas) {
    assert cartas != null : "Precondición: cartas no null";
    if (!cartas.isEmpty()) {
      baraja.addAll(cartas);
    }
    Invariante();
  }

  public void setKrupier(Krupier krupier) {
    this.krupier = krupier;
  }

  private void crearBaraja() {
    String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};

    for (String color : colores) {
      baraja.add(new Carta(0, color)); // un solo 0
      for (int i = 1; i <= 9; i++) {  // dos copias del 1 al 9
        baraja.add(new Carta(i, color));
        baraja.add(new Carta(i, color));
      }
      baraja.add(new Carta("Block", color));
      baraja.add(new Carta("Block", color));
      baraja.add(new Carta("Reverse", color));
      baraja.add(new Carta("Reverse", color));
      baraja.add(new Carta("+2", color));
      baraja.add(new Carta("+2", color));
    }

    for (int i = 0; i < 4; i++) {
      baraja.add(new Carta("Change", "Negro"));
      baraja.add(new Carta("+4", "Negro"));
    }
  }

  public void barajar() {
    baraja = krupier.barajar(baraja); // mock o implementación real
    Invariante();
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
    for (int i = 0; i < n && !baraja.isEmpty(); i++) { // loop simple
      robadas.add(robar());
    }
    return robadas;
  }
}
