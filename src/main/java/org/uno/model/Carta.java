package org.uno.model;

public class Carta {

  private int valor;
  private String color;
  //efectos disponibles: bloquear turno="Block", revertir orden="Reverse", sumar dos cartas="+2"
  //efectos especiales: cambiar color="Change", sumar cuatro cartas y cambiar color="+4"
  private String efecto;

  public Carta (){
    //el 0 es válido
    this.valor = -1;
    this.color = null;
    this.efecto = null;
  }

  public Carta (int v, String c){
    this.valor = v;
    this.color = c;
    this.efecto = null;
  }

  public Carta (String e, String c){
    this.efecto = e;
    this.color = c;
    this.valor = -1;
  }

  public void setValor(int v){this.valor = v;};
  public int getValor(){return this.valor;};

  public void setColor(String c){ this.color = c;};
  public String getColor(){return this.color;};

  public void setEfecto(String e){ this.efecto = e;};
  public String getEfecto(){return this.efecto;};

  //para poder comprobar si la carta jugada es válida.
  public boolean mismoColor(Carta carta1) {
    //si el color es negro significa que es carta especial, siempre se puede tirar.
    if(!carta1.getColor().equals("Negro")) {
      return this.getColor().equals(carta1.getColor());
    }
    else
      return true;
  }
  public boolean mismoValor(Carta carta1) {
    //si el color es negro significa que es carta especial, siempre se puede tirar.
    if(!carta1.getColor().equals("Negro")) {
      return this.getValor() == carta1.getValor();
    }
    else
      return true;
  }

}
