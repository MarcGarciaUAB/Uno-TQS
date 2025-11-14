package org.uno.model;

public class Carta {

  private int valor;
  private String color;

  public Carta (int v, String c){
    this.valor = v;
    this.color = c;
  }
  public void setValor(int v){this.valor = v;};
  public int getValor(){return valor;};

  public void setColor(String c){ this.color = c;};
  public String getColor(){return color;};

  //para poder comprobar si la carta jugada es válida.
  public boolean mismoColor(Carta carta1) {
  return false;}
  public boolean mismoValor(Carta carta1) {
  return false;}

}
