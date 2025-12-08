package org.uno.model;

public class Carta {

  private int valor;
  private String color;
  private String efecto; // efectos especiales: "Block", "Reverse", "+2", "Change", "+4"

  // Invariante: valor >= -1, valor < 10, color != null si valor >=0 o efecto != null, efecto = null si valor != -1
  private void Invariante() {
    assert valor >= -1 : "Invariante: valor >= -1";
    assert valor <10 : "Invariante: valor < 10";
    assert color != null || efecto != null : "Invariante: color o efecto no puede ser null";
    if(valor != -1) assert efecto == null : "Invariante: carta numerica no tiene efecto";
  }

  public Carta() {
	  //creamos un cambia color por default para cumplir la invariante
    this.valor = -1;
    this.color = "Negro";
    this.efecto = "Change";
    Invariante();
  }

  public Carta(int v, String c) {
    assert v >= 0 && v <= 9 : "Precondicion: valor entre 0 y 9";
    assert c != null : "Precondicion: color no puede ser null";

    this.valor = v;
    this.color = c;
    this.efecto = null;
    Invariante();

    assert getValor() == v : "Postcondicion: valor seteado";
    assert getColor().equals(c) : "Postcondicion: color seteado";
    assert getEfecto() == null : "Postcondicion: efecto null";
  }

  public Carta(String e, String c) {
    assert e != null && ("Block".equals(e) || "Reverse".equals(e) || "+2".equals(e) || "Change".equals(e) || "+4".equals(e)) : "Precondicion: efecto valido";
    assert c != null : "Precondicion: color no puede ser null";

    this.efecto = e;
    this.color = c;
    this.valor = -1;
    Invariante();

    assert getEfecto().equals(e) : "Postcondicion: efecto seteado";
    assert getColor().equals(c) : "Postcondicion: color seteado";
    assert getValor() == -1 : "Postcondicion: valor -1";
  }

  public void setValor(int v){
    assert v >= 0 && v <= 9 : "Precondicion: valor entre 0 y 9";
    this.valor = v;
    Invariante();
    assert this.valor == v : "Postcondicion: valor seteado";
  }

  public int getValor(){ return this.valor; }

  public void setColor(String c){
    assert c != null : "Precondicion: color no null";
    this.color = c;
    Invariante();
    assert this.color.equals(c) : "Postcondicion: color seteado";
  }

  public String getColor(){ return this.color; }

  public void setEfecto(String e){
    assert e != null && ("Block".equals(e) || "Reverse".equals(e) || "+2".equals(e) || "Change".equals(e) || "+4".equals(e)) : "Precondicion: efecto valido";
    this.efecto = e;
    Invariante();
    assert this.efecto.equals(e) : "Postcondicion: efecto seteado";
  }

  public String getEfecto(){ return this.efecto; }

  public boolean mismoColor(Carta carta1) {
    assert carta1 != null : "Pre: carta no null";
    assert carta1.getColor() != null : "Pre: carta.color no null";

    boolean resultado = this.getColor().equals(carta1.getColor());

    assert resultado == (this.color.equals(carta1.color)) : "Post: resultado coincide con colores";
    return resultado;
  }

  public boolean mismoValor(Carta carta1) {
    assert carta1 != null : "Pre: carta no null";

    if (valor == -1) return false;
    boolean resultado = this.valor == carta1.getValor();
    assert resultado == (this.valor == carta1.valor) : "Post: resultado coincide con valores";
    return resultado;
  }

  @Override
  public String toString() {
    return (efecto != null) ? "[" + efecto + " - " + color + "]" : "[" + valor + " - " + color + "]";
  }
}
