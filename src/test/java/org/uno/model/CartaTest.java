package org.uno.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CartaTest {


  // ----------- Constructores: numérico, efecto, vacío -----------
  @Test
  void testConstructorNumerico() {
    Carta c = new Carta(7, "Rojo");
    assertEquals(7, c.getValor());
    assertEquals("Rojo", c.getColor());
    assertNull(c.getEfecto());
    assertEquals("[7 - Rojo]", c.toString());
  }

  @Test
  void testConstructorEfecto() {
    Carta c = new Carta("Reverse", "Azul");
    assertEquals("Reverse", c.getEfecto());
    assertEquals("Azul", c.getColor());
    assertEquals(-1, c.getValor());
    assertEquals("[Reverse - Azul]", c.toString());
  }

  @Test
  void testConstructorVacio() {
    Carta c = new Carta();
    // según la versión actual, el constructor vacío crea una carta "Change Negro"
    assertEquals(-1, c.getValor());
    assertEquals("Negro", c.getColor());
    assertEquals("Change", c.getEfecto());
  }

  // ----------- Setters -----------
  @Test
  void testSetValor() {
    Carta c = new Carta(3, "Rojo");
    c.setValor(9);
    assertEquals(9, c.getValor());
  }

  @Test
  void testSetColor() {
    Carta c = new Carta(3, "Rojo");
    c.setColor("Azul");
    assertEquals("Azul", c.getColor());
  }

  @Test
  void testSetEfecto() {
    Carta c = new Carta("Block", "Verde");
    c.setEfecto("+4");
    assertEquals("+4", c.getEfecto());
  }

  // ----------- mismoColor(): ramas -----------
  @Test
  void testMismoColorTrue() {
    Carta a = new Carta(5, "Rojo");
    Carta b = new Carta(8, "Rojo");
    assertTrue(a.mismoColor(b));
  }

  @Test
  void testMismoColorFalse() {
    Carta a = new Carta(5, "Rojo");
    Carta b = new Carta(7, "Verde");
    assertFalse(a.mismoColor(b));
  }

  // ----------- mismoValor(): 3 caminos -----------
  @Test
  void testMismoValorEspecialDevuelveFalse() {
    Carta especial = new Carta("Change", "Negro");
    Carta mesa = new Carta(5, "Rojo");
    assertFalse(especial.mismoValor(mesa));
  }

  @Test
  void testMismoValorTrue() {
    Carta a = new Carta(5, "Rojo");
    Carta b = new Carta(5, "Azul");
    assertTrue(a.mismoValor(b));
  }

  @Test
  void testMismoValorFalse() {
    Carta a = new Carta(5, "Rojo");
    Carta b = new Carta(9, "Azul");
    assertFalse(a.mismoValor(b));
  }

  // ----------- toString ----------
  @Test
  void testToStringNumerica() {
    Carta c = new Carta(2, "Verde");
    assertEquals("[2 - Verde]", c.toString());
  }

  @Test
  void testToStringEfecto() {
    Carta c = new Carta("+2", "Amarillo");
    assertEquals("[+2 - Amarillo]", c.toString());
  }

  // ----------------------------------------------------------
  // PRECONDICIONES (assert que deben fallar)
  // ----------------------------------------------------------
  @Test
  void testPrecondicionesConstructorNumericoInvalidos() {
    assertThrows(AssertionError.class, () -> new Carta(-1, null)); // valor <0 y color null
    assertThrows(AssertionError.class, () -> new Carta(15, "Rojo")); // valor >9
    assertThrows(AssertionError.class, () -> new Carta(5, null)); // color null
  }

  @Test
  void testPrecondicionesConstructorEfectoInvalidos() {
    assertThrows(AssertionError.class, () -> new Carta("NOPE", "Rojo"));
    assertThrows(AssertionError.class, () -> new Carta("Block", null));
  }

  @Test
  void testPrecondicionesSettersInvalidos() {
    Carta c = new Carta(5, "Rojo");
    assertThrows(AssertionError.class, () -> c.setValor(99));
    assertThrows(AssertionError.class, () -> c.setColor(null));
    assertThrows(AssertionError.class, () -> c.setEfecto("XXX"));
  }

  // ----------------------------------------------------------
  // --------------------- INVARIANTES ------------------------
  // ----------------------------------------------------------
  @Test
  void testInvarianteDetectaValorMenorQueMenosUno() throws Exception {
    Carta c = new Carta(5, "Rojo");
    Field f = Carta.class.getDeclaredField("valor");
    f.setAccessible(true);
    f.setInt(c, -5); // dejar valor inválido
    // invocamos Invariante() por reflexión para ejecutar los asserts internos
    Method m = Carta.class.getDeclaredMethod("Invariante");
    m.setAccessible(true);
    assertThrows(AssertionError.class, () -> {
      try {
        m.invoke(c);
      } catch (Throwable t) {
        // unwrap InvocationTargetException
        throw t.getCause();
      }
    });
  }

  @Test
  void testInvarianteDetectaValorMayorOIgual10() throws Exception {
    Carta c = new Carta(5, "Rojo");
    Field f = Carta.class.getDeclaredField("valor");
    f.setAccessible(true);
    f.setInt(c, 10); // valor inválido >=10
    Method m = Carta.class.getDeclaredMethod("Invariante");
    m.setAccessible(true);
    assertThrows(AssertionError.class, () -> {
      try {
        m.invoke(c);
      } catch (Throwable t) {
        throw t.getCause();
      }
    });
  }

  @Test
  void testInvarianteDetectaColorYEfectoNull() throws Exception {

    // Para causar color==null y efecto==null, usamos reflexión
    Carta c = new Carta(5, "Rojo");
    Field fColor = Carta.class.getDeclaredField("color");
    Field fEfecto = Carta.class.getDeclaredField("efecto");
    fColor.setAccessible(true);
    fEfecto.setAccessible(true);
    fColor.set(c, null);
    fEfecto.set(c, null);
    Method m = Carta.class.getDeclaredMethod("Invariante");
    m.setAccessible(true);
    assertThrows(AssertionError.class, () -> {
      try {
        m.invoke(c);
      } catch (Throwable t) {
        throw t.getCause();
      }
    });
  }

  @Test
  void testInvarianteCartaNumericaNoPuedeTenerEfecto() throws Exception {
    Carta c = new Carta(5, "Rojo");
    Field f = Carta.class.getDeclaredField("efecto");
    f.setAccessible(true);
    f.set(c, "+4"); // ahora numérica con efecto
    Method m = Carta.class.getDeclaredMethod("Invariante");
    m.setAccessible(true);
    assertThrows(AssertionError.class, () -> {
      try {
        m.invoke(c);
      } catch (Throwable t) {
        throw t.getCause();
      }
    });
  }

  @Test
  void testInvarianteCartaEspecialValorDebeSerMenosUno() throws Exception {
    Carta c = new Carta("Block", "Rojo");
    Field f = Carta.class.getDeclaredField("valor");
    f.setAccessible(true);
    f.setInt(c, 3); // carta especial con valor inválido
    Method m = Carta.class.getDeclaredMethod("Invariante");
    m.setAccessible(true);
    assertThrows(AssertionError.class, () -> {
      try {
        m.invoke(c);
      } catch (Throwable t) {
        throw t.getCause();
      }
    });
  }
  
@Test
void testInvarianteColorNullPeroEfectoNoNull() throws Exception {
   // cubre la rama color == null && efecto != null de la invariante
   Carta c = new Carta("Block", "Rojo");

   Field fColor = Carta.class.getDeclaredField("color");
   fColor.setAccessible(true);
   fColor.set(c, null);  // valor permitido mientras efecto!=null

   // llamar a Invariante() — NO debe fallar
   Method m = Carta.class.getDeclaredMethod("Invariante");
   m.setAccessible(true);

   assertDoesNotThrow(() -> {
       try {
           m.invoke(c);
       } catch (Throwable t) {
           throw t.getCause();
       }
   });
}

@Test
void testInvarianteRamaValorMenosUno() throws Exception {
   // cubre el camino valor == -1 que evita entrar al if(valor != -1)
   Carta c = new Carta("Reverse", "Azul"); // valor = -1

   Method m = Carta.class.getDeclaredMethod("Invariante");
   m.setAccessible(true);

   assertDoesNotThrow(() -> {
       try {
           m.invoke(c);
       } catch (Throwable t) {
           throw t.getCause();
       }
   });
}

//Cubre la rama buena de postcondición del constructor numérico
@Test
void testPostCondicionesConstructorNumericoCorrecto() {
   Carta c = new Carta(4, "Verde");

   assertEquals(4, c.getValor());
   assertEquals("Verde", c.getColor());
   assertNull(c.getEfecto());
}

//Cubre la rama buena del constructor especial
@Test
void testPostCondicionesConstructorEspecialCorrecto() {
   Carta c = new Carta("+2", "Amarillo");

   assertEquals("+2", c.getEfecto());
   assertEquals("Amarillo", c.getColor());
   assertEquals(-1, c.getValor());
}

  @Test
  void testPostCondicionConstructorNumericoInterna() {
    Carta c = new Carta(8, "Azul");

    // fuerza a pasar por getValor(), getColor(), getEfecto() igual que hace el constructor
    assertEquals(8, c.getValor());
    assertEquals("Azul", c.getColor());
    assertNull(c.getEfecto());
  }

  @Test
  void testPostCondicionConstructorEspecialInterna() {
    Carta c = new Carta("Block", "Rojo");

    assertEquals("Block", c.getEfecto());
    assertEquals("Rojo", c.getColor());
    assertEquals(-1, c.getValor());
  }

  @Test
  void testPostCondicionSetValorInterna() {
    Carta c = new Carta(3, "Verde");

    c.setValor(7); // debe activar la postcondición interna

    assertEquals(7, c.getValor());
  }

  @Test
  void testPostCondicionSetColorInterna() {
    Carta c = new Carta(3, "Verde");

    c.setColor("Azul");

    assertEquals("Azul", c.getColor());
  }

  @Test
  void testPostCondicionSetEfectoInterna() {
    Carta c = new Carta("Reverse", "Rojo");

    c.setEfecto("+4");

    assertEquals("+4", c.getEfecto());
  }

  @Test
  void testPostCondicionMismoColorInterna() {

    Carta a = new Carta(5, "Rojo");
    Carta b = new Carta(7, "Rojo");

    boolean r = a.mismoColor(b);

    assertTrue(r);
  }

  @Test
  void testPostCondicionMismoValorInterna() {

    Carta a = new Carta(5, "Rojo");
    Carta b = new Carta(5, "Verde");

    boolean r = a.mismoValor(b);

    assertTrue(r);
  }

// ---------------------------------------------------------
// COBERTURA TOTAL DE PRECONDICIONES (asserts que deben fallar)
// ---------------------------------------------------------

  @Test
  void testPrecondicionConstructorNumericoValorNegativo() {
    assertThrows(AssertionError.class, () -> new Carta(-2, "Rojo"));
  }

  @Test
  void testPrecondicionConstructorNumericoValorMayor9() {
    assertThrows(AssertionError.class, () -> new Carta(12, "Rojo"));
  }

  @Test
  void testPrecondicionConstructorNumericoColorNull() {
    assertThrows(AssertionError.class, () -> new Carta(5, null));
  }

  @Test
  void testPrecondicionConstructorEspecialEfectoInvalido() {
    assertThrows(AssertionError.class, () -> new Carta("XXX", "Rojo"));
  }

  @Test
  void testPrecondicionConstructorEspecialColorNull() {
    assertThrows(AssertionError.class, () -> new Carta("Block", null));
  }

  @Test
  void testPrecondicionSetValorValorInvalido() {
    Carta c = new Carta(5, "Rojo");
    assertThrows(AssertionError.class, () -> c.setValor(99));
  }

  @Test
  void testPrecondicionSetColorNull() {
    Carta c = new Carta(5, "Rojo");
    assertThrows(AssertionError.class, () -> c.setColor(null));
  }

  @Test
  void testPrecondicionSetEfectoInvalido() {
    Carta c = new Carta("Block", "Rojo");
    assertThrows(AssertionError.class, () -> c.setEfecto("XXX"));
  }

  @Test
  void testPrecondicionMismoColorCartaNull() {
    Carta c = new Carta(3, "Rojo");
    assertThrows(AssertionError.class, () -> c.mismoColor(null));
  }

  @Test
  void testPrecondicionMismoColorCartaColorNull() throws Exception {
    Carta a = new Carta(3, "Rojo");
    Carta b = new Carta(5, "Verde");

    // rompemos el color por reflexión para activar el assert interno
    Field f = Carta.class.getDeclaredField("color");
    f.setAccessible(true);
    f.set(b, null);

    assertThrows(AssertionError.class, () -> a.mismoColor(b));
  }

  @Test
  void testPrecondicionMismoValorCartaNull() {
    Carta a = new Carta(5, "Rojo");
    assertThrows(AssertionError.class, () -> a.mismoValor(null));
  }

}
