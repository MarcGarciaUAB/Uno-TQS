package org.uno.model;

public class Carta {

  private int valor;
  private String color;

  public Carta (){
    this.valor = 0;
    this.color = null;
  }

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
    return this.getColor().equals(carta1.getColor());
  }
  public boolean mismoValor(Carta carta1) {
    return this.getValor()==carta1.getValor();}

}
