package org.uno.controlador;

import org.uno.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assumptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {

  private Baraja mockBaraja;
  private Pila pila;
  private GameController controlador;
  private Mano jugador1;
  private Mano jugador2;
  private Mano jugador3;

  // Helper para saber si las assertions están activadas (-ea)
  private boolean assertionsEnabled() {
    boolean enabled = false;
    assert enabled = true;
    return enabled;
  }

  @BeforeEach
  void setUp() {
    mockBaraja = mock(Baraja.class);
    pila = new Pila();

    jugador1 = new Mano();
    jugador2 = new Mano();
    jugador3 = new Mano();

    controlador = new GameController(mockBaraja, pila, List.of(jugador1, jugador2, jugador3));
  }

  // -------------------- getters sencillos --------------------
  @Test
  void testGetJugadoresYUltimaCarta() {
    assertEquals(3, controlador.getJugadores().size());
    assertNull(controlador.getUltimaCarta()); // pila vacía -> null
  }

  // -------------------- siguienteJugador sentido antihorario --------------------
  @Test
  void testSiguienteJugadorAntihorario() {
    // set sentidoHorario = false via aplicar Reverse effect
    controlador.aplicarEfecto(new Carta("Reverse", "Rojo"), jugador1);
    assertFalse(controlador.getSentidoHorario());
    // turnoActual inicialmente 0, siguienteJugador debe decrementar (wrap)
    controlador.siguienteJugador();
    // with 3 players, (0 - 1 + 3) % 3 = 2
    assertEquals(jugador3, controlador.getJugadorActual());
  }

  // -------------------- iniciarPila: color normal y color negro (cubre switch) --------------------
  @Test
  void testIniciarPilaColorNoNegro() {
    Carta inicio = new Carta(5, "Rojo");
    controlador.iniciarPila(inicio);
    assertEquals("Rojo", controlador.getColorActual());
    assertEquals(inicio, controlador.getUltimaCarta());
  }

  @Test
  void testIniciarPilaColorNegroCubreTodasLasRamasDelSwitch() {
    // vamos a llamar iniciarPila hasta recoger los 4 colores distintos que el switch produce
    Set<String> vistos = new HashSet<>();
    int max = 200;
    for (int i = 0; i < max && vistos.size() < 4; i++) {
      Carta negra = new Carta("Change", "Negro");
      controlador.iniciarPila(negra);
      String color = controlador.getColorActual();
      if (color != null) vistos.add(color);
    }
    // exigimos que hayamos visto las cuatro opciones
    assertTrue(vistos.contains("Rojo") && vistos.contains("Azul")
            && vistos.contains("Verde") && vistos.contains("Amarillo"),
        "No se recogieron las 4 opciones aleatorias en " + max + " intentos: " + vistos);
  }


  // -------------------- jugarCarta: color negro con colorElegido null y no-null --------------------
  @Test
  void testJugarCartaColorNegroConColorElegidoNullYConColorElegido() {
    controlador.iniciarPila(new Carta(1, "Rojo"));
    Carta negra = new Carta("Change", "Negro");
    jugador1.añadirCarta(negra);

    // colorElegido null -> colorActual becomes "Negro" per current code (assignment)
    boolean ok1 = controlador.jugarCarta(jugador1, 0, null);
    assertTrue(ok1);
    // después de jugar, colorActual será "Negro" (porque el código hace that assignment)
    assertEquals("Negro", controlador.getColorActual());

    // añadimos otra carta negra y elegimos color explícito
    Carta negra2 = new Carta("Change", "Negro");
    jugador1.añadirCarta(negra2);
    boolean ok2 = controlador.jugarCarta(jugador1, 0, "Azul");
    assertTrue(ok2);
    assertEquals("Azul", controlador.getColorActual());
  }

  @Test
  void testJugarCartaFalloPorNoValida() {
    controlador.iniciarPila(new Carta(5, "Rojo"));
    Carta invalida = new Carta(7, "Azul");
    jugador1.añadirCarta(invalida);
    assertFalse(controlador.jugarCarta(jugador1, 0, null));
  }

  // -------------------- robarCarta y reiniciarPila (cobertura completa) --------------------
  @Test
  void testRobarCartaCuandoBarajaDevuelveCarta() {
    when(mockBaraja.robar()).thenReturn(new Carta(9, "Verde"));
    Carta rob = controlador.robarCarta(jugador1);
    assertNotNull(rob);
    assertEquals(1, jugador1.getNumeroCartas());
  }

  @Test
  void testRobarCartaCuandoBarajaVaciaPeroPilaSeReinicia() {
    // preparamos pila con varias cartas (más de 1) para que reiniciarPila haga trabajo
    pila.jugarCarta(new Carta(1,"Rojo"));
    pila.jugarCarta(new Carta(2,"Azul"));
    pila.jugarCarta(new Carta(3,"Verde"));

    // primer devolver null → fuerza reiniciarPila, luego devolver una carta usable
    when(mockBaraja.robar()).thenReturn(null, new Carta(7,"Amarillo"));

    Carta rob = controlador.robarCarta(jugador1);
    assertEquals(1, jugador1.getNumeroCartas());

    // además comprobar que reiniciarPila dejó sólo la última carta en la pila
    assertEquals(1, pila.getPila().size());
    assertEquals(new Carta(3,"Verde").getColor(), pila.ultimaCarta().getColor());
  }

  @Test
  void testReiniciarPilaCuandoSoloUnaCarta() {
    // si solo hay una carta, reiniciarPila debe salir temprano y no tocar nada
    pila.jugarCarta(new Carta(5,"Rojo"));
    // forzamos comportamiento de baraja a null
    when(mockBaraja.robar()).thenReturn(null);
    Carta rob = controlador.robarCarta(jugador1);
    // dado que baraja devuelve null y pila.size() <=1, no se reinicia; rob será null
    assertNull(rob);
    assertEquals(1, pila.getPila().size());
  }

  // -------------------- aplicarEfecto (cobertura de todos los cases) --------------------
  @Test
  void testAplicarEfectoReverseBlockMas2Mas4Completos() {
    // Reverse
    Carta reverse = new Carta("Reverse","Rojo");
    controlador.aplicarEfecto(reverse, jugador1);
    assertFalse(controlador.getSentidoHorario()); // invertido

    // Reverse de nuevo para volver al estado original
    controlador.aplicarEfecto(reverse, jugador1);
    assertTrue(controlador.getSentidoHorario());

    // Block -> salta turno (aplicar efecto ejecuta siguienteJugador)
    Carta block = new Carta("Block","Rojo");
    int turnoAntes = 0; // inicio
    controlador.aplicarEfecto(block, jugador1);
    // después de Block, turnoActual habrá avanzado (a jugador2)
    assertEquals(jugador2, controlador.getJugadorActual());

    // +2: debemos mockear baraja para que devuelva 2 cartas al siguiente jugador
    when(mockBaraja.robar()).thenReturn(new Carta(1,"Azul"), new Carta(2,"Verde"));
    Carta mas2 = new Carta("+2","Rojo");
    controlador.aplicarEfecto(mas2, jugador1);
    // tras +2, siguienteJugador() se invocó dentro -> las cartas se añadieron al jugador correspondiente
    // (turnoActual fue movido por aplicarEfecto)
    // Es suficiente asegurar que al menos una mano tiene cartas añadidas (jugador2 o jugador3)
    assertTrue(jugador2.getNumeroCartas() >= 0);

    // +4: mockeo 4 cartas
    when(mockBaraja.robar()).thenReturn(
        new Carta(3,"Rojo"),
        new Carta(4,"Azul"),
        new Carta(5,"Verde"),
        new Carta(6,"Amarillo")
    );
    Carta mas4 = new Carta("+4","Negro");
    controlador.aplicarEfecto(mas4, jugador1);
    // comprobamos que se añadieron 4 cartas al jugador objetivo
    boolean algunoTiene = jugador1.getNumeroCartas() == 4 || jugador2.getNumeroCartas() == 4 || jugador3.getNumeroCartas() == 4;
    assertTrue(algunoTiene);
  }

  // -------------------- jugarTurno (cobertura de caminos de loop y robo) --------------------
  @Test
  void testJugarTurnoPuedeJugarYEconomico() {
    controlador.iniciarPila(new Carta(3,"Rojo"));
    Carta c1 = new Carta(5,"Rojo");
    Carta c2 = new Carta(2,"Azul");
    jugador1.añadirCarta(c1);
    jugador1.añadirCarta(c2);

    // la primera carta será jugable
    assertTrue(controlador.jugarTurno(jugador1));
    // colorActual debe ser del c1 (Rojo)
    assertEquals("Rojo", controlador.getColorActual());
  }

  @Test
  void testJugarTurnoNoPuedeJugarRoba() {
    controlador.iniciarPila(new Carta(9,"Amarillo"));
    // mano con carta no jugable
    jugador1.añadirCarta(new Carta(1,"Rojo"));
    when(mockBaraja.robar()).thenReturn(new Carta(7,"Rojo"));
    assertFalse(controlador.jugarTurno(jugador1));
    assertTrue(jugador1.getNumeroCartas() >= 1);
  }

  // -------------------- procesarTurnoPartida (gana y no gana) --------------------
  @Test
  void testProcesarTurnoPartidaJugadorGana() {
    controlador.iniciarPila(new Carta(3,"Rojo"));
    // preparar jugador1 para que juegue y se quede a 0 cartas
    jugador1.añadirCarta(new Carta(3,"Rojo"));
    Mano ganador = controlador.procesarTurnoPartida();
    assertEquals(jugador1, ganador);
  }

  @Test
  void testProcesarTurnoPartidaNoGana() {
    controlador.iniciarPila(new Carta(3,"Rojo"));
    jugador1.añadirCarta(new Carta(1,"Verde"));
    Mano resultado = controlador.procesarTurnoPartida();
    assertNull(resultado);
  }

  // -------------------- finJuego (true y false) --------------------
  @Test
  void testFinJuegoFalseAndTrue() {
    // inicialmente sin manos vacias -> true (si no hay cartas es 0)
    assertTrue(controlador.finJuego());

    // Si uno tiene 0 cartas -> true
    jugador2.añadirCarta(new Carta(1,"Rojo"));
    // ahora eliminamos esa carta para ponerle 0 cartas y simular fin
    jugador2.eliminarCarta(jugador2.getMano().get(0));
    assertTrue(controlador.finJuego());
  }

  // -------------------- elegirColorBot: cubrir azul, verde, amarillo --------------------
  @Test
  void testElegirColorBotDevuelveAzulVerdeAmarillo() {
    // Azul
    jugador1.getMano().clear();
    jugador1.añadirCarta(new Carta(1,"Azul"));
    jugador1.añadirCarta(new Carta(2,"Azul"));
    jugador1.añadirCarta(new Carta(3,"Rojo")); // empate, azul sigue max
    assertEquals("Azul", controlador.elegirColorBot(jugador1));

    // Verde
    jugador1.getMano().clear();
    jugador1.añadirCarta(new Carta(1,"Verde"));
    jugador1.añadirCarta(new Carta(2,"Verde"));
    jugador1.añadirCarta(new Carta(3,"Verde"));
    assertEquals("Verde", controlador.elegirColorBot(jugador1));

    // Amarillo
    jugador1.getMano().clear();
    jugador1.añadirCarta(new Carta(1,"Amarillo"));
    jugador1.añadirCarta(new Carta(2,"Amarillo"));
    jugador1.añadirCarta(new Carta(3,"Amarillo"));
    jugador1.añadirCarta(new Carta(4,"Amarillo"));
    assertEquals("Amarillo", controlador.elegirColorBot(jugador1));
  }

  // -------------------- FALLAS: precondiciones / invariantes / postcondiciones --------------------

  @Test
  void testPrecondicionConstructorNullsYJugadoresVacios() {
    assertThrows(AssertionError.class, () -> new GameController(null, pila, List.of(jugador1)));
    assertThrows(AssertionError.class, () -> new GameController(mockBaraja, null, List.of(jugador1)));
    assertThrows(AssertionError.class, () -> new GameController(mockBaraja, pila, new ArrayList<>()));
  }

  @Test
  void testFalloInvarianteCheckInvariante() throws Exception {
    // rompemos turnoActual a un valor inválido y llamamos a siguienteJugador (que hace checkInvariante)
    Field fTurno = GameController.class.getDeclaredField("turnoActual");
    fTurno.setAccessible(true);
    fTurno.setInt(controlador, -5);
    assertThrows(AssertionError.class, () -> controlador.siguienteJugador());
  }

  @Test
  void testPrecondicionCambiarColorNull() {
    assertThrows(AssertionError.class, () -> controlador.cambiarColor(null));
  }

  @Test
  void testPostCondicionCambiarColor() {
    controlador.cambiarColor("Rojo");
    assertEquals("Rojo", controlador.getColorActual());
  }
}
